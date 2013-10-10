/*
 * Copyright (C) 2013 by Stefan Rothe
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

import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

public abstract class Shape {

    protected static final Color DEBUG_FILL_COLOR = new Color(255, 0, 0, 70);
    protected static final Color DEBUG_OUTLINE_COLOR = Color.RED;
    protected static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    private Color outlineColor;
    private Color fillColor;

    public final Collision collideWith(final Shape other) {
        if (this.getBoundingBox().intersects(other.getBoundingBox())) {
            if (this instanceof Polygon && other instanceof Polygon) {
                return ((Polygon) this).collideWithPolygon((Polygon) other);
            }
        }

        return null;
    }

    public boolean contains(final float x, final float y) {
        return this.getBoundingBox().contains(x, y) && this.doesContain(x, y);
    }

    public abstract void draw(final Canvas canvas);

    public abstract BoundingBox getBoundingBox();

    public abstract float getCenterX();

    public abstract float getCenterY();

    public final Color getFillColor() {
        return this.fillColor;
    }

    public final Color getOutlineColor() {
        return this.outlineColor;
    }

    public abstract void move(float dx, float dy);

    public final void setFillColor(final Color value) {
        this.fillColor = value;
    }

    public final void setOutlineColor(final Color value) {
        this.outlineColor = value;
    }

    protected Shape() {
        this.fillColor = null;
        this.outlineColor = Color.BLACK;
    }

    abstract boolean doesContain(float x, float y);
}
