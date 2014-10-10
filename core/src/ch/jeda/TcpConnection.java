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

import ch.jeda.event.EventType;
import ch.jeda.event.ConnectionEvent;
import ch.jeda.event.MessageEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Represents TCP network connection. This class is thread-safe.
 *
 * @since 1.4
 */
public final class TcpConnection extends Connection {

    private final Charset charset;
    private final Object lock;
    private BufferedReader in;
    private PrintWriter out;
    private String remoteAddress;
    private Socket socket;

    /**
     * Constructs a new network socket.
     *
     * @since 1.4
     */
    public TcpConnection() {
        this.charset = Charset.forName("UTF-8");
        this.lock = new Object();
    }

    TcpConnection(final Socket socket) {
        this();
        this.init(socket);
    }

    /**
     * Closes an open connection. Has no effect if the connection is not open.
     *
     * @since 1.4
     */
    @Override
    public void close() {
        synchronized (this.lock) {
            if (this.socket != null) {
                try {
                    this.socket.close();
                }
                catch (final IOException ex) {
                    // ignore
                }

                this.socket = null;
                this.in = null;
                this.out = null;
                Jeda.postEvent(new ConnectionEvent(this, EventType.CONNECTION_CLOSED));
            }
        }
    }

    /**
     * Tries to open a connection to the specified server. If a connection is already open, the connection will be
     * closed beforehand.
     *
     * @param hostName the host name or IP address of the server
     * @param port the TCP port of the server
     * @return <tt>true</tt> if the connection has been successfully established, otherwise <tt>false</tt>
     *
     * @since 1.4
     */
    public boolean open(final String hostName, final int port) {
        this.close();
        final Socket newSocket = new Socket();
        try {
            newSocket.connect(new InetSocketAddress(hostName, port));
            this.init(newSocket);
        }
        catch (final IOException ex) {
            this.close();
        }

        return this.isOpen();
    }

    /**
     * Returns a unique address of the remote endpoint of the connection. The address consists of the remote endpoint's
     * IP address and port number separated by a colon. Returns <tt>null</tt> if no connection has yet been established.
     *
     * @return a unique address of the remote endpoint of the connection
     *
     * @since 1.4
     */
    @Override
    public String getRemoteAddress() {
        synchronized (this.lock) {
            return this.remoteAddress;
        }
    }

    @Override
    public boolean isOpen() {
        synchronized (this.lock) {
            return this.socket != null;
        }
    }

    @Override
    public void sendLine(final String line) {
        synchronized (this.lock) {
            if (this.out != null) {
                this.out.println(line);
                this.out.flush();
            }
        }
    }

    private void init(final Socket socket) {
        try {
            synchronized (this.lock) {
                this.socket = socket;
                this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), this.charset));
                this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), this.charset));
                this.remoteAddress = Convert.toString(this.socket.getInetAddress().getHostAddress(), ':',
                                                      this.socket.getPort());
                new TcpConnectionThread(this).start();
            }
        }
        catch (final IOException ex) {
            this.close();
        }
    }

    private static class TcpConnectionThread extends Thread {

        private final TcpConnection connection;

        public TcpConnectionThread(final TcpConnection connection) {
            this.connection = connection;
            this.setName("Jeda Tcp Connection Listener (" + connection.remoteAddress + ")");
        }

        @Override
        public void run() {
            while (this.connection.isOpen()) {
                try {
                    final String line = this.connection.in.readLine();
                    if (line == null) {
                        this.connection.close();
                    }
                    else {
                        Jeda.postEvent(new MessageEvent(this.connection, line));
                    }
                }
                catch (final IOException ex) {
                    this.connection.close();
                }
            }
        }
    }
}
