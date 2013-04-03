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

    /**
     * The non-existing collision. This collision value represents the non-
     * collision. It's meaning is that the concerned shapes do not collide.
     */
    public static final Collision NULL = new Collision();
    /**
     * The collision point. This is the point on the border of the second shape
     * that realises the deepest penetratation of the first shape.
     */
    public final Vector point;
    /**
     * The collision normal. This is the shortest path from the collision point
     * to the border of the first shape. This means that the collision can be
     * resolved by moving the second shape by the collision normal. The length
     * of the collision normal is the <b>penetration depth</b>.
     */
    public final Vector normal;

    /**
     * Checks if this collision is a null collision.
     *
     * @return <tt>true</tt> is this collision is a null collision
     */
    public boolean isNull() {
        return this.normal == null;
    }

    Collision(Vector point, Vector normal) {
        this.point = point;
        this.normal = normal;
    }

    Collision invert() {
        if (!this.isNull()) {
            this.point.add(this.normal);
            this.normal.invert();
        }

        return this;
    }

    private Collision() {
        this.normal = null;
        this.point = null;
    }
}
