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

import ch.jeda.Log;

/**
 * Represents a connection of two bodies. A joint imposes restrictions of movement and position on the two bodies.
 *
 * @since 2.2
 */
public abstract class Joint {

    private final Body bodyA;
    private final Body bodyB;
    private final Physics physics;
    private boolean destroyed;

    Joint(final Body bodyA, final Body bodyB) {
        if (bodyA.getPhysics() == null || bodyB.getPhysics() == null) {
            Log.e("Ein Body muss in eine PhysicsView eingefügt werden, bevor eine Verbindung erstellt werden kann.");
            throw new IllegalStateException("Cannot create joint for detachted body.");
        }

        this.bodyA = bodyA;
        this.bodyB = bodyB;
        this.physics = bodyA.getPhysics();
        this.destroyed = false;
    }

    /**
     * Destroys this joint. A destroyed joint has no effect and cannot be used anymore.
     *
     * @since 2.2
     */
    public final void destroy() {
        physics.destroyJBoxJoint(getJBoxJoint());
        destroyed = true;
    }

    /**
     * Returns the first body connected by this joint.
     *
     * @return the first body connected by this joint
     *
     * @since 2.2
     */
    public final Body getBodyA() {
        return bodyA;
    }

    /**
     * Returns the second body connected by this joint.
     *
     * @return the second body connected by this joint
     *
     * @since 2.2
     */
    public final Body getBodyB() {
        return bodyB;
    }

    /**
     * Checks if this joint has been destroyed.
     *
     * @return <code>true</code> if this joint has been destroyed, otherwise <code>false</code>
     *
     * @since 2.2
     */
    public final boolean isDestroyed() {
        return destroyed;
    }

    org.jbox2d.dynamics.joints.Joint createJBoxJoint(org.jbox2d.dynamics.joints.JointDef jointDef) {
        return physics.createJBoxJoint(jointDef);
    }

    abstract org.jbox2d.dynamics.joints.Joint getJBoxJoint();
}
