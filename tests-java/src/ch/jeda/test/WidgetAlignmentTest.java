package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class WidgetAlignmentTest extends Program {

    private static final float OFFSET = 20f;
    private static final double RADIUS = 4;
    private View view;

    @Override
    public void run() {
        view = new View(700, 230);
        float w = view.getWidthDp();
        float h = view.getHeightDp();
        TextButton b = new TextButton(OFFSET, OFFSET, "Bottom Left", 0, Alignment.BOTTOM_LEFT);
        view.add(b);
        view.add(new TextButton(w / 2, OFFSET, "Bottom Center", 0, Alignment.BOTTOM_CENTER));
        view.add(new TextButton(w - OFFSET, OFFSET, "Bottom Center", 0, Alignment.BOTTOM_RIGHT));
        view.add(new TextButton(OFFSET, h / 2, "Left", 0, Alignment.LEFT));
        view.add(new TextButton(w / 2, h / 2, "Center", 0, Alignment.CENTER));
        view.add(new TextButton(w - OFFSET, h / 2, "Right", 0, Alignment.RIGHT));
        view.add(new TextButton(OFFSET, h - OFFSET, "Top Left", 0, Alignment.TOP_LEFT));
        view.add(new TextButton(w / 2, h - OFFSET, "Top Center", 0, Alignment.TOP_CENTER));
        view.add(new TextButton(w - OFFSET, h - OFFSET, "Top Right", 0, Alignment.TOP_RIGHT));
        view.add(new Overlay());
        view.addEventListener(this);
    }

    private static class Overlay extends Element {

        Overlay() {
            setDrawOrder(1001);
        }

        @Override
        protected void draw(Canvas canvas) {
            double w = canvas.getWidth();
            double h = canvas.getHeight();
            canvas.setColor(Color.RED);
            canvas.fillCircle(OFFSET, OFFSET, RADIUS);
            canvas.fillCircle(w / 2, OFFSET, RADIUS);
            canvas.fillCircle(w - OFFSET, OFFSET, RADIUS);
            canvas.fillCircle(OFFSET, h / 2, RADIUS);
            canvas.fillCircle(w / 2, h / 2, RADIUS);
            canvas.fillCircle(w - OFFSET, h / 2, RADIUS);
            canvas.fillCircle(OFFSET, h - OFFSET, RADIUS);
            canvas.fillCircle(w / 2, h - OFFSET, RADIUS);
            canvas.fillCircle(w - OFFSET, h - OFFSET, RADIUS);
        }

        @Override
        public float getX() {
            return 0;
        }

        @Override
        public float getY() {
            return 0;
        }

        @Override
        public float getAngleRad() {
            return 0;
        }
    }
}
