package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class CanvasSamples extends Program {

    private Canvas canvas;

    @Override
    public void run() {
        View view = new View(300, 250);
        canvas = view.getBackground();
        canvas.setAntiAliasing(true);

        clear();
        markPoint(4, 3, "(x, y)");
        markLine(4, 3, 4 + 2 * Math.cos(3 * Math.PI / 4), 3 + 2 * Math.sin(3 * Math.PI / 4), "r");
        canvas.drawCircle(4, 3, 2);
        sleep(100);

        clear();
        markPoint(4, 3, "(x, y)");
        markLine(4, 3, 4 + 3, 3, "rx");
        markLine(4, 3, 4, 3 + 2, "ry");
        canvas.drawEllipse(4, 3, 3, 2);
        sleep(100);

        clear();
        canvas.drawPolygon(2, 1, 1, 4, 6, 5);
        markPoint(2, 1, "(x1, y1)");
        markPoint(1, 4, "(x2, y2)");
        markPoint(6, 5, "(x3, y3)");
        sleep(100);

        clear();
        canvas.drawPolyline(2, 1, 1, 4, 6, 5);
        markPoint(2, 1, "(x1, y1)");
        markPoint(1, 4, "(x2, y2)");
        markPoint(6, 5, "(x3, y3)");
        sleep(100);

        clear();
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawRectangle(2, 1, 4, 3);
        markPoint(2, 1, "(x, y)");
        markLine(2, 4.2, 6, 4.2, "w");
        markLine(6.2, 1, 6.2, 4, "h");
        sleep(100);

        clear();
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawText(2, 1, "Greetings, programs!");
        markPoint(2, 1, "(x, y)");
        sleep(100);
    }

    private void clear() {
        canvas.setColor(Color.LIGHT_GREEN_200);
        canvas.fill();
        canvas.setColor(Color.LIGHT_GREEN_900);
    }

    private void markPoint(double x, double y, String label) {
        canvas.setColor(Color.RED_200);
        canvas.drawPolyline(x, 0, x, y);
        canvas.drawPolyline(0, y, x, y);
        canvas.setColor(Color.RED_A700);
        canvas.fillCircle(x, y, 0.1);
        canvas.setAlignment(Alignment.TOP_LEFT);
        canvas.drawText(x + 0.1, y - 0.1, label);
        canvas.setColor(Color.LIGHT_GREEN_900);
    }

    private void markLine(double x1, double y1, double x2, double y2, String label) {
        canvas.setColor(Color.RED_A700);
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawText((x1 + x2) / 2 + 0.1, (y1 + y2) / 2 + 0.1, label);
        canvas.drawPolyline(x1, y1, x2, y2);
        canvas.setColor(Color.LIGHT_GREEN_900);
    }
}
