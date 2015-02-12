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
package fr.com.jellyfish.jfformicarius.formicarius.entities.tiles.effects.blood;

import fr.com.jellyfish.jfformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfformicarius.formicarius.utils.RandomUtils;
import fr.com.jellyfish.jfformicarius.formicarius.utils.SpriteUtils;

/**
 *
 * @author thw
 */
public class BloodAfterImpact extends AbstractEntity {

    private int frameVal = 0;
    public static final int SPRT_WH = 60;
    public static final String REF = "blood_after_impact";
    public static final int FRAME_COUNT = 6;
    public static int occurs = 0;
    
    /**
     * Consttructor.
     * @param sprtStringFormat String format for path towards texture file. 
     */
    public BloodAfterImpact(final String sprtStringFormat) {
        super(Game.getInstance(), SpriteUtils.getSprite(
            Game.getInstance().getTextureLoader(), String.format(sprtStringFormat, 0)),
            0, 0, 0, BloodAfterImpact.REF + BloodAfterImpact.occurs);
        
        final int random = RandomUtils.randInt(0, BloodAfterImpact.FRAME_COUNT - 1);
        
        sprite = SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
            String.format(sprtStringFormat, random));
        setAnimeUpdateRequired(true);
        ++BloodAfterImpact.occurs;
    }
    
    /**
     * 
     * @param x
     * @param y 
     */
    public void appendTile(final int x, final int y) {
        this.setX(x);
        this.setY(y);
        Game.getInstance().getEntityHelper().getStaticEntities().put(this.ABSTRACT_REF, this);
    }
    
    @Override
    public void collideWith(final AbstractEntity other) { }

    @Override
    public void doLogic() { }

    @Override
    public void beforeRender(final boolean force) { }

    @Override
    public void afterRender(final boolean force) { 
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }
    
}
