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

import java.io.Serializable;

/**
 * This class represents a collision between two shapes.
 */
public final class Collision implements Serializable {

    private float p1x;
    private float p1y;
    private float p2x;
    private float p2y;

    /**
     * The x coordinate of the collision point belonging to the first shape.
     *
     * @return x coordinate of the collision point belonging to the first shape
     */
    public float get1X() {
        return this.p1x;
    }

    /**
     * The y coordinate of the collision point belonging to the first shape.
     *
     * @return y coordinate of the collision point belonging to the first shape
     */
    public float get1Y() {
        return this.p1y;
    }

    /**
     * The x coordinate of the collision point belonging to the second shape.
     *
     * @return x coordinate of the collision point belonging to the second shape
     */
    public float get2X() {
        return this.p2x;
    }

    /**
     * The y coordinate of the collision point belonging to the second shape.
     *
     * @return y coordinate of the collision point belonging to the second shape
     */
    public float get2Y() {
        return this.p2y;
    }

    public float penetrationDepth() {
        final float dx = this.p2x - this.p1x;
        final float dy = this.p2y - this.p1y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    static Collision invert(final Collision collision) {
        if (collision != null) {
            final float tx = collision.p1x;
            collision.p1x = collision.p2x;
            collision.p2x = tx;
            final float ty = collision.p1y;
            collision.p1y = collision.p2y;
            collision.p2y = ty;
        }

        return collision;
    }

    Collision(final float p1x, final float p1y,
              final float p2x, final float p2y) {
        this.p1x = p1x;
        this.p1y = p1y;
        this.p2x = p2x;
        this.p2y = p2y;
    }
}
