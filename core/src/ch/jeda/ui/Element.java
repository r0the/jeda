/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.ui;

import java.util.Comparator;

/**
 * Base class for objects with a graphical representation. Elements can be added to a {@link ch.jeda.ui.View}. The view
 * will automatically draw the elements. Every element has a <b>draw order</b> that determines the order in which the
 * elements are drawn. Elements with a smaller draw order are drawn first.
 *
 * @see ch.jeda.ui.View#add(ch.jeda.ui.Element)
 * @see ch.jeda.ui.View#remove(ch.jeda.ui.Element)
 * @since 2.0
 * @version 2
 */
public abstract class Element {

    static final Comparator<Element> DRAW_ORDER = new DrawOrder();
    private static final int DEFAULT_DRAW_ORDER = -1;
    private float angle;
    private int drawOrder;
    private String name;
    private View view;
    private float x;
    private float y;

    /**
     * Constructs a new element.
     *
     * @since 2.0
     */
    protected Element() {
        drawOrder = DEFAULT_DRAW_ORDER;
        name = null;
    }

    /**
     * Returns the current draw order of this element. The draw order determines the order in which the elements are
     * drawn on a {@link ch.jeda.ui.View}. Elements with a smaller draw order are drawn first.
     *
     * @return the current draw order of this element
     *
     * @see #setDrawOrder(int)
     * @since 2.0
     */
    public final int getDrawOrder() {
        return drawOrder;
    }

    /**
     * Returns the name of this element.
     *
     * @return the name of this element
     *
     * @see #setName(java.lang.String)
     * @since 2.0
     */
    public final String getName() {
        if (name == null) {
            name = getClass().getSimpleName();
        }

        return name;
    }

    /**
     * Returns the horizontal world coordinate of this element.
     *
     * @return the horizontal world coordinate of this element
     *
     * @see #getY()
     * @see #setPosition(double, double)
     * @since 2.0
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the vertical world coordinate of this element.
     *
     * @return the vertical world coordinate of this element
     *
     * @see #getX()
     * @see #setPosition(double, double)
     * @since 2.0
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the current rotation angle of this element in degrees.
     *
     * @return the current rotation angle of this element in degrees
     *
     * @see #getAngleRad()
     * @see #setAngleDeg(double)
     * @see #setAngleRad(double)
     * @since 2.0
     */
    public final float getAngleDeg() {
        return (float) Math.toDegrees(getAngleRad());
    }

    /**
     * Returns the current rotation angle of this element in radians.
     *
     * @return the current rotation angle of this element in radians
     *
     * @see #getAngleDeg()
     * @see #setAngleDeg(double)
     * @see #setAngleRad(double)
     * @since 2.0
     */
    public float getAngleRad() {
        return this.angle;
    }

    /**
     * Checks if this element is currently pinned to the canvas. Pinned elements are positioned in the canvas coordinate
     * system rather than the world coordinate system.
     *
     * @return <code>true</code>, if this element is currently pinned to the canvas, otherwise <code>false</code>
     *
     * @since 2.0
     */
    public final boolean isPinned() {
        return getDrawOrder() > 0;
    }

    /**
     * Removes this element from the view. Has no effect if the element is not currently in a view.
     *
     * @since 2.2
     */
    public final void remove() {
        if (view != null) {
            view.remove(this);
        }
    }

    /**
     * Sets the rotation angle of this element in degrees.
     *
     * @param angle the angle of this element in degrees
     *
     * @see #getAngleDeg()
     * @see #getAngleRad()
     * @see #setAngleRad(double)
     * @since 2.1
     */
    public final void setAngleDeg(final double angle) {
        this.setAngleRad((float) Math.toRadians(angle));
    }

    /**
     * Sets the rotation angle of this element in radians.
     *
     * @param angle the angle of this element in radians
     *
     * @see #getAngleDeg()
     * @see #getAngleRad()
     * @see #setAngleRad(double)
     * @since 2.1
     */
    public void setAngleRad(final double angle) {
        this.angle = (float) angle;
    }

    /**
     * Sets the draw order of the element. The draw order determines the order in which the element are drawn on a
     * {@link ch.jeda.ui.View}. Elements with a smaller draw order are drawn first.
     *
     * @param drawOrder the new draw order for this element
     *
     * @see #getDrawOrder()
     * @since 2.0
     */
    public final void setDrawOrder(final int drawOrder) {
        this.drawOrder = drawOrder;
        if (view != null) {
            view.drawOrderChanged(this);
        }
    }

    /**
     * Sets the name of this element.
     *
     * @param name the name of this element
     *
     * @see #getName()
     * @since 2.0
     */
    public final void setName(String name) {
        if (name == null) {
            name = "";
        }

        if (view != null) {
            view.removeName(this, getName());

            view.addName(this, name);
        }

        this.name = name;
    }

    /**
     * Pins or unpins this element. Pinned elements are positioned in the canvas coordinate system rather than the world
     * coordinate system. This is achieved by setting the draw order to -1 or 1.
     *
     * @param pinned <code>true</code> to pin the element, <code>false</code> to unpin it
     *
     * @since 2.0
     * @deprecated Use {@link #setDrawOrder(int)} instead.
     */
    public final void setPinned(final boolean pinned) {
        if (pinned) {
            setDrawOrder(1);
        }
        else {
            setDrawOrder(-1);
        }
    }

    /**
     * Sets the position of this element.
     *
     * @param x the horizontal coordinate of this element
     * @param y the vertical coordinate of this element
     *
     * @see #getX()
     * @see #getY()
     * @since 2.1
     */
    public void setPosition(final double x, final double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * Draws the element. This method is called by the {@link ch.jeda.ui.Window} whenever the element needs to be drawn.
     * Override this method to draw the element.
     *
     * @param canvas the canvas on which the element should be drawn.
     *
     * @since 2.0
     */
    protected abstract void draw(final Canvas canvas);

    /**
     * Returns the view containing the element. Returns <tt>null</tt> if the element has not yet been added to a view.
     *
     * @return the view containing the element
     *
     * @since 2.0
     */
    protected final View getView() {
        return view;
    }

    void addToView(final View view) {
        if (view != this.view && this.view != null) {
            this.view.remove(this);
        }

        this.view = view;
    }

    void internalDraw(final Canvas canvas) {
        canvas.localBegin(getX(), getY(), getAngleRad());
        draw(canvas);
        canvas.localEnd();
    }

    void removeFromView(final View view) {
        if (view == this.view) {
            this.view = null;
        }
    }

    private static class DrawOrder implements Comparator<Element> {

        @Override
        public int compare(final Element object1, final Element object2) {
            return object1.drawOrder - object2.drawOrder;
        }
    }
}
