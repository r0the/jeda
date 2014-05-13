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

import ch.jeda.Jeda;
import ch.jeda.JedaInternal;
import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
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
    private final EventDispatcher eventDispatcher;
    private final GraphicsItems graphicsItems;
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
        this.graphicsItems = new GraphicsItems(this);
        this.eventDispatcher = new EventDispatcher();
        this.title = Jeda.getProgramName();
        this.resetImp(width, height, toSet(features));
        Jeda.addTickListener(new EventLoop(this));
    }

    /**
     * Adds a {@link ch.jeda.ui.GraphicsItem} to the window. Has no effect if <tt>graphicsItem</tt> is <tt>null</tt>.
     *
     * @param graphicsItem the graphics item to be added to the window
     *
     * @see #remove(ch.jeda.ui.GraphicsItem)
     * @see #getGraphicsItems()
     * @see #getGraphicsItems(java.lang.Class)
     * @since 1
     */
    public final void add(final GraphicsItem graphicsItem) {
        this.graphicsItems.add(graphicsItem);
    }

    /**
     * Adds an event listener to the window. The specified object will receive events for all events listener interfaces
     * it implements. Has no effect if <tt>listener</tt> is <tt>null</tt>.
     *
     * @param listener the event listener
     *
     * @since 1
     */
    public final void addEventListener(final Object listener) {
        this.eventDispatcher.addListener(listener);
    }

    /**
     * Closes the window. The window becomes invalid, all subsequent method calls to the window will cause an error.
     *
     * @since 1
     */
    public final void close() {
        this.imp.close();
    }

    /**
     * Returns all graphics items currently managed by the window.
     *
     * @return all graphics items currently managed by the window.
     *
     * @see #add(ch.jeda.ui.GraphicsItem)
     * @see #getGraphicsItems(java.lang.Class)
     * @see #remove(ch.jeda.ui.GraphicsItem)
     * @since 1
     */
    public final GraphicsItem[] getGraphicsItems() {
        return this.graphicsItems.getAll();
    }

    /**
     * Returns all graphics items of the specified class currently managed by the window.
     *
     * @return all graphics items currently managed by the window.
     * @throws NullPointerException if <tt>clazz</tt> is <tt>null</tt>
     *
     * @see #add(ch.jeda.ui.GraphicsItem)
     * @see #getGraphicsItems()
     * @see #getGraphicsItems(java.lang.Class)
     * @since 1
     */
    public final <T extends GraphicsItem> T[] getGraphicsItems(final Class<T> clazz) {
        return this.graphicsItems.get(clazz);
    }

    /**
     * Returns the current window title.
     *
     * @return current window title
     *
     * @see #setTitle(java.lang.String)
     * @since 1
     */
    public final String getTitle() {
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
    public final boolean hasFeature(final WindowFeature feature) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        return this.imp.getFeatures().contains(feature);
    }

    /**
     * Removes a {@link ch.jeda.ui.GraphicsItem} from the window. Has no effect if <tt>graphicsItem</tt> is
     * <tt>null</tt>.
     *
     * @param graphicsItem the graphics item to be removed from the window
     *
     * @see ch.jeda.ui.GraphicsItem
     * @since 1
     */
    public final void remove(final GraphicsItem item) {
        this.graphicsItems.remove(item);
    }

    /**
     * Removes an event listener from the window. The specified object will not receive events anymore. Has no effect if
     * <tt>listener</tt> is <tt>null</tt>.
     *
     * @param listener the event listener
     * @since 1
     */
    public final void removeEventListener(final Object listener) {
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
     * @see #hasFeature(WindowFeature)
     * @since 1
     */
    public final void setFeature(final WindowFeature feature, final boolean enabled) {
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
     * Sets the shape of the mouse cursor.
     *
     * @param mouseCursor new shape of mouse cursor
     * @throws NullPointerException if <tt>mouseCursor</tt> is
     * <tt>null</tt>
     *
     * @see MouseCursor
     * @since 1
     */
    public final void setMouseCursor(final MouseCursor mouseCursor) {
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
    public final void setTitle(final String title) {
        if (title == null) {
            throw new NullPointerException("title");
        }

        this.title = title;
        this.imp.setTitle(title);
    }

    void sendAction(final Object source, final String name) {
        this.eventDispatcher.addAction(source, name);
    }

    private void tick(final TickEvent event) {
        if (this.imp.isVisible()) {
            this.eventDispatcher.dispatchEvents(this.imp.fetchEvents());
            this.eventDispatcher.dispatchTick(event);
            this.graphicsItems.processPending();
            this.graphicsItems.draw(this);
            this.imp.update();
        }
    }

    private void resetImp(final int width, final int height, final EnumSet<WindowFeature> features) {
        if (this.imp != null) {
            this.imp.close();
        }

        fixWindowFeatures(features);

        this.imp = JedaInternal.createWindowImp(width, height, features);
        this.imp.setTitle(this.title);
        if (!this.hasFeature(WindowFeature.DOUBLE_BUFFERED)) {
            this.imp.setColor(Color.WHITE);
            this.imp.fill();
        }

        super.setImp(this.imp);
    }

    private static void fixWindowFeatures(final EnumSet<WindowFeature> features) {
        if (features.contains(WindowFeature.DoubleBuffered)) {
            features.add(WindowFeature.DOUBLE_BUFFERED);
        }

        if (features.contains(WindowFeature.Fullscreen)) {
            features.add(WindowFeature.FULLSCREEN);
        }

        if (features.contains(WindowFeature.HoveringPointer)) {
            features.add(WindowFeature.HOVERING_POINTER);
        }

        if (features.contains(WindowFeature.OrientationLandscape)) {
            features.add(WindowFeature.ORIENTATION_LANDSCAPE);
        }

        if (features.contains(WindowFeature.OrientationPortrait)) {
            features.add(WindowFeature.ORIENTATION_PORTRAIT);
        }

        if (features.contains(WindowFeature.DOUBLE_BUFFERED)) {
            features.add(WindowFeature.DoubleBuffered);
        }

        if (features.contains(WindowFeature.FULLSCREEN)) {
            features.add(WindowFeature.Fullscreen);
        }

        if (features.contains(WindowFeature.HOVERING_POINTER)) {
            features.add(WindowFeature.HoveringPointer);
        }

        if (features.contains(WindowFeature.ORIENTATION_LANDSCAPE)) {
            features.add(WindowFeature.OrientationLandscape);
        }

        if (features.contains(WindowFeature.ORIENTATION_PORTRAIT)) {
            features.add(WindowFeature.OrientationPortrait);
        }
    }

    private static EnumSet<WindowFeature> initImpChangingFeatures() {
        final EnumSet<WindowFeature> result = EnumSet.noneOf(WindowFeature.class);
        result.add(WindowFeature.DOUBLE_BUFFERED);
        result.add(WindowFeature.FULLSCREEN);
        return result;
    }

    private static EnumSet<WindowFeature> toSet(final WindowFeature... features) {
        final EnumSet<WindowFeature> result = EnumSet.noneOf(WindowFeature.class);
        for (WindowFeature feature : features) {
            result.add(feature);
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
            this.window.tick(event);
        }
    }
}
