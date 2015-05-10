/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
package ch.jeda.physics;

import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

/**
 * Represents a circle shape.
 *
 * @since 2.0
 */
public class CircleShape extends Shape {

    private final double radius;

    /**
     * Constructs a circle shape.
     *
     * @param radius the circle's radius
     *
     * @since 2.0
     */
    public CircleShape(final double radius) {
        this.radius = radius;
    }

    @Override
    void draw(final Canvas canvas) {
        canvas.setColor(Color.RED);
        canvas.drawCircle(0, 0, this.radius);
    }

    @Override
    org.jbox2d.collision.shapes.Shape createImp(final double scale) {
        final org.jbox2d.collision.shapes.CircleShape result = new org.jbox2d.collision.shapes.CircleShape();
        result.m_radius = (float) (this.radius / scale);
        return result;
    }
}
