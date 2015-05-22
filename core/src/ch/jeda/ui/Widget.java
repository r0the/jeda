/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
package ch.jeda.ui;

import ch.jeda.Jeda;

/**
 * Represents an element of a graphical user interface.
 * </p><p>
 * A widget can be <b>selected</b> by clicking on it or by calling the method {@link #select()}}. Only one widget can be
 * selected at a time.
 *
 * @since 1.3
 * @version 3
 */
public abstract class Widget extends Element {

    private static final int DEFAULT_DRAW_ORDER = 1000;
    private Alignment alignment;
    private Color background;
    private boolean selected;
    private float x;
    private float y;

    /**
     * Constructs a widget at the specified position.
     *
     * @param x the horizontal canvas coordinate of this widget
     * @param y the vertical canvas coordinate of this widget
     * @param alignment specifies how to align the widget relative to (<tt>x</tt>, <tt>y</tt>)
     *
     * @since 1.3
     */
    protected Widget(final double x, final double y, final Alignment alignment) {
        if (alignment == null) {
            this.alignment = Alignment.BOTTOM_LEFT;
        }
        else {
            this.alignment = alignment;
        }

        this.x = (float) x;
        this.y = (float) y;
        background = Color.LIGHT_GREEN_900;
        setDrawOrder(DEFAULT_DRAW_ORDER);
        setPinned(true);
    }

    /**
     * Checks if this widget contains a point. The point is specified in canvas coordinates.
     *
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @return <code>true</code> if the point (<code>x</code>, <code>y</code>) lies inside this widget, otherwise
     * <code>false</code>
     *
     * @since 2.0
     */
    public final boolean contains(final float x, final float y) {
        return containsLocal(x - this.x, y - this.y);
    }

    /**
     * Returns the current alignment of the widget. The alignment determines how the widget is positioned relative to
     * it's coordinates.
     *
     * @return the current alignment of the widget
     *
     * @see #setAlignment(ch.jeda.ui.Alignment)
     * @since 1.3
     */
    public final Alignment getAlignment() {
        return alignment;
    }

    @Override
    public float getAngleRad() {
        return 0f;
    }

    /**
     * Returns the background color of this widget.
     *
     * @return the background color of this widget
     *
     * @since 2.0
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Returns the height of the widget.
     *
     * @return the height of the widget
     *
     * @since 1.3
     */
    public abstract float getHeight();

    /**
     * Returns the width of the widget.
     *
     * @return the width of the widget
     *
     * @since 1.3
     */
    public abstract float getWidth();

    /**
     * Returns the horizontal coordinate of the widget.
     *
     * @return the horizontal coordinate of the widget
     *
     * @since 1.3
     */
    @Override
    public final float getX() {
        return x;
    }

    /**
     * Returns the vertical alignment coordinate of this widget.
     *
     * @return the vertical alignment coordinate of this widget
     *
     * @since 1.3
     */
    @Override
    public final float getY() {
        return y;
    }

    /**
     * Checks if this widget is selected.
     *
     * @return <code>true</code> if this widget is selected, otherwise <code>false</code>
     * @since 1.3
     */
    public final boolean isSelected() {
        return selected;
    }

    /**
     * Selects this widget. Deselects all other widget in the view.
     *
     * @since 1.3
     */
    public final void select() {
        if (getView() == null) {
            return;
        }

        final Widget[] widgets = getView().getElements(Widget.class);
        for (int i = 0; i < widgets.length; ++i) {
            if (widgets[i].selected) {
                widgets[i].selected = false;
            }
        }

        selected = true;
        checkVirtualKeyboard();
    }

    /**
     * Sets the alignment of the widget. The alignment determines how the widget is positioned relative to it's
     * coordinates.
     *
     * @param alignment the new alignment of the widget
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @see #getAlignment()
     * @since 1.3
     */
    public final void setAlignment(final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        this.alignment = alignment;
    }

    /**
     * Sets the background color of this widget. Has no effect if <code>color</code> is <code>null</code>.
     *
     * @param background the background color
     *
     * @since 2.0
     */
    public void setBackground(final Color background) {
        this.background = background;
    }

    /**
     * Sets the position of the widget. The widget is positioned relative to the specified coordinates (<code>x</code>,
     * <code>y</code>) depending on the current alignment.
     *
     * @param x the x coordinate of the widget
     * @param y the y coordinate of the widget
     *
     * @see #setAlignment(ch.jeda.ui.Alignment)
     * @since 1.3
     */
    public final void setPosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if this widget contains a point in local coordinates.
     *
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @return <code>true</code> if the point (<code>x</code>, <code>y</code>) lies inside this widget, otherwise
     * <code>false</code>
     *
     * @since 2.0
     */
    protected abstract boolean containsLocal(final float x, final float y);

    /**
     * Returns the horizontal local coordinate of the center of this widget.
     *
     * @return the horizontal local coordinate of the center of this widget
     *
     * @since 2.0
     */
    protected final float getCenterX() {
        switch (alignment.horiz) {
            case MAX:
                return -getWidth();
            case MIDDLE:
                return -getWidth() / 2f;
            case MIN:
            default:
                return 0;
        }
    }

    /**
     * Returns the vertical local coordinate of the center of this widget.
     *
     * @return the vertical local coordinate of the center of this widget
     *
     * @since 2.0
     */
    protected final float getCenterY() {
        switch (alignment.horiz) {
            case MAX:
                return -getHeight();
            case MIDDLE:
                return -getHeight() / 2f;
            case MIN:
            default:
                return 0;
        }
    }

    private void checkVirtualKeyboard() {
//        final boolean isVisible = Jeda.isVirtualKeyboardVisible();
//        final boolean shouldBeVisible = this instanceof NewInputField;
//        if (isVisible != shouldBeVisible) {
//            Jeda.setVirtualKeyboardVisible(shouldBeVisible);
//        }
    }
}
