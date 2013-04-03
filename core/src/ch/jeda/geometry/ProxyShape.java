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

public abstract class ProxyShape extends Figure {

    private Shape collisionShape;

    public Shape getCollisionShape() {
        return this.collisionShape;
    }

    public void setCollisionShape(Shape value) {
        if (this.collisionShape != null) {
            this.collisionShape.setParent(null);
        }

        this.collisionShape = value;
        if (this.collisionShape != null) {
            this.collisionShape.setParent(this);
        }
    }

    @Override
    protected void changed() {
        super.changed();
        if (this.collisionShape != null) {
            this.collisionShape.changed();
        }
    }

    @Override
    protected Collision doCollideWith(Figure other) {
        return other.doCollideWithShape(this.collisionShape).invert();
    }

    @Override
    protected Collision doCollideWithShape(Shape other) {
        return this.collisionShape.doCollideWithShape(other);
    }

    @Override
    protected boolean doesContain(Vector point) {
        return this.collisionShape.doesContain(point);
    }
}
