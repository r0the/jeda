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

/**
 * Represents a connection of two bodies. A joint imposes restrictions of movement and position on the two bodies.
 *
 * @since 2.2
 */
public abstract class Joint {

    private final Body bodyA;
    private final Body bodyB;

    Joint(final Body bodyA, final Body bodyB) {
        if (bodyA == null) {
            throw new NullPointerException("bodyA");
        }

        if (bodyB == null) {
            throw new NullPointerException("bodyB");
        }

        this.bodyA = bodyA;
        this.bodyB = bodyB;
    }

    /**
     * Destroys this joint. A destroyed joint has no effect and cannot be used anymore.
     *
     * @since 2.2
     */
    public final void destroy() {
        setPhysics(null);
        bodyA.removeJoint(this);
        bodyB.removeJoint(this);
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

    Body getOtherBody(final Body body) {
        if (body == bodyA) {
            return bodyB;
        }
        else {
            return bodyA;
        }
    }

    abstract boolean setPhysics(final Physics physics);
}
