package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class MusicTest extends Program implements ActionListener {

    Window window;
    Music music;
    int x;
    int y;

    @Override
    public void run() {
        window = new Window();
//        music = new Music("res:raw/battle.mid");
        music = new Music("res:raw/move_forward.mp3");
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
        switch (event.getName()) {
            case "Play":
                music.play();
                break;
            case "Stop":
                music.stop();
                break;
            case "Pause":
                music.pause();
                break;
        }
    }
}
