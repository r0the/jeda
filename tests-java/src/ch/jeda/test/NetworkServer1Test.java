package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class NetworkServer1Test extends Program implements ConnectionAcceptedListener,
                                                           MessageReceivedListener {

    TcpServer server;

    @Override
    public void run() {
        server = new TcpServer();
        if (server.start(1248)) {
            writeLines("Server gestartet...");
        }
        else {
            writeLines("Kann Server nicht starten.");
        }

        Jeda.addEventListener(this);
    }

    @Override
    public void onConnectionAccepted(ConnectionEvent event) {
        Connection connection = event.getConnection();
        connection.sendLine("Willkommen beim Echo-Server.");
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        String line = event.getLine();
        event.getConnection().sendLine("Echo: " + line);
    }
}
