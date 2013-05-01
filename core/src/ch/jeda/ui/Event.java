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
package ch.jeda.ui;

/**
 * Represents an external input for the application. An event results from an
 * external action such as the user pressing a key or moving the mouse, or a
 * sensor registering a change.
 *
 * @since 1
 */
public class Event {

    private final InputDevice inputDevice;
    private final EventType type;

    /**
     * Constructs an event.
     *
     * @param inputDevice the input device that generates the event
     * @param type the type of the event
     * @throws NullPointerException if <tt>type</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public Event(final InputDevice inputDevice, final EventType type) {
        if (inputDevice == null) {
            throw new NullPointerException("inputDevice");
        }
        if (type == null) {
            throw new NullPointerException("type");
        }

        this.inputDevice = inputDevice;
        this.type = type;
    }

    /**
     * Returns the input device that generated the event.
     *
     * @return the input device that generated the event
     *
     * @since 1
     */
    public InputDevice getInputDevice() {
        return this.inputDevice;
    }

    /**
     * Returns the event type.
     *
     * @return the event type
     *
     * @since 1
     */
    public final EventType getType() {
        return this.type;
    }
}
