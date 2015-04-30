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
     * Enables double buffering in a view. A double-buffered view performs drawing operations in an off-screen buffer.
     * This prevents flickering in animations.
     *
     * @since 2.0
     */
    DOUBLE_BUFFERED,
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
     * Enable automatic scrolling on drag for a view. If this feature is enabled, the view automatically translates
     * pointer drag movements to scroll events.
     *
     * @since 2.0
     */
    SCROLLABLE
}
