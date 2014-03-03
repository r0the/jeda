package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.HashMap;
import java.util.Map;

public class SensorTest extends Program implements SensorListener {

    Window fenster;
    Map<Object, Float> yPos;
    float nextY = 10;
    float height = 77;

    @Override
    public void run() {
        fenster = new Window();
        fenster.setFontSize(12);
        yPos = new HashMap<Object, Float>();
        Jeda.enableSensor(SensorType.GRAVITY);
        Jeda.enableSensor(SensorType.ACCELERATION);
        Jeda.enableSensor(SensorType.LINEAR_ACCELERATION);
        Jeda.enableSensor(SensorType.MAGNETIC_FIELD);
        Jeda.enableSensor(SensorType.LIGHT);
        Jeda.enableSensor(SensorType.PRESSURE);
        Jeda.enableSensor(SensorType.PROXIMITY);
        Jeda.enableSensor(SensorType.TEMPERATURE);
        fenster.addEventListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!yPos.containsKey(event.getSource())) {
            yPos.put(event.getSource(), nextY);
            nextY = nextY + height;
        }

        float y = yPos.get(event.getSource());
        fenster.setColor(Color.WHITE);
        fenster.fillRectangle(0, y, fenster.getWidth(), height);
        fenster.setColor(Color.BLACK);
        fenster.drawText(10, y, "type=" + event.getSensorType());
        fenster.drawText(10, y + 12, "source=" + event.getSource());

        fenster.drawText(10, y + 24, valueString(event));
        fenster.drawText(10, y + 36, "x = " + event.getX());
        fenster.drawText(10, y + 48, "y = " + event.getY());
        fenster.drawText(10, y + 60, "z = " + event.getZ());
    }

    private String valueString(SensorEvent event) {
        if (event.isMaximum()) {
            return "value = " + event.getValue() + " (MAX)";
        }
        else {
            return "value = " + event.getValue();
        }
    }
}
