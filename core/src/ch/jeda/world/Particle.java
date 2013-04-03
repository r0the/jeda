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

import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

public class Particle extends Entity {

    private static final double DEFAULT_DAMPING = 0.9995;
    private double damping;
    private double fx;
    private double fy;
    private final double inverseMass;
    private double vx;
    private double vy;

    public Particle(double x, double y, double mass) {
        this(x, y, mass, DEFAULT_DAMPING);
    }

    public Particle(double x, double y, double mass, double damping) {
        super(x, y);
        this.damping = damping;
        if (Double.isInfinite(mass)) {
            this.inverseMass = 0;
        }
        else {
            this.inverseMass = 1d / mass;
        }
    }

    public void addForce(double fx, double fy) {
        if (!hasInfiniteMass()) {
            this.fx = this.fx + fx;
            this.fy = this.fy + fy;
        }
    }

    public void addVelocity(double vx, double vy) {
        if (!hasInfiniteMass()) {
            this.vx = this.vx + vx;
            this.vy = this.vy + vy;
        }
    }

    public void clearForce() {
        this.fx = 0;
        this.fy = 0;
    }

    public final double getInverseMass() {
        return inverseMass;
    }

//    public final Vector getVelocity() {
//        return new Vector();
//    }
    public boolean hasInfiniteMass() {
        return inverseMass == 0;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    @Override
    protected void update(World world) {
        if (!this.hasInfiniteMass()) {
            final double dt = world.getLastStepDuration();
            final double damp = Math.pow(damping, dt);
            final double ax = this.fx * this.inverseMass;
            final double ay = this.fy * this.inverseMass;
            this.vx = (this.vx + ax * dt) * damp;
            this.vy = (this.vy + ay * dt) * damp;
            this.translate(this.vx * dt, this.vy * dt);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append("(");
        result.append("p=");
        result.append(getLocation());
        result.append(")");
        return result.toString();
    }

    @Override
    protected void doDraw(Canvas canvas) {
        canvas.setColor(Color.BLACK);
        canvas.fillRectangle(0, 0, 2, 2);
    }
}
