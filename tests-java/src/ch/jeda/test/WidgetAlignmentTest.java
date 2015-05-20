package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class WidgetAlignmentTest extends Program {

    private static final int OFFSET = 10;
    private static final int RADIUS = 5;
    private View view;

    @Override
    public void run() {
        Theme.getDefault().setButtonStyle(DefaultButtonStyle.MODERN_GREEN);
        view = new View(700, 230);
        int w = (int) view.getWidth();
        int h = (int) view.getHeight();
        view.addEventListener(this);
        view.add(new Button(OFFSET, OFFSET, Alignment.TOP_LEFT, "Top Left"));
        view.add(new Button(w / 2, OFFSET, Alignment.TOP_CENTER, "Top"));
        view.add(new Button(w - OFFSET, OFFSET, Alignment.TOP_RIGHT, "Top Right"));
        view.add(new Button(OFFSET, h / 2, Alignment.LEFT, "Left"));
        view.add(new Button(w / 2, h / 2, Alignment.CENTER, "Center"));
        view.add(new Button(w - OFFSET, h / 2, Alignment.RIGHT, "Right"));
        view.add(new Button(OFFSET, h - OFFSET, Alignment.BOTTOM_LEFT, "Bottom Left"));
        view.add(new Button(w / 2, h - OFFSET, Alignment.BOTTOM_CENTER, "Bottom Center"));
        view.add(new Button(w - OFFSET, h - OFFSET, Alignment.BOTTOM_RIGHT, "Bottom Right"));
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
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getY() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getAngleRad() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
