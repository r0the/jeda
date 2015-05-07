package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class WidgetTest extends Program implements TickListener, ActionListener {

    Button buttonAntique;
    Button buttonModern;
    View view;
    Image background;
    Theme cortana;
    ProgressBar progressBar;

    @Override
    public void run() {
        view = new View();
        buttonAntique = new Button(view.getWidth() / 2, 100, Alignment.CENTER, "Antique");
        buttonAntique.setStyle(DefaultButtonStyle.ANTIQUE_BROWN);

        buttonModern = new Button(view.getWidth() / 2, 200, Alignment.CENTER, "Modern");
        buttonModern.setStyle(DefaultButtonStyle.MODERN_GREEN);

        view.add(buttonAntique);
        view.add(buttonModern);
        view.addEventListener(this);
    }

    @Override
    public void onTick(TickEvent event) {
        if (progressBar != null) {
            progressBar.setValue(progressBar.getValue() + event.getDuration());
        }
    }

    @Override
    public void onAction(ActionEvent event) {
        if ("Antique".equals(event.getName())) {
            initAntique();
            showSamples();
        }
        else if ("Modern".equals(event.getName())) {
            initModern();
            showSamples();
        }
        else if ("Start".equals(event.getName())) {
            progressBar.setValue(0.0);
        }
    }

    private void initAntique() {
        Theme.setDefault(Theme.ANTIQUE);
        view.getBackground().drawImage(0, 0, new Image("res:drawable/background_antique.png"));
    }

    private void initModern() {
        view.getBackground().drawImage(0, 0, new Image("res:drawable/background_modern.jpg"));
    }

    private void showSamples() {
        view.remove(buttonAntique);
        view.remove(buttonModern);
        StringInputField textField = new StringInputField(30, 55);
        view.add(new Label(35, 30, "Name:"));
        view.add(textField);

        view.add(new Label(35, 130, "Age:"));
        view.add(new IntInputField(30, 155));

        view.add(new Label(35, 230, "Toggle with [Ctrl]:"));
        CheckBox cb = new CheckBox(270, 215);
        cb.setKey(Key.CTRL_LEFT);
        view.add(cb);

        Button startProgress = new Button(35, 280, "Start");
        startProgress.setKey(Key.ENTER);
        view.add(startProgress);

        progressBar = new ProgressBar(35, 350);
        progressBar.setRange(0.0, 3.0);
        view.add(progressBar);
    }
}
