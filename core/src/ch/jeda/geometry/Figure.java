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
    private final Transformation localToWorld;
    private final Transformation worldToLocal;
    private boolean localToWorldDirty;
    private boolean worldToLocalDirty;
    private Figure parent;
    private double rotation;
    private float x;
    private float y;

    /**
     * Collides two figures. Determines if the two figures intersect. Returns
     * <tt>null</tt> if they don't intersect or if <tt>other</tt> is
     * <tt>null</tt>. Returns collision information if they do intersect. A
     * figure never intersects with itself.
     *
     * @param other the other figure
     * @return collision information or <tt>null</tt>
     */
    public final Collision collideWith(final Figure other) {
        if (other == null || other == this) {
            return null;
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
     * Returns the parent of the figure. Returns <tt>null</tt> if the figure has
     * no parent.
     *
     * @return parent of the figure or <tt>null</tt>
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
     * Returns the rotation of the figure. If the figure has a parent, the
     * rotation is relative to the parent figure. It is measured in radians.
     *
     * @return rotation of the figure
     */
    public final double getRotation() {
        return this.rotation;
    }

    /**
     * Returns the x coordinate of position of the figure. If the figure has a
     * parent, the position is relative to the parent figure.
     *
     * @return x coordinate of the position
     */
    public final float getX() {
        return this.x;
    }

    /**
     * Returns the y coordinate of position of the figure. If the figure has a
     * parent, the position is relative to the parent figure.
     *
     * @return y coordinate of the position
     */
    public final float getY() {
        return this.y;
    }

    /**
     * Checks if two figures intersect. Returns <tt>falst</tt> if they don't
     * intersect or if <tt>other</tt> is <tt>null</tt>. A figure never
     * intersects with itself.
     *
     * @param other the other figure
     * @return <tt>true</tt> if figures intersect, otherwise <tt>false</tt>
     */
    public final boolean intersectsWith(final Shape other) {
        if (other == null || other == this) {
            return false;
        }
        else {
            return this.doCollideWith(other) != null;
        }
    }

    /**
     * Rotates the figure. Rotates the figure by the specified angle.
     *
     * @param angle the rotation angle
     */
    public final void rotate(final double angle) {
        this.rotation = normalizeAngle(this.rotation + angle);
        this.changed();
    }

    /**
     * Sets the position of the figure. If the figure has a parent, the position
     * is relative to the parent figure.
     *
     * @param x the x coordinate of the position
     * @param y the y coordinate of the position
     */
    public final void setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.changed();
    }

    /**
     * Sets the rotation of the figure. If the figure has a parent, the rotation
     * is relative to the parent figure. It is measured in radians.
     *
     * @param angle the relative rotation angle
     */
    public final void setRotation(final double angle) {
        this.rotation = normalizeAngle(angle);
        this.changed();
    }

    /**
     * Translates the figure. If the figure has a parent, the translation is
     * relative to the parent figure.
     *
     * @param dx the translation along the x-axis
     * @param dy the translation along the x-axis
     */
    public final void translate(final float dx, final float dy) {
        this.x += dx;
        this.y += dy;
        this.changed();
    }

    /**
     * Constructs a figure.
     */
    protected Figure() {
        this.localToWorld = new Transformation();
        this.worldToLocal = new Transformation();
    }

    /**
     * Draws the figure. Override this method to draw the figure on the
     * specified canvas. The drawing operations are interpreted in the local
     * coordinate system of the figure.
     *
     * @param canvas the canvas to draw on
     */
    protected abstract void doDraw(Canvas canvas);

    protected final void setTransformation(final Canvas canvas) {
        this.checkLocalToWorld();
        canvas.setTransformation(this.localToWorld);
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

    void changed() {
        this.localToWorldDirty = true;
        this.worldToLocalDirty = true;
    }

    abstract Collision doCollideWith(Figure other);

    abstract Collision doCollideWithShape(Shape other);

    abstract boolean doesContain(Vector point);

    final Vector globalPosition() {
        final Vector result = new Vector();
        this.localToWorld(result);
        return result;
    }

    final void localToWorld(final Vector v) {
        this.checkLocalToWorld();
        this.localToWorld.transformPoint(v);
    }

    final void localToWorldDirection(final Vector v) {
        this.checkLocalToWorld();
        this.localToWorld.transformDelta(v);
    }

    final void worldToLocal(final Vector v) {
        this.checkWorldToLocal();
        this.worldToLocal.transformPoint(v);
    }

    final void setParent(final Figure value) {
        this.parent = value;
        this.changed();
    }

    private void checkLocalToWorld() {
        if (!this.localToWorldDirty) {
            return;
        }

        if (this.parent != null) {
            this.parent.checkLocalToWorld();
            this.localToWorld.set(this.parent.localToWorld);
        }
        else {
            this.localToWorld.setIdentity();
        }

        this.localToWorld.translate(this.x, this.y);
        if (this.rotation != 0.0) {
            this.localToWorld.rotate(this.rotation);
        }

        this.localToWorldDirty = false;
    }

    private void checkWorldToLocal() {
        if (!this.worldToLocalDirty) {
            return;
        }

        this.checkLocalToWorld();
        this.localToWorld.invert(this.worldToLocal);
        this.worldToLocalDirty = false;
    }
}
