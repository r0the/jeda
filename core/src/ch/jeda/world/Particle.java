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

import ch.jeda.Vector;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

public class Particle extends MovingEntity {

    private static final double DEFAULT_DAMPING = 0.9995;
    private Vector acceleration;
    private double damping;
    private Vector force;
    private final double inverseMass;
    private Vector velocity;

    public Particle(Vector location, double mass) {
        this(location, mass, DEFAULT_DAMPING);
    }

    public Particle(Vector location, double mass, double damping) {
        super(location);
        acceleration = Vector.NULL;
        this.damping = damping;
        force = Vector.NULL;
        if (Double.isInfinite(mass)) {
            this.inverseMass = 0;
        }
        else {
            this.inverseMass = 1d / mass;
        }
        velocity = Vector.NULL;
    }

    public void addForce(Vector force) {
        if (!hasInfiniteMass()) {
            this.force = this.force.plus(force);
        }
    }

    public void addVelocity(Vector velocity) {
        if (!hasInfiniteMass()) {
            this.velocity = this.velocity.plus(velocity);
        }
    }

    public void clearForce() {
        this.force = Vector.NULL;
    }

    public final double getInverseMass() {
        return inverseMass;
    }

    public final Vector getVelocity() {
        return velocity;
    }

    public boolean hasInfiniteMass() {
        return inverseMass == 0;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    @Override
    protected void update(World world) {
        if (!hasInfiniteMass()) {
            double dt = world.getLastStepDuration();
            Vector newPosition = getLocation().plus(velocity.times(dt));
            Vector acc = acceleration.plus(force.times(inverseMass));
            Vector newVelocity = velocity.plus(acc.times(dt));
            setLocation(newPosition);
            double damp = Math.pow(damping, dt);
            velocity = newVelocity.times(damp);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append("(");
        result.append("p=");
        result.append(getLocation());
        result.append(", v=");
        result.append(velocity);
        result.append(", a=");
        result.append(acceleration);
        result.append(")");
        return result.toString();
    }

    @Override
    protected void doDraw(Canvas canvas) {
        canvas.setColor(Color.BLACK);
        canvas.fillRectangle(0, 0, 2, 2);
    }
}
