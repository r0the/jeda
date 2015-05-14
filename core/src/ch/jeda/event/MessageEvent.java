/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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

import ch.jeda.Connection;
import ch.jeda.Data;

/**
 * Represents an event of type {@link ch.jeda.event.EventType#MESSAGE_RECEIVED}.
 *
 * @since 1.4
 */
public final class MessageEvent extends ConnectionEvent {

    private final String line;

    /**
     * Constructs a new message event. The basic form of a message is always a line of text.
     *
     * @param connection the connection that generates the event
     * @param line the line of text
     *
     * @since 1.4
     */
    public MessageEvent(final Connection connection, final String line) {
        super(connection, EventType.MESSAGE_RECEIVED);
        this.line = line;
    }

    /**
     * Returns the message as a {@link ch.jeda.Data} object.
     *
     * @return the message as a {@link ch.jeda.Data} object
     *
     * @since 1.4
     */
    public Data getData() {
        return new Data(line);
    }

    /**
     * Returns the message as a line of text.
     *
     * @return the message as a line of text
     *
     * @since 1.4
     */
    public String getLine() {
        return line;
    }
}
