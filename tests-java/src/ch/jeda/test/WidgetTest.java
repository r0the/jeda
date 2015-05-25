package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class WidgetTest extends Program {

    private View view;

    @Override
    public void run() {
        view = new View();
        view.add(new StringInputField(1, 1));
        // Write initialization code here.
        view.addEventListener(this);
    }
}
