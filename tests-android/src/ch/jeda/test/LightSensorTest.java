package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class LightSensorTest extends Program implements SensorListener {

    Window window;
    int y;

    @Override
    public void run() {
        window = new Window();
        reset();
        Jeda.enableSensor(SensorType.LIGHT);
        window.addEventListener(this);
    }

    void drawMessage(String message) {
        if (y > window.getHeight()) {
            reset();
        }

        window.drawText(10, y, message);
        y += 15;
    }

    void reset() {
        y = 10;
        window.setColor(Color.WHITE);
        window.fill();
        window.setColor(Color.BLACK);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        drawMessage(toMessage(event));
    }

    private String toMessage(SensorEvent event) {
        return Util.toString("type=", event.getSensorType(), ", value=", event.getValue(), ", x=",
                             event.getX(), ", y=", event.getY(), ", z=", event.getZ());
    }
}
