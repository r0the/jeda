package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class IconButtonTest extends Program {

    View view;
    double angle;

    @Override
    public void run() {
        view = new View();
        double x = 1.5;
        double y = 1.5;
        for (Icon icon : Icon.values()) {
            view.add(new IconButton(x, y, icon));
            x = x + 2;
            if (x > view.getWidth() - 1.5) {
                x = 1.5;
                y = y + 2;
            }
        }
    }
}
