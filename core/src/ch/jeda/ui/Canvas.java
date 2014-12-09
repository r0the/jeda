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

import ch.jeda.JedaInternal;
import ch.jeda.Transformation;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.CanvasTransformation;
import java.util.Stack;

/**
 * Represents a drawing surface. It provides methods to draw geometric primitives and images.
 *
 * <p>
 * A canvas has some attributes that influence the drawing operations:
 * <ul>
 * <li> <b>anti-aliasing</b>:
 * <li> <b>color</b>: The color used to draw geometric primitives. Initially, the color is black. The color can be
 * changed with {@link #setColor(ch.jeda.ui.Color)}.
 * <li> <b>line width</b>: the line width used to draw geometric shapes. The line width can be changed with
 * {@link #setLineWidth(double)}.
 * <li> <b>text size</b>: the size of the text. Initially, the text size is 16. The text size can be changed with
 * {@link #setTextSize(int)}.
 * <li> <b>typeface</b>: The typeface (font family) used to render text.
 * </ul>
 * <strong>Example:</strong>
 * <pre><code> Canvas canvas = new Canvas(100, 100);
 * canvas.setColor(Color.RED);
 * canvas.fillCircle(50, 50, 20);</code></pre>
 *
 * @since 1.0
 * @version 3
 */
public class Canvas {

    private static final double MINIMUM_SCALE = 1.0E-10;
    private static final int DEFAULT_TEXT_SIZE = 16;
    private static final Color DEFAULT_FOREGROUND = Color.BLACK;
    private boolean antiAliasing;
    private Color color;
    private CanvasImp imp;
    private double lineWidth;
    private int textSize;
    private CanvasTransformation transformation;
    private Typeface typeface;

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
     * @since 1.0
     */
    public Canvas(final int width, final int height) {
        if (width < 1) {
            throw new IllegalArgumentException("width");
        }

        if (height < 1) {
            throw new IllegalArgumentException("height");
        }

        this.antiAliasing = false;
        this.color = DEFAULT_FOREGROUND;
        this.textSize = DEFAULT_TEXT_SIZE;
        this.transformation = new CanvasTransformation();
        this.typeface = Typeface.SANS_SERIF;
        this.setImp(JedaInternal.createCanvasImp(width, height));
    }

    Canvas() {
        this.color = DEFAULT_FOREGROUND;
        this.textSize = DEFAULT_TEXT_SIZE;
        this.transformation = new CanvasTransformation();
        this.typeface = Typeface.SANS_SERIF;
    }

    /**
     * @deprecated
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
     * @since 1.0
     */
    public void drawCircle(final int x, final int y, final int radius) {
        if (radius > 0) {
            this.imp.drawCircle(x, y, radius);
        }
    }

    /**
     * Draws a circle. The circle is drawn using the current color, line width, and transformation. Has no effect if the
     * specified radius is not positive.
     *
     * @param x the x coordinate of the circle's centre
     * @param y the y coordinate of the circle's centre
     * @param radius the circle's radius
     *
     * @since 1.0
     */
    public void drawCircle(final double x, final double y, final double radius) {
        this.drawCircle((int) x, (int) y, (int) radius);
    }

