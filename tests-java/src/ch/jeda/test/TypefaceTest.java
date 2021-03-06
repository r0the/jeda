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
        drawFontSample("Custom loaded Font: ", new Typeface("res:jeda/fonts/kenvector_future.ttf"));

        String text = "Grumpy wizards make toxic brew for the evil Queen and Jack.";
        drawFontSample(text, Typeface.SERIF);
        drawFontSample(text, Typeface.SANS_SERIF);
        drawFontSample(text, Typeface.MONSPACED);
        drawFontSample(text, Typeface.KENVECTOR_FUTURE);
        drawFontSample(text, Typeface.KENVECTOR_FUTURE_THIN);
    }

    private void drawFontSample(String name, Typeface typeface) {
        window.setTypeface(typeface);
        window.drawText(10, y, name + typeface);
        y += 50;
    }
}
