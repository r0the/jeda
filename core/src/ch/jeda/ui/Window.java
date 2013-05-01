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
import ch.jeda.Size;
import ch.jeda.platform.WindowImp;
import java.util.EnumSet;

/**
 * Represents a drawing window. The window class has the follwoing
 * functionality:
 * <ul>
 * <li> fullscreen: the window can be displayed in framed or fullscreen mode.
 * <li> double buffering: the window supports a double buffering mode for
 * animations.
 * <li> user input: the window provides means to query keyboard and mouse input.
 * </ul>
 *
 * @since 1
 */
public class Window extends Canvas {

    public enum Feature {

        /**
         * Enables double buffering in a window. A double-buffered window
         * performs drawing operations in an off-screen buffer. The
         * {@link Window#update()} method must be called to display the contents
         * of this buffer. This prevents flickering in animations.
         *
         * @see Window#update()
         * @since 1
         */
        DoubleBuffered,
        /**
         * Sets fullscreen mode for a window. The behaviour of this feature
         * depends on the platform:
         * <p>
         * <img src="../../../windows.png"> <img src="../../../linux.png">
         * Displays the window in fullscreen mode. The screen resolution is
         * automatically selected to fit the window size as closely as possible.
         * <p>
         * <img src="../../../android.png"> Fullscreen mode is not yet supported
         * on Android devices.
         *
         * @since 1
         */
        Fullscreen,
        /**
         *
         * @see Window#update()
         * @since 1
         */
        HoveringPointer,
        /**
         * Set landscape orientation for a window. The behaviour of this feature
         * depends on the platform:
         * <p>
         * <img src="../../../windows.png"> <img src="../../../linux.png"> Has
         * no effect.
         * <p>
         * <img src="../../../android.png"> The screen orientation of the device
         * is set to landscape. Thus, the width of the window's drawing area is
         * larger than it's height.
         *
         * @since 1
         */
        OrientationLandscape,
        /**
         * Set portrait orientation for a window. The behaviour of this feature
         * depends on the platform:
         * <p>
         * <img src="../../../windows.png"> <img src="../../../linux.png"> Has
         * no effect.
         * <p>
         * <img src="../../../android.png"> The screen orientation of the device
         * is set to portrait. Thus, the width of the window's drawing area is
         * smaller than it's height.
         *
         * @since 1
         */
        OrientationPortrait
    }
    private static final EnumSet<Feature> IMP_CHANGING_FEATURES = initImpChangingFeatures();
    private final EventDispatcher eventDispatcher;
    private final Events events;
    private WindowImp imp;
    private boolean processedEvents;
    private String title;

    /**
     * @deprecated Use {@link #Window()} instead.
     */
    @Deprecated
    public static Window create() {
        return new Window();
    }

    /**
     * @deprecated Use {@link #Window(Feature... )} instead.
     */
    @Deprecated
    public static Window create(Feature... features) {
        return new Window(features);
    }

    /**
     * @deprecated Use {@link #Window(int, int, Feature... )} instead.
     */
    @Deprecated
    public static Window create(int width, int height, Feature... features) {
        return new Window(width, height, features);
    }

    /**
     * Constructs a window. The window is shown on the screen. All drawing
     * methods inherited from {@link Canvas} are supported.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The
     * drawing area of the window has a width of 800 pixels and a height of 600
     * pixels.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the
     * screen size of the device.
     *
     * @since 1
     */
    public Window() {
        this(0, 0);
    }

    /**
     * Constructs a window. The window is shown on the screen. All drawing
     * methods inherited from {@link Canvas} are supported. The specified
     * features will be enabled for the window.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The
     * drawing area of the window has a width of 800 pixels and a height of 600
     * pixels.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the
     * screen size of the device.
     *
     * @param features the features of the window
     *
     * @since 1
     */
    public Window(final Feature... features) {
        this.eventDispatcher = new EventDispatcher();
        this.events = new Events();
        this.eventDispatcher.addListener(this.events.listener);
        this.title = Thread.currentThread().getName();
        this.resetImp(0, 0, toSet(features));
    }

    /**
     * Constructs a window. The window is shown on the screen. All drawing
     * methods inherited from {@link Canvas} are supported. The specified
     * features will be enabled for the window.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The
     * drawing area of the window has the specified <tt>width</tt> and
     * <tt>height</tt>.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the
     * screen size of the device.
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of the window
     * @throws IllegalArgumentException if width or height are smaller than 1
     *
     * @since 1
     */
    public Window(final int width, final int height,
                  final Feature... features) {
        this.eventDispatcher = new EventDispatcher();
        this.events = new Events();
        this.eventDispatcher.addListener(this.events.listener);
        this.title = Engine.getProgramName();
        this.resetImp(width, height, toSet(features));
    }

    /**
     * @deprecated Use {@link #Window(int, int, ch.jeda.ui.Window.Feature[])}
     * instead.
     */
    @Deprecated
    public Window(Size size, Feature... features) {
        this(size.width, size.height, features);
    }

