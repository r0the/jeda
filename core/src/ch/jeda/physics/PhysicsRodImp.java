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
 */package ch.jeda.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

class PhysicsRodImp implements RodImp {

    private final Physics physics;
    private final DistanceJoint imp;

    public PhysicsRodImp(final Physics physics, final Rod rod, final RodImp oldImp) {
        this.physics = physics;
        final org.jbox2d.dynamics.Body jboxBodyA = rod.getBodyA().getJBoxBody();
        final org.jbox2d.dynamics.Body jboxBodyB = rod.getBodyB().getJBoxBody();
        final DistanceJointDef def = new DistanceJointDef();
        def.initialize(jboxBodyA, jboxBodyB,
                       jboxBodyA.getWorldPoint(new Vec2(oldImp.getAnchorAx(), oldImp.getAnchorAy())),
                       jboxBodyB.getWorldPoint(new Vec2(oldImp.getAnchorBx(), oldImp.getAnchorBy())));
        def.collideConnected = true;
        imp = (DistanceJoint) physics.createJBoxJoint(def);
    }

    @Override
    public boolean belongsTo(final Physics physics) {
        return this.physics == physics;
    }

    @Override
    public void destroy() {
        this.physics.destroyJBoxJoint(imp);
    }

    @Override
    public float getAnchorAx() {
        return imp.getLocalAnchorA().x;
    }

    @Override
    public float getAnchorAy() {
        return imp.getLocalAnchorA().y;
    }

    @Override
    public float getAnchorBx() {
        return imp.getLocalAnchorB().x;
    }

    @Override
    public float getAnchorBy() {
        return imp.getLocalAnchorB().y;
    }

    @Override
    public float getLength() {
        return imp.getLength();
    }

    @Override
    public void setLength(final float length) {
        imp.setLength(length);
    }
}
