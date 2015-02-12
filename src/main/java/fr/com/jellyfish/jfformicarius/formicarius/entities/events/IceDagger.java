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
package fr.com.jellyfish.jfformicarius.formicarius.entities.events;

import fr.com.jellyfish.jfformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfformicarius.formicarius.helpers.time.StopWatch;
import fr.com.jellyfish.jfformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfformicarius.formicarius.utils.SpriteUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thw
 */
public class IceDagger extends AbstractEntity implements Ignitable {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final float DAMAGE_VALUE = 1.5f;
    private String mapRef = null;
    public static final int SPRT_W = 34;
    public static final int SPRT_H = 13;
    public static final float DAGGER_SPEED = 420;
    public static final int FRAME_COUNT = 4;
    public static Integer occurs = 0;
    private boolean ignited = false;
    public static final double SPAWN_DELAY = 0.8;

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
     * constructor.
     * @param game
     * @param x
     * @param y
     * @param direction
     * @param ref
     */
    public IceDagger(Game game, int x, int y, int direction, String ref) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), StaticSpriteVars.ice_dagger_icon), 
            x, y, direction, ref);

        stopwatch = new StopWatch(IceDagger.SPAWN_DELAY);
        
        final List<String> sprtRefs = new ArrayList<>();
        for (int i = 1; i <= 4; ++i) {
            sprtRefs.add(String.format(StaticSpriteVars.ice_dagger, i));
        }

        frames = new Sprite[sprtRefs.size()];
        for (int i = 0; i < frames.length; ++i) {
            frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), sprtRefs.get(i));
        }
        sprite = frames[0];

        initMvt(direction);
        setAnimeUpdateRequired(false);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Initialize mouvement rules for this.move() override.
     *
     * @param mvt
     */
    @Override
    public final void initMvt(final int mvt) {
        
        this.setMvt(mvt);
        
        if (mvt == MvtConst.DOWN) {
            dy = DAGGER_SPEED; dx = 0;
        } else if (mvt == MvtConst.UP) {
            dy = -DAGGER_SPEED; dx = 0;
        } else if (mvt == MvtConst.RIGHT) {
            dx = DAGGER_SPEED; dy = 0;
        } else if (mvt == MvtConst.LEFT) {
            dx = -DAGGER_SPEED; dy = 0;
        }
    }
    
    @Override
    public void clear() {
        // Remove from game AbstractEntity map.
        if (game.accessGlobalEntities().containsKey(this.mapRef)) {
            game.accessGlobalEntities().remove(this.mapRef);
        }
        this.ignited = false;
        sprite = frames[0];
        setAnimeUpdateRequired(false);
        this.x = -IceDagger.SPRT_W;
        this.y = -IceDagger.SPRT_H;
    }
    
    /**
     * Spawn after updating it's coordinates relative to AbstractEntity subclass.
     */
    @Override
    public void ignite(final int x, final int y, final int srcMvt) {
        
        // Update location. Add to game's entity map. Set visible and 
        // candidate for remove from game's Entity main hashmap.
        if (!this.ignited && this.stopwatch.hasReachedMaxElapsedMS()) {
            
            initMvt(srcMvt);
            stopwatch.start();
            this.x = x;
            this.y = y + 16;
            this.ignited = true;
            sprite = frames[srcMvt - 1];
            this.mapRef = this.ABSTRACT_REF + occurs;
            ++occurs;
            game.getSoundManager().playEffect(StaticSoundVars.bloody_dagger);
            setAnimeUpdateRequired(true);
            setInCollision(false);
            this.game.accessGlobalEntities().put(this.mapRef, this);
        }
    }

    @Override
    public void move(final long delta) {

        if (this.isInCollision()) {
            this.clear();
            return;
        }
        
        if (((dx < 0 - SPRT_W) && (x < 0 - SPRT_W)) || ((dy < 0 - SPRT_H) && (y < 0 - SPRT_H))) {
            clear();
            return;
        }

        if (((dx > 0 + SPRT_W) && (x > FrameConst.FRM_WIDTH_800 + SPRT_W))
                || ((dy > 0 + SPRT_H) && (y > FrameConst.FRM_HEIGHT_600 + SPRT_H))) {
            clear();
            return;
        }

        super.move(delta);
    }

    @Override
    public void collideWith(AbstractEntity other) { }

    @Override
    public void doLogic() { }

    @Override
    public void beforeRender(boolean force) { }

    @Override
    public void afterRender(boolean force) { 
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters & setters">
    @Override
    public boolean isIgnited() {
        return this.ignited;
    }
    
    @Override
    public float getDamageValue() {
        return IceDagger.DAMAGE_VALUE;
    }

    @Override
    public String getAbstractRef() {
        return this.ABSTRACT_REF;
    }
    //</editor-fold>
    
}
