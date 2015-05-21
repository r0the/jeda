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
 * Represents an element of a graphical user interface. A widget has a style that determines how to draw the widget.
 * <p>
 * A widget can be <b>selected</b> by clicking on it or by calling the method {@link #select()}}. Only one widget can be
 * selected at a time.
 *
 * @since 1.3
 * @version 2
 */
public abstract class OldWidget extends Element {

    private static final int DEFAULT_DRAW_ORDER = 1000;
    private Alignment alignment;
    private boolean selected;
    private int x;
    private int y;

    /**
     * Constructs a widget at the specified position with the specified alignment.
     *
     * @param x the x coordinate of the widget
     * @param y the y coordinate of the widget
     * @param alignment specifies how to align the widget relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    protected OldWidget(final int x, final int y, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        this.alignment = alignment;
        this.x = x;
        this.y = y;
        setDrawOrder(DEFAULT_DRAW_ORDER);
    }

    /**
     * Checks if if the point (<tt>x</tt>, <tt>y</tt>) lies inside the widget.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <tt>true</tt> if the point (<tt>x</tt>, <tt>y</tt>) lies inside the widget, otherwise <tt>false</tt>
     */
    public abstract boolean contains(final float x, final float y);

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
     * Returns the y coordinate of the bottom border of the widget.
     *
     * @return the y coordinate of the bottom border of the widget
     *
     * @since 1.3
     */
    public final int getBottom() {
        final int height = getHeight();
        return alignment.alignY(y, height) + height;
    }

    /**
     * Returns the x coordinate of the widget's centre.
     *
     * @return the x coordinate of the widget's centre
     *
     * @since 1.3
     */
    public final int getCenterX() {
        final int width = getWidth();
        return alignment.alignX(x, width) + width / 2;
    }

    /**
     * Returns the y coordinate of the widget's centre.
     *
     * @return the y coordinate of the widget's centre
     *
     * @since 1.3
     */
    public final int getCenterY() {
        final int height = getHeight();
        return alignment.alignY(y, height) + height / 2;
    }

    /**
     * Returns the height of the widget.
     *
     * @return the height of the widget
     *
     * @since 1.3
     */
    public abstract int getHeight();

    /**
     * Returns the x coordinate of the left border of the widget.
     *
     * @return the x coordinate of the left border of the widget
     *
     * @since 1.3
     */
    public final int getLeft() {
        return alignment.alignX(x, getWidth());
    }

    /**
     * Returns the x coordinate of the right border of the widget.
     *
     * @return the x coordinate of the right border of the widget
     *
     * @since 1.3
     */
    public final int getRight() {
        final int width = getWidth();
        return alignment.alignX(x, width) + width;
    }

    /**
     * Returns the y coordinate of the top border of the widget.
     *
     * @return the y coordinate of the top border of the widget
     *
     * @since 1.3
     */
    public final int getTop() {
        return alignment.alignY(y, getHeight());
    }

    /**
     * Returns the width of the widget. The widget's width is given by it's background image.
     *
     * @return the width of the widget
     *
     * @since 1.3
     */
    public abstract int getWidth();

    /**
     * Returns the x coordinate of the widget.
     *
     * @return the x coordinate of the widget
     *
     * @since 1.3
     */
    public final float getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the widget.
     *
     * @return the y coordinate of the widget
     *
     * @since 1.3
     */
    public final float getY() {
        return y;
    }

    /**
     * Checks if the widget is selected.
     *
     * @return <tt>true</tt> if the widget is selected, otherwise <tt>false</tt>
     * @since 1.3
     */
    public final boolean isSelected() {
        return selected;
    }

    /**
     * Selects the widget. Deselects all other widget in the window.
     *
     * @since 1.3
     */
    public final void select() {
        if (getView() == null) {
            return;
        }

        final OldWidget[] widgets = getView().getElements(OldWidget.class);
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
     * Sets the position of the widget. The widget is positioned relative to the specified coordinates (<tt>x</tt>,
     * <tt>y</tt>) depending on the current alignment.
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

    private void checkVirtualKeyboard() {
        final boolean isVisible = Jeda.isVirtualKeyboardVisible();
        final boolean shouldBeVisible = this instanceof InputField;
        if (isVisible != shouldBeVisible) {
            Jeda.setVirtualKeyboardVisible(shouldBeVisible);
        }
    }
}
