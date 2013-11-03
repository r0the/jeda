package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class LogTest extends Program {

    @Override
    public void run() {
        write("Hello World");
        throw new RuntimeException("Some exception");
    }
}
