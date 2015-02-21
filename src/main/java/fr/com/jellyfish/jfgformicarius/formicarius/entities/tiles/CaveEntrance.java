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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class CaveEntrance extends AbstractEntity implements CollidableObject {
    
    /**
     * Sprite size.
     */
    public static final int SPRT_WH = 60;
    
    /**
     * 
     */
    public static final String REF = "cave_entrance";
    
    /**
     * constructor.
     * @param game
     * @param sprite
     * @param x
     * @param y
     */
    public CaveEntrance(final Game game, final Sprite sprite, final int x, final int y) {
        super(game, sprite, x, y, MvtConst.STILL, CaveEntrance.class.getSimpleName());
    }
    
    @Override
    public void collideWith(final AbstractEntity other) { 
        if (other instanceof MainCharacter && 
            ((MainCharacter)other).getRectangle().intersects(this.getRectangle())) {
            /**
             * Then change environment for cave scenarios. 
             * Affect new TransitionAction class to MainCharacter.
             * @see TransitionAction
             * @see MainCharacterZoneHelper
             */
            
        }
    }

    @Override
    public void doLogic() { }

    @Override
    public void beforeRender(final boolean force) { }

    @Override
    public void afterRender(final boolean force) { 
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getX() + (CaveEntrance.SPRT_WH / 4),
            this.getY() + (CaveEntrance.SPRT_WH / 4), SPRT_WH / 2, SPRT_WH / 2);
    }

    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        return null;
    }
    
}
