package ch.jeda.platform.java;

import ch.jeda.event.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

class KeyReleaseTimer implements ActionListener {

    private static final int KEY_RELEASE_TIMEOUT = 2;
    private final Key key;
    private final Timer timer;
    private final JavaViewImp view;
    private boolean ok;

    public KeyReleaseTimer(final Key key, final JavaViewImp view) {
        this.key = key;
        this.timer = new Timer(KEY_RELEASE_TIMEOUT, this);
        this.view = view;
        this.ok = true;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (this.ok) {
            this.cancel();
            this.view.keyReleased(this.key);
        }
    }

    void start() {
        this.ok = true;
        this.timer.start();
    }

    void cancel() {
        this.ok = false;
        this.timer.stop();
    }
}
