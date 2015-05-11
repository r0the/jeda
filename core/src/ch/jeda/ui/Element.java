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

import ch.jeda.event.ActionEvent;
import java.util.Comparator;

/**
 * Base class for objects with a graphical representation. Elements can be added to a {@link ch.jeda.ui.View}. The view
 * will automatically draw the elements. Every element has a <b>draw order</b> that determines the order in which the
 * elements are drawn. Elements with a smaller draw order are drawn first.
 *
 * @see ch.jeda.ui.View#add(ch.jeda.ui.Element)
 * @see ch.jeda.ui.View#remove(ch.jeda.ui.Element)
 * @since 2.0
 */
public abstract class Element {

    static final Comparator<Element> DRAW_ORDER = new DrawOrder();
    private int drawOrder;
    private String name;
    private View view;

    /**
     * Constructs a new element.
     *
     * @since 2.0
     */
    protected Element() {
        this.name = null;
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
        return this.drawOrder;
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
        if (this.name == null) {
            this.name = this.getClass().getSimpleName();
        }

        return this.name;
    }

    /**
     * Sets the draw order of the element. The draw order determines the order in which the element are drawn on a
     * {@link ch.jeda.ui.Window}. Elements with a smaller draw order are drawn first.
     *
     * @param drawOrder the new draw order for this element
     *
     * @see #getDrawOrder()
     * @since 2.0
     */
    public final void setDrawOrder(final int drawOrder) {
        this.drawOrder = drawOrder;
        if (this.view != null) {
            this.view.drawOrderChanged(this);
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

        if (this.view != null) {
            this.view.removeName(this, this.getName());

            this.view.addName(this, name);
        }

        this.name = name;
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
        return this.view;
    }

    /**
     * Creates a new action event and posts the event to the current window. The event will be processed during the next
     * tick. Has no effect if the widget is part of a window.
     *
     * @param name the name of the action
     *
     * @since 1.4
     */
    protected void triggerAction(final String name) {
        final View view = this.getView();
        if (view != null) {
            view.postEvent(new ActionEvent(this, name));
        }
    }

    void addToView(final View view) {
        if (this.view != view && this.view != null) {
            this.view.remove(this);
        }

        this.view = view;
    }

    void removeFromView(final View view) {
        if (this.view == view) {
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
