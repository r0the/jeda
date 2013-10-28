package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.ui.*;

public class ImageTest extends Program {

    @Override
    public void run() {
        Window window = new Window();
        window.setTitle("Image Example");
        Image image = new Image("res:drawable/background.jpg");
        window.drawImage(0, 0, image);

        Image balloon = Image.JEDA_LOGO_64x64.createScaledImage(2f);
        int alpha = 255;
        window.setFontSize(15);
        int x = 100;
        while (x <= 700) {
            window.setColor(Color.WHITE);
            window.fillRectangle(x, 40, 90, 40, Alignment.TOP_CENTER);
            window.setColor(Color.BLACK);
            window.drawText(x, 50, "alpha=" + alpha, Alignment.CENTER);
            window.drawImage(x, 150, balloon, alpha, Alignment.CENTER);
            alpha = alpha - 30;
            x = x + 100;
        }

        //Image defaultImage = new Image("no/such/image");
        //window.drawImage(100, 400, defaultImage);
    }
}
