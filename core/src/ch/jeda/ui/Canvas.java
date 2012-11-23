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
import ch.jeda.platform.CanvasImp;
import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.Transformation;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class represents a drawing surface.
 * <p>
 * The drawing surface has certain states that influence the drawing operations:
 * <ul>
 *   <li> color: the color of the pen used to draw. Initially, the color is black.
 *   <li> font size: the size of the font used to render text. Initially, the
 *      font size is 16.
 * </ul>
 *
 * @version 1.0
 */
public class Canvas {

    private static final int DEFAULT_FONT_SIZE = 16;
    private static final Color DEFAULT_FOREGROUND = Color.BLACK;
    private final Stack<Transformation> transformationStack;
    private int alpha;
    private Color color;
    private int fontSize;
    private CanvasImp imp;
    private Transformation transformation;

    Canvas() {
        this.transformationStack = new Stack();
        this.alpha = 255;
        this.color = DEFAULT_FOREGROUND;
        this.fontSize = DEFAULT_FONT_SIZE;
        this.transformation = Transformation.IDENTITY;
    }

    /**
     * Initializes this Canvas.
     *
     * @param width the width of this canvas in pixels
     * @param height the height of this canvas in pixels
     * @throws IllegalArgumentException if width or height are smaller than 1
     * 
     * @since 1.0
     */
    public Canvas(int width, int height) {
        this(new Size(width, height));
    }

    /**
     * Initializes this Canvas.
     *
     * @param size the size of this canvas in pixels
     * @throws NullPointerException if size is <code>null</code>
     * @throws IllegalArgumentException if size is empty
     * 
     * @since 1.0
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
     * Clears the entire drawing canvas.
     * The canvas is filled with the transparent color.
     * 
     * @since 1.0
     */
    public void clear() {
        this.imp.clear();
    }

    public void copyFrom(Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException("canvas");
        }

