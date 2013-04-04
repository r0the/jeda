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

import ch.jeda.Transformation;
import ch.jeda.Vector;
import ch.jeda.ui.Canvas;

/**
 * Represents a two-dimensional figure. A figure can be drawn on a canvas. It
 * maintains information about it's location and rotation.
 */
public abstract class Figure {

    private final Vector location;
    private final Transformation worldTransformation;
    private final Transformation inverse;
    private boolean dirty;
    private Figure parent;
    private double rotation;

    public final Collision collideWith(Figure other) {
        if (other == null || other == this) {
            return Collision.NULL;
        }
        else {
            return this.doCollideWith(other);
        }
    }

    /**
     * Checks if a point is inside the figure.
     *
     * @param x the x coordinate of the point to check
     * @param y the y coordinate of the point to check
     * @return <tt>true</tt> if the point is inside the figure, otherwise
     * <tt>false</tt>
     */
    public final boolean contains(double x, double y) {
        return this.doesContain(new Vector(x, y));
    }

    /**
     * Draws the figure on a canvas.
     *
     * @param canvas the canvas to draw on
     * @throws NullPointerException if <tt>canvas</tt> is <tt>null</tt>
     */
    public final void draw(Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException("canvas");
        }

        this.setTransformation(canvas);
        this.doDraw(canvas);
    }

    /**
     * Returns the parent of the figure.
     *
     * @return parent of the figure
     */
    public final Figure getParent() {
        return this.parent;
    }

    /**
     * Returns the relative location of the figure. The location is relative to
     * the parent figure.
     *
     * @return relative location of the figure
     */
    public final Vector getLocation() {
        return new Vector(this.location);
    }

    /**
     * Returns the relative rotation of the figure. The rotation is relative to
     * the parent figure. It is measured in radians.
     *
     * @return relative rotation of the figure
     */
    public final double getRotation() {
        return this.rotation;
    }

    /**
     * Returns the x coordinate of the relative location.
     *
     * @return
     */
    public final float getX() {
        return this.location.x;
    }

    /**
     * Returns the y coordinate of the relative location.
     *
     * @return
     */
    public final float getY() {
        return this.location.y;
    }

    /**
     * Sets the relative location of the figure. The location is relative to the
     * parent figure.
     *
     * @param x the x coordinate of the relative location
     * @param y the y coordinate of the relative location
     */
    public final void setLocation(double x, double y) {
        this.location.x = (float) x;
        this.location.y = (float) y;
        this.changed();
    }

    /**
     * Sets the relative location of the figure. The location is relative to the
     * parent figure.
     *
     * @param location the relative location
     * @throws NullPointerException if <tt>location</tt> is <tt>null</tt>
     */
    public final void setLocation(Vector location) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        this.location.x = location.x;
        this.location.y = location.y;
        this.changed();
    }

    /**
     * Sets the relative rotation of the figure. The rotation is relative to the
     * parent figure. It is measured in radians.
     *
     * @param angle the relative rotation
     */
    public final void setRotation(double angle) {
        this.rotation = angle;
        this.changed();
    }

    public final void translate(double x, double y) {
        this.setLocation(this.location.x + x, this.location.y + y);
    }

    public final void translate(Vector t) {
        if (t == null) {
            throw new NullPointerException("d");
        }

        this.translate(t.x, t.y);
    }

    public final void rotate(double angle) {
        this.rotation = this.rotation + angle;
        this.changed();
    }

    protected void updateTransformation() {
        if (!this.dirty) {
            return;
        }

        if (this.parent != null) {
            this.worldTransformation.set(this.parent.worldTransformation);
        }
        else {
            this.worldTransformation.setIdentity();
        }

        this.worldTransformation.translate(this.location);
        this.worldTransformation.rotate(this.rotation);
        this.worldTransformation.invert(this.inverse);
        this.dirty = false;
    }

    /**
     * Constructs a figure.
     */
    protected Figure() {
        this.location = new Vector();
        this.worldTransformation = Transformation.createIdentity();
        this.inverse = Transformation.createIdentity();
    }

    protected abstract Collision doCollideWith(Figure other);

    protected abstract Collision doCollideWithShape(Shape other);

    protected abstract void doDraw(Canvas canvas);

    protected abstract boolean doesContain(Vector point);

    protected void changed() {
        this.dirty = true;
    }

    protected void setTransformation(Canvas canvas) {
        this.updateTransformation();
        canvas.setTransformation(this.worldTransformation);
    }

    void localToWorld(Vector v) {
        this.updateTransformation();
        this.worldTransformation.transformPoint(v);
    }

    void localToWorldDirection(Vector v) {
        this.updateTransformation();
        this.worldTransformation.transformDelta(v);
    }

    void worldToLocal(Vector v) {
        this.updateTransformation();
        this.inverse.transformPoint(v);
    }

    void setParent(Figure value) {
        this.parent = value;
        this.changed();
    }
}
