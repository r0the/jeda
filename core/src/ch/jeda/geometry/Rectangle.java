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

public class Rectangle extends AbstractPolygon {

    private final Circle circumscribedCircle;
    private final float halfWidth;
    private final float halfHeight;
    private final float height;
    private final float width;

    public Rectangle(final float width, final float height) {
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
        this.height = height;
        this.width = width;
        final float diameter = (float) Math.sqrt(width * width + height * height);
        this.circumscribedCircle = new Circle(diameter / 2f);
    }

    @Override
    protected Vector[] axes() {
        final Vector[] result = new Vector[2];
        result[0] = new Vector(1, 0);
        result[1] = new Vector(0, 1);
        for (Vector v : result) {
            this.localToWorld(v);
        }

        return result;
    }

    @Override
    protected void doDraw(final Canvas canvas) {
        final Color fillColor = this.getFillColor();
        if (fillColor != null) {
            canvas.setColor(fillColor);
            canvas.fillRectangle((int) -this.halfWidth, (int) -this.halfHeight,
                                 (int) this.width, (int) this.height);
        }

        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawRectangle((int) -this.halfWidth, (int) -this.halfHeight,
                                 (int) this.width, (int) this.height);
        }
    }

    @Override
    protected Collision doCollideWithCircle(final Circle other) {
        final Vector center = new Vector();
        other.localToWorld(center);
        this.worldToLocal(center);
        final float radius = other.getRadius();
        final float dx = Math.abs(center.x) - this.halfWidth;
        final float dy = Math.abs(center.y) - this.halfHeight;
        if (dx > radius) {
            return Collision.NULL;
        }

        if (dy > radius) {
            return Collision.NULL;
        }

        if (dx <= 0 && dy <= 0) {
            if (Math.abs(dx) < Math.abs(dy)) {
                final double sig = Math.signum(center.x);
                return this.createCollision(new Vector(this.halfWidth * sig, center.y),
                                            new Vector(-sig * (radius - dx), 0));
            }
            else {
                final double sig = Math.signum(center.y);
                return this.createCollision(new Vector(center.x, this.halfHeight * sig),
                                            new Vector(0, -sig * (radius - dy)));
            }
        }
        else if (dx <= 0) {
            final double sig = Math.signum(center.y);
            return this.createCollision(new Vector(center.x, this.halfHeight * sig),
                                        new Vector(0, -sig * (radius - dy)));
        }
        else if (dy <= 0) {
            final double sig = Math.signum(center.x);
            return this.createCollision(new Vector(this.halfWidth * sig, center.y),
                                        new Vector(-sig * (radius - dx), 0));
        }

        if (dx * dx + dy * dy <= radius * radius) {
            final Vector p = new Vector(this.halfWidth * Math.signum(center.x),
                                        this.halfHeight * Math.signum(center.y));
            final Vector n = new Vector(center);
            n.subtract(p);
            n.setLength(Math.sqrt(dx * dx + dy * dy) - radius);
            return this.createCollision(p, n);
        }
        else {
            return Collision.NULL;
        }
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
        // Calculate negative penetration depth in both directions
        final float px = Math.abs(otherLocation.x) - this.halfWidth;
        final float py = Math.abs(otherLocation.y) - this.halfHeight;
        if (px > 0 || py > 0) {
            // No penetration
            return Collision.NULL;
        }
        // Check if horizontal penetration is smaller (px and py are < 0)
        else if (px > py) {
            final float sig = Math.signum(otherLocation.x);
            return this.createCollision(
                    new Vector(this.halfWidth * sig, otherLocation.y),
                    new Vector(px * sig, 0));
        }
        else {
            final float sig = Math.signum(otherLocation.y);
            return this.createCollision(
                    new Vector(otherLocation.x, this.halfHeight * sig),
                    new Vector(0, py * sig));
        }
    }

    @Override
    protected Collision doCollideWithRectangle(final Rectangle other) {
        // Do a fast first check using rectangles' circumscribed circles.
        if (!this.circumscribedCircle.intersectsWith(other.circumscribedCircle)) {
            return Collision.NULL;
        }
        else {
            return this.doIntersectWithPolygon(other);
        }
    }

    @Override
    protected Collision doCollideWithShape(final Shape other) {
        return other.doCollideWithRectangle(this).invert();
    }

    @Override
    protected boolean doesContain(final Vector point) {
        this.worldToLocal(point);
        return Math.abs(point.x) <= this.halfWidth &&
               Math.abs(point.y) <= this.halfHeight;
    }

    @Override
    protected Vector[] vertices() {
        Vector[] result = new Vector[4];
        result[0] = new Vector(this.halfWidth, this.halfHeight);
        result[1] = new Vector(this.halfWidth, -this.halfHeight);
        result[2] = new Vector(-this.halfWidth, this.halfHeight);
        result[3] = new Vector(-this.halfWidth, -this.halfHeight);
        for (Vector v : result) {
            this.localToWorld(v);
        }

        return result;
    }
}
