package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.ui.*;

public class InputTest extends Program implements KeyListener,
                                                  KeyTypedListener,
                                                  PointerListener {

    Window window;
    int y;

    @Override
    public void run() {
        window = new Window();
        window.setColor(Color.WHITE);
        window.fill();
        window.addEventListener(this);
    }

    void drawMessage(String message) {
        if (y > window.getHeight()) {
            y = 10;
            window.setColor(Color.WHITE);
            window.fill();
            window.setColor(Color.BLACK);
        }

        window.drawText(10, y, message);
        y += 15;
    }

    @Override
    public void onPointerDown(PointerEvent event) {
        drawMessage(toMessage(event));
    }

    @Override
    public void onPointerMoved(PointerEvent event) {
        drawMessage(toMessage(event));
    }

    @Override
    public void onPointerUp(PointerEvent event) {
        drawMessage(toMessage(event));
    }

    @Override
    public void onKeyDown(KeyEvent event) {
        drawMessage(toMessage(event));
    }

    @Override
    public void onKeyUp(KeyEvent event) {
        drawMessage(toMessage(event));
    }

    @Override
    public void onKeyTyped(KeyEvent event) {
        drawMessage(toMessage(event));
    }

    private String toMessage(PointerEvent event) {
        return Util.toString("type=", event.getType(), ", id=", event.getPointerId(), ", x=",
                             event.getX(), ", y=", event.getY(), ", device=", event.getSource());
    }

    private String toMessage(KeyEvent event) {
        return Util.toString("type=", event.getType(), ", key=", event.getKey(), ", repeat=",
                             event.getRepeatCount(), ", device=", event.getSource());
    }
}
