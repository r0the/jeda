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
import ch.jeda.Size;
import ch.jeda.Transformation;
import ch.jeda.platform.CanvasImp;
import java.util.Stack;

/**
 * Represents a drawing surface. It provides methods to draw geometric
 * primitives and images.
 *
 * <p>
 * A canvas has some attributes that influence the drawing operations:
 * <ul>
 * <li> <b>color</b>: The color used to draw geometric primitives. Initially,
 * the color is black. The color can be changed with
 * {@link #setColor(ch.jeda.ui.Color)}.
 * <li> <b>line width</b>: the line width used to draw geometric shapes. The
 * line width can be changed with {@link #setLineWidth(double)}.
 * <li> <b>font size</b>: the size of the font used to render text. Initially,
 * the font size is 16. The font size can be changed with
 * {@link #setFontSize(int)}.
 * <li> <b>alpha</b>: The opaqueness of drawing operations. Initially, the alpha
 * value is set to 255 (fully opaque). The value can be changed with
 * {@link #setAlpha(int)}.
 * <lI> <b>transformation</b>: The canvas has an affine transformation that is
 * applied to all drawing operations. The default transformation is the
 * identity.
 * </ul>
 * <strong>Example:</strong>
 * <pre><code> Canvas canvas = new Canvas(100, 100);
 * canvas.setColor(Color.RED);
 * canvas.fillCircle(50, 50, 20);</code></pre>
 *
 * @since 1
 */
public class Canvas {

    private static final int DEFAULT_FONT_SIZE = 16;
    private static final Color DEFAULT_FOREGROUND = Color.BLACK;
    private final Stack<Transformation> transformationStack;
    private int alpha;
    private Color color;
    private int fontSize;
    private CanvasImp imp;
    private double lineWidth;
    private Transformation transformation;

    /**
     * Constructs a drawing surface. The drawing surface has the specified width
     * and height. A drawing surface constructed in this way is <b>invisible</b>
     * and can be used to draw images for later use. Use
     * {@link Window#Window(int, int, ch.jeda.ui.Window.Feature[])} to create a
     * visible drawing surface.
     *
     * @param width the width of the canvas in pixels
     * @param height the height of the canvas in pixels
     * @throws IllegalArgumentException if width or height are smaller than 1
     *
     * @since 1
     */
    public Canvas(final int width, final int height) {
        if (width < 1) {
            throw new IllegalArgumentException("width");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height");
        }

        this.transformationStack = new Stack<Transformation>();
        this.alpha = 255;
        this.color = DEFAULT_FOREGROUND;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.transformation = Transformation.createIdentity();
        this.imp = Engine.getContext().createCanvasImp(width, height);
    }

    /**
     * @deprecated Use {@link #Canvas(int, int)} instead.
     */
    @Deprecated
    public Canvas(Size size) {
        this(size.width, size.height);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void clear() {
        this.imp.clear();
    }

    /**
     * <b>Experimental</b>
     */
    public void copyFrom(final Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException("canvas");
        }

        this.imp.copyFrom(0, 0, canvas.imp);
    }

    /**
     * Draws a circle. The circle is drawn using the current color, line width,
     * and alpha value. Has no effect if the specified radius is not positive.
     *
     * @param x the x coordinate of the circle's centre
     * @param y the y coordinate of the circle's centre
     * @param radius the circle's radius
     *
     * @since 1
     */
    public void drawCircle(final int x, final int y, final int radius) {
        if (radius > 0) {
            this.imp.drawCircle(x, y, radius);
        }
    }

    /**
     * @deprecated Use {@link #drawCircle(ch.jeda.Location, int)} instead.
     */
    @Deprecated
    public void drawCircle(Location center, int radius) {
        if (center == null) {
            throw new NullPointerException("center");
        }

        if (radius > 0) {
            this.imp.drawCircle(center.x, center.y, radius);
        }
    }

