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
package ch.jeda;

import ch.jeda.event.ConnectionEvent;
import ch.jeda.event.EventType;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents a TCP network server that listens for connections on a port. This class is thread-safe.
 *
 * @since 1.4
 */
public final class TcpServer {

    private final Object lock;
    private ServerSocket serverSocket;

    /**
     * Constructs a new network server. Initially, the server is not running.
     *
     * @since 1.4
     */
    public TcpServer() {
        lock = new Object();
    }

    /**
     * Checks if the network server is running.
     *
     * @return <tt>true</tt> if the network server is running, otherwise <tt>false</tt>
     *
     * @since 1.4
     */
    public boolean isRunning() {
        synchronized (lock) {
            return serverSocket != null;
        }
    }

    /**
     * Starts the server. The server tries to listen for connections at the specified port. Returns
     * <tt>true</tt>, if the server started listening successfully. Returns <tt>false</tt> if there was an error. This
     * could mean that
     * <ul>
     * <li>the application is not allowed to access the network.
     * <li>another application (or another instance of this application) is already using the specified port.
     * </ul>
     *
     * @param port the TCP port on which the server will listen for connections
     * @return <tt>true</tt> if the server started listening successfully, otherwise <tt>false</tt>
     * @throws IllegalArgumentException if the port number is not between 0 and 65535.
     * @throws IllegalStateException if the server is already running
     *
     * @since 1.4
     */
    public boolean start(int port) {
        synchronized (lock) {
            if (serverSocket != null) {
                throw new IllegalStateException("Server is already running.");
            }

            try {
                serverSocket = new ServerSocket(port);
                new TcpServerThread(this).start();
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
     * Stops the server. The server stops listening. Has no effect if the server is not running.
     *
     * @since 1.4
     */
    public void stop() {
        synchronized (lock) {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }
                catch (final IOException ex) {
                    // ignore
                }

                serverSocket = null;
            }
        }
    }

    private Socket accept() {
        synchronized (lock) {
            if (serverSocket == null) {
                return null;
            }

            try {
                return serverSocket.accept();
            }
            catch (final IOException ex) {
                return null;
            }
        }
    }

    private static class TcpServerThread extends Thread {

        private final TcpServer server;

        public TcpServerThread(final TcpServer server) {
            this.server = server;
            setName("Jeda Tcp Server (Port " + server.serverSocket.getLocalPort() + ")");
        }

        @Override
        public void run() {
            while (server.isRunning()) {
                final Socket socket = server.accept();
                if (socket != null) {
                    Jeda.postEvent(new ConnectionEvent(new TcpConnection(socket), EventType.CONNECTION_ACCEPTED));
                }
            }
        }
    }
}
