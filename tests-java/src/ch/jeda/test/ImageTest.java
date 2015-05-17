package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.image.GrayImageFilter;
import ch.jeda.image.MaskImageFilter;
import ch.jeda.image.SepiaImageFilter;
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
        Image image = new Image("res:drawable/spain.jpg");
        window.drawImage(0, 0, image);

        image.flipHorizontally().save("flipHorizontally.png");
        image.flipVertically().save("flipVertically.png");
        image.rotateDeg(45).save("rotate.png");
        image.scale(0.5).save("scale.png");
        image.subImage(100, 100, 600, 400).save("subImage.png");
        image.filter(EDGE).save("image-edge.png");
        image.filter(SHARPEN).save("image-sharpen.png");
        image.filter(new GrayImageFilter()).save("image-gray.png");
        image.filter(new SepiaImageFilter(10)).save("image-sepia.png");
        window.drawText(10, 10, "DONE");
    }
}
