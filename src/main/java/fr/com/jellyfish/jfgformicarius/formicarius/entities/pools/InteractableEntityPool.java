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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.pools;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.characters.Frog;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.characters.Knight;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.characters.Mage;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.characters.Spider;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.RandomAccessible;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.RandomUtils;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author thw
 */
public class InteractableEntityPool extends RandomAccessible {
    
    /**
     * Singleton instance.
     */
    private static InteractableEntityPool instance;
    
    /**
     * Pool of complexe objects that are best to re-instantiate.
     */
    private final Map<String, AbstractEntity> randomEntityPool = new HashMap<>();
    
    /**
     * private constructor.
     */
    private InteractableEntityPool() { }
    
    /**
     * Singleton accessor.
     * @return InteractableEntityPool instance or new InteractableEntityPool.
     */
    public static InteractableEntityPool getInstance() {
        
        if (instance == null) {
            return new InteractableEntityPool();
        } else {
            return instance;
        }
    }
        
    @Override
    public Map<String, AbstractEntity> getRandomSubPool() {
        
        // Clear pool for new random pool :
        this.randomEntityPool.clear();
        // Init pools :
        initInteractableEntityPool();
        initComplexeInteractablePool();
        
        return this.randomEntityPool;
    }

    /**
     * Re-instantiate all complexe objects. 
     */
    private void initComplexeInteractablePool() {
        
        final int result = RandomUtils.randInt(1, 3);
        
        if (result == 2) {
            
            final Mage mage = new Mage (Game.getInstance(), StaticSpriteVars.mage,
                16, 
                RandomUtils.randInt(0, FrameConst.FRM_WIDTH - Mage.SPRT_W),
                220, MvtConst.LEFT, Mage.class.getSimpleName());
            randomEntityPool.put(mage.ABSTRACT_REF, mage);
        } else if (result == 3) {
        
            final Knight knight = new Knight(Game.getInstance(), StaticSpriteVars.golbez,
                20, 400, 220, MvtConst.LEFT, Knight.class.getSimpleName());
            randomEntityPool.put(knight.ABSTRACT_REF, knight);
        }
        
    }
    
    /**
     * Re-instantiate all complexe interactable "less complexe" objects. 
     */
    private void initInteractableEntityPool() {
        
        int frogMvt = MvtConst.LEFT;
        Frog frog = null;
        
        for (int i = 0; i < 3; i++) {
            frogMvt = Frog.occurs % 2 == 0 ? MvtConst.LEFT : MvtConst.RIGHT;
            frog = new Frog(Game.getInstance(), String.format(StaticSpriteVars.red_frog_left, 0),
                RandomUtils.randInt(0, FrameConst.FRM_WIDTH - Frog.SPRT_WH), 
                RandomUtils.randInt(0, ((FrameConst.FRM_HEIGHT - 100) / 100)) * 100,
                Frog.RED, frogMvt);
            randomEntityPool.put(frog.getABSTRACTRef(), frog);
        }
        
        /*Spider spider = new Spider(Game.getInstance(), 
            String.format(StaticSpriteVars.spider, 0), 100, 210);
        randomEntityPool.put(spider.ABSTRACT_REF, spider);*/
        
        Spider spider = new Spider(Game.getInstance(), 
            String.format(StaticSpriteVars.spider, 0), 
                RandomUtils.randInt(0, FrameConst.FRM_WIDTH - Spider.SPRT_WH),
                RandomUtils.randInt(0, FrameConst.FRM_HEIGHT - Spider.SPRT_WH));
        randomEntityPool.put(spider.ABSTRACT_REF, spider);
        
    }
    
}
