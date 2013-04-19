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
 * maintains information about it's position and rotation.
 */
public abstract class Figure {

    private static final double TWO_PI = 2.0 * Math.PI;
    private final Transformation worldTransformation;
    private final Transformation inverse;
    private boolean dirty;
    private Figure parent;
    private double rotation;
    private float x;
    private float y;

    public final Collision collideWith(final Figure other) {
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
    public final boolean contains(final float x, final float y) {
        return this.doesContain(new Vector(x, y));
    }

    /**
     * Draws the figure on a canvas.
     *
     * @param canvas the canvas to draw on
     * @throws NullPointerException if <tt>canvas</tt> is <tt>null</tt>
     */
    public final void draw(final Canvas canvas) {
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
     * Returns the relative position of the figure. Creates and returns a new
     * {@link Vector} representing the current position of this figure relative
     * to it's parent.
     *
     * @return relative position of the figure
     */
    public final Vector getPosition() {
        return new Vector(this.x, this.y);
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
     * Returns the x coordinate of the relative position of the figure.
     *
     * @return x coordinate of the relative position
     */
    public final float getX() {
        return this.x;
    }

    /**
     * Returns the y coordinate of the relative position of the figure.
     *
     * @return y coordinate of the relative position
     */
    public final float getY() {
        return this.y;
    }

    /**
     * Sets the relative position of the figure. The position is relative to the
     * parent figure.
     *
     * @param x the x coordinate of the relative position
     * @param y the y coordinate of the relative position
     */
    public final void setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.dirty = true;
    }

    /**
     * Sets the relative position of the figure. The position is relative to the
     * parent figure.
     *
     * @param position the relative position
     * @throws NullPointerException if <tt>position</tt> is <tt>null</tt>
     */
    public final void setPosition(final Vector position) {
        if (position == null) {
            throw new NullPointerException("position");
        }

        this.x = position.x;
        this.y = position.y;
        this.dirty = true;
    }

    /**
     * Sets the relative rotation of the figure. The rotation is relative to the
     * parent figure. It is measured in radians.
     *
     * @param angle the relative rotation angle
     */
    public final void setRotation(final double angle) {
        this.rotation = normalizeAngle(angle);
        this.dirty = true;
    }

    public final void translate(final float x, final float y) {
        this.x += x;
        this.y += y;
        this.dirty = true;
    }

    public final void translate(final Vector t) {
        if (t == null) {
            throw new NullPointerException("d");
        }

        this.translate(t.x, t.y);
    }

    public final void rotate(final double angle) {
        this.rotation = normalizeAngle(this.rotation + angle);
        this.dirty = true;
    }

    protected void updateTransformation() {
        if (!this.isDirty()) {
            return;
        }

        if (this.parent != null) {
            this.parent.updateTransformation();
            this.worldTransformation.set(this.parent.worldTransformation);
        }
        else {
            this.worldTransformation.setIdentity();
        }

        this.worldTransformation.translate(this.x, this.y);
        if (this.rotation != 0.0) {
            this.worldTransformation.rotate(this.rotation);
        }

        this.worldTransformation.invert(this.inverse);
        this.dirty = false;
    }

    /**
     * Constructs a figure.
     */
    protected Figure() {
        this.worldTransformation = new Transformation();
        this.inverse = new Transformation();
    }

    protected abstract Collision doCollideWith(Figure other);

    protected abstract Collision doCollideWithShape(Shape other);

    protected abstract void doDraw(Canvas canvas);

    protected abstract boolean doesContain(Vector point);

    protected final void setTransformation(final Canvas canvas) {
        this.updateTransformation();
        canvas.setTransformation(this.worldTransformation);
    }

    protected static double normalizeAngle(double angle) {
        while (angle < 0.0) {
            angle = angle + TWO_PI;
        }

        while (angle > TWO_PI) {
            angle = angle - TWO_PI;
        }

        return angle;

    }

    final void localToWorld(final Vector v) {
        this.updateTransformation();
        this.worldTransformation.transformPoint(v);
    }

    final void localToWorldDirection(final Vector v) {
        this.updateTransformation();
        this.worldTransformation.transformDelta(v);
    }

    final void worldToLocal(final Vector v) {
        this.updateTransformation();
        this.inverse.transformPoint(v);
    }

    final void setParent(final Figure value) {
        this.parent = value;
        this.dirty = true;
    }

    private boolean isDirty() {
        return this.dirty || (this.parent != null && this.parent.isDirty());
    }
}
