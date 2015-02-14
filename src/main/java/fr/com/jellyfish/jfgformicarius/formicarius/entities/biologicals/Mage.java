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

import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.events.PlasmaBall;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.effects.blood.BloodImpact;
import fr.com.jellyfish.jfgformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.AIMvtHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Ignitable;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.SpellBoundable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.RandomUtils;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.SpriteUtils;
import java.awt.Rectangle;

/**
 *
 * @author thw
 */
public class Mage extends Knight {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * AI helper.
     */
    private final AIMvtHelper aiHelper = new AIMvtHelper();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     * @param game
     * @param sprts
     * @param frameCount
     * @param x
     * @param y
     * @param startMvt
     * @param ref 
     */
    public Mage(final Game game, final String sprts, final int frameCount,
            final int x, final int y, final int startMvt, final String ref) {
        super(game, sprts, frameCount, x, y, startMvt, ref + Mage.occurs);

        this.speed = 100.0f;

        init();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     *
     */
    private void init() {
        final PlasmaBall pBall = new PlasmaBall(this.game, 0, 0, 4, MvtConst.DOWN,
                PlasmaBall.class.getSimpleName());
        this.ignitable = pBall;
    }

    @Override
    public void collideWith(final AbstractEntity other) {

        if (other instanceof Mage || other.equals(this.ignitable)) {
            return;
        }

        if (other instanceof MainCharacter
                && CollisionUtils.inCollision(this.getRectangle(), ((MainCharacter) other).getRectangle())) {
            this.deblockCollision();
        }

        final boolean inCollision = CollisionUtils.inCollision(this.getRectangle(),
                other, CollidableObject.COLLIDABLE_OBJ_INNER_PADDING_4);

        if (inCollision && (other instanceof Ignitable) && !other.equals(this.ignitable)
                && !(other instanceof PlasmaBall)) {

            // Spawn blood then clear Ignitable instance.
            CollisionUtils.appendCollideEffect(new BloodImpact(
                    SpriteUtils.getSprite(game.getTextureLoader(),
                            String.format(StaticSpriteVars.blood_impact1, 0))),
                    this, BloodImpact.SPRT_WH, BloodImpact.SPRT_WH);
            final Ignitable ig = (Ignitable) other;
            ig.clear();
            this.setInCollision(false);
            this.health -= ig.getDamageValue();

            if (this.health <= 0) {
                Game.getInstance().getSoundManager().playEffect(StaticSoundVars.cunning_laugh);
                this.clear();
            }

            if (RandomUtils.randInt(0, 10) > 4) {
                Game.getInstance().getSoundManager().playEffect(StaticSoundVars.scream1);
            }
        }
    }

    @Override
    public SpellBoundable spellbound() {
        return this;
    }

    /**
     *
     * @param eY
     */
    @Override
    protected void updateMvtVertical(final int eY) {

        if (eY > this.y) {
            this.setMvt(MvtConst.DOWN);
            sprite = frames[0];
            this.mvtConstant = MvtConst.DOWN;
            this.setVerticalMovement(this.speed);
        } else if (eY < this.y) {
            this.setMvt(MvtConst.UP);
            sprite = frames[12];
            this.mvtConstant = MvtConst.UP;
            this.setVerticalMovement(-this.speed);
        }
    }

    @Override
    public void doLogic() {

        // TODO : devide by sprt w & h (see XYObservableinterface).
        final int eX = observable.observedXY()[0];
        final int eY = observable.observedXY()[1];

        // TODO : below, fix. Use Rectangle for triggering fire(). 
        final Rectangle obsRectW = new Rectangle(0, eY + 4,
                FrameConst.FRM_WIDTH_800, observable.observedWH()[1] - 8);
        final Rectangle obsRectH = new Rectangle(eX + 4, 0,
                observable.observedWH()[0] - 8, FrameConst.FRM_HEIGHT_600);

        if (obsRectH.intersects(this.getRectangle())) {
            updateMvtVertical(eY);
            setHorizontalMovement(0);
            this.fire();
        } else if (obsRectW.intersects(this.getRectangle())) {
            updateMvtHorizontal(eX);
            setVerticalMovement(0);
            this.fire();
        } else {
            setAnimeUpdateRequired(true);
        }

    }

    /**
     *
     * @param delta
     */
    @Override
    public void move(final long delta) {

        // Here deal with deblocking collisions by simulating random ai moves.
        if (isAnimeUpdateRequired()) {

            if (this.isInCollision() || neverMoved) {
                this.deblockCollision();
            }

            if (updateIfOutOfBounds()) {
                return;
            }

            super.move(delta);
            neverMoved = false;
        }
    }

    /**
     * Get out of collision situation randomly using AI mouvement helper.
     */
    private void deblockCollision() {
        this.mvtConstant = aiHelper.simulateRandonNonDiagonalMvtExcludedMvt(this,
                this.speed, this.mvtConstant);
        setInCollision(false);
        setAnimeUpdateRequired(false);
    }
    //</editor-fold>

}
