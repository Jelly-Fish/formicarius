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
package fr.com.jellyfish.jfgformicarius.formicarius.helpers.entities;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.characters.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.backgrounds.Background;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractFader;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.faders.InFader;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.faders.OutFader;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.ZonePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author thw
 */
public class EntityHelper {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * Main game ref.
     */
    private Game game;

    /**
     * The list of all entities.
     */
    private final Map<String, AbstractEntity> globalEntities;
    
    /**
     * The list of all tile entities.
     */
    private final Map<String, AbstractEntity> tileEntities;
    
    /**
     * The list of all object entities.
     */
    private final Map<String, AbstractEntity> objectEntities;
    
    /**
     * Interactable entity collection such as enemies. Or Breakable objects.
     */
    private final Map<String, AbstractEntity> interactableEntities;
    
    /**
     * Main etity collection.
     */
    private final Map<String, AbstractEntity> mainEntities;
    
    /**
     * Static non collidable entity collection such vegetation.
     */
    private final Map<String, AbstractEntity> staticEntities;

    /**
     * Fader for game transitions, fade out effect.
     */
    private OutFader outFader;
    
    /**
     * Fader for game transitions, fade in effect.
     */
    private InFader inFader;
    
    /**
     * Fader instance for transitions.
     */
    private AbstractFader fader;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * constructor.
     */
    public EntityHelper() {
        this.mainEntities = new ConcurrentHashMap<>();
        this.globalEntities = new ConcurrentHashMap<>();
        this.tileEntities = new ConcurrentHashMap<>();
        this.objectEntities = new ConcurrentHashMap<>();
        this.interactableEntities = new ConcurrentHashMap<>();
        this.staticEntities = new ConcurrentHashMap<>();
        this.outFader = null;
        this.inFader = null;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Initialize this helper class.
     * @param game
     */
    public void init(final Game game) {
        
        this.game = game;
        // Clear all hash maps.
        clearAllEntityMaps();
        
        // Init AbstractEntity collection.
        initEntities();
        initTilingEntities(StaticSpriteVars.abstract_dark2_400);
        initFaders();
    }
    
    /**
     * Clear all hash maps collections of AbstractEntity classes.
     */
    private void clearAllEntityMaps() {
        globalEntities.clear();
        tileEntities.clear();
        objectEntities.clear();
        interactableEntities.clear();
    }
    
    /**
     * Initialize background texturing.
     * @param spriteRef400x400 
     */
    public void initTilingEntities(final String spriteRef400x400) {
        
        // Main start off background.
        Background b = null;
        for (int i = 0; i < FrameConst.FRM_WIDTH / 400; ++i) {
            for (int j = 0; j < FrameConst.FRM_HEIGHT / 300; ++j) {
                b = new Background(this.game, spriteRef400x400, i * 400, j * 400, null);
                tileEntities.put(Background.REF + i + j, b);
            }
        }
    }
    
    /**
     * Initialize all necessary global and main entities.
     */
    private void initEntities() {
        
        final MainCharacter wiz = new MainCharacter(
            this.game, StaticSpriteVars.red_wizzard, StaticSpriteVars.red_wizzard_hits,
            20, 280, 220, new ZonePosition(0,0), MainCharacter.class.getSimpleName());
        // Clear refs before new usage.

        // Finally append in order for drawing.
        mainEntities.put(MainCharacter.class.getSimpleName(), wiz);
    }
    
    /**
     * Init faders.
     */
    private void initFaders() {
        
        // Init faders :
        List<String> refs = new ArrayList<>();
        for (int i = 0; i < OutFader.FRAME_COUNT; ++i) {
            refs.add(String.format(StaticSpriteVars.fader1, i));
        }
        this.outFader = new OutFader(game, refs.get(OutFader.FRAME_COUNT - 1), 0, 0, refs);
        refs.clear();
        
        for (int i = 0; i < InFader.FRAME_COUNT; ++i) {
            refs.add(String.format(StaticSpriteVars.fader1, i));
        }
        this.inFader = new InFader(game, refs.get(InFader.FRAME_COUNT - 1), 0, 0, refs);
        refs.clear();
        
        this.fader = this.outFader;
    }
    
    /**
     * Get MagicalHumanoid main game AbstractEntity.
     * @return 
     */
    public MainCharacter getMainCharacter() {
        return (MainCharacter)mainEntities.get(MainCharacter.class.getSimpleName());
    }
    
    /**
     * When building Zone randomly, some entites must be removed or trimed in order
     * to avoid collision or overlapping.
     * @return AbstractEntity[] All active and necessary to display AbstractEntity.
     */
    public AbstractEntity[] returnActiveEntititesForZoneBuild() {
        
        final List<AbstractEntity> zoneActiveEntities = new ArrayList<>(this.mainEntities.values());
        zoneActiveEntities.addAll(this.interactableEntities.values());
        
        return zoneActiveEntities.toArray(new AbstractEntity[zoneActiveEntities.size()]);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters & setters">
    public Map<String, AbstractEntity> getStaticEntities() {
        return staticEntities;
    }

    public Map<String, AbstractEntity> getMainEntities() {
        return mainEntities;
    }
    
    public Map<String, AbstractEntity> getInteractableEntities() {
        return interactableEntities;
    }
    
    public AbstractFader getFader() {
        return fader;
    }
    
    public OutFader getOutFader() {
        return outFader;
    }

    public InFader getInFader() {
        return inFader;
    }
    
    public void setFader(final AbstractFader fader) {
        this.fader = fader;
    }
    
    public Map<String, AbstractEntity> getGlobalEntities() {
        return globalEntities;
    }
    
    public Map<String, AbstractEntity> getTileEntities() {
        return tileEntities;
    }

    public Map<String, AbstractEntity> getObjectEntities() {
        return objectEntities;
    }
    //</editor-fold>
    
}
