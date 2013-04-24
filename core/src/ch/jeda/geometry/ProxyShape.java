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
package ch.jeda.geometry;

import ch.jeda.Vector;

/**
 * Represents a figure that delegates its collision detection to a shape.
 */
public abstract class ProxyShape extends Figure {

    private Shape collisionShape;

    /**
     * Returns the collision shape of the figure.
     *
     * @return collision shape of the figure
     */
    public final Shape getCollisionShape() {
        return this.collisionShape;
    }

    /**
     * Sets the colision shape of the figure. The collision shape is used to
     * detect collisions with this figure.
     *
     * @param value the collision shape
     */
    public final void setCollisionShape(final Shape value) {
        if (this.collisionShape != null) {
            this.collisionShape.setParent(null);
        }

        this.collisionShape = value;
        if (this.collisionShape != null) {
            this.collisionShape.setParent(this);
        }
    }

    /**
     * Constructs a proxy shape. Initially, no collision shape is set.
     */
    protected ProxyShape() {
    }

    @Override
    void changed() {
        super.changed();
        if (this.collisionShape != null) {
            this.collisionShape.changed();
        }
    }

    @Override
    final Collision doCollideWith(final Figure other) {
        if (this.collisionShape == null) {
            return null;
        }
        else {
            return Collision.invert(other.doCollideWithShape(this.collisionShape));
        }
    }

    @Override
    final Collision doCollideWithShape(final Shape other) {
        if (this.collisionShape == null) {
            return null;
        }
        else {
            return this.collisionShape.doCollideWithShape(other);
        }
    }

    @Override
    final boolean doesContain(final Vector point) {
        return this.collisionShape.doesContain(point);
    }
}
