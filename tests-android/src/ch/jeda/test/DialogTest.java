package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;


public class DialogTest extends Program {

    @Override
    public void run() {
        String name = Dialog.readString("Wie lautet ihr Name?");
        int zahl = Dialog.readInt(name, ", wie alt sind sie?");
    }
}
