package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.image.GrayImageFilter;
import ch.jeda.image.MaskImageFilter;
import ch.jeda.image.SepiaImageFilter;
import ch.jeda.ui.*;

public class ImageTest extends Program {

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
//        image.filter(MaskImageFilter.BLUR).save("image-blur.png");
//        image.filter(MaskImageFilter.MOTION_BLUR).save("image-motionblur.png");
        image.filter(new GrayImageFilter()).save("image-gray.png");
        image.filter(new SepiaImageFilter(10)).save("image-sepia.png");
        window.drawText(10, 10, "DONE");
//        image.filter(new MonochromeFilter()).save("filter-gray.png");
    }
}
