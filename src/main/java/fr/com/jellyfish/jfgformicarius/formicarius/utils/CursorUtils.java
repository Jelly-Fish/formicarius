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

package fr.com.jellyfish.jfgformicarius.formicarius.utils;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.IconConst;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

/**
 *
 * @author thw
 */
public class CursorUtils {

    private static final int MIN_INT_BUF_ALLOCATION = 16384;
    
    /**
     * Returns a new Cursor.
     * @param bufimg
     * @return Cursor intance
     * @throws java.io.IOException 
     * @throws org.lwjgl.LWJGLException 
     */
    public static Cursor buildCursor(final BufferedImage bufimg) throws IOException, LWJGLException {

        // size of each cursor and amount of different cursor images
        final int cursorWidth = 32;
        final int cursorHeight = 32;
        final int numImages = 1;

        try {
            // load cursor image into BufferedImage 
            int[] rgbs = new int[cursorWidth * cursorHeight];

            // set up IntBuffers
            IntBuffer imageBuffer = IntBuffer.allocate(MIN_INT_BUF_ALLOCATION);
            //bufimg.getWidth() * bufimg.getHeight() / 16 * numImages);
            //IntBuffer delays = IntBuffer.allocate(MIN_INT_BUF_ALLOCATION);

            // put cursor images and delays into IntBuffers
            for (int y = 0; y < bufimg.getHeight() / cursorHeight; y++) {
                for (int x = 0; x < bufimg.getWidth() / cursorWidth; x++) {
                    if (y * (bufimg.getWidth() / cursorWidth) + x < numImages) {
                        imageBuffer.put(bufimg.getRGB(x * cursorWidth, y * cursorHeight, cursorWidth, cursorHeight, rgbs, 0, cursorWidth));
                        //delays.put(0);
                    }
                }
            }

            // rewind IntBuffers
            imageBuffer.flip();
            imageBuffer.rewind();
            //delays.rewind();
            
            // return cursor
            return new Cursor(cursorWidth, cursorHeight, 0, cursorWidth - 1, numImages, imageBuffer, null);
            
        } catch (final BufferOverflowException boe) {
            Logger.getLogger(CursorUtils.class.getName()).log(Level.SEVERE, null, boe);
            return null;
        } catch (final IllegalArgumentException iae) {
            Logger.getLogger(CursorUtils.class.getName()).log(Level.SEVERE, null, iae);
            return null;
        }
    }
    
    /**
     * Append a new cursor to in game mouse cursor.
     * @param path Path to ressource image or icon.
     */
    public static void appendNativeCurcor(final String path) {
        try {
            // grab the mouse, dont want that hideous cursor when we're playing!
            // Mouse.setGrabbed(true); << for NOT displaing mouse in Frame.
            BufferedImage bi = ImageIO.read(new File(path));
            Mouse.setNativeCursor(CursorUtils.buildCursor(bi));
        } catch (final IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final LWJGLException ex) {
            Logger.getLogger(CursorUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
