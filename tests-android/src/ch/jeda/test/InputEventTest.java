package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class InputEventTest extends Program implements KeyListener,
                                                       KeyTypedListener,
                                                       PointerListener,
                                                       TurnListener,
                                                       ActionListener {

    Window window;
    int y;
    boolean keyboard;

    @Override
    public void run() {
        window = new Window();
        reset();
        window.add(new Button(10, 10, "Keyboard"));
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
        y = 100;
        window.setColor(Color.WHITE);
        window.fill();
        window.setColor(Color.BLACK);
    }

    @Override
    public void onAction(ActionEvent event) {
        keyboard = !keyboard;
        Jeda.setVirtualKeyboardVisible(keyboard);
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
    public void onTurn(TurnEvent event) {
        drawMessage(toMessage(event));
    }

    private String toMessage(PointerEvent event) {
        return Convert.toString("type=", event.getType(), ", id=", event.getPointerId(), ", x=",
                                event.getX(), ", y=", event.getY(), ", device=", event.getSource());
    }

    private String toMessage(TurnEvent event) {
        return Convert.toString("type=", event.getType(), ", amount=", event.getAmount(), ", axis=",
                                event.getAxis(), ", device=", event.getSource());
    }

    private String toMessage(KeyEvent event) {
        if (event.getKey() == Key.UNDEFINED) {
            return Convert.toString("type=", event.getType(), ", char=", event.getKeyChar(), ", repeat=",
                                    event.getRepeatCount(), ", device=", event.getSource());
        }
        else {
            return Convert.toString("type=", event.getType(), ", key=", event.getKey(), ", repeat=",
                                    event.getRepeatCount(), ", device=", event.getSource());
        }
    }

}
