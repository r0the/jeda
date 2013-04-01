/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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

import ch.jeda.Engine;
import ch.jeda.Location;
import ch.jeda.Log;
import ch.jeda.Message;
import ch.jeda.Size;
import ch.jeda.platform.ImageImp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a bitmap image. A bitmap image can be loaded from an image file or
 * be obtained by making a snapshot of a {@link Canvas}. Simple image
 * transformations are also supported.
 *
 * @since 1
 */
public final class Image {

    public static final String JEDA_LOGO_16x16 = ":ch/jeda/resources/logo-16x16.png";
    public static final String JEDA_LOGO_48x48 = ":ch/jeda/resources/logo-48x48.png";
    public static final String JEDA_LOGO_64x64 = ":ch/jeda/resources/logo-64x64.png";
    private static final Map<String, ImageImp> CACHE = new HashMap();
    private static final Map<String, ImageImp.Encoding> FORMAT_MAP = initFormatMap();
    private final ImageImp imp;

    /**
     * Constructs an image from a file. Loads the contents of the specified
     * image file. Currently, the image file formats JPEG, and PNG are
     * supported.
     * <p>
     * The file can either be located on the local computer, or in the project.
     * To read a file located in the project, put ':' in front of the file path.
     * For example, use the following code to load an image located in the
     * <tt>ch.jeda.samples</tt> package of the project:
     *
     * <pre><code>Image sample = new Image("src/ch/jeda/samples/sample.png");</code></pre>
     *
     * @param filePath path to the image file
     *
     * @since 1
     */
    public Image(String filePath) {
        this(loadImp(filePath));
    }

    /**
     * Creates a scaled copy of the image. Width and height are both scaled
     * proportionally.
     *
     * @param factor the scaling factor
     * @return scaled image
     *
     * @see #createScaledImage(int, int)
     * @see #createScaledImage(Size)
     *
     * @since 1
     */
    public Image createScaledImage(double factor) {
        return this.createScaledImage(this.getSize().scaled(factor));
    }

    /**
     * Creates a scaled copy of the image. Both width and height of the new
     * image can be specified. The aspect ratio may not be preserved.
     *
     * @param width the width of the new image
     * @param height the height of the new image
     * @return scaled image
     * @throws IllegalArgumentException if <tt>width</tt> or <tt>height</tt> are
     * smaller than 1
     *
     * @see #createScaledImage(double)
     * @see #createScaledImage(Size)
     *
     * @since 1
     */
    public Image createScaledImage(int width, int height) {
        return this.createScaledImage(new Size(width, height));
    }

    /**
     * Creates a scaled copy of the image.
     *
     * @param size
     * @throws NullPointerException if <tt>size</tt> is <tt>null</tt>
     * @return scaled image
     *
     * @see #createScaledImage(int, int)
     * @see #createScaledImage(double)
     */
    public Image createScaledImage(Size size) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        return new Image(this.imp.createScaledImage(size));
    }

    /**
     * Returns a rectangular part of the image as a new image.
     *
     * @param x the x coordinate of the top left corner of the part
     * @param y the y coordinate of the top left corner of the part
     * @param width the width of the part
     * @param height the height of the part
     * @return specified part of image
     * @throws IllegalArgumentException if <tt>width</tt> or <tt>height</tt> are
     * smaller than 1
     *
     * @since 1
     */
    public Image createSubImage(int x, int y, int width, int height) {
        return this.createSubImage(new Location(x, y), new Size(width, height));
    }

    /**
     * Returns a rectangular part of the image as a new image.
     *
     * @param location the top left corner of the part
     * @param size the size of the part
     * @return specified part of image
     * @throws NullPointerException if <tt>location</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>size</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public Image createSubImage(Location location, Size size) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        if (size == null) {
            throw new NullPointerException("size");
        }

        return new Image(this.imp.createSubImage(location, size));
    }

    /**
     * Returns the size of the image in pixels.
     *
     * @return size of image
     *
     * @see #getHeight()
     * @see #getWidth()
     *
     * @since 1
     */
    public Size getSize() {
        return this.imp.getSize();
    }

    /**
     * Returns the height of the image in pixels.
     *
     * @return height of image
     *
     * @see #getSize()
     * @see #getWidth()
     * @since 1
     */
    public int getHeight() {
        return this.imp.getSize().height;
    }

    /**
     * Returns the width of the image in pixels.
     *
     * @return width of image
     *
     * @see #getHeight()
     * @see #getSize()
     * @since 1
     */
    public int getWidth() {
        return this.imp.getSize().width;
    }

    /**
     * Replaces all pixels of one specific color with another color. This can be
     * useful for creating transparent images.
     *
     * @param oldColor color to be replaced
     * @param newColor color to replace oldColor
     *
     * @since 1
     */
    public Image replacePixels(Color oldColor, Color newColor) {
        return new Image(this.imp.replacePixels(oldColor, newColor));
    }

    /**
     * Saves the contents of the image to a file. Saving to a resource file
     * (i.e. a file path starting with ':') is not allowed. The file path must
     * end with a valid image file extension. Currently, the extension ".jpeg",
     * ".jpg", and ".png" are supported.
     *
     * @param filePath file to save to
     * @return <tt>true</tt> if file has been saved sucessfully
     * @throws NullPointerException if <tt>filePath</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public boolean save(String filePath) {
        if (filePath == null) {
            throw new NullPointerException("filePath");
        }

        int pos = filePath.indexOf('.');
        if (pos == -1) {
            return false;
        }

        String extension = filePath.substring(pos + 1).toLowerCase();
        if (!FORMAT_MAP.containsKey(extension)) {
            Log.warning(Message.IMAGE_FORMAT_ERROR, filePath, extension);
            return false;
        }

        java.io.File dir = new File(filePath).getParentFile();
        if (dir != null) {
            dir.mkdirs();
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            return this.imp.write(out, FORMAT_MAP.get(extension));
        }
        catch (IOException ex) {
            Log.warning(Message.IMAGE_WRITE_ERROR, filePath, ex.getMessage());
            return false;
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException ex) {
                    // ignore
                }
            }
        }
    }

    Image(ImageImp imp) {
        this.imp = imp;
    }

    ImageImp getImp() {
        return this.imp;
    }

    private static Map<String, ImageImp.Encoding> initFormatMap() {
        Map<String, ImageImp.Encoding> result = new HashMap();
        result.put(".jpeg", ImageImp.Encoding.JPEG);
        result.put(".jpg", ImageImp.Encoding.JPEG);
        result.put(".png", ImageImp.Encoding.PNG);
        return result;
    }

    private static ImageImp loadImp(String filePath) {
        if (!CACHE.containsKey(filePath)) {
            CACHE.put(filePath, Engine.getCurrentEngine().loadImageImp(filePath));
        }

        return CACHE.get(filePath);
    }
}
