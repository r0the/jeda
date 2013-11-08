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
package ch.jeda.ui;

/**
 * Represents a mouse cursor. The mouse cursor is the small picture that indicates the location of the mouse on the
 * screen). Mouse cursors can be changed using the {@link Window#setMouseCursor(ch.jeda.ui.MouseCursor)} method.
 *
 * @see Window
 * @since 1
 */
public enum MouseCursor {

    /**
     * A crosshair mouse cursor.
     *
     * @since 1
     */
    CROSSHAIR,
    /**
     * The default mouse cursor (an arrow).
     *
     * @since 1
     */
    DEFAULT,
    /**
     * A hand mouse cursor.
     *
     * @since 1
     */
    HAND,
    /**
     * An invisible mouse cursor.
     *
     * @since 1
     */
    INVISIBLE,
    /**
     * A text input mouse cursor (the shape of an I).
     *
     * @since 1
     */
    TEXT
}