    /**
     * Draws an image. The image is drawn using the current alpha value. The top
     * left corner of the image is positioned at the specified coordinates. Has
     * no effect if <tt>image</tt> is <tt>null</tt>.
     *
     * @param x the x coordinate of the image's top left corner
     * @param y the y coordinate of the image's top left corner
     * @param image the image to draw
     *
     * @since 1
     */
    public void drawImage(final int x, final int y, final Image image) {
        if (image != null) {
            this.imp.drawImage(x, y, image.getImp());
        }
    }

    /**
     * @deprecated Use {@link #drawImage(int, int, ch.jeda.ui.Image)} instead.
     */
    @Deprecated
    public void drawImage(Location topLeft, Image image) {
        if (topLeft == null) {
            throw new NullPointerException("topLeft");
        }

        if (image != null) {
            this.imp.drawImage(topLeft.x, topLeft.y, image.getImp());
        }
    }

    /**
     * Draws an image. The image is drawn using the current alpha value. The
     * image is aligned relative to the specified coordinates (<tt>x</tt>,
     * <tt>y</tt>). Has no effect if <tt>image</tt> is <tt>null</tt>.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param image the image to draw
     * @param alignment specifies how to align the image relative to
     * (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawImage(final int x, final int y, final Image image,
                          final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (image != null) {
            this.imp.drawImage(alignment.alignX(x, image.getWidth()),
                               alignment.alignY(y, image.getHeight()),
                               image.getImp());
        }
    }

    /**
     * @deprecated Use
     * {@link #drawImage(int, int, ch.jeda.ui.Image, ch.jeda.ui.Alignment)}
     * instead.
     */
    @Deprecated
    public void drawImage(Location anchor, Image image, Alignment alignment) {
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }

        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (image != null) {
            this.imp.drawImage(alignment.alignX(anchor.x, image.getWidth()),
                               alignment.alignY(anchor.y, image.getHeight()),
                               image.getImp());
        }
    }

    /**
     * Draws a straight line. The line is drawn from the coordinates
     * (<tt>x1</tt>, <tt>y1</tt>) to the coordinates (<tt>x2</tt>, <tt>y2</tt>)
     * with the current color, line width, and alpha value.
     *
     * @param x1 the x coordinate of the line's start point
     * @param y1 the y coordinate of the lines' start point
     * @param x2 the x coordinate of the line's end point
     * @param y2 the y coordinate of the line's end point
     *
     * @since 1
     */
    public void drawLine(final int x1, final int y1,
                         final int x2, final int y2) {
        this.imp.drawLine(x1, y1, x2, y2);
    }

    /**
     * @deprecated Use {@link #drawLine(int, int, int, int)} instead.
     */
    @Deprecated
    public void drawLine(Location from, Location to) {
        if (from == null) {
            throw new NullPointerException("from");
        }

        if (to == null) {
            throw new NullPointerException("to");
        }

        this.imp.drawLine(from.x, from.y, to.x, to.y);
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current color, line
     * width and alpha value. The top left corner of the rectangle is positioned
     * at the coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if
     * <tt>width</tt> or <tt>height</tt> are not positive.
     *
     * @param x the x coordinate of the rectangle's top left corner
     * @param y the y coordinate of the rectangle's top left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1
     */
    public void drawRectangle(final int x, final int y,
                              final int width, final int height) {
        if (width > 0 && height > 0) {
            this.imp.drawRectangle(x, y, width, height);
        }
    }

    /**
     * @deprecated Use {@link #drawRectangle(int, int, int, int)} instead.
     */
    @Deprecated
    public void drawRectangle(Location topLeft, Size size) {
        if (topLeft == null) {
            throw new NullPointerException("topLeft");
        }

        if (size == null) {
            throw new NullPointerException("size");
        }

        if (!size.isEmpty()) {
            this.imp.drawRectangle(topLeft.x, topLeft.y, size.width, size.height);
        }
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current color, line
     * width and alpha value. The rectangle is aligned relative to the specified
     * coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if <tt>width</tt> or
     * <tt>height</tt> are not positive.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param alignment specifies how to align the rectangle relative to
     * (<tt>x</tt>, <tt>y</tt>).
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawRectangle(final int x, final int y, final int width,
                              final int height, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            this.imp.drawRectangle(alignment.alignX(x, width),
                                   alignment.alignY(y, height),
                                   width, height);
        }
    }

    /**
     * @deprecated Use
     * {@link #drawRectangle(int, int, int, int, ch.jeda.ui.Alignment)} instead.
     */
    @Deprecated
    public void drawRectangle(Location anchor, Size size, Alignment alignment) {
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }

        if (size == null) {
            throw new NullPointerException("size");
        }

        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (!size.isEmpty()) {
            this.imp.drawRectangle(alignment.alignX(anchor.x, size.width),
                                   alignment.alignY(anchor.y, size.height),
                                   size.width, size.height);
        }
    }

    /**
     * @deprecated Use {@link #drawText(int, int, java.lang.String)} instead.
     */
    @Deprecated
    public void drawString(int x, int y, String text) {
        this.drawText(new Location(x, y), text, Alignment.TOP_LEFT);
    }

    /**
     * @deprecated Use {@link #drawText(ch.jeda.Location, java.lang.String)}
     * instead.
     */
    @Deprecated
    public void drawString(Location topLeft, String text) {
        this.drawText(topLeft, text, Alignment.TOP_LEFT);
    }

    /**
     * @deprecated Use
     * {@link #drawText(int, int, java.lang.String, ch.jeda.ui.Alignment)}
     * instead.
     */
    @Deprecated
    public final void drawString(int x, int y, String text, Alignment alignment) {
        this.drawText(new Location(x, y), text, alignment);
    }

    /**
     * @deprecated Use
     * {@link #drawText(ch.jeda.Location, java.lang.String, ch.jeda.ui.Alignment)}
     * instead.
     */
    @Deprecated
    public void drawString(Location anchor, String text, Alignment alignment) {
        this.drawText(anchor, text, alignment);
    }

    /**
     * Draws a text. The text is drawn using the current color, alpha value and
     * font size. The top left corner of the text is positioned at the
     * coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if <tt>text</tt>
     * is <tt>null</tt> or empty.
     *
     * @param x the x coordinate of the top left corner
     * @param y the y coordinate of the top left corner
     * @param text the text to draw
     *
     * @since 1
     */
    public void drawText(final int x, final int y, final String text) {
        if (text != null && !text.isEmpty()) {
            this.imp.drawText(x, y, text);
        }
    }

    /**
     * @deprecated Use {@link #drawText(int, int, java.lang.String)} instead.
     */
    @Deprecated
    public void drawText(Location topLeft, String text) {
        if (topLeft == null) {
            throw new NullPointerException("topLeft");
        }

        if (text != null && !text.isEmpty()) {
            this.imp.drawText(topLeft.x, topLeft.y, text);
        }
    }

    /**
     * Draws a text. The text is drawn using the current color, alpha value and
     * font size. The text is aligned relative to the specified coordinates
     * (<tt>x</tt>, <tt>y</tt>). Has no effect if <tt>text</tt>
     * is <tt>null</tt> or empty.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param text the text to draw
     * @param alignment specifies how to align the text relative to (<tt>x</tt>,
     * <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public final void drawText(final int x, final int y, final String text,
                               final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (text != null && !text.isEmpty()) {
            this.imp.drawText(alignment.alignX(x, this.imp.textWidth(text)),
                              alignment.alignY(y, this.imp.textHeight(text)),
                              text);
        }
    }

    /**
     * @deprecated Use
     * {@link #drawText(int, int, java.lang.String, ch.jeda.ui.Alignment)}
     * instead.
     */
    @Deprecated
    public void drawText(Location anchor, String text, Alignment alignment) {
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }

        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (text != null && !text.isEmpty()) {
            this.imp.drawText(
                    alignment.alignX(anchor.x, this.imp.textWidth(text)),
                    alignment.alignY(anchor.y, this.imp.textHeight(text)),
                    text);
        }
    }

    /**
     * Draws a triangle. The triangle is specified by the three corners
     * (<tt>x1</tt>, <tt>y1</tt>), (<tt>x2</tt>, <tt>y2</tt>), and (<tt>x3</tt>,
     * <tt>y3</tt>). The triangle is drawn using the current color, line width,
     * and alpha value.
     *
     * @param x1 x coordinate of the first corner
     * @param y1 y coordinate of the first corner
     * @param x2 x coordinate of the second corner
     * @param y2 y coordinate of the second corner
     * @param x3 x coordinate of the third corner
     * @param y3 y coordinate of the third corner
     *
     * @since 1
     */
    public void drawTriangle(final int x1, final int y1, final int x2,
                             final int y2, final int x3, final int y3) {
        this.imp.drawPolygon(new int[]{x1, y1, x2, y2, x3, y3});
    }

    /**
     * Fills the entire canvas. The canvas is filled using the current color and
     * alpha value.
     *
     * @since 1
     */
    public void fill() {
        this.imp.fill();
    }

    /**
     * Draws a filled a circle. The circle is filled using the current color and
     * alpha value. Has no effect if the specified radius is not positive.
     *
     * @param x the x coordinate of the circle's centre
     * @param y the y coordinate of the circle's centre
     * @param radius the circle's radius
     *
     * @since 1
     */
    public void fillCircle(final int x, final int y, final int radius) {
        if (radius > 0) {
            this.imp.fillCircle(x, y, radius);
        }
    }

    /**
     * @deprecated Use {@link #fillCircle(int, int, int)} instead.
     */
    @Deprecated
    public void fillCircle(Location center, int radius) {
        if (center == null) {
            throw new NullPointerException("center");
        }

        if (radius > 0) {
            this.imp.fillCircle(center.x, center.y, radius);
        }
    }

    /**
     * Draws a filled rectangle. The rectangle is filled using the current color
     * and alpha value. The top left corner of the rectangle is positioned at
     * the coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if
     * <tt>width</tt> or <tt>height</tt> are not positive.
     *
     * @param x the x coordinate of the rectangle's top left corner
     * @param y the y coordinate of the rectangle's top left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1
     */
    public void fillRectangle(final int x, final int y,
                              final int width, final int height) {
        if (width > 0 && height > 0) {
            this.imp.fillRectangle(x, y, width, height);
        }
    }

    /**
     * @deprecated Use {@link #fillRectangle(int, int, int, int)} instead.
     */
    @Deprecated
    public void fillRectangle(Location topLeft, Size size) {
        if (topLeft == null) {
            throw new NullPointerException("topLeft");
        }

        if (size == null) {
            throw new NullPointerException("size");
        }

        if (!size.isEmpty()) {
            this.imp.fillRectangle(topLeft.x, topLeft.y, size.width, size.height);
        }
    }

    /**
     * Draws a filled rectangle. The rectangle is filled using the current color
     * and alpha value. The rectangle is aligned relative to the specified
     * coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if
     * <tt>width</tt> or
     * <tt>height</tt> are not positive.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param alignment specifies how to align the rectangle relative to
     * (<tt>x</tt>, <tt>y</tt>).
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void fillRectangle(final int x, final int y, final int width,
                              final int height, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            this.imp.fillRectangle(alignment.alignX(x, width),
                                   alignment.alignY(y, height),
                                   width, height);
        }
    }

    /**
     * @deprecated Use
     * {@link #fillRectangle(int, int, int, int, ch.jeda.ui.Alignment)} instead.
     */
    @Deprecated
    public void fillRectangle(Location anchor, Size size, Alignment alignment) {
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }

        if (size == null) {
            throw new NullPointerException("size");
        }

        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (!size.isEmpty()) {
            this.imp.fillRectangle(alignment.alignX(anchor.x, size.width),
                                   alignment.alignY(anchor.y, size.height),
                                   size.width, size.height);
        }
    }

    /**
     * Draws a filled triangle. The triangle is specified by the three corners
     * (<tt>x1</tt>, <tt>y1</tt>), (<tt>x2</tt>, <tt>y2</tt>), and (<tt>x3</tt>,
     * <tt>y3</tt>). The triangle is filled using the current color and alpha
     * value.
     *
     * @param x1 x coordinate of the first corner
     * @param y1 y coordinate of the first corner
     * @param x2 x coordinate of the second corner
     * @param y2 y coordinate of the second corner
     * @param x3 x coordinate of the third corner
     * @param y3 y coordinate of the third corner
     *
     * @since 1
     */
    public void fillTriangle(final int x1, final int y1, final int x2,
                             final int y2, final int x3, final int y3) {
        this.imp.fillPolygon(new int[]{x1, y1, x2, y2, x3, y3});
    }

    /**
     * <b>Experimental</b>
     */
    public void floodFill(int x, int y,
                          final Color oldColor, final Color newColor) {
        if (oldColor == null) {
            throw new NullPointerException("oldColor");
        }

        if (newColor == null) {
            throw new NullPointerException("newColor");
        }

        Stack<Integer> stackX = new Stack<Integer>();
        Stack<Integer> stackY = new Stack<Integer>();
        stackX.push(x);
        stackY.push(y);
        while (!stackX.isEmpty()) {
            x = stackX.pop();
            y = stackY.pop();
            if (this.getPixelAt(x, y).equals(oldColor)) {
                this.setPixelAt(x, y, newColor);
                stackX.push(x);
                stackY.push(y + 1);
                stackX.push(x);
                stackY.push(y - 1);
                stackX.push(x + 1);
                stackY.push(y);
                stackX.push(x - 1);
                stackY.push(y);
            }
        }
    }

    /**
     * Returns the current alpha value.
     *
     * @return current alpha value
     *
     * @see #setAlpha(int)
     * @since 1
     */
    public int getAlpha() {
        return this.alpha;
    }

    /**
     * Returns the current color.
     *
     * @return current drawing color
     *
     * @see #setColor(ch.jeda.ui.Color)
     * @since 1
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Returns the current font size.
     *
     * @return current font size
     *
     * @see #setFontSize(int)
     * @since 1
     */
    public int getFontSize() {
        return this.fontSize;
    }

    /**
     * Returns the height of the canvas in pixels.
     *
     * @return height of canvas
     *
     * @see #getWidth()
     * @since 1
     */
    public int getHeight() {
        return this.imp.getHeight();
    }

    /**
     * Returns the current line width in pixels.
     *
     * @return current line width
     */
    public double getLineWidth() {
        return this.imp.getLineWidth();
    }

    /**
     * Returns the color of a pixel. Returns the color of the pixel at the
     * coordinates (<tt>x</tt>, <tt>y</tt>). Returns
     * {@link ch.jeda.ui.Color#NONE} if the coordinates do not reference a pixel
     * inside the canvas.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the color of the pixel at (<tt>x</tt>, <tt>y</tt>)
     *
     * @see #setPixelAt(int, int, ch.jeda.ui.Color)
     * @since 1
     */
    public Color getPixelAt(final int x, final int y) {
        if (this.contains(x, y)) {
            return this.imp.getPixelAt(x, y);
        }
        else {
            return Color.NONE;
        }
    }

    /**
     * Returns the color of a pixel. Returns the color of the pixel at the
     * specified location. Returns {@link ch.jeda.ui.Color#NONE} if the
     * coordinates do not reference a pixel inside the canvas.
     *
     * @param location the location of the pixel
     * @return the color of the pixel at the specified location
     * @throws NullPointerException if <tt>location</tt> is <tt>null</tt>
     *
     * @see #setPixelAt(ch.jeda.Location, ch.jeda.ui.Color)
     * @since 1
     */
    public Color getPixelAt(Location location) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        return this.getPixelAt(location.x, location.y);
    }

    /**
     * @deprecated Use {@link #getWidth()} and {@link #getHeight()} instead.
     */
    @Deprecated
    public Size getSize() {
        return new Size(this.imp.getWidth(), this.imp.getHeight());
    }

    /**
     * Returns the current transformation for the canvas.
     *
     * @return the current transformation
     *
     * @see #popTransformation()
     * @see #pushTransformation(ch.jeda.Transformation)
     * @since 1
     */
    public Transformation getTransformation() {
        return this.transformation;
    }

    /**
     * Returns the width of the canvas in pixels.
     *
     * @return width of canvas
     *
     * @see #getHeight()
     * @since 1
     */
    public int getWidth() {
        return this.imp.getWidth();
    }

    /**
     * @deprecated Use {@link #getTransformation()} to retrieve and store the
     * current transformation.
     */
    @Deprecated
    public void popTransformation() {
        if (this.transformationStack.isEmpty()) {
            throw new IllegalStateException("Empty transformation stack.");
        }

        this.transformation = this.transformationStack.pop();
        this.imp.setTransformation(this.transformation);
    }

    /**
     * @deprecated Use {@link #setTransformation(ch.jeda.Transformation)}
     * instead.
     */
    @Deprecated
    public void pushTransformation(Transformation transformation) {
        if (transformation == null) {
            throw new NullPointerException("transformation");
        }

        this.transformationStack.push(this.transformation);
        this.transformation = transformation;
        this.imp.setTransformation(this.transformation);
    }

    /**
     * Sets the alpha value. The alpha value describes the opaqueness (the
     * opposite of transparency) of drawing operations. The value set by this
     * method is applied to all subsequent <tt>draw...</tt> and <tt>fill...</tt>
     * operations. The alpha value must be in the range from 0 (fully
     * transparent) to 255 (fully opaque).
     *
     * @param alpha the new alpha value
     * @throws IllegalArgumentException if alpha value is outside the valid
     * range
     *
     * @see #getAlpha()
     * @since 1
     */
    public void setAlpha(final int alpha) {
        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        if (this.alpha != alpha) {
            this.alpha = alpha;
            this.imp.setAlpha(this.alpha);
        }
    }

    /**
     * Sets the drawing color. The value set by this method is applied to all
     * subsequent <tt>draw...</tt> and <tt>fill...</tt> operations.
     *
     * @param color new drawing color.
     * @throws NullPointerException if <tt>color</tt> is <tt>null</tt>
     *
     * @see #getColor()
     * @since 1
     */
    public void setColor(final Color color) {
        if (color == null) {
            throw new NullPointerException("color");
        }

        if (!this.color.equals(color)) {
            this.color = color;
            this.imp.setColor(this.color);
        }
    }

    /**
     * Sets the font size. The font size set by this method is applied to all
     * subsequent <tt>drawText(...)</tt> operations.
     *
     * @param size the new font size
     * @throws IllegalArgumentException if <tt>size</tt> is not positive
     *
     * @see #getFontSize()
     *
     * @since 1
     */
    public void setFontSize(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size");
        }

        if (this.fontSize != size) {
            this.fontSize = size;
            this.imp.setFontSize(this.fontSize);
        }
    }

    /**
     * Sets the line width. The line width set by this method is applied to all
     * subsequent <tt>draw...</tt> operations.
     *
     * @param lineWidth the new line width
     *
     * @throws IllegalArgumentException if <tt>lineWidth</tt> is not positive
     *
     * @since 1
     */
    public void setLineWidth(final double lineWidth) {
        if (lineWidth <= 0.0) {
            throw new IllegalArgumentException("lineWidth");
        }

        this.lineWidth = lineWidth;
        this.imp.setLineWidth(lineWidth);
    }

    /**
     * Sets the color of a pixel. Sets the color of the pixel at the coordinates
     * (<tt>x</tt>, <tt>y</tt>). Has no effect if the coordinates do not
     * reference a pixel inside the canvas.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @param color new color of the pixel
     * @throws NullPointerException if <tt>color</tt> is <tt>null</tt>
     *
     * @see #getPixelAt(int, int)
     * @since 1
     */
    public void setPixelAt(final int x, final int y, final Color color) {
        if (color == null) {
            throw new NullPointerException("color");
        }

        if (this.getSize().contains(x, y)) {
            this.imp.setPixelAt(x, y, color);
        }
    }

    /**
     * Sets the color of a pixel. Sets the color of the pixel at the specified
     * location. Has no effect if the coordinates do not reference a pixel
     * inside the canvas.
     *
     * @param location the location of the pixel
     * @param color the new color of the pixel
     * @throws NullPointerException if <tt>location</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>color</tt> is <tt>null</tt>
     *
     * @see #getPixelAt(ch.jeda.Location)
     * @since 1
     */
    public void setPixelAt(Location location, Color color) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        if (color == null) {
            throw new NullPointerException("color");
        }

        if (this.getSize().contains(location)) {
            this.imp.setPixelAt(location.x, location.y, color);
        }
    }

    /**
     * Sets the affine transformation. The transformation set by this method is
     * applied to all subsequent drawing operations.
     *
     * @param transformation the new transformation
     * @throws NullPointerException if <tt>transformation</tt> is <tt>null</tt>
     *
     * @see #getTransformation()
     * @since 1
     */
    public void setTransformation(final Transformation transformation) {
        if (transformation == null) {
            throw new NullPointerException("transformation");
        }

        this.transformation = transformation;
        this.imp.setTransformation(this.transformation);
    }

    /**
     * Takes a snapshot of the canvas. Creates an image that contains a copy of
     * the contents of the canvas.
     *
     * @return image containing a copy of the canvas
     *
     * @since 1
     */
    public Image takeSnapshot() {
        return new Image(this.imp.takeSnapshot());
    }

    /**
     * Returns the height of a text in pixels. Returns the height in pixels of
     * the specified text given the current font size. Returns zero if
     * <tt>text</tt> is <tt>null</tt> or empty.
     *
     * @param text
     * @return height of text in pixels
     */
    public int textHeight(final String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        else {
            return this.imp.textHeight(text);
        }
    }

    /**
     * @deprecated Use {@link #textWidth(java.lang.String)} and
     * {@link #textHeight(java.lang.String)} instead.
     */
    @Deprecated
    public Size textSize(String text) {
        if (text == null || text.isEmpty()) {
            return Size.EMPTY;
        }
        else {
            return new Size(this.imp.textWidth(text),
                            this.imp.textHeight(text));
        }
    }

    /**
     * Returns the width of a text in pixels. Returns the width in pixels of the
     * specified text given the current font size. Returns zero if <tt>text</tt>
     * is <tt>null</tt> or empty.
     *
     * @param text
     * @return width of text in pixels
     */
    public int textWidth(final String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        else {
            return this.imp.textWidth(text);
        }
    }

    Canvas() {
        this.transformationStack = new Stack<Transformation>();
        this.alpha = 255;
        this.color = DEFAULT_FOREGROUND;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.transformation = Transformation.createIdentity();
    }

    void setImp(final CanvasImp imp) {
        this.imp = imp;
        this.imp.setAlpha(this.alpha);
        this.imp.setColor(this.color);
        this.imp.setFontSize(this.fontSize);
        this.imp.setLineWidth(this.lineWidth);
        this.imp.setTransformation(this.transformation);
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < this.getWidth() && 0 <= y && y < this.getHeight();
    }
}
