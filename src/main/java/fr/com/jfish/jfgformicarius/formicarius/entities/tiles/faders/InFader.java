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
package fr.com.jfish.jfgformicarius.formicarius.entities.tiles.faders;

import fr.com.jfish.jfgformicarius.formicarius.entities.abstractentities.AbstractFader;
import fr.com.jfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import fr.com.jfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.util.List;

/**
 *
 * @author thw
 */
public class InFader extends AbstractFader {

    public static final String REF = "infader";
    public static final int FRAME_COUNT = 16;
    private final Sprite[] frames;
    private int frameVal = 0;
    public static boolean fadding = false;

    public InFader(final Game game, final String sprtDefault, final int x, final int y, final List<String> sprtRefs) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), sprtDefault), x, y,
                MvtConst.DOWN, InFader.REF);
        
        frames = new Sprite[InFader.FRAME_COUNT];
        for (int i = 0; i < InFader.FRAME_COUNT; ++i) {
            frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), sprtRefs.get(i));
        }
       
        sprite = frames[0];
        setAnimeUpdateRequired(false);
    }
    
    @Override
    public void fade() {
        InFader.fadding = true;
        setAnimeUpdateRequired(true);
    }

    @Override
    public void collideWith(AbstractEntity other) {
    }

    @Override
    public void doLogic() {
    }

    @Override
    public void beforeRender(final boolean force) {
        
        if (((gLoopCntr1 >= AnimationConst.FPS / 80) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            if (frameVal >= InFader.FRAME_COUNT - 1) {
                frameVal = 0;
                InFader.fadding = false;
                Game.transiting = false;
                sprite = frames[frameVal];
                setAnimeUpdateRequired(false);
                game.getEntityHelper().setFader(game.getEntityHelper().getOutFader());
            } else {
                sprite = frames[frameVal];
                ++frameVal;
            }
        } else {
            gLoopCntr1++;
        }
    }

    @Override
    public void afterRender(final boolean force) {
    }
    
}
