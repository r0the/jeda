package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class TcpClientTest extends Program implements MessageReceivedListener {

    TcpConnection connection;

    @Override
    public void run() {
        Jeda.addEventListener(this);
        connection = new TcpConnection();
        connection.open("www.jeda.ch", 80);

        if (connection.isOpen()) {
            writeLines("Verbunden");
            connection.sendLine("GET /wiki/start HTTP/1.1");
            connection.sendLine("Host: www.jeda.ch");
            connection.sendLine("");
        }
        else {
            writeLines("Verbindung konnte nicht hergestellt werden.");
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        writeLines(event.getLine());
    }
}
