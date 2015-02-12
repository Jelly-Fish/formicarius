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
package fr.com.jellyfish.jfformicarius.formicarius.entities.biologicals;

import fr.com.jellyfish.jfformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfformicarius.formicarius.exceptions.RandonMvtSimulationException;
import fr.com.jellyfish.jfformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfformicarius.formicarius.helpers.AIMvtHelper;
import fr.com.jellyfish.jfformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfformicarius.formicarius.interfaces.Observer;
import fr.com.jellyfish.jfformicarius.formicarius.interfaces.XYObservable;
import fr.com.jellyfish.jfformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfformicarius.formicarius.utils.SpriteUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thw
 */
public class Cat extends AbstractEntity implements Observer {
  
    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final float SPEED = 180;
    public static final String BLACK = "black";
    public static final String WHITE = "white";
    public final int SPRT_WH = 32;
    public static final int STILLNESS_FACTOR = 2;
    private int frameVal = 0;
    private int[] masterXY = new int[2];
    private boolean reachMaster = false;
    private int soundIndex = 0;
    
    /**
     * AI mouvement helper instance.
     */
    private final AIMvtHelper aiMvtHelper = new AIMvtHelper();
    
    /**
     * Observable entities.
     */
    public final Map<String, XYObservable> observables;
    
    /**
     * Array of spell sounds references.
     */
    private final int[] catSounds;

