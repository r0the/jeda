package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class NetworkServerTest extends Program implements TickListener {

    NetworkServer server;
    NetworkSocket socket;

    @Override
    public void run() {
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
        if (socket == null) {
            if (server.hasNewConnection()) {
                socket = server.acceptNewConnection();
            }
        }
        else {
            if (socket.hasLine()) {
                String line = socket.receiveLine();
                socket.sendLine("Echo: " + line);
            }
        }
    }
}
