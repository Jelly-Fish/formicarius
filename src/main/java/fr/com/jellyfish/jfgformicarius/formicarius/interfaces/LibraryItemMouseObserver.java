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
package fr.com.jellyfish.jfgformicarius.formicarius.interfaces;

import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;

/**
 *
 * @author thw
 */
public interface LibraryItemMouseObserver {
    
    /**
     * @return X position of library Item.
     */
    public int getObserverdX();
    
    /**
     * @return Y position of library Item.
     */
    public int getObserverdY();
    
    /**
     * @param x current mouse X position.
     * @param y current mouse Y position.
     */
    public void notifyMouseXY(final int x, final int y);
    
    /**
     * Notify observed that click event has occured on it.
     */
    public void notityClickEvent();
    
    /**
     * Mouse has entered item witout event.
     */
    public void notifyMouseEntered();
    
    /**
     * Mouse is not hovering item or out of hovering event.
     * @param entity
     */
    public void notifyMouseExited(final AbstractEntity entity);
    
    /**
     * Get library item cainter's contained AbstractEntity sub class.
     * @return AbstractEntity
     */
    public AbstractEntity getContainedEntity();
    
    /**
     * Has been clicked ?
     * @return 
     */
    public boolean isClicked();

    /**
     * Set clicked event active.
     * @param clicked 
     */
    public void setClicked(boolean clicked);
    
}
