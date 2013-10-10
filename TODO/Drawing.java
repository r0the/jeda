package ch.jeda.demo;

import ch.jeda.*;
import ch.jeda.ui.*;

public class Drawing extends Program implements PointerMovedListener, ActionListener {

    Window window;
    Button red;
    Button black;
    Button clear;
    Color paint;
    int brushRadius;

    @Override
    public void run() {
        window = new Window();
        red = new Button(window, 10, 10, "Red");
        black = new Button(window, 10, 70, "Black");
        clear = new Button(window, 10, 130, "Clear");
        brushRadius = 10;
        paint = Color.BLACK;
        window.addEventListener(this);
    }

    @Override
    public void onPointerMoved(PointerEvent event) {
        if (event.getX() > 130) {
            window.setColor(paint);
            window.fillCircle(event.getX(), event.getY(), brushRadius);
        }
    }

    @Override
    public void onAction(ActionEvent event) {
        if (event.getName().equals("Red")) {
            paint = Color.RED;
        }
        if (event.getName().equals("Black")) {
            paint = Color.BLACK;
        }
        if (event.getName().equals("Clear")) {
            window.setColor(Color.WHITE);
            window.fill();
        }
    }
}
