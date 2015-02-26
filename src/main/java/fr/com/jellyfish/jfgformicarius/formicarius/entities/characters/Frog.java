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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.characters;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.events.PlasmaBall;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.effects.blood.BloodImpact;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Interactable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Observer;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.XYObservable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.AIMvtUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author thw
 */
public class Frog extends AbstractEntity implements Observer, CollidableObject, Interactable {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final float SPEED = 220;
    public static final String REF = "frog";
    public static final String RED = "red";
    public static final float HEALTH_VALUE = 3.0f;
    public final String color;
    public static final int SPRT_WH = 32;
    public static int occurs = 0;
    public static final int STILLNESS_FACTOR = 2;
    private int frameVal = 0;
    private boolean movable = false;
    private float health;
    
    /**
     * Observable entities.
     */
    public final Map<String, XYObservable> observables;

    /**
     * The animation frames of this entity for left mouvement.
     */
    private final Sprite[] framesMvtLeft;
    
    /**
     * The animation frames of this entity for right mouvement.
     */
    private final Sprite[] framesMvtRight;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    public Frog(final Game game, final String sprtDefault, final int x, final int y, 
        final String color, final int direction) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), sprtDefault), x, y, 
            direction, Frog.class.getSimpleName() + color + Frog.occurs);
        
        setCurrentMvt(direction);
        
        this.color = color;
        this.health = Frog.HEALTH_VALUE;
        this.observables = new HashMap<>();
        this.framesMvtLeft = new Sprite[8];
        this.framesMvtRight = new Sprite[8];
        
        for (int i = 0; i < this.framesMvtLeft.length; ++i) {
            this.framesMvtLeft[i] = SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(StaticSpriteVars.red_frog_left, i));
        }
        for (int i = 0; i < this.framesMvtRight.length; ++i) {
            this.framesMvtRight[i] = SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(StaticSpriteVars.red_frog_right, i));
        }
        
        sprite = this.framesMvtLeft[0];
        
        setAnimeUpdateRequired(true);
        ++Frog.occurs;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * 
     * @param delta 
     */
    @Override
    public void move(final long delta) {
        
        if (this.isInCollision()) {
            return;
        }
        
        // if we're moving left and have reached the left hand side
        // of the screen, don't move and swap sides.
        if (isAnimeUpdateRequired()) {
            
            if (x < -Frog.SPRT_WH) {
                dx = FrameConst.FRM_WIDTH;
                x = FrameConst.FRM_WIDTH;
                return;
            }
            if (y < -Frog.SPRT_WH) {
                dy = FrameConst.FRM_HEIGHT;
                y = FrameConst.FRM_HEIGHT;
                return;
            }
            // if we're moving right or up and down and have reached the 
            //side of the screen, don't move.
            if (x > FrameConst.FRM_WIDTH) {
                dx = -Frog.SPRT_WH;
                x = -Frog.SPRT_WH;
                return;
            }
            if (y > FrameConst.FRM_HEIGHT) {
                dy = -Frog.SPRT_WH;
                y = -Frog.SPRT_WH;
                return;
            }
            
            if (movable) {
                // TODO : check, improve.
                if (!AIMvtUtils.assertRandEquals(3, 2)) {
                    if (this.getMvt().equals(MvtConst.LEFT)) {
                        this.setHorizontalMovement(-Frog.SPEED);
                    } else if (this.getMvt().equals(MvtConst.RIGHT)) {
                        this.setHorizontalMovement(+Frog.SPEED);
                    }
                    this.setVerticalMovement(0);
                    super.move(delta);
                }
                movable = false;
            }
        }
    }
    
    @Override
    public void collideWith(final AbstractEntity other) {
        
        final boolean inCollision = CollisionUtils.inCollision(this.getRectangle(), 
            other, CollidableObject.COLLIDABLE_OBJ_INNER_PADDING_4);
        
        if (inCollision && (other instanceof Ignitable) && !(other instanceof PlasmaBall)) {
            
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
                this.clear(); 
            }
        }
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
        this.movable = true;
        this.health = Frog.HEALTH_VALUE;
    }

    @Override
    public void beforeRender(final boolean force) {

        if (((gLoopCntr1 >= AnimationConst.FPS / 12) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            frameVal++;
            movable = frameVal > 0 && frameVal < 4;
            if (frameVal >= 8) { frameVal = 0; }
            if (this.getMvt().equals(MvtConst.LEFT)) {
                sprite = framesMvtLeft[frameVal];
            } else if (this.getMvt().equals(MvtConst.RIGHT)) {
                sprite = framesMvtRight[frameVal];
            }
        } else {
            gLoopCntr1++;
        }
    }

    @Override
    public void afterRender(final boolean force) { 
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }

    @Override
    public void observed() { }
    
    @Override
    public void doLogic() { }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getX() + 8, this.getY() + 8, sprite.getWidth() - 8, 
            sprite.getHeight() - 8);
    }
    
    @Override
    public void setObserver(final XYObservable observed) { }
    
    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        return null;
    }
    
    public String getABSTRACTRef() {
        return this.ABSTRACT_REF;
    }
    //</editor-fold>
    
}
