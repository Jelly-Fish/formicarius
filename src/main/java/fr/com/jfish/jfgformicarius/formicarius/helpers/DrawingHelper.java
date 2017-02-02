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
package fr.com.jfish.jfgformicarius.formicarius.helpers;

import fr.com.jfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import java.util.LinkedList;

/**
 *
 * @author thw
 */
public class DrawingHelper {
    
    /**
     * Singleton instance.
     */
    private static DrawingHelper instance;
    
    /**
     * Queue of AbstractEntity classes entries.
     */
    private final LinkedList<AbstractEntity> drawableQueue;

    /**
     * constructor.
     */
    private DrawingHelper() {
        this.drawableQueue = new LinkedList<>();
    }
    
    /**
     * Draw game entities when game is a static or paused state.
     * @param drawGlobals draw global entities or not.
     */
    public void drawStaticGame(final boolean drawGlobals) {

        // Draw backgrounds first.
        for (AbstractEntity bg : 
                Game.getInstance().getEntityHelper().getTileEntities().values()) {
            bg.draw(); 
        }
        
        if (drawGlobals) {
            
            this.drawableQueue.addAll(0, 
                Game.getInstance().getEntityHelper().getStaticEntities().values());
            
            for (AbstractEntity obj : this.drawableQueue) {
                obj.draw();
            }
        }
        
        this.drawableQueue.clear();
    }
    
    /**
     * @return LinkedList<AbstractEntity> queue entries.
     */
    public LinkedList<AbstractEntity> getDrawableQueue() {
        return drawableQueue;
    }
    
    /**
     * Singleton accessor.
     * @return 
     */
    public static DrawingHelper getInstance() {
        
        if (instance != null) {
            return instance;
        } else {
            instance = new DrawingHelper();
            return instance;
        }
    }
    
}
