/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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

    final BufferedImage bufferedImage;

    JavaImageImp(BufferedImage bufferedImage) {
        assert bufferedImage != null;

        this.bufferedImage = bufferedImage;
    }

    @Override
    public JavaImageImp createScaledImage(int width, int height) {
        assert width > 0;
        assert height > 0;

        final BufferedImage result = createImage(width, height);
        result.createGraphics().drawImage(
                this.bufferedImage, 0, 0, width, height, null);
        return new JavaImageImp(result);
    }

    @Override
    public ImageImp createSubImage(int x, int y, int width, int height) {
        assert width > 0;
        assert height > 0;

        return new JavaImageImp(
                this.bufferedImage.getSubimage(x, y, width, height));
    }

    @Override
    public int getHeight() {
        return this.bufferedImage.getHeight();
    }

    @Override
    public int getWidth() {
        return this.bufferedImage.getWidth();
    }

    @Override
    public JavaImageImp replacePixels(Color oldColor, Color newColor) {
        assert oldColor != null;
        assert newColor != null;

        final Image image = ReplaceColorFilter.replaceColor(this.bufferedImage, oldColor, newColor);
        final BufferedImage result = createImage(this.getWidth(), this.getHeight());
        result.getGraphics().drawImage(image, 0, 0, null);
        return new JavaImageImp(result);
    }

    @Override
    public boolean write(OutputStream out, Encoding encoding) throws IOException {
        return ImageIO.write(this.bufferedImage, convertEncoding(encoding), out);
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

    private static BufferedImage createImage(int width, int height) {
        final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    private static class ReplaceColorFilter extends RGBImageFilter {

        private final int oldColor;
        private final int newColor;

        public static java.awt.Image replaceColor(BufferedImage image, Color oldColor, Color newColor) {
            final ImageFilter filter = new ReplaceColorFilter(oldColor, newColor);
            final FilteredImageSource filteredSrc = new FilteredImageSource(image.getSource(), filter);
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
