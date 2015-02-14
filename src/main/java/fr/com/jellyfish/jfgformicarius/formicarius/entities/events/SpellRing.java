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
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Spawnable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.SpellBoundable;
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
public class SpellRing extends AbstractEntity implements Spawnable, CollidableObject {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final float STAMINA_VALUE = 12.0f;
    public static final int SPRT_WH = 128;
    public static final int FRAME_COUNT = 62;
    private final int drawRate = 30;
    private int frameVal = 0;
    private static int occurs = 0;
    private boolean spawned = false;
    private boolean spellActive = false;

    /**
     * quakeRing frame array.
     */
    private final Sprite[] frames;

    /**
     *
     */
    private SpellBoundable spellBoundable = null;
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public SpellRing(final Game game, final Sprite defaultSprt, final int x, final int y) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), StaticSpriteVars.quake_ring_lib_icon),
                x, y, 0, SpellRing.class.getSimpleName() + SpellRing.occurs);

        frames = new Sprite[SpellRing.FRAME_COUNT];
        for (int i = 0; i < frames.length; ++i) {
            frames[i] = SpriteUtils.getSprite(game.getTextureLoader(),
                    String.format(StaticSpriteVars.quake_ring, i));
        }

        sprite = frames[frameVal];
        setAnimeUpdateRequired(false);
    }
    //</editor-fold> 
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public float spawn(final int x, final int y) {

        if (!this.spawned) {
            // Update location. Add to game's entity map. Set visible and 
            // candidate for remove from game's Entity main hashmap.
            setAnimeUpdateRequired(true);
            this.spawned = true;
            this.spellActive = false;
            game.getSoundManager().playEffect(StaticSoundVars.spellring);
            this.x = x - (SpellRing.SPRT_WH / 2);
            this.y = y - (SpellRing.SPRT_WH / 2);
            this.game.accessGlobalEntities().put(this.ABSTRACT_REF, this);
            ++SpellRing.occurs;
            return SpellRing.STAMINA_VALUE;
        }
        
        return 0.0f;
    }
    
    @Override
    public void basicSpawn(final int x, final int y) { }

    @Override
    public void collideWith(final AbstractEntity other) {

        if (!this.spellActive && this.spawned && other instanceof SpellBoundable) {
            if (CollisionUtils.inFullCollision(this.getRectangle(), new Rectangle(
                    other.getX(), other.getY(), other.sprite.getWidth(),
                    other.sprite.getHeight()))) {
                this.spellBoundable = ((SpellBoundable) other).spellbound();
                this.spellActive = true;
            }
        }
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getX() + (SpellRing.SPRT_WH / 4), 
            this.getY() + (SpellRing.SPRT_WH / 4), SpellRing.SPRT_WH / 2,
            SpellRing.SPRT_WH / 2);
    }

    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        return null;
    }

    @Override
    public void beforeRender(final boolean force) {

        if (((gLoopCntr1 >= AnimationConst.FPS / this.drawRate) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            if (frameVal >= SpellRing.FRAME_COUNT) {
                // Remove occurence of this from Game AbstractEntity map,
                // finally reset.
                this.spawned = false;
                this.game.getEntityHelper().getMainEntities().remove(this.ABSTRACT_REF);
                frameVal = 0;
                sprite = frames[frameVal];
                sprite.draw((int) x, (int) y);
                setAnimeUpdateRequired(false);
                this.clear();
            } else {
                sprite = frames[frameVal];
                ++frameVal;
            }
        } else {
            gLoopCntr1++;
        }
    }

    @Override
    public void notifySpawnEvent() {
    }

    @Override
    public void clear() {

        if (this.spellBoundable != null) {
            this.spellBoundable.removeSpellbound();
        }
        frameVal = 0;
        sprite = frames[frameVal];
        this.spawned = false;
        this.spellActive = false;
        setAnimeUpdateRequired(false);
    }

    @Override
    public void afterRender(final boolean force) {
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }

    @Override
    public boolean isSpawned() {
        return spawned;
    }

    @Override
    public String getAbstractRef() {
        return this.ABSTRACT_REF;
    }

    @Override
    public void doLogic() {
    }
    //</editor-fold> 

}
