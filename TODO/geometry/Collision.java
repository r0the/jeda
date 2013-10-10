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

import ch.jeda.Util;
import java.io.Serializable;

/**
 * This class represents a collision between two shapes.
 */
public final class Collision implements Serializable {

    private final float penetrationDepth;
    private Shape shape1;
    private Shape shape2;
    private float normalX;
    private float normalY;

    public float getNormalX() {
        return this.normalX;
    }

    public float getNormalY() {
        return this.normalY;
    }

    public float getPenetrationDepth() {
        return this.penetrationDepth;
    }

    public Shape getShape1() {
        return this.shape1;
    }

    public Shape getShape2() {
        return this.shape2;
    }

    Collision(final Shape shape1, final Shape shape2, final float normalX, final float normalY,
              final float penetrationDepth) {
        assert Util.isZero(normalX * normalX + normalY * normalY - 1f);
        assert penetrationDepth > 0f;

        this.penetrationDepth = penetrationDepth;
        this.normalX = normalX;
        this.normalY = normalY;
        this.shape1 = shape1;
        this.shape2 = shape2;
    }
}
