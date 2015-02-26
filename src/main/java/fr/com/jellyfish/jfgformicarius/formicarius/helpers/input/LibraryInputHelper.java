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
package fr.com.jellyfish.jfgformicarius.formicarius.helpers.input;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.IconConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.characters.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.LibItemContainer;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.LibraryItemMouseObserver;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Spawnable;
import fr.com.jellyfish.jfgformicarius.formicarius.library.Library;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticFrameVars;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CursorUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.InputUtils;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 *
 * @author thw
 */
public class LibraryInputHelper extends AbstractInput {
    
    //<editor-fold defaultstate="collapsed" desc="varibales">
    /**
     * ollection of mouse observers, typically, lib menu items.
     */
    public final List<LibraryItemMouseObserver> mouseObservers;
    
    /**
     * Library mouse cursor rectangle representation.
     */
    private final Rectangle mouseCursorRECT = new Rectangle();
    
    /**
     * Library item container rectangle representation.
     */
    private final Rectangle entityRECT = new Rectangle();
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    public LibraryInputHelper(final Game game) {
        super(game);
        mouseObservers = new ArrayList<>();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Catch all mouse input and process with it.
     */
    @Override
    public void observeMouseInput() {
        
        // get mouse movement on x axis. We need to get it now, since
        // we can only call getDX ONCE! - secondary calls will yield 0, since
        // there haven't been any movement since last call.
        StaticFrameVars.mouseLibDX = Mouse.getDX();
        StaticFrameVars.mouseLibDY = Mouse.getDY();
        StaticFrameVars.mouseLibX = Mouse.getX();
        StaticFrameVars.mouseLibY = FrameConst.FRM_HEIGHT - Mouse.getY();
        final boolean mousePressed = Mouse.isButtonDown(0);
        
        // Observe input for library cursor collisions with library items.
        for (LibraryItemMouseObserver obs : mouseObservers) {

            mouseCursorRECT.setBounds(StaticFrameVars.mouseLibX, StaticFrameVars.mouseLibY,
                FrameConst.LIB_MOUSE_CURSOR_POINTER_WH, FrameConst.LIB_MOUSE_CURSOR_POINTER_WH);
            entityRECT.setBounds(obs.getObserverdX() + LibItemContainer.PADDING, 
                obs.getObserverdY() + LibItemContainer.PADDING, 
                LibItemContainer.SPRT_WH - (LibItemContainer.PADDING * 2), 
                LibItemContainer.SPRT_WH - (LibItemContainer.PADDING * 2));
                final boolean intersects = mouseCursorRECT.intersects(entityRECT);
            if (mousePressed && intersects) {
                obs.notityClickEvent();
                
                ////////////////////////////////////////////////////////////////
                // Swap Spawnable / Ignitable ref swap//////////////////////////
                final MainCharacter mh = (MainCharacter)
                    this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName());
                if (obs.getContainedEntity() instanceof Spawnable && !obs.isClicked()) {
                    mh.swapSpawnable(obs.getContainedEntity());
                    obs.getContainedEntity().setInGameActive(true);
                    obs.setClicked(true);
                }
                
                if (obs.getContainedEntity() instanceof Ignitable && !obs.isClicked()) {
                    mh.swapIgnitable(obs.getContainedEntity());
                    obs.getContainedEntity().setInGameActive(true);
                    obs.setClicked(true);
                }
                // End ref swap ////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////
                
            } else if (!mousePressed && intersects) {
                obs.notifyMouseEntered();
            } else if (!mousePressed && !intersects) {
                obs.notifyMouseExited(obs.getContainedEntity());
            }
        }
        
    }
    
    /**
     * Catch all keyboard input and process it.
     */
    @Override
    public void observeKeyBoardInput() {
          
        // if escape has been pressed, stop the game :
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Display.isCloseRequested()) {
            // Remove/Hide maenu board, then restart in game processing.
            InGameInputHelper.gamePaused = false;
        }
        
        // we delegate input checking to submethod since we want to check
        // for keyboard, mouse & controller
        final boolean leftPressed = InputUtils.hasInput(Keyboard.KEY_LEFT);
        final boolean rightPressed = InputUtils.hasInput(Keyboard.KEY_RIGHT);
        final boolean upPressed = InputUtils.hasInput(Keyboard.KEY_UP); 
        final boolean downPressed = InputUtils.hasInput(Keyboard.KEY_DOWN); 
        final boolean spacePressed = InputUtils.hasInput(Keyboard.KEY_SPACE);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_O)) { 
            // TODO : Some other event for out of menu and back in to game input.
            CursorUtils.appendNativeCurcor(IconConst.WAND_CURSOR2);
            InGameInputHelper.gamePaused = false;
            Library.visible = false;
        }
    }
    //</editor-fold>
    
}
