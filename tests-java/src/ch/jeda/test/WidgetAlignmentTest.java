package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class WidgetAlignmentTest extends Program implements TickListener {

    private static final int OFFSET = 10;
    private static final int RADIUS = 5;
    private Window window;

    @Override
    public void run() {
        Theme.getDefault().setButtonStyle(DefaultButtonStyle.MODERN_GREEN);
        window = new Window(700, 230);
        int w = window.getWidth();
        int h = window.getHeight();
        window.addEventListener(this);
        window.add(new Button(OFFSET, OFFSET, Alignment.TOP_LEFT, "Top Left"));
        window.add(new Button(w / 2, OFFSET, Alignment.TOP_CENTER, "Top"));
        window.add(new Button(w - OFFSET, OFFSET, Alignment.TOP_RIGHT, "Top Right"));
        window.add(new Button(OFFSET, h / 2, Alignment.LEFT, "Left"));
        window.add(new Button(w / 2, h / 2, Alignment.CENTER, "Center"));
        window.add(new Button(w - OFFSET, h / 2, Alignment.RIGHT, "Right"));
        window.add(new Button(OFFSET, h - OFFSET, Alignment.BOTTOM_LEFT, "Bottom Left"));
        window.add(new Button(w / 2, h - OFFSET, Alignment.BOTTOM_CENTER, "Bottom Center"));
        window.add(new Button(w - OFFSET, h - OFFSET, Alignment.BOTTOM_RIGHT, "Bottom Right"));
        window.add(new Overlay());
    }

    @Override
    public void onTick(TickEvent event) {
        window.setColor(Color.WHITE);
        window.fill();
    }

    private static class Overlay extends Element {

        Overlay() {
            setDrawOrder(1001);
        }

        @Override
        protected void draw(Canvas canvas) {
            int w = canvas.getWidth();
            int h = canvas.getHeight();
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
    }
}
