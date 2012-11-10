/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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
import ch.jeda.Log;
import ch.jeda.Size;
import ch.jeda.platform.ImageImp;

/**
 * This class represents an image. An image can be loaded from an image file or
 * be obtained by making a snapshot of a {@link Canvas}.
 */
public final class Image {

    public static final String JEDA_LOGO_16x16 = ":ch/jeda/resources/logo-16x16.png";
    public static final String JEDA_LOGO_48x48 = ":ch/jeda/resources/logo-48x48.png";
    public static final String JEDA_LOGO_64x64 = ":ch/jeda/resources/logo-64x64.png";
    private final ImageImp imp;

    /**
     * Reads an image file and creates a new image from the contents of the
     * file.
     *
     * To read a resource file, put ':' in front of the file path.
     *
     * @param filePath path to the image file
     */
    public Image(String filePath) {
        this(Engine.getCurrentEngine().loadImageImp(filePath));
    }

    Image(ImageImp imp) {
        this.imp = imp;
    }

    /**
     * Create a scaled copy of this image. Width and height are both scaled
     * proportionally.
     *
     * @param factor scaling factor
     * @return scaled image
     * @see #createScaledImage(int, int)
     */
    public Image createScaledImage(double factor) {
        return this.createScaledImage(this.getSize().scaled(factor));
    }

    /**
     * Creates a scaled copy of this image. A both width and height of the new
     * image can be specified, the aspect ratio may not be preserved.
     *
     * @param width width of new image
     * @param height height of new image
     * @return scaled image
     * @see #createScaledImage(double)
     */
    public Image createScaledImage(int width, int height) {
        return this.createScaledImage(new Size(width, height));
    }

    public Image createScaledImage(Size size) {
        return new Image(this.imp.createScaledImage(size));
    }

    public Size getSize() {
        return this.imp.getSize();
    }

    /**
     * Returns the height of the image in pixels.
     * 
     * @return height of image in pixels
     * 
     * @see #getWidth()
     * @since 1.0
     */
    public int getHeight() {
        return this.imp.getSize().height;
    }

    /**
     * Returns the width of the image in pixels.
     *
     * @return width of image in pixels
     *
     * @see #getHeight()
     * @since 1.0
     */
    public int getWidth() {
        return this.imp.getSize().width;
    }

    /**
     * Replaces all pixels of one specific color with another color. This can
     * be useful for creating transparent images.
     *
     * @param oldColor color to be replaced
     * @param newColor color to replace oldColor
     */
    public Image replacePixels(Color oldColor, Color newColor) {
        return new Image(this.imp.replacePixels(oldColor, newColor));
    }

    /**
     * Saves the contents of this image to a file. Saving to a resource file
     * (i.e. a file paht starting with ':') is not allowed. The file path
     * must end with ".png".
     * 
     * @param filePath file to save to
     * @return <code>true</code> if file has been saved sucessfully
     */
    public boolean save(String filePath) {
        if (!filePath.endsWith(".png")) {
            Log.warning("jeda.gui.image.format.error", filePath, "png");
            return false;
        }

        return this.imp.save(filePath);
    }

    ImageImp getImp() {
        return this.imp;
    }
}
