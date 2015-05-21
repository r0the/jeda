package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class ViewTest extends Program implements KeyDownListener, WheelListener {

    View view;
    double angle;

    @Override
    public void run() {
        view = new View(ViewFeature.USER_SCROLL);
        view.add(new TestElement());
        draw();
        view.addEventListener(this);
    }

    @Override
    public void onKeyDown(KeyEvent event) {
        if (event.getKey() == Key.UP) {
            view.setScale(view.getScale() * 1.1);
            draw();
        }

        if (event.getKey() == Key.DOWN) {
            view.setScale(view.getScale() / 1.1);
            draw();
        }

        if (event.getKey() == Key.LEFT) {
            view.translate(-0.01, 0);
            draw();
        }
    }

    private void draw() {
        Canvas canvas = view.getBackground();
        canvas.setColor(Color.BLUE);
        canvas.setAntiAliasing(true);
        canvas.fill();
        canvas.setColor(Color.WHITE);
        canvas.drawCircle(0, 0, 1);
        canvas.drawEllipse(0, 0, 3, 2);
        canvas.setTextSize(20);
        canvas.drawText(3, 3, "Scale=" + view.getScale());
        canvas.drawRectangle(2, 2, 4, 4);
        canvas.drawImage(2, 2, new Image("res:drawable/spain.jpg"));
        canvas.drawImage(2, 2, Image.JEDA_LOGO_64x64);
        canvas.drawLine(1, 1, 3, 1);
    }

    @Override
    public void onWheel(WheelEvent event) {
//        view.translate(-event.getDx(), -event.getDy());
        angle = angle + event.getRotation() * 0.1;
    }

    class TestElement extends Element {

        @Override
        public float getX() {
            return 10;
        }

        @Override
        public float getY() {
            return 10;
        }

        @Override
        public float getAngleRad() {
            return (float) angle;
        }

        @Override
        protected void draw(Canvas canvas) {
            canvas.setColor(Color.RED);
            canvas.fillRectangle(-2, -1, 4, 2);
        }
    }
}
