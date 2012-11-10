package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.Engine;
import ch.jeda.ui.*;

public class House extends Program {

    @Override
    public void run() {
        Engine.getCurrentEngine().setLogLevel(Log.Level.Debug);
        Window window = new Window();

        // Rasen
        window.setColor(Color.LIME);
        window.fillRectangle(0, 500, 800, 100);
        // Sonne
        window.setColor(Color.YELLOW);
        window.fillCircle(700, 100, 70);
        // Haus
        window.setColor(Color.SILVER);
        window.fillRectangle(300, 250, 400, 250);
        // Dach
        window.setColor(Color.RED);
        window.fillTriangle(300, 250, 700, 250, 500, 100);
        // Fenster
        window.setColor(Color.AQUA);
        window.fillRectangle(350, 300, 100, 200);
        window.fillRectangle(500, 300, 150, 100);
        // Fensterrahmen
        window.setColor(Color.BLACK);
        window.drawRectangle(350, 300, 100, 200);
        window.drawRectangle(500, 300, 150, 100);
        // Baumstamm
        window.setColor(Color.MAROON);
        window.fillRectangle(150, 200, 50, 300);
        // Baumkrone
        window.setColor(Color.GREEN);
        window.fillCircle(175, 200, 150);
    }
}
