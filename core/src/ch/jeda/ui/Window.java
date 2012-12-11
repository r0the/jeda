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
import ch.jeda.platform.ViewImp;
import ch.jeda.Size;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Provides a drawing window. The window class has the follwoing functionality:
 * <ul>
 *   <li> fullscreen: the window can be displayed in framed or fullscreen mode.
 *   <li> double buffering: the window supports a double buffering mode for animations.
 *   <li> user input: the window provides means to query keyboard and mouse input.
 * </ul>
 */
public class Window extends Canvas {

    public enum Feature {

        DoubleBuffered, Fullscreen
    }
    private final Events events;
    private ViewImp imp;
    private String title;

    /**
     * Creates a new window with an automatically sized drawing area.
     * On the Android platform, the drawing area is adjusted to the screen
     * size.
     * On the Java platform, the drawing area has a width of 800 and a height
     * of 600 pixels.
     *
     * @since 1.0
     */
    public static Window create() {
        return new Window();
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
     * @since 1.0
     */
    public static Window create(Feature... features) {
        return new Window(Size.EMPTY, features);
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
     * @since 1.0
     */
    public static Window create(int width, int height) {
        return new Window(Size.from(width, height));
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
     * @since 1.0
     */
    public static Window create(int width, int height, Feature... features) {
        return new Window(Size.from(width, height), features);
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
     * 
     * @since 1.0
     */
    public static Window create(Size size, Feature... features) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        return new Window(size, features);
    }

    /**
     * Creates a new window with an automatically sized drawing area.
     * On the Android platform, the drawing area is adjusted to the screen
     * size.
     * On the Java platform, the drawing area has a width of 800 and a height
     * of 600 pixels.
     */
    public Window() {
        this(Size.EMPTY);
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
        this(Size.EMPTY, features);
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
        this(Size.from(width, height), features);
    }

    /**
     * Closes this window and destroys the window object.
     *
     * @since 1.0
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
     * @since 1.0
     */
    public Events getEvents() {
        return this.events;
    }

    /**
     * Returns the window's current title.
     *
     * @return current window title
     * @see #setTitle(java.lang.String)
     * @since 1.0
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
     * 
     * @since 1.0
     */
    public boolean hasFeature(Feature feature) {
        return this.imp.getFeatures().contains(feature);
    }

    public void setFeature(Feature feature, boolean enabled) {
        EnumSet<Feature> featureSet = EnumSet.copyOf(this.imp.getFeatures());
        if (enabled) {
            featureSet.add(feature);
        }
        else {
            featureSet.remove(feature);
        }

        this.resetImp(this.getSize(), featureSet);
    }

    public void setFeatures(Feature... features) {
        this.resetImp(this.getSize(), toSet(features));
    }

    /**
     * @deprecated use #setFeature(Feature.Fullscreen, fullscreen) instead
     */
    @Deprecated
    public void setFullscreen(boolean fullscreen) {
        this.setFeature(Feature.Fullscreen, fullscreen);
    }

    public void setMouseCursor(MouseCursor mouseCursor) {
        this.imp.setMouseCursor(mouseCursor);
    }

    /**
     * Sets the window's title.
     *
     * @param title new title of the window
     * @throws NullPointerException if title is null
     * @see #getTitle()
     * @since 1.0
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
     * @since 1.0
     */
    public void update() {
        this.imp.update();
    }

    private Window(Size size, Feature... features) {
        this.events = new Events();
        this.title = Thread.currentThread().getName();
        this.resetImp(size, toSet(features));
    }

    private void resetImp(Size size, EnumSet<Feature> features) {
        if (this.imp != null) {
            this.imp.close();
        }

        this.imp = Engine.getCurrentEngine().showView(size, features);
        this.events.setImp(this.imp.getEventsImp());
        this.imp.setTitle(this.title);
        if (!this.hasFeature(Feature.DoubleBuffered)) {
            this.imp.setColor(Color.WHITE);
            this.imp.fill();
        }

        super.setImp(this.imp);
        this.update();
    }

    private static EnumSet<Feature> toSet(Feature... features) {
        EnumSet<Feature> result = EnumSet.noneOf(Feature.class);
        result.addAll(Arrays.asList(features));
        return result;
    }
}
