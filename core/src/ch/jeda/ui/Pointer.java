/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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

import ch.jeda.Location;

/**
 * Represents a pointer (a finger, pen, or mouse cursor) on a window.
 */
public final class Pointer {

    private final int id;
    private int deltaX;
    private int deltaY;
    private int x;
    private int y;

    @Override
    public boolean equals(Object object) {
        if (object instanceof Pointer) {
            return this.id == ((Pointer) object).id;
        }
        else {
            return false;
        }
    }

    public int getDeltaX() {
        return this.deltaX;
    }

    public int getDeltaY() {
        return this.deltaY;
    }

    /**
     * Returns the current location of the pointer in window coordinates.
     * Returns <tt>null</tt> if the pointer is currently not available.
     *
     * @return current location of this pointer or <tt>null</tt>
     *
     * @see #isAvailable()
     * @see #getX()
     * @see #getY()
     * @since 1
     */
    @Deprecated
    public Location getLocation() {
        return new Location(this.x, this.y);
    }

    /**
     * Returns the location of this pointer relative to it's last location.
     * Returns {@link  Location#ORIGIN} if this pointer is currently not
     * available or no movement has occurred since the last call to
     * {@link Window#update()}.
     *
     * @return relative location of this pointer
     *
     * @see #getLocation()
     * @since 1
     */
    @Deprecated
    public Location getMovement() {
        return new Location(this.deltaX, this.deltaY);
    }

    /**
     * Returns the x coordinate of the pointer in window coordinates. Returns
     * <tt>-1</tt> if the pointer is currently not available.
     *
     * @return x coordinate or <tt>-1</tt>
     * @see #getLocation()
     * @see #getY()
     * @since 1
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y coordinate of the pointer in window coordinates. Returns
     * <tt>-1</tt> if the pointer is currently not available.
     *
     * @return y coordinate or <tt>-1</tt>
     * @see #getLocation()
     * @see #getX()
     * @since 1
     */
    public int getY() {
        return this.y;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * Checks if the pointer is currently available. On a device with touch
     * screen, a pointer becomes available if the screen is touched. The pointer
     * remains available while the screen is being touched. On a device with
     * mouse cursor, the behavior depends on the {@link Window} feature
     * {@link WindowFeature#HoveringPointer}. If the feature is set, the
     * pointer representing the mouse cursor becomes available if the cursor
     * enters the window and remains available while the mouse cursor is inside
     * the window. If the feature is not set, the pointer becomes available if a
     * mouse button is pressed while the mouse cursor is inside the window and
     * remains available until the button is released.
     *
     * @return <tt>true</tt> if the pointer is available, <tt>false</tt>
     * otherwise
     */
    public boolean isAvailable() {
        return this.x != -1;
    }

    Pointer(int id) {
        this.id = id;
        this.x = -1;
        this.y = -1;
        this.deltaX = 0;
        this.deltaY = 0;
    }

    void prepare() {
        this.deltaX = 0;
        this.deltaY = 0;
    }

    void setLocation(int x, int y) {
        if (this.x == -1) {
            this.deltaX = 0;
            this.deltaY = 0;
        }
        else {
            this.deltaX = this.deltaX + x - this.x;
            this.deltaY = this.deltaY + y - this.y;
        }

        this.x = x;
        this.y = y;
    }

    void setUnavailable() {
        this.x = -1;
        this.y = -1;
        this.deltaX = 0;
        this.deltaY = 0;
    }
}
