/*
 * Copyright (C) 2014 by Stefan Rothe
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

/**
 * Represents an event of type {@link ch.jeda.event.EventType#CONNECTION_ACCEPTED} or
 * {@link ch.jeda.event.EventType#CONNECTION_CLOSED}.
 *
 * @since 1.4
 */
public class ConnectionEvent extends Event {

    private final Connection connection;

    /**
     * Constructs a new connection event.
     *
     * @param connection the connection that generates the event
     * @param eventType the event type
     *
     * @since 1.4
     */
    public ConnectionEvent(final Connection connection, final EventType eventType) {
        super(connection, eventType);
        this.connection = connection;
    }

    /**
     * Returns the connection.
     *
     * @return the connection
     *
     * @since 1.4
     */
    public final Connection getConnection() {
        return this.connection;
    }
}
