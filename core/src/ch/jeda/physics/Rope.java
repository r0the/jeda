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

import org.jbox2d.dynamics.joints.RopeJoint;
import org.jbox2d.dynamics.joints.RopeJointDef;

public final class Rope extends Joint {

    private final RopeJoint imp;

    public Rope(final Body bodyA, final Body bodyB) {
        this(bodyA, bodyB, 0.0, 0.0, 0.0, 0.0);
    }

    public Rope(final Body bodyA, final Body bodyB, final double anchorAx, final double anchorAy,
                final double anchorBx, final double anchorBy) {
        super(bodyA, bodyB);
        final org.jbox2d.dynamics.Body jboxBodyA = bodyA.getJBoxBody();
        final org.jbox2d.dynamics.Body jboxBodyB = bodyB.getJBoxBody();
        final RopeJointDef def = new RopeJointDef();
        def.bodyA = jboxBodyA;
        def.bodyB = jboxBodyB;
        def.localAnchorA.x = (float) anchorAx;
        def.localAnchorA.y = (float) anchorAy;
        def.localAnchorB.x = (float) anchorBx;
        def.localAnchorB.y = (float) anchorBy;
        imp = (RopeJoint) createJBoxJoint(def);
    }

    public float getMaxLength() {
        return imp.getMaxLength();
    }

    public void setMaxLength(final double maxLength) {
        imp.setMaxLength((float) maxLength);
    }

    @Override
    org.jbox2d.dynamics.joints.Joint getJBoxJoint() {
        return imp;
    }
}
