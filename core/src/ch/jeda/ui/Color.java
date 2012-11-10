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

import java.io.Serializable;

/**
 * Represents a color in the RGBA color model. A color is defined by three
 * values for the red, green, and blue part, and an alpha value defining
 * the color's opacity. Each can be a number between 0 and 255.
 *
 * @version 1.0
 */
public final class Color implements Serializable {

    /**
     * The VGA color <i>aqua</i>. Same as <tt>new Color(0, 255, 255)</tt>.
     *
     * @since 1.0
     */
    public static final Color AQUA = new Color(0, 255, 255);
    /**
     * The VGA color <i>black</i>. Same as <tt>new Color(0, 0, 0)</tt>.
     *
     * @since 1.0
     */
    public static final Color BLACK = new Color(0, 0, 0);
    /**
     * The VGA color <i>blue</i>. Same as <tt>new Color(0, 0, 255)</tt>.
     *
     * @since 1.0
     */
    public static final Color BLUE = new Color(0, 0, 255);
    /**
     * The VGA color <i>fuchsia</i>. Same as <tt>new Color(255, 0, 255)</tt>.
     *
     * @since 1.0
     */
    public static final Color FUCHSIA = new Color(255, 0, 255);
    /**
     * The VGA color <i>gray</i>. Same as <tt>new Color(128, 128, 128)</tt>.
     *
     * @since 1.0
     */
    public static final Color GRAY = new Color(128, 128, 128);
    /**
     * The VGA color <i>green</i>. Same as <tt>new Color(0, 128, 0)</tt>.
     *
     * @since 1.0
     */
    public static final Color GREEN = new Color(0, 128, 0);
    /**
     * The VGA color <i>lime</i>. Same as <tt>new Color(0, 255, 0)</tt>.
     *
     * @since 1.0
     */
    public static final Color LIME = new Color(0, 255, 0);
    /**
     * The VGA color <i>maroon</i>. Same as <tt>new Color(128, 0, 0)</tt>.
     *
     * @since 1.0
     */
    public static final Color MAROON = new Color(128, 0, 0);
    /**
     * The VGA color <i>navy</i>. Same as <tt>new Color(0, 0, 128)</tt>.
     *
     * @since 1.0
     */
    public static final Color NAVY = new Color(0, 0, 128);
    /**
     * The fully transparent color.
     */
    public static final Color NONE = new Color(255, 255, 255, 0);
    /**
     * The VGA color <i>olive</i>. Same as <tt>new Color(128, 128, 0)</tt>.
     *
     * @since 1.0
     */
    public static final Color OLIVE = new Color(128, 128, 0);
    /**
     * The VGA color <i>purple</i>. Same as <tt>new Color(128, 0, 128)</tt>.
     *
     * @since 1.0
     */
    public static final Color PURPLE = new Color(128, 0, 128);
    /**
     * The VGA color <i>red</i>. Same as <tt>new Color(255, 0, 0)</tt>.
     *
     * @since 1.0
     */
    public static final Color RED = new Color(255, 0, 0);
    /**
     * The VGA color <i>silver</i>. Same as <tt>new Color(192, 192, 192)</tt>.
     *
     * @since 1.0
     */
    public static final Color SILVER = new Color(192, 192, 192);
    /**
     * The VGA color <i>teal</i>. Same as <tt>new Color(0, 128, 128)</tt>.
     *
     * @since 1.0
     */
    public static final Color TEAL = new Color(0, 128, 128);
    /**
     * The VGA color <i>white</i>. Same as <tt>new Color(255, 255, 255)</tt>.
     *
     * @since 1.0
     */
    public static final Color WHITE = new Color(255, 255, 255);
    /**
     * The VGA color <i>yellow</i>. Same as <tt>new Color(255, 255, 0)</tt>.
     *
     * @since 1.0
     */
    public static final Color YELLOW = new Color(255, 255, 0);
    private final int value;

    /**
     * Creates a new color from the specified value.
     * 
     * @since 1.0
     */
    public Color(int value) {
        this.value = value;
    }

    /**
     * Creates a new color with the specified red, green, and blue components.
     *
     * @param red color's red component
     * @param green color's green component
     * @param blue color's blue component
     * 
     * @since 1.0
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    /**
     * Creates a new color with the specified red, green, and blue componentns,
     * and the specified opacity.
     *
     * @param red color's red component
     * @param green color's green component
     * @param blue color's blue component
     * @param alpha color's opacity
     * 
     * @since 1.0
     */
    public Color(int red, int green, int blue, int alpha) {
        if (red < 0 || 255 < red) {
            throw new IllegalArgumentException("red");
        }

        if (green < 0 || 255 < green) {
            throw new IllegalArgumentException("green");
        }

        if (blue < 0 || 255 < blue) {
            throw new IllegalArgumentException("blue");
        }

        if (alpha < 0 || 255 < alpha) {
            throw new IllegalArgumentException("alpha");
        }

        this.value = (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Color) {
            return this.value == ((Color) object).value;
        }
        else {
            return false;
        }
    }

    /**
     * 
     * @since 1.0
     */
    public int getAlpha() {
        return 255 & (this.value >> 24);
    }

    /**
     * 
     * @since 1.0
     */
    public int getBlue() {
        return 255 & this.value;
    }

    public int getGreen() {
        return 255 & (this.value >> 8);
    }

    /**
     * 
     * @since 1.0
     */
    public int getRed() {
        return 255 & (this.value >> 16);
    }

    /**
     * 
     * @since 1.0
     */
    public int getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Color(red=");
        result.append(this.getRed());
        result.append(", green=");
        result.append(this.getGreen());
        result.append(", blue=");
        result.append(this.getBlue());
        int alpha = this.getAlpha();
        if (alpha != 255) {
            result.append(", alpha=");
            result.append(alpha);
        }
        result.append(")");
        return result.toString();
    }
}
