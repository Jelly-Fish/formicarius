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
 * *****************************************************************************
 */
package fr.com.jellyfish.jfgformicarius.formicarius.entities.events;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.time.StopWatch;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Observer;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Spawnable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.XYObservable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class EvilDarkFog extends AbstractEntity implements Spawnable, Observer {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final float STAMINA_VALUE = 10.0f;
    public static final int SPRT_WH = 128;
    public static final int FRAME_COUNT = 46;
    public static final int SPANW_TIME_LIMIT = 5;
    private int frameVal = 0;
    private boolean spawned = false;
    private final int soundEffectRef;
    private boolean fade = false;

    /**
     * Stop watch instance.
     */
    private StopWatch stopwatch;

    /**
     * XYObservable instance.
     */
    private XYObservable observable;

    /**
     * Static instances counter for reference marking.
     */
    public static int occurs = 0;

    /**
     * frame array.
     */
    private final Sprite[] frames;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     *
     * @param game
     * @param x
     * @param y
     * @param iconPath
     * @param abstractRef
     * @param soundEffectRef
     */
    public EvilDarkFog(final Game game, final int x, final int y,
            final String iconPath, final String abstractRef, final int soundEffectRef) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), iconPath),
                x, y, 0, abstractRef);

        observable = (XYObservable) game.accessGlobalEntities().get(MainCharacter.class.getSimpleName());

        this.spawned = false;
        this.soundEffectRef = soundEffectRef;
        frames = new Sprite[EvilDarkFog.FRAME_COUNT];
        for (int i = 0; i < frames.length; ++i) {
            frames[i] = SpriteUtils.getSprite(game.getTextureLoader(),
                    String.format(StaticSpriteVars.evil_fog2, i));
        }

        ++EvilDarkFog.occurs;
        sprite = frames[EvilDarkFog.FRAME_COUNT - 1];
        setAnimeUpdateRequired(true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public void collideWith(final AbstractEntity other) {
    }

    @Override
    public void doLogic() {
        final int[] masterXY = this.observable.observedXY();
        x = masterXY[0] - ((EvilDarkFog.SPRT_WH - MainCharacter.SPRT_W) / 2);
        y = masterXY[1] - ((EvilDarkFog.SPRT_WH - MainCharacter.SPRT_H) / 2);

        if (this.stopwatch != null && !this.fade && this.stopwatch.hasReachedMaxElapsedMS()) {
            // End effect and then clear.
            this.fade = true;
            frameVal = EvilDarkFog.FRAME_COUNT - 16;
        }
    }

    @Override
    public void beforeRender(final boolean force) {

        if (((gLoopCntr1 >= AnimationConst.FPS / 80) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;

            if (this.fade) {
                if (frameVal == EvilDarkFog.FRAME_COUNT - 1) {
                    clear();
                    return;
                }
                sprite = frames[frameVal];
                ++frameVal;
            } else {
                if (frameVal == 0) {
                    // Frame count - 16 because the sprites above that index are for
                    // fade effect before clearing.
                    frameVal = EvilDarkFog.FRAME_COUNT - 16;
                } else {
                    sprite = frames[frameVal];
                    --frameVal;
                }
            }
        } else {
            gLoopCntr1++;
        }
    }

    @Override
    public void afterRender(final boolean force) {
        DrawingHelper.getInstance().getDrawableQueue().addLast(this);
    }

    @Override
    public float spawn(final int x, final int y) {

        if (!this.spawned) {
            // Update location. Add to game's entity map. Set visible and 
            // candidate for remove from game's Entity main hashmap.
            setAnimeUpdateRequired(true);
            this.spawned = true;
            game.getSoundManager().playEffect(this.soundEffectRef);
            this.x = x - (EvilDarkFog.SPRT_WH / 2);
            this.y = y - (EvilDarkFog.SPRT_WH / 2);
            this.game.accessGlobalEntities().put(this.ABSTRACT_REF, this);
            ++EvilDarkFog.occurs;
            this.stopwatch = new StopWatch(EvilDarkFog.SPANW_TIME_LIMIT);
            notifySpawnEvent();
            return EvilDarkFog.STAMINA_VALUE;
        }
        
        return 0.0f;
    }
    
    @Override
    public void basicSpawn(final int x, final int y) { }

    @Override
    public void notifySpawnEvent() {
    }

    @Override
    public void clear() {
        this.frameVal = EvilDarkFog.FRAME_COUNT - 1;
        this.spawned = false;
        this.fade = false;
        this.game.accessGlobalEntities().remove(this.ABSTRACT_REF);
        setAnimeUpdateRequired(false);
    }

    @Override
    public boolean isSpawned() {
        return this.spawned;
    }

    @Override
    public String getAbstractRef() {
        return this.ABSTRACT_REF;
    }

    @Override
    public void observed() {
    }

    @Override
    public void setObserver(final XYObservable observed) {
        this.observable = observed;
    }

    /**
     * return this current Sprite's rectangle definition.
     * @return Rectangle
     */
    public Rectangle getRectangle() {
        
        return new Rectangle(this.getX() + 8, this.getY() + 8, EvilDarkFog.SPRT_WH - 16,
            EvilDarkFog.SPRT_WH - 16);
    }
    //</editor-fold>

}
