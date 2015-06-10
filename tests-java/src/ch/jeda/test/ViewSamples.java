package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class ViewSamples extends Program {

    private View view;

    @Override
    public void run() {
        view = new View(300, 200, ViewFeature.USER_SCALE, ViewFeature.USER_SCROLL);
        Canvas background = view.getBackground();
        background.setColor(Color.LIGHT_GREEN_200);
        background.fill();
        background.setColor(Color.LIGHT_GREEN_900);
        background.setAlignment(Alignment.CENTER);
        background.setTextSize(50);
        background.drawText(background.getWidth() / 2, background.getHeight() / 2, "Hintergrund");
        Element someElement = new SomeElement();
        someElement.setPosition(10, 10);
        view.add(someElement);
        someElement = new SomeElement();
        someElement.setPosition(20, 8);
        view.add(someElement);
    }
}

class SomeElement extends Element {

    @Override
    protected void draw(Canvas canvas) {
        canvas.setColor(Color.RED_A400);
        canvas.fillCircle(0, 0, 2);
    }
}
