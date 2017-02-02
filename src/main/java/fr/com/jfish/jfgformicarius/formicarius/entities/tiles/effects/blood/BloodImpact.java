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
package fr.com.jfish.jfgformicarius.formicarius.entities.tiles.effects.blood;

import fr.com.jfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import fr.com.jfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jfish.jfgformicarius.formicarius.interfaces.Spawnable;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jfish.jfgformicarius.formicarius.utils.RandomUtils;
import fr.com.jfish.jfgformicarius.formicarius.utils.SpriteUtils;

/**
 *
 * @author thw
 */
public class BloodImpact extends AbstractEntity implements Spawnable {
    
    public static final String REF = "blood_impact";
    public static final int SPRT_WH = 50;
    public static final int FRAME_COUNT = 6;
    private final int drawRate = 12;
    private int frameVal = 0;
    public static int occurs = 0;
    
    /**
     * quakeRing frame array.
     */
    private final Sprite[] frames;
    
    public BloodImpact(final Sprite defaultSprt) {
        super(Game.getInstance(), SpriteUtils.getSprite(
            Game.getInstance().getTextureLoader(), String.format(StaticSpriteVars.blood_impact1, 0)),
            0, 0, 0, BloodImpact.REF + BloodImpact.occurs);
        
        frames = new Sprite[BloodImpact.FRAME_COUNT];
        for (int i = 0; i < frames.length; ++i) {
            frames[i] = SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                String.format(StaticSpriteVars.blood_impact1, i));
        }
        
        sprite = frames[frameVal];
        setAnimeUpdateRequired(false);
        
        ++BloodImpact.occurs;
    }
    
    /**
     * 
     */
    @Override
    public boolean basicSpawn(final int x, final int y) {
        this.setX(x); 
        this.setY(y);
        this.setCurrentMvt(MvtConst.STILL);
        Game.getInstance().getEntityHelper().getGlobalEntities().put(this.ABSTRACT_REF, this);
        if (RandomUtils.randInt(0, 10) % 2 == 0) {
            Game.getInstance().getSoundManager().playEffect(StaticSoundVars.bloody_squash2);
        } else {
            Game.getInstance().getSoundManager().playEffect(StaticSoundVars.bloody_squash1);
        }
        setAnimeUpdateRequired(true);
        frameVal = 0;
        
        return true;
    }

    @Override
    public void collideWith(final AbstractEntity other) { }

    @Override
    public void doLogic() { }

    @Override
    public void beforeRender(final boolean force) { 

        if (((gLoopCntr1 >= AnimationConst.FPS / this.drawRate) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            if (frameVal >= BloodImpact.FRAME_COUNT) { 
                // Remove occurence of this from Game.
                Game.getInstance().getEntityHelper().getGlobalEntities().remove(this.ABSTRACT_REF);
                setAnimeUpdateRequired(false);
            } else {
                sprite = frames[frameVal];
                if (frameVal == BloodImpact.FRAME_COUNT / 2) {
                    new BloodAfterImpact(StaticSpriteVars.blood_after_impact).appendTile(
                        this.getX() - ((BloodAfterImpact.SPRT_WH - BloodImpact.SPRT_WH) / 2),
                        this.getY());
                }
                ++frameVal;
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
    public boolean isSpawned() { 
        return isAnimeUpdateRequired();
    }

    @Override
    public String getAbstractRef() {
        return this.ABSTRACT_REF;
    }

    @Override
    public void clear() { }

    @Override
    public void notifySpawnEvent() { }

    @Override
    public float spawn(final int x, final int y) {
        return 0.0f;
    }
    
}
