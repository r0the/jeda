package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.HashMap;
import java.util.Map;

public class SensorTest extends Program implements SensorListener {

    Window window;
    Map<Object, Float> yPos;
    float nextY = 10;
    float height = 65;

    @Override
    public void run() {
        window = new Window();
        window.setTextSize(12);
        yPos = new HashMap<Object, Float>();
        Jeda.enableSensor(SensorType.GRAVITY);
        Jeda.enableSensor(SensorType.ACCELERATION);
        Jeda.enableSensor(SensorType.LINEAR_ACCELERATION);
        Jeda.enableSensor(SensorType.MAGNETIC_FIELD);
        window.addEventListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!yPos.containsKey(event.getSource())) {
            yPos.put(event.getSource(), nextY);
            nextY = nextY + height;
        }

        float y = yPos.get(event.getSource());
        window.setColor(Color.WHITE);
        window.fillRectangle(0, y, window.getWidth(), height);
        window.setColor(Color.BLACK);
        window.drawText(10, y, "type=" + event.getSensorType());
        window.drawText(10, y + 12, "source=" + event.getSource());
        window.drawText(10, y + 24, "x = " + event.getX());
        window.drawText(10, y + 36, "y = " + event.getY());
        window.drawText(10, y + 48, "z = " + event.getZ());
    }
}
