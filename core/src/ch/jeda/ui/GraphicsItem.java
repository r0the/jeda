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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.ui;

import java.util.Comparator;

/**
 * Represents an object with a graphical representation. Graphics items can be added to a {@link ch.jeda.ui.Window}. The
 * window will automatically draw the graphics items. Every graphics item has a <b>draw order</b> that determines the
 * order in which the items are drawn. Graphics items with a smaller draw order are drawn first.
 *
 * @see ch.jeda.ui.Window#add(ch.jeda.ui.GraphicsItem)
 * @see ch.jeda.ui.Window#remove(ch.jeda.ui.GraphicsItem)
 * @since 1.0
 */
public abstract class GraphicsItem {

    static final Comparator<GraphicsItem> DRAW_ORDER = new DrawOrder();
    GraphicsItems owner;
    private int drawOrder;

    /**
     * Constructs a new graphics item.
     *
     * @since 1.0
     */
    protected GraphicsItem() {
    }

    /**
     * Returns the current draw order of the graphics item. The draw order determines the order in which the items are
     * drawn on a {@link ch.jeda.ui.Window}. Graphics items with a smaller draw order are drawn first.
     *
     * @return the current draw order of this graphics item
     *
     * @see #setDrawOrder(int)
     * @since 1.0
     */
    public final int getDrawOrder() {
        return this.drawOrder;
    }

    /**
     * Sets the draw order of the graphics item. The draw order determines the order in which the items are drawn on a
     * {@link ch.jeda.ui.Window}. Graphics items with a smaller draw order are drawn first.
     *
     * @param drawOrder the new draw order for this graphics item
     *
     * @see #getDrawOrder()
     * @since 1.0
     */
    public final void setDrawOrder(final int drawOrder) {
        this.drawOrder = drawOrder;
        if (this.owner != null) {
            this.owner.setDirty();
        }
    }

    /**
     * @deprecated This method should be defined in inherited classes when needed.
     * @since 1.0
     */
    protected boolean contains(int x, int y) {
        return false;
    }

    /**
     * Draws the graphics item. This method is called by the {@link ch.jeda.ui.Window} whenever the item needs to be
     * drawn. Override this method to draw the item.
     *
     * @param canvas the canvas on which the graphics item should be drawn.
     *
     * @since 1.0
     */
    protected abstract void draw(final Canvas canvas);

    /**
     * Returns the window containing the graphics item. Returns <tt>null</tt> if the graphics item has not yet been
     * added to a window.
     *
     * @return the window containing the graphics item
     *
     * @since 1.0
     */
    protected final Window getWindow() {
        if (this.owner == null) {
            return null;
        }
        else {
            return this.owner.getWindow();
        }
    }

    private static class DrawOrder implements Comparator<GraphicsItem> {

        @Override
        public int compare(final GraphicsItem object1, final GraphicsItem object2) {
            return object1.drawOrder - object2.drawOrder;
        }
    }
}
