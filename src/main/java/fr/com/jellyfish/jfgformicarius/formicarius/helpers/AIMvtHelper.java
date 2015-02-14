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
package fr.com.jellyfish.jfgformicarius.formicarius.helpers;

import fr.com.jellyfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.RandonMvtSimulationException;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Observer;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.RandomUtils;

/**
 *
 * @author thw
 */
public class AIMvtHelper {

    private boolean horizontalLineUp = false;
    private boolean verticalLineUp = false;
    private int randomMvtExceptionCntr = 0;
    public static final int RANDOM_MVT_EX_VALUE = 6;

    public AIMvtHelper() {
    }

    /**
     * Force AbstractEntity to join up with another AbstractEntity's
     * coordinates. The target's xy coordinates are neither static or final and
     * can therefor move will this method is still being sollicitated.
     *
     * @param entity The AbstractEntity sub class calling this method.
     * @param observer The observer. In many cases, diverted reference of the
     * AbstractEntity sub class sent as param 1.
     * @param entitySpeed The calling AbstractEntity's speed value.
     * @param verticalMargin The vertical space to add to targets x value.
     * @param entityW AbstractEntity's Sprite Texture width.
     * @param entityH AbstractEntity's Sprite Texture height.
     * @param targetX Targets x position which is not static or constant.
     * @param targetY Targets y position which is not static or constant.
     * @return Has target been reached ?
     */
    public boolean reachMasterXYViaNonDiagonalMvt(final AbstractEntity entity,
            final Observer observer, final float entitySpeed, final int verticalMargin,
            final int entityW, final int entityH, final int targetX, final int targetY) {

        // Horizontal mouvement.
        if (!this.horizontalLineUp) {
            final int xGap = targetX - entity.getX();
            if (xGap > 0 && xGap < entityW) {
                this.horizontalLineUp = true;
                return false;
            } else if (targetX < entity.getX()) {
                entity.updateMvt(MvtConst.LEFT, true);
                entity.setHorizontalMovement(-entitySpeed);
                entity.setVerticalMovement(0);
                return false;
            } else if (targetX > entity.getX()) {
                entity.updateMvt(MvtConst.RIGHT, true);
                entity.setHorizontalMovement(entitySpeed);
                entity.setVerticalMovement(0);
                return false;
            }
        }

        // Vertical mouvement.
        if (!this.verticalLineUp) {
            final int yGap = targetY - (entity.getY() - verticalMargin);
            if (yGap > 0 && yGap < entityH) {
                this.verticalLineUp = true;
            } else if (targetY < entity.getY() - verticalMargin) {
                entity.updateMvt(MvtConst.UP, true);
                entity.setVerticalMovement(-entitySpeed);
                entity.setHorizontalMovement(0);
                return false;
            } else if (targetY > entity.getY() - verticalMargin) {
                entity.updateMvt(MvtConst.DOWN, true);
                entity.setVerticalMovement(entitySpeed);
                entity.setHorizontalMovement(0);
                return false;
            }
        }

        if (this.verticalLineUp && this.horizontalLineUp) {
            this.resetReachMasterXYViaNonDiagonalMvt();
            observer.observed();
            return true;
        }

        return this.verticalLineUp && this.horizontalLineUp;
    }

