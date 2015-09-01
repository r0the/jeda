package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class FullscreenTest extends Program {

    private View view;

    @Override
    public void run() {
        view = new View(ViewFeature.FULLSCREEN);
    }
}
