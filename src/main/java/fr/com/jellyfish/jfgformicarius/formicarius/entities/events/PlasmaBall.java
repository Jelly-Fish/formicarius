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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.events;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.effects.blood.BloodImpact;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.effects.blood.impacts.SpellImpact;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.time.StopWatch;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;
/**
 *
 * @author thw
 */
public class PlasmaBall extends AbstractEntity implements Ignitable, CollidableObject {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final float STAMINA_VALUE = 1.2f;
    public static final float DAMAGE_VALUE = 2.5f;
    public static final int SPRT_WH = 32;
    public static final float PLASMA_SPEED = 60;
    public static final int FRAME_COUNT = 4;
    public static final double SPAWN_DELAY = 1.2;
    public static Integer occurs = 0;
    private int currentSpeedSNDRef = 0;
    private int speed_inc_rate = 1;
    private int spawn_delay = 0;
    private boolean ignited = false;
    private int frameVal = 0;
        
    /**
     * Stop watch instance.
     */
    private final StopWatch stopwatch;

    /**
     * Array of spell sounds references.
     */
    private final int[] spellSounds = new int[6];

    /**
     * The animation frames
     */
    private final Sprite[] frames;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     *
     * @param game
     * @param x
     * @param y
     * @param frameCount
     * @param direction
     * @param ref
     */
    public PlasmaBall(final Game game, final int x, final int y,
        final int frameCount, final int direction, final String ref) {
        super(game, SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(StaticSpriteVars.plasma_ball, 0)), x, y, direction,
                ref + PlasmaBall.occurs);

        this.stopwatch = new StopWatch(PlasmaBall.SPAWN_DELAY);

