package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class AlignmentSample extends Program {

    private Canvas canvas;

    @Override
    public void run() {
        View view = new View(550, 200);
        canvas = view.getBackground();
        canvas.setAntiAliasing(true);
        clear();

        canvas.drawRectangle(4, 1, 6, 3);
        canvas.setColor(Color.RED_A700);
        canvas.fillCircle(4, 1, 0.1);
        canvas.setAlignment(Alignment.TOP_RIGHT);
        canvas.drawText(3.8, 0.8, "BOTTOM_LEFT");

        canvas.fillCircle(7, 1, 0.1);
        canvas.setAlignment(Alignment.TOP_CENTER);
        canvas.drawText(7, 0.8, "BOTTOM_CENTER");

        canvas.fillCircle(10, 1, 0.1);
        canvas.setAlignment(Alignment.TOP_LEFT);
        canvas.drawText(10.2, 0.8, "BOTTOM_RIGHT");

        canvas.fillCircle(4, 2.5, 0.1);
        canvas.setAlignment(Alignment.RIGHT);
        canvas.drawText(3.8, 2.5, "LEFT");

        canvas.fillCircle(7, 2.5, 0.1);
        canvas.setAlignment(Alignment.TOP_CENTER);
        canvas.drawText(7, 2.3, "CENTER");

        canvas.fillCircle(10, 2.5, 0.1);
        canvas.setAlignment(Alignment.LEFT);
        canvas.drawText(10.2, 2.5, "RIGHT");

        canvas.fillCircle(4, 4, 0.1);
        canvas.setAlignment(Alignment.BOTTOM_RIGHT);
        canvas.drawText(3.8, 4.2, "TOP_LEFT");

        canvas.fillCircle(7, 4, 0.1);
        canvas.setAlignment(Alignment.BOTTOM_CENTER);
        canvas.drawText(7, 4.2, "TOP_CENTER");

        canvas.fillCircle(10, 4, 0.1);
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        canvas.drawText(10.2, 4.2, "TOP_RIGHT");
    }

    private void clear() {
        canvas.setColor(Color.LIGHT_GREEN_200);
        canvas.fill();
        canvas.setColor(Color.LIGHT_GREEN_900);
    }
}
