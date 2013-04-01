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
 *
 */
public abstract class Shape extends AbstractFigure {

    private Color outlineColor;
    private Color fillColor;

    public final Color getFillColor() {
        return this.fillColor;
    }

    public final Color getOutlineColor() {
        return this.outlineColor;
    }

    public final void setFillColor(Color value) {
        this.fillColor = value;
    }

    public final void setOutlineColor(Color value) {
        this.outlineColor = value;
    }

    protected Shape() {
        this.fillColor = null;
        this.outlineColor = Color.BLACK;
    }

    @Override
    protected final Collision doCollideWith(AbstractFigure other) {
        return other.doCollideWithShape(this).inverted();
    }

    protected abstract Collision doCollideWithCircle(Circle other);

    protected abstract Collision doCollideWithHalfPlane(HalfPlane other);

    protected abstract Collision doCollideWithLineSegment(LineSegment other);

    protected abstract Collision doCollideWithPoint(Point other);

    protected abstract Collision doCollideWithRectangle(Rectangle other);

    protected final Collision createCollision(Vector point, Vector normal) {
        return new Collision(this.transformLocation(point), this.transformDirection(normal));
    }
}
