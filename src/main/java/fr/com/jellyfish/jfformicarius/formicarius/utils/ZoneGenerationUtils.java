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
package fr.com.jellyfish.jfformicarius.formicarius.utils;

import fr.com.jellyfish.jfformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfformicarius.formicarius.entities.tiles.StaticObject;
import fr.com.jellyfish.jfformicarius.formicarius.entities.tiles.vegetation.Tree;
import fr.com.jellyfish.jfformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfformicarius.formicarius.texture.Sprite;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thw
 */
public class ZoneGenerationUtils {
    
    /**
     * 
     * @param zoneW
     * @param zoneH
     * @param randMax
     * @param expected
     * @return List<AbstractEntity>
     */
    public static List<AbstractEntity> buildRandomTerrainBareTrees(final int zoneW, final int zoneH, 
            final int randMax, final int expected) {
        
        final Sprite sprt1 = SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                    StaticSpriteVars.bare_tree1_100x50);
        final Sprite sprt2 = SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                    StaticSpriteVars.bare_tree2_100x50);
        
        final List<AbstractEntity> result = new ArrayList<>();
        Tree tree = null;
        int index = -1;

        for (int x = 0; x < zoneW / sprt1.getWidth(); ++x) {
            for (int y = 0; y < zoneH / sprt1.getHeight(); ++y) {
                if (expected == RandomUtils.randInt(0, randMax)) {
                    
                    if (expected == RandomUtils.randInt(0, randMax)) {
                        tree = new Tree(Game.getInstance(), sprt1, x * sprt1.getWidth(),
                            y * sprt1.getHeight());
                    } else {
                        tree = new Tree(Game.getInstance(), sprt2, x * sprt2.getWidth(),
                            y * sprt2.getHeight());
                    }
                    result.add(++index, tree);
                }
            }
        }

        return result;
    }
    
    /**
     * 
     * @param zoneW
     * @param zoneH
     * @param sprt
     * @param randMax
     * @param expected
     * @return 
     */
    public static List<AbstractEntity> appendRandomTerrainNonCollibableElements(final int zoneW, final int zoneH, 
        final Sprite sprt, final int randMax, final int expected) {
        
        final List<AbstractEntity> result = new ArrayList<>();
        StaticObject nce = null;
        int index = -1;

        for (int x = 0; x < zoneW / sprt.getWidth(); ++x) {
            for (int y = 0; y < zoneH / sprt.getHeight(); ++y) {
                if (expected == RandomUtils.randInt(0, randMax)) {
                    nce = new StaticObject(Game.getInstance(), sprt, x * sprt.getWidth(),
                        y * sprt.getHeight());
                    result.add(++index, nce);
                }
            }
        }

        return result;
    }
    
}
