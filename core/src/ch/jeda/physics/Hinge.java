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
import org.jbox2d.dynamics.joints.RevoluteJointDef;

/**
 * A joint that connects two bodies widh a hinge. Both bodies can rotate around the hinge point
 *
 * @since 2.2
 */
public final class Hinge extends Joint {

    private final org.jbox2d.dynamics.joints.RevoluteJoint imp;

    /**
     * Constructs a hinge that connects the centers of two bodies.
     *
     * @param bodyA the first body to connect
     * @param bodyB the second body to connect
     * @throws IllegalArgumentException if <code>bodyA</code> or <code>bodyB</code> are not in a physical simulation
     * @throws NullPointerException if <code>bodyA</code> or <code>bodyB</code> are <code>null</code>
     *
     * @since 2.2
     */
    public Hinge(final Body bodyA, final Body bodyB) {
        this(bodyA, bodyB, 0.0, 0.0);
    }

    /**
     * Construct a hinge that connects two bodies.
     *
     * @param bodyA the first body to connect
     * @param bodyB the second body to connect
     * @param x the horizontal local coordinate of the connection of the two bodies
     * @param y the vertical local coordinate of the connection of the two bodies
     * @throws IllegalArgumentException if <code>bodyA</code> or <code>bodyB</code> are not in a physical simulation
     * @throws NullPointerException if <code>bodyA</code> or <code>bodyB</code> are <code>null</code>
     *
     * @since 2.2
     */
    public Hinge(final Body bodyA, final Body bodyB, final double x, final double y) {
        super(bodyA, bodyB);
        final org.jbox2d.dynamics.Body jboxBodyA = bodyA.getJBoxBody();
        final org.jbox2d.dynamics.Body jboxBodyB = bodyB.getJBoxBody();
        final RevoluteJointDef def = new RevoluteJointDef();
        def.initialize(jboxBodyB, jboxBodyB, jboxBodyA.getWorldPoint(new Vec2((float) x, (float) y)));
        imp = (org.jbox2d.dynamics.joints.RevoluteJoint) createJBoxJoint(def);
    }

    public float getAnchorAx() {
        return imp.getLocalAnchorA().x;
    }

    public float getAnchorAy() {
        return imp.getLocalAnchorA().y;
    }

    public float getAnchorBx() {
        return imp.getLocalAnchorB().x;
    }

    public float getAnchorBy() {
        return imp.getLocalAnchorA().y;
    }

    @Override
    org.jbox2d.dynamics.joints.Joint getJBoxJoint() {
        return imp;
    }
}
