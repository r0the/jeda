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
public class Circle extends Shape {

    private final float radius;

    public Circle(final float radius) {
        if (radius <= 0f) {
            throw new IllegalArgumentException("radius");
        }

        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    @Override
    protected Collision doCollideWithCircle(final Circle other) {
        final Vector otherCenter = new Vector();
        other.localToWorld(otherCenter);
        this.worldToLocal(otherCenter);
        return this.intersectCircle(otherCenter, this.radius + other.radius - otherCenter.length());
    }

    @Override
    protected Collision doCollideWithLineSegment(final LineSegment other) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Collision doCollideWithPoint(final Point other) {
        final Vector otherLocation = new Vector();
        other.localToWorld(otherLocation);
        this.worldToLocal(otherLocation);
        return this.intersectCircle(otherLocation, this.radius - otherLocation.length());
    }

    @Override
    protected Collision doCollideWithRectangle(final Rectangle other) {
        return other.doCollideWithCircle(this).invert();
    }

    @Override
    protected Collision doCollideWithShape(final Shape other) {
        return other.doCollideWithCircle(this).invert();
    }

    @Override
    protected void doDraw(final Canvas canvas) {
        final Color fillColor = this.getFillColor();
        if (fillColor != null) {
            canvas.setColor(fillColor);
            canvas.fillCircle(0, 0, (int) this.radius);
        }

        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawCircle(0, 0, (int) this.radius);
        }
    }

    @Override
    protected boolean doesContain(final Vector point) {
        this.worldToLocal(point);
        return point.dot(point) < this.radius * this.radius;
    }

    private Collision intersectCircle(Vector normal, double penetrationDepth) {
        if (penetrationDepth > 0) {
            final Vector p = new Vector(normal);
            p.setLength(this.radius);
            normal.setLength(-penetrationDepth);
            return this.createCollision(p, normal);
        }
        else {
            return Collision.NULL;
        }
    }
}
