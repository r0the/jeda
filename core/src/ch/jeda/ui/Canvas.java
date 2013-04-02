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
import ch.jeda.platform.CanvasImp;
import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.Transformation;
import java.util.ArrayList;
import java.util.List;
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
    public Canvas(int width, int height) {
        this(new Size(width, height));
    }

    /**
     * Constructs a drawing surface. The drawing surface has the specified size.
     * A drawing surface constructed in this way is <b>invisible</b> and can be
     * used to draw images for later use. Use
     * {@link Window#Window(ch.jeda.Size, ch.jeda.ui.Window.Feature[])} to
     * create a visible drawing surface.
     *
     * @param size the size of the canvas in pixels
     * @throws NullPointerException if <tt>size</tt> is <tt>null</tt>
     * @throws IllegalArgumentException if size is empty
     *
     * @since 1
     */
    public Canvas(Size size) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        if (size.isEmpty()) {
            throw new IllegalArgumentException("size");
        }

        this.transformationStack = new Stack();
        this.alpha = 255;
        this.color = DEFAULT_FOREGROUND;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.transformation = Transformation.IDENTITY;
        this.imp = Engine.getCurrentEngine().createCanvasImp(size);
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
    public void copyFrom(Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException("canvas");
        }

        this.imp.copyFrom(Location.ORIGIN, canvas.imp);
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
    public void drawCircle(int x, int y, int radius) {
        if (radius > 0) {
            this.imp.drawCircle(new Location(x, y), radius);
        }
    }

    /**
     * Draws a circle. The circle is drawn using the current color, line width,
     * and alpha value. Has no effect if the specified radius is not positive.
     *
     * @param center the coordinates of the circle's centre
     * @param radius the circle's radius
     * @throws NullPointerException if <tt>center</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawCircle(Location center, int radius) {
        if (center == null) {
            throw new NullPointerException("center");
        }

        if (radius > 0) {
            this.imp.drawCircle(center, radius);
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
    public void drawImage(int x, int y, Image image) {
        if (image != null) {
            this.imp.drawImage(new Location(x, y), image.getImp());
        }
    }

    /**
     * Draws an image. The image is drawn using the current alpha value. The top
     * left corner of the image is positioned at <tt>topLeft</tt>. Has no effect
     * if <tt>image</tt> is <tt>null</tt>.
     *
     * @param topLeft the coordinates of the top left corner
     * @param image the image to draw
     * @throws NullPointerException if <tt>topLeft</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawImage(Location topLeft, Image image) {
        if (topLeft == null) {
            throw new NullPointerException("topLeft");
        }

        if (image != null) {
            this.imp.drawImage(topLeft, image.getImp());
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
    public void drawImage(int x, int y, Image image, Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (image != null) {
            this.imp.drawImage(alignment.align(new Location(x, y), image.getSize()), image.getImp());
        }
    }

    /**
     * Draws an image. The image is drawn using the current alpha value. The
     * image is aligned relative to <tt>anchor</tt>. Has no effect if
     * <tt>image</tt> is <tt>null</tt>.
     *
     * @param anchor the alignment point
     * @param image the image to draw
     * @param alignment specifies how to align the image relative to
     * <tt>anchor</tt>
     * @throws NullPointerException if <tt>anchor</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawImage(Location anchor, Image image, Alignment alignment) {
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }

        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (image != null) {
            this.imp.drawImage(alignment.align(anchor, image.getSize()), image.getImp());
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
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.imp.drawLine(new Location(x1, y1), new Location(x2, y2));
    }

    /**
     * Draws a straight line. The line is drawn from the coordinates
     * <tt>from</tt> to the coordinates <tt>to</tt> with the current color, line
     * width, and alpha value.
     *
     * @param from the coordinates of the line's start point
     * @param to the coordinates of the lines' end point
     * @throws NullPointerException if <tt>from</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>to</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawLine(Location from, Location to) {
        if (from == null) {
            throw new NullPointerException("from");
        }

        if (to == null) {
            throw new NullPointerException("to");
        }

        this.imp.drawLine(from, to);
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
    public void drawRectangle(int x, int y, int width, int height) {
        if (width > 0 && height > 0) {
            this.imp.drawRectangle(new Location(x, y), new Size(width, height));
        }
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current color, line
     * width, and alpha value. The top left corner of the rectangle is
     * positioned at the coordinates <tt>topLeft</tt>. Has no effect if size is
     * empty.
     *
     * @param topLeft the coordinates of the rectangle's top left corner
     * @param size the size of the rectangle
     * @throws NullPointerException if <tt>topLeft</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>size</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawRectangle(Location topLeft, Size size) {
        if (topLeft == null) {
            throw new NullPointerException("topLeft");
        }

        if (size == null) {
            throw new NullPointerException("size");
        }

        if (!size.isEmpty()) {
            this.imp.drawRectangle(topLeft, size);
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
    public void drawRectangle(int x, int y, int width, int height, Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            final Size size = new Size(width, height);
            this.imp.drawRectangle(alignment.align(new Location(x, y), size), size);
        }
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current color, line
     * width, and alpha value. The rectangle is aligned relative
     * to<tt>anchor</tt>. Has no effect if size is empty.
     *
     * @param anchor the alignment point
     * @param size the size of the rectangle
     * @param alignment specifies how to align the rectangle relative to
     * <tt>anchor</tt>
     * @throws NullPointerException if <tt>anchor</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>size</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
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
            this.imp.drawRectangle(alignment.align(anchor, size), size);
        }
    }

    /**
     * @deprecated Use {@link #drawText(int, int, java.lang.String)} instead.
     */
    @Deprecated
    public void drawString(int x, int y, String text) {
        this.drawString(new Location(x, y), text, Alignment.TOP_LEFT);
    }

    /**
     * @deprecated Use {@link #drawText(ch.jeda.Location, java.lang.String)}
     * instead.
     */
    @Deprecated
    public void drawString(Location topLeft, String text) {
        this.drawString(topLeft, text, Alignment.TOP_LEFT);
    }

    /**
     * @deprecated Use
     * {@link #drawText(int, int, java.lang.String, ch.jeda.ui.Alignment)}
     * instead.
     */
    @Deprecated
    public final void drawString(int x, int y, String text, Alignment alignment) {
        this.drawString(new Location(x, y), text, alignment);
    }

    /**
     * @deprecated Use
     * {@link #drawText(ch.jeda.Location, java.lang.String, ch.jeda.ui.Alignment)}
     * instead.
     */
    @Deprecated
    public void drawString(Location anchor, String text, Alignment alignment) {
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }

        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (text == null || text.length() == 0) {
            return;
        }

        this.imp.drawText(alignment.align(anchor, this.imp.textSize(text)), text);
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
    public void drawText(int x, int y, String text) {
        if (text != null && !text.isEmpty()) {
            this.imp.drawText(new Location(x, y), text);
        }
    }

    /**
     * Draws a text. The text is drawn using the current color, alpha value and
     * font size. The top left corner of the rectangle is positioned at the
     * coordinates <tt>topLeft</tt>. Has no effect if <tt>text</tt>
     * is <tt>null</tt> or empty.
     *
     * @param topLeft the coordinates of the top left corner
     * @param text the text to draw
     * @throws NullPointerException if <tt>topLeft</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawText(Location topLeft, String text) {
        if (topLeft == null) {
            throw new NullPointerException("topLeft");
        }

        if (text != null && !text.isEmpty()) {
            this.imp.drawText(topLeft, text);
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
    public final void drawText(int x, int y, String text, Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (text != null && !text.isEmpty()) {
            this.imp.drawText(alignment.align(new Location(x, y), this.imp.textSize(text)), text);
        }
    }

    /**
     * Draws a text. The text is drawn using the current color, alpha value and
     * font size. The rectangle is aligned relative to <tt>anchor</tt>. Has no
     * effect if <tt>text</tt>
     * is <tt>null</tt> or empty.
     *
     * @param anchor the alignment point
     * @param text the text to draw
     * @param alignment specifies how to align the text relative to
     * <tt>anchor</tt>
     * @throws NullPointerException if <tt>anchor</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void drawText(Location anchor, String text, Alignment alignment) {
        if (anchor == null) {
            throw new NullPointerException("anchor");
        }

        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (text != null && !text.isEmpty()) {
            this.imp.drawText(alignment.align(anchor, this.imp.textSize(text)), text);
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
    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.imp.drawPolygon(createPoints(x1, y1, x2, y2, x3, y3));
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
    public void fillCircle(int x, int y, int radius) {
        if (radius > 0) {
            this.imp.fillCircle(new Location(x, y), radius);
        }
    }

    /**
     * Draws a filled circle. The circle is filled using the current color and
     * alpha value. Has no effect if the specified radius is not positive.
     *
     * @param center the coordinates of the circle's centre
     * @param radius the circle's radius
     * @throws NullPointerException if <tt>center</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void fillCircle(Location center, int radius) {
        if (center == null) {
            throw new NullPointerException("center");
        }

        if (radius > 0) {
            this.imp.fillCircle(center, radius);
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
    public void fillRectangle(int x, int y, int width, int height) {
        if (width > 0 && height > 0) {
            this.imp.fillRectangle(new Location(x, y), new Size(width, height));
        }
    }

    /**
     * Draws a filled rectangle. The rectangle is filled using the current color
     * and alpha value. The top left corner of the rectangle is positioned at
     * the coordinates <tt>topLeft</tt>. Has no effect if size is empty.
     *
     * @param topLeft the coordinates of the rectangle's top left corner
     * @param size the size of the rectangle
     * @throws NullPointerException if <tt>topLeft</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>size</tt> is <tt>null</tt>
     *
     * @since 1
     */
    public void fillRectangle(Location topLeft, Size size) {
        if (topLeft == null) {
            throw new NullPointerException("topLeft");
        }

        if (size == null) {
            throw new NullPointerException("size");
        }

        if (!size.isEmpty()) {
            this.imp.fillRectangle(topLeft, size);
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
    public void fillRectangle(int x, int y, int width, int height, Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            final Size size = new Size(width, height);
            this.imp.fillRectangle(alignment.align(new Location(x, y), size), size);
        }
    }

    /**
     * Draws a filled rectangle. The rectangle is filled using the current color
     * and alpha value. The rectangle is aligned relative to<tt>anchor</tt>. Has
     * no effect if size is empty.
     *
     * @param anchor the alignment point
     * @param size the size of the rectangle
     * @param alignment specifies how to align the rectangle relative to
     * <tt>anchor</tt>
     * @throws NullPointerException if <tt>anchor</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>size</tt> is <tt>null</tt>
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1
     */
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
            this.imp.fillRectangle(alignment.align(anchor, size), size);
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
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.imp.fillPolygon(createPoints(x1, y1, x2, y2, x3, y3));
    }

    /**
     * <b>Experimental</b>
     */
    public void floodFill(int x, int y, Color oldColor, Color newColor) {
        this.floodFill(new Location(x, y), oldColor, newColor);
    }

    /**
     * <b>Experimental</b>
     */
    public void floodFill(Location location, Color oldColor, Color newColor) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        if (oldColor == null) {
            throw new NullPointerException("oldColor");
        }

        if (newColor == null) {
            throw new NullPointerException("newColor");
        }

        Stack<Location> stack = new Stack();
        stack.push(location);
        while (!stack.isEmpty()) {
            location = stack.pop();
            if (this.getPixelAt(location).equals(oldColor)) {
                this.setPixelAt(location, newColor);
                stack.push(new Location(location.x, location.y + 1));
                stack.push(new Location(location.x, location.y - 1));
                stack.push(new Location(location.x + 1, location.y));
                stack.push(new Location(location.x - 1, location.y));
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
     * @see #getSize()
     * @see #getWidth()
     * @since 1
     */
    public int getHeight() {
        return this.imp.getSize().height;
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
    public Color getPixelAt(int x, int y) {
        return this.getPixelAt(new Location(x, y));
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

        if (this.getSize().contains(location)) {
            return this.imp.getPixelAt(location);
        }
        else {
            return Color.NONE;
        }
    }

    /**
     * Returns the size of the canvas in pixels.
     *
     * @return size of canvas
     *
     * @see #getHeight()
     * @see #getWidth()
     * @since 1
     */
    public Size getSize() {
        return this.imp.getSize();
    }

    /**
     * Returns the current affine transformation of the canvas.
     *
     * @see #popTransformation()
     * @see #pushTransformation(ch.jeda.Transformation)
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
     * @see #getSize()
     * @since 1
     */
    public int getWidth() {
        return this.imp.getSize().width;
    }

    /**
     * Pops a transfromation from the stack and sets it as current affine
     * transformation of the canvas. All subsequent drawing operations will be
     * affected by the current affine transformation.
     *
     * @throws IllegalStateException if there is no element on the
     * transformation stack.
     *
     * @see #getTransformation()
     * @see #pushTransformation(ch.jeda.Transformation)
     * @since 1
     */
    public void popTransformation() {
        if (this.transformationStack.isEmpty()) {
            throw new IllegalStateException("Empty transformation stack.");
        }

        this.transformation = this.transformationStack.pop();
        this.imp.setTransformation(this.transformation);
    }

    /**
     * Pushes the current transformation to the transformation stack and sets
     * the specified transformation as current affine transformation of the
     * canvas. All subsequent drawing operations will be affected by the current
     * affine transformation.
     *
     * @param transformation the new transformation
     * @throws NullPointerException if transformation is <code>null</code>
     *
     * @see #getTransformation()
     * @see #popTransformation()
     * @since 1
     */
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
     * @param alpha new alpha value
     * @throws IllegalArgumentException if alpha value is outside the valid
     * range
     *
     * @see #getAlpha()
     * @since 1
     */
    public void setAlpha(int alpha) {
        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        this.alpha = alpha;
        this.imp.setAlpha(this.alpha);
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
    public void setColor(Color color) {
        if (color == null) {
            throw new NullPointerException("color");
        }

        this.color = color;
        this.imp.setColor(this.color);
    }

    /**
     * Sets the font size. The font size set by this method is applied to all
     * subsequent <tt>drawText(...)</tt> operations.
     *
     * @param size new font size
     * @throws IllegalArgumentException if <tt>size</tt> is not positive
     *
     * @see #getFontSize()
     *
     * @since 1
     */
    public void setFontSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size");
        }

        this.fontSize = size;
        this.imp.setFontSize(this.fontSize);
    }

    /**
     * Sets the line width. The line width set by this method is applied to all
     * subsequent <tt>draw...</tt> operations.
     *
     * @param width new line width
     *
     * @throws IllegalArgumentException if <tt>lineWidth</tt> is not positive
     *
     * @since 1
     */
    public void setLineWidth(double lineWidth) {
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
    public void setPixelAt(int x, int y, Color color) {
        this.setPixelAt(new Location(x, y), color);
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
            this.imp.setPixelAt(location, color);
        }
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
     * Returns the size of a text in pixels. Returns the width and height in
     * pixels of the specified text given the current font size.
     *
     * @param text
     * @return size of text in pixels
     */
    public Size textSize(String text) {
        if (text == null || text.isEmpty()) {
            return Size.EMPTY;
        }
        else {
            return this.imp.textSize(text);
        }
    }

    Canvas() {
        this.transformationStack = new Stack();
        this.alpha = 255;
        this.color = DEFAULT_FOREGROUND;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.transformation = Transformation.IDENTITY;
    }

    void setImp(CanvasImp imp) {
        this.imp = imp;
        this.imp.setAlpha(this.alpha);
        this.imp.setColor(this.color);
        this.imp.setFontSize(this.fontSize);
        this.imp.setLineWidth(this.lineWidth);
        this.imp.setTransformation(this.transformation);
    }

    private static Iterable<Location> createPoints(int x1, int y1, int x2, int y2, int x3, int y3) {
        final List result = new ArrayList();
        result.add(new Location(x1, y1));
        result.add(new Location(x2, y2));
        result.add(new Location(x3, y3));
        return result;
    }
}
