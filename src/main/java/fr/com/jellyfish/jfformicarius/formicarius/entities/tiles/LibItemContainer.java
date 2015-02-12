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
package fr.com.jellyfish.jfformicarius.formicarius.entities.tiles;

import fr.com.jellyfish.jfformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfformicarius.formicarius.interfaces.LibraryItemMouseObserver;
import fr.com.jellyfish.jfformicarius.formicarius.interfaces.Spawnable;
import fr.com.jellyfish.jfformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfformicarius.formicarius.utils.SpriteUtils;

/**
 *
 * @author thw
 */
public class LibItemContainer extends AbstractEntity implements LibraryItemMouseObserver {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public final static String REF = "libitem";
    
    /**
     * Is library item selectable ?
     */
    private boolean selectable = false;
    
    /**
     * Instances count of all libitems.
     */
    public static int instanceNum = 0;
    
    /**
     * Container's Sprtie width & height : is square.
     */
    public final static int SPRT_WH = 50;
    
    /**
     * Padding of entities in the container layout.
     */
    public static final int PADDING = 4;
    
    /**
     * Is container containing an AbstractEntity.
     */
    private boolean empty = true;
    
    /**
     * This instance's count number.
     */
    public final int INSTANCE_NUM;
    
    /**
     * Contained AbstractEntity instance's Sprite.
     */
    private AbstractEntity containedEntity = null;

    /**
     * Contained AbstractEntity instance REF.
     */
    private String conatinedRef = null;
    
    /**
     * If mouse event occurs on this item container, then top to true to avoid
     * looping on notification and sending multiple calls whenonly one is 
     * necessary for processing event.
     */
    public boolean eventTriggered;
    
    /**
     * Sprite used when when mouse is hovering library Item.
     */
    public final Sprite hoverSprt;
    
    /**
     * Default sprite for drawing.
     */
    public final Sprite defaultSprt;
    
    /**
     * Sprtite for in game active Spawnable.class elements, these must be
     * displayed as so in the library's UI.
     */
    public final Sprite activeSpawnableContentSprt;
    
    /**
     * Sprtite for in game active Ignitable.class elements, these must be
     * displayed as so in the library's UI.
     */
    public final Sprite activeIgnitableContentSprt;
    
    /**
     * Has item been clicked ?
     */
    private boolean clicked = false;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     * @param game
     * @param sprtDefault
     * @param hoverSprt
     * @param activeSpwanableSprt
     * @param activeIgnitableSprt
     * @param x 
     * @param y 
     */
    public LibItemContainer(final Game game, final String sprtDefault, 
        final String hoverSprt, final String activeSpwanableSprt, final String activeIgnitableSprt, 
        final int x, final int y) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), sprtDefault), 
            x, y, 0, LibItemContainer.REF);
        this.INSTANCE_NUM = LibItemContainer.instanceNum;
        this.hoverSprt = SpriteUtils.getSprite(game.getTextureLoader(), hoverSprt);
        this.activeSpawnableContentSprt = SpriteUtils.getSprite(game.getTextureLoader(), activeSpwanableSprt);
        this.activeIgnitableContentSprt = SpriteUtils.getSprite(game.getTextureLoader(), activeIgnitableSprt);
        this.defaultSprt = sprite;
        ++LibItemContainer.instanceNum;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public void collideWith(final AbstractEntity other) {}

    @Override
    public void doLogic() {}

    @Override
    public void beforeRender(final boolean force) {}

    @Override
    public void afterRender(final boolean force) {}

    /**
     * Set library item container's conatined AbstractEntity Sprite and REF.
     * @param entity
     */
    public void setContainedLibEntity(final AbstractEntity entity) {
        
        this.containedEntity = entity;
        this.empty = false;
        this.conatinedRef = entity.ABSTRACT_REF;
        
        // If Lib item contains something it must be observed.
        this.game.getLibraryInputHelper().mouseObservers.add(this);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Mouse observer impl methods">
    @Override
    public AbstractEntity getContainedEntity() {
        return containedEntity;
    }

    @Override
    public void notifyMouseXY(final int x, final int y) {}

    @Override
    public void notityClickEvent() {
        
    }
    
    @Override
    public void notifyMouseEntered() { 
        if (this.selectable) { sprite = hoverSprt; }
    }

    @Override
    public void notifyMouseExited(final AbstractEntity entity) {
        if (containedEntity.isInGameActive() && this.selectable) {
            if (entity instanceof Spawnable) {
                sprite = activeSpawnableContentSprt;
            } else if (entity instanceof Ignitable) {
                sprite = activeIgnitableContentSprt;
            }
        } else {
            sprite = defaultSprt;
        }
        // Click event, if occured, is know out of date :
        // See observeMouseInput() in LibraryInputHelper.
        this.clicked = false;
    }
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter & setters">
    public void setSelectable(final boolean selectable) {
        this.selectable = selectable;
    }
    
    public boolean isSelectable() {
        return this.selectable;
    }
    
    @Override
    public boolean isClicked() {
        return clicked;
    }

    @Override
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
    
    public boolean isEmpty() {
        return empty;
    }
    
    public String getConatinedRef() {
        return conatinedRef;
    }
    
    public Sprite getActiveContentSprt() {
        return activeSpawnableContentSprt;
    }
        
    @Override
    public int getObserverdX() { return (int)x; }

    @Override
    public int getObserverdY() { return (int)y; }
    //</editor-fold>
    
}
