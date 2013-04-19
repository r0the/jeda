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

    public final Shape getCollisionShape() {
        return this.collisionShape;
    }

    public final void setCollisionShape(final Shape value) {
        if (this.collisionShape != null) {
            this.collisionShape.setParent(null);
        }

        this.collisionShape = value;
        if (this.collisionShape != null) {
            this.collisionShape.setParent(this);
        }
    }

    @Override
    protected final Collision doCollideWith(final Figure other) {
        return other.doCollideWithShape(this.collisionShape).invert();
    }

    @Override
    protected final Collision doCollideWithShape(final Shape other) {
        return this.collisionShape.doCollideWithShape(other);
    }

    @Override
    protected final boolean doesContain(final Vector point) {
        return this.collisionShape.doesContain(point);
    }
}
