package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.EnumMap;

public class ViewCanvasTest extends Program implements KeyDownListener,
                                                       PointerDownListener {

    private static final double BORDER = 0.5;
    private double w;
    private double h;
    private double cx;
    private double cy;
    private double r;
    private EnumMap<Alignment, Double> alignX;
    private EnumMap<Alignment, Double> alignY;
    private View view;
    private Canvas background;
    private int step;

    public ViewCanvasTest() {

    }

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
        view = new View();
        background = view.getBackground();
        w = background.getWidth();
        h = background.getHeight() - 1;
        cx = w / 2;
        cy = h / 2;
        r = Math.min(w, h) / 2 - 2 * BORDER;
        step = 0;

        alignX = new EnumMap<Alignment, Double>(Alignment.class);
        alignX.put(Alignment.BOTTOM_CENTER, cx);
        alignX.put(Alignment.BOTTOM_LEFT, BORDER);
        alignX.put(Alignment.BOTTOM_RIGHT, w - BORDER);
        alignX.put(Alignment.CENTER, cx);
        alignX.put(Alignment.LEFT, BORDER);
        alignX.put(Alignment.RIGHT, w - BORDER);
        alignX.put(Alignment.TOP_CENTER, cx);
        alignX.put(Alignment.TOP_LEFT, BORDER);
        alignX.put(Alignment.TOP_RIGHT, w - BORDER);

        alignY = new EnumMap<Alignment, Double>(Alignment.class);
        alignY.put(Alignment.BOTTOM_CENTER, BORDER);
        alignY.put(Alignment.BOTTOM_LEFT, BORDER);
        alignY.put(Alignment.BOTTOM_RIGHT, BORDER);
        alignY.put(Alignment.CENTER, cy);
        alignY.put(Alignment.LEFT, cy);
        alignY.put(Alignment.RIGHT, cy);
        alignY.put(Alignment.TOP_CENTER, h - BORDER);
        alignY.put(Alignment.TOP_LEFT, h - BORDER);
        alignY.put(Alignment.TOP_RIGHT, h - BORDER);

        view.addEventListener(this);
        nextStep();
    }

    public void nextStep() {
        background.setColor(Color.WHITE);
        background.fill();
        background.setColor(Color.BLACK);

        switch (step) {
            case 0:
                setFontSizeTest();
                break;
            case 1:
                shadowTest();
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
                // TODO
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
                view.close();
                break;
        }

        ++step;
    }

    private void msg(String message) {
        background.setAlignment(Alignment.BOTTOM_LEFT);
        background.setColor(new Color(200, 200, 200, 200));
        background.fillRectangle(0, h, w, 1);
        background.setColor(Color.BLACK);
        background.setTextSize(20);
        background.drawText(0.5, h + 0.3, message);
    }

    private void shadowTest() {
        background.setAntiAliasing(true);
        for (int i = 0; i < 10; ++i) {
            background.setColor(new Color(0, 0, 0, 10 * i));
            background.fillCircle(2, 2, 1 + 0.01 * i);
        }

        background.setColor(new Color(255, 0, 0));
        background.fillCircle(2, 2, 1);

        msg("shadow");
    }

    private void drawCircleTest() {
        background.setColor(Color.BLACK);
        background.drawCircle(cx, cy, r);
        msg("drawCircle() in black");
    }

    private void drawImageTest1() {
        Image img = Image.JEDA_LOGO_64x64;
        for (Alignment alignment : Alignment.values()) {
            background.setAlignment(alignment);
            background.drawImage(alignX.get(alignment), alignY.get(alignment), img);
        }

        msg("drawImage() with all alignments");
    }

    private void drawImageTest2() {
        Image img = Image.JEDA_LOGO_64x64;
        double x = 1.5;
        double y = 0.5;
        background.setAlignment(Alignment.BOTTOM_CENTER);
        double width = img.getWidthOn(background);
        double height = img.getHeightOn(background);
        for (int alpha = 255; alpha >= 0; alpha = alpha - 17) {
            background.drawImage(x, y + 0.5, img, alpha);
            background.drawText(x, y, "" + alpha);
            x += width + 0.5;
            if (x > w - width - 1) {
                y += height + 1;
                x = 1.5;
            }
        }

        msg("drawImage() with alpha");
    }

    private void drawPolygonTest() {
        double INSET = 2 * BORDER;
        background.drawPolygon(BORDER, BORDER, cx, INSET, w - BORDER, BORDER, w - INSET, cy, w - BORDER, h - BORDER,
                               cx, h - INSET, BORDER, h - BORDER, INSET, cy);
        msg("drawPolygonTest()");
    }

    private void drawRectangleTest() {
        int WIDTH = 2;
        int HEIGHT = 1;
        for (Alignment alignment : Alignment.values()) {
            background.setAlignment(alignment);
            background.drawRectangle(alignX.get(alignment), alignY.get(alignment), WIDTH, HEIGHT);
        }

        msg("drawRectangle() with all alignments");
    }

    private void drawTextTest() {
        for (Alignment alignment : Alignment.values()) {
            background.setAlignment(alignment);
            background.drawText(alignX.get(alignment), alignY.get(alignment), alignment.name());
        }

        msg("drawText() with all alignments");
    }

    private void fillTest1() {
        background.setColor(Color.RED);
        background.fill();
        msg("fill() in red");
    }

    private void fillTest2() {
        background.setColor(new Color(0, 255, 0, 150));
        background.fill();
        msg("fill() in green with alpha=150");
    }

    private void fillCircleTest() {
        background.setColor(Color.RED);
        background.fillCircle(cx, cy, r);
        msg("fillCircle() in red");
    }

    private void fillPolygonTest() {
        double INSET = 2 * BORDER;
        background.fillPolygon(BORDER, BORDER, cx, INSET, w - BORDER, BORDER, w - INSET, cy, w - BORDER, h - BORDER,
                               cx, h - INSET, BORDER, h - BORDER, INSET, cy);
        msg("fillPolygonTest()");
    }

    private void jedaColorTest() {
        background.setColor(new Color(126, 218, 66));
        background.fill();
        background.drawImage(w / 2, h / 2, Image.JEDA_LOGO_64x64, Alignment.CENTER);
        msg("Jeda color and Jeda logo");
    }

    private void setFontSizeTest() {
        background.drawText(1, 1, "default font size");
        background.setTextSize(10);
        background.drawText(1, 2, "font size 10");
        background.setTextSize(15);
        background.drawText(1, 3, "font size 15");
        background.setTextSize(20);
        background.drawText(1, 4, "font size 20");
        msg("drawText() with different font sizes");
    }

    private void antiAliasingTest() {
        background.setTextSize(50);
        background.fillCircle(100, 100, 50);
        background.drawText(170, 100, "Here be 'Jaggies'", Alignment.LEFT);
        background.setAntiAliasing(true);
        background.fillCircle(100, 400, 50);
        background.drawText(170, 400, "Anti-Aliasing Enabled", Alignment.LEFT);
        msg("Effect of setAntiAliasing()");
    }
}
