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
import ch.jeda.ui.Color;

/**
 * Represents a geometrical shape.
 */
public abstract class Shape extends Figure {

    protected static final Color DEBUG_FILL_COLOR = new Color(255, 0, 0, 70);
    protected static final Color DEBUG_OUTLINE_COLOR = Color.RED;
    protected static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    private Color outlineColor;
    private Color fillColor;

    public final Color getFillColor() {
        return this.fillColor;
    }

    public final Color getOutlineColor() {
        return this.outlineColor;
    }

    public final boolean intersectsWith(final Shape other) {
        if (other == null || other == this) {
            return false;
        }
        else {
            return !this.doCollideWith(other).isNull();
        }
    }

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

    @Override
    protected final Collision doCollideWith(final Figure other) {
        return other.doCollideWithShape(this).invert();
    }

    protected abstract Collision doCollideWithCircle(Circle other);

    protected abstract Collision doCollideWithLineSegment(LineSegment other);

    protected abstract Collision doCollideWithPoint(Point other);

    protected abstract Collision doCollideWithRectangle(Rectangle other);

    protected final Collision createCollision(final Vector point,
                                              final Vector normal) {
        this.localToWorld(point);
        this.localToWorldDirection(normal);
        return new Collision(point, normal);
    }
}
