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

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a simple network server that listens for connections on a port. This class is thread-safe.
 *
 * @since 1.2
 */
public final class NetworkServer {

    private static final int SERVER_SOCKET_TIMEOUT = 10; // milliseconds
    private final Object lock;
    private final Queue<NetworkSocket> newConnections;
    private ServerSocket serverSocket;

    /**
     * Constructs a new network server. Initially, the server is not running.
     *
     * @since 1.2
     */
    public NetworkServer() {
        this.lock = new Object();
        this.newConnections = new ConcurrentLinkedQueue<NetworkSocket>();
    }

    /**
     * Accepts a new connection from a client. Returns the network socket for the new connection. Returns <tt>null</tt>
     * if there is no connection to be accepted.
     *
     * @return the network socket for the new connection
     *
     * @see #hasNewConnection()
     * @since 1.2
     */
    public NetworkSocket acceptNewConnection() {
        return this.newConnections.poll();
    }

    /**
     * Checks if there is a new connection to be accepted.
     *
     * @return <tt>true</tt> if there is a new connection to be accepted, otherwise <tt>false</tt>
     *
     * @see #acceptNewConnection()
     * @since 1.2
     */
    public boolean hasNewConnection() {
        return !this.newConnections.isEmpty();
    }

    /**
     * Checks if the network server is running.
     *
     * @return <tt>true</tt> if the network server is running, otherwise <tt>false</tt>
     */
    public boolean isRunning() {
        synchronized (this.lock) {
            return this.serverSocket != null;
        }
    }

    /**
     * Starts the network server. The server tries to listen for connections at the specified port. Returns
     * <tt>true</tt>, if the server started listening successfully. Returns <tt>false</tt> if there was an error. This
     * could mean that
     * <ul>
     * <li>the application is not allowed to access the network.
     * <li>another application (or another instance of this application) is already using the specified port.
     * </ul>
     *
     * @param port the port on which the server will listen for connections
     * @return <tt>true</tt> if the server started listening successfully, otherwise <tt>false</tt>
     * @throws IllegalArgumentException if the port number is not between 0 and 65535.
     * @throws IllegalStateException if the server is already running
     *
     * @since 1.2
     */
    public boolean start(int port) {
        synchronized (this.lock) {
            if (this.serverSocket != null) {
                throw new IllegalStateException("Server is already running.");
            }

            try {
                this.serverSocket = new ServerSocket(port);
                this.serverSocket.setSoTimeout(SERVER_SOCKET_TIMEOUT);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        listen();
                    }
                }).start();
                return true;
            }
            catch (final SecurityException ex) {
                return false;
            }
            catch (final IOException ex) {
                return false;
            }
        }
    }

    /**
     * Stops the network server. The server stops listening. Has no effect if the server is not running.
     *
     * @since 1.2
     */
    public void stop() {
        synchronized (this.lock) {
            if (this.serverSocket != null) {
                try {
                    this.serverSocket.close();
                }
                catch (final IOException ex) {
                    // ignore
                }

                this.serverSocket = null;
            }
        }
    }

    private void listen() {
        ServerSocket s = null;
        synchronized (this.lock) {
            s = this.serverSocket;
        }

        while (s != null) {
            try {
                this.newConnections.add(new NetworkSocket(s.accept()));
            }
            catch (final IOException ex) {
                // ignore
            }

            synchronized (this.lock) {
                s = this.serverSocket;
            }
        }
    }
}
