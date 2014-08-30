package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class WidgetTest extends Program implements TickListener, ActionListener {

    Button buttonAntique;
    Button buttonModern;
    Window window;
    Image background;
    Theme cortana;
    ProgressBar progressBar;

    @Override
    public void run() {
        window = new Window();
        buttonAntique = new Button(0, 0, "Antique");
        buttonAntique.setPosition(window.getWidth() / 2, 100, Alignment.CENTER);
        buttonAntique.setText("Antique");
        buttonAntique.setStyle(DefaultButtonStyle.ANTIQUE_BROWN);

        buttonModern = new Button(0, 0, "Modern");
        buttonModern.setPosition(window.getWidth() / 2, 200, Alignment.CENTER);
        buttonModern.setText("Modern");

        window.add(buttonAntique);
        window.add(buttonModern);
        window.addEventListener(this);
    }

    @Override
    public void onTick(TickEvent event) {
        if (background != null) {
            window.drawImage(0, 0, background);
        }
        else {
            window.setColor(Color.WHITE);
            window.fill();
        }

        if (progressBar != null) {
            progressBar.setValue(progressBar.getValue() + event.getDuration());
        }
    }

    @Override
    public void onAction(ActionEvent event) {
        switch (event.getName()) {
            case "Antique":
                initAntique();
                showSamples();
                break;
            case "Modern":
                initModern();
                showSamples();
                break;
            case "Start":
                progressBar.setValue(0.0);
                break;
        }
    }

    private void initAntique() {
        Theme.setDefault(Theme.ANTIQUE);
        background = new Image("res:drawable/background_antique.png");
    }

    private void initModern() {
        background = new Image("res:drawable/background_modern.jpg");
    }

    private void showSamples() {
        window.remove(buttonAntique);
        window.remove(buttonModern);
        StringInputField textField = new StringInputField(30, 55);
        window.add(new Label(35, 30, "Name:"));
        window.add(textField);

        window.add(new Label(35, 130, "Age:"));
        window.add(new IntInputField(30, 155));

        window.add(new Label(35, 230, "Toggle with [Ctrl]:"));
        CheckBox cb = new CheckBox(270, 215);
        cb.setKey(Key.CTRL_LEFT);
        window.add(cb);

        Button startProgress = new Button(35, 280, "Start");
        startProgress.setKey(Key.SPACE);
        window.add(startProgress);

        progressBar = new ProgressBar(35, 350);
        progressBar.setRange(0.0, 3.0);
        window.add(progressBar);
    }
}
