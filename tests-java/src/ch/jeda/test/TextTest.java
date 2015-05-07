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
        text = new Text(view.getWidth() / 2, view.getHeight() / 2, "Sample Text");
        view.add(text);
        view.add(new Button(10, 10, "A-"));
        view.add(new Button(10, 70, "A+"));
        view.add(new Button(10, 130, "Sans"));
        view.add(new Button(10, 190, "Serif"));
        view.add(new Button(10, 250, "Top Left"));
        view.add(new Button(10, 310, "Bottom Right"));

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
