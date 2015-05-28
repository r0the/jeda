package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class TextTest extends Program implements ActionListener {

    private static final int INC_TEXT_SIZE = 0;
    private static final int DEC_TEXT_SIZE = 1;
    private static final int SANS = 2;
    private static final int SERIF = 3;
    private static final int TOP_LEFT = 4;
    private static final int BOTTOM_RIGHT = 5;
    private View view;
    private Text text;

    @Override
    public void run() {
        view = new View();
        text = new Text((int) view.getWidthDp() / 2, (int) view.getHeightDp() / 2, "Sample Text");
        view.add(text);
        view.add(new TextButton(40, 40, "A-", INC_TEXT_SIZE));
        view.add(new TextButton(40, 100, "A+", DEC_TEXT_SIZE));
        view.add(new TextButton(40, 160, "Sans", SANS));
        view.add(new TextButton(40, 220, "Serif", SERIF));
        view.add(new TextButton(40, 280, "Top Left", TOP_LEFT));
        view.add(new TextButton(40, 340, "Bottom Right", BOTTOM_RIGHT));

        view.addEventListener(this);
    }

    @Override
    public void onAction(ActionEvent action) {
        if (action.getId() == DEC_TEXT_SIZE) {
            text.setTextSize(text.getTextSize() - 2);
        }
        if (action.getId() == INC_TEXT_SIZE) {
            text.setTextSize(text.getTextSize() + 2);
        }
        if (action.getId() == SANS) {
            text.setTypeface(Typeface.SANS_SERIF);
        }
        if (action.getId() == SERIF) {
            text.setTypeface(Typeface.SERIF);
        }
        if (action.getId() == TOP_LEFT) {
            text.setAlignment(Alignment.TOP_LEFT);
        }
        if (action.getId() == BOTTOM_RIGHT) {
            text.setAlignment(Alignment.BOTTOM_RIGHT);
        }
    }
}
