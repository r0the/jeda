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
        bla.setMinimumValue(100);
        bla.setMaximumValue(600);
        bla.setHintText("Age");

        StringInputField pwd = new StringInputField(200, 600);
        pwd.setHintText("Password");
        pwd.setInputHidden(true);
        pwd.setTextColor(Color.BLACK);
        pwd.setTextSize(20);

        view.add(input, bla, pwd);
        view.addEventListener(this);
    }
}
