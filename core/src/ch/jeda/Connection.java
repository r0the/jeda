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
package ch.jeda;

/**
 * Represents a generic remote connection.
 *
 * @since 1.4
 */
public abstract class Connection {

    /**
     * Constructs a new connection.
     *
     * @since 1.4
     */
    protected Connection() {
    }

    /**
     * Closes an open connection. Has no effect if the connection is not open.
     *
     * @since 1.4
     */
    public abstract void close();

    /**
     * Returns a unique address of the remote endpoint of the connection. The form of the address depends of the type of
     * connection. Returns <tt>null</tt> if no connection has yet been established.
     *
     * @return a unique address of the remote endpoint of the connection
     *
     * @since 1.4
     */
    public abstract String getRemoteAddress();

    /**
     * Checks if the connection is currently open.
     *
     * @return <tt>true</tt> if the connection is currently open, otherwise <tt>false</tt>
     *
     * @since 1.4
     */
    public abstract boolean isOpen();

    /**
     * Sends a data object to the remote endpoint. Has no effect if the connection is not open.
     *
     * @param data the data object to send
     *
     * @since 1.4
     */
    public final void sendData(final Data data) {
        this.sendLine(data.toLine());
    }

    /**
     * Sends a line of text to the remote endpoint. Has no effect if the connection is not open.
     *
     * @param line the line of text to send
     *
     * @since 1.4
     */
    public abstract void sendLine(final String line);
}
