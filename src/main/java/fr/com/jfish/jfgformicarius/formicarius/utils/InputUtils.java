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

package fr.com.jfish.jfgformicarius.formicarius.utils;

import org.lwjgl.input.Keyboard;

/**
 *
 * @author thw
 */
public class InputUtils {
    
    /**
     * @param direction
     * @return
     */
    public static boolean hasInput(final int direction) {
        
        switch (direction) {
            case Keyboard.KEY_LEFT:
                return Keyboard.isKeyDown(Keyboard.KEY_LEFT);
                        //|| StaticFrameVars.mouseX < 0;
            case Keyboard.KEY_RIGHT:
                return Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
                        //|| StaticFrameVars.mouseX > 0;
            case Keyboard.KEY_UP:
                return Keyboard.isKeyDown(Keyboard.KEY_UP);
                        //|| StaticFrameVars.mouseY < 0;
            case Keyboard.KEY_DOWN:
                return Keyboard.isKeyDown(Keyboard.KEY_DOWN);
                        //|| StaticFrameVars.mouseY > 0;
            case Keyboard.KEY_SPACE:
                return Keyboard.isKeyDown(Keyboard.KEY_SPACE);
                        //|| Mouse.isButtonDown(0);
        }
        return false;
    }
    
}
