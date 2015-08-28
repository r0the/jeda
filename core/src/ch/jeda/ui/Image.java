/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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
package ch.jeda.ui;

import ch.jeda.Log;
import ch.jeda.JedaInternal;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a bitmap image. A bitmap image can be loaded from an image file or be obtained by making a snapshot of a
 * {@link Canvas}. Simple image transformations are also supported.
 *
 * @since 1.0
 * @version 3
 */
public final class Image {

    private static final Map<String, ImageImp> CACHE = new HashMap<String, ImageImp>();
    private static final Map<String, ImageImp.Encoding> FORMAT_MAP = initFormatMap();
    /**
     * @since 1.0
     */
    public static final Image JEDA_LOGO_16x16 = new Image("res:jeda/logo-16x16.png");
    /**
     * @since 1.0
     */
    public static final Image JEDA_LOGO_48x48 = new Image("res:jeda/logo-48x48.png");
    /**
     * @since 1.0
     */
    public static final Image JEDA_LOGO_64x64 = new Image("res:jeda/logo-64x64.png");
    private final ImageImp imp;

    /**
     * Constructs an image from a file. Loads the contents of the specified image file. Currently, the image file
     * formats JPEG, and PNG are supported.
     * <p>
     * The file can either be located on the local computer, or in the project. To read a file located in the project,
     * put 'res:' in front of the file path. For example, use the following code to load an image located in the
     * <tt>ch.jeda.samples</tt> package of the project:
     *
     * <tt>Image sample = new Image("src/ch/jeda/samples/sample.png");</tt>
     *
     * @param filePath path to the image file
     *
     * @since 1.0
     */
    public Image(final String filePath) {
        this(loadImp(filePath));
    }

    Image(final ImageImp imp) {
        this.imp = imp;
    }

    /**
     * Creates a filtered copy of the image. The new image has the same width and height as this image. The pixel colors
     * of the new image are determined by calling {@link ch.jeda.ui.ImageFilter#apply(ch.jeda.ui.Image, int, int)} for
     * each pixel.
     *
     * @param filter the image filter
     * @return the new image
     * @throws NullPointerException if <tt>filter</tt> is <tt>null</tt>
     *
     * @since 2.1
     */
    public Image filter(final ImageFilter filter) {
        final CanvasImp canvas = JedaInternal.createCanvasImp(this.getWidth(), this.getHeight());
        for (int x = 0; x < this.getWidth(); ++x) {
            for (int y = 0; y < this.getHeight(); ++y) {
                canvas.setPixel(x, y, filter.apply(this, x, y));
            }
        }
        return new Image(canvas.takeSnapshot(0, 0, canvas.getWidth(), canvas.getHeight()));
    }

    /**
     * Creates a horizontally flipped copy of this image.
     *
     * @return a horizontally flipped copy of this image
     *
     * @see #flipVertically()
     * @since 1.1
     */
    public Image flipHorizontally() {
        return new Image(imp.flipHorizontally());
    }

    /**
     * Creates a vertically flipped copy of this image.
     *
     * @return a vertically flipped copy of this image
     *
     * @see #flipVertically()
     * @since 1.1
     */
    public Image flipVertically() {
        return new Image(imp.flipVertically());
    }

    /**
     * Returns the height of this mage in pixels.
     *
     * @return height of this image in pixels
     *
     * @see #getWidth()
     * @since 1.0
     */
    public int getHeight() {
        return imp.getHeight();
    }

    /**
     * Returns the pixel color at the specified coordinates. If the specified coordinates lay outside the image, the
     * color of the pixel closest to the coordinates is returned.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the pixel color
     *
     * @since 2.1
     */
    public Color getPixel(final int x, final int y) {
        return new Color(this.imp.getPixel(this.toRangeX(x), this.toRangeY(y)));
    }

    /**
     * @deprecated Use {@link #getPixel(int, int)} instead.
     */
    public int[] getPixels(final int x, final int y, final int width, final int height) {
        if (width < 1) {
            throw new IllegalArgumentException("width");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height");
        }

        if (x < 0) {
            throw new IllegalArgumentException("x");
        }

        if (y < 0) {
            throw new IllegalArgumentException("y");
        }

        if (x + width > imp.getWidth()) {
            throw new IllegalArgumentException("x + width");
        }

        if (y + height > imp.getHeight()) {
            throw new IllegalArgumentException("y + height");
        }

        return imp.getPixels(x, y, width, height);
    }

    /**
     * Returns the width of this image in pixels.
     *
     * @return width of this image in pixels
     *
     * @see #getHeight()
     * @since 1.0
     */
    public int getWidth() {
        return imp.getWidth();
    }

    /**
     * Checks if this image is available.
     *
     * @return <code>true</code> if this image is available, otherwise <code>false</code>
     *
     * @since 2.0
     */
    public boolean isAvailable() {
        return imp != null;
    }

    /**
     * @deprecated Use {@link #rotateRad(double)} instead.
     */
    public Image rotate(final double angle) {
        return rotateDeg(angle);
    }

    /**
     * Creates a rotated copy of this image.
     *
     * @param angle the angle in degrees
     * @return the rotated copy of this image
     *
     * @since 2.0
     */
    public Image rotateDeg(final double angle) {
        return rotateRad(Math.toRadians(angle));
    }

