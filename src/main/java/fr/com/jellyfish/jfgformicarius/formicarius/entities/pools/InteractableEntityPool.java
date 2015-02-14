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

import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.Knight;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.Mage;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.Spider;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Interactable;
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
    private final Map<String, AbstractEntity> complexeInteractablePool = new HashMap<>();
    
    /**
     * private constructor.
     */
    private InteractableEntityPool() {
        initComplexeInteractablePool();
    }

    /**
     * Static pool of AbstractEntity classes that implement Interactable interface.
     * Is built staticly and is accessed from accessor like method 
     * getRandomSubPool(...), this method can only be accessed via Sigleton accessor.
     */
    private static final Map<String, AbstractEntity> interactablePool = new HashMap<>();
    static
    {
        /*int frogMvt = Frog.occurs % 2 == 0 ? MvtConst.LEFT : MvtConst.RIGHT;
        interactablePool.put(
            Frog.REF + Frog.RED + Frog.occurs,
            new Frog(Game.getInstance(), String.format(StaticSpriteVars.red_frog_left, 0), 400, 300,
                Frog.RED, frogMvt));
        frogMvt = Frog.occurs % 2 == 0 ? MvtConst.LEFT : MvtConst.RIGHT;
        interactablePool.put(
            Frog.REF + Frog.RED + Frog.occurs,
            new Frog(Game.getInstance(), String.format(StaticSpriteVars.red_frog_left, 0), 100, 100,
                Frog.RED, frogMvt));
        frogMvt = Frog.occurs % 2 == 0 ? MvtConst.LEFT : MvtConst.RIGHT;
        interactablePool.put(
            Frog.REF + Frog.RED + Frog.occurs,
            new Frog(Game.getInstance(), String.format(StaticSpriteVars.red_frog_left, 0), 600, 500,
                Frog.RED, frogMvt));
        
        Spider spider = new Spider(Game.getInstance(), 
            String.format(StaticSpriteVars.spider, 0), 100, 210);
        interactablePool.put(spider.ABSTRACT_REF, spider);*/
        
        Spider spider = new Spider(Game.getInstance(), 
            String.format(StaticSpriteVars.spider, 0), 400, 420);
        interactablePool.put(spider.ABSTRACT_REF, spider);
    }

    @Override
    public Map<String, AbstractEntity> getRandomSubPool(final int maxRandValue) {
        
        // TODO : finish code. Randomly access a sub map of this main 
        // Map<String, AbstractEntity>. Also scramble and reset x coordinates.
        // Scramble x coordinates :
        for (AbstractEntity entity : InteractableEntityPool.interactablePool.values()) {
            entity.setX(RandomUtils.randInt(entity.sprite.getWidth(), maxRandValue));
            ((Interactable)entity).clear();
        }
        
        final Map<String, AbstractEntity> result = new HashMap<>();
        result.putAll(InteractableEntityPool.interactablePool);
        initComplexeInteractablePool();
        result.putAll(complexeInteractablePool);
        
        return result;
    }
    
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

    /**
     * Re-instantiate all complexe objects. 
     */
    private void initComplexeInteractablePool() {
        
        // First clear hash map :
        this.complexeInteractablePool.clear();
        
        /*final Mage mage = new Mage (Game.getInstance(), StaticSpriteVars.mage,
            16, 400, 220, MvtConst.LEFT, Mage.class.getSimpleName());
        complexeInteractablePool.put(mage.ABSTRACT_REF, mage);*/
        
        final Knight knight = new Knight(Game.getInstance(), StaticSpriteVars.golbez,
            20, 400, 220, MvtConst.LEFT, Knight.class.getSimpleName());
        complexeInteractablePool.put(knight.ABSTRACT_REF, knight);
        
    }
    
}
