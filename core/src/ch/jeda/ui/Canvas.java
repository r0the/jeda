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

import ch.jeda.Jeda;
import ch.jeda.JedaInternal;
import ch.jeda.Log;
import ch.jeda.platform.CanvasImp;
import java.util.EnumMap;

/**
 * Represents a drawing surface. It provides methods to draw geometric primitives and images. The canvas uses a
 * mathematical coordinate system: the origin is the lower left corner of the drawing area. The length unit are
 * density-independent pixels (dp). A dp equals 1/160th of an inch.
 * <p>
 * A canvas has some attributes that influence the drawing operations:
 * <ul>
 * <li> <b>alignment</b>: The alignment defines how rectangular forms are aligned relative to the provided coordinates.
 * Use {@link #setAlignment(ch.jeda.ui.Alignment)} to change the alignment. The default alignment is
 * {@link Alignment#BOTTOM_LEFT}.
 * <li> <b>color</b>: The color used to draw geometric shapes and text. The default color is {@link Color#BLACK}. The
 * color can be changed with {@link #setColor(ch.jeda.ui.Color)}.
 * <li> <b>line width</b>: the line width used to draw geometric shapes. The default line width is 1. The line width can
 * be changed with {@link #setLineWidth(double)}.
 * <li> <b>opacity</b>: the opacity used to draw images. The default opacity is 255 (fully opaque). The opacity can be
 * changed with {@link #setOpacity(int)}.
 * <li> <b>text size</b>: the size of the text. The default text size is 16. The text size can be changed with
 * {@link #setTextSize(float)}.
 * <li> <b>typeface</b>: The typeface (font family) used to render text. The default typeface is
 * {@link Typeface#SANS_SERIF}.
 * </ul>
 * <strong>Example:</strong>
 * <pre><code> Canvas canvas = new Canvas(300, 250);
 * canvas.setColor(Color.RED);
 * canvas.fillCircle(200, 150, 100);</code></pre>
 *
 * @since 1.0
 * @version 5
 */
public class Canvas {

    private static final Alignment DEFAULT_ALIGNMENT = Alignment.BOTTOM_LEFT;
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_OPACITY = 255;
    private static final float DEFAULT_TEXT_SIZE = 16f;
    private static final Typeface DEFAULT_TYPEFACE = Typeface.SANS_SERIF;
    private static final float DEFAULT_LINE_WIDTH = 1f;
    private final EnumMap<Icon, Image> icons;
    private final float canvasToDevice;
    private final float deviceToCanvas;
    private final float pixelsY;
    private CanvasImp imp;
    private Alignment alignment;
    private boolean antiAliasing;
    private Color color;
    private DisplayAdaption displayAdaption;
    private float lineWidth;
    private int opacity;
    private float textSize;
    private Typeface typeface;
    private float sx;
    private float slx;
    private float sly;
    private float sy;
    private float tx;
    private float ty;

    /**
     * Constructs a new canvas. <code>width</code> and <code>height</code> are specified in density-independent pixels.
     * The actual size of the canvas in pixels is dependent on the pixel density of the device.
     *
     * @param width width of the canvas in dp
     * @param height height of the canvas in dp
     *
     * @since 1.0
     */
    public Canvas(final int width, final int height) {
        this(JedaInternal.createCanvasImp(Jeda.getDisplayMetrics().dpToPx(width),
                                          Jeda.getDisplayMetrics().dpToPx(height)));
    }

