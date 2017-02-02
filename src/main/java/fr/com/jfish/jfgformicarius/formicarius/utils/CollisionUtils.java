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
package fr.com.jfish.jfgformicarius.formicarius.utils;

import fr.com.jfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jfish.jfgformicarius.formicarius.interfaces.Spawnable;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;

/**
 *
 * @author thw
 */
public class CollisionUtils {

    /**
     * Collision utility method that will define collision depending on
     * collider's mouvement. Param 3 "padding" possibilities are defined in
     * Collidable object Interface.
     *
     * @see formicarius.interfaces.CollidableObject
     *
     * @param blocker
     * @param collider
     * @param padding depending on mouvementthat will redefine blocker param.
     * @return true if in collision
     */
    public static boolean inCollision(final Rectangle blocker, final AbstractEntity collider,
            final int padding) {

        final Rectangle collidingEntity = new Rectangle();

        collidingEntity.setBounds((int) collider.getX(), (int) collider.getY(), collider.sprite.getWidth(),
                collider.sprite.getHeight());

        switch (collider.getMvt()) {
            case MvtConst.RIGHT:
                return collidingEntity.intersectsLine(blocker.getMinX(), blocker.getMinY()
                        + padding, blocker.getMinX(), blocker.getMaxY() - padding);
            case MvtConst.LEFT:
                return collidingEntity.intersectsLine(blocker.getMaxX(),
                        blocker.getMinY() + padding, blocker.getMaxX(),
                        blocker.getMaxY() - padding);
            case MvtConst.DOWN:
                return collidingEntity.intersectsLine(blocker.getMinX() + padding,
                        blocker.getMinY(), blocker.getMaxX() - padding, blocker.getMinY());
            case MvtConst.UP:
                return collidingEntity.intersectsLine(blocker.getMinX() + padding,
                        blocker.getMaxY(), blocker.getMaxX() - padding, blocker.getMaxY());
        }

        return false;
    }
    
    /**
     * Collision utility method that will define collision status.
     *
     * @param blocker
     * @param collider
     * @return true if in collision
     */
    public static boolean inCollision(final Rectangle blocker, final AbstractEntity collider) {

        final Rectangle collidingEntity = new Rectangle(collider.getX(), collider.getY(), 
            collider.sprite.getWidth(), collider.sprite.getHeight());
        
        return collidingEntity.intersects(blocker);
    }

    /**
     * Defines is collision is fully verified. Checks rectangle collision,
     * Rectangle contains Rectangle and also (and first) is blocking entity's
     * point represented by center of Rectangle is containde is colliding
     * Rectangle. If no classic collision evaluations are true, method returns
     * false.
     *
     * @param blocker 
     * @param collider
     * @return true for collision.
     */
    public static boolean inFullCollision(final Rectangle blocker, final Rectangle collider) {

        final Point p = new Point((int) blocker.getWidth() / 2, (int) blocker.getHeight() / 2);

        if (collider.contains(p)) {
            return true;
        } else if (collider.contains(blocker)) {
            return true;
        } else if (collider.intersects(blocker)) {
            return true;
        } else if (blocker.contains(collider)) {
            return true;
        } else if (blocker.intersects(collider)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * If Point issue from a Sprite belonging to entity is contaied in a
     * Rectangled object.
     *
     * @param p
     * @param collider
     * @return
     */
    public static boolean pointsTowards(final Point p, final Rectangle collider) {

        return collider.contains(p);
    }

    /**
     * Evaluation of two Rectangles intersecting but not contained. Meaning one
     * may be contained in another.
     *
     * @param blocker
     * @param collider
     * @return
     */
    public static boolean inCollision(final Rectangle blocker, final Rectangle collider) {
        return collider.intersects(blocker);
    }

    /**
     * Evaluates if a occurence of a collection is in intersects true with
     * another unique collider entity. The collider entity's Rectangle is built
     * locally.
     *
     * @param blockers
     * @param collider
     * @return
     */
    public static boolean inCollision(final Map<String, AbstractEntity> blockers, final AbstractEntity collider) {

        final Rectangle collidingEntity = new Rectangle();
        collidingEntity.setBounds((int) collider.getX(), (int) collider.getY(), collider.sprite.getWidth(),
                collider.sprite.getHeight());

        for (AbstractEntity entity : blockers.values()) {
            final Rectangle collidingBlocker = new Rectangle();
            collidingBlocker.setBounds((int) entity.getX(), (int) entity.getY(), entity.sprite.getWidth(),
                    entity.sprite.getHeight());
            if (collidingBlocker.intersects(collidingEntity)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Append a collision effect (Spawnable class in param 1) to draw in game
     * process. Effetct or Spawnable instance is centered if possible. The
     * coordinates are centered depending on AbstractEntity source param 2's
     * dimensions.
     *
     * @param effect
     * @param source
     * @param effectW
     * @param effectH
     */
    public static void appendCollideEffect(final Spawnable effect, final AbstractEntity source,
            final int effectW, final int effectH) {

        effect.basicSpawn(source.getX() - ((effectW - source.sprite.getWidth()) / 2),
                source.getY() - ((effectH - source.sprite.getHeight()) / 2));
    }

    /**
     * Append a collision effect (Spawnable class in param 1) to draw in game
     * process.
     *
     * @param effect Spawnable
     * @param source AbstractEntity
     */
    public static void appendCollideEffect(final Spawnable effect, final AbstractEntity source) {
        effect.basicSpawn(source.getX(), source.getY());
    }

}
