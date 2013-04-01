/*
 * Copyright (C) 2010 - 2013 by Stefan Rothe
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

public class LineSegment extends Shape {

    private final Vector start;
    private final Vector end;

    public LineSegment(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected boolean doesContain(Vector point) {
        return false;
    }

    @Override
    protected Collision doCollideWithCircle(Circle other) {
        return other.doCollideWithLineSegment(this).inverted();
    }

    @Override
    protected Collision doCollideWithHalfPlane(HalfPlane other) {
        return other.doCollideWithLineSegment(this).inverted();
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
        return other.doCollideWithLineSegment(this).inverted();
    }

    @Override
    protected Collision doCollideWithShape(Shape other) {
        return other.doCollideWithLineSegment(this).inverted();
    }

    @Override
    protected void doDraw(Canvas canvas) {
        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawLine(this.start.toLocation(), this.end.toLocation());
        }
    }
}
