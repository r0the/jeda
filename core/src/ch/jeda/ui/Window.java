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
import ch.jeda.event.Event;
import ch.jeda.event.EventQueue;
import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import ch.jeda.platform.ViewImp;
import java.util.EnumSet;

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
public class Window extends Canvas {

    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 800;
    private static final EnumSet<ViewFeature> IMP_CHANGING_FEATURES = initImpChangingFeatures();
    private final EventQueue eventQueue;
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
        eventQueue = new EventQueue();
        title = Jeda.getProgramName();
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
        else if (feature == WindowFeature.SCROLLABLE) {
            eventQueue.setDragEnabled(enabled);
        }
        else {
            imp.setFeature(viewFeature, enabled);
        }
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

        imp = JedaInternal.createViewImp(width, height, features);
        imp.setEventQueue(eventQueue);
        imp.setTitle(title);
        if (!hasFeature(WindowFeature.DOUBLE_BUFFERED)) {
            imp.getCanvas().setColor(Color.WHITE);
            imp.getCanvas().fill();
        }

        eventQueue.setDragEnabled(features.contains(ViewFeature.SCROLLABLE));
        super.setImp(imp.getCanvas());
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
            case SCROLLABLE:
                return ViewFeature.SCROLLABLE;
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
