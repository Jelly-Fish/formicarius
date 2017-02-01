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

package fr.com.jellyfish.jfgformicarius.formicarius.texture;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 * Implementation of sprite that uses an OpenGL quad and a texture to render a
 * given image to the screen.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class Sprite {

    /**
     * The texture that stores the image for this sprite
     */
    protected Texture texture;

    /**
     * The width in pixels of this sprite
     */
    protected int width;

    /**
     * The height in pixels of this sprite
     */
    protected int height;

    /**
     * Create a new sprite from a specified image.
     *
     * @param loader the texture loader to use
     * @param ref A reference to the image on which this sprite should be based
     */
    public Sprite(final TextureLoader loader, final String ref) {
        try {
            texture = loader.getTexture(ref);
            width = texture.getImageWidth();
            height = texture.getImageHeight();
        } catch (final IOException ioe) {
            Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ioe);
            System.exit(-1);
        }
    }
    
    /**
     * Get the width of this sprite in pixels
     *
     * @return The width of this sprite in pixels
     */
    public int getWidth() {
        return texture.getImageWidth();
    }

    /**
     * Get the height of this sprite in pixels
     *
     * @return The height of this sprite in pixels
     */
    public int getHeight() {
        return texture.getImageHeight();
    }

    /**
     * Draw the sprite at the specified location
     *
     * @param x The x location at which to draw this sprite
     * @param y The y location at which to draw this sprite
     */
    public void draw(int x, int y) {
        // store the current model matrix
        glPushMatrix();

        // bind to the appropriate texture for this sprite
        texture.bind();

        // translate to the right location and prepare to draw
        glTranslatef(x, y, 0);

        // draw a quad textured to match the sprite
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(0, 0);

            glTexCoord2f(0, texture.getHeight());
            glVertex2f(0, height);

            glTexCoord2f(texture.getWidth(), texture.getHeight());
            glVertex2f(width, height);

            glTexCoord2f(texture.getWidth(), 0);
            glVertex2f(width, 0);
            
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }
        glEnd();

        // restore the model view matrix to prevent contamination
        glPopMatrix();
    }
}