        this.imp.copyFrom(Location.ORIGIN, canvas.imp);
    }

    /**
     * Draws a circle using the current color.
     * Has no effect if radius is not positive.
     *
     * @param x the x coordinate of the circle's centre
     * @param y the y coordinate of the circle's centre
     * @param radius the circle's radius
     *
     * @since 1.0
     */
    public void drawCircle(int x, int y, int radius) {
        this.drawCircle(new Location(x, y), radius);
    }

    /**
     * Draws a circle using the current color.
     * Has no effect if radius is not positive.
     *
     * @param center the coordinates of the circle's centre
     * @param radius the circle's radius
     * @throws NullPointerException if <code>center</code> is <code>null</code>
     *
     * @since 1.0
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
     * Draws an image using the current alpha settings.
     * The top left corner of the image is positioned at the coordinates
     * (<code>x</code>, <code>y</code>).
     * Has no effect if <code>image</code> is <code>null</code>.
     *
     * @param x the x coordinate of the image's top left corner
     * @param y the y coordinate of the image's top left corner
     * @param image the image to draw
     *
     * @since 1.0
     */
    public void drawImage(int x, int y, Image image) {
        this.drawImage(new Location(x, y), image, Alignment.TOP_LEFT);
    }

    /**
     * Draws an image using the current alpha settings.
     * The top left corner of the image is positioned at <code>topLeft</code>.
     * Has no effect if <code>image</code> is <code>null</code>.
     * 
     * @param topLeft the coordinates of the top left corner
     * @param image the image to draw
     * @throws NullPointerException if <code>anchor</code> is <code>null</code>
     * 
     * @since 1.0
     */
    public void drawImage(Location topLeft, Image image) {
        this.drawImage(topLeft, image, Alignment.TOP_LEFT);
    }

    /**
     * Draws an image using the current alpha settings.
     * The image is aligned relative to the coordinates (<code>x</code>, <code>y</code>).
     * Has no effect if <code>image</code> is <code>null</code>.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param image the image to draw
     * @param alignment specifies how to align the image relative to (<code>x</code>, <code>y</code>)
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     *
     * @since 1.0
     */
    public void drawImage(int x, int y, Image image, Alignment alignment) {
        this.drawImage(new Location(x, y), image, alignment);
    }

    /**
     * Draws an image using the current alpha settings.
     * The image is aligned relative to <code>anchor</code>.
     * Has no effect if <code>image</code> is <code>null</code>.
     *
     * @param anchor the alignment point
     * @param image the image to draw
     * @param alignment specifies how to align the image relative to <code>anchor</code>
     * @throws NullPointerException if <code>anchor</code> is <code>null</code>
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     *
     * @since 1.0
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
     * Draws a straight line from the coordinates (<code>x1</code>, <code>y1</code>)
     * to the coordinates (<code>x2</code>, <code>y2</code>) using the current color.
     *
     * @param x1 the x coordinate of the line's start point
     * @param y1 the y coordinate of the lines' start point
     * @param x2 the x coordinate of the line's end point
     * @param y2 the y coordinate of the line's end point
     *
     * @since 1.0
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.drawLine(new Location(x1, y1), new Location(x2, y2));
    }

    /**
     * Draws a straight line from the coordinates <code>from</code> to the
     * coordinates <code>to</code> using the current color.
     *
     * @param from the coordinates of the line's start point
     * @param to the coordinates of the lines' end point
     * @throws NullPointerException if <code>from</code> is <code>null</code>
     * @throws NullPointerException if <code>to</code> is <code>null</code>
     *
     * @since 1.0
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
     * Draws a rectangle using the current color.
     * The top left corner is a the coordinates (<code>x</code>, <code>y</code>).
     *
     * @param x the x coordinate of the rectangle's top left corner
     * @param y the y  coordinate of the rectangle's top left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @throws IllegalArgumentException if <code>width</code> or <code>height</code> are negative
     *
     * @since 1.0
     */
    public void drawRectangle(int x, int y, int width, int height) {
        this.drawRectangle(new Location(x, y), new Size(width, height), Alignment.TOP_LEFT);
    }

    /**
     * Draws a rectangle using the current color.
     * The top left corner is a the coordinates <code>topLeft</code>.
     *
     * @param topLeft the coordinates of the rectangle's top left corner
     * @param size the size of the rectangle
     * @throws NullPointerException if <code>topLeft</code> is <code>null</code>
     * @throws NullPointerException if <code>size</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>
     *
     * @since 1.0
     */
    public void drawRectangle(Location topLeft, Size size) {
        this.drawRectangle(topLeft, size, Alignment.TOP_LEFT);
    }

    /**
     * Draws a rectangle using the current color.
     * The rectangle is aligned relative to the coordinates (<code>x</code>, <code>y</code>).
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param alignment specifies how to align the rectangle relative to (<code>x</code>, <code>y</code>).
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     *
     * @since 1.0
     */
    public void drawRectangle(int x, int y, int width, int height, Alignment alignment) {
        this.drawRectangle(new Location(x, y), new Size(width, height), alignment);
    }

    /**
     * Draws a rectangle using the current color.
     * The rectangle is aligned relative to <code>anchor</code>.
     *
     * @param anchor the alignment point
     * @param size the size of the rectangle
     * @param alignment specifies how to align the rectangle relative to <code>anchor</code>
     * @throws NullPointerException if <code>anchor</code> is <code>null</code>
     * @throws NullPointerException if <code>size</code> is <code>null</code>
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     * 
     * @since 1.0
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

        this.imp.drawRectangle(alignment.align(anchor, size), size);
    }

    /**
     * Draws a text using the current color and font size.
     * Text text's top left corner is at the coordinates (x, y).
     * Has no effect is <code>text</code> is <code>null</code>.
     *
     * @param x the x coordinate of the top left corner
     * @param y the y coordinate of the top left corner
     * @param text the text to draw
     *
     * @since 1.0
     */
    public void drawString(int x, int y, String text) {
        this.drawString(new Location(x, y), text, Alignment.TOP_LEFT);
    }

    /**
     * Draws a text using the current color and font size.
     * Text text's top left corner is at <code>topLeft</code>.
     * Has no effect is <code>text</code> is <code>null</code>.
     *
     * @param topLeft the coordinates of the top left corner
     * @param text the text to draw
     * @throws NullPointerException if <code>topLeft</code> is <code>null</code>
     *
     * @since 1.0
     */
    public void drawString(Location topLeft, String text) {
        this.drawString(topLeft, text, Alignment.TOP_LEFT);
    }

    /**
     * Draws a text using the current color and font size.
     * The text is aligned relative to the coordinates (<code>x</code>, <code>y</code>).
     * Has no effect is <code>text</code> is <code>null</code>.
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param text the text to draw
     * @param alignment specifies how to align the text relative to (<code>x</code>, <code>y</code>)
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     *
     * @since 1.0
     */
    public final void drawString(int x, int y, String text, Alignment alignment) {
        this.drawString(new Location(x, y), text, alignment);
    }

    /**
     * Draws a text using the current color and font size.
     * The text is aligned relative to <code>anchor</code>.
     * Has no effect is <code>text</code> is <code>null</code>.
     *
     * @param anchor the alignment point
     * @param text the text to draw
     * @param alignment specifies how to align the text relative to <code>anchor</code>
     * @throws NullPointerException if <code>anchor</code> is <code>null</code>
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     *
     * @since 1.0
     */
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

        this.imp.drawString(alignment.align(anchor, this.imp.textSize(text)), text);
    }

    /**
     * Draws a triangle using the current color. The triangle is specified by
     * the three corners (x1, y1), (x2, y2), and (x3, y3).
     *
     * @param x1 x coordinate of the first corner
     * @param y1 y coordinate of the first corner
     * @param x2 x coordinate of the second corner
     * @param y2 y coordinate of the second corner
     * @param x3 x coordinate of the third corner
     * @param y3 y coordinate of the third corner
     *
     * @since 1.0
     */
    public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.imp.drawPolygon(createPoints(x1, y1, x2, y2, x3, y3));
    }

    /**
     * Fills the entire canvas using the current color.
     * @since 1.0
     */
    public void fill() {
        this.imp.fill();
    }

    /**
     * Draws and fills a circle using the current color.
     * Has no effect if radius is not positive.
     *
     * @param x the x coordinate of the circle's centre
     * @param y the y coordinate of the circle's centre
     * @param radius the circle's radius
     *
     * @since 1.0
     */
    public void fillCircle(int x, int y, int radius) {
        this.fillCircle(new Location(x, y), radius);
    }

    /**
     * Draws and fills a circle using the current color.
     * Has no effect if radius is not positive.
     *
     * @param center the coordinates of the circle's centre
     * @param radius the circle's radius
     * @throws NullPointerException if <code>center</code> is <code>null</code>
     *
     * @since 1.0
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
     * Draws and fills a rectangle using the current color.
     * The top left corner is a the coordinates (<code>x</code>, <code>y</code>).
     *
     * @param x the x coordinate of the rectangle's top left corner
     * @param y the y  coordinate of the rectangle's top left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1.0
     */
    public void fillRectangle(int x, int y, int width, int height) {
        this.fillRectangle(new Location(x, y), new Size(width, height), Alignment.TOP_LEFT);
    }

    /**
     * Draws and fills a rectangle using the current color.
     * The top left corner is a the coordinates <code>topLeft</code>.
     *
     * @param topLeft the coordinates of the rectangle's top left corner
     * @param size the size of the rectangle
     * @throws NullPointerException if <code>topLeft</code> is <code>null</code>
     * @throws NullPointerException if <code>size</code> is <code>null</code>
     *
     * @since 1.0
     */
    public void fillRectangle(Location topLeft, Size size) {
        this.fillRectangle(topLeft, size, Alignment.TOP_LEFT);
    }

    /**
     * Draws and fills a rectangle using the current color.
     * The rectangle is aligned relative to the coordinates (<code>x</code>, <code>y</code>).
     *
     * @param x the x coordinate of the alignment point
     * @param y the y coordinate of the alignment point
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param alignment specifies how to align the rectangle relative to (<code>x</code>, <code>y</code>).
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     *
     * @since 1.0
     */
    public void fillRectangle(int x, int y, int width, int height, Alignment alignment) {
        this.fillRectangle(new Location(x, y), new Size(width, height), alignment);
    }

    /**
     * Draws and fills a rectangle using the current color.
     * The rectangle is aligned relative to <code>anchor</code>.
     *
     * @param anchor the alignment point
     * @param size the size of the rectangle
     * @param alignment specifies how to align the rectangle relative to <code>anchor</code>
     * @throws NullPointerException if <code>anchor</code> is <code>null</code>
     * @throws NullPointerException if <code>size</code> is <code>null</code>
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     * 
     * @since 1.0
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

        this.imp.fillRectangle(alignment.align(anchor, size), size);
    }

    /**
     * Draws and fills a triangle using the current color. The triangle is
     * specified by the three corners (x1, y1), (x2, y2), and (x3, y3).
     *
     * @param x1 x coordinate of the first corner
     * @param y1 y coordinate of the first corner
     * @param x2 x coordinate of the second corner
     * @param y2 y coordinate of the second corner
     * @param x3 x coordinate of the third corner
     * @param y3 y coordinate of the third corner
     * 
     * @since 1.0
     */
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        this.imp.fillPolygon(createPoints(x1, y1, x2, y2, x3, y3));
    }

    public void floodFill(int x, int y, Color oldColor, Color newColor) {
        this.imp.floodFill(new Location(x, y), oldColor, newColor);
    }

    /**
     * Returns the current alpha value.
     *
     * @return current alpha value.
     *
     * @see #setAlpha(int)
     * @since 1.0
     */
    public int getAlpha() {
        return this.alpha;
    }

    /**
     * Returns the current drawing color.
     *
     * @return current drawing color.
     *
     * @see #setColor(ch.jeda.ui.Color)
     * @since 1.0
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
     * @since 1.0
     */
    public int getFontSize() {
        return this.fontSize;
    }

    /**
     * Returns the height of the canvas in pixels.
     *
     * @return height of canvas in pixels
     *
     * @since 1.0
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
     * Returns the color of the pixel at the coordinates (x, y). Returns
     * {@link ch.jeda.ui.Color#NONE} when the pixel is outside the
     * canvas.
     *
     * @param x x coordinate of the pixel
     * @param y y coordinate of the pixel
     * @return the color of the pixel at (x, y)
     *
     * @see #setPixelAt(int, int, ch.jeda.ui.Color)
     * @since 1.0
     */
    public Color getPixelAt(int x, int y) {
        return this.getPixelAt(new Location(x, y));
    }

    public Color getPixelAt(Location location) {
        if (location == null) {
            throw new NullPointerException("location");
        }

        if (location.isInside(Location.ORIGIN, this.getSize())) {
            return this.imp.getPixelAt(location);
        }
        else {
            return Color.NONE;
        }
    }

    /**
     * Returns the size of this canvas.
     *
     * @return size of this canvas
     */
    public Size getSize() {
        return this.imp.getSize();
    }

    /**
     * Returns the current affine transformation of this canvas.
     * 
     * @see #popTransformation()
     * @see #pushTransformation(ch.jeda.Transformation)
     * @return the current transformation
     */
    public Transformation getTransformation() {
        return this.transformation;
    }

    /**
     * Returns the width of the canvas in pixels.
     *
     * @return width of canvas in pixels
     *
     * @since 1.0
     */
    public int getWidth() {
        return this.imp.getSize().width;
    }

    /**
     * Pops a transfromation from the stack and sets it as current affine
     * transformation of this canvas.
     * All subsequent drawing operations will be affected by the current affine
     * transformation.
     * 
     * @throws IllegalStateException if there is no element on the 
     *         transformation stack.
     * 
     * @see #getTransformation() 
     * @see #pushTransformation(ch.jeda.Transformation)
     * @since 1.0
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
     * the specified transformation as current affine transformation of this
     * canvas.
     * All subsequent drawing operations will be affected by the current affine
     * transformation.
     * 
     * @param transformation the new transformation
     * @throws NullPointerException if transformation is <code>null</code>
     * 
     * @see #getTransformation()
     * @see #popTransformation()
     * @since 1.0
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
     * Sets the alpha value to be used to draw subsequently. The alpha value
     * must be in the range from 0 (fully transparent) to 255 (fully opaque).
     *
     * @param alpha new alpha value
     * @throws IllegalArgumentException if alpha value is outside the valid range
     * 
     * @see #getAlpha()
     * @since 1.0
     */
    public void setAlpha(int alpha) {
        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        this.alpha = alpha;
        this.imp.setAlpha(this.alpha);
    }

    /**
     * Sets the current drawing color. Has no effect when color is null.
     *
     * @param color new drawing color.
     * @throws NullPointerException if color is null
     *
     * @see #getColor()
     * @since 1.0
     */
    public void setColor(Color color) {
        if (color == null) {
            throw new NullPointerException("color");
        }

        this.color = color;
        this.imp.setColor(this.color);
    }

    /**
     * Sets the current font size.
     *
     * @param size new font size
     *
     * @see #getFontSize()
     * @since 1.0
     */
    public void setFontSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size");
        }

        this.fontSize = size;
        this.imp.setFontSize(this.fontSize);
    }

    /**
     * Sets the current line width in pixels.
     * 
     * @param width new line width
     */
    public void setLineWidth(double width) {
        this.imp.setLineWidth(width);
    }

    /**
     * Sets the color of the pixel at the coordinates (x, y). Has no effect
     * when the pixel is outside the canvas.
     *
     * @param x x coordinate of the pixel
     * @param y y coordinate of the pixel
     * @param color new color of the pixel
     *
     * @see #getPixelAt(int, int)
     * @since 1.0
     */
    public void setPixelAt(int x, int y, Color color) {
        this.imp.setPixelAt(new Location(x, y), color);
    }

    public void setPixelAt(Location location, Color color) {
        if (location == null) {
            throw new NullPointerException("location");
        }
        if (color == null) {
            throw new NullPointerException("color");
        }

        if (location.isInside(Location.ORIGIN, this.getSize())) {
            this.imp.setPixelAt(location, color);
        }
    }

    /**
     * Creates an image that contains a copy of the contents of this canvas.
     *
     * @return Image containing a copy of this canvas
     *
     * @since 1.0
     */
    public Image takeSnapshot() {
        return new Image(this.imp.takeSnapshot());
    }

    /**
     * Returns the width and height of the specified text in pixels given the
     * current font.
     *
     * @param text
     * @return size of text in pixels
     * @throws NullPointerException if text is null
     */
    public Size textSize(String text) {
        return this.imp.textSize(text);
    }

    protected void setImp(CanvasImp imp) {
        this.imp = imp;
        this.imp.setAlpha(this.alpha);
        this.imp.setColor(this.color);
        this.imp.setFontSize(this.fontSize);
        this.imp.setTransformation(this.transformation);
    }

    private static Iterable<Location> createPoints(int x1, int y1, int x2, int y2, int x3, int y3) {
        List result = new ArrayList<Location>();
        result.add(new Location(x1, y1));
        result.add(new Location(x2, y2));
        result.add(new Location(x3, y3));
        return result;
    }
}
