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
 * OK
 */
public class Point extends Shape {

    private static final int MARKER_RADIUS = 10;

    public Point() {
    }

    @Override
    protected boolean doesContain(Vector point) {
        return false;
    }

    @Override
    protected void doDraw(Canvas canvas) {
        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawLine(0, -MARKER_RADIUS, 0, MARKER_RADIUS);
            canvas.drawLine(-MARKER_RADIUS, 0, MARKER_RADIUS, 0);
        }
    }

    @Override
    protected Collision doCollideWithCircle(Circle other) {
        return other.doCollideWithPoint(this).invert();
    }

    @Override
    protected Collision doCollideWithLineSegment(LineSegment other) {
        return Collision.NULL;
    }

    @Override
    protected Collision doCollideWithPoint(Point other) {
        return Collision.NULL;
    }

    @Override
    protected Collision doCollideWithRectangle(Rectangle other) {
        return other.doCollideWithPoint(this).invert();
    }

    @Override
    protected Collision doCollideWithShape(Shape other) {
        return other.doCollideWithPoint(this).invert();
    }
}
