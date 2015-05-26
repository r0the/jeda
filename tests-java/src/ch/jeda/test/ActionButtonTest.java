package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class ActionButtonTest extends Program {

    private static final float BORDER = 60;
    private static final float STEP = 70;
    private View view;

    @Override
    public void run() {
        view = new View();
        double x = BORDER;
        double y = BORDER;
        for (Icon icon : Icon.values()) {
            view.add(new ActionButton(x, y, icon));
            x = x + STEP;
            if (x > view.getWidthDp() - BORDER) {
                x = BORDER;
                y = y + STEP;
            }
        }
    }
}
