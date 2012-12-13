/*
 * Copyright (C) 2012 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.platform.java;

import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.platform.ImageImp;
import ch.jeda.ui.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

class JavaImageImp implements ImageImp {

    private final BufferedImage bufferedImage;
    private final Size size;

    JavaImageImp(BufferedImage bufferedImage) {
        assert bufferedImage != null;

        this.bufferedImage = bufferedImage;
        this.size = Size.from(bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @Override
    public JavaImageImp createScaledImage(Size newSize) {
        assert newSize != null;

        BufferedImage result = createImage(newSize);
        result.createGraphics().drawImage(
                this.bufferedImage, 0, 0, newSize.width, newSize.height, null);
        return new JavaImageImp(result);
    }

    @Override
    public ImageImp createSubImage(Location topLeft, Size size) {
        assert topLeft != null;
        assert size != null;

        return new JavaImageImp(this.bufferedImage.getSubimage(
                topLeft.x, topLeft.y, size.width, size.height));
    }

    @Override
    public Size getSize() {
        return this.size;
    }

    @Override
    public JavaImageImp replacePixels(Color oldColor, Color newColor) {
        assert oldColor != null;
        assert newColor != null;

        Image image = ReplaceColorFilter.replaceColor(this.bufferedImage, oldColor, newColor);
        BufferedImage result = createImage(this.size);
        result.getGraphics().drawImage(image, 0, 0, null);
        return new JavaImageImp(result);
    }

    @Override
    public boolean write(OutputStream out, Encoding encoding) throws IOException {
        return ImageIO.write(this.bufferedImage, convertEncoding(encoding), out);
    }

    BufferedImage getBufferedImage() {
        return this.bufferedImage;
    }

    private static String convertEncoding(Encoding encoding) {
        switch (encoding) {
            case JPEG:
                return "jpg";
            case PNG:
                return "png";
            default:
                return null;
        }
    }

    private static BufferedImage createImage(Size size) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createCompatibleImage(size.width, size.height, Transparency.TRANSLUCENT);
    }

    private static class ReplaceColorFilter extends RGBImageFilter {

        private final int oldColor;
        private final int newColor;

        public static java.awt.Image replaceColor(BufferedImage image, Color oldColor, Color newColor) {
            ImageFilter filter = new ReplaceColorFilter(oldColor, newColor);
            FilteredImageSource filteredSrc = new FilteredImageSource(image.getSource(), filter);
            return Toolkit.getDefaultToolkit().createImage(filteredSrc);
        }

        ReplaceColorFilter(Color oldColor, Color newColor) {
            this.oldColor = oldColor.value;
            this.newColor = oldColor.value;
        }

        @Override
        public int filterRGB(int x, int y, int rgb) {
            if (rgb == this.oldColor) {
                return this.newColor;
            }
            else {
                return rgb;
            }
        }
    }
}
