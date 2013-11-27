package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class MusicTest extends Program implements ActionListener {

    Window window;
    Music music;
    float x;
    float y;

    @Override
    public void run() {
        window = new Window();
        music = new Music("res:raw/battle.mid");
        x = 10;
        y = 10;

        addButton("Play");
        addButton("Pause");
        addButton("Stop");
        window.addEventListener(this);
    }

    private void addButton(String text) {
        new Button(window, x, y, text);
        y = y + 60;
    }

    @Override
    public void onAction(ActionEvent event) {
        if ("Play".equals(event.getName())) {
            music.play();
        }
        else if ("Stop".equals(event.getName())) {
            music.play();
        }
        else if ("Pause".equals(event.getName())) {
            music.pause();
        }
    }
}
