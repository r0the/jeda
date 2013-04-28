/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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

import ch.jeda.Transformation;
import ch.jeda.geometry.Collision;
import ch.jeda.geometry.ProxyShape;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

/**
 * This class represents an object in a simulated world. An object has a
 * location and an orientation. It may have a visual representation, can change
 * it's location and orientation. It has a bounding shape that determines
 * whether it collides with other entities.
 */
public abstract class Entity extends ProxyShape {

    protected static final Color DEBUG_FILL_COLOR = new Color(255, 0, 0, 70);
    protected static final Color DEBUG_OUTLINE_COLOR = Color.RED;
    protected static final Color DEBUG_TEXT_COLOR = Color.RED;
    private boolean canMove;
    private float damping;
    private double direction;
    private float fx;
    private float fy;
    private boolean hasMass;
    private float inverseMass;
    private float speed;
    private float vx;
    private float vy;

    public void addForce(final float fx, final float fy) {
        if (this.canMove) {
            this.fx += fx;
            this.fy += fy;
        }
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
            this.canMove = false;
            this.hasMass = true;
            this.inverseMass = 0f;
        }
        else if (mass == 0f) {
            this.canMove = true;
            this.hasMass = false;
            this.inverseMass = Float.POSITIVE_INFINITY;
        }
        else {
            this.canMove = true;
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
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(this.getPosition());
        return result.toString();
    }

    /**
     * Constructs an entity. Initially, the entity is positioned at the origin.
     * It has no velocity and no mass.
     *
     * @since 1
     */
    protected Entity() {
        this.canMove = true;
        this.damping = 1f;
    }

    protected Entity(float x, float y) {
        this();
        this.setPosition(x, y);
    }

    /**
     * Returns the paint order of this entity. Actors with a lower paint order
     * are painted first, i.e. below actors with a higher paint order. Override
     * this method to change the paint order of your actor subclass. Each
     * implementation of this method should always return the same value.
     *
     * @return paint order of this actor class
     */
    protected int paintOrder() {
        return 0;
    }

    protected void update(World world) {
    }

    protected int updateOrder() {
        return 0;
    }

    boolean canMove() {
        return this.canMove;
    }

    void drawCollisionShape(Canvas canvas) {
        if (this.getCollisionShape() != null) {
            final Transformation oldTransformation = canvas.getTransformation();
            this.getCollisionShape().setFillColor(DEBUG_FILL_COLOR);
            this.getCollisionShape().setOutlineColor(DEBUG_OUTLINE_COLOR);
            this.getCollisionShape().draw(canvas);
            canvas.setTransformation(oldTransformation);
        }
    }

    void drawDebugInfo(Canvas canvas) {
        final Transformation oldTransformation = canvas.getTransformation();
        this.setTransformation(canvas);
        canvas.setColor(DEBUG_TEXT_COLOR);
        canvas.setFontSize(12);
        canvas.drawText(15, 15, "pos=" + this.getPosition());
        canvas.drawText(15, 30, "rot=" + this.getRotation());
        canvas.drawText(15, 45, "dir=" + this.getDirection());
        canvas.drawText(15, 60, "force=(" + this.fx + ", " + this.fy + ")");
        canvas.setTransformation(oldTransformation);
    }

    void move(final float dt) {
        if (this.canMove) {
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
    }

    static class Contact {

        private final Entity entity1;
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
        private float s1x;
        // y component of the shift to be applied to entity1
        private float s1y;
        // x component of the shift to be applied to entity2
        private float s2x;
        // y component of the shift to be applied to entity2
        private float s2y;

        Contact(final Entity entity1, final Entity entity2,
                final Collision collision, final float restitution) {
            this.entity1 = entity1;
            this.entity2 = entity2;
            this.restitution = restitution;
            final float dx = collision.get2X() - collision.get1X();
            final float dy = collision.get2Y() - collision.get1Y();
            this.penetration = (float) Math.sqrt(dx * dx + dy * dy);
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
        float separatingSpeed() {
            final float dvx = this.entity1.vx - this.entity2.vx;
            final float dvy = this.entity1.vy - this.entity2.vy;
            return (float) (dvx * this.nx + dvy * this.ny);
        }

        double getPenetration() {
            return this.penetration;
        }

        void resolve(float dt) {
            this.resolveVelocity(dt);
            this.resolveInterpenetration();
        }

        void updatePenetration(final Entity.Contact contact) {
            if (this.entity1 == contact.entity1) {
                this.penetration -= contact.s1x * this.nx + contact.s1y * this.ny;
            }
            if (this.entity1 == contact.entity2) {
                this.penetration -= contact.s2x * this.nx + contact.s2y * this.ny;;
            }
            if (this.entity2 == contact.entity1) {
                this.penetration += contact.s1x * this.nx + contact.s1y * this.ny;
            }
            if (this.entity2 == contact.entity2) {
                this.penetration += contact.s2x * this.nx + contact.s2y * this.ny;;
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
            if (this.entity1.hasMass && this.entity1.canMove) {
                ax += this.entity1.fx * this.entity1.inverseMass;
                ay += this.entity1.fy * this.entity1.inverseMass;
            }

            if (this.entity2.hasMass && this.entity2.canMove) {
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
            if (!this.entity1.canMove) {
                // entity1 is immobile
                this.entity2.vx -= this.nx * dv;
                this.entity2.vy -= this.ny * dv;
            }
            else if (!this.entity2.canMove) {
                // entity2 is immobile
                this.entity1.vx += this.nx * dv;
                this.entity1.vy += this.ny * dv;
            }
            else {
                // calculate total impulse
                final float inverseMass = this.entity1.inverseMass + this.entity2.inverseMass;
                final float ix = this.nx * dv / inverseMass;
                final float iy = this.ny * dv / inverseMass;
                this.entity1.vx += ix * this.entity1.inverseMass;
                this.entity1.vy += iy * this.entity1.inverseMass;
                this.entity2.vx -= ix * this.entity2.inverseMass;
                this.entity2.vy -= iy * this.entity2.inverseMass;
            }
        }

        private void resolveInterpenetration() {
            if (this.penetration < 0) {
                return;
            }

            if (!this.entity1.canMove) {
                this.s1x = 0f;
                this.s1y = 0f;
                this.s2x = -this.nx * this.penetration;
                this.s2y = -this.ny * this.penetration;
            }
            else if (!this.entity2.canMove) {
                this.s1x = this.nx * this.penetration;
                this.s1y = this.ny * this.penetration;
                this.s2x = 0f;
                this.s2y = 0f;
            }
            else {
                // calculate total shift
                final float inverseMass = this.entity1.inverseMass + this.entity2.inverseMass;
                final float sx = this.nx * this.penetration / inverseMass;
                final float sy = this.ny * this.penetration / inverseMass;
                this.s1x = sx * this.entity1.inverseMass;
                this.s1y = sy * this.entity1.inverseMass;
                this.s2x = -sx * this.entity2.inverseMass;
                this.s2y = -sy * this.entity2.inverseMass;
            }

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
}
