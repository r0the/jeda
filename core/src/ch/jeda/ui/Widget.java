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
import ch.jeda.event.ActionEvent;

/**
 * Represents an element of a graphical user interface.
 * <p>
 * A widget can be <b>selected</b> by clicking on it or by calling the method {@link #select()}}. Only one widget can be
 * selected at a time.
 *
 * @since 1.3
 * @version 4
 */
public abstract class Widget extends Element {

    private static final int DEFAULT_DRAW_ORDER = Integer.MAX_VALUE;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.LIGHT_GREEN_900;
    private Alignment alignment;
    private Color backgroundColor;
    private boolean selected;

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

        backgroundColor = DEFAULT_BACKGROUND_COLOR;
        setDrawOrder(DEFAULT_DRAW_ORDER);
        setPinned(true);
        setPosition(x, y);
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
        return containsLocal(x - this.getX(), y - this.getY());
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
    public Color getBackgroundColor() {
        return backgroundColor;
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
            selected = true;
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
     * @param backgroundColor the background color
     *
     * @since 2.0
     */
    public void setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Applies the style of this widget to the canvas to prepare drawing. This implementation sets the alignment and
     * backround color.
     *
     * @param canvas the canvas
     *
     * @since 2.0
     */
    protected void applyStyle(final Canvas canvas) {
        canvas.setAlignment(alignment);
        canvas.setColor(backgroundColor);
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
     * Returns the vertical local coordinate of the bottom border of this widget.
     *
     * @return the vertical local coordinate of the bottom border of this widget
     *
     * @since 2.0
     */
    protected final float getBottom() {
        switch (alignment.vert) {
            case MAX:
                return -getHeight();
            case MIDDLE:
                return -getHeight() / 2f;
            case MIN:
            default:
                return 0f;
        }
    }

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
                return -getWidth() / 2f;
            case MIDDLE:
                return 0f;
            case MIN:
            default:
                return getWidth() / 2f;
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
        switch (alignment.vert) {
            case MAX:
                return -getHeight() / 2f;
            case MIDDLE:
                return 0f;
            case MIN:
            default:
                return getHeight() / 2f;
        }
    }

    /**
     * Returns the horizontal local coordinate of the left border of this widget.
     *
     * @return the horizontal local coordinate of the left border of this widget
     *
     * @since 2.0
     */
    protected final float getLeft() {
        switch (alignment.horiz) {
            case MAX:
                return -getWidth();
            case MIDDLE:
                return -getWidth() / 2f;
            case MIN:
            default:
                return 0f;
        }
    }

    /**
     * Returns the horizontal local coordinate of the right border of this widget.
     *
     * @return the horizontal local coordinate of the right border of this widget
     *
     * @since 2.0
     */
    protected final float getRight() {
        switch (alignment.horiz) {
            case MAX:
                return 0f;
            case MIDDLE:
                return getWidth() / 2f;
            case MIN:
            default:
                return getWidth();
        }
    }

    /**
     * Returns the vertical local coordinate of the top border of this widget.
     *
     * @return the vertical local coordinate of the top border of this widget
     *
     * @since 2.0
     */
    protected final float getTop() {
        switch (alignment.vert) {
            case MAX:
                return 0f;
            case MIDDLE:
                return getHeight() / 2f;
            case MIN:
            default:
                return getHeight();
        }
    }

    /**
     * Creates a new action event and posts the event to the current view. The event will be processed during the next
     * tick. Has no effect if this widget is not part of a view.
     *
     * @param id the action id
     *
     * @since 2.1
     */
    protected void triggerAction(final int id) {
        if (getView() != null) {
            getView().postEvent(new ActionEvent(this, id));
        }
    }

    private void checkVirtualKeyboard() {
        final boolean isVisible = Jeda.isVirtualKeyboardVisible();
        final boolean shouldBeVisible = this instanceof InputField;
        if (isVisible != shouldBeVisible) {
            Jeda.setVirtualKeyboardVisible(shouldBeVisible);
        }
    }
}
