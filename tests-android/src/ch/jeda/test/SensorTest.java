package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.HashMap;
import java.util.Map;

public class SensorTest extends Program implements SensorListener {

    Canvas canvas;
    Map<Object, Double> yPos;
    double x = 0.5;
    double nextY = 0.5;
    double height = 3.5;
    double dy = 0.5;

    @Override

    public void run() {
        View view = new View();
        canvas = view.getBackground();
        canvas.setAlignment(Alignment.TOP_LEFT);
        yPos = new HashMap<Object, Double>();
        nextY = canvas.getHeight() - dy;
        Jeda.enableSensor(SensorType.GRAVITY);
        Jeda.enableSensor(SensorType.ACCELERATION);
        Jeda.enableSensor(SensorType.LINEAR_ACCELERATION);
        Jeda.enableSensor(SensorType.MAGNETIC_FIELD);
        Jeda.enableSensor(SensorType.LIGHT);
        Jeda.enableSensor(SensorType.PRESSURE);
        Jeda.enableSensor(SensorType.PROXIMITY);
        Jeda.enableSensor(SensorType.TEMPERATURE);
        view.addEventListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!yPos.containsKey(event.getSource())) {
            yPos.put(event.getSource(), nextY);
            nextY = nextY - height;
        }

        double y = yPos.get(event.getSource());
        canvas.setColor(Color.WHITE);
        canvas.fillRectangle(0, y, canvas.getWidth(), height);
        canvas.setColor(Color.BLACK);
        canvas.drawText(x, y, "type=" + event.getSensorType());
        canvas.drawText(x, y - dy, "source=" + event.getSource());

        canvas.drawText(x, y - 2 * dy, valueString(event));
        canvas.drawText(x, y - 3 * dy, "x = " + event.getX());
        canvas.drawText(x, y - 4 * dy, "y = " + event.getY());
        canvas.drawText(x, y - 5 * dy, "z = " + event.getZ());
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
