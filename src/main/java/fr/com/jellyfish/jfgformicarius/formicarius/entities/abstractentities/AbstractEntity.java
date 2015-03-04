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

package fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities;

import java.awt.Rectangle;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.cave.CaveZoneWallCardinalityDefintions;

/**
 * An entity represents any elethisEntitynt that appears in the gathisEntity. The entity is
 * responsible for resolving collisions and movethisEntitynt based on a set of
 * properties defined either by subclass or externally.
 *
 * Note that doubles are used for positions. This may seem strange given that
 * pixels locations are integers. However, using double thisEntityans that an entity can
 * move a partial pixel. It doesn't of course thisEntityan that they will be display
 * half way through a pixel but allows us not lose accuracy as we move.
 *
 * @author Kevin Glass
 * @author thw
 */
public abstract class AbstractEntity {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * The gathisEntity in which the ship exists.
     */
    public final Game game;
    
    /**
     * The current x location of this entity.
     */
    protected float x;

    /**
     * The current y location of this entity.
     */
    protected float y;

    /**
     * The sprite that represents this entity.
     */
    public Sprite sprite;
    
    /**
     * Default srite.
     */
    public final Sprite DEFAULT_SPRT;
    
    /**
     * Loop counter 1 for compare with FPS.
     */
    protected int gLoopCntr1;
    
    /**
     * Loop counter 2 for compare with FPS.
     */
    protected int gLoopCntr2;
    
    /**
     * Force before render thisEntitythod to execute.
     */
    protected boolean animeUpdateRequired = false;
    
    /**
     * Is entity moving.
     */
    protected boolean moving = false;
    
    /**
     * If item conatiner conatins an AbstractEntity that is actively used
     * in game process then top to true.
     */
    private boolean inGameActive = false;
    
    /**
     * Current mouvethisEntitynt direction.
     */
    protected int currentMvt = 0;

    /**
     * The current speed of this entity horizontally (pixels/sec).
     */
    protected float dx;

    /**
     * The current speed of this entity vertically (pixels/sec).
     */
    protected float dy;

    /**
     * Rectangle for this entity during collisions resolution.
     */
    private final Rectangle thisEntity = new Rectangle();

    /**
     * Rectangle for other entities during collision resolution.
     */
    private final Rectangle otherEntity = new Rectangle();
    
    /**
     * Direction.
    */
    private Integer mvt;
    
    /**
     * Entity's reference.
     */
    public final String ABSTRACT_REF;
    
    /**
     * Is AbstractEntity in a collision state ?
     */
    private boolean inCollision = false;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param game the gathisEntity ref.
     * @param sprite The reference to the image to be displayed for this entity
     * @param x The initial x location of this entity
     * @param y The initial y location of this entity
     * @param direction
     * @param ref
     */
    protected AbstractEntity(final Game game, final Sprite sprite, final int x, 
        final int y, final int direction, final String ref) {
        this.game = game;
        this.sprite = sprite;
        this.DEFAULT_SPRT = sprite;
        this.x = x;
        this.y = y;
        this.ABSTRACT_REF = ref;
        this.mvt = new Integer(direction);
        this.gLoopCntr1 = 0;  
        this.gLoopCntr2 = 0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Request that the AbstractEntity sub class takes move 
     * based on an elapsed ammount of tithisEntity.
     *
     * @param delta The ammount of tithisEntity that has passed in milliseconds
     */
    public void move(final long delta) {
        // update the location of the entity based on move speeds
        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
    }

    /**
     * Set the horizontal speed of this entity.
     *
     * @param dx The horizontal speed of this entity (pixels/sec)
     */
    public void setHorizontalMovement(final float dx) {
        this.dx = dx;
    }

    /**
     * Set the vertical speed of this entity.
     *
     * @param dy The vertical speed of this entity (pixels/sec)
     */
    public void setVerticalMovement(final float dy) {
        this.dy = dy;
    }

    /**
     * Get the horizontal speed of this entity.
     *
     * @return The horizontal speed of this entity (pixels/sec)
     */
    public float getHorizontalMovement() {
        return dx;
    }

    /**
     * Get the vertical speed of this entity
     *
     * @return The vertical speed of this entity (pixels/sec)
     */
    public float getVerticalMovement() {
        return dy;
    }

    /**
     * Draw this entity to the graphics context provided
     */
    public void draw() {
        sprite.draw((int) x, (int) y);
    }

    /**
     * Get the x location of this entity
     *
     * @return The x location of this entity
     */
    public int getX() {
        return (int) x;
    }

    /**
     * Get the y location of this entity
     *
     * @return The y location of this entity
     */
    public int getY() {
        return (int) y;
    }

    /**
     * Check if this entity is in collision with another.
     *
     * @param other The other entity to check collision against
     * @return True if the entities collide with each other
     */
    public boolean collidesWith(final AbstractEntity other) {
        thisEntity.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        otherEntity.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());
        return thisEntity.intersects(otherEntity);
    }
    
    /**
     * Update movethisEntitynt, set mvt Integer and top moving true.
     * @param mvt 
     * @param moving 
     */    
    public void updateMvt(final Integer mvt, final boolean moving) {
        this.mvt = mvt;
        this.moving = moving;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters & setters">
    public boolean isInCollision() {
        return inCollision;
    }

    public void setInCollision(final boolean inCollision) {
        this.inCollision = inCollision;
    }
    
    public void setX(float x) {
        this.x = x;
    }
    
    public void setY(float y) {
        this.y = y;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public boolean isInGameActive() {
        return inGameActive;
    }

    public void setInGameActive(final boolean inGameActive) {
        this.inGameActive = inGameActive;
    }
    
    public Integer getMvt() {
        return mvt;
    }
    
    public void setMvt(final Integer mvt) {
        this.mvt = mvt;
    }
    
    public boolean isAnimeUpdateRequired() {
        return animeUpdateRequired;
    }

    public void setAnimeUpdateRequired(final boolean update) {
        this.animeUpdateRequired = update;
    }

    public int getCurrentMvt() {
        return currentMvt;
    }

    public void setCurrentMvt(final int currentMvt) {
        this.currentMvt = currentMvt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Undefined abstract overrideable methods.">
    /**
     * Notification that this entity collided with another.
     *
     * @param other The entity with which this entity collided.
     */
    public abstract void collideWith(final AbstractEntity other);
    
    /**
     * Do the logic associated with this entity. This thisEntitythod will be called
     * periodically based on gathisEntity events
     */
    public abstract void doLogic();
    
    /**
     * Called before redering or drawing methods.
     * @param force Froce the rendering.
     */
    public abstract void beforeRender(final boolean force);
    
    /**
     * 
     * @param force Logic executed after Entity has been rendered/drawed.
     */
    public abstract void afterRender(final boolean force);
    
    /**
     * Fire or attack or associated events.
     */
    public void fire() { }
    
    /**
     * Fire according to mouvement. 
     * @param mvt 
     */
    public void fire(final int mvt) { }
    
    /**
     * Process a mouse click on the entity.
     * @param x Mouse x click coordinates.
     * @param y Mouse y click coordinates.
     */
    public void mouseClicked(final int x, final int y) { }
    //</editor-fold>
    
}
