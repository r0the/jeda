package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.ArrayList;
import java.util.List;

public class InputEventTest extends Program implements KeyListener,
                                                       KeyTypedListener,
                                                       PointerListener,
                                                       TurnListener,
                                                       TickListener,
                                                       ActionListener {

    Window window;
    boolean keyboard;
    List<String> messages;

    @Override
    public void run() {
        window = new Window();
        messages = new ArrayList<String>();
        window.add(new Button(10, 10, "Keyboard"));
        window.addEventListener(this);
    }

    public void onTick(TickEvent event) {
        window.setColor(Color.WHITE);
        window.fill();
        window.setColor(Color.BLACK);
        window.setTypeface(Typeface.SANS_SERIF);
        if (Jeda.isVirtualKeyboardVisible()) {
            window.drawText(250, 20, "Virtual keyboard is visible.");
        }
        else {
            window.drawText(250, 20, "Virtual keyboard is hidden.");
        }
        int y = 100;
        for (String message : messages) {
            window.drawText(10, y, message);
            y += 20;
        }
    }

    void addMessage(String message) {
        if (messages.size() > 50) {
            messages.remove(messages.size() - 1);
        }
        messages.add(0, message);
    }

    @Override
    public void onAction(ActionEvent event) {
        keyboard = !keyboard;
        Jeda.setVirtualKeyboardVisible(keyboard);
    }

    @Override
    public void onPointerDown(PointerEvent event) {
        addMessage(toMessage(event));
    }

    @Override
    public void onPointerMoved(PointerEvent event) {
        addMessage(toMessage(event));
    }

    @Override
    public void onPointerUp(PointerEvent event) {
        addMessage(toMessage(event));
    }

    @Override
    public void onKeyDown(KeyEvent event) {
        addMessage(toMessage(event));
    }

    @Override
    public void onKeyUp(KeyEvent event) {
        addMessage(toMessage(event));
    }

    @Override
    public void onKeyTyped(KeyEvent event) {
        addMessage(toMessage(event));
    }

    @Override
    public void onTurn(TurnEvent event) {
        addMessage(toMessage(event));
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
