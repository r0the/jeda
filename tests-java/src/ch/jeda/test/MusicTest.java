package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class MusicTest extends Program implements TickListener {

    Window window;
    Music mp3Music;
    Music oggMusic;
    int x;
    int y;

    @Override
    public void run() {
        window = new Window();
        mp3Music = new Music("res:raw/move_forward.mp3");
        oggMusic = new Music("res:raw/evil_laugh.mp3");
        x = 10;
        y = 40;

        addButton("Play MP3");
        addButton("Pause MP3");
        addButton("Stop MP3");

        x = 200;
        y = 40;
        addButton("Play OGG");
        addButton("Pause OGG");
        addButton("Stop OGG");
        window.addEventListener(this);
    }

    private void addButton(String text) {
        window.add(new Button(x, y, text));
        y = y + 60;
    }

    public void onAction(ActionEvent event) {
        if ("Play MP3".equals(event.getName())) {
            mp3Music.play();
        }
        else if ("Pause MP3".equals(event.getName())) {
            mp3Music.pause();
        }
        else if ("Stop MP3".equals(event.getName())) {
            mp3Music.stop();
        }
        else if ("Play OGG".equals(event.getName())) {
            oggMusic.play();
        }
        else if ("Pause OGG".equals(event.getName())) {
            oggMusic.pause();
        }
        else if ("Stop OGG".equals(event.getName())) {
            oggMusic.stop();
        }
    }

    public void onTick(TickEvent te) {
        window.setColor(Color.WHITE);
        window.fill();
        window.setColor(Color.BLACK);
        window.drawText(10, 10, mp3Music.getPlaybackState().toString());
        window.drawText(200, 10, oggMusic.getPlaybackState().toString());
    }
}
