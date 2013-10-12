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
import ch.jeda.platform.WindowImp;
import java.util.EnumSet;

/**
 * Represents a drawing window. The window class has the following functionality:
 * <ul>
 * <li> fullscreen: the window can be displayed in framed or fullscreen mode.
 * <li> double buffering: the window supports a double buffering mode for animations.
 * <li> user input: the window provides means to query keyboard and mouse input.
 * </ul>
 *
 * @since 1
 */
public class Window extends Canvas {

    private static final EnumSet<WindowFeature> IMP_CHANGING_FEATURES = initImpChangingFeatures();
    private static final int PAUSE_SLEEP_MILLIS = 200;
    private final Drawables drawables;
    private final EventDispatcher eventDispatcher;
    private final Events events;
    private final FrequencyMeter frequencyMeter;
    private final Timer timer;
    private WindowImp imp;
    private String title;

    /**
     * Constructs a window. The window is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the window has a width of 800
     * pixels and a height of 600 pixels.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @since 1
     */
    public Window() {
        this(0, 0);
    }

    /**
     * Constructs a window. The window is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported. The specified features will be enabled for the window.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the window has a width of 800
     * pixels and a height of 600 pixels.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @param features the features of the window
     *
     * @since 1
     */
    public Window(final WindowFeature... features) {
        this(0, 0, features);
    }

    /**
     * Constructs a window. The window is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported. The specified features will be enabled for the window.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the window has the specified
     * <tt>width</tt> and
     * <tt>height</tt>.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of the window
     * @throws IllegalArgumentException if width or height are smaller than 1
     *
     * @since 1
     */
    public Window(final int width, final int height, final WindowFeature... features) {
        this.drawables = new Drawables();
        this.eventDispatcher = new EventDispatcher();
        this.events = new Events();
        this.eventDispatcher.addListener(this.events.listener);
        this.frequencyMeter = new FrequencyMeter();
        this.timer = new Timer();
        this.title = Engine.getProgramName();
        this.resetImp(width, height, toSet(features));
        new EventLoop(this).start();
    }

    public void addDrawable(final Drawable drawable) {
        this.drawables.addDrawable(drawable);
        this.eventDispatcher.addListener(drawable);
    }

    /**
     * Adds an event listener to the window. The specified object will receive events for all events listener interfaces
     * it implements.
     *
     * @param listener the event listener
     * @since 1
     */
    public void addEventListener(final Object listener) {
        this.eventDispatcher.addListener(listener);
    }

    /**
     * Closes the window. The window becomes invalid, all subsequent method calls to the window will cause an error.
     *
     * @since 1
     */
    public void close() {
        this.imp.close();
    }

    /**
     * Returns the target frame rate in Hertz.
     *
     * @return the target frame rate
     *
     * @see #setFrameRate(float)
     * @since 1
     */
    public final float getFrameRate() {
        return this.timer.getFrameRate();
    }

    /**
     * @deprecated Use {@link Window#addEventListener(java.lang.Object)} instead.
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
     * Checks for a window feature. Returns <tt>true</tt> if the specified feature is currently enabled for the window.
     *
     * @return <tt>true</tt> if the feature is enabled, otherwise returns
     * <tt>false</tt>
     * @throws NullPointerException if <tt>feature</tt> is <tt>null</tt>
     *
     * @see #setFeature(WindowFeature, boolean)
     * @since 1
     */
    public boolean hasFeature(final WindowFeature feature) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        return this.imp.getFeatures().contains(feature);
    }

    public void removeDrawable(final Drawable drawable) {
        this.drawables.removeDrawable(drawable);
        this.eventDispatcher.removeListener(drawable);
    }

    /**
     * Removes an event listener from the window. The specified object will not receive events anymore.
     *
     * @param listener the event listener
     * @since 1
     */
    public void removeEventListener(final Object listener) {
        this.eventDispatcher.removeListener(listener);
    }

    public void sendAction(final Object source, final String name) {
        this.eventDispatcher.addAction(source, name);
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
     * @since 1
     */
    public void setFeature(final WindowFeature feature, final boolean enabled) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        if (IMP_CHANGING_FEATURES.contains(feature)) {
            final EnumSet<WindowFeature> featureSet =
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
     * Sets the target frame rate in Hertz. This is the frequency in which the window will be refreshed and
     * {@link TickEvent} events will be emitted.
     *
     * @param hertz new frame rate in hertz
     *
     * @see #getFrameRate()
     * @since 1
     */
    public final void setFrameRate(final float hertz) {
        this.timer.setFrameRate(hertz);
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
     * Updates the window. Updates the {@link Events} of the window. If the window has the feature
     * {@link WindowFeature#DoubleBuffered} activated, also flips foreground and background buffer.
     *
     * @since 1
     */
    @Deprecated
    public void update() {
    }

    private void eventLoop() {
        this.timer.start();
        while (this.imp.isValid()) {
            if (this.imp.isActive()) {
                this.frequencyMeter.count();
                this.events.prepare();
                this.eventDispatcher.dispatchEvents(this.imp.fetchEvents());
                final TickEvent event = new TickEvent(this, this.timer.getLastStepDuration(),
                                                      this.frequencyMeter.getFrequency());
                this.eventDispatcher.dispatchTick(event);
                this.drawables.draw(this);
                this.imp.update();
                this.timer.tick();
            }
            else {
                try {
                    Thread.sleep(PAUSE_SLEEP_MILLIS);
                }
                catch (InterruptedException ex) {
                    // ignore
                }
                this.timer.start();
            }
        }
    }

    private void resetImp(final int width, final int height, final EnumSet<WindowFeature> features) {
        if (this.imp != null) {
            this.imp.close();
        }

        this.imp = Engine.getContext().showWindow(width, height, features);
        this.events.reset();
        this.imp.setTitle(this.title);
        if (!this.hasFeature(WindowFeature.DoubleBuffered)) {
            this.imp.setColor(Color.WHITE);
            this.imp.fill();
        }

        super.setImp(this.imp);
    }

    private static EnumSet<WindowFeature> initImpChangingFeatures() {
        final EnumSet<WindowFeature> result = EnumSet.noneOf(WindowFeature.class);
        result.add(WindowFeature.DoubleBuffered);
        result.add(WindowFeature.Fullscreen);
        return result;
    }

    private static EnumSet<WindowFeature> toSet(final WindowFeature... features) {
        final EnumSet<WindowFeature> result = EnumSet.noneOf(WindowFeature.class);
        for (WindowFeature feature : features) {
            result.add(feature);
        }

        return result;
    }

    private static class EventLoop extends Thread {

        private final Window window;

        public EventLoop(Window window) {
            this.window = window;
        }

        @Override
        public void run() {
            this.window.eventLoop();
        }
    }
}
