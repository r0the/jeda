package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.TurnEvent;
import ch.jeda.event.TurnListener;
import ch.jeda.ui.*;

public class InputTest extends Program implements KeyListener,
                                                  KeyTypedListener,
                                                  PointerListener,
                                                  TurnListener {

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
    public void onTurn(TurnEvent event) {
        drawMessage(toMessage(event));
    }

    private String toMessage(PointerEvent event) {
        return Util.toString("type=", event.getType(), ", id=", event.getPointerId(), ", x=",
                             event.getX(), ", y=", event.getY(), ", device=", event.getSource());
    }

    private String toMessage(TurnEvent event) {
        return Util.toString("type=", event.getType(), ", amount=", event.getAmount(), ", axis=",
                             event.getAxis(), ", device=", event.getSource());
    }

    private String toMessage(KeyEvent event) {
        return Util.toString("type=", event.getType(), ", key=", event.getKey(), ", repeat=",
                             event.getRepeatCount(), ", device=", event.getSource());
    }
}
