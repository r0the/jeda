/*
 * Copyright (C) 2011 - 2014 by Stefan Rothe
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
 * @since 1
 */
public final class Image {

    private static final Map<String, ImageImp> CACHE = new HashMap<String, ImageImp>();
    private static final Map<String, ImageImp.Encoding> FORMAT_MAP = initFormatMap();
    public static final Image JEDA_LOGO_16x16 = new Image("res:jeda/logo-16x16.png");
    public static final Image JEDA_LOGO_48x48 = new Image("res:jeda/logo-48x48.png");
    public static final Image JEDA_LOGO_64x64 = new Image("res:jeda/logo-64x64.png");
    private final ImageImp imp;

    /**
     * Constructs an image from a file. Loads the contents of the specified image file. Currently, the image file
     * formats JPEG, and PNG are supported.
     * <p>
     * The file can either be located on the local computer, or in the project. To read a file located in the project,
     * put ':' in front of the file path. For example, use the following code to load an image located in the
     * <tt>ch.jeda.samples</tt> package of the project:
     *
     * <pre><code>Image sample = new Image("src/ch/jeda/samples/sample.png");</code></pre>
     *
     * @param filePath path to the image file
     *
     * @since 1
     */
    public Image(final String filePath) {
        this(loadImp(filePath));
    }

    public Image createRotatedImage(final double angle) {
        return new Image(this.imp.createRotatedImage(angle));
    }

    /**
     * Creates a scaled copy of the image. Width and height are both scaled proportionally.
     *
     * @param factor the scaling factor
     * @return scaled image
     * @throws IllegalArgumentException if <tt>factor</tt> is not positive
     *
     * @see #createScaledImage(int, int)
     * @since 1
     */
    public Image createScaledImage(final double factor) {
        if (factor <= 0.0) {
            throw new IllegalArgumentException("factor");
        }

        final int width = Math.max((int) (this.getWidth() * factor), 1);
        final int height = Math.max((int) (this.getHeight() * factor), 1);
        return this.createScaledImage(width, height);
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
     * @see #createScaledImage(double)
     * @since 1
     */
    public Image createScaledImage(final int width, final int height) {
        if (width < 1) {
            throw new IllegalArgumentException("width");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height");
        }

        return new Image(this.imp.createScaledImage(width, height));
    }

    /**
     * Returns a rectangular part of the image as a new image.
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
     * @since 1
     */
    public Image createSubImage(final int x, final int y, final int width, final int height) {
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

        if (x + width > this.imp.getWidth()) {
            throw new IllegalArgumentException("x + width");
        }

        if (y + height > this.imp.getHeight()) {
            throw new IllegalArgumentException("y + height");
        }

        return new Image(this.imp.createSubImage(x, y, width, height));
    }

    /**
     * Returns the height of the image in pixels.
     *
     * @return height of the image
     *
     * @see #getWidth()
     * @since 1
     */
    public int getHeight() {
        return this.imp.getHeight();
    }

    /**
     * Returns the pixel values of a rectangular part of the image.
     *
     * @param x the x coordinate of the top left corner of the part
     * @param y the y coordinate of the top left corner of the part
     * @param width the width of the part
     * @param height the heigh of the part
     * @return an array containing the pixels of the specified part of the image
     * @throws IllegalArgumentException if <tt>width</tt> or <tt>height</tt> are smaller than 1
     *
     * @since 1
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

        if (x + width > this.imp.getWidth()) {
            throw new IllegalArgumentException("x + width");
        }

        if (y + height > this.imp.getHeight()) {
            throw new IllegalArgumentException("y + height");
        }

        return this.imp.getPixels(x, y, width, height);
    }

    /**
     * Returns the width of the image in pixels.
     *
     * @return width of the image
     *
     * @see #getHeight()
     * @since 1
     */
    public int getWidth() {
        return this.imp.getWidth();
    }

    /**
     * Replaces all pixels of one specific color with another color. This can be useful for creating transparent images.
     *
     * @param oldColor color to be replaced
     * @param newColor color to replace oldColor
     *
     * @since 1
     */
    public Image replacePixels(final Color oldColor, final Color newColor) {
        return new Image(this.imp.replacePixels(oldColor, newColor));
    }

    /**
     * Saves the contents of the image to a file. Saving to a resource file (i.e. a file path starting with ':') is not
     * allowed. The file path must end with a valid image file extension. Currently, the extension ".jpeg", ".jpg", and
     * ".png" are supported.
     *
     * @param filePath file to save to
     * @return <tt>true</tt> if file has been saved sucessfully
     * @throws NullPointerException if <tt>filePath</tt> is <tt>null</tt>
     *
     * @since 1
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
            Log.err("jeda.image.error.format", filePath, extension);
            return false;
        }

        final java.io.File dir = new File(filePath).getParentFile();
        if (dir != null) {
            dir.mkdirs();
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            return this.imp.write(out, FORMAT_MAP.get(extension));
        }
        catch (final IOException ex) {
            Log.err(ex, "jeda.image.error.write", filePath);
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

    Image(final ImageImp imp) {
        this.imp = imp;
    }

    ImageImp getImp() {
        return this.imp;
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
