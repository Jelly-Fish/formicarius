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
import fr.com.jellyfish.jfgformicarius.formicarius.entities.pools.CaveWallOpeningEntityPool;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.backgrounds.cave.walls.CaveWall;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.backgrounds.cave.walls.CaveWallCorner;
import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneBuildException;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.ZoneBuilder;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticEntityReferences;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author thw
 */
public class CaveZone implements ZoneBuilder {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * Collection of wall entities.
     */
    private final Map<String, AbstractEntity> walls;

    /**
     * Wall definitions instance.
     */
    private final CaveZoneWallCardinalityDefintions wallDefinitions;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     *
     * @param wallDefinitions
     */
    public CaveZone(final CaveZoneWallCardinalityDefintions wallDefinitions) {
        this.wallDefinitions = wallDefinitions;
        walls = new HashMap<>();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public void buildZone(final AbstractEntity[] entities) throws ZoneBuildException {

        this.buildCaveZoneCorners();
        this.buildCaveZoneWalls();
    }

    /**
     * Build walls and therefor linkup corners.
     *
     * @param definitions
     */
    private void buildCaveZoneWalls() throws ZoneBuildException {

        if (this.wallDefinitions == null) {
            throw new ZoneBuildException();
        }

        CaveWall wall = null;

        /**
         * Walls top/north.
         */
        for (int i = 1; i <= 14; i++) {
            wall = new CaveWall(
                    SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                            StaticSpriteVars.cave_border_top), i * CaveWallCorner.SPRT_WH,
                    0, StaticEntityReferences.TOP_REF + i);
            walls.put(wall.ABSTRACT_REF, wall);
        }
        applyWallCardinalityDefinition(14, StaticEntityReferences.TOP_REF,
                this.wallDefinitions.NORTH, CaveZoneWallCardinalityDefintions.north);

        /**
         * Walls bottom.
         */
        for (int i = 1; i <= 14; i++) {
            wall = new CaveWall(
                    SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                            StaticSpriteVars.cave_border_bottom), i * CaveWallCorner.SPRT_WH,
                    FrameConst.FRM_HEIGHT - (CaveWallCorner.SPRT_WH / 2),
                    StaticEntityReferences.BOTTOM_REF + i);
            walls.put(wall.ABSTRACT_REF, wall);
        }
        applyWallCardinalityDefinition(14, StaticEntityReferences.BOTTOM_REF,
                this.wallDefinitions.SOUTH, CaveZoneWallCardinalityDefintions.south);

