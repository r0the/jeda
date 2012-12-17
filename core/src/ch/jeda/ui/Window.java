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
import ch.jeda.platform.WindowImp;
import ch.jeda.Size;
import java.util.EnumSet;

/**
 * Provides a drawing window. The window class has the follwoing functionality:
 * <ul>
 *   <li> fullscreen: the window can be displayed in framed or fullscreen mode.
 *   <li> double buffering: the window supports a double buffering mode for animations.
 *   <li> user input: the window provides means to query keyboard and mouse input.
 * </ul>
 * 
 * @since 1
 */
public class Window extends Canvas {

    public enum Feature {

        DoubleBuffered, Fullscreen, HoveringPointer
    }
    private static final EnumSet<Feature> NO_FEATURES = EnumSet.noneOf(Feature.class);
    private static final EnumSet<Feature> IMP_CHANGING_FEATURES = initImpChangingFeatures();
    private final Events events;
    private WindowImp imp;
    private String title;

    /**
     * Creates a new window with an automatically sized drawing area.
     * On the Android platform, the drawing area is adjusted to the screen
     * size.
     * On the Java platform, the drawing area has a width of 800 and a height
     * of 600 pixels.
     *
     * @since 1
     */
    public static Window create() {
        return new Window(null, NO_FEATURES);
    }

    /**
     * Creates a new window with an automatically sized drawing area.
     * On the Android platform, the drawing area is adjusted to the screen
     * size.
     * On the Java platform, the drawing area has a width of 800 and a height
     * of 600 pixels.
     * The specified window features are activated.
     *
     * @param features the features of this window
     *
     * @since 1
     */
    public static Window create(Feature... features) {
        return new Window(null, toSet(features));
    }

    /**
     * Creates a new window that has a drawing with the specified width and
     * height in pixels.
     * On the Android platform, the size parameter has no effect.
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     *
     * @throws IllegalArgumentException if width or height are smaller than 1
     * 
     * @since 1
     */
    public static Window create(int width, int height) {
        return new Window(Size.from(width, height), NO_FEATURES);
    }

    /**
     * Creates a new window that has a drawing with the specified width and
     * height in pixels.
     * On the Android platform, the size parameter has no effect.
     * The specified window features are activated.
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of this window
     *
     * @throws IllegalArgumentException if width or height are smaller than 1
     * 
     * @since 1
     */
    public static Window create(int width, int height, Feature... features) {
        return new Window(Size.from(width, height), toSet(features));
    }

    /**
     * Creates a new window that has a drawing with the specified size in
     * pixels.
     * On the Android platform, the size parameter has no effect.
     * The specified window features are activated.
     *
     * @param size the size of the drawing area in pixels
     * @param features the features of this window
     *
     * @throws NullPointerException if size is <code>null</code>
     */
    public static Window create(Size size, Feature... features) {
        return new Window(size, toSet(features));
    }

    /**
     * Creates a new window with an automatically sized drawing area.
     * On the Android platform, the drawing area is adjusted to the screen
     * size.
     * On the Java platform, the drawing area has a width of 800 and a height
     * of 600 pixels.
     */
    public Window() {
        this(null, NO_FEATURES);
    }

    /**
     * Creates a new window with an automatically sized drawing area.
     * On the Android platform, the drawing area is adjusted to the screen
     * size.
     * On the Java platform, the drawing area has a width of 800 and a height
     * of 600 pixels.
     * The specified window features are activated.
     *
     * @param features the features of this window
     */
    public Window(Feature... features) {
        this(null, toSet(features));
    }

    /**
     * Creates a new window that has a drawing with the specified width and
     * height in pixels.
     * On the Android platform, the size parameter has no effect.
     * The specified window features are activated.
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of this window
     *
     * @throws IllegalArgumentException if width or height are smaller than 1
     */
    public Window(int width, int height, Feature... features) {
        this(Size.from(width, height), toSet(features));
    }

    /**
     * Closes this window and destroys the window object.
     *
     * @since 1
     */
    public void close() {
        this.imp.close();
    }

