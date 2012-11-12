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
    /**
     * The default height of the drawing area of a window.
     *
     * @since 1.0
     */
    public static final int DEFAULT_HEIGHT = 600;
    /**
     * The default width of the drawing area of a window.
     *
     * @since 1.0
     */
    public static final int DEFAULT_WIDTH = 800;
    private final KeyEvents keyEvents;
//    private final Mouse mouse;
    private ViewImp imp;
    private Size size;
    private String title;

    /**
     * Creates a new window that has a drawing area of default size as defined
     * by {@link #DEFAULT_WIDTH} and {@link #DEFAULT_HEIGHT}.
     *
     * @since 1.0
     */
    public Window() {
        this(new Size());
    }

    public Window(Feature... features) {
        this(new Size(), features);
    }

    /**
     * Creates a new window that has a drawing with the specified width and
     * height in pixels.
     *
     * @param width width of the drawing area in pixels
     * @param height height of the drawing area in pixels
     *
     * @throws IllegalArgumentException if width or height are smaller than 1
     * @since 1.0
     */
    public Window(int width, int height, Feature... features) {
        this(new Size(width, height), features);
    }

    private Window(Size size, Feature... features) {
        super();
        this.keyEvents = new KeyEvents();
//        this.mouse = new Mouse();
        if (size.isEmpty()) {
            size = new Size(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }

        this.size = size;
        this.title = Thread.currentThread().getName();
        this.resetImp(toSet(features));
    }

    /**
     * Closes this window and destroys the window object.
     *
     * @since 1.0
     */
    public void close() {
        this.imp.close();
    }

    public KeyEvents getKeyEvents() {
        return this.keyEvents;
    }
//
//    @Deprecated
//    public Keyboard keyboard() {
//        return this.keyboard;
//    }
//
//    @Deprecated
//    public Mouse mouse() {
//        return this.mouse;
//    }

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
     * @return true if the window has the specified feature.
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

        this.resetImp(featureSet);
    }

    public void setFeatures(Feature... features) {
        this.resetImp(toSet(features));
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

    private void resetImp(EnumSet<Feature> features) {
        if (this.imp != null) {
            this.imp.close();
        }

        this.imp = Engine.getCurrentEngine().showView(this.size, features);
        this.keyEvents.setImp(this.imp.getKeyEventsImp());
//        this.mouse.setImp(this.imp);
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
        for (Feature feature : features) {
            result.add(feature);
        }

        return result;
    }
}
