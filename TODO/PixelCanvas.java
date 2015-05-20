/*
 * Copyright (C) 2015 by Stefan Rothe
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

import java.util.Stack;

public class PixelCanvas {

    /**
     * Draws a circle. The circle is drawn using the current color, line width, and transformation. Has no effect if the
     * specified radius is not positive.
     *
     * @param x the x coordinate of the circle's center
     * @param y the y coordinate of the circle's center
     * @param radius the circle's radius
     *
     * @since 1.0
     */
    public void drawCircle(final int x, final int y, final int radius) {
        if (radius > 0) {
            imp.drawEllipse(x - radius, y - radius, 2 * radius, 2 * radius);
        }
    }

    /**
     * Draws an ellipse. The ellipse is drawn using the current color, line width, and transformation. Has no effect if
     * the specified radii are not positive.
     *
     * @param x the x coordinate of the ellipse's centre
     * @param y the y coordinate of the ellipse's centre
     * @param rx the horizontal radius of the ellipse
     * @param ry the vertical radius of the ellipse
     *
     * @since 2.0
     */
    public void drawEllipse(final int x, final int y, final int rx, final int ry) {
        if (rx > 0 && ry > 0) {
            imp.drawEllipse(x - rx, y - ry, 2 * rx, 2 * ry);
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
    public void drawImage(final int x, final int y, final Image image) {
        if (image != null) {
            imp.drawImage(x, y, image.getImp(), 255);
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
    public void drawImage(final int x, final int y, final Image image, final int alpha) {
        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        if (image != null && alpha > 0) {
            imp.drawImage(x, y, image.getImp(), alpha);
        }
    }

    /**
     * Draws an image. The image is drawn using the current transformation. The image is aligned relative to the
     * specified coordinates (<tt>x</tt>, <tt>y</tt>). Has no effect if <tt>image</tt> is <tt>null</tt>.
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
            imp.drawImage(alignment.alignX(x, image.getWidth()), alignment.alignY(y, image.getHeight()),
                          image.getImp(), 255);
        }
    }

    /**
     * Draws an image. The image is drawn using the current transformation. The image is aligned relative to the
     * specified coordinates (<tt>x</tt>, <tt>y</tt>). The image is drawn with a translucency effect specified by the
     * alpha value. Specify an alpha value of 255 for a completely opaque image, and alpha value of 0 for a completely
     * transparent image. Has no effect if
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
            imp.drawImage(alignment.alignX(x, image.getWidth()), alignment.alignY(y, image.getHeight()),
                          image.getImp(), alpha);
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
     * @since 1.0
     */
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        imp.drawLine(x1, y1, x2, y2);
    }

    /**
     * Draws a polygon. The polygon is drawn using the current color, line width, and transformation. The polygon is
     * defined by a sequence of coordinate pairs specifiying the vertices of the polygon. For example, the code
     * <pre><code>drawPolygon(x1, y1, x2, y2, x3, y3);</code></pre> will draw a triangle with the vertices (x1, y2),
     * (x2, y2), and (x3, y3).
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

        imp.drawPolygon(points);
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
            imp.drawRectangle(x, y, width, height);
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
     * @param alignment specifies how to align the rectangle relative to (<tt>x</tt>, <tt>y</tt>)
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
            imp.drawRectangle(alignment.alignX(x, width), alignment.alignY(y, height), width, height);
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
    public void drawText(final int x, final int y, final String text) {
        if (text != null && !text.isEmpty()) {
            imp.drawText(x, y, text);
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
     * @param alignment specifies how to align the text relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.0
     */
    public final void drawText(final int x, final int y, final String text, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (text != null && !text.isEmpty()) {
            imp.drawText(alignment.alignX(x, imp.textWidth(text)), alignment.alignY(y, imp.textHeight(text)), text);
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
    public void fillCircle(final int x, final int y, final int radius) {
        if (radius > 0) {
            imp.fillEllipse(x - radius, y - radius, 2 * radius, 2 * radius);
        }
    }

    /**
     * Draws a filled ellipse. The ellipse is drawn using the current color, line width, and transformation. Has no
     * effect if the specified radii are not positive.
     *
     * @param x the x coordinate of the ellipse's center
     * @param y the y coordinate of the ellipse's center
     * @param rx the horizontal radius of the ellipse
     * @param ry the vertical radius of the ellipse
     *
     * @since 2.0
     */
    public void fillEllipse(final int x, final int y, final int rx, final int ry) {
        if (rx > 0 && ry > 0) {
            imp.fillEllipse(x - rx, y - ry, 2 * rx, 2 * ry);
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
            imp.fillRectangle(x, y, width, height);
        }
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
            if (getPixel(x, y).equals(oldColor)) {
                setPixel(x, y, newColor);
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
     * Returns the color of a pixel. Returns the color of the pixel at the coordinates (<tt>x</tt>, <tt>y</tt>). Returns
     * {@link ch.jeda.ui.Color#TRANSPARENT} if the coordinates do not reference a pixel inside the canvas.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the color of the pixel at (<tt>x</tt>, <tt>y</tt>)
     *
     * @see #setPixel(int, int, ch.jeda.ui.Color)
     * @since 2.0
     */
    public Color getPixel(final int x, final int y) {
        if (contains(x, y)) {
            return imp.getPixel(x, y);
        }
        else {
            return Color.TRANSPARENT;
        }
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
     * @see #getPixel(int, int)
     * @since 2.0
     */
    public void setPixel(final int x, final int y, final Color color) {
        if (color == null) {
            throw new NullPointerException("color");
        }

        if (contains(x, y)) {
            imp.setPixel(x, y, color);
        }
    }
}