    /**
     * Adds an event listener to the window. The specified object will recieve
     * events for all events listener interfaces it implements.
     *
     * @param listener the event listener
     * @since 1
     */
    public void addEventListener(final Object listener) {
        this.eventDispatcher.addListener(listener);
    }

    /**
     * Closes the window. The window becomes invalid, all subsequent method
     * calls to the window will cause an error.
     *
     * @since 1
     */
    public void close() {
        this.imp.close();
    }

    /**
     * Returns an object holding the events that are taking place on the window.
     * The {@link Events} object returned by this method stays valid as long as
     * the window is open. The {@link Events} object is updated only by a call
     * to the {@link #update()} method.
     *
     * @return {@link Events} object representing the events on the window
     *
     * @since 1
     */
    public Events getEvents() {
        return this.events;
    }

    /**
     * Returns the current window title.
     *
     * @return current window title
     *
     * @see #setTitle(java.lang.String)
     * @since 1
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Checks for a window feature. Returns <tt>true</tt> if the specified
     * feature is currently enabled for the window.
     *
     * @return <tt>true</tt> if the feature is enabled, otherwise returns
     * <tt>false</tt>
     * @throws NullPointerException if <tt>feature</tt> is <tt>null</tt>
     *
     * @see #setFeature(ch.jeda.ui.Window.Feature, boolean)
     * @since 1
     */
    public boolean hasFeature(final Feature feature) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        return this.imp.getFeatures().contains(feature);
    }

    public void processEvents() {
        this.events.prepare();
        this.eventDispatcher.dispatchEvents(this.imp.fetchEvents());
        this.processedEvents = true;
    }

    /**
     * Removes an event listener from the window. The specified object will not
     * receive events anymore.
     *
     * @param listener the event listener
     * @since 1
     */
    public void removeEventListener(final Object listener) {
        this.eventDispatcher.removeListener(listener);
    }

    /**
     * Enables or disables a window feature.
     *
     * @param feature the feature to be enabled or disabled
     * @param enabled <tt>true</tt> to enable the feature,
     * <tt>false</tt> to disable it
     * @throws NullPointerException if <tt>feature</tt> is <tt>null</tt>
     *
     * @see #hasFeature(ch.jeda.ui.Window.Feature)
     * @since 1
     */
    public void setFeature(final Feature feature, final boolean enabled) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        if (IMP_CHANGING_FEATURES.contains(feature)) {
            final EnumSet<Feature> featureSet =
                    EnumSet.copyOf(this.imp.getFeatures());
            if (enabled) {
                featureSet.add(feature);
            }
            else {
                featureSet.remove(feature);
            }

            this.resetImp(this.getWidth(), this.getHeight(), featureSet);
        }
        else {
            this.imp.setFeature(feature, enabled);
        }
    }

    /**
     * @deprecated use #setFeature(Window.Feature.Fullscreen, fullscreen)
     * instead
     */
    @Deprecated
    public void setFullscreen(boolean fullscreen) {
        this.setFeature(Feature.Fullscreen, fullscreen);
    }

    /**
     * Sets the shape of the mouse cursor.
     *
     * @param mouseCursor new shape of mouse cursor
     * @throws NullPointerException if <tt>mouseCursor</tt> is
     * <tt>null</tt>
     *
     * @see MouseCursor
     * @since 1
     */
    public void setMouseCursor(final MouseCursor mouseCursor) {
        if (mouseCursor == null) {
            throw new NullPointerException("mouseCursor");
        }

        this.imp.setMouseCursor(mouseCursor);
    }

    /**
     * Sets the window title.
     *
     * @param title new title of the window
     * @throws NullPointerException if <tt>title</tt> is <tt>null</tt>
     *
     * @see #getTitle()
     * @since 1
     */
    public void setTitle(final String title) {
        if (title == null) {
            throw new NullPointerException("title");
        }

        this.title = title;
        this.imp.setTitle(title);
    }

    /**
     * Updates the window. Updates the {@link Events} of the window. If the
     * window has the feature {@link Feature#DoubleBuffered} activiated, also
     * flips foreground and background buffer.
     *
     * @since 1
     */
    public void update() {
        if (!this.processedEvents) {
            this.processEvents();
        }

        this.imp.update();
        this.processedEvents = false;
    }

    private void resetImp(final int width, final int height,
                          final EnumSet<Feature> features) {
        if (this.imp != null) {
            this.imp.close();
        }

        this.imp = Engine.getContext().showWindow(width, height, features);
        this.events.reset();
        this.imp.setTitle(this.title);
        if (!this.hasFeature(Feature.DoubleBuffered)) {
            this.imp.setColor(Color.WHITE);
            this.imp.fill();
        }

        super.setImp(this.imp);
        this.update();
    }

    private static EnumSet<Feature> initImpChangingFeatures() {
        final EnumSet<Feature> result = EnumSet.noneOf(Feature.class);
        result.add(Feature.DoubleBuffered);
        result.add(Feature.Fullscreen);
        return result;
    }

    private static EnumSet<Feature> toSet(final Feature... features) {
        final EnumSet<Feature> result = EnumSet.noneOf(Feature.class);
        for (Feature feature : features) {
            result.add(feature);
        }

        return result;
    }
}
