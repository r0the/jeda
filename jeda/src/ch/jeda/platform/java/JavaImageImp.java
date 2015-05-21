/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

class JavaImageImp implements ImageImp {

    final BufferedImage bufferedImage;

    JavaImageImp(final BufferedImage bufferedImage) {
        assert bufferedImage != null;

        this.bufferedImage = bufferedImage;
    }

    @Override
    public ImageImp flipHorizontally() {
        final int height = getHeight();
        final int width = getWidth();
        final BufferedImage result = createImage(width, height);

        result.createGraphics().drawImage(bufferedImage, width, 0, 0, height, 0, 0, width, height, null);
        return new JavaImageImp(result);
    }

    @Override
    public ImageImp flipVertically() {
        final int height = getHeight();
        final int width = getWidth();
        final BufferedImage result = createImage(width, height);

        result.createGraphics().drawImage(bufferedImage, 0, height, width, 0, 0, 0, width, height, null);
        return new JavaImageImp(result);
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public int getPixel(int x, int y) {
        return bufferedImage.getRGB(x, y);
    }

    @Override
    public int[] getPixels(final int x, final int y, final int width, final int height) {
        assert width > 0;
        assert height > 0;

        return bufferedImage.getRGB(x, y, width, height, null, 0, width);
    }

    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public ImageImp rotateRad(final double angle) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int diameter = (int) Math.ceil(Math.sqrt(width * width + height * height));
        final BufferedImage result = createImage(diameter, diameter);
        final Graphics2D graphics = result.createGraphics();
        final AffineTransform tx = new AffineTransform();
        tx.rotate(angle, diameter / 2, diameter / 2);
        graphics.setTransform(tx);
        graphics.drawImage(bufferedImage, (diameter - width) / 2, (diameter - height) / 2, null);
        return new JavaImageImp(result);
    }

    @Override
    public ImageImp scale(final int width, final int height) {
        assert width > 0;
        assert height > 0;

        final BufferedImage result = createImage(width, height);
        result.createGraphics().drawImage(
            bufferedImage, 0, 0, width, height, null);
        return new JavaImageImp(result);
    }

    @Override
    public ImageImp subImage(final int x, final int y, final int width, final int height) {
        assert width > 0;
        assert height > 0;

        return new JavaImageImp(
            bufferedImage.getSubimage(x, y, width, height));
    }

    @Override
    public boolean write(final OutputStream out, final Encoding encoding) throws IOException {
        return ImageIO.write(bufferedImage, convertEncoding(encoding), out);
    }

    private static String convertEncoding(final Encoding encoding) {
        switch (encoding) {
            case JPEG:
                return "jpg";
            case PNG:
                return "png";
            default:
                return null;
        }
    }

    private static BufferedImage createImage(final int width, final int height) {
        final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }
}
