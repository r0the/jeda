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
import ch.jeda.event.Button;
import ch.jeda.event.Event;
import ch.jeda.event.EventQueue;
import ch.jeda.event.EventType;
import ch.jeda.event.Key;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.PointerEvent;
import ch.jeda.event.WheelEvent;
import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import ch.jeda.platform.ViewCallback;
import ch.jeda.platform.ViewImp;
import java.util.EnumSet;
import java.util.Stack;

/**
 * Represents a drawing window. The window class has the following functionality:
 * <ul>
 * <li> fullscreen: the window can be displayed in framed or fullscreen mode.
 * <li> double buffering: the window supports a double buffering mode for animations.
 * <li> user input: the window provides means to query keyboard and mouse input.
 * </ul>
 *
 * @since 1.0
 * @version 3
 */
public class Window {

    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_TEXT_SIZE = 16;
    private static final Color DEFAULT_FOREGROUND = Color.BLACK;
    private static final EnumSet<ViewFeature> IMP_CHANGING_FEATURES = initImpChangingFeatures();
    private final ViewCallback callback;
    private final EventQueue eventQueue;
    private boolean antiAliasing;
    private Color color;
    private float lineWidth;
    private int textSize;
    private Typeface typeface;
    private ViewImp imp;
    private String title;

    /**
     * Constructs a window. The window is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the window has a width of 800
     * pixels and a height of 600 pixels.
     * </p><p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @since 1.0
     */
    public Window() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Constructs a window. The window is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported. The specified features will be enabled for the window.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the window has a width of 800
     * pixels and a height of 600 pixels.
     * </p><p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.</p>
     *
     * @param features the features of the window
     *
     * @since 1.0
     */
    public Window(final WindowFeature... features) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, features);
    }

    /**
     * Constructs a window. The window is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported. The specified features will be enabled for the window.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the window has the specified
     * <tt>width</tt> and <tt>height</tt>.
     * </p><p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.</p>
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of the window
     * @throws IllegalArgumentException if width or height are smaller than 1
     *
     * @since 1.0
     */
    public Window(final int width, final int height, final WindowFeature... features) {
        callback = new Callback(this);
        eventQueue = new EventQueue();
        title = Jeda.getProgramName();
        antiAliasing = false;
        color = DEFAULT_FOREGROUND;
        textSize = DEFAULT_TEXT_SIZE;
        typeface = Typeface.SANS_SERIF;
        resetImp(width, height, convertFeatures(features));
        Jeda.addEventListener(eventQueue);
        Jeda.addEventListener(new EventLoop(this));
    }

    /**
     * Adds an event listener to the window. The specified object will receive events for all events listener interfaces
     * it implements. Has no effect if <tt>listener</tt> is <tt>null</tt>.
     *
     * @param listener the event listener
     *
     * @since 1.0
     */
    public final void addEventListener(final Object listener) {
        eventQueue.addListener(listener);
    }

    /**
     * Closes the window. The window becomes invalid, all subsequent method calls to the window will cause an error.
     *
     * @since 1.0
     */
    public final void close() {
        imp.close();
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
            imp.getForeground().drawEllipse(x, y, radius, radius);
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
        if (radius > 0) {
            imp.getForeground().drawEllipse((float) x, (float) y, (float) radius, (float) radius);
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
            imp.getForeground().drawImage(x, y, image.getWidth(), image.getHeight(), image.getImp(), 255);
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
        drawImage((int) x, (int) y, image);
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
            imp.getForeground().drawImage(x, y, image.getWidth(), image.getHeight(), image.getImp(), alpha);
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
        drawImage((int) x, (int) y, image, alpha);
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
            imp.getForeground().drawImage(alignment.alignX(x, image.getWidth()), alignment.alignY(y, image.getHeight()),
                                          image.getWidth(), image.getHeight(), image.getImp(), 255);
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
    public void drawImage(final double x, final double y, final Image image, final Alignment alignment) {
        drawImage((int) x, (int) y, image, alignment);
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
            imp.getForeground().drawImage(alignment.alignX(x, image.getWidth()), alignment.alignY(y, image.getHeight()),
                                          image.getWidth(), image.getHeight(),
                                          image.getImp(), alpha);
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
    public void drawImage(final double x, final double y, final Image image, final int alpha,
                          final Alignment alignment) {
        drawImage((int) x, (int) y, image, alpha, alignment);
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
        float[] points = new float[4];
        points[0] = x1;
        points[1] = y1;
        points[2] = x2;
        points[3] = y2;
        imp.getForeground().drawPolyline(points);
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
        drawLine((int) x1, (int) y1, (int) x2, (int) y2);
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

        imp.getForeground().drawPolygon(toFloatArray(points));
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

        this.imp.getForeground().drawPolygon(toFloatArray(points));
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
            imp.getForeground().drawRectangle(x, y, width, height);
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
        drawRectangle((int) x, (int) y, (int) width, (int) height);
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
            imp.getForeground().drawRectangle(alignment.alignX(x, width), alignment.alignY(y, height), width, height);
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
    public void drawRectangle(final double x, final double y, final double width, final double height,
                              final Alignment alignment) {
        drawRectangle((int) x, (int) y, (int) width, (int) height, alignment);
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
            imp.getForeground().drawText(x, y, text);
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
        drawText((int) x, (int) y, text);
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
            imp.getForeground().drawText(alignment.alignX(x, imp.getForeground().textWidth(text)),
                                         alignment.alignY(y, imp.getForeground().textHeight(text)), text);
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
    public final void drawText(final double x, final double y, final String text, final Alignment alignment) {
        drawText((int) x, (int) y, text, alignment);
    }

    /**
     * Fills the entire canvas. The canvas is filled using the current color.
     *
     * @since 1.0
     */
    public void fill() {
        imp.getForeground().fill();
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
            imp.getForeground().fillEllipse(x, y, radius, radius);
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
        if (radius > 0) {
            imp.getForeground().fillEllipse((float) x, (float) y, (float) radius, (float) radius);
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

        this.imp.getForeground().fillPolygon(toFloatArray(points));
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

        this.imp.getForeground().fillPolygon(toFloatArray(points));
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
            imp.getForeground().fillRectangle(x, y, width, height);
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
        fillRectangle((int) x, (int) y, (int) width, (int) height);
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
     * @param alignment specifies how to align the rectangle relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.0
     */
    public void fillRectangle(final int x, final int y, final int width, final int height, final Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }

        if (width > 0 && height > 0) {
            imp.getForeground().fillRectangle(alignment.alignX(x, width), alignment.alignY(y, height), width, height);
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
     * @param alignment specifies how to align the rectangle relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.0
     */
    public void fillRectangle(final double x, final double y, final double width, final double height,
                              final Alignment alignment) {
        fillRectangle((int) x, (int) y, (int) width, (int) height, alignment);
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
            if (getPixelAt(x, y).equals(oldColor)) {
                setPixelAt(x, y, newColor);
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
        return color;
    }

    /**
     * Returns the current text size.
     *
     * @return current text size
     *
     * @see #setFontSize(int)
     * @since 1.0
     */
    public int getFontSize() {
        return textSize;
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
        return imp.getForeground().getHeight();
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
        return lineWidth;
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
        if (contains(x, y)) {
            return imp.getForeground().getPixel(x, y);
        }
        else {
            return Color.TRANSPARENT;
        }
    }

    /**
     * Returns the current window title.
     *
     * @return current window title
     *
     * @see #setTitle(java.lang.String)
     * @since 1.0
     */
    public final String getTitle() {
        return title;
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
     * Returns the width of the canvas in pixels.
     *
     * @return width of canvas
     *
     * @see #getHeight()
     * @since 1.0
     */
    public int getWidth() {
        return imp.getForeground().getWidth();
    }

    /**
     * Checks for a window feature. Returns <tt>true</tt> if the specified feature is currently enabled for the window.
     *
     * @param feature the feature to check for
     * @return <tt>true</tt> if the feature is enabled, otherwise returns
     * <tt>false</tt>
     * @throws NullPointerException if <tt>feature</tt> is <tt>null</tt>
     *
     * @see #setFeature(WindowFeature, boolean)
     * @since 1.0
     */
    public final boolean hasFeature(final WindowFeature feature) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        return imp.getFeatures().contains(convertFeature(feature));
    }

    /**
     * Removes an event listener from the window. The specified object will not receive events anymore. Has no effect if
     * <tt>listener</tt> is <tt>null</tt>.
     *
     * @param listener the event listener
     * @since 1.0
     */
    public final void removeEventListener(final Object listener) {
        eventQueue.removeListener(listener);
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
        if (antiAliasing != this.antiAliasing) {
            this.antiAliasing = antiAliasing;
            imp.getForeground().setAntiAliasing(antiAliasing);
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

        if (!color.equals(this.color)) {
            this.color = color;
            imp.getForeground().setColor(color);
        }
    }

    /**
     * Enables or disables a window feature.
     *
     * @param feature the feature to be enabled or disabled
     * @param enabled <tt>true</tt> to enable the feature,
     * <tt>false</tt> to disable it
     * @throws NullPointerException if <tt>feature</tt> is <tt>null</tt>
     *
     * @see #hasFeature(WindowFeature)
     * @since 1.0
     */
    public final void setFeature(final WindowFeature feature, final boolean enabled) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        final ViewFeature viewFeature = convertFeature(feature);
        if (IMP_CHANGING_FEATURES.contains(viewFeature)) {
            final EnumSet<ViewFeature> featureSet = EnumSet.copyOf(imp.getFeatures());
            if (enabled) {
                featureSet.add(viewFeature);
            }
            else {
                featureSet.remove(viewFeature);
            }

            resetImp(getWidth(), getHeight(), featureSet);
        }
        else {
            imp.setFeature(viewFeature, enabled);
        }
    }

    /**
     * Sets the text size. The text size set by this method is applied to all subsequent <tt>drawText(...)</tt>
     * operations.
     *
     * @param size the new text size
     * @throws IllegalArgumentException if <tt>size</tt> is not positive
     *
     * @see #getFontSize()
     * @since 1.0
     */
    public void setFontSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size");
        }

        if (textSize != size) {
            textSize = size;
            imp.getForeground().setTextSize(textSize);
        }
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

        this.lineWidth = (float) lineWidth;
        imp.getForeground().setLineWidth(this.lineWidth);
    }

    /**
     * Sets the shape of the mouse cursor.
     *
     * @param mouseCursor new shape of mouse cursor
     * @throws NullPointerException if <tt>mouseCursor</tt> is
     * <tt>null</tt>
     *
     * @see MouseCursor
     * @since 1.0
     */
    public final void setMouseCursor(final MouseCursor mouseCursor) {
        if (mouseCursor == null) {
            throw new NullPointerException("mouseCursor");
        }

        imp.setMouseCursor(mouseCursor);
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

        if (contains(x, y)) {
            imp.getForeground().setPixel(x, y, color);
        }
    }

    /**
     * Sets the window title.
     *
     * @param title new title of the window
     * @throws NullPointerException if <tt>title</tt> is <tt>null</tt>
     *
     * @see #getTitle()
     * @since 1.0
     */
    public final void setTitle(final String title) {
        if (title == null) {
            throw new NullPointerException("title");
        }

        this.title = title;
        imp.setTitle(title);
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

        if (!typeface.equals(this.typeface)) {
            this.typeface = typeface;
            imp.getForeground().setTypeface(typeface.imp);
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
        return new Image(imp.getForeground().takeSnapshot());
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
            return imp.getForeground().textHeight(text);
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
            return imp.getForeground().textWidth(text);
        }
    }

    void postEvent(final Event event) {
        eventQueue.addEvent(event);
    }

    private void tick(final TickEvent event) {
        if (imp.isVisible()) {
            eventQueue.processEvents();
            imp.update();
        }
    }

    private void resetImp(final int width, final int height, final EnumSet<ViewFeature> features) {
        if (imp != null) {
            imp.close();
        }

        imp = JedaInternal.createViewImp(callback, width, height, features);
        imp.setTitle(title);
        imp.getForeground().setAntiAliasing(antiAliasing);
        imp.getForeground().setColor(color);
        imp.getForeground().setTextSize(textSize);
        imp.getForeground().setLineWidth(lineWidth);
        imp.getForeground().setTypeface(typeface.imp);
        if (!hasFeature(WindowFeature.DOUBLE_BUFFERED)) {
            imp.getForeground().setColor(Color.WHITE);
            imp.getForeground().fill();
        }
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < getWidth() && 0 <= y && y < getHeight();
    }

    private static EnumSet<ViewFeature> initImpChangingFeatures() {
        final EnumSet<ViewFeature> result = EnumSet.noneOf(ViewFeature.class);
        result.add(ViewFeature.DOUBLE_BUFFERED);
        result.add(ViewFeature.FULLSCREEN);
        return result;
    }

    private static ViewFeature convertFeature(final WindowFeature windowFeature) {
        switch (windowFeature) {
            case DOUBLE_BUFFERED:
                return ViewFeature.DOUBLE_BUFFERED;
            case FULLSCREEN:
                return ViewFeature.FULLSCREEN;
            case HOVERING_POINTER:
                return ViewFeature.HOVERING_POINTER;
            case ORIENTATION_LANDSCAPE:
                return ViewFeature.ORIENTATION_LANDSCAPE;
            case ORIENTATION_PORTRAIT:
                return ViewFeature.ORIENTATION_PORTRAIT;
            default:
                return null;
        }
    }

    private static EnumSet<ViewFeature> convertFeatures(final WindowFeature... features) {
        final EnumSet<ViewFeature> result = EnumSet.noneOf(ViewFeature.class);
        for (final WindowFeature feature : features) {
            final ViewFeature viewFeature = convertFeature(feature);
            if (viewFeature != null) {
                result.add(viewFeature);
            }
        }

        return result;
    }

    private static float[] toFloatArray(int[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < values.length; ++i) {
            result[i] = values[i];
        }

        return result;
    }

    private static float[] toFloatArray(double[] values) {
        final float[] result = new float[values.length];
        for (int i = 0; i < values.length; ++i) {
            result[i] = (float) values[i];
        }

        return result;
    }

    private static class Callback implements ViewCallback {

        private final Window window;

        public Callback(final Window window) {
            this.window = window;
        }

        @Override
        public void postKeyDown(Object source, Key key, int count) {
            postEvent(new KeyEvent(source, EventType.KEY_DOWN, key));
        }

        @Override
        public void postKeyTyped(Object source, Key key) {
            postEvent(new KeyEvent(source, EventType.KEY_TYPED, key));
        }

        @Override
        public void postKeyTyped(Object source, char ch) {
            postEvent(new KeyEvent(source, EventType.KEY_TYPED, ch));
        }

        @Override
        public void postKeyUp(Object source, Key key) {
            postEvent(new KeyEvent(source, EventType.KEY_UP, key));
        }

        @Override
        public void postPointerDown(Object source, int pointerId, EnumSet<Button> pressedButtons, float x, float y) {
            postEvent(new PointerEvent(source, EventType.POINTER_DOWN, pointerId, pressedButtons, x, y, x, y));
        }

        @Override
        public void postPointerMoved(Object source, int pointerId, EnumSet<Button> pressedButtons, float x, float y) {
            postEvent(new PointerEvent(source, EventType.POINTER_MOVED, pointerId, pressedButtons, x, y, x, y));
        }

        @Override
        public void postPointerUp(Object source, int pointerId, EnumSet<Button> pressedButtons, float x, float y) {
            postEvent(new PointerEvent(source, EventType.POINTER_UP, pointerId, pressedButtons, x, y, x, y));
        }

        @Override
        public void postWheel(Object source, float rotation) {
            postEvent(new WheelEvent(source, rotation));
        }

        private void postEvent(final Event event) {
            window.eventQueue.addEvent(event);
        }
    }

    private static class EventLoop implements TickListener {

        private final Window window;

        public EventLoop(final Window window) {
            this.window = window;
        }

        @Override
        public void onTick(final TickEvent event) {
            window.tick(event);
        }
    }
}
