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
 * *****************************************************************************
 */
package fr.com.jellyfish.jfformicarius.formicarius.entities.tiles.status;

import fr.com.jellyfish.jfformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfformicarius.formicarius.entities.biologicals.MainCharacter;
import fr.com.jellyfish.jfformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfformicarius.formicarius.texture.ResizableSprite;
import fr.com.jellyfish.jfformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfformicarius.formicarius.utils.SpriteUtils;

/**
 *
 * @author thw
 */
public class StatusFrame extends AbstractEntity {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final int SPRT_W = 384;
    public static final int SPRT_H = 40;
    public static final String REF = "health_bar";
    
    /**
     * Padding values for health & stamina zone sprite drawing.
     */
    private final int healthValueSprite_padding_top = 10;
    private final int sprite_padding_left = 43;
    private final int staminaValueSprite_padding_top = 25;
    
    /**
     * Maximum width of health or stamina zone sprite.
     */
    private final int MAX_VALUE_WIDTH;
    
    /**
     * Damage or stamina loss ratio for calculating true damage or stamina loss 
     * value depending on main entity's health or stamina value and entity 
     * inflicting damage or stamina loss.
     * Ratio = main entity Maximum healthvalue | staminaValue / health bar's 
     * corresponding zone sprite's maximum width. 
     * True damage = ratio * damage | loss value.
     */
    private final int RATIO;

    /**
     * The animation frame of this entity.
     */
    private final Sprite frame;

    /**
     * Sprite that represents health value.
     */
    private final ResizableSprite healthFrame;
    
    /**
     * Sprite that represents stamina value.
     */
    private final ResizableSprite staminaFrame;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     *
     * @param game
     */
    public StatusFrame(final Game game) {
        super(game, null, (FrameConst.FRM_WIDTH_800 - StatusFrame.SPRT_W) / 2,
                12, MvtConst.STILL, StatusFrame.REF);

        // Build frame sprite :
        this.frame = SpriteUtils.getSprite(game.getTextureLoader(), StaticSpriteVars.status_bar_frame);
        this.healthFrame = SpriteUtils.getResizableSprite(game.getTextureLoader(),
                StaticSpriteVars.health_value);
        this.staminaFrame = SpriteUtils.getResizableSprite(game.getTextureLoader(),
                StaticSpriteVars.stamina_value); 
        
        this.sprite = this.frame;
        this.MAX_VALUE_WIDTH = this.healthFrame.getWidth();
        this.RATIO = this.MAX_VALUE_WIDTH / MainCharacter.MAX_HEALTH_VALUE;
        
        init();
        this.setAnimeUpdateRequired(true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     *
     */
    private void init() {
    }

    /**
     *
     * @param damageValue
     */
    public void notifyDamage(final float damageValue) {
        final float val = this.healthFrame.getDeffinedWidth();
        if (val > 0) {
            this.healthFrame.setDeffinedWidth(val - (damageValue * this.RATIO));
        }
    }
    
    /**
     * 
     * @param lossValue 
     */
    public void notifyStaminaLoss(final float lossValue) {
        final float val = this.staminaFrame.getDeffinedWidth();
        if (val > 0) {
            this.staminaFrame.setDeffinedWidth(val - (lossValue * this.RATIO));
        }
    }

    /**
     * Draw this entity and it's sub entities.
     */
    @Override
    public void draw() {
        healthFrame.draw((int) x + sprite_padding_left, (int) y + healthValueSprite_padding_top);
        staminaFrame.draw((int) x + sprite_padding_left, (int) y + staminaValueSprite_padding_top);
        frame.draw((int) x, (int) y);
    }

    @Override
    public void collideWith(final AbstractEntity other) {
    }

    @Override
    public void doLogic() {
    }

    @Override
    public void beforeRender(final boolean force) {
    }

    @Override
    public void afterRender(final boolean force) {
    }
    
    public ResizableSprite getStaminaFrame() {
        return staminaFrame;
    }

    //</editor-fold>

}
