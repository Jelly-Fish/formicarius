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

package fr.com.jellyfish.jfformicarius.formicarius.utils;

import fr.com.jellyfish.jfformicarius.formicarius.texture.ResizableSprite;
import fr.com.jellyfish.jfformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfformicarius.formicarius.texture.TextureLoader;

/**
 *
 * @author thw
 */
public class SpriteUtils {
    
    /**
     * Create or get a sprite which displays the image that is pointed to in the
     * classpath by "ref"
     *
     * @param loader reference to the loader to use
     * @param ref reference to the image to load
     * @return A sprite that can be drawn onto the current graphics context.
     */
    public static Sprite getSprite(final TextureLoader loader, final String ref) {
        return new Sprite(loader, ref);
    }
    
    /**
     * Create or get a resizable sprite which displays the image that is pointed 
     * to in the classpath by "ref".
     *
     * @param loader reference to the loader to use
     * @param ref reference to the image to load
     * @return A ResizableSprite that can be drawn onto the current graphics context.
     */
    public static ResizableSprite getResizableSprite(final TextureLoader loader, final String ref) {
        return new ResizableSprite(loader, ref);
    }
    
}
