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

import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.Vector;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

public class HalfPlane extends Shape {

    public Vector normal() {
        return this.transformLocation(new Vector(0, 1));
    }

    @Override
    protected void doDraw(Canvas canvas) {
        Color color = this.getFillColor();
        if (color != null) {
            canvas.setColor(color);
            Location start = new Location((int) -this.getLocation().x, 0);
            Location end = new Location(canvas.getSize().width, 0);
            canvas.drawLine(start, end);
        }

        color = this.getOutlineColor();
        if (color != null) {
            canvas.setColor(color);
            Location start = new Location((int) -this.getLocation().x, 0);
            Size size = canvas.getSize();
            canvas.fillRectangle(start, size);
        }

    }

    @Override
    protected boolean doesContain(Vector point) {
        point = this.toLocal(point);
        return point.y > 0;
    }

    @Override
    protected Collision doCollideWithCircle(Circle other) {
        Vector center = this.toLocal(other.origin());
        return this.intersectCircle(center.x, center.y + other.getRadius());
    }

    @Override
    protected Collision doCollideWithHalfPlane(HalfPlane other) {
        return Collision.NULL;
        //    throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Collision doCollideWithLineSegment(LineSegment other) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Collision doCollideWithPoint(Point other) {
        Vector center = this.toLocal(other.origin());
        return this.intersectCircle(center.x, center.y);
    }

    @Override
    protected Collision doCollideWithRectangle(Rectangle other) {
        return Collision.NULL;
        //   throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Collision doCollideWithShape(Shape other) {
        return other.doCollideWithHalfPlane(this).inverted();
    }

    private Collision intersectCircle(double x, double penetrationDepth) {
        if (penetrationDepth > 0) {
            Vector p = new Vector(x, penetrationDepth);
            Vector n = new Vector(0, -penetrationDepth);
            return this.createCollision(p, n);
        }
        else {
            return Collision.NULL;
        }
    }
}
