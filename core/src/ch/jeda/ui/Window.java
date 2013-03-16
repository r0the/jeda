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
import ch.jeda.platform.InputDeviceImp;
import ch.jeda.platform.WindowImp;
import ch.jeda.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

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
    private final List<InputDevice> inputDevices;
    private WindowImp imp;
    private String title;

    /**
     * @deprecated Use {@link #Window()} instead.
     */
    public static Window create() {
        return new Window();
    }

    /**
     * @deprecated Use {@link #Window(Feature... )} instead.
     */
    public static Window create(Feature... features) {
        return new Window(features);
    }

    /**
     * @deprecated Use {@link #Window(int, int, Feature... )} instead.
     */
    public static Window create(int width, int height, Feature... features) {
        return new Window(width, height, features);
    }

    /**
     * Creates a new window with an automatically sized drawing area.
     * On the Android platform, the drawing area is adjusted to the screen
     * size.
     * On the Java platform, the drawing area has a width of 800 and a height
     * of 600 pixels.
     * 
     * @since 1
     */
    public Window() {
        this(new Size(), NO_FEATURES);
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
    public Window(Feature... features) {
        this(new Size(), toSet(features));
    }

    /**
     * Creates a new window that has a drawing area with the specified width
     * and height in pixels.
     * On the Android platform, the width and height parameters have no effect.
     * The specified window features are activated.
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of this window
     *
     * @throws IllegalArgumentException if width or height are smaller than 1
     * @since 1
     */
    public Window(int width, int height, Feature... features) {
        this(new Size(width, height), toSet(features));
    }

    /**
     * Creates a new window that has a drawing with the specified size in pixels.
     * On the Android platform, the size parameter has no effect.
     * The specified window features are activated.
     *
     * @param size the size of the drawing area in pixels
     * @param features the features of this window
     *
     * @throws IllegalArgumentException if size.width or size.height are
     *         smaller than 1.
     * @since 1
     */
    public Window(Size size, Feature... features) {
        this(size, toSet(features));
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
     * Detects input devices and returns a list of all available devices.
     * 
     * @return list of available input devices
     * @see InputDevice
     */
    public List<InputDevice> detectInputDevices() {
        this.inputDevices.clear();
        for (InputDeviceImp imp : this.imp.detectInputDevices()) {
            this.inputDevices.add(new InputDevice(imp));
        }

        return Collections.unmodifiableList(this.inputDevices);
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
     * Updates this window. Updates the {@link Events} of this window. When
     * this window has the feature {@link Feature#DoubleBuffered} activiated,
     * also flips foreground and background buffer.
     *
     * @since 1
     */
    public void update() {
        this.events.digestEvents(this.imp.update());
        for (InputDevice inputDevice : this.inputDevices) {
            inputDevice.update();
        }
    }

    private Window(Size size, EnumSet<Feature> features) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        this.events = new Events();
        this.inputDevices = new ArrayList();
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
