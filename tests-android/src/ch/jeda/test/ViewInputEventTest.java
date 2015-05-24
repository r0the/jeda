package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class ViewInputEventTest extends Program implements KeyListener,
                                                           KeyTypedListener,
                                                           PointerListener,
                                                           WheelListener {

    View view;
    float y;

    @Override
    public void run() {
        view = new View();
        reset();
        view.addEventListener(this);
    }

    void drawMessage(String message) {
        if (y < 0) {
            reset();
        }

        view.getBackground().drawText(0.25, y, message);
        y = y - 0.5f;
    }

    void reset() {
        y = view.getHeight() - 0.5f;
        view.getBackground().setColor(Color.WHITE);
        view.getBackground().fill();
        view.getBackground().setColor(Color.BLACK);
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
