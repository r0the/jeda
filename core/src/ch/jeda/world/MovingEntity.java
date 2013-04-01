/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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

/**
 * EXPERIMENTAL - API MAY CHANGE.
 */
public abstract class MovingEntity extends Entity {

    private Vector velocity;

    protected MovingEntity() {
        this.velocity = Vector.NULL;
    }

    protected MovingEntity(double x, double y) {
        super(x, y);
        this.velocity = Vector.NULL;
    }

    protected MovingEntity(Vector location) {
        super(location);
        this.velocity = Vector.NULL;
    }

    protected final void accelerate(Vector vector) {
        this.velocity = this.velocity.plus(vector);
    }

    public final double getDirection() {
        return this.velocity.direction();
    }

    public final double getSpeed() {
        return this.velocity.length();
    }

    public final void setDirection(double direction) {
        this.velocity = this.velocity.withDirection(direction);
    }

    public final void setSpeed(double pixelsPerSecond) {
        this.velocity = this.velocity.withLength(pixelsPerSecond);
    }

    @Override
    protected void update(World world) {
        this.translate(this.velocity.times(world.getLastStepDuration()));
    }
}
