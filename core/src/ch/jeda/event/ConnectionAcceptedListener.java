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

/**
 * The listener interface for receiving connection accepted events. To have an object receive events of type
 * {@link ch.jeda.event.EventType#CONNECTION_ACCEPTED} have the class of the object implement the interface and register
 * the object with {@link ch.jeda.Jeda#addEventListener(java.lang.Object)}.
 *
 * @since 1.4
 */
public interface ConnectionAcceptedListener {

    /**
     * Invoked when a new connection has been accepted. Use {@link ch.jeda.event.ConnectionEvent#getConnection() ()} to
     * get the new connection.
     *
     * @param event the event
     *
     * @since 1.4
     */
    void onConnectionAccepted(ConnectionEvent event);
}
