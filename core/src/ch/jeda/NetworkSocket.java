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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Represents an endpoint for a network connection. A socket can be connected to a remote socket. This class is
 * thread-safe.
 *
 * @since 1.2
 * @deprecated Use {@link TcpConnection} instead.
 */
public final class NetworkSocket {

    private final Charset charset;
    private final Queue<String> inputBuffer;
    private final Object lock;
    private BufferedReader in;
    private PrintWriter out;
    private String remoteAddress;
    private Socket socket;

    /**
     * Constructs a new network socket.
     *
     * @since 1.2
     */
    public NetworkSocket() {
        this.charset = Charset.forName("UTF-8");
        this.inputBuffer = new ArrayDeque<String>();
        this.lock = new Object();
    }

    NetworkSocket(final Socket socket) {
        this();
        this.init(socket);
    }

    /**
     * Tries to open a connection to the specified server.
     *
     * @param hostName the host name of the server
     * @param port the port of the server
     *
     * @since 1.2
     */
    public boolean connect(final String hostName, final int port) {
        this.disconnect();

        final Socket newSocket = new Socket();
        try {
            newSocket.connect(new InetSocketAddress(hostName, port));
            this.init(newSocket);
        }
        catch (final IOException ex) {
            this.disconnect();
        }

        return this.isConnected();
    }

    /**
     * Closes an existing connection. Has no effect if the socket has no open connection.
     *
     * @since 1.2
     */
    public void disconnect() {
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
                this.inputBuffer.clear();
            }
        }
    }

    /**
     * Checks if the socket has an active connection.
     *
     * @return <tt>true</tt> if the socket has an active connection, otherwise <tt>false</tt
     *
     * @since 1.2
     */
    public boolean isConnected() {
        synchronized (this.lock) {
            return this.socket != null;
        }
    }

    /**
     * Checks if the socket has at least one data object to receive.
     *
     * @return <tt>true</tt> if the socket has at least one data object to receive, otherwise <tt>false</tt>
     *
     * @see #receiveData()
     * @since 1.2
     */
    public boolean hasData() {
        return this.hasLine();
    }

    /**
     * Checks if the socket has at least one line of text to receive.
     *
     * @return <tt>true</tt> if the socket has at least one line of text to receive, otherwise <tt>false</tt>
     *
     * @see #receiveLine()
     * @since 1.2
     */
    public boolean hasLine() {
        synchronized (this.lock) {
            if (this.socket == null) {
                return false;
            }

            if (this.inputBuffer.isEmpty()) {
                this.tryReceive();
            }

            return !this.inputBuffer.isEmpty();
        }
    }

    /**
     * Returns a unique address of the remote endpoint of the connection. The address consists of the remote endpoints
     * IP address followed by the port number. Returns <tt>null</tt> if no connection has yet been established.
     *
     * @return a unique address of the remote endpoint of the connection
     *
     * @since 1.2
     */
    public String getRemoteAddress() {
        synchronized (this.lock) {
            return this.remoteAddress;
        }
    }

    /**
     * Receives a data object.
     *
     * @return a data object
     *
     * @since 1.2
     */
    public Data receiveData() {
        synchronized (this.lock) {
            if (this.socket != null) {
                return new Data(this.receiveLine());
            }
            else {
                return new Data();
            }
        }
    }

    /**
     * Receives a line of text. Returns <tt>null</tt> if no line can be received.
     *
     * @return a line of text or <tt>null</tt>
     *
     * @see #hasLine()
     * @since 1.2
     */
    public String receiveLine() {
        synchronized (this.lock) {
            if (this.socket == null) {
                return null;
            }

            if (this.inputBuffer.isEmpty()) {
                this.tryReceive();
            }

            return this.inputBuffer.poll();
        }
    }

    /**
     * Sends a data object to the remote endpoint. Has no effect if the socket is not connected.
     *
     * @param data the data object to send
     *
     * @since 1.2
     */
    public void sendData(final Data data) {
        synchronized (this.lock) {
            if (this.out != null) {
                this.out.println(data.toLine());
                this.out.flush();
            }
        }
    }

    /**
     * Sends a line of text to the remote endpoint. Has no effect if the socket is not connected.
     *
     * @param line the line of text to send
     *
     * @since 1.2
     */
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
                this.socket.setSoTimeout(1);
                this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), this.charset));
                this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), this.charset));
                this.remoteAddress = Convert.toString(this.socket.getInetAddress().getHostAddress(), this.socket.getPort());
            }
        }
        catch (final IOException ex) {
            this.disconnect();
        }
    }

    private void tryReceive() {
        try {
            final String line = this.in.readLine();
            if (line == null) {
                this.disconnect();
            }
            else {
                this.inputBuffer.add(line);
            }
        }
        catch (final SocketTimeoutException ex) {
            // no data to receive, ignore
        }
        catch (final IOException ex) {
            this.disconnect();
        }
    }
}
