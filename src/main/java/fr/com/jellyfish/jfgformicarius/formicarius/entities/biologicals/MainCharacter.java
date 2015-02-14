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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals;

import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import java.util.List;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.events.BloodyDagger;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.events.SpellRing;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.pools.InteractableEntityPool;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.effects.blood.BloodImpact;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.status.StatusFrame;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.vegetation.Tree;
import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.ZoneBuildException;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Observer;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Spawnable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.XYObservable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticRandomValues;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.Zone;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.ZonePosition;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thw
 */
public class MainCharacter extends AbstractEntity implements XYObservable {

    //<editor-fold defaultstate="collapsed" desc="variables">
    public static final float SPEED = 160;
    public static final int SPRT_W = 32;
    public static final int SPRT_H = 48;
    public static final int MAX_HEALTH_VALUE = 100;
    private int frameVal = 0;
    private int spawnMvt;
    private boolean moved = false;
    private float collisionEffectValue = 0.0f;

    /**
     * Current zone position.
     */
    public ZonePosition currentZonePosition;

    /**
     * The animation frames of this entity.
     */
    private final Sprite[] frames;

    /**
     * Plasma ball frames collection.
     */
    private Ignitable ignitable;

    /**
     * Spawnable entity.
     */
    private Spawnable spawnable;

    /**
     * This Cat instance reference.
     */
    public final Cat cat;

