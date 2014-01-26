package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class MusicTest extends Program implements ActionListener {

    Window window;
    Music midiMusic;
    Music oggMusic;
    int x;
    int y;

    @Override
    public void run() {
        window = new Window();
        midiMusic = new Music("res:raw/battle.ogg");
        oggMusic = new Music("res:raw/base_under_attack.ogg");
        x = 10;
        y = 10;

        addButton("Play MIDI");
        addButton("Pause MIDI");
        addButton("Stop MIDI");

        x = 120;
        y = 10;
        addButton("Play OGG");
        addButton("Pause OGG");
        addButton("Stop OGG");
        window.addEventListener(this);
    }

    private void addButton(String text) {
        new Button(window, x, y, text);
        y = y + 60;
    }

    @Override
    public void onAction(ActionEvent event) {
        if ("Play MIDI".equals(event.getName())) {
            midiMusic.play();
        }
        else if ("Pause MIDI".equals(event.getName())) {
            midiMusic.pause();
        }
        else if ("Stop MIDI".equals(event.getName())) {
            midiMusic.stop();
        }
        if ("Play OGG".equals(event.getName())) {
            oggMusic.play();
        }
        else if ("Pause OGG".equals(event.getName())) {
            oggMusic.pause();
        }
        else if ("Stop OGG".equals(event.getName())) {
            oggMusic.stop();
        }
    }
}
