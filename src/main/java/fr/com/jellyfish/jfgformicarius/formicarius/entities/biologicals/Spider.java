/**
 * *****************************************************************************
 * Copyright (c) 2014, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 ******************************************************************************
 */
package fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.effects.blood.BloodImpact;
import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.RandonMvtSimulationException;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.AIMvtHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Interactable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.SpellBoundable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.RandomUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class Spider extends AbstractEntity implements Interactable, CollidableObject,
        SpellBoundable {
    
    //<editor-fold defaultstate="collapsed" desc="variables">
    public final static float SPEED = 190.0f;
    protected float speed = 190.0f;
    public static final float HEALTH_VALUE = 6.0f;
    public static final int SPRT_WH = 40;
    public static final int STILLNESS_FACTOR = 1;
    private static final int FRAME_COUNT = 20;
    private float health;
    public static int occurs = 0;
    private int frameVal = 0;
    private boolean spellbound = false;
    
    /**
     * AI mouvement helper instance.
     */
    private final AIMvtHelper aiMvtHelper = new AIMvtHelper();

    /**
     * The animation frames of this entity.
     */
    private final Sprite[] frames;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     * @param game
     * @param sprtDefault
     * @param x
     * @param y 
     */
    public Spider(final Game game, final String sprtDefault, final int x, final int y) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), String.format(sprtDefault, 0)), x, y, 
            MvtConst.DOWN, Spider.class.getSimpleName() + Spider.occurs);
        
        setCurrentMvt(MvtConst.DOWN);
        this.health = Spider.HEALTH_VALUE;
        
        this.frames = new Sprite[Spider.FRAME_COUNT];
        for (int i = 0; i < frames.length; ++i) {
            this.frames[i] = SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(StaticSpriteVars.spider, i));
        }
        sprite = this.frames[0];
        
        setAnimeUpdateRequired(true);
        ++Spider.occurs;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Request that the AbstractEntity sub class takes move 
     * based on an elapsed ammount of time.
     *
     * @param delta The time that has elapsed since last move (ms)
     */
    @Override
    public void move(final long delta) {
        
        if (this.spellbound) {
            return;
        }
        
        // No return if is in collision. This entity only collides with Ignitables.
        
        // if we're moving left and have reached the left hand side
        // of the screen, don't move.
        if (isAnimeUpdateRequired()) {
            if (((dx < 0) && (x < 0)) || ((dy < 0) && (y < 0))) {
                return;
            }
            // if we're moving right or up and down and have reached the 
            //side of the screen, don't move.
            if (((dx > 0) && (x > FrameConst.FRM_WIDTH_800 - Spider.SPRT_WH))
                    || ((dy > 0) && (y > FrameConst.FRM_HEIGHT_600 - Spider.SPRT_WH))) {
                return;
            }
        
            super.move(delta);
        }
    }
    
    @Override
    public void collideWith(final AbstractEntity other) {
        
        final boolean inCollision = CollisionUtils.inCollision(this.getRectangle(), 
            other, CollidableObject.COLLIDABLE_OBJ_INNER_PADDING_4);
        
        if (inCollision && (other instanceof Ignitable)) {
            
            // Spawn blood then clear Ignitable instance.
            CollisionUtils.appendCollideEffect(new BloodImpact(
                SpriteUtils.getSprite(game.getTextureLoader(), 
                String.format(StaticSpriteVars.blood_impact1, 0))), 
                this, BloodImpact.SPRT_WH, BloodImpact.SPRT_WH);
            final Ignitable ignitable = (Ignitable)other;
            ignitable.clear();
            this.health -= ignitable.getDamageValue();
            this.setInCollision(false);
            
            if (this.health <= 0) { 
                Game.getInstance().getSoundManager().playEffect(StaticSoundVars.scream2);
                this.clear(); 
                return;
            }
            
            if (RandomUtils.randInt(0, 10) > 4) {
                Game.getInstance().getSoundManager().playEffect(StaticSoundVars.scream5);
            }
        } else if (inCollision && (other instanceof MainCharacter)) {
            ((MainCharacter)other).notifyCollision(Spider.HEALTH_VALUE, 10);
        }
    }

    @Override
    public void doLogic() {
        
        if (this.spellbound) {
            return;
        }
        
        // Simulate keyboard input randomly.
        if (gLoopCntr2 >= AnimationConst.FPS / 2) {
            try {                
                final boolean movable = aiMvtHelper.simulateRandonNonDiagonalMvt(this,
                    this.speed, Spider.STILLNESS_FACTOR);
                setAnimeUpdateRequired(movable);
                gLoopCntr2 = 0;
            } catch (final RandonMvtSimulationException ex) { }
        } else {
            gLoopCntr2++;
        }
    }

    @Override
    public void beforeRender(final boolean force) {
        
        if (this.spellbound) {
            return;
        }
        
        if (((gLoopCntr1 >= AnimationConst.FPS / 12) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            switch ((int)getMvt()) {
                case MvtConst.LEFT:
                    frameVal = currentMvt != MvtConst.LEFT || frameVal == 7 ? 4 : frameVal;
                    currentMvt = MvtConst.LEFT;
                    break;
                case MvtConst.UP:
                    frameVal = currentMvt != MvtConst.UP || frameVal == 15 ? 12 : frameVal;
                    currentMvt = MvtConst.UP;
                    break;
                case MvtConst.RIGHT:
                    frameVal = currentMvt != MvtConst.RIGHT || frameVal == 11 ? 8 : frameVal;
                    currentMvt = MvtConst.RIGHT;
                    break;
                case MvtConst.DOWN:
                    frameVal = currentMvt != MvtConst.DOWN || frameVal == 3 ? 0 : frameVal;
                    currentMvt = MvtConst.DOWN;
                    break;
                default:
                    frameVal = 0;
                    currentMvt = MvtConst.DOWN;
                    break;
            }
            sprite = frames[frameVal];
            frameVal++;
        } else {
            gLoopCntr1++;
        }
    }

    @Override
    public void afterRender(final boolean force) { 
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }

    /**
     * Get this ready for another copy to game Map from associated pool.
     */
    @Override
    public void clear() {
        game.getEntityHelper().getInteractableEntities().remove(this.ABSTRACT_REF);
        this.setAnimeUpdateRequired(true);
        this.setInCollision(false);
        this.frameVal = 0;
        this.health = Spider.HEALTH_VALUE;
        this.speed = Spider.SPEED;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getX() + 4, this.getY() + 4, sprite.getWidth() - 4, 
            sprite.getHeight() - 4);
    }

    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        return null;
    }
    
    @Override
    public SpellBoundable spellbound() {
        this.spellbound = true;
        // Set texture/sprite effects. getMvt() will return left or right.
        // 1 for left and/or 3 for right.
        sprite = this.frames[this.frames.length - 5 + this.getMvt()];
        return this;
    }

    @Override
    public void removeSpellbound() {
        this.spellbound = false;
    }

    @Override
    public SpellBoundable spellboundFreeze() {
        this.speed = 0.0f;
        return this;
    }
    //</editor-fold>
    
}
