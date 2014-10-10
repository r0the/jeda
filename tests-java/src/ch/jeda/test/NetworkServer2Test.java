package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.*;

public class NetworkServer2Test extends Program implements ServerListener {

    private static final int MAX_CONNECTIONS = 2;
    private static final int PORT = 1248;
    private TcpServer server;
    private Map<Connection, String> clientNames;

    @Override
    public void run() {
        clientNames = new HashMap<Connection, String>();
        server = new TcpServer();
        Jeda.addEventListener(this);
        if (server.start(PORT)) {
            writeLines("Server listening on port " + PORT + ".");
        }
        else {
            writeLines("Can't start server. Check if port " + PORT + " is already in use.");
        }
    }

    @Override
    public void onConnectionAccepted(ConnectionEvent event) {
        Connection connection = event.getConnection();
        if (clientNames.size() > MAX_CONNECTIONS) {
            connection.sendLine("Zur Zeit ist keine Verbindung möglich.");
            connection.close();
        }
        else {
            connection.sendLine("Willkommen beim Chat-Server. Bitte geben Sie Ihren Namen mit 'NAME=Joda' an.");
            clientNames.put(connection, "Unbekannt@" + connection.getRemoteAddress());
            String name = clientNames.get(event.getConnection());
            broadcast(name + " hat den Chat betreten.");
        }
    }

    @Override
    public void onConnectionClosed(ConnectionEvent event) {
        clientNames.remove(event.getConnection());
        String name = clientNames.get(event.getConnection());
        broadcast(name + " hat den Chat verlassen.");
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        String line = event.getLine();
        if (line.startsWith("NAME=")) {
            String oldName = clientNames.get(event.getConnection());
            clientNames.put(event.getConnection(), line.substring(5));
            String name = clientNames.get(event.getConnection());
            broadcast(oldName + " heisst nun " + name);
        }
        else {
            String name = clientNames.get(event.getConnection());
            broadcast(name + ": " + line);
        }
    }

    public void broadcast(String line) {
        for (Connection connection : clientNames.keySet()) {
            connection.sendLine(line);
        }
    }
}
