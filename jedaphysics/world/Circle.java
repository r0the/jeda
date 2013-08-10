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
package ch.jeda.world;

import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

/**
 * Represents a circle shape.
 */
public class Circle extends Shape {

    private float centerX;
    private float centerY;
    private final float radius;

    /**
     * Constructs a circle shape. The circle shape has the specified radius. It's center is initially positioned at the
     * origin.
     *
     * @param radius the radius of the circle
     */
    public Circle(final float radius) {
        if (radius <= 0f) {
            throw new IllegalArgumentException("radius");
        }

        this.radius = radius;
    }

    @Override
    public final boolean contains(float x, float y) {
        final float dx = this.centerX - x;
        final float dy = this.centerY - y;
        return dx * dx + dy * dy <= this.radius * this.radius;
    }

    @Override
    public final void draw(final Canvas canvas) {
        final Color fillColor = this.getFillColor();
        if (fillColor != null) {
            canvas.setColor(fillColor);
            canvas.fillCircle(this.centerX, this.centerY, this.radius);
        }

        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawCircle(this.centerX, this.centerY, this.radius);
        }
    }

    @Override
    public final void move(float dx, float dy) {
        this.centerX += dx;
        this.centerY += dy;
    }

    public final float getCenterX() {
        return this.centerX;
    }

    public final float getCenterY() {
        return this.centerY;
    }

    /**
     * Returns the radius of the circle.
     *
     * @return radius of the circle
     */
    public final float getRadius() {
        return this.radius;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Circle(x=");
        result.append(this.centerX);
        result.append(", y=");
        result.append(this.centerY);
        result.append(", r=");
        result.append(this.radius);
        result.append(")");
        return result.toString();
    }
}
