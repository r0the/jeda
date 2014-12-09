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

/**
 * Represents an object with a graphical representation. Graphics items can be added to a {@link ch.jeda.ui.Window}. The
 * window will automatically draw the graphics items. Every graphics item has a <b>draw order</b> that determines the
 * order in which the items are drawn. Graphics items with a smaller draw order are drawn first.
 *
 * @since 1.0
 * @deprecated Use {@link Element} instead.
 */
public abstract class GraphicsItem extends Element {

    /**
     * Constructs a new graphics item.
     *
     * @since 1.0
     * @deprecated
     */
    protected GraphicsItem() {
    }

    /**
     * @deprecated This method should be defined in inherited classes when needed.
     * @since 1.0
     */
    protected boolean contains(int x, int y) {
        return false;
    }
}
