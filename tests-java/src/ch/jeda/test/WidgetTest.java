package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class WidgetTest extends Program {

    private View view;

    @Override
    public void run() {
        view = new View();
        StringInputField input = new StringInputField(200, 200);
        input.setValue("Greetings, programs!");
        input.setTextColor(Color.BLACK);
        input.setTextSize(20);

        IntInputField bla = new IntInputField(200, 400);
        bla.setHintText("Age");
        view.add(input, bla);
        // Write initialization code here.
        view.addEventListener(this);
    }
}
