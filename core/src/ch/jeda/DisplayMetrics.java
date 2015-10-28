/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda;

/**
 * Contains information about the display.
 *
 * @since 2.0
 * @version 2
 */
public final class DisplayMetrics {

    /**
     * Factor for conversion from meters to device-independent pixels. The factor is equal to
     * <code>100f * 160f / 2.54f</code>.
     *
     * @since 2.4
     */
    public static final float METER_TO_DP = 100f * 160f / 2.54f;
    /**
     * Factor for conversion from device-independent pixels to meters. The factor is equal to
     * <code>1f / METER_TO_DP</code>.
     *
     * @since 2.4
     */
    public static final float DP_TO_METER = 1f / METER_TO_DP;
    private final int dpi;
    private final int displayHeight;
    private final int displayWidth;

    /**
     * Constructs a display metrics object.
     *
     * @param dpi the dots per inch resolution of the display
     * @param displayWidth the width of the display in pixels
     * @param displayHeight the height of the display in pixels
     *
     * @since 2.0
     */
    public DisplayMetrics(final int dpi, final int displayWidth, final int displayHeight) {
        this.dpi = dpi;
        this.displayHeight = displayHeight;
        this.displayWidth = displayWidth;
    }

    /**
     * Converts a lengh in density-independent pixels to pixels based on the resolution of the display.
     *
     * @param dp the length in density-independent pixels to convert
     *
     * @return the length in pixels
     *
     * @since 2.0
     */
    public int dpToPx(final float dp) {
        return (int) (dp * dpi / 160f);
    }

    /**
     * Returns the height of the display in pixels.
     *
     * @return the height of the display in pixels
     *
     * @since 2.0
     */
    public int getDisplayHeight() {
        return displayHeight;
    }

    /**
     * Returns the dots per inch resolution of the display.
     *
     * @return the dots per inch resolution of the display
     *
     * @since 2.0
     */
    public int getDpi() {
        return dpi;
    }

    /**
     * Returns the width of the display in pixels.
     *
     * @return the width of the display in pixels
     *
     * @since 2.0
     */
    public int getDisplayWidth() {
        return displayWidth;
    }

    /**
     * Converts a lenght in meters to pixels based on the resolution of the display.
     *
     * @param m the length in meters
     * @return the length in pixels
     *
     * @since 2.4
     */
    public int mToPx(final float m) {
        return (int) (m * METER_TO_DP * dpi / 160f);
    }

    /**
     * Converts a length in pixels to density-independent pixels based on the resolution of the display.
     *
     * @param px the length in pixels to convert
     * @return the length in density-independent pixels
     *
     * @since 2.0
     */
    public float pxToDp(final float px) {
        return px * 160f / dpi;
    }

    /**
     * Converts a length in pixels to meters based on the resolution of the display
     *
     * @param px the length in pixels to convert
     * @return the length in meters
     *
     * @since 2.4
     */
    public float pxToM(final float px) {
        return px * DP_TO_METER * 160f / dpi;
    }
}
