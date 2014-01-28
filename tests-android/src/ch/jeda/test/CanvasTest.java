package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class CanvasTest extends Program implements KeyDownListener,
                                                   PointerDownListener {

    private int w;
    private int h;
    private int cx;
    private int cy;
    private int r;
    private Window window;
    private int step;

    @Override
    public void onKeyDown(KeyEvent event) {
        nextStep();
    }

    @Override
    public void onPointerDown(PointerEvent event) {
        nextStep();
    }

    @Override
    public void run() {
        window = new Window();
        w = window.getWidth();
        h = window.getHeight() - 30;
        cx = w / 2;
        cy = h / 2;
        r = Math.min(w, h) / 2 - 10;
        step = 0;
        window.addEventListener(this);
        nextStep();
    }

    public void nextStep() {
        window.setColor(Color.WHITE);
        window.fill();
        window.setColor(Color.BLACK);

        switch (step) {
            case 0:
                setFontSizeTest();
                break;
            case 1:
                jedaColorTest();
                break;
            case 2:
                drawPolygonTest();
                break;
            case 3:
                fillPolygonTest();
                break;
            case 4:
                drawTextTest();
                break;
            case 5:
                drawImageTest1();
                break;
            case 6:
                drawImageTest2();
                break;
            case 7:
                drawRectangleTest();
                break;
            case 8:
                fillTest1();
                break;
            case 9:
                fillTest2();
                break;
            case 10:
                break;
            case 11:
                drawCircleTest();
                break;
            case 12:
                fillCircleTest();
                break;
            case 13:
                antiAliasingTest();
                break;
            case 14:
                window.close();
                break;
        }

        ++step;
    }

    private void msg(String message) {
        window.setColor(new Color(200, 200, 200, 200));
        window.fillRectangle(5, h, w - 10, 25);
        window.setColor(Color.BLACK);
        window.setFontSize(20);
        window.drawText(10, h + 5, message);
    }

    private void drawCircleTest() {
        window.setColor(Color.RED);
        window.fillCircle(cx, cy, r);
        msg("drawCircle() in red");
    }

    private void drawImageTest1() {
        Image img = Image.JEDA_LOGO_64x64;
        window.drawImage(5, 5, img, Alignment.TOP_LEFT);
        window.drawImage(cx, 5, img, Alignment.TOP_CENTER);
        window.drawImage(w - 5, 5, img, Alignment.TOP_RIGHT);
        window.drawImage(5, cy, img, Alignment.LEFT);
        window.drawImage(cx, cy, img, Alignment.CENTER);
        window.drawImage(w - 5, cy, img, Alignment.RIGHT);
        window.drawImage(5, h - 5, img, Alignment.BOTTOM_LEFT);
        window.drawImage(cx, h - 5, img, Alignment.BOTTOM_CENTER);
        window.drawImage(w - 5, h - 5, img, Alignment.BOTTOM_RIGHT);
        msg("drawImage() with all alignments");
    }

    private void drawImageTest2() {
        Image img = Image.JEDA_LOGO_64x64;
        int x = 5;
        int y = 5;
        for (int alpha = 255; alpha >= 0; alpha = alpha - 17) {
            window.drawImage(x, y, img, alpha);
            window.drawText(x, y, "" + alpha);
            x += img.getWidth() + 10;
            if (x > w - img.getWidth() - 5) {
                y += img.getHeight() + 10;
                x = 5;
            }
        }

        msg("drawImage() with alpha");
    }

    private void drawPolygonTest() {
        window.drawPolygon(5, 5, cx, 30, w - 5, 5, w - 30, cy, w - 5, h - 5, cx, h - 30, 5, h - 5, 30, cy);
        msg("drawPolygonTest()");
    }

    private void drawRectangleTest() {
        window.drawRectangle(5, 5, 50, 30, Alignment.TOP_LEFT);
        window.drawRectangle(cx, 5, 50, 30, Alignment.TOP_CENTER);
        window.drawRectangle(w - 5, 5, 50, 30, Alignment.TOP_RIGHT);
        window.drawRectangle(5, cy, 50, 30, Alignment.LEFT);
        window.drawRectangle(cx, cy, 50, 30, Alignment.CENTER);
        window.drawRectangle(w - 5, cy, 50, 30, Alignment.RIGHT);
        window.drawRectangle(5, h - 5, 50, 30, Alignment.BOTTOM_LEFT);
        window.drawRectangle(cx, h - 5, 50, 30, Alignment.BOTTOM_CENTER);
        window.drawRectangle(w - 5, h - 5, 50, 30, Alignment.BOTTOM_RIGHT);
        msg("drawRectangle() with all alignments");
    }

    private void drawTextTest() {
        window.drawText(5, 5, "TOP_LEFT", Alignment.TOP_LEFT);
        window.drawText(cx, 5, "TOP_CENTER", Alignment.TOP_CENTER);
        window.drawText(w - 5, 5, "TOP_RIGHT", Alignment.TOP_RIGHT);
        window.drawText(5, cy, "LEFT", Alignment.LEFT);
        window.drawText(cx, cy, "CENTER", Alignment.CENTER);
        window.drawText(w - 5, cy, "RIGHT", Alignment.RIGHT);
        window.drawText(5, h - 5, "BOTTOM_LEFT", Alignment.BOTTOM_LEFT);
        window.drawText(cx, h - 5, "BOTTOM_CENTER", Alignment.BOTTOM_CENTER);
        window.drawText(w - 5, h - 5, "BOTTOM_RIGHT", Alignment.BOTTOM_RIGHT);
        msg("drawText() with all alignments");
    }

    private void fillTest1() {
        window.setColor(Color.RED);
        window.fill();
        msg("fill() in red");
    }

    private void fillTest2() {
        window.setColor(new Color(0, 255, 0, 150));
        window.fill();
        msg("fill() in green with alpha=150");
    }

    private void fillCircleTest() {
        window.setColor(Color.BLACK);
        window.drawCircle(cx, cy, r);
        msg("fillCircle() in black");
    }

    private void fillPolygonTest() {
        window.fillPolygon(5, 5, cx, 30, w - 5, 5, w - 30, cy, w - 5, h - 5, cx, h - 30, 5, h - 5, 30, cy);
        msg("fillPolygonTest()");
    }

    private void jedaColorTest() {
        window.setColor(new Color(126, 218, 66));
        window.fill();
        window.drawImage(w / 2, h / 2, Image.JEDA_LOGO_64x64, Alignment.CENTER);
        msg("Jeda color and Jeda logo");
    }

    private void setFontSizeTest() {
        window.drawText(5, 5, "default font size");
        window.setFontSize(10);
        window.drawText(5, 25, "font size 10");
        window.setFontSize(15);
        window.drawText(5, 45, "font size 15");
        window.setFontSize(20);
        window.drawText(5, 65, "font size 20");
        msg("drawText() with different font sizes");
    }

    private void antiAliasingTest() {
        window.setFontSize(50);
        window.fillCircle(100, 100, 50);
        window.drawText(170, 100, "Here be 'Jaggies'", Alignment.LEFT);

        window.setAntiAliasing(true);
        window.fillCircle(100, 400, 50);
        window.drawText(170, 400, "Anti-Aliasing Enabled", Alignment.LEFT);
        msg("Effect of setAntiAliasing()");
    }
}
