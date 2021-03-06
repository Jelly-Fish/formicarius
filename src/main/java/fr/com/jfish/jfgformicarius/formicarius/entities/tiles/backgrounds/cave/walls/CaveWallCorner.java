/**
 * *****************************************************************************
 * Copyright (c) 2014 - 2015, Thomas.H Warner. All rights reserved.
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
package fr.com.jfish.jfgformicarius.formicarius.entities.tiles.backgrounds.cave.walls;

import fr.com.jfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jfish.jfgformicarius.formicarius.utils.CollisionUtils;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class CaveWallCorner extends CaveWall {

    /**
     * Dimension of the full tile including alpha.
     */
    public static final int SPRT_WH = 50;
        
    /**
     * Rectangles that deffine this complexe object.
     */
    private final Rectangle[] rectangles;
    
    public CaveWallCorner(final Sprite sprt, final int x, final int y, final String ref,
        final Rectangle[] rectangles) {
        super(sprt, x, y, ref);
        
        this.rectangles = rectangles;
    }
    
    @Override
    public void collideWith(final AbstractEntity other) {
        
        if (!other.isInCollision()) {
            
            for (int i = 0; i < this.rectangles.length; ++i) {
                if (CollisionUtils.inCollision(this.rectangles[i], other, 4)) {
                    other.setInCollision(true);
                    return;
                }
            }
            
            other.setInCollision(false);
        }
    }
    
}