    /**
     *
     */
    private final StatusFrame statusFrame;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     *
     * @param game
     * @param sprts
     * @param effectSprts
     * @param x
     * @param frameCount
     * @param y
     * @param zonePosition
     * @param cat
     * @param ref
     */
    public MainCharacter(final Game game, final String sprts, final String effectSprts,
            final int frameCount, final int x, final int y, final ZonePosition zonePosition,
            final Cat cat, final String ref) {
        super(game, null, x, y, MvtConst.DOWN, ref);
        
        this.currentZonePosition = zonePosition;
        this.cat = cat;

        // Build frame array :
        this.frames = new Sprite[frameCount];
        for (int i = 0; i < this.frames.length - 4; ++i) {
            this.frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), String.format(sprts, i));
        }
        // Add collision/hit or other sprite effetcs :
        this.frames[16] = SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(effectSprts, MvtConst.LEFT));
        this.frames[17] = SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(effectSprts, MvtConst.UP));
        this.frames[18] = SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(effectSprts, MvtConst.RIGHT));
        this.frames[19] = SpriteUtils.getSprite(game.getTextureLoader(),
                String.format(effectSprts, MvtConst.DOWN));
        sprite = this.frames[0];
        
        this.statusFrame = new StatusFrame(game);
        init();
        setAnimeUpdateRequired(false);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="init methods">
    /**
     * Prior initialization.
     */
    private void init() {

        // Add Cat observes MagicalHumanoid :
        if (cat != null) {
            cat.setObservableMaster(MainCharacter.class.getSimpleName(), this);
        }
        
        this.spawnMvt = MvtConst.DOWN;
        /////// INIT Ignitable ENT /////////////////////////////////////////////
        this.spawnable = new SpellRing(game, null, 0, 0);
        final BloodyDagger dag = new BloodyDagger(game, 0, 0, MvtConst.LEFT,
                BloodyDagger.class.getSimpleName());
        ignitable = dag;
        ////////////////////////////////////////////////////////////////////////
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Clear maps for new Zone or new Zone transition.
     */
    public void triggerTransition() {

        // Clear Spawnable :
        if (this.spawnable != null && this.spawnable.isSpawned()) {
            this.spawnable.clear();
        }
        // Clear Ingnitable :
        if (this.ignitable != null && this.ignitable.isIgnited()) {
            this.ignitable.clear();
        }

        // Clear entity helperscollections : objects, iteractable + all entities
        // other than main character and cat :
        game.clearEntityCollectionsForTransition();

        // Build new zone :
        final HashMap<String, int[]> randomDefinitions = new HashMap<>();
        randomDefinitions.put(Tree.class.getSimpleName().toString(),
                new int[]{StaticRandomValues.bare_tree_50x100_rand_max_many,
                    StaticRandomValues.bare_tree_50x100_rand_expected});
        final List<AbstractEntity> zoneActiveEntities = new ArrayList<>();
        zoneActiveEntities.addAll(this.game.getEntityHelper().getMainEntities().values());
        zoneActiveEntities.addAll(this.game.getEntityHelper().getInteractableEntities().values());
        AbstractEntity[] array = new AbstractEntity[zoneActiveEntities.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = zoneActiveEntities.get(i);
        }
        
        try {
            final Zone zone = new Zone(randomDefinitions, array);
            game.getEntityHelper().getObjectEntities().putAll(zone.getGlobals());
            game.getEntityHelper().getStaticEntities().putAll(zone.getStatics());
        } catch (final ZoneBuildException zbex) {
            Logger.getLogger(MainCharacter.class.getName()).log(Level.SEVERE, null, zbex);
        }

        // Fill the following HashMap with randomly accessed pool of Interactable
        // class instances.
        game.getEntityHelper().getInteractableEntities().putAll(
                InteractableEntityPool.getInstance().getRandomSubPool(
                        FrameConst.FRM_WIDTH_800 - 80));
    }

    /**
     * Swap Spawnable ref for another via AbstractEntity instance ref.
     *
     * @param spawnable
     */
    public void swapSpawnable(final AbstractEntity spawnable) {
        
        if (spawnable instanceof Spawnable) {
            // Cast this Swapable instance to set in game active = false :
            final AbstractEntity abstractEntity = (AbstractEntity) this.spawnable;
            abstractEntity.setInGameActive(false);
            // Clear this Spawnable instance :
            if (this.spawnable != null) {
                this.spawnable.clear();
            }
            // Also remove this Spawnable instance :
            this.game.accessGlobalEntities().remove(this.spawnable.getAbstractRef());
            // Finally swap instances. Spawnable is nowready for spawn(...) method call.
            spawnable.setInGameActive(true);
            this.spawnable = (Spawnable) spawnable;
            if (spawnable instanceof Observer) {
                final Observer obs = (Observer) this.spawnable;
                obs.setObserver(this);
            }
        }
    }

    /**
     * Swap Ignitable ref for another via AbstractEntity instance ref.
     *
     * @param ignitable
     */
    public void swapIgnitable(final AbstractEntity ignitable) {
        
        if (ignitable instanceof Ignitable) {
            // Cast this Swapable instance to set in game active = false :
            final AbstractEntity abstractEntity = (AbstractEntity) this.ignitable;
            abstractEntity.setInGameActive(false);
            // Clear this Spawnable instance :
            if (this.ignitable != null) {
                this.ignitable.clear();
            }
            // Also remove this Spawnable instance :
            this.game.getEntityHelper().getInteractableEntities().remove(this.ignitable.getAbstractRef());
            // Finally swap instances. Spawnable is nowready for spawn(...) method call.
            ignitable.setInGameActive(true);
            this.ignitable = (Ignitable) ignitable;
        }
    }

    /**
     *
     * @param val
     * @param factor
     */
    public void notifyCollision(final float val, final int factor) {
        
        if (this.collisionEffectValue <= 0.0f) {

            // This will cause a return statement to trigger in move() method.
            // If this.collisionEffectValue > 0 then return & decrement.
            this.collisionEffectValue = val * factor;
            CollisionUtils.appendCollideEffect(new BloodImpact(
                    SpriteUtils.getSprite(game.getTextureLoader(),
                            String.format(StaticSpriteVars.blood_impact1, 0))),
                    this, BloodImpact.SPRT_WH, BloodImpact.SPRT_WH);

            // Change current sprite :
            sprite = this.frames[this.frames.length - 5 + this.getMvt()];

            // Finally notify of damage :
            this.statusFrame.notifyDamage(val);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="overriden methods">
    @Override
    public void callEntity() {
        if (cat != null) {
            this.cat.reachMasterEntityCoordinates();
        }
    }
    
    @Override
    public int[] observedXY() {
        if (this != null) {
            return new int[]{(int) x, (int) y};
        } else {
            return null;
        }
    }
    
    @Override
    public int[] observedWH() {
        return new int[]{MainCharacter.SPRT_W, MainCharacter.SPRT_H};
    }
    
    @Override
    public void observed(final AbstractEntity observer) {
    }

    /**
     * Fire sprite.
     */
    @Override
    public void fire() {
        if (!this.ignitable.isIgnited() && this.collisionEffectValue <= 0.0f) {
            if (moved) {
                this.ignitable.ignite(this.getX(), this.getY(), this.getMvt());
            } else {
                this.ignitable.ignite(this.getX(), this.getY(), this.spawnMvt);
            }
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y) {
        if (!this.spawnable.isSpawned() && 
            this.statusFrame.getStaminaFrame().getDeffinedWidth() > 0.0f && 
            this.collisionEffectValue <= 0.0f) {
            this.statusFrame.notifyStaminaLoss(this.spawnable.spawn(x, y));
        }
    }
    
    @Override
    public void collideWith(final AbstractEntity other) {
    }

    /**
     * Request that the AbstractEntity sub class takes move based on an elapsed
     * ammount of time.
     *
     * @param delta The time that has elapsed since last move (ms)
     */
    @Override
    public void move(final long delta) {
        
        if (this.isInCollision()) {
            return;
        }

        // If collision effect is still running depending on collisionEffectValue,
        // then return after decrementing the value. Otherwise, reset the value.
        if (this.collisionEffectValue > 0) {            
            --this.collisionEffectValue;
            return;
        } else {            
            this.collisionEffectValue = 0.0f;            
        }

        // if we're moving left and have reached the edge of the screen.
        if (dx < 0 && x < 0) {
            // Check zone matrix for possible zone move. If true, move what is
            // necessary, set new ZonePosition to this. Redo background tilling :
            // remove, append to EntityHelper's tile entities.
            game.getEntityHelper().getFader().fade();
            x = FrameConst.FRM_WIDTH_800 - MainCharacter.SPRT_W;
            if (this.cat != null) {
                this.cat.followTrasition(MvtConst.LEFT, x, y, MainCharacter.SPRT_W, MainCharacter.SPRT_H);
            }
            triggerTransition();
            return;
        }

        // if we're moving right and have reached the edge of the screen.
        if (dx > 0 && x > FrameConst.FRM_WIDTH_800 - MainCharacter.SPRT_W) {
            game.getEntityHelper().getFader().fade();
            x = 0;
            if (this.cat != null) {
                this.cat.followTrasition(MvtConst.RIGHT, x, y, MainCharacter.SPRT_W, MainCharacter.SPRT_H);
            }
            triggerTransition();
            return;
        }

        // if we're moving down and have reached the edge of the screen.
        if (dy < 0 && y < 0) {
            game.getEntityHelper().getFader().fade();
            y = FrameConst.FRM_HEIGHT_600 - MainCharacter.SPRT_H;
            if (this.cat != null) {
                this.cat.followTrasition(MvtConst.UP, x, y, MainCharacter.SPRT_W, MainCharacter.SPRT_H);
            }
            triggerTransition();
            return;
        }

        // if we're moving up and have reached the edge of the screen.
        if (dy > 0 && y > FrameConst.FRM_HEIGHT_600 - SPRT_H) {
            game.getEntityHelper().getFader().fade();
            y = 0;
            if (this.cat != null) {
                this.cat.followTrasition(MvtConst.DOWN, x, y, MainCharacter.SPRT_W, MainCharacter.SPRT_H);
            }
            triggerTransition();
            return;
        }
        
        super.move(delta);
    }
    
    @Override
    public void doLogic() {
    }
    
    @Override
    public void beforeRender(final boolean force) {

        // If collision effect is still running then return.
        if (this.collisionEffectValue > 0) {            
            return;
        }
        
        if (((gLoopCntr1 >= AnimationConst.FPS / 10) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            switch ((int) getMvt()) {
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
            moved = true;
        } else {
            gLoopCntr1++;
        }
    }
    
    @Override
    public void afterRender(final boolean force) {

        // Deal with sound effects.
        if ((moving && ((gLoopCntr2 >= AnimationConst.FPS / 3) && isAnimeUpdateRequired())) || force) {
            game.getSoundManager().playSound(StaticSoundVars.step_grass1);
            gLoopCntr2 = 0;
        } else {
            gLoopCntr2++;
        }
        
        if (this.isInCollision()) {            
            this.setInCollision(false);            
        }
        
        DrawingHelper.getInstance().getDrawableQueue().addLast(this.statusFrame);
    }

    /**
     * @return This rectangles representation.
     */
    public Rectangle getRectangle() {
        return new Rectangle((int) x, (int) y, MainCharacter.SPRT_W, MainCharacter.SPRT_H);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters & setters"> 
    public StatusFrame getStatusFrame() {
        return statusFrame;
    }
    
    public Ignitable getIgnitable() {
        return ignitable;
    }
    
    public Spawnable getSpawnable() {
        return spawnable;
    }
    //</editor-fold>

}
