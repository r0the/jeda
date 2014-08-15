package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class NetworkServer1Test extends Program implements TickListener {

    NetworkServer server;
    NetworkSocket socket;

    @Override
    public void run() {
        socket = new NetworkSocket();
        server = new NetworkServer();
        if (server.start(1248)) {
            writeLines("Server gestartet...");
        }
        else {
            writeLines("Kann Server nicht starten.");
        }

        Jeda.addTickListener(this);
    }

    @Override
    public void onTick(final TickEvent event) {
        if (socket.isConnected()) {
            if (socket.hasLine()) {
                String line = socket.receiveLine();
                socket.sendLine("Echo: " + line);
            }
        }
        else if (server.hasNewConnection()) {
            writeLines("Akzeptiere Verbindung...");
            socket = server.acceptNewConnection();
        }
    }
}
