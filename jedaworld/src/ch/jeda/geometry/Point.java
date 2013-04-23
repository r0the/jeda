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
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

/**
 * Represents a point on the two-dimensional plane.
 */
public class Point extends Shape {

    private static final int MARKER_RADIUS = 10;

    public Point() {
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Point(x=");
        result.append(this.getX());
        result.append(", y=");
        result.append(this.getY());
        result.append(")");
        return result.toString();
    }

    @Override
    protected boolean doesContain(final Vector point) {
        return false;
    }

    @Override
    protected void doDraw(final Canvas canvas) {
        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawLine(0, -MARKER_RADIUS, 0, MARKER_RADIUS);
            canvas.drawLine(-MARKER_RADIUS, 0, MARKER_RADIUS, 0);
        }
    }

    @Override
    Collision doCollideWithCircle(final Circle other) {
        return Collision.invert(other.doCollideWithPoint(this));
    }

    @Override
    Collision doCollideWithPoint(final Point other) {
        return null;
    }

    @Override
    Collision doCollideWithRectangle(final Rectangle other) {
        return Collision.invert(other.doCollideWithPoint(this));
    }

    @Override
    Collision doCollideWithShape(final Shape other) {
        return Collision.invert(other.doCollideWithPoint(this));
    }
}
