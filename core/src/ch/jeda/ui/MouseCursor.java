/*
 * Copyright (C) 2011 by Stefan Rothe
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
 * Represents a mouse cursor (the small picture that indicates the location of
 * the mouse on the screen). Mouse cursors can be changed using the
 * {@link Window#setMouseCursor(ch.jeda.ui.MouseCursor)} method.
 *
 * @version 1.0
 * @see Window
 */
public final class MouseCursor {

    /**
     * A crosshair mouse cursor.
     *
     * @since 1.0
     */
    public static final MouseCursor CROSSHAIR = new MouseCursor(1, "Crosshair");
    /**
     * The default mouse cursor (an arrow).
     *
     * @since 1.0
     */
    public static final MouseCursor DEFAULT = new MouseCursor(0, "Default");
    /**
     * A hand mouse cursor.
     *
     * @since 1.0
     */
    public static final MouseCursor HAND = new MouseCursor(12, "Hand");
    /**
     * An invisible mouse cursor.
     *
     * @since 1.0
     */
    public static final MouseCursor INVISIBLE = new MouseCursor(0, "Invisible");
    /**
     * A text input mouse cursor (the shape of an I).
     *
     * @since 1.0
     */
    public static final MouseCursor TEXT = new MouseCursor(2, "Text");
    private final int cursor;
    private final String name;

    private MouseCursor(int cursor, String name) {
        this.cursor = cursor;
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MouseCursor) {
            return this.cursor == ((MouseCursor) object).cursor;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.cursor;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
