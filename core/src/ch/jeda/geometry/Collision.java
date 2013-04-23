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
package ch.jeda.geometry;

import ch.jeda.Vector;
import java.io.Serializable;

/**
 * This class represents a collision between two shapes. It provides the
 * following information about the collision:
 * <ul>
 * <li> The <b>collision point</b> is the point on the border of the second
 * tested shape that realises the deepest penetratation of the first shape.</li>
 * <li> The <b>collision normal</b> is the shortest path from the collision
 * point to the border of the first shape. This means that the collision can be
 * resolved by moving the second shape by the collision normal. The length of
 * the collision normal is the <b>penetration depth</b>.</li>
 * </ul>
 */
public final class Collision implements Serializable {

    private Vector point1;
    private Vector point2;

    public Vector getPoint1() {
        return this.point1;
    }

    public Vector getPoint2() {
        return this.point2;
    }

    public Vector normal() {
        return new Vector(this.point2.x - this.point1.x,
                          this.point2.y - this.point1.y);
    }

    static Collision invert(final Collision collision) {
        if (collision != null) {
            final Vector temp = collision.point1;
            collision.point1 = collision.point2;
            collision.point2 = temp;
        }

        return collision;
    }

    Collision(final Vector point1, final Vector point2) {
        this.point1 = point1;
        this.point2 = point2;
    }
}
