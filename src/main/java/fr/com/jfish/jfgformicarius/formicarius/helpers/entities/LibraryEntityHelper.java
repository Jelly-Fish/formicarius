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
package fr.com.jfish.jfgformicarius.formicarius.helpers.entities;

import fr.com.jfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jfish.jfgformicarius.formicarius.entities.tiles.LibBackground;
import fr.com.jfish.jfgformicarius.formicarius.entities.tiles.LibItemContainer;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import fr.com.jfish.jfgformicarius.formicarius.library.ActiveLibraryItem;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thw
 */
public class LibraryEntityHelper {
    
    /**
     * Main game instance reference.
     */
    private final Game game;
    
    /**
     * Librairy's tile AbstractEntity collection.
     */
    private final Map<String, LibItemContainer> libraryContainers;
    
    /**
     * Library's activly 'in game' items to be highlighted.
     */
    public final Map<String, ActiveLibraryItem> activeLibItems = new HashMap<>();
    
    /**
     * List of background AbstractEntity classes.
     */
    private final List<LibBackground> libraryBackgroundTiles;

    public LibraryEntityHelper(final Game game) {
        this.game = game;
        this.libraryContainers = new HashMap<>();
        this.libraryBackgroundTiles = new ArrayList<>();
        libraryContainers.clear();
        initMenuEntities();
    }
    
    private void initMenuEntities() {
        
        // Menu background.
        LibBackground mb = null;
        for (int i = 0; i < FrameConst.FRM_WIDTH / 400; ++i) {
            for (int j = 0; j < FrameConst.FRM_HEIGHT / 300; ++j) {
                mb = new LibBackground(this.game, StaticSpriteVars.menu_bg, i * 400, j * 400, null);
                libraryBackgroundTiles.add(mb);
            }
        }
        
        // build all textures and associated events.
        // Build libitem container :
        LibItemContainer libItem = null;
        for (int i = 1; i < (FrameConst.FRM_WIDTH / LibItemContainer.SPRT_WH - 1); ++i) {
            for (int j = 1; j < (FrameConst.FRM_HEIGHT / LibItemContainer.SPRT_WH - 1); ++j) {
                libItem = new LibItemContainer(this.game, StaticSpriteVars.menu_item_container,
                    StaticSpriteVars.menu_item_container_hover, 
                    StaticSpriteVars.menu_item_container_active_spanwable,
                    StaticSpriteVars.menu_item_container_active_ignitable, 
                    i * LibItemContainer.SPRT_WH, j * LibItemContainer.SPRT_WH);
                libraryContainers.put(LibItemContainer.REF + libItem.INSTANCE_NUM, libItem);
            }
        }
    }
    
    public Map<String, LibItemContainer> getLibraryContainerEntities() {
        return libraryContainers;
    }
    
    public List<LibBackground> getLibraryBackgroundTiles() {
        return libraryBackgroundTiles;
    }
    
}
