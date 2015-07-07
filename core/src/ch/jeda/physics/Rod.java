/*
 * Copyright (C) 2015 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

/**
 * A joint that connects two bodies widh a rod. Both bodies can rotate around their attachment point to the rod.
 *
 * @since 2.2
 */
public final class Rod extends Joint {

    private final DistanceJoint imp;

    /**
     * Constructs a rod that connects the centers of two bodies.
     *
     * @param bodyA the first body to connect
     * @param bodyB the second body to connect
     * @throws IllegalArgumentException if <code>bodyA</code> or <code>bodyB</code> are not in a physical simulation
     * @throws NullPointerException if <code>bodyA</code> or <code>bodyB</code> are <code>null</code>
     *
     * @since 2.2
     */
    public Rod(final Body bodyA, final Body bodyB) {
        this(bodyA, bodyB, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Construct a rod that connects two bodies.
     *
     * @param bodyA the first body to connect
     * @param bodyB the second body to connect
     * @param anchorAx the horizontal local coordinate of the connection of the rod to the first body
     * @param anchorAy the vertical local coordinate of the connection of the rod to the first body
     * @param anchorBx the horizontal local coordinate of the connection of the rod to the second body
     * @param anchorBy the vertical local coordinate of the connection of the rod to the second body
     * @throws IllegalArgumentException if <code>bodyA</code> or <code>bodyB</code> are not in a physical simulation
     * @throws NullPointerException if <code>bodyA</code> or <code>bodyB</code> are <code>null</code>
     *
     * @since 2.2
     */
    public Rod(final Body bodyA, final Body bodyB, final double anchorAx, final double anchorAy,
               final double anchorBx, final double anchorBy) {
        super(bodyA, bodyB);
        final org.jbox2d.dynamics.Body jboxBodyA = bodyA.getJBoxBody();
        final org.jbox2d.dynamics.Body jboxBodyB = bodyB.getJBoxBody();
        final DistanceJointDef def = new DistanceJointDef();
        def.initialize(jboxBodyA, jboxBodyB,
                       jboxBodyA.getWorldPoint(new Vec2((float) anchorAx, (float) anchorAy)),
                       jboxBodyB.getWorldPoint(new Vec2((float) anchorBx, (float) anchorBy)));
        def.collideConnected = true;
        imp = (DistanceJoint) createJBoxJoint(def);
    }

    /**
     * Returns the horizontal local coordinate of the connection point between the rod and the first body.
     *
     * @return the horizontal local coordinate of the connection point between the rod and the first body
     *
     * @since 2.2
     */
    public float getAnchorAx() {
        return imp.getLocalAnchorA().x;
    }

    /**
     * Returns the vertical local coordinate of the connection point between the rod and the first body.
     *
     * @return the vertical local coordinate of the connection point between the rod and the first body
     *
     * @since 2.2
     */
    public float getAnchorAy() {
        return imp.getLocalAnchorA().y;
    }

    /**
     * Returns the horizontal local coordinate of the connection point between the rod and the second body.
     *
     * @return the horizontal local coordinate of the connection point between the rod and the second body
     *
     * @since 2.2
     */
    public float getAnchorBx() {
        return imp.getLocalAnchorB().x;
    }

    /**
     * Returns the vertical local coordinate of the connection point between the rod and the second body.
     *
     * @return the vertical local coordinate of the connection point between the rod and the second body
     *
     * @since 2.2
     */
    public float getAnchorBy() {
        return imp.getLocalAnchorA().y;
    }

    /**
     * Returns the length of the rod.
     *
     * @return the length of the rod
     *
     * @since 2.2
     */
    public float getLength() {
        return imp.getLength();
    }

    /**
     * Sets the length of the rod.
     *
     * @param length the length of the rod
     *
     * @since 2.2
     */
    void setLength(final double length) {
        imp.setLength((float) length);
    }

    @Override
    org.jbox2d.dynamics.joints.Joint getJBoxJoint() {
        return imp;
    }
}