    /**
     * Returns an object holding the events that are taking place on this
     * window. The {@link Events} object returned by this method stays valid as
     * long as the window is open. The {@link Events} object is updated only by
     * a call to the {@link #update()} method.
     * 
     * @return {@link Events} object representing the events on this window
     * 
     * @since 1
     */
    public Events getEvents() {
        return this.events;
    }

    /**
     * Returns the window's current title.
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
     * Checks whether this window has the specified feature.
     *
     * @return <code>true</code> if the window has the specified feature, 
     *         otherwise returns <code>false</code>
     * @see #setFeatures(Feature...)
     * @throws NullPointerException if <code>feature</code> is <code>null</code>
     * 
     * @see #setFeature(ch.jeda.ui.Window.Feature, boolean)
     * @since 1
     */
    public boolean hasFeature(Feature feature) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        return this.imp.getFeatures().contains(feature);
    }

    /**
     * Enables or disables the specified feature of this window.
     * 
     * @param feature the feature to be enabled or disabled
     * @param enabled <code>true</code> to enable the feature,
     *                <code>false</code> to disable it
     * @throws NullPointerException if <code>feature</code> is <code>null</code>
     * 
     * @see #hasFeature(ch.jeda.ui.Window.Feature)
     * @since 1
     */
    public void setFeature(Feature feature, boolean enabled) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        if (IMP_CHANGING_FEATURES.contains(feature)) {
            EnumSet<Feature> featureSet = EnumSet.copyOf(this.imp.getFeatures());
            if (enabled) {
                featureSet.add(feature);
            }
            else {
                featureSet.remove(feature);
            }

            this.resetImp(this.getSize(), featureSet);
        }
        else {
            this.imp.setFeature(feature, enabled);
        }
    }

    /**
     * @deprecated use #setFeature(Feature.Fullscreen, fullscreen) instead
     */
    @Deprecated
    public void setFullscreen(boolean fullscreen) {
        this.setFeature(Feature.Fullscreen, fullscreen);
    }

    /**
     * Sets the shape of the mouse cursor.
     * 
     * @param mouseCursor new shape of mouse cursor
     * @throws NullPointerException if <code>mouseCursor</code> is 
     *         <code>null</code>
     * 
     * @since 1
     */
    public void setMouseCursor(MouseCursor mouseCursor) {
        if (mouseCursor == null) {
            throw new NullPointerException("mouseCursor");
        }

        this.imp.setMouseCursor(mouseCursor);
    }

    /**
     * Sets the window's title.
     *
     * @param title new title of the window
     * @throws NullPointerException if <code>title</code> is <code>null</code>
     * 
     * @see #getTitle()
     * @since 1
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new NullPointerException("title");
        }

        this.title = title;
        this.imp.setTitle(title);
    }

    /**
     * Updates this window. Depending on the current bufferMode of this window,
     * this method has different effects.
     * <ul>
     *   <li> It always updated the keyboard and mouse state of this window.
     *   <li> In BufferMode.Double, it flips the hidden and visible screen
     *        buffers.
     *   <li> In BufferMode.SingleLazy, it updates the display.
     * </ul>
     *
     * @since 1
     */
    public void update() {
        this.events.digestEvents(this.imp.update());
    }

    private Window(Size size, EnumSet<Feature> features) {
        this.events = new Events();
        this.title = Thread.currentThread().getName();
        this.resetImp(size, features);
    }

    private void resetImp(Size size, EnumSet<Feature> features) {
        if (this.imp != null) {
            this.imp.close();
        }

        this.imp = Engine.getCurrentEngine().showWindow(size, features);
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
        EnumSet<Feature> result = EnumSet.noneOf(Feature.class);
        result.add(Feature.DoubleBuffered);
        result.add(Feature.Fullscreen);
        return result;
    }

    private static EnumSet<Feature> toSet(Feature... features) {
        EnumSet<Feature> result = EnumSet.noneOf(Feature.class);
        for (Feature feature : features) {
            result.add(feature);
        }

        return result;
    }
}
