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
package fr.com.jellyfish.jfgformicarius.formicarius.entities.characters;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.AnimationConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.events.Axe;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.events.PlasmaBall;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.effects.blood.impacts.MetalImpact;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Interactable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.SpellBoundable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.XYObservable;
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
public class Knight extends AbstractEntity implements Interactable, CollidableObject,
        SpellBoundable {

    //<editor-fold defaultstate="collapsed" desc="variables">
    protected float speed = 60.0f;
    public static final int SPRT_W = 32;
    public static final int SPRT_H = 48;
    public static final float HEALTH_VALUE = 10.0f;
    public static final int FRAME_COUNT = 20;
    public static int occurs = 0;
    private int frameVal = 0;
    protected float health;
    protected int mvtConstant;
    protected boolean spellbound = false;

    /**
     *
     */
    protected final XYObservable observable;

    /**
     * The animation frames of this entity.
     */
    protected final Sprite[] frames;

    /**
     * Plasma ball frames collection.
     */
    protected Ignitable ignitable;

    /**
     *
     */
    protected boolean neverMoved = true;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     *
     * @param game
     * @param sprts
     * @param frameCount
     * @param x
     * @param y
     * @param startMvt
     * @param ref
     */
    public Knight(final Game game, final String sprts, final int frameCount,
            final int x, final int y, final int startMvt, final String ref) {
        super(game, null, x, y, startMvt, ref + Knight.occurs);

        // Build frame array :
        this.frames = new Sprite[frameCount];
        for (int i = 0; i < this.frames.length; ++i) {
            this.frames[i] = SpriteUtils.getSprite(game.getTextureLoader(), String.format(sprts, i));
        }

        this.mvtConstant = startMvt;
        this.setMvt(startMvt);
        this.currentMvt = startMvt;
        this.health = HEALTH_VALUE;

        this.sprite = this.frames[0];
        this.observable = Game.getInstance().getEntityHelper().getMainCharacter();

        init();
        ++Knight.occurs;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="init methods">
    /**
     * Prior initialization.
     */
    private void init() {

        final Axe axe = new Axe(game, 0, 0, StaticSpriteVars.axe1, this.mvtConstant,
                Axe.class.getSimpleName());
        this.ignitable = axe;

        this.setInCollision(false);
        this.spellbound = false;
        this.setAnimeUpdateRequired(false);
        
        // Trigger a mouvement direction. Otherwise entity will wait for doLogic
        // method returning a update mouvement direction.
        updateMvtHorizontal(0);
        setVerticalMovement(0);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Fire sprite.
     */
    @Override
    public void fire() {
        if (!this.ignitable.isIgnited()) {
            setAnimeUpdateRequired(false);
            this.ignitable.ignite(this.getX(), this.getY(), this.getMvt());
        }
    }

    @Override
    public void collideWith(final AbstractEntity other) {

        final boolean inCollision = CollisionUtils.inCollision(this.getRectangle(),
                other, CollidableObject.COLLIDABLE_OBJ_INNER_PADDING_4);

        if (inCollision && (other instanceof Ignitable) && !(other instanceof PlasmaBall)
                && !other.equals(this.ignitable)) {

            // Spawn blood then clear Ignitable instance.
            CollisionUtils.appendCollideEffect(new MetalImpact(
                    SpriteUtils.getSprite(game.getTextureLoader(),
                            String.format(StaticSpriteVars.metal_impact, 0))),
                    this, MetalImpact.SPRT_WH, MetalImpact.SPRT_WH);
            final Ignitable ig = (Ignitable) other;
            ig.clear();
            this.setInCollision(false);
            this.health -= ig.getDamageValue();

            if (this.health <= 0) {
                Game.getInstance().getSoundManager().playEffect(StaticSoundVars.metal_clatter);
                this.clear();
            }
        }
    }

    /**
     *
     * @param delta
     */
    @Override
    public void move(final long delta) {

        if (this.isInCollision() || !isAnimeUpdateRequired() || this.spellbound) {
            return;
        }

        if (updateIfOutOfBounds()) {
            return;
        }

        super.move(delta);
        this.neverMoved = false;
    }

    /**
     * Update location if entity has reached the bounds of the screen.
     *
     * @return boolean true if is out of bounds.
     */
    protected boolean updateIfOutOfBounds() {

        // if we're moving left and have reached the left hand side
        // of the screen, don't move and swap sides.   
        if (x < 1 && this.getMvt() == MvtConst.LEFT) {
            updateMvtHorizontal(observable.observedXY()[0]);
            return true;
        } else if (x > FrameConst.FRM_WIDTH - (SPRT_W + 1) && this.getMvt() == MvtConst.RIGHT) {
            updateMvtHorizontal(observable.observedXY()[0]);
            return true;
        } else if (y < 1 && this.getMvt() == MvtConst.UP) {
            updateMvtVertical(observable.observedXY()[1]);
            return true;
        } else if (y > FrameConst.FRM_HEIGHT - (SPRT_H + 1) && this.getMvt() == MvtConst.DOWN) {
            updateMvtVertical(observable.observedXY()[1]);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public SpellBoundable spellbound() {
        this.spellbound = true;
        // Set texture/sprite effects. getMvt() will return left or right.
        // 1 for left and/or 3 for right.
        sprite = this.frames[this.frames.length - 5 + this.getMvt()];
        return this;
    }
    
    @Override
    public SpellBoundable spellboundFreeze() {
        // no impact on this class here.
        return this;
    }

    @Override
    public void removeSpellbound() {
        this.spellbound = false;
    }

    @Override
    public void doLogic() {

        if (this.spellbound) {
            return;
        }

        // TODO : devide by sprt w & h (see XYObservableinterface).
        final int eX = observable.observedXY()[0];
        final int eY = observable.observedXY()[1];

        // TODO : below, fix. Use Rectangle for triggering fire(). 
        final Rectangle obsRectW = new Rectangle(0, eY + 4,
                FrameConst.FRM_WIDTH, observable.observedWH()[1] - 8);
        final Rectangle obsRectH = new Rectangle(eX + 4, 0,
                observable.observedWH()[0] - 8, FrameConst.FRM_HEIGHT);

        if (obsRectH.intersects(this.getRectangle())) {
            updateMvtVertical(eY);
            setVerticalMovement(0);
            this.fire();
        } else if (obsRectW.intersects(this.getRectangle())) {
            updateMvtHorizontal(eX);
            setVerticalMovement(0);
            this.fire();
        } else {
            setAnimeUpdateRequired(true);
        }

        // Finally reset mouvent to the constant mouvent value (left or right).
        setMvt(this.mvtConstant);
    }

    @Override
    public void beforeRender(final boolean force) {

        if (isInCollision() || this.spellbound) {
            return;
        }

        if (((gLoopCntr1 >= AnimationConst.FPS / 6) && isAnimeUpdateRequired()) || force) {
            gLoopCntr1 = 0;
            switch ((int) this.getMvt()) {
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
                    break;
            }

            frameVal++;
            sprite = frames[frameVal];
        } else {
            gLoopCntr1++;
        }
    }

    @Override
    public void afterRender(final boolean force) {
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }

    /**
     *
     * @param eY
     */
    protected void updateMvtVertical(final int eY) {

        if (eY > this.y) {
            this.setMvt(MvtConst.DOWN);
            sprite = frames[0];
        } else if (eY < this.y) {
            this.setMvt(MvtConst.UP);
            sprite = frames[12];
        }
    }

    /**
     *
     * @param eX
     */
    protected void updateMvtHorizontal(final int eX) {

        if (eX > this.x) {
            this.setMvt(MvtConst.RIGHT);
            sprite = frames[8];
            this.mvtConstant = MvtConst.RIGHT;
            this.setHorizontalMovement(this.speed);
        } else if (eX < this.x) {
            this.setMvt(MvtConst.LEFT);
            sprite = frames[4];
            this.mvtConstant = MvtConst.LEFT;
            this.setHorizontalMovement(-this.speed);
        }
    }

    @Override
    public void clear() {

        if (game.getEntityHelper().getInteractableEntities().containsKey(this.ABSTRACT_REF)) {
            game.getEntityHelper().getInteractableEntities().remove(this.ABSTRACT_REF);
            this.ignitable.clear();
            game.getEntityHelper().getInteractableEntities().remove(this.ignitable.getAbstractRef());
        }

        this.health = HEALTH_VALUE;
        this.setAnimeUpdateRequired(false);
        this.frameVal = 0;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getX(), this.getY(), SPRT_W, SPRT_H);
    }

    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        return null;
    }
    //</editor-fold>

}
