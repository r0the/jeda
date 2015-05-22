package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.Map;

public class SoundTest extends Program implements ActionListener {

    View view;
    Map<String, Sound> sounds;

    @Override
    public void run() {
        view = new View();
        view.add(new ActionButton(2, 2, Icon.PLAY));
        view.addEventListener(this);
    }

    @Override
    public void onAction(ActionEvent event) {
        for (int i = 0; i < 10; ++i) {
            sounds.get(event.getName()).play();
            sleep(100);
        }
    }
}
