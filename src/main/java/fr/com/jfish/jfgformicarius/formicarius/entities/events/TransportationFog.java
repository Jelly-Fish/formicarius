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

package fr.com.jfish.jfgformicarius.formicarius.entities.events;

import fr.com.jfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jfish.jfgformicarius.formicarius.entities.characters.MainCharacter;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import fr.com.jfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jfish.jfgformicarius.formicarius.interfaces.SpawnCollidable;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class TransportationFog extends EvilFog {
    
    /**
     * Observed AbstractEntity instance.
     */
    final AbstractEntity observed;
    
    /**
     * Constructor.
     * @param game
     * @param x
     * @param y
     * @param iconPath
     * @param abstractRef
     * @param soundEffectRef 
     */
    public TransportationFog(final Game game, final int x, final int y, 
        final String iconPath, final String abstractRef, final int soundEffectRef) {
        super(game, x, y, iconPath, abstractRef, soundEffectRef);
        
        observed = (AbstractEntity)observable;
        
        this.frames = new Sprite[TransportationFog.FRAME_COUNT];
        
        for (int i = 0; i < this.frames.length; ++i) {
            this.frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), 
                String.format(StaticSpriteVars.transportation_fog, i));
        }
        
        this.staminaValue = 13.0f;
    }
    
    @Override
    public float spawn(final int x, final int y) {
        
        // if MainCharacter will be in a collision state do not perform basicSpawn.
        Rectangle rect = new Rectangle(x - (MainCharacter.SPRT_W / 2),
            y - (MainCharacter.SPRT_H / 2), MainCharacter.SPRT_W, MainCharacter.SPRT_H);

        for (AbstractEntity obj : game.getEntityHelper().getObjectEntities().values()) {
            if (obj instanceof SpawnCollidable && CollisionUtils.inFullCollision(rect, 
                ((SpawnCollidable)obj).getSpawnCollidableRectangle())) {
                game.getSoundManager().playEffect(StaticSoundVars.error1);
                return 0.0f;
            }
        }
        
        this.basicSpawn(x, y);
        return this.staminaValue;
    }

    @Override
    public void observed() {
        // TODO : fix collision possibilities when triggered on CollidableObject
        observed.setX(x + ((TransportationFog.SPRT_WH - MainCharacter.SPRT_W) / 2)); 
        observed.setY(y + ((TransportationFog.SPRT_WH - MainCharacter.SPRT_H) / 2));
    }
    
    @Override
    public void collideWith(final AbstractEntity other) { }
    
    @Override
    public void afterRender(final boolean force) {
        DrawingHelper.getInstance().getDrawableQueue().addLast(this);
    }
    
}
