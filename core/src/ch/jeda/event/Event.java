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
 * Represents an external input for the application. An event results from an external action such as the user pressing
 * a key or moving the mouse, or a sensor registering a change.
 *
 * @since 1
 */
public class Event {

    private final Object source;
    private final EventType type;

    /**
     * Constructs an event.
     *
     * @param source the event source that generates the event
     * @param type the type of the event
     * @throws NullPointerException if <tt>type</tt> or <tt>source</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public Event(final Object source, final EventType type) {
        if (source == null) {
            throw new NullPointerException("inputDevice");
        }
        if (type == null) {
            throw new NullPointerException("type");
        }

        this.source = source;
        this.type = type;
    }

    /**
     * Returns the event source that generated the event.
     *
     * @return the event source that generated the event
     *
     * @since 1
     */
    public Object getSource() {
        return this.source;
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
