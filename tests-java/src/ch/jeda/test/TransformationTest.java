package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.ui.*;

public class TransformationTest extends Program {

    int alpha = 150;

    @Override
    public void run() {
        Window window = new Window();

        Transformation t = new Transformation();
        t.translate(window.getWidth() / 2, window.getHeight() / 2);

        window.setTransformation(t);
        window.setColor(new Color(0, 0, 0, alpha));
        draw(window);

        t.scale(2, 2);
        window.setTransformation(t);
        window.setColor(new Color(255, 0, 0, alpha));
        draw(window);

        Transformation t2 = new Transformation();
        t2.set(t);
        t2.rotate(Math.PI * 0.25);
        window.setTransformation(t2);
        window.setColor(new Color(0, 0, 255, alpha));
        draw(window);

    }

    private void draw(Canvas canvas) {
        canvas.fillRectangle(0, 0, 100, 50, Alignment.CENTER);
        canvas.fillCircle(100, 0, 50);
        canvas.drawText(0, 30, "Hello World", Alignment.TOP_CENTER);
        canvas.drawImage(-50, 0, Image.JEDA_LOGO_48x48, alpha, Alignment.RIGHT);
    }
}
