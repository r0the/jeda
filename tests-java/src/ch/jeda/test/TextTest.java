package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class TextTest extends Program implements ActionListener {

    private View view;
    private Text text;

    @Override
    public void run() {
        view = new View();
        text = new Text((int) view.getWidth() / 2, (int) view.getHeight() / 2, "Sample Text");
        view.add(text);
        view.add(new Button(1, 1, "A-"));
        view.add(new Button(1, 2, "A+"));
        view.add(new Button(1, 3, "Sans"));
        view.add(new Button(1, 4, "Serif"));
        view.add(new Button(1, 5, "Top Left"));
        view.add(new Button(1, 6, "Bottom Right"));

        view.addEventListener(this);
    }

    @Override
    public void onAction(ActionEvent action) {
        if (action.getName().equals("A-")) {
            text.setTextSize(text.getTextSize() - 2);
        }
        if (action.getName().equals("A+")) {
            text.setTextSize(text.getTextSize() + 2);
        }
        if (action.getName().equals("Sans")) {
            text.setTypeface(Typeface.SANS_SERIF);
        }
        if (action.getName().equals("Serif")) {
            text.setTypeface(Typeface.SERIF);
        }
        if (action.getName().equals("Top Left")) {
            text.setAlignment(Alignment.TOP_LEFT);
        }
        if (action.getName().equals("Bottom Right")) {
            text.setAlignment(Alignment.BOTTOM_RIGHT);
        }
    }
}
