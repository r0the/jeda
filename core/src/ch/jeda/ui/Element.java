/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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
 * Represents an object with a graphical representation. Elements can be added to a {@link ch.jeda.ui.Window}. The
 * window will automatically draw the elements. Every element has a <b>draw order</b> that determines the order in which
 * the elements are drawn. Elements with a smaller draw order are drawn first.
 *
 * @see ch.jeda.ui.Window#add(ch.jeda.ui.Element)
 * @see ch.jeda.ui.Window#remove(ch.jeda.ui.Element)
 * @since 1.6
 */
public abstract class Element {

    static final Comparator<Element> DRAW_ORDER = new DrawOrder();
    ElementsPage page;
    private int drawOrder;

    /**
     * Constructs a new element.
     *
     * @since 1.6
     */
    protected Element() {
    }

    /**
     * Returns the current draw order of this element. The draw order determines the order in which the elements are
     * drawn on a {@link ch.jeda.ui.Window}. Elements with a smaller draw order are drawn first.
     *
     * @return the current draw order of this element
     *
     * @see #setDrawOrder(int)
     * @since 1.6
     */
    public final int getDrawOrder() {
        return this.drawOrder;
    }

    /**
     * Sets the draw order of the element. The draw order determines the order in which the element are drawn on a
     * {@link ch.jeda.ui.Window}. Elements with a smaller draw order are drawn first.
     *
     * @param drawOrder the new draw order for this element
     *
     * @see #getDrawOrder()
     * @since 1.6
     */
    public final void setDrawOrder(final int drawOrder) {
        this.drawOrder = drawOrder;
        if (this.page != null) {
            this.page.dirty = true;
        }
    }

    /**
     * Draws the element. This method is called by the {@link ch.jeda.ui.Window} whenever the element needs to be drawn.
     * Override this method to draw the element.
     *
     * @param canvas the canvas on which the element should be drawn.
     *
     * @since 1.6
     */
    protected abstract void draw(final Canvas canvas);

    /**
     * Returns the window containing the element. Returns <tt>null</tt> if the element has not yet been added to a
     * window.
     *
     * @return the window containing the element
     *
     * @since 1.6
     */
    protected final Window getWindow() {
        if (this.page == null) {
            return null;
        }
        else {
            return this.page.window;
        }
    }

    private static class DrawOrder implements Comparator<Element> {

        @Override
        public int compare(final Element object1, final Element object2) {
            return object1.drawOrder - object2.drawOrder;
        }
    }
}
