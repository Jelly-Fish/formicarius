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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.vegetation;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Callable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.SpawnCollidable;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class Tree extends AbstractEntity implements CollidableObject, Callable,
    SpawnCollidable {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static int occurs = 0;
    public static final String REF = "tree";
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    public Tree(final Game game, final Sprite sprtDefault, final int x, final int y) {
        super(game, sprtDefault, x, y, 
            MvtConst.STILL, Tree.REF);
        ++Tree.occurs;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public void collideWith(final AbstractEntity other) {
        
        if (!other.isInCollision()) {
            other.setInCollision(CollisionUtils.inCollision(this.getRectangle(other),
                other, CollidableObject.COLLIDABLE_OBJ_INNER_PADDING_4));
        }
    }

    @Override
    public void doLogic() { }

    @Override
    public void beforeRender(final boolean force) { }

    @Override
    public void afterRender(final boolean force) { 
        
        // Test for collision with MainCharacter. If top collision perform
        // simple add. If collision is on lower level .add(MainCharacter Index + 1)
        final MainCharacter mc = game.getEntityHelper().getMainCharacter();
        if (CollisionUtils.inCollision(this.getLowerRectangle(), mc.getRectangle())) {
            DrawingHelper.getInstance().getDrawableQueue().add(
                DrawingHelper.getInstance().getDrawableQueue().indexOf(mc), this);
        } else {
            DrawingHelper.getInstance().getDrawableQueue().add(this);
        }
    }
        
    @Override
    public Callable newInstance() {
        return this;
    }

    @Override
    public String getABSTRACTREF() {
        return this.ABSTRACT_REF;
    }

    @Override
    public int getWidth() {
        return sprite.getWidth();
    }

    @Override
    public int getHeight() {
        return sprite.getHeight();
    }
    
    @Override
    public Rectangle getRectangle() {
        
        return new Rectangle((int)x + (sprite.getWidth() / 4),
            (int)y + sprite.getHeight() - ((int)(sprite.getHeight() / 4) + 3), 
            sprite.getWidth() / 2, 7);
    }
    
    /**
     * 
     * @return 
     */
    private Rectangle getLowerRectangle() {
        
        return new Rectangle((int)x + 2, (int)y + 85, 
            sprite.getWidth() - 4, 15);
    }
    
    @Override
    public Rectangle getSpawnCollidableRectangle() {
        return this.getRectangle();
    }
    
    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        
        if (entity instanceof MainCharacter) {
            return this.getRectangle();
        } else {
            return new Rectangle((int)x + (sprite.getWidth() / 4),
                (int)y + sprite.getHeight() - (sprite.getHeight() / 4), 
                sprite.getWidth() / 2, sprite.getHeight() / 4);
        }
    }
    //</editor-fold>
    
}
