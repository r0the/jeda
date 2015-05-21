package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class InputEventTest extends Program implements KeyListener,
                                                       KeyTypedListener,
                                                       PointerListener,
                                                       WheelListener {

    Window window;
    int y;

    @Override
    public void run() {
        window = new Window();
        reset();
        window.addEventListener(this);
    }

    void drawMessage(String message) {
        if (y > window.getHeight()) {
            reset();
        }

        window.drawText(10, y, message);
        y += 15;
    }

    void reset() {
        y = 10;
        window.setColor(Color.WHITE);
        window.fill();
        window.setColor(Color.BLACK);
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

    @Override
    public void onWheel(PointerEvent event) {
        drawMessage(toMessage(event));
    }

    private String toMessage(PointerEvent event) {
        String result = Convert.toString("type=", event.getType(), ", id=", event.getPointerId(), ", x=",
                                         event.getX(), ", y=", event.getY(), ", device=", event.getSource());
        if (event.isPressed(Button.PRIMARY)) {
            result += ", PRIMARY";
        }
        if (event.isPressed(Button.TERTIARY)) {
            result += ", TERTIARY";
        }
        if (event.isPressed(Button.SECONDARY)) {
            result += ", SECONDARY";
        }

        return result;
    }

    private String toMessage(KeyEvent event) {
        return Convert.toString("type=", event.getType(), ", key=", event.getKey(), ", repeat=",
                                event.getRepeatCount(), ", device=", event.getSource());
    }
}
