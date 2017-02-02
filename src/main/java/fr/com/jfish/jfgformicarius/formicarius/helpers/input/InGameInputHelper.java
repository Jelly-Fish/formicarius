/*******************************************************************************
 * Copyright (c) 2014, Thomas.H Warner.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 *******************************************************************************/

package fr.com.jfish.jfgformicarius.formicarius.helpers.input;

import fr.com.jfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jfish.jfgformicarius.formicarius.entities.characters.MainCharacter;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticFrameVars;
import fr.com.jfish.jfgformicarius.formicarius.utils.InputUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 *
 * @author thw
 */
public class InGameInputHelper extends AbstractInput {
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    public InGameInputHelper(final Game game) {
        super(game);
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
        StaticFrameVars.mouseDX = Mouse.getDX();
        StaticFrameVars.mouseDY = Mouse.getDY();
        StaticFrameVars.mouseX = Mouse.getX();
        StaticFrameVars.mouseY = FrameConst.FRM_HEIGHT - Mouse.getY();
        
        if (!InGameInputHelper.gamePaused) {
            if (Mouse.isButtonDown(0)) {
                StaticFrameVars.mouseClickX = Mouse.getX();
                StaticFrameVars.mouseClickY = FrameConst.FRM_HEIGHT - Mouse.getY();
                this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).mouseClicked(
                    StaticFrameVars.mouseClickX, StaticFrameVars.mouseClickY);

            } else if (Mouse.isButtonDown(1)) {
                StaticFrameVars.mouseClickX = Mouse.getX();
                StaticFrameVars.mouseClickY = FrameConst.FRM_HEIGHT - Mouse.getY();
                this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).fire();
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
            Game.running = false;
        }
        
        // we delegate input checking to submethod since we want to check
        // for keyboard, mouse & controller
        final boolean leftPressed = InputUtils.hasInput(Keyboard.KEY_LEFT);
        final boolean rightPressed = InputUtils.hasInput(Keyboard.KEY_RIGHT);
        final boolean upPressed = InputUtils.hasInput(Keyboard.KEY_UP); 
        final boolean downPressed = InputUtils.hasInput(Keyboard.KEY_DOWN); 
        final boolean spacePressed = InputUtils.hasInput(Keyboard.KEY_SPACE);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_P)) { 
            InGameInputHelper.gamePaused = true;
            // Display menu board in OpenGL Display.
        }
        
        if (spacePressed) { 
            ////////////////////////////////////////////////////////////////////
            // Below, code for calling Catentity to join up with MainCharacter position :
            //this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).callEntity();
            ////////////////////////////////////////////////////////////////////
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).fire();
        }
        
        if (leftPressed) {
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).updateMvt(MvtConst.LEFT, true);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setHorizontalMovement(-MainCharacter.SPEED);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setAnimeUpdateRequired(true);
        } else if (rightPressed) {
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).updateMvt(MvtConst.RIGHT, true);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setHorizontalMovement(MainCharacter.SPEED);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setAnimeUpdateRequired(true);
        } else if (upPressed) {
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).updateMvt(MvtConst.UP, true);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setVerticalMovement(-MainCharacter.SPEED);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setAnimeUpdateRequired(true);
        } else if (downPressed) {
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).updateMvt(MvtConst.DOWN, true);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setVerticalMovement(MainCharacter.SPEED);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setAnimeUpdateRequired(true);
        } else {
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).updateMvt(
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).getCurrentMvt(), false);
            this.game.getEntityHelper().getMainEntities().get(MainCharacter.class.getSimpleName()).setAnimeUpdateRequired(false);
        }
        
    }
    //</editor-fold>
    
}
