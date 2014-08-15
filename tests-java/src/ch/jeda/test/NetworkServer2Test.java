package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.*;

public class NetworkServer2Test extends Program implements TickListener {

    private static final int MAX_CONNECTIONS = 2;
    private static final int PORT = 1248;
    private NetworkServer server;
    private List<NetworkSocket> sockets;

    @Override
    public void run() {
        sockets = new ArrayList<>();
        server = new NetworkServer();
        if (server.start(PORT)) {
            writeLines("Server listening on port " + PORT + ".");
        }
        else {
            writeLines("Can't start server. Check if port " + PORT + " is already in use.");
        }

        Jeda.addTickListener(this);
    }

    @Override
    public void onTick(TickEvent event) {
        if (server.hasNewConnection()) {
            NetworkSocket socket = server.acceptNewConnection();
            if (sockets.size() < MAX_CONNECTIONS) {
                writeLines("Accepting connection from " + socket.getRemoteAddress());
                sockets.add(socket);
            }
            else {
                socket.sendLine("We are full, sorry. Bye.");
                socket.disconnect();
            }
        }

        final List<NetworkSocket> deadSockets = new ArrayList<>();
        for (NetworkSocket socket : sockets) {
            if (socket.isConnected()) {
                handleClient(socket);
            }
            else {
                deadSockets.add(socket);
                writeLines("Closing connection to " + socket.getRemoteAddress());
            }
        }

        sockets.removeAll(deadSockets);
    }

    private void handleClient(NetworkSocket socket) {
        if (socket.hasLine()) {
            socket.sendLine("Echo: " + socket.receiveLine());
        }
    }
}
