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
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Observer;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Spawnable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.SpellBoundable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.XYObservable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class EvilFog extends AbstractEntity implements Spawnable, Observer, CollidableObject {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public float staminaValue = 5.0f;
    public static final int SPRT_WH = 128;
    public static final int FRAME_COUNT = 46;
    private Integer drawRate = 60;
    private int frameVal = 0;
    private boolean spawned = false;
    protected final int soundEffectRef;
    private boolean observed = false;
    
    /**
     * 
     */
    private XYObservable observable = null;
    
    /**
     * Static instances counter for reference marking.
     */
    public static int occurs = 0;
    
    /**
     * frame array.
     */
    protected Sprite[] frames;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     * @param game
     * @param x
     * @param y
     * @param iconPath
     * @param abstractRef
     * @param soundEffectRef 
     */
    public EvilFog(final Game game, final int x, final int y, final String iconPath, 
        final String abstractRef, final int soundEffectRef) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), iconPath),
            x, y, 0, abstractRef);
        
        this.observable = (XYObservable)game.accessGlobalEntities().get(MainCharacter.class.getSimpleName());
        
        this.spawned = false;
        this.soundEffectRef = soundEffectRef;
        frames = new Sprite[EvilFog.FRAME_COUNT];
        for (int i = 0; i < frames.length; ++i) {
            frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), String.format(StaticSpriteVars.evil_fog1, i));
        }

        ++EvilFog.occurs;
        sprite = frames[frameVal];
        setAnimeUpdateRequired(true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public void collideWith(final AbstractEntity other) { 
        
        if (this.spawned && other instanceof SpellBoundable) {
            if (CollisionUtils.inFullCollision(this.getRectangle(), new Rectangle(
                    other.getX(), other.getY(), other.sprite.getWidth(),
                    other.sprite.getHeight()))) {
                ((SpellBoundable) other).spellboundFreeze();
            }
        }
    }

    @Override
    public void doLogic() { }

    @Override
    public void beforeRender(final boolean force) {
        
        if (((gLoopCntr1 >= AnimationConst.FPS / this.drawRate) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            if (frameVal >= EvilFog.FRAME_COUNT - 1) {
                this.spawned = false;
                frameVal = 0;
                this.game.accessGlobalEntities().remove(EvilFog.class.getSimpleName());
                sprite = frames[frameVal];
                sprite.draw((int)x, (int)y);
                observed = false;
                setAnimeUpdateRequired(false);
            } else {
                sprite = frames[frameVal];
                ++frameVal;
                if (frameVal >= EvilFog.FRAME_COUNT / 4 && !observed) { 
                    this.observed();
                    observed = true;
                }
            }
        } else {
            gLoopCntr1++;
        }
    }
    
    @Override
    public void observed() { }

    @Override
    public void afterRender(final boolean force) { 
        
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }
    
    public Integer getDrawRate() {
        return drawRate;
    }

    public void setDrawRate(final Integer drawRate) {
        this.drawRate = drawRate;
    }

    @Override
    public float spawn(final int x, final int y) {
        
        if (!this.spawned) {
            // Update location. Add to game's entity map. Set visible and 
            // candidate for remove from game's Entity main hashmap.
            setAnimeUpdateRequired(true);
            this.spawned = true;
            game.getSoundManager().playEffect(this.soundEffectRef);
            this.x = x - (EvilFog.SPRT_WH / 2);
            this.y = y - (EvilFog.SPRT_WH / 2);
            this.game.accessGlobalEntities().put(EvilFog.class.getSimpleName(), this);
            ++EvilFog.occurs;
            notifySpawnEvent();
            return this.staminaValue;
        }
        
        return 0.0f;
    }
        
    @Override
    public void notifySpawnEvent() { }

    @Override
    public void clear() {
        this.frameVal = EvilFog.FRAME_COUNT - 1;
        this.beforeRender(true);
        this.spawned = false;
        this.game.accessGlobalEntities().remove(EvilFog.class.getSimpleName());
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
    public void setObserver(final XYObservable observed) {
        this.observable = observed;
    }

    @Override
    public void basicSpawn(final int x, final int y) { }
    
    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getX() + (EvilFog.SPRT_WH / 6), 
            this.getY() + (EvilFog.SPRT_WH / 6), EvilFog.SPRT_WH / 3,
            EvilFog.SPRT_WH / 3);
    }

    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        return null;
    }
    //</editor-fold>
    
}
