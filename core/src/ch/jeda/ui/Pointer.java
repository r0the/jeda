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
 * This class represents a pointer (e.g. finger, pen, mouse cursor) on a Jeda
 * window.
 */
public final class Pointer {

    private final int id;
    private Location lastLocation;
    private Location location;

    @Override
    public boolean equals(Object object) {
        if (object instanceof Pointer) {
            return this.id == ((Pointer) object).id;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the current location of this pointer in window coordinates.
     * Returns <code>null</code> this pointer is currently not available.
     * 
     * @return current location of this pointer or <code>null</code>
     * 
     * @see #isAvailable()
     * @see #getX()
     * @see #getY()
     * @since 1
     */
    public Location getLocation() {
        return this.location;
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
    public Location getMovement() {
        if (this.location == null || this.lastLocation == null) {
            return Location.ORIGIN;
        }
        else {
            return this.location.minus(this.lastLocation);
        }
    }

    /**
     * Returns the x coordinate of this pointer in window coordinates.
     * Returns -1 if this pointer is currently not available.
     * 
     * @return x coordinate of this pointer or -1
     * @see #getLocation()
     * @see #getY()
     * @since 1
     */
    public int getX() {
        if (this.location == null) {
            return -1;
        }
        else {
            return this.location.x;
        }
    }

    /**
     * Returns the y coordinate of this pointer in window coordinates.
     * Returns -1 if this pointer is currently not available.
     * 
     * @return y coordinate of this pointer or -1
     * @see #getLocation()
     * @see #getX()
     * @since 1
     */
    public int getY() {
        if (this.location == null) {
            return -1;
        }
        else {
            return this.location.y;
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * Checks if this pointer is currently available.
     * On a device with touch screen, a pointer becomes available if the screen
     * is touched. The pointer remains available while the screen is beeing 
     * touched.
     * On a device with mouse cursor, the behavior depends on the {@link Window}
     * feature {@link Window.Feature#HoveringPointer}. If the feature is set,
     * the pointer representing the mouse cursor becomes available if the
     * cursor enters the window and remains available while the mouse cursor is
     * inside the window. If the feature is not set, the pointer becomes
     * available if a mouse button is pressed while the mouse cursor is inside
     * the window and remains available until the button is released.
     * 
     * @return <code>true</code> if this pointer is available, 
     *         <code>false</code> otherwise
     */
    public boolean isAvailable() {
        return this.location != null;
    }

    Pointer(int id) {
        this.id = id;
    }

    void prepare() {
        this.lastLocation = this.location;
    }

    void setLocation(Location location) {
        this.location = location;
    }
}
