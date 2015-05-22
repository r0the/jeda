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

import ch.jeda.platform.CanvasImp;
import java.util.EnumMap;

/**
 * Represents a drawing surface. It provides methods to draw geometric primitives and images.
 *
 * <p>
 * A canvas has some attributes that influence the drawing operations:
 * <ul>
 * <li> <b>anti-aliasing</b>:
 * <li> <b>color</b>: The color used to draw geometric shapes and text. Initially, the color is black. The color can be
 * changed with {@link #setColor(ch.jeda.ui.Color)}.
 * <li> <b>line width</b>: the line width used to draw geometric shapes in centimeters. Initially, the line width is 1.
 * The line width can be changed with {@link #setLineWidth(double)}.
 * <li> <b>text size</b>: the size of the text in centimeters. Initially, the text size is 16. The text size can be
 * changed with {@link #setTextSize(float)}.
 * <li> <b>typeface</b>: The typeface (font family) used to render text.
 * </ul>
 * <strong>Example:</strong>
 * <pre><code> Canvas canvas = new Canvas(100, 100);
 * canvas.setColor(Color.RED);
 * canvas.fillCircle(2, 2, 1);</code></pre>
 *
 * @since 1.0
 * @version 4
 */
public class Canvas {

    private static final float DEFAULT_LINE_WIDTH = 1f;
    private static final float DEFAULT_TEXT_SIZE = 16f;
    private static final Color DEFAULT_FOREGROUND = Color.BLACK;
    private final EnumMap<Icon, Image> icons;
    private CanvasImp imp;
    private Alignment alignment;
    private boolean antiAliasing;
    private Color color;
    private DisplayAdaption displayAdaption;
    private float lineWidth;
    private float textSize;
    private Typeface typeface;
    private float scale;
    private float tx;
    private float ty;

    Canvas(final CanvasImp imp) {
        icons = new EnumMap<Icon, Image>(Icon.class);
        alignment = Alignment.BOTTOM_LEFT;
        antiAliasing = false;
        color = DEFAULT_FOREGROUND;
        lineWidth = DEFAULT_LINE_WIDTH;
        textSize = DEFAULT_TEXT_SIZE;
        typeface = Typeface.SANS_SERIF;
        this.imp = imp;
        displayAdaption = new DisplayAdaption(imp.getDpi(), imp.getHeight());
        // TODO
        imp.setAntiAliasing(antiAliasing);
        imp.setColor(color);
        imp.setLineWidth(lineWidth);
        imp.setTextSize(textSize * displayAdaption.textSizeFactor);
        imp.setTypeface(typeface.imp);
        System.out.println("Jeda DPI: " + imp.getDpi());
        setWorldTransformation(100f, 0f, 0f);
    }

    /**
     * Copies the contents of another canvas to this canvas.
     *
     * @param canvas the other canvas
     *
     * @since 1.0
     */
    public void copyFrom(final Canvas canvas) {
        imp.resetTransformation();
        imp.drawCanvas(0, 0, canvas.imp);
    }

    /**
     * Draws a circle. The circle is drawn using the current color and line width. Has no effect if the specified radius
     * is not positive.
     *
     * @param centerX the x coordinate of the circle's center
     * @param centerY the y coordinate of the circle's center
     * @param radius the circle's radius
     *
     * @since 1.0
     */
    public void drawCircle(final double centerX, final double centerY, final double radius) {
        drawCircle((float) centerX, (float) centerY, (float) radius);
    }

    /**
     * Draws a circle. The circle is drawn using the current color and line width. Has no effect if the specified radius
     * is not positive.
     *
     * @param centerX the x coordinate of the circle's center
     * @param centerY the y coordinate of the circle's center
     * @param radius the circle's radius
     *
     * @since 2.0
     */
    public void drawCircle(final float centerX, final float centerY, final float radius) {
        if (radius > 0f) {
            imp.drawEllipse(toPixelX(centerX), toPixelY(centerY), toPixel(radius), toPixel(radius));
        }
    }