    /**
     * Creates a rotated copy of this image.
     *
     * @param angle the angle in radians
     * @return the rotated copy of this image
     *
     * @since 2.0
     */
    public Image rotateRad(final double angle) {
        return new Image(imp.rotateRad(angle));
    }

    /**
     * Saves the contents of this image to a file. Saving to a resource file (i.e. a file path starting with ':') is not
     * allowed. The file path must end with a valid image file extension. Currently, the extension ".jpeg", ".jpg", and
     * ".png" are supported.
     *
     * @param filePath file to save to
     * @return <tt>true</tt> if file has been saved sucessfully
     * @throws NullPointerException if <tt>filePath</tt> is <tt>null</tt>
     *
     * @since 1.0
     */
    public boolean save(final String filePath) {
        if (filePath == null) {
            throw new NullPointerException("filePath");
        }

        final int pos = filePath.indexOf('.');
        if (pos == -1) {
            return false;
        }

        final String extension = filePath.substring(pos + 1).toLowerCase();
        if (!FORMAT_MAP.containsKey(extension)) {
            Log.e("Invalid Jeda image format '", extension, "'.");
            return false;
        }

        final java.io.File dir = new File(filePath).getParentFile();
        if (dir != null) {
            dir.mkdirs();
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            return imp.write(out, FORMAT_MAP.get(extension));
        }
        catch (final IOException ex) {
            Log.e(ex, "Error while saving image file '", filePath, "'.");
            return false;
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (final IOException ex) {
                    // ignore
                }
            }
        }
    }

    /**
     * Creates a scaled copy of this image. Width and height are both scaled proportionally.
     *
     * @param factor the scaling factor
     * @return scaled image
     * @throws IllegalArgumentException if <tt>factor</tt> is not positive
     *
     * @see #scale(int, int)
     * @since 1.0
     */
    public Image scale(final double factor) {
        if (factor <= 0.0) {
            throw new IllegalArgumentException("factor");
        }

        final int width = Math.max((int) (getWidth() * factor), 1);
        final int height = Math.max((int) (getHeight() * factor), 1);
        return scale(width, height);
    }

    /**
     * Creates a scaled copy of the image. Both width and height of the new image can be specified. The aspect ratio may
     * not be preserved.
     *
     * @param width the width of the new image
     * @param height the height of the new image
     * @return scaled image
     * @throws IllegalArgumentException if <tt>x</tt> or <tt>y</tt> are smaller than 0
     * @throws IllegalArgumentException if <tt>width</tt> or <tt>height</tt> are smaller than 1
     * @throws IllegalArgumentException if <tt>x + width</tt> is greater or equal to the image width
     * @throws IllegalArgumentException if <tt>y + height</tt> is greater or equal to the image height
     *
     * @see #scale(double)
     * @since 1.1
     */
    public Image scale(final int width, final int height) {
        if (width < 1) {
            throw new IllegalArgumentException("width");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height");
        }

        return new Image(imp.scale(width, height));
    }

    /**
     * Returns a rectangular part of this image as a new image.
     *
     * @param x the x coordinate of the top left corner of the part
     * @param y the y coordinate of the top left corner of the part
     * @param width the width of the part
     * @param height the height of the part
     * @return the specified part of the image
     * @throws IllegalArgumentException if <tt>x</tt> or <tt>y</tt> are smaller than 0
     * @throws IllegalArgumentException if <tt>width</tt> or <tt>height</tt> are smaller than 1
     * @throws IllegalArgumentException if <tt>x + width</tt> is greater or equal to the image width
     * @throws IllegalArgumentException if <tt>y + height</tt> is greater or equal to the image height
     *
     * @since 1.1
     */
    public Image subImage(final int x, final int y, final int width, final int height) {
        if (width < 1) {
            throw new IllegalArgumentException("width");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height");
        }

        if (x < 0) {
            throw new IllegalArgumentException("x");
        }

        if (y < 0) {
            throw new IllegalArgumentException("y");
        }

        if (x + width > imp.getWidth()) {
            throw new IllegalArgumentException("x + width");
        }

        if (y + height > imp.getHeight()) {
            throw new IllegalArgumentException("y + height");
        }

        return new Image(imp.subImage(x, y, width, height));
    }

    ImageImp getImp() {
        return imp;
    }

    private int toRangeX(final int x) {
        return Math.max(0, Math.min(x, this.getWidth() - 1));
    }

    private int toRangeY(final int y) {
        return Math.max(0, Math.min(y, this.getHeight() - 1));
    }

    private static Map<String, ImageImp.Encoding> initFormatMap() {
        final Map<String, ImageImp.Encoding> result = new HashMap<String, ImageImp.Encoding>();
        result.put("jpeg", ImageImp.Encoding.JPEG);
        result.put("jpg", ImageImp.Encoding.JPEG);
        result.put("png", ImageImp.Encoding.PNG);
        return result;
    }

    private static ImageImp loadImp(final String filePath) {
        if (!CACHE.containsKey(filePath)) {
            CACHE.put(filePath, JedaInternal.createImageImp(filePath));
        }

        return CACHE.get(filePath);
    }
}
