/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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
 * Configurable features for a view.
 *
 * @since 2.0
 */
public enum ViewFeature {

    /**
     * Sets fullscreen mode for a view. The behaviour of this feature depends on the platform:
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png">
     * Displays the view in fullscreen mode. The screen resolution is automatically selected to fit the view size as
     * closely as possible.
     * <p>
     * <img src="../../../android.png"> Fullscreen mode is not yet supported on Android devices.
     *
     * @since 2.0
     */
    FULLSCREEN,
    /**
     * Enable hovering pointer tracking for a view. If this feature is enabled, the view generates pointer events if the
     * mouse pointer is hovering over the view. Normally, pointer events are only generated when a mouse button is being
     * pressed.
     *
     * @since 2.0
     */
    HOVERING_POINTER,
    /**
     * Set landscape orientation for a view. The behaviour of this feature depends on the platform:
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Has no effect.
     * <p>
     * <img src="../../../android.png"> The screen orientation of the device is set to landscape. Thus, the width of the
     * view's drawing area is larger than it's height.
     *
     * @since 2.0
     */
    ORIENTATION_LANDSCAPE,
    /**
     * Set portrait orientation for a view. The behaviour of this feature depends on the platform:
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> Has no effect.
     * <p>
     * <img src="../../../android.png"> The screen orientation of the device is set to portrait. Thus, the width of the
     * view's drawing area is smaller than it's height.
     *
     * @since 2.0
     */
    ORIENTATION_PORTRAIT,
    /**
     * Enable user scrolling for a view. If this feature is enabled, the view is automatically moved when the user drags
     * a pointer.
     *
     * @since 2.0
     */
    USER_SCROLL,
    /**
     * Enable user scaling for a view. If this feature is enabled, the view is automatically scaled whenn the user turns
     * the mouse wheel.
     *
     * @since 2.0
     */
    USER_SCALE
}