    /**
     * Draws an ellipse. The ellipse is drawn using the current color and line width. Has no effect if the specified
     * radii are not positive.
     *
     * @param centerX the x coordinate of the ellipse's center
     * @param centerY the y coordinate of the ellipse's center
     * @param radiusX the horizontal radius of the ellipse
     * @param radiusY the vertical radius of the ellipse
     *
     * @since 2.0
     */
    public void drawEllipse(final double centerX, final double centerY, final double radiusX, final double radiusY) {
        drawEllipse((float) centerX, (float) centerY, (float) radiusX, (float) radiusY);
    }

    /**
     * Draws an ellipse. The ellipse is drawn using the current color and line width. Has no effect if the specified
     * radii are not positive.
     *
     * @param centerX the x coordinate of the ellipse's center
     * @param centerY the y coordinate of the ellipse's center
     * @param radiusX the horizontal radius of the ellipse
     * @param radiusY the vertical radius of the ellipse
     *
     * @since 2.0
     */
    public void drawEllipse(final float centerX, final float centerY, final float radiusX, final float radiusY) {
        if (radiusX > 0f && radiusY > 0f) {
            imp.drawEllipse(toPixelX(centerX), toPixelY(centerY), toPixel(radiusX), toPixel(radiusY));
        }
    }

    void drawIcon(final double x, final double y, final Icon icon) {
        if (!icons.containsKey(icon)) {
            icons.put(icon, new Image("res:jeda/icon/" + icon.name().toLowerCase() + "_" + displayAdaption.iconSize + ".png"));
        }

        drawImage((float) x, (float) y, icons.get(icon), 255);
    }

    /**
     * Draws an image. The image is positioned relative to (x, y) according to the current alignment. Has no effect if
     * <code>image</code> is <code>null</code>.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param image the image to draw
     *
     * @since 1.0
     */
    public void drawImage(final double x, final double y, final Image image) {
        drawImage((float) x, (float) y, image, 255);
    }

    /**
     * Draws an image. The image is positioned relative to (x, y) according to the current alignment. Has no effect if
     * <code>image</code> is <code>null</code>.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param image the image to draw
     *
     * @since 2.0
     */
    public void drawImage(final float x, final float y, final Image image) {
        drawImage(x, y, image, 255);
    }

    /**
     * Draws an image with translucency. The image is positioned relative to (x, y) according to the current alignment.
     * The image is drawn with a translucency effect specified by the alpha value. Specify an alpha value of 255 for a
     * completely opaque image, and alpha value of 0 for a completely transparent image. Has no effect if
     * <code>image</code> is <code>null</code>.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param image the image to draw
     * @param alpha the alpha value
     *
     * @since 1.0
     */
    public void drawImage(final double x, final double y, final Image image, final int alpha) {
        drawImage((float) x, (float) y, image, alpha);
    }

    /**
     * Draws an image with translucency. The image is positioned relative to (x, y) according to the current alignment.
     * The image is drawn with a translucency effect specified by the alpha value. Specify an alpha value of 255 for a
     * completely opaque image, and alpha value of 0 for a completely transparent image. Has no effect if
     * <code>image</code> is <code>null</code>.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param image the image to draw
     * @param alpha the alpha value
     *
     * @since 2.0
     */
    void drawImage(float x, float y, final Image image, final int alpha) {
        if (image != null && image.isAvailable()) {
            x = toPixelX(x);
            y = toPixelY(y);
            final int width = image.getWidth();
            final int height = image.getHeight();
            imp.drawImage(alignX(x, width), alignY(y, height), width, height, image.getImp(), alpha);
        }
    }

