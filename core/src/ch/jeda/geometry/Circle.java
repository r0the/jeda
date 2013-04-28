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
 * Represents a circle shape.
 */
public class Circle extends Shape {

    private final float radius;

    /**
     * Constructs a circle shape. The circle shape has the specified radius.
     * It's center is initially positioned at the origin.
     *
     * @param radius the radius of the circle
     */
    public Circle(final float radius) {
        if (radius <= 0f) {
            throw new IllegalArgumentException("radius");
        }

        this.radius = radius;
    }

    /**
     * Returns the radius of the circle.
     *
     * @return radius of the circle
     */
    public float getRadius() {
        return this.radius;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Circle(x=");
        result.append(this.getX());
        result.append(", y=");
        result.append(this.getY());
        result.append(", r=");
        result.append(this.radius);
        result.append(")");
        return result.toString();
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
    Collision doCollideWithCircle(final Circle other) {
        final Vector p = other.globalPosition();
        // Determine the vector 'delta' pointing from the first circle's center
        // to the second circle's center.
        final Vector delta = new Vector(p);
        this.worldToLocal(delta);
        // Determine the penetration depth 'depth', which is the difference of
        // the sum of both circles' radii and the length of 'delta'.
        final float depth = this.radius + other.radius - (float) delta.length();
        // If the penetration depth 'depth' is positive, a collision has 
        // occurred.
        if (depth > 0f) {
            // The second penetration point lies on the second circle in the
            // opposite direction of 'delta' from the second circle's center.
            delta.setLength(-other.radius);
            // Add it to the first circle's center to get the absolute
            // coordinates of the penetration point.
            p.add(delta);
            // The first penetration point lies on the first circle in direction 
            // of 'delta' from the first circle's center.
            delta.setLength(-this.radius);
            this.localToWorld(delta);
            return new Collision(delta.x, delta.y, p.x, p.y);
        }
        else {
            return null;
        }
    }

    @Override
    Collision doCollideWithPoint(final Point other) {
        final Vector p = other.globalPosition();
        // Determine the vector 'delta' pointing from the circle's center to the 
        // point.
        final Vector delta = new Vector(p);
        this.worldToLocal(delta);
        // Determine the penetration depth 'depth', which is the difference of
        // the circle's radius and the length of 'delta'.
        final float depth = this.radius - (float) delta.length();
        // If the penetration depth 'depth' is positive, a collision has 
        // occurred.
        if (depth > 0f) {
            // The first penetration point lies on the circle in direction of 
            // 'delta' from the circle's center.
            delta.setLength(this.radius);
            this.localToWorld(delta);
            // The second penetration point is the point itself.
            return new Collision(delta.x, delta.y, p.x, p.y);
        }
        else {
            return null;
        }
    }

    @Override
    Collision doCollideWithRectangle(final Rectangle other) {
        return Collision.invert(other.doCollideWithCircle(this));
    }

    @Override
    Collision doCollideWithShape(final Shape other) {
        return Collision.invert(other.doCollideWithCircle(this));
    }

    @Override
    boolean doesContain(final Vector point) {
        this.worldToLocal(point);
        return point.dot(point) < this.radius * this.radius;
    }
}