    /**
     * The animation frames of this entity.
     */
    private final Sprite[] frames;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * 
     * @param game
     * @param sprtDefault
     * @param x
     * @param y
     * @param sprtRefs
     * @param color 
     */
    public Cat(final Game game, final String sprtDefault, final int x, final int y, 
        final List<String> sprtRefs, final String color) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(), sprtDefault), x, y, 
            MvtConst.DOWN, Cat.class.getSimpleName());
        setCurrentMvt(MvtConst.DOWN);
        
        this.observables = new HashMap<>();
        this.frames = new Sprite[sprtRefs.size()];
        for (int i = 0; i < frames.length; ++i) {
            this.frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), sprtRefs.get(i));
        }
        sprite = this.frames[0]; // Moving right, right ?
        
        // Sounds :
        this.catSounds = new int[2];
        appendCatSounds();
        setAnimeUpdateRequired(false);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Set cat's master.
     * @param ref
     * @param entity 
     */
    void setObservableMaster(final String ref, final MainCharacter entity) {
        observables.put(ref, entity);
    }
    
    /**
     * Catch up with another entitiy's coordinates.
     */
    void reachMasterEntityCoordinates() {
        if (!this.reachMaster) {
            this.reachMaster = true;
        }
    }
    
    /**
     * Follow screen transitions.
     * @param direction
     * @param masterX
     * @param masterY
     * @param masterSprtW
     * @param masterSprtH 
     */
    void followTrasition(final int direction, final float masterX, final float masterY,
        final int masterSprtW, final int masterSprtH) {
        
        switch (direction) {
            case MvtConst.LEFT:
                x = masterX - (masterSprtW + this.SPRT_WH);
                y = masterY + ((masterSprtH - this.SPRT_WH) / 2);
                break;
            case MvtConst.UP:
                x = masterX - ((masterSprtW - this.SPRT_WH) / 2);
                y = masterY - (masterSprtH + this.SPRT_WH);
                break;
            case MvtConst.RIGHT:
                x = masterX + (masterSprtW + this.SPRT_WH);
                y = masterY + ((masterSprtH - this.SPRT_WH) / 2);
                break;
            case MvtConst.DOWN:
                x = masterX - ((masterSprtW - this.SPRT_WH) / 2);
                y = masterY + (masterSprtH + this.SPRT_WH);
                break;
            default:
                break;
        }
        this.setMvt(direction);
        this.beforeRender(true);
    }
    
    /**
     * Request that the AbstractEntity sub class takes move 
     * based on an elapsed ammount of time.
     *
     * @param delta The time that has elapsed since last move (ms)
     */
    @Override
    public void move(final long delta) {
        
        if (this.isInCollision()) {
            return;
        }
        
        // if we're moving left and have reached the left hand side
        // of the screen, don't move.
        if (isAnimeUpdateRequired()) {
            if (((dx < 0) && (x < 0)) || ((dy < 0) && (y < 0))) {
                this.reachMaster = false;
                return;
            }
            // if we're moving right or up and down and have reached the 
            //side of the screen, don't move.
            if (((dx > 0) && (x > FrameConst.FRM_WIDTH_800 - this.SPRT_WH))
                    || ((dy > 0) && (y > FrameConst.FRM_HEIGHT_600 - this.SPRT_WH))) {
                this.reachMaster = false;
                return;
            }
        
            super.move(delta);
        }
    }
    
    @Override
    public void observed() {
        gLoopCntr1 = AnimationConst.FPS / 12;
        setMvt(MvtConst.DOWN);
        setAnimeUpdateRequired(true);
        playSoundEffect(-1);
    }
    
    @Override
    public void doLogic() {
        
        // Observation patterns logic :
        if (this.reachMaster) {
            // Observer MagicalHumanoid class coordinates.
            this.masterXY = observables.get(MainCharacter.class.getSimpleName()).observedXY();
            final boolean result = aiMvtHelper.reachMasterXYViaNonDiagonalMvt(
                this, this, SPEED, 40, SPRT_WH, SPRT_WH, this.masterXY[0], (int)this.masterXY[1]);
            this.reachMaster = !result;
            setAnimeUpdateRequired(!result);
        } else {
            // Simulate keyboard input randomly.
            if (gLoopCntr2 >= AnimationConst.FPS / 2) {
                this.masterXY = observables.get(MainCharacter.class.getSimpleName()).observedXY();
                try {
                    final boolean movable = aiMvtHelper.simulateRandonNonDiagonalMvt(this,
                            Cat.SPEED, Cat.STILLNESS_FACTOR);
                    setAnimeUpdateRequired(movable);
                    gLoopCntr2 = 0;
                } catch (final RandonMvtSimulationException ex) {
                    reachMasterEntityCoordinates();
                }
            } else {
                gLoopCntr2++;
            }
        }
    }

    /**
     * Set sounds to reference array.
     */
    private void appendCatSounds() {
        this.catSounds[0] = StaticSoundVars.grumpycat;
        this.catSounds[1] = StaticSoundVars.grumpycat;
    }
    
    @Override
    public void beforeRender(final boolean force) {

        if (((gLoopCntr1 >= AnimationConst.FPS / 12) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            switch ((int)getMvt()) {
                case MvtConst.LEFT:
                    frameVal = currentMvt != MvtConst.LEFT || frameVal == 7 ? 4 : frameVal;
                    currentMvt = MvtConst.LEFT;
                    break;
                case MvtConst.UP:
                    frameVal = currentMvt != MvtConst.UP || frameVal == 15 ? 12 : frameVal;
                    currentMvt = MvtConst.UP;
                    break;
                case MvtConst.RIGHT:
                    frameVal = currentMvt != MvtConst.RIGHT || frameVal == 11 ? 8 : frameVal;
                    currentMvt = MvtConst.RIGHT;
                    break;
                case MvtConst.DOWN:
                    frameVal = currentMvt != MvtConst.DOWN || frameVal == 3 ? 0 : frameVal;
                    currentMvt = MvtConst.DOWN;
                    break;
                default:
                    frameVal = 0;
                    currentMvt = MvtConst.DOWN;
                    break;
            }
            frameVal++;
            sprite = frames[frameVal];
        } else {
            gLoopCntr1++;
        }
    }
    
    @Override
    public void collideWith(final AbstractEntity other) {
        
        final boolean inCollision = super.collidesWith(other);
        if (inCollision) {
            this.setMvt(this.getMvt() < MvtConst.DOWN ? this.getMvt() + 1 : MvtConst.LEFT);
            setAnimeUpdateRequired(!inCollision);
        }
    }
    
    /**
     * Play sound effect.
     * @param index Sound index to play.
     */
    private void playSoundEffect(final int index) {

        if (index >= 0 && index < catSounds.length) {
            game.getSoundManager().playEffect(catSounds[index]);
        } else {
            game.getSoundManager().playEffect(catSounds[soundIndex]);
            soundIndex = soundIndex == 1 ? 0 : 1;
        }
    }

    @Override
    public void afterRender(final boolean force) {
        
        if (this.isInCollision()) {
            this.setInCollision(false);
        }
        
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }
        
    @Override
    public void setObserver(XYObservable observed) {}
    //</editor-fold>
    
}
