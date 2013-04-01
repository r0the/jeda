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
import ch.jeda.geometry.Collision;
import ch.jeda.geometry.AbstractFigure;
import ch.jeda.geometry.Shape;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Events;

/**
 * This class represents an object in a simulated world. An object has a
 * location and an orientation. It may have a visual representation, can change
 * it's location and orientation. It has a bounding shape that determines
 * whether it collides with other entities.
 */
public abstract class Entity extends AbstractFigure {

    private Shape collisionShape;

    public Entity() {
        this(Vector.NULL);
    }

    public Entity(double x, double y) {
        this(new Vector(x, y));
    }

    public Entity(Vector location) {
        this.setLocation(location);
    }

    public final Shape getCollisionShape() {
        return this.collisionShape;
    }

    public final void setCollisionShape(Shape shape) {
        this.collisionShape = shape;
        if (this.collisionShape != null) {
            this.collisionShape.setLocation(this.getLocation());
        }
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append(this.getLocation());
        return result.toString();
    }

    @Override
    protected final boolean doesContain(Vector location) {
        return this.collisionShape.contains(location);
    }

    @Override
    protected final Collision doCollideWith(AbstractFigure other) {
        return this.collisionShape.collideWith(other);
    }

    @Override
    protected final Collision doCollideWithShape(Shape other) {
        return this.collisionShape.collideWith(other);
    }

    @Override
    protected void onLocationChanged() {
        if (this.collisionShape != null) {
            this.collisionShape.setLocation(this.getLocation());
        }
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

    void drawDebugOverlay(Canvas canvas) {
        if (this.collisionShape != null) {
            this.collisionShape.setFillColor(DEBUG_FILL_COLOR);
            this.collisionShape.setOutlineColor(DEBUG_OUTLINE_COLOR);
            this.collisionShape.draw(canvas);
        }
    }
}
