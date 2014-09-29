package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.HashMap;
import java.util.Map;

public class SoundTest extends Program implements ActionListener {

    Window window;
    Map<String, Sound> sounds;
    int x;
    int y;

    @Override
    public void run() {
        window = new Window();
        sounds = new HashMap<String, Sound>();
        x = 10;
        y = 10;

        addButton("Rooster (WAV)", "rooster.wav");
        window.addEventListener(this);
    }

    private void addButton(String text, String sound) {
        sounds.put(text, new Sound("res/raw/" + sound));
        new Button(window, x, y, text);
        y = y + 50;
    }

    @Override
    public void onAction(ActionEvent event) {
        for (int i = 0; i < 10; ++i) {
            sounds.get(event.getName()).play();
            sleep(100);
        }
    }
}
