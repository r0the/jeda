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
import java.util.ArrayList;
import java.util.List;

public class Rectangle extends AbstractPolygon {

    private static final List<Vector> AXES = initAxes();
    private final Circle circumscribedCircle;
    private final double halfWidth;
    private final double halfHeight;
    private final Location topLeft;
    private final Size size;

    public Rectangle(double width, double height) {
        this.halfWidth = width / 2.0;
        this.halfHeight = height / 2.0;
        final double radius = Math.sqrt(width * width + height * height) / 2.0;
        this.circumscribedCircle = new Circle(radius);
        this.topLeft = new Location((int) -this.halfWidth, (int) -this.halfHeight);
        this.size = new Size((int) width, (int) height);
    }

    @Override
    protected List<Vector> axes() {
        return AXES;
    }

    @Override
    protected void doDraw(Canvas canvas) {
        final Color fillColor = this.getFillColor();
        if (fillColor != null) {
            canvas.setColor(fillColor);
            canvas.fillRectangle(this.topLeft, this.size);
        }

        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawRectangle(this.topLeft, this.size);
        }
    }

    @Override
    protected boolean doesContain(Vector point) {
        return Math.abs(point.x) <= this.halfWidth &&
               Math.abs(point.y) <= this.halfHeight;
    }

    @Override
    protected Collision doCollideWithCircle(Circle other) {
        return this.intersectCircle(other.origin(), other.getRadius());
    }

    @Override
    protected Collision doCollideWithHalfPlane(HalfPlane other) {
        return Collision.NULL;
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Collision doCollideWithLineSegment(LineSegment other) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Collision doCollideWithPoint(Point other) {
        return this.intersectCircle(other.origin(), 0.0);
    }

    @Override
    protected Collision doCollideWithRectangle(Rectangle other) {
        // Do a fast first check using rectangles' circumscribed circles.
        if (!this.circumscribedCircle.intersectsWith(other.circumscribedCircle)) {
            return Collision.NULL;
        }
        else {
            return this.doIntersectWithPolygon(other);
        }
    }

    @Override
    protected Collision doCollideWithShape(Shape other) {
        return other.doCollideWithRectangle(this).inverted();
    }

    @Override
    protected List<Vector> vertices() {
        final List<Vector> result = new ArrayList();
        result.add(this.transformLocation(new Vector(this.halfWidth, this.halfHeight)));
        result.add(this.transformLocation(new Vector(this.halfWidth, -this.halfHeight)));
        result.add(this.transformLocation(new Vector(-this.halfWidth, -this.halfHeight)));
        result.add(this.transformLocation(new Vector(-this.halfWidth, this.halfHeight)));
        return result;
    }

    private Collision intersectCircle(Vector center, double radius) {
        center = this.toLocal(center);
        final double dx = Math.abs(center.x) - this.halfWidth;
        final double dy = Math.abs(center.y) - this.halfHeight;
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
            final Vector n = center.minus(p);
            return this.createCollision(p, n.withLength(Math.sqrt(dx * dx + dy * dy) - radius));
        }
        else {
            return Collision.NULL;
        }
    }

    private static List<Vector> initAxes() {
        final List<Vector> result = new ArrayList();
        result.add(new Vector(1.0, 0.0));
        result.add(new Vector(0.0, 1.0));
        return result;
    }
}
