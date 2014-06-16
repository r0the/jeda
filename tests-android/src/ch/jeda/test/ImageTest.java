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

        Image example = Image.JEDA_LOGO_64x64.scale(2.0);
        int alpha = 255;
        window.setFontSize(15);
        int x = 100;
        double angle = 0;
        while (x <= 700) {
            window.setColor(Color.WHITE);
            window.fillRectangle(x, 40, 90, 40, Alignment.TOP_CENTER);
            window.setColor(Color.BLACK);
            window.drawText(x, 50, "alpha=" + alpha, Alignment.CENTER);
            window.drawImage(x, 150, example, alpha, Alignment.CENTER);
            alpha = alpha - 30;
            Image i = Image.JEDA_LOGO_64x64.rotate(angle);
            window.drawImage(x, 300, i, Alignment.CENTER);
            window.drawRectangle(x - i.getWidth() / 2, 300 - i.getHeight() / 2, i.getWidth(), i.getHeight());
            angle += Math.PI / 8;
            x = x + 100;
        }

        Image pacman = new Image("res:drawable/ms_pac_man.png");
        window.drawImage(50, 400, pacman);
        window.drawImage(200, 400, pacman.flipHorizontally());
        window.drawImage(350, 400, pacman.flipVertically());

    }
}