    /**
     * Draws a straight line. The line is drawn from the coordinates (<code>x1</code>, <code>y1</code>) to the
     * coordinates (<code>x2</code>, <code>y2</code>) with the current color, line width, and transformation.
     *
     * @param x1 the x coordinate of the line's start point
     * @param y1 the y coordinate of the lines' start point
     * @param x2 the x coordinate of the line's end point
     * @param y2 the y coordinate of the line's end point
     * @deprecated Use {@link #drawPolyline(double...)} instead.
     *
     * @since 1.0
     */
    public void drawLine(final double x1, final double y1, final double x2, final double y2) {
        drawPolyline(x1, y1, x2, y2);
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
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        imp.drawPolygon(convertPoints(points));
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
     * @since 2.0
     */
    public void drawPolygon(final float... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        imp.drawPolygon(convertPoints(points));
    }

    /**
     * Draws a a connected series of line segments. The polyline is drawn from the coordinates with the current color,
     * line width, and transformation. The polyline is defined by a sequence of coordinate pairs specifiying the
     * endpoints of the line segments. For example, the code
     * <pre><code>drawPolyline(x1, y1, x2, y2, x3, y3);</code></pre> will draw two line segments: (x1, y1) to (x2, y2)
     * and (x2, y2) to (x3, y3).
     *
     * @param points the points of the polyline as sequence of coordinate pairs
     *
     * @since 2.0
     */
    public void drawPolyline(final double... points) {
        imp.drawPolyline(convertPoints(points));
    }

    /**
     * Draws a a connected series of line segments. The polyline is drawn from the coordinates with the current color,
     * line width, and transformation. The polyline is defined by a sequence of coordinate pairs specifiying the
     * endpoints of the line segments. For example, the code
     * <pre><code>drawPolyline(x1, y1, x2, y2, x3, y3);</code></pre> will draw two line segments: (x1, y1) to (x2, y2)
     * and (x2, y2) to (x3, y3).
     *
     * @param points the points of the polyline as sequence of coordinate pairs
     *
     * @since 2.0
     */
    public void drawPolyline(final float... points) {
        imp.drawPolyline(convertPoints(points));
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current color, line width, and transformation. The bottom
     * left corner of the rectangle is positioned at the coordinates (<code>x</code>, <code>y</code>). Has no effect if
     * <code>width</code> or <code>height</code> are not positive.
     *
     * @param x the x coordinate of the rectangle's bottom left corner
     * @param y the y coordinate of the rectangle's bottom left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1.0
     */
    public void drawRectangle(final double x, final double y, final double width, final double height) {
        drawRectangle((float) x, (float) y, (float) width, (float) height);
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current color, line width, and transformation. The bottom
     * left corner of the rectangle is positioned at the coordinates (<code>x</code>, <code>y</code>). Has no effect if
     * <code>width</code> or <code>height</code> are not positive.
     *
     * @param x the x coordinate of the rectangle's bottom left corner
     * @param y the y coordinate of the rectangle's bottom left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 2.0
     */
    public void drawRectangle(float x, float y, float width, float height) {
        x = toPixelX(x);
        y = toPixelY(y);
        width = toPixel(width);
        height = toPixel(height);
        imp.drawRectangle(alignX(x, width), alignY(y, height), width, height);
    }

    /**
     * Draws a shadow around a circle.
     *
     * @param x the horizontal coordinate of the circle's center
     * @param y the vertical coordinate of the circle's center
     * @param radius the radius of the circle
     *
     * @since 2.0
     */
    public void drawShadowCircle(float x, float y, float radius) {
        x = toPixelX(x);
        y = toPixelY(y);
        radius = toPixel(radius);
        final int SHADOW_MAX_OPACITY = 80;
        final int opacityStep = SHADOW_MAX_OPACITY / displayAdaption.shadowThickness;
        int opacity = SHADOW_MAX_OPACITY;
        for (int i = 0; i < displayAdaption.shadowThickness; i++) {
            imp.setColor(new Color(0, 0, 0, opacity));
            imp.drawEllipse(x, y, radius + i, radius + i);
            opacity = opacity - opacityStep;
        }

        imp.setColor(color);
    }

    /**
     * Draws a shadow around a rectangle.
     *
     * @param x the horizontal coordinate of the rectangle
     * @param y the vertical coordinate of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 2.0
     */
    public void drawShadowRectangle(float x, float y, float width, float height) {
        width = toPixel(width);
        height = toPixel(height);
        x = alignX(toPixelX(x), width);
        y = alignY(toPixelY(y), height);
        final int SHADOW_MAX_OPACITY = 80;
        final int opacityStep = SHADOW_MAX_OPACITY / displayAdaption.shadowThickness;
        int opacity = SHADOW_MAX_OPACITY;
        for (int i = 0; i < displayAdaption.shadowThickness; i++) {
            imp.setColor(new Color(0, 0, 0, opacity));
            imp.drawRectangle(x - i, y - i, width + 2 * i, height + 2 * i);
            opacity = opacity - opacityStep;
        }

        imp.setColor(color);
    }

    /**
     * Draws a text. The text is drawn using the current color, transformation, and text size. The bottom left corner of
     * the text is positioned at the coordinates (<code>x</code>, <code>y</code>). Has no effect if <code>text</code> is
     * <code>null</code> or empty.
     *
     * @param x the x coordinate of the bottom left corner
     * @param y the y coordinate of the bottom left corner
     * @param text the text to draw
     *
     * @since 1.0
     */
    public void drawText(final double x, final double y, final String text) {
        drawText((float) x, (float) y, text);
    }

    /**
     * Draws a text. The text is drawn using the current color, transformation, and font size. The bottom left corner of
     * the text is positioned at the coordinates (<code>x</code>, <code>y</code>). Has no effect if <code>text</code> is
     * <code>null</code> or empty.
     *
     * @param x the x coordinate of the bottom left corner
     * @param y the y coordinate of the bottom left corner
     * @param text the text to draw
     *
     * @since 2.0
     */
    public void drawText(float x, float y, final String text) {
        if (text != null && !text.isEmpty()) {
            x = toPixelX(x);
            y = toPixelY(y);
            final float width = imp.textWidth(text);
            final float height = imp.textHeight(text);
            imp.drawText(alignX(x, width), alignY(y, height), text);
        }
    }

    /**
     * @deprecated
     */
    void drawText(final double x, final double y, final String text, final Alignment alignment) {
        setAlignment(alignment);
        drawText(x, y, text);
    }

    /**
     * Fills the entire canvas with the current color.
     *
     * @since 1.0
     */
    public void fill() {
        imp.fill();
    }

    /**
     * Draws a filled a circle. The circle is drawn using the current color and transformation. Has no effect if the
     * specified radius is not positive.
     *
     * @param centerX the x coordinate of the circle's center
     * @param centerY the y coordinate of the circle's center
     * @param radius the circle's radius
     *
     * @since 1.0
     */
    public void fillCircle(final double centerX, final double centerY, final double radius) {
        fillCircle((float) centerX, (float) centerY, (float) radius);
    }

    /**
     * Draws a filled a circle. The circle is drawn using the current color and transformation. Has no effect if the
     * specified radius is not positive.
     *
     * @param centerX the x coordinate of the circle's center
     * @param centerY the y coordinate of the circle's center
     * @param radius the circle's radius
     *
     * @since 2.0
     */
    public void fillCircle(final float centerX, final float centerY, final float radius) {
        imp.fillEllipse(toPixelX(centerX), toPixelY(centerY), toPixel(radius), toPixel(radius));
    }

    /**
     * Draws a filled ellipse. The ellipse is drawn using the current color, line width, and transformation. Has no
     * effect if the specified radii are not positive.
     *
     * @param centerX the x coordinate of the ellipse's center
     * @param centerY the y coordinate of the ellipse's center
     * @param radiusX the horizontal radius of the ellipse
     * @param radiusY the vertical radius of the ellipse
     *
     * @since 2.0
     */
    public void fillEllipse(final double centerX, final double centerY, final double radiusX, final double radiusY) {
        fillEllipse((float) centerX, (float) centerY, (float) radiusX, (float) radiusY);
    }

    /**
     * Draws a filled ellipse. The ellipse is drawn using the current color, line width, and transformation. Has no
     * effect if the specified radii are not positive.
     *
     * @param centerX the x coordinate of the ellipse's center
     * @param centerY the y coordinate of the ellipse's center
     * @param radiusX the horizontal radius of the ellipse
     * @param radiusY the vertical radius of the ellipse
     *
     * @since 2.0
     */
    public void fillEllipse(final float centerX, final float centerY, final float radiusX, final float radiusY) {
        imp.fillEllipse(toPixelX(centerX), toPixelY(centerY), toPixel(radiusX), toPixel(radiusY));
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
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.imp.fillPolygon(convertPoints(points));
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
     * @since 2.0
     */
    public void fillPolygon(final float... points) {
        if (points.length < 6 || points.length % 2 == 1) {
            throw new IllegalArgumentException("points");
        }

        this.imp.fillPolygon(convertPoints(points));
    }

    /**
     * Draws a filled rectangle. The rectangle is drawn using the current color and transformation. The bottom left
     * corner of the rectangle is positioned at the coordinates (<code>x</code>, <code>y</code>). Has no effect if
     * <code>width</code> or <code>height</code> are not positive.
     *
     * @param x the x coordinate of the rectangle's bottom left corner
     * @param y the y coordinate of the rectangle's bottom left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1.0
     */
    public void fillRectangle(final double x, final double y, final double width, final double height) {
        fillRectangle((float) x, (float) y, (float) width, (float) height);
    }

    /**
     * Draws a filled rectangle. The rectangle is drawn using the current color and transformation. The bottom left
     * corner of the rectangle is positioned at the coordinates (<code>x</code>, <code>y</code>). Has no effect if
     * <code>width</code> or <code>height</code> are not positive.
     *
     * @param x the x coordinate of the rectangle's bottom left corner
     * @param y the y coordinate of the rectangle's bottom left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 2.0
     */
    public void fillRectangle(float x, float y, float width, float height) {
        x = toPixelX(x);
        y = toPixelY(y);
        width = toPixel(width);
        height = toPixel(height);
        imp.fillRectangle(alignX(x, width), alignY(y, height), width, height);
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
        return color;
    }

    /**
     * @deprecated Use {@link #getTextSize()} instead.
     */
    public int getFontSize() {
        return (int) textSize;
    }

    /**
     * Returns the height of this canvas in centimeters.
     *
     * @return the height of this canvas in centimeters
     *
     * @since 1.0
     */
    public float getHeight() {
        return imp.getHeight() / scale;
    }

    /**
     * Returns the current line width in pixels.
     *
     * @return current line width
     *
     * @see #setLineWidth(double)
     * @since 1.0
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Returns the current text size.
     *
     * @return current text size
     *
     * @see #setTextSize(double)
     * @since 1.0
     */
    public float getTextSize() {
        return textSize;
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
        return typeface;
    }

    /**
     * Returns the width of this canvas in centimeters.
     *
     * @return the width of this canvas in centimeters
     *
     * @since 1.0
     */
    public float getWidth() {
        return imp.getWidth() / scale;
    }

    /**
     * Checks is anti-aliasing is enabled.
     *
     * @return <code>true</code> if anti-aliasing is enabled, otherwise <code>false</code>
     *
     * @see #setAntiAliasing(boolean)
     * @since 1.0
     */
    public boolean isAntiAliasing() {
        return antiAliasing;
    }

    public void setAlignment(Alignment alignment) {
        if (alignment != null) {
            this.alignment = alignment;
        }
    }

    /**
     * Enables or disables the anti-aliasing filter. The borders of drawn text or shapes may not appear "smooth". This
     * effect is called <a href="http://en.wikipedia.org/wiki/Jaggies" target="_blank">Jaggies</a>. To counter this
     * effect, an <a href=http://en.wikipedia.org/wiki/Anti-aliasing_filter" target="_blank">anti-aliasing filter</a> is
     * used when rendering the text or shapes.
     *
     * @param antiAliasing <code>true</code> to enable the anti-aliasing filter, <code>false</code> to disable it.
     * @since 1.0
     */
    public void setAntiAliasing(final boolean antiAliasing) {
        if (antiAliasing != this.antiAliasing) {
            this.antiAliasing = antiAliasing;
            imp.setAntiAliasing(antiAliasing);
        }
    }

    /**
     * Sets the drawing color. The value set by this method is applied to all subsequent <code>draw...</code> and
     * <code>fill...</code> operations.
     *
     * @param color new drawing color.
     * @throws NullPointerException if <code>color</code> is <code>null</code>
     *
     * @see #getColor()
     * @since 1.0
     */
    public void setColor(final Color color) {
        if (color == null) {
            throw new NullPointerException("color");
        }

        if (!color.equals(this.color)) {
            this.color = color;
            imp.setColor(color);
        }
    }

    /**
     * @deprecated Use {@link #setTextSize(double)} instead.
     */
    public void setFontSize(final int size) {
        setTextSize(size);
    }

    /**
     * Sets the line width. The line width set by this method is applied to all subsequent <code>draw...</code>
     * operations. Set 0 for drawing hairlines independent of the transformation.
     *
     * @param lineWidth the new line width
     * @throws IllegalArgumentException if <code>lineWidth</code> is negative
     *
     * @see #getLineWidth()
     * @since 1.0
     */
    public void setLineWidth(final double lineWidth) {
        if (lineWidth < 0.0) {
            throw new IllegalArgumentException("lineWidth");
        }

        this.lineWidth = (float) lineWidth;
        imp.setLineWidth(this.lineWidth);
    }

    /**
     * Sets the text size. The text size set by this method is applied to all subsequent <code>drawText(...)</code>
     * operations.
     *
     * @param size the new text size
     * @throws IllegalArgumentException if <code>size</code> is not positive
     *
     * @see #getTextSize()
     * @since 2.0
     */
    public void setTextSize(final double size) {
        setTextSize((float) size);
    }

    /**
     * Sets the text size. The text size set by this method is applied to all subsequent <code>drawText(...)</code>
     * operations.
     *
     * @param size the new text size
     * @throws IllegalArgumentException if <code>size</code> is not positive
     *
     * @see #getTextSize()
     * @since 2.0
     */
    public void setTextSize(final float size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size");
        }

        if (textSize != size) {
            textSize = size;
            imp.setTextSize(textSize * displayAdaption.textSizeFactor);
        }
    }

    /**
     * Sets the typeface to be used to draw text.
     *
     * @param typeface the typeface to be used to draw text
     * @throws NullPointerException if <code>typeface</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>typeface.isAvailable()</code> is <code>false</code>
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

        if (!typeface.equals(this.typeface)) {
            this.typeface = typeface;
            imp.setTypeface(typeface.imp);
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
        return new Image(imp.takeSnapshot());
    }

    /**
     * Returns the height of a text in pixels. Returns the height in pixels of the specified text given the current font
     * size. Returns zero if <code>text</code> is <code>null</code> or empty.
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
            return imp.textHeight(text);
        }
    }

    /**
     * Returns the width of a text in pixels. Returns the width in pixels of the specified text given the current font
     * size. Returns zero if <code>text</code> is <code>null</code> or empty.
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
            return imp.textWidth(text);
        }
    }

    final void setWorldTransformation(final float scale, final float tx, final float ty) {
        this.scale = displayAdaption.screenScale * 100 / scale;
        this.tx = -tx * this.scale;
        this.ty = ty * this.scale;
    }

    void worldBegin(final boolean pinned, final float tx, final float ty, final float angleRad) {
        imp.translate(toPixel(tx), -toPixel(ty));
        imp.rotateRad(-angleRad, toPixelX(0), toPixelY(0));

    }

    void worldEnd() {
        imp.resetTransformation();
    }

    private float[] convertPoints(double[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < values.length; i = i + 2) {
            result[i] = toPixelX((float) values[i]);
            result[i + 1] = toPixelY((float) values[i + 1]);
        }

        return result;
    }

    private float[] convertPoints(float[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < values.length; i = i + 2) {
            result[i] = toPixelX(values[i]);
            result[i + 1] = toPixelY(values[i + 1]);
        }

        return result;
    }

    float alignX(final float x, final float w) {
        switch (alignment.horiz) {
            case MAX:
                return x - w;
            case MIDDLE:
                return x - w / 2f;
            case MIN:
            default:
                return x;
        }
    }

    float alignY(final float y, final float h) {
        switch (alignment.vert) {
            case MAX:
                return y;
            case MIDDLE:
                return y - h / 2f;
            case MIN:
            default:
                return y - h;
        }
    }

    float lengthToWorld(final int pixels) {
        return pixels / scale;
    }

    float toCanvasX(final float x) {
        return x / displayAdaption.screenScale;
    }

    float toCanvasY(final float y) {
        return (displayAdaption.pixelsY - y) / displayAdaption.screenScale;
    }

    private float toPixel(final float len) {
        return len * scale;
    }

    private float toPixelX(final float x) {
        return x * scale + tx;
    }

    private float toPixelY(final float y) {
        return displayAdaption.pixelsY - y * scale + ty;
    }
}