    /**
     * Simulate random non diagonal mouvement; if left | right, vertical
     * mouvement is set to 0 and if up | down horizontal mouvement is set to 0.
     *
     * @param entity The AbstractEntity sub class reference.
     * @param entitySpeed The speed of the AbstractEntity sub class.
     * @param stillness value to add to hightest random value, if random value
     * is in the range of this value + max random value return false meaning no
     * move.
     * @return boolean Is move required.
     * @throws formicarius.exceptions.RandonMvtSimulationException
     */
    public boolean simulateRandonNonDiagonalMvt(final AbstractEntity entity,
            final float entitySpeed, final int stillness) throws RandonMvtSimulationException {

        final int mvt = RandomUtils.randInt(MvtConst.STILL, MvtConst.DOWN + stillness);
        if ((mvt <= 0 || mvt > 4) && mvt < MvtConst.DOWN + stillness) {
            return false;  // No mouvement.
        } else {

            // Mouvement exception event :
            if (mvt == MvtConst.DOWN + stillness) {
                if (randomMvtExceptionCntr >= RANDOM_MVT_EX_VALUE) {
                    randomMvtExceptionCntr = 0;
                    throw new RandonMvtSimulationException();
                } else {
                    ++randomMvtExceptionCntr;
                }
            }

            // Decide on move direction:
            switch (mvt) {
                case MvtConst.LEFT:
                    entity.updateMvt(MvtConst.LEFT, true);
                    entity.setHorizontalMovement(-entitySpeed);
                    entity.setVerticalMovement(0);
                    break;
                case MvtConst.UP:
                    entity.updateMvt(MvtConst.UP, true);
                    entity.setVerticalMovement(-entitySpeed);
                    entity.setHorizontalMovement(0);
                    break;
                case MvtConst.RIGHT:
                    entity.updateMvt(MvtConst.RIGHT, true);
                    entity.setHorizontalMovement(entitySpeed);
                    entity.setVerticalMovement(0);
                    break;
                case MvtConst.DOWN:
                    entity.updateMvt(MvtConst.DOWN, true);
                    entity.setVerticalMovement(entitySpeed);
                    entity.setHorizontalMovement(0);
                    break;
                default:
                    entity.updateMvt(entity.getCurrentMvt(), false);
                    return false;
            }
        }

        return true;
    }

    /**
     * Simulate random non diagonal mouvement; if left | right, vertical
     * mouvement is set to 0 and if up | down horizontal mouvement is set to 0.
     *
     * @param entity The AbstractEntity sub class reference.
     * @param entitySpeed The speed of the AbstractEntity sub class.
     * @param excludedMvt mouvent direction that must not be affected to entity.
     * @return int new mouvement direction.
     */
    public int simulateRandonNonDiagonalMvtExcludedMvt(final AbstractEntity entity,
            final float entitySpeed, final int excludedMvt) {

        // Loop on randInt call until mouvent is not equal to the 
        // excluded mouvement value.
        int mvt = -1;
        do {
            mvt = RandomUtils.randInt(MvtConst.LEFT, MvtConst.DOWN, excludedMvt);
        } while (mvt == excludedMvt);

        // Decide on move direction:
        switch (mvt) {
            case MvtConst.LEFT:
                entity.updateMvt(MvtConst.LEFT, true);
                entity.setHorizontalMovement(-entitySpeed);
                entity.setVerticalMovement(0);
                break;
            case MvtConst.UP:
                entity.updateMvt(MvtConst.UP, true);
                entity.setVerticalMovement(-entitySpeed);
                entity.setHorizontalMovement(0);
                break;
            case MvtConst.RIGHT:
                entity.updateMvt(MvtConst.RIGHT, true);
                entity.setHorizontalMovement(entitySpeed);
                entity.setVerticalMovement(0);
                break;
            case MvtConst.DOWN:
                entity.updateMvt(MvtConst.DOWN, true);
                entity.setVerticalMovement(entitySpeed);
                entity.setHorizontalMovement(0);
                break;
        }

        return mvt;
    }

    /**
     *
     * @param direction
     * @return Mouvement direction
     * @see MvtConst class.
     */
    public int randomlyExitCollision(final int direction) {

        int result = MvtConst.STILL;

        switch (direction) {
            case MvtConst.LEFT:
                result = RandomUtils.randInt(1, 4);
            case MvtConst.UP:
            case MvtConst.RIGHT:
            case MvtConst.DOWN:
        }

        return result;
    }

    /**
     *
     */
    private void resetReachMasterXYViaNonDiagonalMvt() {
        this.horizontalLineUp = false;
        this.verticalLineUp = false;
    }

}
