/**
 * *****************************************************************************
 * Copyright (c) 2014 - 2015, Thomas.H Warner. All rights reserved.
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
package fr.com.jellyfish.jfgformicarius.formicarius.world.zone.cave;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.backgrounds.cave.walls.CaveWall;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.backgrounds.cave.walls.CaveWallCorner;
import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneBuildException;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.ZoneBuilder;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticEntityReferences;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author thw
 */
public class CaveZone implements ZoneBuilder {

    /**
     *
     */
    private final Map<String, AbstractEntity> walls;

    public CaveZone() {
        walls = new HashMap<>();
    }

    @Override
    public void buildZone(final AbstractEntity[] entities) throws ZoneBuildException {

        /**
         * Corner borders.
         */
        CaveWall wall = new CaveWallCorner(
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_border_top_left), 0, 0,
                StaticEntityReferences.TOP_LEFT_CORNER_REF);
        walls.put(wall.ABSTRACT_REF, wall);
        wall = new CaveWallCorner(
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_border_bottom_left), 0,
                FrameConst.FRM_HEIGHT - CaveWall.SPRT_WH,
                StaticEntityReferences.BOTTOM_LEFT_CORNER_REF);
        walls.put(wall.ABSTRACT_REF, wall);
        wall = new CaveWallCorner(
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_border_top_right), FrameConst.FRM_WIDTH - CaveWall.SPRT_WH,
                0, StaticEntityReferences.TOP_RIGHT_CORNER_REF);
        walls.put(wall.ABSTRACT_REF, wall);
        wall = new CaveWallCorner(
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_border_bottom_right), FrameConst.FRM_WIDTH - CaveWall.SPRT_WH,
                FrameConst.FRM_HEIGHT - CaveWall.SPRT_WH,
                StaticEntityReferences.BOTTOM_RIGHT_CORNER_REF);
        walls.put(wall.ABSTRACT_REF, wall);

        /**
         * Walls top.
         */
        for (int i = 1; i <= 14; i++) {
            wall = new CaveWall(
                    SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                            StaticSpriteVars.cave_border_top), i * CaveWall.SPRT_WH,
                    0, StaticEntityReferences.TOP_REF + i);
            walls.put(wall.ABSTRACT_REF, wall);
        }

        /**
         * Walls bottom.
         */
        for (int i = 1; i <= 14; i++) {
            wall = new CaveWall(
                    SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                            StaticSpriteVars.cave_border_bottom), i * CaveWall.SPRT_WH,
                    FrameConst.FRM_HEIGHT - CaveWall.SPRT_WH,
                    StaticEntityReferences.BOTTOM_REF + i);
            walls.put(wall.ABSTRACT_REF, wall);
        }

        /**
         * Walls left.
         */
        for (int i = 1; i <= 10; i++) {
            wall = new CaveWall(
                    SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                            StaticSpriteVars.cave_border_left), 0,
                    i * CaveWall.SPRT_WH, StaticEntityReferences.LEFT_REF + i);
            walls.put(wall.ABSTRACT_REF, wall);
        }

        /**
         * Walls right.
         */
        for (int i = 1; i <= 10; i++) {
            wall = new CaveWall(
                    SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                            StaticSpriteVars.cave_border_right),
                    FrameConst.FRM_WIDTH - CaveWall.SPRT_WH,
                    i * CaveWall.SPRT_WH, StaticEntityReferences.RIGHT_REF + i);
            walls.put(wall.ABSTRACT_REF, wall);
        }
    }

    @Override
    public Map<String, AbstractEntity> getGlobals() throws ZoneBuildException {
        return this.walls;
    }

    @Override
    public Map<String, AbstractEntity> getStatics() throws ZoneBuildException {
        return null;
    }

}
