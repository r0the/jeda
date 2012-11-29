/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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
import ch.jeda.platform.EventsImp;
import java.util.List;
import java.util.Set;

/**
 * This class represents events that are taking place on this window. These
 * are typically keys pressed or typed by the user or motion events such as
 * moving or clicking with a mouse, trackball, pen or finger.
 * 
 * @since 1.0
 */
public final class Events {

    private EventsImp imp;

    /**
     * Returns the current location of the pointing device in window
     * coordinates. Returns <code>null</code> if no pointing device is
     * available.
     * 
     * @return current location of the pointing device
     * 
     * @see #isPointerAvailable()
     * @since 1.0
     */
    public Location getPointerLocation() {
        return this.imp.getPointerLocation();
    }

    /**
     * Returns the location of the pointing device relative to it's last
     * location. Returns <code>Location.ORIGIN</code> if no pointing device
     * is available or no movement has occurred since the last call to
     * {@link Window#update()}.
     * 
     * @return relative location of the pointing device
     * 
     * @see #getPointerLocation()
     * @since 1.0
     */
    public Location getPointerMovement() {
        return this.imp.getPointerMovement();
    }

    /**
     * Returns a set of all keys that are currently pressed.
     *
     * @return set of all keys that are pressed.
     * 
     * @since 1.0
     */
    public Set<Key> getPressedKeys() {
        return this.imp.getPressedKeys();
    }

    /**
     * Returns a String representing the recently typed characters. When a
     * character is typed, it is appended to the String. The string is cleared
     * when {@link Window#update()} is called. Returns an empty String ("") when
     * no characters have been typed since the last call to {@link Window#update()}.
     *
     * @return recently typed characters or ""
     * 
     * @since 1.0
     */
    public String getTypedChars() {
        return this.imp.getTypedChars();
    }

    /**
     * Returns a set of all keys that have been typed recently.
     * 
     * @return recently typed keys
     * 
     * @since 1.0
     */
    public List<Key> getTypedKeys() {
        return this.imp.getTypedKeys();
    }

    /**
     * Checks whether the Jeda window has been clicked by a pointing device.
     * 
     * @return <code>true</code> if a click with a pointing device has been
     *         performed, otherwise <code>false</code> 
     * 
     * @since 1.0
     */
    public boolean isClicked() {
        return this.imp.isClicked();
    }

    /**
     * Checks whether a pointing device is currently dragged over the Jeda
     * window.
     * 
     * @return <code>true</code> if a a pointing device is dragged dragged,
     *         otherwise <code>false</code> 
     * 
     * @since 1.0
     */
    public boolean isDragging() {
        return this.imp.isDragging();
    }

    /**
     * Checks whether the specified key is currently pressed.
     *
     * @param key the key to check for
     * @return <code>true</code> if specified key is currently pressed
     * @throws NullPointerException when key is null
     * 
     * @since 1.0
     */
    public boolean isKeyPressed(Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        return this.imp.getPressedKeys().contains(key);
    }

    /**
     * Checks whether a key was typed recently.
     *
     * @param key key to check
     * @return <code>true</code> if specified key was typed recently
     * @throws NullPointerException when key is null
     * 
     * @since 1.0
     */
    public boolean isKeyTyped(Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        return this.imp.getTypedKeys().contains(key);
    }

    /**
     * Checks whether a pointer location is currently available. If this method
     * returns <code>true</code>, the method {@link #getPointerLocation()}
     * returns the current location of the pointer.
     *
     * On a device with a mouse pointer, this method only returns
     * <code>true</code>, if the mouse pointer is currently inside the Jeda 
     * window.
     * On a device with touch screen, this method returns only 
     * <code>true</code>, if the screen is currently touched by the pointing
     * device (pen or finger).
     *
     * @return <code>true</code> if pointer location is available, <code>false
     * </code> otherwise.
     * 
     * @since 1.0
     */
    public boolean isPointerAvailable() {
        return this.imp.getPointerLocation() != null;
    }

    void setImp(EventsImp imp) {
        this.imp = imp;
    }
}
