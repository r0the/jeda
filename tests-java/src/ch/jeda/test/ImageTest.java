package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.image.*;
import ch.jeda.ui.*;

public class ImageTest extends Program {

    public static final ImageFilter SHARPEN = new MaskImageFilter(new double[][]{
        {0, -1, 0},
        {-1, 5, -1},
        {0, -1, 0}});

    public static final ImageFilter EDGE = new MaskImageFilter(new double[][]{
        {-1, -1, -1},
        {-1, 8, -1},
        {-1, -1, -1}}, 1.0);

    @Override
    public void run() {
        Window window = new Window();
        window.setTitle("Image Tests");
        Image original = new Image("res:drawable/spain.jpg");
        window.drawImage(0, 0, original);

        Image flipHorizontally = original.flipHorizontally();
        flipHorizontally.save("flipHorizontally.png");

        Image flipVertically = original.flipVertically();
        flipVertically.flipVertically();
        flipVertically.save("flipVertically.png");

//        bitmap.rotateDeg(45).save("rotate.png");
        Image scale = original.scale(0.5);
        scale.save("scale.png");

        original.subImage(100, 100, 600, 400).save("subImage.png");

        Image edge = original.filter(EDGE);
        edge.save("image-edge.png");

        Image sharpen = original.filter(SHARPEN);
        sharpen.save("image-sharpen.png");

        original.filter(new GrayImageFilter()).save("image-gray.png");
        original.filter(new SepiaImageFilter(10)).save("image-sepia.png");
        window.drawText(10, 10, "DONE");
    }
}