        frames = new Sprite[frameCount];
        for (int i = 0; i < frames.length; ++i) {
            frames[i] = SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(StaticSpriteVars.plasma_ball, i));
        }
        sprite = frames[0];
        
        appendSpellSounds();
        setAnimeUpdateRequired(false);
        ++PlasmaBall.occurs;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public Rectangle getRectangle() {
        
        return new Rectangle(this.getX(), this.getY(), PlasmaBall.SPRT_WH - 4,
            PlasmaBall.SPRT_WH - 4);
    }

    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        return null;
    }
    
    /**
     * Initialize sound reference array.
     */
    private void appendSpellSounds() {
        spellSounds[0] = StaticSoundVars.spell1;
        spellSounds[1] = StaticSoundVars.spell2;
        spellSounds[2] = StaticSoundVars.spell3;
        spellSounds[3] = StaticSoundVars.spell4;
        spellSounds[4] = StaticSoundVars.spell5;
        spellSounds[5] = StaticSoundVars.spell5;
    }

    /**
     * Initialize mouvement rules for this.move() override.
     *
     * @param mvt
     */
    @Override
    public void initMvt(final int mvt) {
        
        this.setMvt(mvt);
        
        if (mvt == MvtConst.DOWN) {
            dy = PLASMA_SPEED; dx = 0;
        } else if (mvt == MvtConst.UP) {
            dy = -PLASMA_SPEED; dx = 0;
        } else if (mvt == MvtConst.RIGHT) {
            dx = PLASMA_SPEED; dy = 0;
        } else if (mvt == MvtConst.LEFT) {
            dx = -PLASMA_SPEED; dy = 0;
        }
    }

    @Override
    public void clear() {
        // Remove from game AbstractEntity map.
        if (game.getEntityHelper().getInteractableEntities().containsKey(this.ABSTRACT_REF)) {
            game.getEntityHelper().getInteractableEntities().remove(this.ABSTRACT_REF);
        }
        this.ignited = false;
        speed_inc_rate = 1;
        spawn_delay = 0;
        sprite = frames[frameVal];
        setAnimeUpdateRequired(false);
        this.x = -PlasmaBall.SPRT_WH;
        this.y = -PlasmaBall.SPRT_WH;
    }
    
    /**
     * Ignite or spawn entity to x y position.
     */
    @Override
    public void ignite(final int x, final int y, final int srcMvt) {
        // Update location. Add to game's entity map. Set visible and 
        // candidate for remove from game's Entity main hashmap.
        if (!this.ignited && 
            this.game.getEntityHelper().getMainCharacter().getStatusFrame(
                ).getStaminaFrame().getDeffinedWidth() > 0.0f &&
            this.stopwatch.hasReachedMaxElapsedMS()) {
            initMvt(srcMvt);
            this.x = x;
            this.y = y + 8;
            this.ignited = true;
            this.stopwatch.start();
            this.game.getEntityHelper().getInteractableEntities().put(this.ABSTRACT_REF, this);
            ++occurs;
            playStaticSoundEffect();
            // PlasmaBall must decrease stamina :
            this.game.getEntityHelper().getMainCharacter().getStatusFrame().notifyStaminaLoss(
                PlasmaBall.STAMINA_VALUE);
            setAnimeUpdateRequired(true);
        }
    }

    /**
     * Staticly play the sound from sound array.
     */
    private void playStaticSoundEffect() {
        if (currentSpeedSNDRef >= spellSounds.length - 1) {
            currentSpeedSNDRef = 0;
        }
        game.getSoundManager().playEffect(spellSounds[currentSpeedSNDRef]);
        ++currentSpeedSNDRef;
    }

    @Override
    public void move(final long delta) {

        if (((dx < 0 - SPRT_WH) && (x < 0 - SPRT_WH)) || ((dy < 0 - SPRT_WH) && (y < 0 - SPRT_WH))) {
            clear();
            return;
        }
        // if we're moving right and have reached the right hand side
        // of the screen, don't move
        if (((dx > 0 + SPRT_WH) && (x > FrameConst.FRM_WIDTH_800 + SPRT_WH))
                || ((dy > 0 + SPRT_WH) && (y > FrameConst.FRM_HEIGHT_600 + SPRT_WH))) {
            clear();
            return;
        }

        // Increase speed :
        if (spawn_delay > 8) {
            super.move(delta + speed_inc_rate);
            speed_inc_rate += 3;
        } else {
            ++spawn_delay;
        }
    }

    @Override
    public void collideWith(final AbstractEntity other) {
        
        if (other instanceof MainCharacter && !((MainCharacter)other).getIgnitable().equals(this)) {
            
            if (this.getRectangle().intersects(((MainCharacter)other).getRectangle())) {
                // Spawn blood then clear Ignitable instance.
                CollisionUtils.appendCollideEffect(new BloodImpact(
                    SpriteUtils.getSprite(game.getTextureLoader(), 
                    String.format(StaticSpriteVars.blood_impact1, 0))), 
                    this, BloodImpact.SPRT_WH, BloodImpact.SPRT_WH);
                ((MainCharacter)other).notifyCollision(PlasmaBall.DAMAGE_VALUE, 24);
                this.clear();
            }
        } else if (other instanceof EvilDarkFog && 
            !game.getEntityHelper().getMainCharacter().getIgnitable().equals(this) &&
            game.getEntityHelper().getMainCharacter().getSpawnable().isSpawned()) {
            
            if (this.getRectangle().intersects(((EvilDarkFog)other).getRectangle())) {
                // Spawn collision effect then clear Ignitable instance.
                CollisionUtils.appendCollideEffect(new SpellImpact(
                    SpriteUtils.getSprite(game.getTextureLoader(), 
                    String.format(StaticSpriteVars.spell_impact, 0))), this);
                this.clear();
            }
        }
    }

    @Override
    public void doLogic() {
    }

    @Override
    public void beforeRender(final boolean force) {

        if (((gLoopCntr1 >= AnimationConst.FPS / 20) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            if (frameVal >= 3) {
                frameVal = 0;
            }
            sprite = frames[frameVal];
            ++frameVal;
        } else {
            gLoopCntr1++;
        }
    }

    @Override
    public void afterRender(final boolean force) {
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters & setters">
    @Override
    public float getDamageValue() {
        return PlasmaBall.DAMAGE_VALUE;
    }
    
    @Override
    public boolean isIgnited() {
        return this.ignited;
    }

    public void setIgnited(final boolean ignited) {
        this.ignited = ignited;
    }
    
    @Override
    public String getAbstractRef() {
        return this.ABSTRACT_REF;
    }
    //</editor-fold>

}
