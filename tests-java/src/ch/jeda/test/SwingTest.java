package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.awt.Frame;

public class SwingTest extends Program implements KeyDownListener {

    @Override
    public void run() {
        Window w = new Window();
        w.addEventListener(this);
    }

    @Override
    public void onKeyDown(KeyEvent event) {
        if (event.getKey() == Key.M) {
            minimize();
        }
    }

    void minimize() {
        Frame activeFrame = null;
        for (Frame frame : Frame.getFrames()) {
            if (frame.isActive()) {
                activeFrame = frame;
                break;
            }
        }

        activeFrame.setState(Frame.ICONIFIED);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex) {
        }

        activeFrame.setState(Frame.MAXIMIZED_BOTH);
        activeFrame.requestFocus();
    }
}
