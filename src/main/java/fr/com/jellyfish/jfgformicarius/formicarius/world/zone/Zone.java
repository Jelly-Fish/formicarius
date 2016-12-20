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
package fr.com.jellyfish.jfgformicarius.formicarius.world.zone;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.backgrounds.Background;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.CaveEntrance;
import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneBuildException;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.ZoneBuilder;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.RandomUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.ZoneGenerationUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author thw
 */
public class Zone implements ZoneBuilder {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public final Map<String, AbstractEntity> interactables;
    public final Map<String, AbstractEntity> globals;
    public final Map<String, AbstractEntity> statics;
    public final List<Background> backgroundTiles = new ArrayList<>();
    private final RandomDefinition randomDefinitions;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     *
     * @param randomDefinitions Definition of randomness values. For key
     * 'Trees', value int[0] will define maximum random value and value int[1]
     * will define expected random number that will validate an action or
     * process to trigger depending on random number returned and it's equality
     * with value int[1] expected integer. Hash map key may be Class name for
     * reflection use.
     * @throws
     * fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneBuildException
     */
    public Zone(final RandomDefinition randomDefinitions)
            throws ZoneBuildException {

        interactables = new HashMap<>();
        globals = new ConcurrentHashMap<>();
        statics = new ConcurrentHashMap<>();
        this.randomDefinitions = randomDefinitions;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Initialize all gloabal AbstractEntity terrain elements and append to
     * globals HashMap.
     *
     * @throws ZoneBuildException
     */
    @Override
    public void buildZone(final AbstractEntity[] entities) throws ZoneBuildException {

        int index = -1;
        ////////////////////////////////////////////////////////////////////////
        // Build trees :
        for (AbstractEntity ent : ZoneGenerationUtils.buildRandomTerrainBareTrees(FrameConst.FRM_WIDTH, FrameConst.FRM_HEIGHT,
                randomDefinitions.MAX, randomDefinitions.AVERAGE)) {
            this.globals.put(String.valueOf(++index), ent);
        }
        index = -1;

        ////////////////////////////////////////////////////////////////////////
        // Build static vegetation :
        for (AbstractEntity ent : ZoneGenerationUtils.appendRandomTerrainNonCollibableElements(FrameConst.FRM_WIDTH, FrameConst.FRM_HEIGHT,
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.mushrooms_brown_25x25),
                randomDefinitions.MAX * 32, randomDefinitions.AVERAGE + 10)) {
            if (!CollisionUtils.inCollision(this.statics, ent)) {
                this.statics.put(String.valueOf(++index), ent);
            }
        }
        
        for (AbstractEntity ent : ZoneGenerationUtils.appendRandomTerrainNonCollibableElements(FrameConst.FRM_WIDTH, FrameConst.FRM_HEIGHT,
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.mushrooms_red_25x25),
                randomDefinitions.MAX * 42, randomDefinitions.AVERAGE + 10)) {
            if (!CollisionUtils.inCollision(this.statics, ent)) {
                this.statics.put(String.valueOf(++index), ent);
            }
        }

        ////////////////////////////////////////////////////////////////////////
        // Build cave hole if random evaluation requires it :
        // this.statics.put
        final CaveEntrance caveEntrance = new CaveEntrance(Game.getInstance(),
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(),
                        StaticSpriteVars.cave_hole1),
                RandomUtils.randInt(0, 
                    FrameConst.FRM_WIDTH - CaveEntrance.SPRT_WH),
                RandomUtils.randInt(0, 
                    FrameConst.FRM_HEIGHT - CaveEntrance.SPRT_WH)
                );
        
        boolean caveEntraceAdded = true;
        for (AbstractEntity ent : this.globals.values()) {
            if (ent.collidesWith(caveEntrance)) {
                caveEntraceAdded = false;
                break;
            }
        }
        
        if (caveEntraceAdded) {
            for (Map.Entry pair : this.statics.entrySet()) {
                if (caveEntrance.collidesWith((AbstractEntity) pair.getValue())) {
                    this.statics.remove((String) pair.getKey());
                }
            }
            this.statics.put(CaveEntrance.REF, caveEntrance);
        }

        ////////////////////////////////////////////////////////////////////////
        // Trim collisions between param AbstractEntity collection and globals :  
        final Iterator iterator = this.globals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            for (int i = 0; i < entities.length; ++i) {
                if (entities[i].collidesWith((AbstractEntity) pair.getValue())) {
                    this.globals.remove((String) pair.getKey());
                    break;
                }
            }
        }

    }

    /**
     * Access populated AbstractEntity hasp map.
     *
     * @return Map<String, AbstractEntity>
     * @throws
     * fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneBuildException
     */
    @Override
    public Map<String, AbstractEntity> getGlobals() throws ZoneBuildException {
        if (this.globals == null) {
            throw new ZoneBuildException();
        }
        return globals;
    }

    /**
     *
     * @return @throws
     * fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneBuildException
     */
    @Override
    public Map<String, AbstractEntity> getStatics() throws ZoneBuildException {
        if (this.statics == null) {
            throw new ZoneBuildException();
        }
        return statics;
    }
    
    @Override
    public List<CardinalityDefinition> getCardinalityDefinitions() {
        return null;
    }
    //</editor-fold>
    
}
