/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.world;

import ch.jeda.Util;
import ch.jeda.geometry.Collision;
import java.util.List;

public abstract class MovingEntity extends Entity {

    private float damping;
    private double direction;
    private float fx;
    private float fy;
    private boolean hasMass;
    private float inverseMass;
    private float speed;
    private float vx;
    private float vy;

    public MovingEntity() {
        this(0f, 0f);
    }

    protected MovingEntity(float x, float y) {
        super(x, y);
        this.damping = 1f;
    }

    public void addForce(final float fx, final float fy) {
        this.fx += fx;
        this.fy += fy;
    }

    public void clearForce() {
        this.fx = 0f;
        this.fy = 0f;
    }

    public final double getDirection() {
        if (Double.isNaN(this.direction)) {
            this.direction = Math.atan2(this.vy, this.vx);
        }

        return this.direction;
    }

    public final float getSpeed() {
        if (Float.isNaN(this.speed)) {
            this.speed = (float) Math.sqrt(this.vx * this.vx + this.vy * this.vy);
        }

        return this.speed;
    }

    public void setDamping(final float damping) {
        this.damping = damping;
    }

    public final void setDirection(double direction) {
        this.direction = normalizeAngle(direction);
        final float oldSpeed = this.getSpeed();
        this.vx = (float) (oldSpeed * Math.cos(direction));
        this.vy = (float) (oldSpeed * Math.sin(direction));
    }

    public final void setMass(final float mass) {
        if (Float.isInfinite(mass)) {
            throw new IllegalArgumentException("mass");
        }

        if (mass == 0f) {
            this.hasMass = false;
            this.inverseMass = Float.POSITIVE_INFINITY;
        }
        else {
            this.hasMass = true;
            this.inverseMass = 1f / mass;
        }
    }

    public final void setSpeed(final float speed) {
        if (speed < 0f) {
            throw new IllegalArgumentException("speed");
        }

        this.speed = speed;
        final double oldDirection = this.getDirection();
        this.vx = (float) (speed * Math.cos(oldDirection));
        this.vy = (float) (speed * Math.sin(oldDirection));
    }

    @Override
    void move(final float dt) {
        if (this.hasMass) {
            final float damp = (float) Math.pow(this.damping, dt);
            final float ax = this.fx * this.inverseMass;
            final float ay = this.fy * this.inverseMass;
            this.vx = (this.vx + ax * dt) * damp;
            this.vy = (this.vy + ay * dt) * damp;
            this.speed = Float.NaN;
            this.direction = Double.NaN;
        }

        this.translate(this.vx * dt, this.vy * dt);
    }

    static abstract class Contact {

        abstract double getPenetration();

        abstract void resolve(float dt);

        abstract float separatingSpeed();

        abstract void updatePenetration(final List<Contact> contacts);

        abstract void updatePenetration(ContactMM contact);

        abstract void updatePenetration(ContactMI contact);
    }

    static class ContactMM extends Contact {

        private final MovingEntity entity1;
        private final MovingEntity entity2;
        // x component of the contact normal
        private final float nx;
        // y component of the contact normal
        private final float ny;
        // restitution factor
        private final float restitution;
        // length of the contact normal
        private float penetration;
        // x component of the shift to be applied to entity1
        private float s1x;
        // y component of the shift to be applied to entity1
        private float s1y;
        // x component of the shift to be applied to entity2
        private float s2x;
        // y component of the shift to be applied to entity2
        private float s2y;

        ContactMM(final MovingEntity entity1, final MovingEntity entity2, final float dx, final float dy, final float restitution) {
            this.entity1 = entity1;
            this.entity2 = entity2;
            this.restitution = restitution;
            this.penetration = Util.distance(dx, dy);
            if (Util.isZero(this.penetration)) {
                this.nx = 0f;
                this.ny = 0f;
            }
            else {
                this.nx = dx / this.penetration;
                this.ny = dy / this.penetration;
            }
        }

        /**
         * Calculates and returns the relative separating speed.
         *
         * @return relative separating speed
         */
        @Override
        float separatingSpeed() {
            final float dx = this.entity1.vx - this.entity2.vx;
            final float dy = this.entity1.vy - this.entity2.vy;
            return dx * this.nx + dy * this.ny;
        }

        @Override
        double getPenetration() {
            return this.penetration;
        }

        @Override
        void resolve(float dt) {
            this.resolveVelocity(dt);
            this.resolveInterpenetration();
        }

        @Override
        void updatePenetration(final List<Contact> contacts) {
            for (int i = 0; i < contacts.size(); ++i) {
                contacts.get(i).updatePenetration(this);
            }
        }

        @Override
        void updatePenetration(final ContactMM contact) {
            if (this.entity1 == contact.entity1) {
                this.penetration -= contact.s1x * this.nx + contact.s1y * this.ny;
            }

            if (this.entity1 == contact.entity2) {
                this.penetration -= contact.s2x * this.nx + contact.s2y * this.ny;
            }

            if (this.entity2 == contact.entity1) {
                this.penetration += contact.s1x * this.nx + contact.s1y * this.ny;
            }

            if (this.entity2 == contact.entity2) {
                this.penetration += contact.s2x * this.nx + contact.s2y * this.ny;
            }
        }

        @Override
        void updatePenetration(final ContactMI contact) {
            if (this.entity1 == contact.entity1) {
                this.penetration -= contact.sx * this.nx + contact.sy * this.ny;
            }

            if (this.entity2 == contact.entity1) {
                this.penetration += contact.sx * this.nx + contact.sy * this.ny;
            }
        }

