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

    public final float getHeight() {
        return this.height;
    }

    public final float getWidth() {
        return this.width;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Rectangle(x=");
        result.append(this.getX());
        result.append(", y=");
        result.append(this.getY());
        result.append(", w=");
        result.append(this.width);
        result.append(", h=");
        result.append(this.height);
        result.append(")");
        return result.toString();
    }

    @Override
    protected Vector[] axes() {
        final Vector[] result = new Vector[2];
        result[0] = new Vector(1, 0);
        result[1] = new Vector(0, 1);
        for (Vector v : result) {
            this.localToWorldDirection(v);
            v.normalize();
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
    Collision doCollideWithCircle(final Circle other) {
        final Vector center = other.globalPosition();
        final Vector delta = new Vector(center);
        this.worldToLocal(delta);
        final float radius = other.getRadius();
        final float dx = Math.abs(delta.x) - this.halfWidth;
        final float dy = Math.abs(delta.y) - this.halfHeight;
        if (dx > radius || dy > radius) {
            return null;
        }
        else if (dx <= 0 && dy <= 0) {
            if (Math.abs(dx) < Math.abs(dy)) {
                final double sig = Math.signum(delta.x);
                return this.createCollision(new Vector(this.halfWidth * sig, delta.y),
                                            new Vector(-sig * (radius - dx), 0));
            }
            else {
                final double sig = Math.signum(delta.y);
                return this.createCollision(new Vector(delta.x, this.halfHeight * sig),
                                            new Vector(0, -sig * (radius - dy)));
            }
        }
        else if (dx <= 0) {
            final double sig = Math.signum(delta.y);
            return this.createCollision(new Vector(delta.x, this.halfHeight * sig),
                                        new Vector(0, -sig * (radius - dy)));
        }
        else if (dy <= 0) {
            final double sig = Math.signum(delta.x);
            return this.createCollision(new Vector(this.halfWidth * sig, delta.y),
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
            return null;
        }
    }

    @Override
    Collision doCollideWithPoint(final Point other) {
        final Vector p = other.globalPosition();
        // Determine the vector 'delta' pointing from the rectangle's center to
        // the point.        
        final Vector delta = new Vector(p);
        this.worldToLocal(delta);
        // Project the vector 'delta' on both axes of the rectangle.
        final float px = Math.abs(delta.x) - this.halfWidth;
        final float py = Math.abs(delta.y) - this.halfHeight;
        if (px > 0 || py > 0) {
            return null;
        }

        final Vector pp;
        // Both p0 and p1 are negative. p0 > p1 means that the point is nearer
        // to the axes[0].
        if (px > py) {
            pp = new Vector(this.halfWidth * Math.signum(delta.x), delta.y);
        }
        else {
            pp = new Vector(delta.x, Math.signum(delta.y) * this.halfHeight);
        }

        this.localToWorld(pp);
        return new Collision(p, pp);
    }

    @Override
    Collision doCollideWithRectangle(final Rectangle other) {
        // Do a fast first check using rectangles' circumscribed circles.
        if (!this.circumscribedCircle.intersectsWith(other.circumscribedCircle)) {
            return null;
        }
        else {
            return this.doIntersectWithPolygon(other);
        }
    }

    @Override
    Collision doCollideWithShape(final Shape other) {
        return Collision.invert(other.doCollideWithRectangle(this));
    }

    @Override
    boolean doesContain(final Vector point) {
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

    Vector heightAxis() {
        final Vector result = new Vector(this.halfWidth, 0);
        this.localToWorld(result);
        return result;
    }

    Vector widthAxis() {
        final Vector result = new Vector(this.halfWidth, 0);
        this.localToWorld(result);
        return result;
    }
}
