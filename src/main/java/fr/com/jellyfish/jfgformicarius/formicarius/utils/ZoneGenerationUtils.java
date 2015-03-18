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
package fr.com.jellyfish.jfgformicarius.formicarius.utils;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.StaticObject;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.vegetation.Tree;
import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneGenerationException;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.ZoneBuilder;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.CardinalityDefinition;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.ZonePosition;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.cave.CaveZone;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.cave.CaveZoneWallCardinalityDefintions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thw
 */
public class ZoneGenerationUtils {

    /**
     *
     * @return
     */
    public static HashMap<ZonePosition, ZoneBuilder> buildCaveZones1() {

        final HashMap<ZonePosition, ZoneBuilder> zones = new HashMap<>();

        // MEMO : N E S W
        zones.put(new ZonePosition(0, 0),
                new CaveZone(new CaveZoneWallCardinalityDefintions()));
        zones.put(new ZonePosition(0, -1),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.NORTH, CardinalityDefinition.SOUTH)));
        zones.put(new ZonePosition(1, 0),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.WEST)));
        zones.put(new ZonePosition(0, 1),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.SOUTH)));
        zones.put(new ZonePosition(-1, 0),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.EAST, CardinalityDefinition.WEST)));
        zones.put(new ZonePosition(-2, 0),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.EAST, CardinalityDefinition.SOUTH)));
        zones.put(new ZonePosition(-2, -1),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.NORTH, CardinalityDefinition.SOUTH)));
        zones.put(new ZonePosition(-2, -2),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.SOUTH, CardinalityDefinition.NORTH)));
        zones.put(new ZonePosition(-2, -3),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.NORTH, CardinalityDefinition.EAST)));
        zones.put(new ZonePosition(-1, -3),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.WEST, CardinalityDefinition.NORTH)));
        zones.put(new ZonePosition(-1, -2),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.SOUTH, CardinalityDefinition.EAST)));
        zones.put(new ZonePosition(0, -2),
                new CaveZone(new CaveZoneWallCardinalityDefintions(CardinalityDefinition.NORTH, CardinalityDefinition.WEST)));

        return zones;
    }

    /**
     *
     * @param maxLoops
     * @return
     * @throws fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneGenerationException
     */
    public static HashMap<ZonePosition, ZoneBuilder> buildRandomCaveZones(final int maxLoops)
            throws ZoneGenerationException {

        final HashMap<ZonePosition, ZoneBuilder> zones = new HashMap<>();
        ZonePosition z = null;
        int loops = 0, x = 0, y = 0;
        CardinalityDefinition c = null;
        List<CardinalityDefinition> cList = new ArrayList<>();
        CardinalityDefinition[] cArray = null;
        CardinalityDefinition tmpC = null;
        
        // MEMO : N E S W
        do {
            
            z = new ZonePosition(x, y);
            
            if (zones.containsKey(z)) { 
                cList.addAll(((CaveZone) zones.get(z)).getCardinalityDefinitions());
                zones.remove(z);
            }

            try {
                
                if (loops != maxLoops) {
                    c = ZoneGenerationUtils.getRandomCardinalityDefintion();
                    cList.add(c);
                }
                
                if (tmpC != null) {
                    cList.add(tmpC);
                }
                
            } catch (final ZoneGenerationException zge) {
                Logger.getLogger(ZoneGenerationUtils.class.getName()).log(Level.SEVERE, null, zge);
                throw zge;
            }

            /**
             * Depending on random direction :
             * 1_increment or decrement x-y coordinates.
             * 2_affect temp cardinality var tmpC with value to impact on next 
             * iteration : if we move north, the new zone at north must have a
             * south cardinality = true - otherwise navigation between these 2
             * zones will be impossible.
             */
            switch (c) {
                case NORTH: ++y; tmpC = CardinalityDefinition.SOUTH; break;
                case EAST: ++x; tmpC = CardinalityDefinition.WEST; break;
                case SOUTH: --y; tmpC = CardinalityDefinition.NORTH; break;
                case WEST: --x; tmpC = CardinalityDefinition.EAST; break;
            }
            
            // Send Array to CaveZoneWallCardinalityDefintions(varargs) :
            cArray = new CardinalityDefinition[cList.size()];
            zones.put(z, new CaveZone(new CaveZoneWallCardinalityDefintions(cList.toArray(cArray))));
            cList.clear();
            
            ++loops;
        }  while (loops <= maxLoops);
        
        return zones;
    }

    /**
     *
     * @return @throws ZoneGenerationException
     */
    public static CardinalityDefinition getRandomCardinalityDefintion() throws ZoneGenerationException {

        switch (RandomUtils.randInt(1, 4)) {
            case MvtConst.UP:
                return CardinalityDefinition.NORTH;
            case MvtConst.RIGHT:
                return CardinalityDefinition.EAST;
            case MvtConst.DOWN:
                return CardinalityDefinition.SOUTH;
            case MvtConst.LEFT:
                return CardinalityDefinition.WEST;
        }

        throw new ZoneGenerationException();
    }

    /**
     * Speudo CODE :
     *
     * create a CellStack (LIFO) to hold a list of cell locations set TotalCells
     * = number of cells in grid
     *
     * choose a cell randomly and call it CurrentCell set VisitedCells = 1
     *
     * while VisitedCells smaller than TotalCells find all neighbors of
     * CurrentCell with all walls intact if one or more found choose one
     * randomly knock down the wall between it and CurrentCell push CurrentCell
     * location on the CellStack make the new cell CurrentCell add 1 to
     * VisitedCells else pop the most recent cell entry off the CellStack make
     * it CurrentCell endIf endWhile return CellStack matrix
     * *************************************************************************
     *
     * @param width : amount of cells on X
     * @param height : amount of cells on Y
     * @return ZoneBuilder[][] matrix of zone to naviate through.
     */
    public static HashMap<ZonePosition, ZoneBuilder> buildZoneMaze(final int width,
            final int height) {

        // Return map (LIFO).
        final LinkedHashMap<Integer[], CaveZoneWallCardinalityDefintions> stack = new LinkedHashMap<>();

        int visited = 1;
        final CaveZoneWallCardinalityDefintions[][] matrix = new CaveZoneWallCardinalityDefintions[width][height];
        int x = RandomUtils.randInt(0, width);
        int y = RandomUtils.randInt(0, height);
        ConcurrentHashMap<Integer[], CaveZoneWallCardinalityDefintions> neighboors = new ConcurrentHashMap<>();
        CaveZoneWallCardinalityDefintions currentCell = new CaveZoneWallCardinalityDefintions("");

        while (visited < width * height) {

            if (y > 0) {
                neighboors.put(new Integer[]{x, y - 1}, matrix[x][y - 1]); // North
            }
            if (x < width - 1) {
                neighboors.put(new Integer[]{x + 1, y}, matrix[x + 1][y]); // East
            }
            if (y < height - 1) {
                neighboors.put(new Integer[]{x, y + 1}, matrix[x][y + 1]); // South
            }
            if (x > 0) {
                neighboors.put(new Integer[]{x - 1, y}, matrix[x - 1][y]); // West
            }
            for (Entry<Integer[], CaveZoneWallCardinalityDefintions> z : neighboors.entrySet()) {
                if (z != null) {
                    neighboors.remove(z.getKey());
                }
            }

            CaveZoneWallCardinalityDefintions tmp = null;
            Integer[] tmpPosition = null;
            if (neighboors.size() == 1) {
                tmp = (CaveZoneWallCardinalityDefintions) neighboors.values().toArray()[0];
                tmpPosition = (Integer[]) neighboors.keySet().toArray()[0];
            } else if (neighboors.size() > 1) {
                final int randMax = RandomUtils.randInt(0, neighboors.size() - 1);
                tmp = (CaveZoneWallCardinalityDefintions) neighboors.values().toArray()[randMax];
                tmpPosition = (Integer[]) neighboors.keySet().toArray()[randMax];
            } else {
                if (stack.size() > 0) {
                    currentCell = stack.get((Integer[]) stack.keySet().toArray()[stack.size() - 1]);
                    stack.remove((Integer[]) stack.keySet().toArray()[stack.size() - 1]);
                }
            }

            // Define selected neighboor direction depending on current position.
            // Then "knock down walls" between the to cardianlity definitions.
            if (tmp != null) {

                if (tmpPosition[0] > x) {
                    // East :
                    currentCell.east = true;
                    tmp.west = true;
                } else if (tmpPosition[0] < x) {
                    // West :
                    currentCell.west = true;
                    tmp.east = true;
                } else if (tmpPosition[1] > y) {
                    // South :
                    currentCell.south = true;
                    tmp.north = true;
                } else if (tmpPosition[1] < y) {
                    // North :
                    currentCell.north = true;
                    tmp.south = true;
                }

                stack.put(new Integer[]{x, y}, currentCell);
                currentCell = tmp;
                ++visited;
            }

        }

        final HashMap<ZonePosition, ZoneBuilder> returnStack = new HashMap<>();
        for (Entry<Integer[], CaveZoneWallCardinalityDefintions> z : stack.entrySet()) {
            returnStack.put(new ZonePosition(z.getKey()[0], z.getKey()[1]), (ZoneBuilder) currentCell);
        }

        return returnStack;
    }

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
