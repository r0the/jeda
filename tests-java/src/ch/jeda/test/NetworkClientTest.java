package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class NetworkClientTest extends Program implements TickListener {

    NetworkSocket socket;

    @Override
    public void run() {
        socket = new NetworkSocket();
        socket.connect("jeda.ch", 80);
        if (socket.isConnected()) {
            writeLines("Verbunden");
            socket.sendLine("GET /wiki/start HTTP/1.1");
            socket.sendLine("Host: jeda.ch");
            socket.sendLine("");
        }
        else {
            writeLines("Verbindung konnte nicht hergestellt werden.");
        }

        Jeda.addTickListener(this);
    }

    @Override
    public void onTick(final TickEvent event) {
        while (socket.hasLine()) {
            writeLines(socket.receiveLine());
        }
    }
}
