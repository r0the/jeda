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

/**
 * Represents a drawing window. The window class has the following functionality:
 * <ul>
 * <li> fullscreen: the window can be displayed in framed or fullscreen mode.
 * <li> double buffering: the window supports a double buffering mode for animations.
 * <li> user input: the window provides means to query keyboard and mouse input.
 * </ul>
 *
 * @since 1.0
 * @version 4
 */
public class Window extends View {

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
     * @since 1.0
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
     * @since 1.0
     */
    public Window(final WindowFeature... features) {
        this(0, 0, features);
    }

    /**
     * Constructs a window. The window is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported. The specified features will be enabled for the window.
     * <p>
     * The size of the window's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the window has the specified
     * <tt>width</tt> and
     * <tt>height</tt>.
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
        super(width, height, convertFeatures(features));
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

        return this.hasFeature(convertFeature(feature));
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
        this.setFeature(convertFeature(feature), enabled);
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

    private static ViewFeature[] convertFeatures(final WindowFeature[] windowFeatures) {
        ViewFeature[] result = new ViewFeature[windowFeatures.length];
        for (int i = 0; i < windowFeatures.length; ++i) {
            result[i] = convertFeature(windowFeatures[i]);
        }

        return result;
    }
}