        /**
         * Walls left.
         */
        for (int i = 1; i <= 10; i++) {
            wall = new CaveWall(
                    SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                            StaticSpriteVars.cave_border_left), 0,
                    i * CaveWallCorner.SPRT_WH, StaticEntityReferences.LEFT_REF + i);
            walls.put(wall.ABSTRACT_REF, wall);

        }
        applyWallCardinalityDefinition(10, StaticEntityReferences.LEFT_REF,
                this.wallDefinitions.WEST, CaveZoneWallCardinalityDefintions.west);

        /**
         * Walls right.
         */
        for (int i = 1; i <= 10; i++) {
            wall = new CaveWall(
                    SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                            StaticSpriteVars.cave_border_right),
                    FrameConst.FRM_WIDTH - (CaveWallCorner.SPRT_WH / 2),
                    i * CaveWallCorner.SPRT_WH, StaticEntityReferences.RIGHT_REF + i);
            walls.put(wall.ABSTRACT_REF, wall);
        }
        applyWallCardinalityDefinition(10, StaticEntityReferences.RIGHT_REF,
                this.wallDefinitions.EAST, CaveZoneWallCardinalityDefintions.east);
    }

    /**
     * Build all corners. Corners are static and cannot be transformed or
     * modified.
     */
    private void buildCaveZoneCorners() {

        /**
         * Corner borders.
         */
        CaveWall wall = new CaveWallCorner(
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_border_top_left), 0, 0,
                StaticEntityReferences.TOP_LEFT_CORNER_REF,
                new Rectangle[]{new Rectangle(0, 0, CaveWallCorner.SPRT_WH / 2, CaveWallCorner.SPRT_WH),
                    new Rectangle(0, 0, CaveWallCorner.SPRT_WH, CaveWallCorner.SPRT_WH / 2)});
        walls.put(wall.ABSTRACT_REF, wall);
        wall = new CaveWallCorner(
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_border_bottom_left), 0,
                FrameConst.FRM_HEIGHT - CaveWallCorner.SPRT_WH,
                StaticEntityReferences.BOTTOM_LEFT_CORNER_REF,
                new Rectangle[]{new Rectangle(0, FrameConst.FRM_HEIGHT - CaveWallCorner.SPRT_WH,
                            CaveWallCorner.SPRT_WH / 2, CaveWallCorner.SPRT_WH),
                    new Rectangle(0, FrameConst.FRM_HEIGHT - (CaveWallCorner.SPRT_WH / 2),
                            CaveWallCorner.SPRT_WH, CaveWallCorner.SPRT_WH / 2)});
        walls.put(wall.ABSTRACT_REF, wall);
        wall = new CaveWallCorner(
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_border_top_right), FrameConst.FRM_WIDTH - CaveWallCorner.SPRT_WH,
                0, StaticEntityReferences.TOP_RIGHT_CORNER_REF,
                new Rectangle[]{new Rectangle(FrameConst.FRM_WIDTH - CaveWallCorner.SPRT_WH, 0,
                            CaveWallCorner.SPRT_WH, CaveWallCorner.SPRT_WH / 2),
                    new Rectangle(FrameConst.FRM_WIDTH - (CaveWallCorner.SPRT_WH / 2), 0,
                            CaveWallCorner.SPRT_WH / 2, CaveWallCorner.SPRT_WH)});
        walls.put(wall.ABSTRACT_REF, wall);
        wall = new CaveWallCorner(
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_border_bottom_right), FrameConst.FRM_WIDTH - CaveWallCorner.SPRT_WH,
                FrameConst.FRM_HEIGHT - CaveWallCorner.SPRT_WH,
                StaticEntityReferences.BOTTOM_RIGHT_CORNER_REF,
                new Rectangle[]{new Rectangle(FrameConst.FRM_WIDTH - (CaveWallCorner.SPRT_WH / 2),
                            FrameConst.FRM_HEIGHT - CaveWallCorner.SPRT_WH,
                            CaveWallCorner.SPRT_WH / 2, CaveWallCorner.SPRT_WH),
                    new Rectangle(FrameConst.FRM_WIDTH - CaveWallCorner.SPRT_WH,
                            FrameConst.FRM_HEIGHT - (CaveWallCorner.SPRT_WH / 2),
                            CaveWallCorner.SPRT_WH, CaveWallCorner.SPRT_WH / 2)});
        walls.put(wall.ABSTRACT_REF, wall);
    }

    /**
     * Trim wall if entrance is defined in cardinality direction.
     *
     * @param wallBlockCount
     * @param ref
     * @param definition
     * @throws ZoneBuildException
     */
    private void applyWallCardinalityDefinition(final int wallBlockCount, final String ref,
            final boolean definition, final String action)
            throws ZoneBuildException {

        if (this.walls == null || this.wallDefinitions == null) {
            throw new ZoneBuildException();
        }

        if (definition) {
            this.walls.get(ref + wallBlockCount / 2).sprite
                    = CaveWallOpeningEntityPool.getWallOpening1(wallDefinitions, action);
            this.walls.get(ref + ((wallBlockCount / 2) + 1)).sprite
                    = CaveWallOpeningEntityPool.getWallOpening2(wallDefinitions, action);
            
            /*******************************************************************
             * TODO : below needs major refactoring.
             * tile coordinates must be adjusted depending on cardinality.
             * Second left tile must be pushed foward onY coordiantes to link
             * up with next tile set in loop. A "opening tile" has different 
             * dimensions than a regular one.
             * If this procedure isnot performes, the "second tiles" are out of 
             * bounds and do not display correctly. 
             * 
             * TODO : re think all of this yet it works fine.
             */
            if (action.equals(CaveZoneWallCardinalityDefintions.north)) {
                this.walls.get(ref + ((wallBlockCount / 2) + 1)).setX( 
                    this.walls.get(ref + ((wallBlockCount / 2) + 1)).getX() + 
                        CaveWallCorner.SPRT_WH - 
                        this.walls.get(ref + ((wallBlockCount / 2) + 1)).sprite.getWidth());
            } else if (action.equals(CaveZoneWallCardinalityDefintions.east)) {
                this.walls.get(ref + ((wallBlockCount / 2) + 1)).setY( 
                    this.walls.get(ref + ((wallBlockCount / 2) + 1)).getY() + 
                        CaveWallCorner.SPRT_WH - 
                        this.walls.get(ref + ((wallBlockCount / 2) + 1)).sprite.getHeight());
            } else if (action.equals(CaveZoneWallCardinalityDefintions.south)) {
                this.walls.get(ref + ((wallBlockCount / 2) + 1)).setX( 
                    this.walls.get(ref + ((wallBlockCount / 2) + 1)).getX() + 
                        CaveWallCorner.SPRT_WH - 
                        this.walls.get(ref + ((wallBlockCount / 2) + 1)).sprite.getWidth());
            } else if (action.equals(CaveZoneWallCardinalityDefintions.west)) {
                this.walls.get(ref + ((wallBlockCount / 2) + 1)).setY( 
                    this.walls.get(ref + ((wallBlockCount / 2) + 1)).getY() + 
                        CaveWallCorner.SPRT_WH - 
                        this.walls.get(ref + ((wallBlockCount / 2) + 1)).sprite.getHeight());
            } else {
                throw new ZoneBuildException();
            }
            /******************************************************************/
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
    //</editor-fold>

}
