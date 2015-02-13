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
package fr.com.jellyfish.jfformicarius.formicarius.library;

import fr.com.jellyfish.jfformicarius.formicarius.constants.IconConst;
import fr.com.jellyfish.jfformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfformicarius.formicarius.entities.biologicals.MainCharacter;
import fr.com.jellyfish.jfformicarius.formicarius.entities.events.Axe;
import fr.com.jellyfish.jfformicarius.formicarius.entities.events.BloodyDagger;
import fr.com.jellyfish.jfformicarius.formicarius.entities.events.EvilDarkFog;
import fr.com.jellyfish.jfformicarius.formicarius.entities.events.EvilFog;
import fr.com.jellyfish.jfformicarius.formicarius.entities.events.IceDagger;
import fr.com.jellyfish.jfformicarius.formicarius.entities.events.PlasmaBall;
import fr.com.jellyfish.jfformicarius.formicarius.entities.events.SpellRing;
import fr.com.jellyfish.jfformicarius.formicarius.entities.tiles.LibBackground;
import fr.com.jellyfish.jfformicarius.formicarius.entities.tiles.LibItemContainer;
import fr.com.jellyfish.jfformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfformicarius.formicarius.helpers.entities.LibraryEntityHelper;
import fr.com.jellyfish.jfformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfformicarius.formicarius.utils.CursorUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thw
 */
public class Library {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * Main Game instance reference.
     */
    private Game game;

    /**
     * Library entity helper.
     */
    private LibraryEntityHelper entityHelper;

    /**
     * Count of items contained in library.
     */
    public static int itemCount = 0;

    /**
     * Library visibility.
     */
    public static boolean visible = false;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public Library() {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Initialize library.
     *
     * @param game
     */
    public void init(final Game game) {
        this.game = game;
        this.entityHelper = new LibraryEntityHelper(game);
    }
    
    /**
     * Build library content by appending all necessary AbstractEntity instances.
     */
    public void build() {
        ////////////////////////////////////////////////////////////////////////
        // TODO : This code must be redirected to helper or utils class...
        // Append all items to library :
        final MainCharacter mh = 
            (MainCharacter)this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName());
        appendLibItem(BloodyDagger.class.getSimpleName(), (AbstractEntity)mh.getIgnitable(), true, true);
        
        // Add quakering to library :
        appendLibItem(SpellRing.class.getSimpleName(), (AbstractEntity)mh.getSpawnable(), true, true);
        
        // Add ice dagger to library :
        appendLibItem(IceDagger.class.getSimpleName(), new IceDagger(this.game, 0, 0, 1, 
            IceDagger.class.getSimpleName()), false, true);
        
        // Add plasma ball to library :
        appendLibItem(PlasmaBall.class.getSimpleName(), new PlasmaBall(this.game, 0, 0, 4, 1, 
            PlasmaBall.class.getSimpleName()), false, true);
        
        // Add evil fog 1 to library :
        appendLibItem(EvilFog.class.getSimpleName(), new EvilFog(this.game, 
            0, 0, StaticSpriteVars.evil_fog1_lib_icon,
            EvilFog.class.getSimpleName(), StaticSoundVars.spell5), 
            false, true);
        
        // Add evil dark fog to library :
        appendLibItem(EvilDarkFog.class.getSimpleName(), new EvilDarkFog(this.game, 0, 0, 
            StaticSpriteVars.evil_fog2_lib_icon, 
            EvilDarkFog.class.getSimpleName() + EvilDarkFog.occurs, StaticSoundVars.spell6), 
            false, true);
        
        // Add Axe to library :
        appendLibItem(Axe.class.getSimpleName(), new Axe(this.game, 0, 0, StaticSpriteVars.axe1, 
            MvtConst.DOWN, Axe.class.getSimpleName()),
            false, true);
        ////////////////////////////////////////////////////////////////////////
        System.out.println("lib built with success.");
        Logger.getLogger(Library.class.getName()).log(Level.INFO, null, "lib built with success.");
    }

    /**
     * Draw or render Library entities and events.
     */
    public void libraryRendering() {

        if (!Library.visible) {
            CursorUtils.appendNativeCurcor(IconConst.LIB_SELECT2);
            Library.visible = true;
        }

        // Main game must be drawn to have paused game background effect
        // when library is rendered.
        DrawingHelper.getInstance().drawStaticGame(true);

        for (LibBackground lib : this.entityHelper.getLibraryBackgroundTiles()) {
            lib.draw();
        }

        for (LibItemContainer lib : this.entityHelper.getLibraryContainerEntities().values()) {

            lib.draw();
            if (!lib.isEmpty()) {
                lib.getContainedEntity().DEFAULT_SPRT.draw(
                        lib.getX() + ((LibItemContainer.SPRT_WH
                        - lib.getContainedEntity().DEFAULT_SPRT.getWidth()) / 2),
                        lib.getY() + ((LibItemContainer.SPRT_WH
                        - lib.getContainedEntity().DEFAULT_SPRT.getHeight()) / 2)
                );
            }
        }
    }

    /**
     * @param entity to add to ilbrary
     * @param mapKey entity's key for origin/game map querying.
     * @param inGameActive is item activelyused in game ?
     * @param selectable
     */
    public void appendLibItem(final String mapKey, final AbstractEntity entity,
        final boolean inGameActive, final boolean selectable) {

        if (entity != null) {
            final LibItemContainer container = this.entityHelper.getLibraryContainerEntities().get(
                LibItemContainer.REF + Library.itemCount);
            entity.setInGameActive(inGameActive);
            container.setContainedLibEntity(entity);
            container.setSelectable(selectable);
            
            ////////////////////////////////////////////////////////////////////
            // TODO : is this necessary ? Either it is in every case ?
            // Either it is never usefull ???? Re-think this and classes used to
            // store this kind of data...
            if (entity.isInGameActive()) {
                //TODO : not yet used in drawing processus, use or not ???
                this.entityHelper.activeLibItems.put(entity.ABSTRACT_REF,
                        new ActiveLibraryItem(mapKey, entity.ABSTRACT_REF, container));
            }
            ////////////////////////////////////////////////////////////////////

            // Finally :
            ++Library.itemCount;
        }
    }
    //</editor-fold>

}
