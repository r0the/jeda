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
import ch.jeda.Transformation;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

/**
 * Represents a two-dimensional figure that can be drawn on a canvas. The
 * location and rotation of a figure can be controlled.
 */
public abstract class AbstractFigure {

    protected static final Color DEBUG_FILL_COLOR = new Color(255, 0, 0, 70);
    protected static final Color DEBUG_OUTLINE_COLOR = Color.RED;
    protected static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    private Transformation transformation;
    private Vector location;
    private double rotation;

    public final Collision collideWith(AbstractFigure other) {
        if (other == null || other == this) {
            return Collision.NULL;
        }
        else {
            return this.doCollideWith(other);
        }
    }

    public final boolean contains(double x, double y) {
        return this.doesContain(new Vector(x, y));
    }

    public final boolean contains(Vector point) {
        return this.doesContain(point);
    }

    public final void draw(Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException("canvas");
        }

        canvas.pushTransformation(this.transformation());
        this.doDraw(canvas);
        canvas.popTransformation();
    }

    public final Vector getLocation() {
        return this.location;
    }

    public final boolean intersectsWith(AbstractFigure other) {
        if (other == null || other == this) {
            return false;
        }
        else {
            return !this.doCollideWith(other).isNull();
        }
    }

    public final double getRotation() {
        return this.rotation;
    }

    public final void setLocation(double x, double y) {
        this.setLocation(new Vector(x, y));
    }

    public final void setLocation(Vector location) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        this.location = location;
        this.transformation = null;
        this.onLocationChanged();
    }

    public final void setRotation(double angle) {
        this.rotation = angle;
        this.transformation = null;
    }

    public final void translate(double x, double y) {
        this.translate(new Vector(x, y));
    }

    public final void translate(Vector d) {
        if (d == null) {
            throw new NullPointerException("d");
        }

        this.setLocation(this.location.plus(d));
    }

    public final void rotate(double angle) {
        this.rotation = this.rotation + angle;
        this.transformation = null;
    }

    protected AbstractFigure() {
        this.location = Vector.NULL;
        this.rotation = 0d;
        this.transformation = null;
    }

    protected abstract void doDraw(Canvas canvas);

    protected abstract Collision doCollideWith(AbstractFigure other);

    protected abstract Collision doCollideWithShape(Shape other);

    protected abstract boolean doesContain(Vector point);

    protected void onLocationChanged() {
    }

    final Vector origin() {
        return this.transformLocation(Vector.NULL);
    }

    Vector toLocal(Vector orig) {
        return this.transformation().inverted().transform(orig);
    }

    Vector transformLocation(Vector orig) {
        return this.transformation().transform(orig);
    }

    Vector transformDirection(Vector orig) {
        return orig.rotatedBy(this.rotation);
    }

    private Transformation transformation() {
        if (this.transformation == null) {
            this.transformation = Transformation.IDENTITY;
            this.transformation = this.transformation.translatedBy(this.location);
            this.transformation = this.transformation.rotatedBy(this.rotation);
        }

        return this.transformation;
    }
}
