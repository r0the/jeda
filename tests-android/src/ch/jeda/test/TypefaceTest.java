package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.ui.*;

public class TypefaceTest extends Program {

    Window window;
    int y;

    @Override
    public void run() {
        window = new Window();
        y = 10;
        window.setFontSize(20);
        window.setColor(Color.BLACK);
        drawFontSample("Default Serif Font: ", Typeface.SERIF);
        drawFontSample("Default Sans-Serif Font: ", Typeface.SANS_SERIF);
        drawFontSample("Default Monspaced Font: ", Typeface.MONSPACED);
        drawFontSample("Default Kenvector Future Font: ", Typeface.KENVECTOR_FUTURE);
        drawFontSample("Default Kenvector Future Thin Font: ", Typeface.KENVECTOR_FUTURE_THIN);
        drawFontSample("Custom loaded Font: ", new Typeface("res:myfont.ttf"));
    }

    private void drawFontSample(String name, Typeface typeface) {
        window.setTypeface(typeface);
        window.drawText(10, y, name + typeface.toString());
        y += 50;
    }
}
