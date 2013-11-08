/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.event;

/**
 * The listener interface for receiving key up events. To have an object receive events of type
 * {@link ch.jeda.event.EventType#KEY_UP}, have the class of the object implement the interface and register the object
 * with {@link ch.jeda.ui.Window#addEventListener(java.lang.Object)}.
 *
 * @since 1
 */
public interface KeyUpListener {

    /**
     * Invoked when a key has been released. This type of event occurs when the user releases a key on the keyboard, a
     * hardware button on the device, or a button on an input device. Use {@link ch.jeda.event.KeyEvent#getKey()} to get
     * the key that has been released.
     *
     * @param event the event
     *
     * @since 1
     */
    void onKeyUp(KeyEvent event);
}
