package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class WidgetAlignmentTest extends Program {

    private static final float OFFSET = 0.5f;
    private static final double RADIUS = 0.2;
    private View view;

    @Override
    public void run() {
        view = new View(700, 230);
        float w = view.getWidth();
        float h = view.getHeight();
        view.addEventListener(this);
        view.add(new TextButton(OFFSET, OFFSET, Alignment.BOTTOM_LEFT, "Bottom Left"));
        view.add(new TextButton(w / 2, OFFSET, Alignment.BOTTOM_CENTER, "Bottom Center"));
        view.add(new TextButton(w - OFFSET, OFFSET, Alignment.BOTTOM_RIGHT, "Bottom Right"));
        view.add(new TextButton(OFFSET, h / 2, Alignment.LEFT, "Left"));
        view.add(new TextButton(w / 2, h / 2, Alignment.CENTER, "Center"));
        view.add(new TextButton(w - OFFSET, h / 2, Alignment.RIGHT, "Right"));
        view.add(new TextButton(OFFSET, h - OFFSET, Alignment.TOP_LEFT, "Top Left"));
        view.add(new TextButton(w / 2, h - OFFSET, Alignment.TOP_CENTER, "Top Center"));
        view.add(new TextButton(w - OFFSET, h - OFFSET, Alignment.TOP_RIGHT, "Top Right"));
        view.add(new Overlay());
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
