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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.event;

/**
 * The types of events supported by Jeda.
 *
 * @since 1.0
 * @version 3
 */
public enum EventType {

    /**
     * An action event.
     *
     * @since 1.0
     */
    ACTION,
    /**
     * A connection connect event. This type of event occurs when a new connection is accepted.
     *
     * @since 1.4
     */
    CONNECTION_ACCEPTED,
    /**
     * A connection disconnect event. This type of event occurs when a connection is closed.
     *
     * @since 1.4
     */
    CONNECTION_CLOSED,
    /**
     * A key down event. A key down event can occur when the user presses a key on the keyboard, a hardware button on
     * the device, or a button on an input device. While the user keeps pressing the key or button, the event may occur
     * repeatedly.
     *
     * @since 1.0
     */
    KEY_DOWN,
    /**
     * A key typed event. This event occurs when the system constructs a key type out of one or multiple key presses.
     * This behaviour depends largely on the system's keyboard layout settings. While the user keeps pressing the key,
     * the event may occur repeatedly.
     *
     * @since 1.0
     */
    KEY_TYPED,
    /**
     * A key up event. This type of event occurs when the user releases a key on the keyboard, a hardware button on the
     * device, or a button on an input device.
     *
     * @since 1.0
     */
    KEY_UP,
    /**
     * A message received event. This type of event occurs when a message is received via a connection.
     *
     * @since 1.4
     */
    MESSAGE_RECEIVED,
    /**
     * A pointer down event.
     *
     * @since 1.0
     */
    POINTER_DOWN,
    /**
     * @since 1.0
     */
    POINTER_MOVED,
    /**
     * @since 1.0
     */
    POINTER_UP,
    /**
     * @since 1.0
     */
    SENSOR,
    /**
     * @since 1.0
     */
    TICK,
    /**
     * A mouse wheel event.
     *
     * @since 2.0
     */
    WHEEL
}
