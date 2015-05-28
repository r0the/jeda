package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class CanvasSamples extends Program {

    private Canvas canvas;

    @Override
    public void run() {
        View view = new View(250, 200);
        canvas = view.getBackground();
        canvas.setAntiAliasing(true);
        canvas.setLineWidth(2);

        clear();
        markPoint(200, 150, "(x, y)");
        markLine(200, 150, 200 + 100 * Math.cos(0.75 * Math.PI), 150 + 100 * Math.sin(0.75 * Math.PI), "r");
        canvas.drawCircle(200, 150, 100);
        sleep(1000);

        clear();
        markPoint(200, 150, "(x, y)");
        markLine(200, 150, 200 + 150, 150, "rx");
        markLine(200, 150, 200, 150 + 100, "ry");
        canvas.drawEllipse(200, 150, 150, 100);
        sleep(1000);

        clear();
        canvas.drawPolygon(100, 50, 50, 200, 300, 250);
        markPoint(100, 50, "(x1, y1)");
        markPoint(50, 200, "(x2, y2)");
        markPoint(300, 250, "(x3, y3)");
        sleep(1000);

        clear();
        canvas.drawPolyline(100, 50, 50, 200, 300, 250);
        markPoint(100, 50, "(x1, y1)");
        markPoint(50, 200, "(x2, y2)");
        markPoint(300, 250, "(x3, y3)");
        sleep(1000);

        clear();
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawRectangle(100, 50, 200, 150);
        markPoint(100, 50, "(x, y)");
        markLine(100, 200 + 8, 300, 200 + 8, "w");
        markLine(300 + 8, 50, 300 + 8, 200, "h");
        sleep(1000);

        clear();
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawText(100, 50, "Greetings, programs!");
        markPoint(100, 50, "(x, y)");
        sleep(1000);

        clear();
        Image image = new Image("res:drawable/spain.jpg");
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawImage(100, 50, 200, 150, image);
        markPoint(100, 50, "(x, y)");
        markLine(100, 200 + 8, 300, 200 + 8, "w");
        markLine(300 + 8, 50, 300 + 8, 200, "h");
        sleep(1000);
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
        canvas.fillCircle(x, y, 4);
        canvas.setAlignment(Alignment.TOP_LEFT);
        canvas.drawText(x + 8, y - 8, label);
        canvas.setColor(Color.LIGHT_GREEN_900);
    }

    private void markLine(double x1, double y1, double x2, double y2, String label) {
        canvas.setColor(Color.RED_A700);
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawText((x1 + x2) / 2 + 8, (y1 + y2) / 2 + 8, label);
        canvas.drawPolyline(x1, y1, x2, y2);
        canvas.setColor(Color.LIGHT_GREEN_900);
    }
}
