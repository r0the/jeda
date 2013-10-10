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
import ch.jeda.Transformation;
import ch.jeda.platform.CanvasImp;
import java.util.Stack;

/**
 * Represents a drawing surface. It provides methods to draw geometric primitives and images.
 *
 * <p>
 * A canvas has some attributes that influence the drawing operations:
 * <ul>
 * <li> <b>color</b>: The color used to draw geometric primitives. Initially, the color is black. The color can be
 * changed with {@link #setColor(ch.jeda.ui.Color)}.
 * <li> <b>line width</b>: the line width used to draw geometric shapes. The line width can be changed with
 * {@link #setLineWidth(float)}.
 * <li> <b>font size</b>: the size of the font used to render text. Initially, the font size is 16. The font size can be
 * changed with {@link #setFontSize(int)}.
 * <lI> <b>transformation</b>: The canvas has an affine transformation that is applied to all drawing operations. The
 * default transformation is the identity.
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
    private Color color;
    private int fontSize;
    private CanvasImp imp;
    private float lineWidth;
    private Transformation transformation;

    /**
     * Constructs a drawing surface. The drawing surface has the specified width and height. A drawing surface
     * constructed in this way is <b>invisible</b>
     * and can be used to draw images for later use. Use {@link Window#Window(int, int, ch.jeda.ui.WindowFeature[])} to
     * create a visible drawing surface.
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

        this.color = DEFAULT_FOREGROUND;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.transformation = new Transformation();
        this.setImp(Engine.getContext().createCanvasImp(width, height));
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
     * Draws a circle. The circle is drawn using the current color, line width, and transformation. Has no effect if the
     * specified radius is not positive.
     *
     * @param x the x coordinate of the circle's centre
     * @param y the y coordinate of the circle's centre
     * @param radius the circle's radius
     *
     * @since 1
     */
    public void drawCircle(final float x, final float y, final float radius) {
        if (radius > 0) {
            this.imp.drawCircle(x, y, radius);
        }
    }

    /**
     * Draws an image. The image is drawn using the current transformation. The top left corner of the image is
     * positioned at the specified coordinates. Has no effect if <tt>image</tt> is <tt>null</tt>.
     *
     * @param x the x coordinate of the image's top left corner
     * @param y the y coordinate of the image's top left corner
     * @param image the image to draw
     *
     * @since 1
     */
    public void drawImage(final float x, final float y, final Image image) {
        if (image != null) {
            this.imp.drawImage(x, y, image.getImp(), 255);
        }
    }

    /**
     * Draws an image. The image is drawn using the current transformation. The top left corner of the image is
     * positioned at the specified coordinates. The image is drawn with a translucency effect specified by the alpha
     * value. Specify an alpha value of 255 for a completely opaque image, and alpha value of 0 for a completely
     * transparent image. Has no effect if
     * <tt>image</tt> is <tt>null</tt>.
     *
     * @param x the x coordinate of the image's top left corner
     * @param y the y coordinate of the image's top left corner
     * @param image the image to draw
     * @param alpha the alpha value
     *
     * @since 1
     */
    public void drawImage(final float x, final float y, final Image image, final int alpha) {
        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        if (image != null && alpha > 0) {
            this.imp.drawImage(x, y, image.getImp(), alpha);
        }
    }

    /**
     * Draws an image. The image is drawn using the current transformation. The image is aligned relative to the
     * specified coordinates (<tt>x</tt>,
     * <tt>y</tt>). Has no effect if <tt>image</tt> is <tt>null</tt>.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param image the image to draw
     * @param alignment specifies how to align the image relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawImage(final float x, final float y, final Image image, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (image != null) {
            this.imp.drawImage(alignment.alignX(x, image.getWidth()), alignment.alignY(y, image.getHeight()), image.getImp(), 255);
        }
    }

    /**
     * Draws an image. The image is drawn using the current transformation. The image is aligned relative to the
     * specified coordinates (<tt>x</tt>,
     * <tt>y</tt>). The image is drawn with a translucency effect specified by the alpha value. Specify an alpha value
     * of 255 for a completely opaque image, and alpha value of 0 for a completely transparent image. Has no effect if
     * <tt>image</tt> is <tt>null</tt>.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param image the image to draw
     * @param alpha the alpha value
     * @param alignment specifies how to align the image relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawImage(final float x, final float y, final Image image, final int alpha, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        if (image != null && alpha > 0) {
            this.imp.drawImage(alignment.alignX(x, image.getWidth()), alignment.alignY(y, image.getHeight()), image.getImp(), alpha);
        }
    }

    /**
     * Draws a straight line. The line is drawn from the coordinates (<tt>x1</tt>, <tt>y1</tt>) to the coordinates
     * (<tt>x2</tt>, <tt>y2</tt>) with the current color, line width, and transformation.
     *
     * @param x1 the x coordinate of the line's start point
     * @param y1 the y coordinate of the lines' start point
     * @param x2 the x coordinate of the line's end point
     * @param y2 the y coordinate of the line's end point
     *
     * @since 1
     */
    public void drawLine(final float x1, final float y1, final float x2, final float y2) {
        this.imp.drawLine(x1, y1, x2, y2);
    }

    /**
     * Draws a polygon. The polygon is drawn using the current color, line width, and transformation. The polygon is
     * defined by a sequence of coordinate pairs specifiying the corners of the polygon. For example, the code
     * <pre><code>drawPolygon(x1, y1, x2, y2, x3, y3);</code></pre> will draw a triangle with the corners (x1, y2), (x2,
     * y2), and (x3, y3).
     *
     * @param points the points of the polygon as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 6 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     */
    public void drawPolygon(final float... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.imp.drawPolygon(points);
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current color, line width, and transformation. The top left
     * corner of the rectangle is positioned at the coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if
     * <tt>width</tt> or <tt>height</tt> are not positive.
     *
     * @param x the x coordinate of the rectangle's top left corner
     * @param y the y coordinate of the rectangle's top left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1
     */
    public void drawRectangle(final float x, final float y, final float width, final float height) {
        if (width > 0 && height > 0) {
            this.imp.drawRectangle(x, y, width, height);
        }
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current color, line width, and transformation. The rectangle
     * is aligned relative to the specified coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if
     * <tt>width</tt> or <tt>height</tt> are not positive.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param alignment specifies how to align the rectangle relative to (<tt>x</tt>, <tt>y</tt>).
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawRectangle(final float x, final float y, final float width, final float height, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            this.imp.drawRectangle(alignment.alignX(x, width), alignment.alignY(y, height), width, height);
        }
    }

    /**
     * Draws a text. The text is drawn using the current color, transformation, and font size. The top left corner of
     * the text is positioned at the coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if <tt>text</tt>
     * is <tt>null</tt> or empty.
     *
     * @param x the x coordinate of the top left corner
     * @param y the y coordinate of the top left corner
     * @param text the text to draw
     *
     * @since 1
     */
    public void drawText(final float x, final float y, final String text) {
        if (text != null && !text.isEmpty()) {
            this.imp.drawText(x, y, text);
        }
    }

    /**
     * Draws a text. The text is drawn using the current color, transformation, and font size. The text is aligned
     * relative to the specified coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if <tt>text</tt> is <tt>null</tt>
     * or empty.
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
    public final void drawText(final float x, final float y, final String text, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (text != null && !text.isEmpty()) {
            this.imp.drawText(alignment.alignX(x, this.imp.textWidth(text)), alignment.alignY(y, this.imp.textHeight(text)), text);
        }
    }

    /**
     * Draws a triangle. The triangle is specified by the three corners (<tt>x1</tt>, <tt>y1</tt>), (<tt>x2</tt>,
     * <tt>y2</tt>), and (<tt>x3</tt>,
     * <tt>y3</tt>). The triangle is drawn using the current color, line width, and transformation.
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
    public void drawTriangle(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        this.imp.drawPolygon(new float[]{x1, y1, x2, y2, x3, y3});
    }

    /**
     * Fills the entire canvas. The canvas is filled using the current color.
     *
     * @since 1
     */
    public void fill() {
        this.imp.fill();
    }

    /**
     * Draws a filled a circle. The circle is drawn using the current color and transformation. Has no effect if the
     * specified radius is not positive.
     *
     * @param x the x coordinate of the circle's centre
     * @param y the y coordinate of the circle's centre
     * @param radius the circle's radius
     *
     * @since 1
     */
    public void fillCircle(final float x, final float y, final float radius) {
        if (radius > 0) {
            this.imp.fillCircle(x, y, radius);
        }
    }

    /**
     * Draws a filled polygon. The polygon is drawn using the current color, line width, and transformation. The polygon
     * is defined by a sequence of coordinate pairs specifiying the corners of the polygon. For example, the code
     * <pre><code>fillPolygon(x1, y1, x2, y2, x3, y3);</code></pre> will draw a triangle with the corners (x1, y2), (x2,
     * y2), and (x3, y3).
     *
     * @param points the points of the polygon as sequence of coordinate pairs
     * @throws IllegalArgumentException if less than 6 arguments are passed
     * @throws IllegalArgumentException if and odd number of arguments are passed
     */
    public void fillPolygon(final float... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.imp.fillPolygon(points);
    }

    /**
     * Draws a filled rectangle. The rectangle is drawn using the current color and transformation. The top left corner
     * of the rectangle is positioned at the coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if
     * <tt>width</tt> or <tt>height</tt> are not positive.
     *
     * @param x the x coordinate of the rectangle's top left corner
     * @param y the y coordinate of the rectangle's top left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1
     */
    public void fillRectangle(final float x, final float y, final float width, final float height) {
        if (width > 0 && height > 0) {
            this.imp.fillRectangle(x, y, width, height);
        }
    }

    /**
     * Draws a filled rectangle. The rectangle is drawn using the current color and transformation. The rectangle is
     * aligned relative to the specified coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if
     * <tt>width</tt> or
     * <tt>height</tt> are not positive.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param alignment specifies how to align the rectangle relative to (<tt>x</tt>, <tt>y</tt>).
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void fillRectangle(final float x, final float y, final float width, final float height, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            this.imp.fillRectangle(alignment.alignX(x, width), alignment.alignY(y, height), width, height);
        }
    }

    /**
     * Draws a filled triangle. The triangle is specified by the three corners (<tt>x1</tt>, <tt>y1</tt>), (<tt>x2</tt>,
     * <tt>y2</tt>), and (<tt>x3</tt>,
     * <tt>y3</tt>). The triangle is drawn using the current color and transformation.
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
    public void fillTriangle(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        this.imp.fillPolygon(new float[]{x1, y1, x2, y2, x3, y3});
    }

    /**
     * <b>Experimental</b>
     */
    public void floodFill(int x, int y, final Color oldColor, final Color newColor) {
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
    public float getLineWidth() {
        return this.imp.getLineWidth();
    }

    /**
     * Returns the color of a pixel. Returns the color of the pixel at the coordinates (<tt>x</tt>, <tt>y</tt>). Returns
     * {@link ch.jeda.ui.Color#NONE} if the coordinates do not reference a pixel inside the canvas.
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
     * Returns the current transformation for the canvas.
     *
     * @return the current transformation
     *
     * @see #setTransformation(ch.jeda.Transformation)
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
     * Sets the drawing color. The value set by this method is applied to all subsequent <tt>draw...</tt> and
     * <tt>fill...</tt> operations.
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
     * Sets the font size. The font size set by this method is applied to all subsequent <tt>drawText(...)</tt>
     * operations.
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
     * Sets the line width. The line width set by this method is applied to all subsequent <tt>draw...</tt> operations.
     * Set 0 for drawing hairlines independent of the transformation.
     *
     * @param lineWidth the new line width
     *
     * @throws IllegalArgumentException if <tt>lineWidth</tt> is negative
     *
     * @since 1
     */
    public void setLineWidth(final float lineWidth) {
        if (lineWidth < 0f) {
            throw new IllegalArgumentException("lineWidth");
        }

        this.lineWidth = lineWidth;
        this.imp.setLineWidth(lineWidth);
    }

    /**
     * Sets the color of a pixel. Sets the color of the pixel at the coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect
     * if the coordinates do not reference a pixel inside the canvas.
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

        if (this.contains(x, y)) {
            this.imp.setPixelAt(x, y, color);
        }
    }

    /**
     * Sets the affine transformation. The transformation set by this method is applied to all subsequent drawing
     * operations.
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
     * Takes a snapshot of the canvas. Creates an image that contains a copy of the contents of the canvas.
     *
     * @return image containing a copy of the canvas
     *
     * @since 1
     */
    public Image takeSnapshot() {
        return new Image(this.imp.takeSnapshot());
    }

    /**
     * Returns the height of a text in pixels. Returns the height in pixels of the specified text given the current font
     * size. Returns zero if
     * <tt>text</tt> is <tt>null</tt> or empty.
     *
     * @param text
     * @return height of text in pixels
     *
     * @since 1
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
     * Returns the width of a text in pixels. Returns the width in pixels of the specified text given the current font
     * size. Returns zero if <tt>text</tt>
     * is <tt>null</tt> or empty.
     *
     * @param text
     * @return width of text in pixels
     *
     * @since 1
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
        this.color = DEFAULT_FOREGROUND;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.transformation = new Transformation();
    }

    final void setImp(final CanvasImp imp) {
        this.imp = imp;
        this.imp.setColor(this.color);
        this.imp.setFontSize(this.fontSize);
        this.imp.setLineWidth(this.lineWidth);
        this.imp.setTransformation(this.transformation);
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < this.getWidth() && 0 <= y && y < this.getHeight();
    }
}