    Canvas(final CanvasImp imp) {
        this.imp = imp;

        icons = new EnumMap<Icon, Image>(Icon.class);
        final int dpi = Jeda.getDisplayMetrics().getDpi();
        canvasToDevice = dpi / 160f;
        deviceToCanvas = 160f / dpi;
        pixelsY = imp.getHeight();
        // Default drawing properties
        alignment = DEFAULT_ALIGNMENT;
        antiAliasing = true;
        color = DEFAULT_COLOR;
        lineWidth = DEFAULT_LINE_WIDTH;
        opacity = DEFAULT_OPACITY;
        textSize = DEFAULT_TEXT_SIZE;
        typeface = DEFAULT_TYPEFACE;

        // TODO
        displayAdaption = new DisplayAdaption(Jeda.getDisplayMetrics().getDpi(), imp.getHeight());

        imp.setAntiAliasing(antiAliasing);
        imp.setColor(color);
        imp.setLineWidth(lineWidth * canvasToDevice);
        imp.setTextSize(textSize * canvasToDevice);
        imp.setTypeface(typeface.imp);
        setWorldTransformation(1f, 1f, 0f, 0f);
    }

    /**
     * @deprecated Use {@link #drawCanvas(double, double, ch.jeda.ui.Canvas)} instead.
     */
    public void copyFrom(final Canvas canvas) {
        imp.resetTransformation();
        imp.drawCanvas(0, 0, canvas.imp, 255);
    }

    /**
     * Copies the contents of another canvas to this canvas. This method uses the current alignment and opacity.
     *
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @param canvas the other canvas
     *
     * @since 2.1
     */
    public void drawCanvas(final double x, final double y, final Canvas canvas) {
        drawCanvas((float) x, (float) y, canvas);
    }

