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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.events;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.characters.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.effects.blood.BloodImpact;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.time.StopWatch;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class Axe extends AbstractEntity implements Ignitable, CollidableObject {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final int SPRT_WH = 26;
    public static final float AXE_SPEED = 200;
    public static final float DAMAGE_VALUE = 3.4f;
    public static final int FRAME_COUNT = 4;
    public static Integer occurs = 0;
    private boolean ignited = false;
    public static final double SPAWN_DELAY = 1.2;
    private int frameVal = 0;
    
    /**
     * Stop watch instance.
     */
    private final StopWatch stopwatch;
    
    /**
     * The animation frames
     */
    private final Sprite[] frames;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     * @param game
     * @param x
     * @param y
     * @param sprtRef
     * @param direction
     * @param ref
     */
    public Axe(final Game game, final int x, final int y, final String sprtRef,
        final int direction, final String ref) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(sprtRef, 0)), x, y, direction, ref + Axe.occurs);
        
        stopwatch = new StopWatch(Axe.SPAWN_DELAY);
        
        frames = new Sprite[Axe.FRAME_COUNT];
        for (int i = 0; i < frames.length; ++i) {
            frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), String.format(sprtRef, i));
        }
        sprite = frames[frameVal];

        initMvt(direction);
        setAnimeUpdateRequired(false);
        ++Axe.occurs;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public void move(final long delta) {
             
        if (this.isInCollision()) {
            this.clear();
            return;
        }
                
        if (((dx < 0 - Axe.SPRT_WH) && (x < 0 - Axe.SPRT_WH)) || 
                ((dy < 0 - Axe.SPRT_WH) && (y < 0 - Axe.SPRT_WH))) {
            clear();
            return;
        }

        if (((dx > 0 + Axe.SPRT_WH) && (x > FrameConst.FRM_WIDTH + Axe.SPRT_WH))
                || ((dy > 0 + Axe.SPRT_WH) && (y > FrameConst.FRM_HEIGHT + Axe.SPRT_WH))) {
            clear();
            return;
        }

        super.move(delta);
    }
    
    @Override
    public void collideWith(final AbstractEntity other) { 

        if (other instanceof MainCharacter && !((MainCharacter) other).getIgnitable().equals(this)) {
            
            if (this.getRectangle().intersects(((MainCharacter) other).getRectangle())) {
                // Spawn blood then clear Ignitable instance.
                CollisionUtils.appendCollideEffect(new BloodImpact(
                    SpriteUtils.getSprite(game.getTextureLoader(), 
                    String.format(StaticSpriteVars.blood_impact1, 0))), 
                    this, BloodImpact.SPRT_WH, BloodImpact.SPRT_WH);
                ((MainCharacter)other).notifyCollision(Axe.DAMAGE_VALUE, 24);
                this.clear();
            }
        }
    }

    @Override
    public void doLogic() { }

    @Override
    public void beforeRender(final boolean force) { 
        
        if (((gLoopCntr1 >= AnimationConst.FPS / 12) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            frameVal = frameVal == 3 ? 0 : frameVal;
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

    @Override
    public boolean isIgnited() { 
        return this.ignited;
    }

    @Override
    public void ignite(final int x, final int y, final int srcMvt) { 
    
        // Update location. Add to game's entity map. Set visible and 
        // candidate for remove from game's Entity main hashmap.
        if (!this.ignited && this.stopwatch.hasReachedMaxElapsedMS()) {

            initMvt(srcMvt);
            stopwatch.start();
            this.x = x + 3;
            this.y = y + 12;
            this.ignited = true;
            sprite = frames[0];
            ++occurs;
            game.getSoundManager().playEffect(StaticSoundVars.golbez_axe);
            setAnimeUpdateRequired(true);
            setInCollision(false);
            this.game.getEntityHelper().getInteractableEntities().put(this.ABSTRACT_REF, this);
        }
    }

    @Override
    public void clear() {
        
        // Remove from game AbstractEntity map.
        if (game.getEntityHelper().getInteractableEntities().containsKey(this.ABSTRACT_REF)) {
            game.getEntityHelper().getInteractableEntities().remove(this.ABSTRACT_REF);
        }
        this.ignited = false;
        sprite = frames[0];
        setAnimeUpdateRequired(false);
        setInCollision(false);
        this.x = -Axe.SPRT_WH;
        this.y = -Axe.SPRT_WH;
    }

    @Override
    public void initMvt(final int mvt) {
        
        this.setMvt(mvt);
        
        if (mvt == MvtConst.DOWN) {
            dy = AXE_SPEED; dx = 0;
        } else if (mvt == MvtConst.UP) {
            dy = -AXE_SPEED; dx = 0;
        } else if (mvt == MvtConst.RIGHT) {
            dx = AXE_SPEED; dy = 0;
        } else if (mvt == MvtConst.LEFT) {
            dx = -AXE_SPEED; dy = 0;
        }
    }

    @Override
    public float getDamageValue() { 
        return Axe.DAMAGE_VALUE;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle((int)x, (int)y, Axe.SPRT_WH, Axe.SPRT_WH);
    }
    
    @Override
    public Rectangle getRectangle(final AbstractEntity entity) { 
        return null;
    }
    //</editor-fold>
    
}
