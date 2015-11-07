package ch.jeda.ex5;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.geometry.*;
import ch.jeda.physics.*;
import ch.jeda.ui.*;

public class Bird extends Element implements TickListener {

    int i = 0;
    boolean left;

    public Bird() {
        setDrawOrder(1);
    }

    @Override
    protected void draw(Canvas canvas) {
        Image image = new Image("res:bird" + i + ".png");
        if (left) {
            image = image.flipHorizontally();
        }

        canvas.drawImage(0, 0, image);
        int a = 50 + (int) (getY() / 1000 * 255);
    }

    @Override
    public void onTick(TickEvent event) {
        if (left) {
            setPosition(getX() - 5, getY());
        }
        else {
            setPosition(getX() + 5, getY());
        }

        if (!left && getX() > getView().getWidthDp()) {
            left = true;
        }

        if (left && getX() < 0) {
            left = false;
        }
        i = i + 1;
        i = i % 16;
    }
}
