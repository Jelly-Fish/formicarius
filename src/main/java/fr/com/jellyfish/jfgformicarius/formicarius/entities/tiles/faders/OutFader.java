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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.faders;

import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractFader;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.util.List;

/**
 *
 * @author thw
 */
public class OutFader extends AbstractFader {

    public static final String REF = "outfader";
    public static final int FRAME_COUNT = 16;
    private final Sprite[] frames;
    private int frameVal = 0;
    public static boolean fadding = false;

    public OutFader(final Game game, final String sprtDefault, final int x, final int y, final List<String> sprtRefs) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), sprtDefault), x, y,
                MvtConst.DOWN, OutFader.REF);
        
        frames = new Sprite[OutFader.FRAME_COUNT];
        for (int i = 0; i < OutFader.FRAME_COUNT; ++i) {
            frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), sprtRefs.get(i));
        }
       
        frameVal = OutFader.FRAME_COUNT - 1;
        sprite = frames[OutFader.FRAME_COUNT - 1];
        setAnimeUpdateRequired(false);
    }
    
    @Override
    public void fade() {
        Game.transiting = true;
        OutFader.fadding = true;
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
        
        if (((gLoopCntr1 >= AnimationConst.FPS / 60) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            if (frameVal <= 0) {
                frameVal = OutFader.FRAME_COUNT - 1;
                OutFader.fadding = false;
                sprite = frames[frameVal];
                setAnimeUpdateRequired(false);
                game.getEntityHelper().getInFader().fade();
                game.getEntityHelper().setFader(game.getEntityHelper().getInFader());
            } else {
                sprite = frames[frameVal];
                --frameVal;
            }
        } else {
            gLoopCntr1++;
        }
    }

    @Override
    public void afterRender(final boolean force) {
    }

}