    /**
     * Copies the contents of another canvas to this canvas. This method uses the current alignment and opacity.
     *
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @param canvas the other canvas
     *
     * @since 2.1
     */
    public void drawCanvas(float x, float y, final Canvas canvas) {
        if (canvas == null) {
            Log.d("Ignoring call with null canvas.");
        }
        else if (canvas == this) {
            Log.d("Ignoring drawing of canvas on itself.");
        }
        else if (opacity != 0) {
            x = x * sx + tx;
            y = y * sy + ty;
            final float width = canvas.getWidth() * canvasToDevice;
            final float height = canvas.getHeight() * canvasToDevice;
            imp.drawCanvas(alignX(x, width), alignY(y, height), canvas.imp, opacity);
        }
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
        if (radius <= 0f) {
            Log.d("Ignoring Canvas.drawCircle() with non-positive radius.");
        }
        else {
            imp.drawEllipse(centerX * sx + tx, centerY * sy + ty, radius * slx, radius * sly);
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
        if (radiusX <= 0f || radiusY <= 0f) {
            Log.d("Ignoring call with non-positive radius.");
        }
        else {
            imp.drawEllipse(centerX * sx + tx, centerY * sy + ty, radiusX * slx, radiusY * sly);
        }
    }

    void drawIcon(final double x, final double y, final Icon icon) {
        if (!icons.containsKey(icon)) {
            icons.put(icon, new Image("res:jeda/icon/" + icon.name().toLowerCase() + "_" + displayAdaption.iconSize + ".png"));
        }

        drawImage((float) x, (float) y, icons.get(icon), 255);
    }

    /**
     * Draws an image. The image is drawn using the current alignment and opacity. The image is positioned relative to
     * (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if <code>image</code> is
     * <code>null</code>.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param image the image to draw
     *
     * @since 1.0
     */
    public void drawImage(final double x, final double y, final Image image) {
        drawImage((float) x, (float) y, image, opacity);
    }

    /**
     * Draws an image. The image is drawn using the current alignment and opacity. The image is positioned relative to
     * (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if <code>image</code> is
     * <code>null</code>.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param image the image to draw
     *
     * @since 2.0
     */
    public void drawImage(float x, float y, final Image image) {
        if (image == null || !image.isAvailable()) {
            Log.d("Ignoring call with null or unavailable image.");
        }
        else if (opacity != 0) {
            x = x * sx + tx;
            y = y * sy + ty;
            final float width = image.getWidth();
            final float height = image.getHeight();
            imp.drawImage(alignX(x, width), alignY(y, height), width, height, image.getImp(), opacity);
        }
    }

    /**
     * @deprecated Use {@link #setOpacity(int)} followed by {@link #drawImage(double, double, ch.jeda.ui.Image)}
     * instead.
     */
    public void drawImage(final double x, final double y, final Image image, final int alpha) {
        drawImage((float) x, (float) y, image, alpha);
    }

    /**
     * @deprecated Use {@link #setOpacity(int)} followed by {@link #drawImage(float, float, ch.jeda.ui.Image)} instead.
     */
    void drawImage(float x, float y, final Image image, final int alpha) {
        if (image != null && image.isAvailable()) {
            x = x * sx + tx;
            y = y * sy + ty;
            final float width = image.getWidth();
            final float height = image.getHeight();
            imp.drawImage(alignX(x, width), alignY(y, height), width, height, image.getImp(), alpha);
        }
    }

    /**
     * Draws an image. The image is drawn using the current alignment and opacity. The image is positioned relative to
     * (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if <code>image</code> is
     * <code>null</code>.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param width the width of the image
     * @param height the height of the image
     * @param image the image to draw
     *
     * @since 2.0
     */
    public void drawImage(double x, double y, double width, double height, final Image image) {
        drawImage((float) x, (float) y, (float) width, (float) height, image);
    }

    /**
     * Draws an image. The image is positioned relative to (<code>x</code>, <code>y</code>) according to the current
     * alignment. Has no effect if <code>image</code> is <code>null</code> or unavailable.
     *
     * @param x the x coordinate of the image
     * @param y the y coordinate of the image
     * @param width the width of the image
     * @param height the height of the image
     * @param image the image to draw
     *
     * @since 2.0
     */
    public void drawImage(float x, float y, float width, float height, final Image image) {
        if (image == null || !image.isAvailable()) {
            Log.d("Ignoring call with null or unavailable image.");
        }
        else if (opacity != 0) {
            x = x * sx + tx;
            y = y * sy + ty;
            width = width * sly;
            height = height * sly;
            imp.drawImage(alignX(x, width), alignY(y, height), width, height, image.getImp(), opacity);
        }
    }

    /**
     * Draws a straight line. The line is drawn from the coordinates (<code>x1</code>, <code>y1</code>) to the
     * coordinates (<code>x2</code>, <code>y2</code>) with the current color and line width.
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
     * Draws a polygon. The polygon is drawn using the current color and line width. The polygon is defined by a
     * sequence of coordinate pairs specifiying the corners of the polygon. For example, the code
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
     * Draws a polygon. The polygon is drawn using the current color and line width. The polygon is defined by a
     * sequence of coordinate pairs specifiying the corners of the polygon. For example, the code
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
     * Draws a a connected series of line segments. The polyline is drawn with the current color and line width. The
     * polyline is defined by a sequence of coordinate pairs specifiying the endpoints of the line segments. For
     * example, the code
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
     * Draws a a connected series of line segments. The polyline is drawn with the current color and line width. The
     * polyline is defined by a sequence of coordinate pairs specifiying the endpoints of the line segments. For
     * example, the code
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
     * Draws a rectangle. The rectangle is drawn using the current alignment, color, and line width. The rectangle is
     * positioned relative to (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if
     * <code>width</code> or <code>height</code> are not positive.
     *
     * @param x the horizontal coordinate of rectangle
     * @param y the vertical coordinate of rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1.0
     */
    public void drawRectangle(final double x, final double y, final double width, final double height) {
        drawRectangle((float) x, (float) y, (float) width, (float) height);
    }

    /**
     * Draws a rectangle. The rectangle is drawn using the current alignment, color, and line width. The rectangle is
     * positioned relative to (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if
     * <code>width</code> or <code>height</code> are not positive.
     *
     * @param x the horizontal coordinate of rectangle
     * @param y the vertical coordinate of rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 2.0
     */
    public void drawRectangle(float x, float y, float width, float height) {
        x = x * sx + tx;
        y = y * sy + ty;
        width = width * slx;
        height = height * sly;
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
    public void drawShadowCircle(float x, float y, final float radius) {
        x = x * sx + tx;
        y = y * sy + ty;
        final float rx = radius * slx;
        final float ry = radius * sly;
        final int SHADOW_MAX_OPACITY = 80;
        final int opacityStep = SHADOW_MAX_OPACITY / displayAdaption.shadowThickness;
        int shadow = SHADOW_MAX_OPACITY;
        for (int i = 0; i < displayAdaption.shadowThickness; i++) {
            imp.setColor(new Color(0, 0, 0, shadow));
            imp.drawEllipse(x, y, rx + i, ry + i);
            shadow = shadow - opacityStep;
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
        width = width * slx;
        height = height * sly;
        x = alignX(x * sx + tx, width);
        y = alignY(y * sy + ty, height);
        final int SHADOW_MAX_OPACITY = 80;
        final int opacityStep = SHADOW_MAX_OPACITY / displayAdaption.shadowThickness;
        int shadow = SHADOW_MAX_OPACITY;
        for (int i = 0; i < displayAdaption.shadowThickness; i++) {
            imp.setColor(new Color(0, 0, 0, shadow));
            imp.drawRectangle(x - i, y - i, width + 2 * i, height + 2 * i);
            shadow = shadow - opacityStep;
        }

        imp.setColor(color);
    }

    /**
     * Draws a text. The text is drawn using the current alignment, color, text size, and typeface. he text is
     * positioned relative to (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if
     * <code>text</code> is <code>null</code> or empty.
     *
     * @param x the horizontal coordinate of the text
     * @param y the vertical coordinate of the text
     * @param text the text to draw
     *
     * @since 1.0
     */
    public void drawText(final double x, final double y, final String text) {
        drawText((float) x, (float) y, text);
    }

    /**
     * Draws a text. The text is drawn using the current alignment, color, text size, and typeface. he text is
     * positioned relative to (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if
     * <code>text</code> is <code>null</code> or empty.
     *
     * @param x the horizontal coordinate of the text
     * @param y the vertical coordinate of the text
     * @param text the text to draw
     *
     * @since 2.0
     */
    public void drawText(float x, float y, final String text) {
        if (text != null && !text.isEmpty()) {
            x = x * sx + tx;
            y = y * sy + ty;
            final float width = imp.measureLength(text, typeface.imp, textSize * canvasToDevice);
            final float height = imp.getTextHeight();
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
     * Draws a filled a circle. The circle is drawn using the current color. Has no effect if the specified radius is
     * not positive.
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
     * Draws a filled a circle. The circle is drawn using the current color. Has no effect if the specified radius is
     * not positive.
     *
     * @param centerX the x coordinate of the circle's center
     * @param centerY the y coordinate of the circle's center
     * @param radius the circle's radius
     *
     * @since 2.0
     */
    public void fillCircle(final float centerX, final float centerY, final float radius) {
        if (radius <= 0f) {
            Log.d("Ignoring call with non-positive radius.");
        }
        else {
            imp.fillEllipse(centerX * sx + tx, centerY * sy + ty, radius * slx, radius * sly);
        }
    }

    /**
     * Draws a filled ellipse. The ellipse is drawn using the current color. Has no effect if the specified radii are
     * not positive.
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
     * Draws a filled ellipse. The ellipse is drawn using the current color. Has no effect if the specified radii are
     * not positive.
     *
     * @param centerX the x coordinate of the ellipse's center
     * @param centerY the y coordinate of the ellipse's center
     * @param radiusX the horizontal radius of the ellipse
     * @param radiusY the vertical radius of the ellipse
     *
     * @since 2.0
     */
    public void fillEllipse(final float centerX, final float centerY, final float radiusX, final float radiusY) {
        if (radiusX <= 0f || radiusY <= 0f) {
            Log.d("Ignoring call with non-positive radius.");
        }
        else {
            imp.fillEllipse(centerX * sx + tx, centerY * sy + ty, radiusX * slx, radiusY * sly);
        }
    }

    /**
     * Draws a filled polygon. The polygon is drawn using the current color. The polygon is defined by a sequence of
     * coordinate pairs specifiying the corners of the polygon. For example, the code
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
     * Draws a filled polygon. The polygon is drawn using the current color. The polygon is defined by a sequence of
     * coordinate pairs specifiying the corners of the polygon. For example, the code
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
     * Draws a filled rectangle. The rectangle is drawn using the current alignment and color. The rectangle is
     * positioned relative to (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if
     * <code>width</code> or <code>height</code> are not positive.
     *
     * @param x the horizontal coordinate of rectangle
     * @param y the vertical coordinate of rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 1.0
     */
    public void fillRectangle(final double x, final double y, final double width, final double height) {
        fillRectangle((float) x, (float) y, (float) width, (float) height);
    }

    /**
     * Draws a filled rectangle. The rectangle is drawn using the current alignment and color. The rectangle is
     * positioned relative to (<code>x</code>, <code>y</code>) according to the current alignment. Has no effect if
     * <code>width</code> or <code>height</code> are not positive.
     *
     * @param x the horizontal coordinate of rectangle
     * @param y the vertical coordinate of rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     *
     * @since 2.0
     */
    public void fillRectangle(float x, float y, float width, float height) {
        x = x * sx + tx;
        y = y * sy + ty;
        width = width * slx;
        height = height * sly;
        imp.fillRectangle(alignX(x, width), alignY(y, height), width, height);
    }

    /**
     * Returns the current alignment.
     *
     * @return the current alignment
     *
     * @see #setAlignment(ch.jeda.ui.Alignment)
     * @since 2.1
     */
    public Alignment getAlignment() {
        return alignment;
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
     * Returns the height of this canvas in density-independent pixels (dp).
     *
     * @return the height of this canvas in dp
     *
     * @since 1.0
     */
    public float getHeight() {
        return imp.getHeight() * deviceToCanvas;
    }

    /**
     * Returns the current line width in density-independent pixels (dp).
     *
     * @return current line width in dp
     *
     * @see #setLineWidth(double)
     * @see #setLineWidth(float)
     * @since 1.0
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Returns the current opacity.
     *
     * @return the current opacity
     *
     * @see #setOpacity(int)
     * @since 2.1
     */
    public int getOpacity() {
        return opacity;
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
     * Returns the width of this canvas in density-independent pixels (dp).
     *
     * @return the width of this canvas in dp
     *
     * @since 1.0
     */
    public float getWidth() {
        return imp.getWidth() * deviceToCanvas;
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

    /**
     * Returns the length of the text in world coordinates.
     *
     * @param text the text
     * @param typeface the typeface
     * @param textSize the text size
     *
     * @return the length of the text
     */
    public float measureLength(final String text, final Typeface typeface, final float textSize) {
        if (text == null || text.isEmpty()) {
            return 0f;
        }
        else {
            return imp.measureLength(text, typeface.imp, textSize * canvasToDevice) / slx;
        }
    }

    /**
     * Sets the alignment for this canvas.
     *
     * @param alignment the new alignment
     *
     * @since 2.0
     */
    public void setAlignment(Alignment alignment) {
        if (alignment == null) {
            Log.d("Ignoring call with null alignment.");
        }
        else {
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
     * <code>fill...</code> operations. Has no effect if <code>color</code> is <code>null</code>.
     *
     * @param color new drawing color.
     *
     * @see #getColor()
     * @since 1.0
     */
    public void setColor(final Color color) {
        if (color == null) {
            Log.d("Ignoring call with null color.");
        }
        else if (!color.equals(this.color)) {
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
     * Sets the line width in density-independent pixels (dp). The line width set by this method is applied to
     * subsequent <code>draw...</code> operations. Has no effect if <code>lineWidth</code> is negative.
     *
     * @param lineWidth the new line width in dp
     *
     * @see #getLineWidth()
     * @since 1.0
     */
    public void setLineWidth(final double lineWidth) {
        setLineWidth((float) lineWidth);
    }

    /**
     * Sets the line width in density-independent pixels (dp). The line width set by this method is applied to
     * subsequent <code>draw...</code> operations. Has no effect if <code>lineWidth</code> is negative.
     *
     * @param lineWidth the new line width in dp
     *
     * @see #getLineWidth()
     * @since 2.0
     */
    public void setLineWidth(final float lineWidth) {
        if (lineWidth < 0f) {
            Log.d("Ignoring call with negative line width.");
        }
        else {
            this.lineWidth = lineWidth;
            imp.setLineWidth(this.lineWidth * canvasToDevice);
        }
    }

    /**
     * Sets the opactiy. The opacity is a value between 0 and 255 defining the translucency used when drawing images. 0
     * means fully transparent, 255 means fully opaque. Values of <code>opacity</code> smaller than 0 are interpreted as
     * 0, valuers greater than 255 as 255. The opacity is applied to all subsequent <code>drawImage(...)</code>
     * operations.
     *
     * @param opacity the opacity
     *
     * @see #getOpacity()
     * @since 2.1
     */
    public void setOpacity(int opacity) {
        this.opacity = Math.max(0, Math.min(opacity, 255));
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
     * operations. Has no effect if <code>textSize</code> is not positive.
     *
     * @param textSize the new text size
     *
     * @see #getTextSize()
     * @since 2.0
     */
    public void setTextSize(final float textSize) {
        if (textSize <= 0) {
            Log.d("Ignoring call with non-positive text size.");
        }

        if (this.textSize != textSize) {
            this.textSize = textSize;
            imp.setTextSize(this.textSize * canvasToDevice);
        }
    }

    /**
     * Sets the typeface to be used to draw text. Has no effect if <code>typeface</code> is <code>null</code> or
     * unavailable.
     *
     * @param typeface the typeface to be used to draw text
     *
     * @since 1.3
     */
    public void setTypeface(final Typeface typeface) {
        if (typeface == null || !typeface.isAvailable()) {
            Log.d("Ignoring call with null or unavailable typeface.");
        }
        else if (!typeface.equals(this.typeface)) {
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
            return imp.getTextHeight();
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
            return imp.measureLength(text, typeface.imp, textSize * canvasToDevice);
        }
    }

    final void setWorldTransformation(final float scaleX, final float scaleY,
                                      final float translateX, final float translateY) {
        sx = canvasToDevice * scaleX;
        sy = -canvasToDevice * scaleY;
        slx = Math.abs(sx);
        sly = Math.abs(sy);
        tx = translateX * slx;
        ty = pixelsY - translateY * sly;
    }

    void localBegin(final float originX, final float originY, final float angleRad) {
        imp.translate(originX * slx, -originY * sly);
        imp.rotateRad(-angleRad, tx, ty);

    }

    void localEnd() {
        imp.resetTransformation();
    }

    private float[] convertPoints(double[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < values.length; i = i + 2) {
            result[i] = (float) values[i] * sx + tx;
            result[i + 1] = (float) values[i + 1] * sy + ty;
        }

        return result;
    }

    private float[] convertPoints(float[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < values.length; i = i + 2) {
            result[i] = values[i] * sx + tx;
            result[i + 1] = values[i + 1] * sy + ty;
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

    float deviceToCanvasX(final float x) {
        return x * deviceToCanvas;
    }

    float deviceToCanvasY(final float y) {
        return (pixelsY - y) * deviceToCanvas;
    }
}