    /**
     * Draws an image. The image is drawn using the current transformation. The top left corner of the image is
     * positioned at the specified coordinates. Has no effect if <tt>image</tt> is <tt>null</tt>.
     *
     * @param x the x coordinate of the image's top left corner
     * @param y the y coordinate of the image's top left corner
     * @param image the image to draw
     *
     * @since 1.0
     */
    public void drawImage(final int x, final int y, final Image image) {
        if (image != null) {
            this.imp.drawImage(x, y, image.getImp(), 255);
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
     * @since 1.0
     */
    public void drawImage(final double x, final double y, final Image image) {
        this.drawImage((int) x, (int) y, image);
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
     * @since 1.0
     */
    public void drawImage(final int x, final int y, final Image image, final int alpha) {
        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        if (image != null && alpha > 0) {
            this.imp.drawImage(x, y, image.getImp(), alpha);
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
     * @since 1.0
     */
    public void drawImage(final double x, final double y, final Image image, final int alpha) {
        this.drawImage((int) x, (int) y, image, alpha);
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
     * @since 1.0
     */
    public void drawImage(final int x, final int y, final Image image, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (image != null) {
            this.imp.drawImage(alignment.alignX(x, image.getWidth()), alignment.alignY(y, image.getHeight()),
                               image.getImp(), 255);
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
     * @since 1.0
     */
    public void drawImage(final double x, final double y, final Image image, final Alignment alignment) {
        this.drawImage((int) x, (int) y, image, alignment);
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
     * @since 1.0
     */
    public void drawImage(final int x, final int y, final Image image, final int alpha, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        if (image != null && alpha > 0) {
            this.imp.drawImage(alignment.alignX(x, image.getWidth()), alignment.alignY(y, image.getHeight()),
                               image.getImp(), alpha);
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
     * @since 1.0
     */
    public void drawImage(final double x, final double y, final Image image, final int alpha,
                          final Alignment alignment) {
        this.drawImage((int) x, (int) y, image, alpha, alignment);
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
     * @since 1.0
     */
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        this.imp.drawLine(x1, y1, x2, y2);
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
     * @since 1.0
     */
    public void drawLine(final double x1, final double y1, final double x2, final double y2) {
        this.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
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
     *
     * @since 1.0
     */
    public void drawPolygon(final int... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.imp.drawPolygon(points);
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
     *
     * @since 1.0
     */
    public void drawPolygon(final double... points) {
        this.imp.drawPolygon(toIntArray(points));
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
     * @since 1.0
     */
    public void drawRectangle(final int x, final int y, final int width, final int height) {
        if (width > 0 && height > 0) {
            this.imp.drawRectangle(x, y, width, height);
        }
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
     * @since 1.0
     */
    public void drawRectangle(final double x, final double y, final double width, final double height) {
        this.drawRectangle((int) x, (int) y, (int) width, (int) height);
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
     * @since 1.0
     */
    public void drawRectangle(final int x, final int y, final int width, final int height,
                              final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            this.imp.drawRectangle(alignment.alignX(x, width), alignment.alignY(y, height), width, height);
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
     * @since 1.0
     */
    public void drawRectangle(final double x, final double y, final double width, final double height,
                              final Alignment alignment) {
        this.drawRectangle((int) x, (int) y, (int) width, (int) height, alignment);
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
     * @since 1.0
     */
    public void drawText(final int x, final int y, final String text) {
        if (text != null && !text.isEmpty()) {
            this.imp.drawText(x, y, text);
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
     * @since 1.0
     */
    public void drawText(final double x, final double y, final String text) {
        this.drawText((int) x, (int) y, text);
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
     * @since 1.0
     */
    public final void drawText(final int x, final int y, final String text, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (text != null && !text.isEmpty()) {
            this.imp.drawText(alignment.alignX(x, this.imp.textWidth(text)), alignment.alignY(y, this.imp.textHeight(text)), text);
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
     * @since 1.0
     */
    public final void drawText(final double x, final double y, final String text, final Alignment alignment) {
        this.drawText((int) x, (int) y, text, alignment);
    }

    /**
     * Fills the entire canvas. The canvas is filled using the current color.
     *
     * @since 1.0
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
     * @since 1.0
     */
    public void fillCircle(final int x, final int y, final int radius) {
        if (radius > 0) {
            this.imp.fillCircle(x, y, radius);
        }
    }

    /**
     * Draws a filled a circle. The circle is drawn using the current color and transformation. Has no effect if the
     * specified radius is not positive.
     *
     * @param x the x coordinate of the circle's centre
     * @param y the y coordinate of the circle's centre
     * @param radius the circle's radius
     *
     * @since 1.0
     */
    public void fillCircle(final double x, final double y, final double radius) {
        this.fillCircle((int) x, (int) y, (int) radius);
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
     *
     * @since 1.0
     */
    public void fillPolygon(final int... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.imp.fillPolygon(points);
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
     *
     * @since 1.0
     */
    public void fillPolygon(final double... points) {
        this.imp.fillPolygon(toIntArray(points));
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
     * @since 1.0
     */
    public void fillRectangle(final int x, final int y, final int width, final int height) {
        if (width > 0 && height > 0) {
            this.imp.fillRectangle(x, y, width, height);
        }
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
     * @since 1.0
     */
    public void fillRectangle(final double x, final double y, final double width, final double height) {
        this.fillRectangle((int) x, (int) y, (int) width, (int) height);
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
     * @since 1.0
     */
    public void fillRectangle(final int x, final int y, final int width, final int height, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            this.imp.fillRectangle(alignment.alignX(x, width), alignment.alignY(y, height), width, height);
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
     * @since 1.0
     */
    public void fillRectangle(final double x, final double y, final double width, final double height,
                              final Alignment alignment) {
        this.fillRectangle((int) x, (int) y, (int) width, (int) height, alignment);
    }

    /**
     * Fills an area of a specified color with another color.
     *
     * @param x the x coordinate of the starting point
     * @param y the y coordinate of the starting point
     * @param oldColor the color to look for
     * @param newColor the color to replace it with
     *
     * @since 1.0
     */
    public void floodFill(int x, int y, final Color oldColor, final Color newColor) {
        if (oldColor == null) {
            throw new NullPointerException("oldColor");
        }

        if (newColor == null) {
            throw new NullPointerException("newColor");
        }

        final Stack<Integer> stackX = new Stack<Integer>();
        final Stack<Integer> stackY = new Stack<Integer>();
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
     * @since 1.0
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * @deprecated Use {@link #getTextSize()} instead.
     */
    public int getFontSize() {
        return this.getTextSize();
    }

    /**
     * Returns the height of the canvas in pixels.
     *
     * @return height of canvas
     *
     * @see #getWidth()
     * @since 1.0
     */
    public int getHeight() {
        return this.imp.getHeight();
    }

    /**
     * Returns the current line width in pixels.
     *
     * @return current line width
     *
     * @see #setLineWidth(double)
     * @since 1.0
     */
    public double getLineWidth() {
        return this.imp.getLineWidth();
    }

    /**
     * Returns the color of a pixel. Returns the color of the pixel at the coordinates (<tt>x</tt>, <tt>y</tt>). Returns
     * {@link ch.jeda.ui.Color#TRANSPARENT} if the coordinates do not reference a pixel inside the canvas.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the color of the pixel at (<tt>x</tt>, <tt>y</tt>)
     *
     * @see #setPixelAt(int, int, ch.jeda.ui.Color)
     * @since 1.0
     */
    public Color getPixelAt(final int x, final int y) {
        if (this.contains(x, y)) {
            return this.imp.getPixelAt(x, y);
        }
        else {
            return Color.TRANSPARENT;
        }
    }

    /**
     * Returns the current rotation angle of the canvas.
     *
     * @return the current rotation angle of the canvas
     *
     * @since 1.6
     * @see #setRotation(double)
     */
    public double getRotation() {
        return this.transformation.rotation;
    }

    /**
     * Returns the current scale of the canvas.
     *
     * @return the current scale of the canvas
     *
     * @since 1.6
     * @see #setScale(double)
     */
    public double getScale() {
        return this.transformation.scale;
    }

    /**
     * Returns the current text size.
     *
     * @return current text size
     *
     * @see #setTextSize(int)
     * @since 1.0
     */
    public int getTextSize() {
        return this.textSize;
    }

    /**
     * Returns the current horizontal translation of the canvas.
     *
     * @return the current horizontal translation of the canvas
     *
     * @since 1.6
     * @see #setTranslation(double, double)
     */
    public double getTranslationX() {
        return this.transformation.translationX;
    }

    /**
     * Returns the current vertical translation of the canvas.
     *
     * @return the current vertical translation of the canvas
     *
     * @since 1.6
     * @see #setTranslation(double, double)
     */
    public double getTranslationY() {
        return this.transformation.translationY;
    }

    /**
     * @deprecated
     */
    public Transformation getTransformation() {
        final Transformation result = new Transformation();
        result.translate((float) this.transformation.translationX, (float) this.transformation.translationY);
        result.scale((float) this.transformation.scale, (float) this.transformation.scale);
        result.rotate((float) this.transformation.rotation);
        return result;
    }

    /**
     * Returns the typeface used by the canvas.
     *
     * @return the typeface used by the canvas
     *
     * @see #setTypeface(ch.jeda.ui.Typeface)
     * @since 1.3
     */
    public Typeface getTypeface() {
        return this.typeface;
    }

    /**
     * Returns the width of the canvas in pixels.
     *
     * @return width of canvas
     *
     * @see #getHeight()
     * @since 1.0
     */
    public int getWidth() {
        return this.imp.getWidth();
    }

    /**
     * Checks is anti-aliasing is enabled.
     *
     * @return <tt>true</tt> if anti-aliasing is enabled, otherwise <tt>false</tt>
     *
     * @see #setAntiAliasing(boolean)
     * @since 1.0
     */
    public boolean isAntiAliasing() {
        return this.antiAliasing;
    }

    /**
     * Resets all canvas transformations (rotation, translation, and scale). All subsequent drawing operations will
     * appear untransformed.
     *
     * @see #setRotation(double)
     * @see #setScale(double)
     * @see #setTranslation(double, double)
     * @since 1.6
     */
    public void resetTransformations() {
        this.transformation.reset();
        this.imp.setTransformation(this.transformation);
    }

    /**
     * Enables or disables the anti-aliasing filter. The borders of drawn text or shapes may not appear "smooth". This
     * effect is called <a href="http://en.wikipedia.org/wiki/Jaggies" target="_blank">Jaggies</a>. To counter this
     * effect, an <a href=http://en.wikipedia.org/wiki/Anti-aliasing_filter" target="_blank">anti-aliasing filter</a> is
     * used when rendering the text or shapes.
     *
     * @param antiAliasing <tt>true</tt> to enable the anti-aliasing filter, <tt>false</tt> to disable it.
     * @since 1.0
     */
    public void setAntiAliasing(final boolean antiAliasing) {
        if (this.antiAliasing != antiAliasing) {
            this.antiAliasing = antiAliasing;
            this.imp.setAntiAliasing(this.antiAliasing);
        }
    }

    /**
     * Sets the drawing color. The value set by this method is applied to all subsequent <tt>draw...</tt> and
     * <tt>fill...</tt> operations.
     *
     * @param color new drawing color.
     * @throws NullPointerException if <tt>color</tt> is <tt>null</tt>
     *
     * @see #getColor()
     * @since 1.0
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
     * @deprecated Use {@link #setTextSize(int)} instead.
     */
    public void setFontSize(int size) {
        this.setTextSize(size);
    }

    /**
     * Sets the line width. The line width set by this method is applied to all subsequent <tt>draw...</tt> operations.
     * Set 0 for drawing hairlines independent of the transformation.
     *
     * @param lineWidth the new line width
     * @throws IllegalArgumentException if <tt>lineWidth</tt> is negative
     *
     * @see #getLineWidth()
     * @since 1.0
     */
    public void setLineWidth(final double lineWidth) {
        if (lineWidth < 0.0) {
            throw new IllegalArgumentException("lineWidth");
        }

        this.lineWidth = lineWidth;
        this.imp.setLineWidth(this.lineWidth);
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
     * @since 1.0
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
     * Sets the text size. The text size set by this method is applied to all subsequent <tt>drawText(...)</tt>
     * operations.
     *
     * @param size the new text size
     * @throws IllegalArgumentException if <tt>size</tt> is not positive
     *
     * @see #getTextSize()
     * @since 1.0
     */
    public void setTextSize(final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size");
        }

        if (this.textSize != size) {
            this.textSize = size;
            this.imp.setTextSize(this.textSize);
        }
    }

    /**
     * Rotates the canvas. The rotation centre is the origin of the canvas. Setting a rotation causes all subsequent
     * drawing operations to appear rotated by the specified angle.
     *
     * @param angle the rotation angle in radians
     *
     * @see #getRotation()
     * @see #resetTransformations()
     * @see #setTranslation(double, double)
     * @see #setScale(double)
     * @since 1.6
     */
    public void setRotation(final double angle) {
        this.transformation.rotation = MathUtil.normalizeAngle(angle);
        this.imp.setTransformation(this.transformation);
    }

    /**
     * Translates the origin of the canvas. Setting a translation causes all subsequent drawing operations to appear
     * translated by the specified <tt>dx</tt> and <tt>dy</tt>.
     *
     * @param dx the horizontal translation in pixels
     * @param dy the vertical translation in pixels
     *
     * @see #getTranslationX()
     * @see #getTranslationY()
     * @see #resetTransformations()
     * @see #setRotation(double)
     * @see #setScale(double)
     * @since 1.6
     */
    public void setTranslation(final double dx, final double dy) {
        this.transformation.translationX = dx;
        this.transformation.translationY = dy;
        this.imp.setTransformation(this.transformation);
    }

    /**
     * Scales the canvas. The scaling centre is the origin of the canvas. Setting a scale causes all subsequent drawing
     * operations to appear scaled by the specified factor.
     *
     * @param scale the scaling factor
     * @throws IllegalArgumentException if <tt>scale</tt> is too close to zero
     *
     * @see #getScale()
     * @see #resetTransformations()
     * @see #setRotation(double)
     * @see #setTranslation(double, double)
     * @since 1.6
     */
    public void setScale(final double scale) {
        if (MathUtil.isZero(scale, MINIMUM_SCALE)) {
            throw new IllegalArgumentException("scale");
        }

        this.transformation.scale = scale;
        this.imp.setTransformation(this.transformation);
    }

    /**
     * @deprecated Use {@link #setRotation(double)}, {@link #setScale(double)}, and
     * {@link #setTranslation(double, double)} instead.
     */
    public void setTransformation(final Transformation transformation) {
    }

    /**
     * Sets the typeface to be used to draw text.
     *
     * @param typeface the typeface to be used to draw text
     * @throws NullPointerException if <tt>typeface</tt> is <tt>null</tt>
     * @throws IllegalArgumentException if <tt>typeface.isAvailable()</tt> is <tt>false</tt>
     *
     * @since 1.3
     */
    public void setTypeface(final Typeface typeface) {
        if (typeface == null) {
            throw new NullPointerException("typeface");
        }

        if (!typeface.isAvailable()) {
            throw new IllegalArgumentException("typeface");
        }

        if (!this.typeface.equals(typeface)) {
            this.typeface = typeface;
            this.imp.setTypeface(this.typeface.imp);
        }
    }

    /**
     * Takes a snapshot of the canvas. Creates an image that contains a copy of the contents of the canvas.
     *
     * @return image containing a copy of the canvas
     *
     * @since 1.0
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
     * @since 1.0
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
     * @since 1.0
     */
    public int textWidth(final String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        else {
            return this.imp.textWidth(text);
        }
    }

    final void setImp(final CanvasImp imp) {
        this.imp = imp;
        this.imp.setAntiAliasing(this.antiAliasing);
        this.imp.setColor(this.color);
        this.imp.setTextSize(this.textSize);
        this.imp.setLineWidth(this.lineWidth);
        this.imp.setTransformation(this.transformation);
        this.imp.setTypeface(this.typeface.imp);
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < this.getWidth() && 0 <= y && y < this.getHeight();
    }

    private static int[] toIntArray(double[] values) {
        final int[] result = new int[values.length];
        for (int i = 0; i < values.length; ++i) {
            result[i] = (int) values[i];
        }

        return result;
    }
}
