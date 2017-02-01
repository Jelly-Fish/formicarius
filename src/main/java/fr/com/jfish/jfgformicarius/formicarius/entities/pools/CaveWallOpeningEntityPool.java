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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.pools;

import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneBuildException;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.cave.CaveZoneWallCardinalityDefintions;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author thw
 */
public class CaveWallOpeningEntityPool {
    
    public final static Map<String, Sprite> openWaveWallBlockSprites = new HashMap<>();
    static
    {
        CaveWallOpeningEntityPool.openWaveWallBlockSprites.put(StaticSpriteVars.cave_border_opening_left1, 
            SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                StaticSpriteVars.cave_border_opening_left1));
        CaveWallOpeningEntityPool.openWaveWallBlockSprites.put(StaticSpriteVars.cave_border_opening_left2, 
            SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                StaticSpriteVars.cave_border_opening_left2));
        CaveWallOpeningEntityPool.openWaveWallBlockSprites.put(StaticSpriteVars.cave_border_opening_top1, 
            SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                StaticSpriteVars.cave_border_opening_top1));
        CaveWallOpeningEntityPool.openWaveWallBlockSprites.put(StaticSpriteVars.cave_border_opening_top2, 
            SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                StaticSpriteVars.cave_border_opening_top2));
        CaveWallOpeningEntityPool.openWaveWallBlockSprites.put(StaticSpriteVars.cave_border_opening_right1, 
            SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                StaticSpriteVars.cave_border_opening_right1));
        CaveWallOpeningEntityPool.openWaveWallBlockSprites.put(StaticSpriteVars.cave_border_opening_right2, 
            SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                StaticSpriteVars.cave_border_opening_right2));
        CaveWallOpeningEntityPool.openWaveWallBlockSprites.put(StaticSpriteVars.cave_border_opening_bottom1, 
            SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                StaticSpriteVars.cave_border_opening_bottom1));
        CaveWallOpeningEntityPool.openWaveWallBlockSprites.put(StaticSpriteVars.cave_border_opening_bottom2, 
            SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                StaticSpriteVars.cave_border_opening_bottom2));
    }

    /**
     * 
     * @param wallDefinitions
     * @param action
     * @return
     * @throws ZoneBuildException 
     */
    public static Sprite getWallOpening1(final CaveZoneWallCardinalityDefintions wallDefinitions,
            final String action) 
        throws ZoneBuildException {
        
        if (wallDefinitions.north && action.equals(CaveZoneWallCardinalityDefintions.NORTH_STRVAL)) {
            return CaveWallOpeningEntityPool.openWaveWallBlockSprites.get(
                    StaticSpriteVars.cave_border_opening_top1);
        } else if (wallDefinitions.east && action.equals(CaveZoneWallCardinalityDefintions.EAST_STRVAL)) {
            return CaveWallOpeningEntityPool.openWaveWallBlockSprites.get(
                    StaticSpriteVars.cave_border_opening_right1);
        } else if (wallDefinitions.south && action.equals(CaveZoneWallCardinalityDefintions.SOUTH_STRVAL)) {
            return CaveWallOpeningEntityPool.openWaveWallBlockSprites.get(
                    StaticSpriteVars.cave_border_opening_bottom1);
        } else if (wallDefinitions.west && action.equals(CaveZoneWallCardinalityDefintions.WEST_STRVAL)) {
            return CaveWallOpeningEntityPool.openWaveWallBlockSprites.get(
                    StaticSpriteVars.cave_border_opening_left1);
        } else {
            throw new ZoneBuildException();
        }
    }

    /**
     * 
     * @param wallDefinitions
     * @param action
     * @return
     * @throws ZoneBuildException 
     */
    public static Sprite getWallOpening2(final CaveZoneWallCardinalityDefintions wallDefinitions,
            final String action)
        throws ZoneBuildException {
    
        if (wallDefinitions.north && action.equals(CaveZoneWallCardinalityDefintions.NORTH_STRVAL)) {
            return CaveWallOpeningEntityPool.openWaveWallBlockSprites.get(
                    StaticSpriteVars.cave_border_opening_top2);
        } else if (wallDefinitions.east && action.equals(CaveZoneWallCardinalityDefintions.EAST_STRVAL)) {
            return CaveWallOpeningEntityPool.openWaveWallBlockSprites.get(
                    StaticSpriteVars.cave_border_opening_right2);
        } else if (wallDefinitions.south && action.equals(CaveZoneWallCardinalityDefintions.SOUTH_STRVAL)) {
            return CaveWallOpeningEntityPool.openWaveWallBlockSprites.get(
                    StaticSpriteVars.cave_border_opening_bottom2);
        } else if (wallDefinitions.west && action.equals(CaveZoneWallCardinalityDefintions.WEST_STRVAL)) {
            return CaveWallOpeningEntityPool.openWaveWallBlockSprites.get(
                    StaticSpriteVars.cave_border_opening_left2);
        } else {
            throw new ZoneBuildException();
        }
    }
    
}
