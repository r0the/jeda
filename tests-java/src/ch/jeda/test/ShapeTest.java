package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.geometry.*;
import ch.jeda.ui.*;

public class ShapeTest extends Program {

    Window window;
    Shape[] shapes;

    @Override
    public void run() {
        window = new Window();
        shapes = new Shape[4];
//        shapes[0] = new Circle(50);
        shapes[1] = new Ellipse(0, 0, 200, 100);
        shapes[2] = new Rectangle(0, 0, 100, 200);
        shapes[3] = ((Ellipse) shapes[1]).toPolygon(24);
        int i = 0;
//        window.setTranslation(window.getWidth() / 2, window.getHeight() / 2);
        while (i < shapes.length) {
//            shapes[i].draw(window);
            ++i;
        }
        window.addEventListener(this);
    }
}
