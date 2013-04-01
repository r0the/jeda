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
import ch.jeda.Vector;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

public class Circle extends Shape {

    private final double radius;

    public Circle(double radius) {
        if (radius <= 0.0) {
            throw new IllegalArgumentException("radius");
        }

        this.radius = radius;
    }

    public double getRadius() {
        return this.radius;
    }

    @Override
    public String toString() {
        final Vector center = this.origin();
        final StringBuilder result = new StringBuilder();
        result.append("Circle(x=");
        result.append(center.x);
        result.append(", y=");
        result.append(center.y);
        result.append(", r=");
        result.append(this.radius);
        result.append(")");
        return result.toString();
    }

    @Override
    protected void doDraw(Canvas canvas) {
        final Color fillColor = this.getFillColor();
        if (fillColor != null) {
            canvas.setColor(fillColor);
            canvas.fillCircle(Location.ORIGIN, (int) this.radius);
        }

        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawCircle(Location.ORIGIN, (int) this.radius);
        }
    }

    @Override
    protected boolean doesContain(Vector point) {
        return point.dot(point) < this.radius * this.radius;
    }

    @Override
    protected Collision doCollideWithCircle(Circle other) {
        final Vector otherCenter = this.toLocal(other.origin());
        return this.intersectCircle(otherCenter, this.radius + other.radius - otherCenter.length());
    }

    @Override
    protected Collision doCollideWithHalfPlane(HalfPlane other) {
        return other.doCollideWithCircle(this).inverted();
    }

    @Override
    protected Collision doCollideWithLineSegment(LineSegment other) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Collision doCollideWithPoint(Point other) {
        final Vector normal = this.toLocal(other.origin());
        return this.intersectCircle(normal, this.radius - normal.length());
    }

    @Override
    protected Collision doCollideWithRectangle(Rectangle other) {
        return other.doCollideWithCircle(this).inverted();
    }

    @Override
    protected Collision doCollideWithShape(Shape other) {
        return other.doCollideWithCircle(this).inverted();
    }

    private Collision intersectCircle(Vector normal, double penetrationDepth) {
        if (penetrationDepth > 0) {
            final Vector p = normal.withLength(this.radius);
            final Vector n = normal.withLength(-penetrationDepth);
            return this.createCollision(p, n);
        }
        else {
            return Collision.NULL;
        }
    }
}