        private void resolveVelocity(final float dt) {
            final float vs = this.separatingSpeed();
            if (vs > 0) {
                return;
            }

            float nvs = -vs * this.restitution;
            // Calculate the velocity build-up due to acceleration.
            float ax = 0f;
            float ay = 0f;
            if (this.entity1.hasMass) {
                ax += this.entity1.fx * this.entity1.inverseMass;
                ay += this.entity1.fy * this.entity1.inverseMass;
            }

            if (this.entity2.hasMass) {
                ax += this.entity2.fx * this.entity2.inverseMass;
                ay += this.entity2.fy * this.entity2.inverseMass;
            }

            final float acv = (ax * this.nx + ay * this.ny) * dt;
            // There is a closing velocity due to acceleration build-up.
            // Remove it from the separating velocity.
            if (acv < 0f) {
                nvs += acv * this.restitution;
                if (nvs < 0f) {
                    nvs = 0f;
                }
            }

            // Calculate delta velocity
            final float dv = nvs - vs;

            // calculate total impulse
            final float inverseMass = this.entity1.inverseMass + this.entity2.inverseMass;
            final float ix = this.nx * dv / inverseMass;
            final float iy = this.ny * dv / inverseMass;
            this.entity1.vx += ix * this.entity1.inverseMass;
            this.entity1.vy += iy * this.entity1.inverseMass;
            this.entity2.vx -= ix * this.entity2.inverseMass;
            this.entity2.vy -= iy * this.entity2.inverseMass;
        }

        private void resolveInterpenetration() {
            if (this.penetration < 0) {
                return;
            }

            // calculate total shift
            final float inverseMass = this.entity1.inverseMass + this.entity2.inverseMass;
            final float sx = this.nx * this.penetration / inverseMass;
            final float sy = this.ny * this.penetration / inverseMass;
            this.s1x = sx * this.entity1.inverseMass;
            this.s1y = sy * this.entity1.inverseMass;
            this.s2x = -sx * this.entity2.inverseMass;
            this.s2y = -sy * this.entity2.inverseMass;


            this.entity1.translate(this.s1x, this.s1y);
            this.entity2.translate(this.s2x, this.s2y);
        }

        void check() {
            Collision c = this.entity1.getCollisionShape().collideWith(this.entity2.getCollisionShape());
            if (c != null) {
                System.out.println("Collision not resolved: " + c.penetrationDepth());
            }
        }
    }

    static class ContactMI extends Contact {

        private final MovingEntity entity1;
        private final Entity entity2;
        // x component of the contact normal
        private final float nx;
        // y component of the contact normal
        private final float ny;
        // restitution factor
        private final float restitution;
        // length of the contact normal
        private float penetration;
        // x component of the shift to be applied to entity1
        private float sx;
        // y component of the shift to be applied to entity1
        private float sy;

        ContactMI(final MovingEntity entity1, final Entity entity2,
                  final float dx, final float dy, final float restitution) {
            this.entity1 = entity1;
            this.entity2 = entity2;
            this.restitution = restitution;
            this.penetration = Util.distance(dx, dy);
            if (Math.abs(this.penetration) < 5f * Float.MIN_VALUE) {
                this.nx = 0f;
                this.ny = 0f;
            }
            else {
                this.nx = dx / this.penetration;
                this.ny = dy / this.penetration;
            }
        }

        /**
         * Calculates and returns the relative separating speed.
         *
         * @return relative separating speed
         */
        @Override
        float separatingSpeed() {
            return this.entity1.vx * this.nx + this.entity1.vy * this.ny;
        }

        @Override
        double getPenetration() {
            return this.penetration;
        }

        @Override
        void resolve(float dt) {
            this.resolveVelocity(dt);
            this.resolveInterpenetration();
        }

        @Override
        void updatePenetration(final List<Contact> contacts) {
            for (int i = 0; i < contacts.size(); ++i) {
                contacts.get(i).updatePenetration(this);
            }
        }

        @Override
        void updatePenetration(final ContactMM contact) {
            if (this.entity1 == contact.entity1) {
                this.penetration -= contact.s1x * this.nx + contact.s1y * this.ny;
            }

            if (this.entity1 == contact.entity2) {
                this.penetration -= contact.s2x * this.nx + contact.s2y * this.ny;;
            }
        }

        @Override
        void updatePenetration(final ContactMI contact) {
            if (this.entity1 == contact.entity1) {
                this.penetration -= contact.sx * this.nx + contact.sy * this.ny;
            }
        }

        private void resolveVelocity(final float dt) {
            final float vs = this.separatingSpeed();
            if (vs > 0) {
                return;
            }

            float nvs = -vs * this.restitution;
            // Calculate the velocity build-up due to acceleration.
            float ax = 0f;
            float ay = 0f;
            if (this.entity1.hasMass) {
                ax += this.entity1.fx * this.entity1.inverseMass;
                ay += this.entity1.fy * this.entity1.inverseMass;
            }

            final float acv = (ax * this.nx + ay * this.ny) * dt;
            // There is a closing velocity due to acceleration build-up.
            // Remove it from the separating velocity.
            if (acv < 0f) {
                nvs += acv * this.restitution;
                if (nvs < 0f) {
                    nvs = 0f;
                }
            }

            // Calculate delta velocity
            final float dv = nvs - vs;

            this.entity1.vx += this.nx * dv;
            this.entity1.vy += this.ny * dv;
        }

        private void resolveInterpenetration() {
            if (this.penetration < 0) {
                return;
            }

            this.sx = this.nx * this.penetration;
            this.sy = this.ny * this.penetration;

            this.entity1.translate(this.sx, this.sy);
        }
    }
}
