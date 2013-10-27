/*
 * Copyright (C) 2013 by Stefan Rothe
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

public enum WindowFeature {

    /**
     * Enables double buffering in a window. A double-buffered window performs drawing operations in an off-screen
     * buffer. The {@link Window#update()} method must be called to display the contents of this buffer. This prevents
     * flickering in animations.
     *
     * @see Window#update()
     * @since 1
     */
    DOUBLE_BUFFERED,
    /**
     * Sets fullscreen mode for a window. The behaviour of this feature depends on the platform:
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png">
     * Displays the window in fullscreen mode. The screen resolution is automatically selected to fit the window size as
     * closely as possible.
     * <p>
     * <img src="../../../android.png"> Fullscreen mode is not yet supported on Android devices.
     *
     * @since 1
     */
    FULLSCREEN,
    /**
     *
     */
    HOVERING_POINTER,
    /**
     * Set landscape orientation for a window. The behaviour of this feature depends on the platform:
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Has no effect.
     * <p>
     * <img src="../../../android.png"> The screen orientation of the device is set to landscape. Thus, the width of the
     * window's drawing area is larger than it's height.
     *
     * @since 1
     */
    ORIENTATION_LANDSCAPE,
    /**
     * Set portrait orientation for a window. The behaviour of this feature depends on the platform:
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Has no effect.
     * <p>
     * <img src="../../../android.png"> The screen orientation of the device is set to portrait. Thus, the width of the
     * window's drawing area is smaller than it's height.
     *
     * @since 1
     */
    ORIENTATION_PORTRAIT,
    /**
     * @deprecated Use {@link #DOUBLE_BUFFERED} instead.
     */
    DoubleBuffered,
    /**
     * @deprecated Use {@link #FULLSCREEN} instead.
     */
    Fullscreen,
    /**
     * @deprecated Use {@link #HOVERING_POINTER} instead.
     */
    HoveringPointer,
    /**
     * @deprecated Use {@link #ORIENTATION_LANDSCAPE} instead.
     */
    OrientationLandscape,
    /**
     * @deprecated Use {@link #ORIENTATION_PORTRAIT} instead.
     */
    OrientationPortrait
}
