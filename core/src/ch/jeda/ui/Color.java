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
 * @version 1
 */
public final class Color implements Serializable {

    /**
     * The VGA color <i>aqua</i>. Same as <tt>new Color(0, 255, 255)</tt>.
     *
     * @since 1
     */
    public static final Color AQUA = fromRGB(0, 255, 255);
    /**
     * The VGA color <i>black</i>. Same as <tt>new Color(0, 0, 0)</tt>.
     *
     * @since 1
     */
    public static final Color BLACK = fromRGB(0, 0, 0);
    /**
     * The VGA color <i>blue</i>. Same as <tt>new Color(0, 0, 255)</tt>.
     *
     * @since 1
     */
    public static final Color BLUE = fromRGB(0, 0, 255);
    /**
     * The VGA color <i>fuchsia</i>. Same as <tt>new Color(255, 0, 255)</tt>.
     *
     * @since 1
     */
    public static final Color FUCHSIA = fromRGB(255, 0, 255);
    /**
     * The VGA color <i>gray</i>. Same as <tt>new Color(128, 128, 128)</tt>.
     *
     * @since 1
     */
    public static final Color GRAY = fromRGB(128, 128, 128);
    /**
     * The VGA color <i>green</i>. Same as <tt>new Color(0, 128, 0)</tt>.
     *
     * @since 1
     */
    public static final Color GREEN = fromRGB(0, 128, 0);
    /**
     * The VGA color <i>lime</i>. Same as <tt>new Color(0, 255, 0)</tt>.
     *
     * @since 1
     */
    public static final Color LIME = fromRGB(0, 255, 0);
    /**
     * The VGA color <i>maroon</i>. Same as <tt>new Color(128, 0, 0)</tt>.
     *
     * @since 1
     */
    public static final Color MAROON = fromRGB(128, 0, 0);
    /**
     * The VGA color <i>navy</i>. Same as <tt>new Color(0, 0, 128)</tt>.
     *
     * @since 1
     */
    public static final Color NAVY = fromRGB(0, 0, 128);
    /**
     * The fully transparent color.
     */
    public static final Color NONE = fromRGBA(255, 255, 255, 0);
    /**
     * The VGA color <i>olive</i>. Same as <tt>new Color(128, 128, 0)</tt>.
     *
     * @since 1
     */
    public static final Color OLIVE = fromRGB(128, 128, 0);
    /**
     * The VGA color <i>purple</i>. Same as <tt>new Color(128, 0, 128)</tt>.
     *
     * @since 1
     */
    public static final Color PURPLE = fromRGB(128, 0, 128);
    /**
     * The VGA color <i>red</i>. Same as <tt>new Color(255, 0, 0)</tt>.
     *
     * @since 1
     */
    public static final Color RED = fromRGB(255, 0, 0);
    /**
     * The VGA color <i>silver</i>. Same as <tt>new Color(192, 192, 192)</tt>.
     *
     * @since 1
     */
    public static final Color SILVER = fromRGB(192, 192, 192);
    /**
     * The VGA color <i>teal</i>. Same as <tt>new Color(0, 128, 128)</tt>.
     *
     * @since 1
     */
    public static final Color TEAL = fromRGB(0, 128, 128);
    /**
     * The VGA color <i>white</i>. Same as <tt>new Color(255, 255, 255)</tt>.
     *
     * @since 1
     */
    public static final Color WHITE = fromRGB(255, 255, 255);
    /**
     * The VGA color <i>yellow</i>. Same as <tt>new Color(255, 255, 0)</tt>.
     *
     * @since 1
     */
    public static final Color YELLOW = fromRGB(255, 255, 0);
    /**
     * @since 1
     */
    public final int value;

    /**
     * Returns a color from the specified value.
     * 
     * @since 1
     */
    public static Color from(int value) {
        return new Color(value);
    }

    /**
     * Returns a color with the specified red, green, and blue components.
     * Returns <code>null</code> if not all component values are valid (in
     * the range of 0 to 255).
     *
     * @param red color's red component
     * @param green color's green component
     * @param blue color's blue component
     * @return color with specified components or <code>null</code>
     * 
     * @since 1
     */
    public static Color fromRGB(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 ||
            blue < 0 || blue > 255) {
            return null;
        }
        else {
            return new Color((255 << 24) | (red << 16) | (green << 8) | blue);
        }
    }

    /**
     * Returns a color with the specified red, green, and blue componentns,
     * and the specified opacity.
     * Returns <code>null</code> if not all component values are valid (in
     * the range of 0 to 255).
     * 
     * @param red color's red component
     * @param green color's green component
     * @param blue color's blue component
     * @param alpha color's opacity
     * @return color with specified components or <code>null</code>
     * 
     * @since 1
     */
    public static Color fromRGBA(int red, int green, int blue, int alpha) {
        if (red < 0 || red > 255 || green < 0 || green > 255 ||
            blue < 0 || blue > 255 || alpha < 0 || alpha > 255) {
            return null;
        }
        else {
            return new Color((alpha << 24) | (red << 16) | (green << 8) | blue);
        }
    }

    /**
     * Creates a new color with the specified red, green, and blue components.
     *
     * @param red color's red component
     * @param green color's green component
     * @param blue color's blue component
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
            final Color other = (Color) object;
            return this.value == other.value;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the alpha component of this color.
     * 
     * @return the alpha component of this color
     * 
     * @since 1
     */
    public int getAlpha() {
        return 255 & (this.value >> 24);
    }

    /**
     * Returns the blue component of this color.
     * 
     * @return the blue component of this color
     * 
     * @since 1
     */
    public int getBlue() {
        return 255 & this.value;
    }

    /**
     * Returns the green component of this color.
     * 
     * @return the green component of this color
     * 
     * @since 1
     */
    public int getGreen() {
        return 255 & (this.value >> 8);
    }

    /**
     * Returns the red component of this color.
     * 
     * @return the red component of this color
     * 
     * @since 1
     */
    public int getRed() {
        return 255 & (this.value >> 16);
    }

    @Override
    public int hashCode() {
        return 23 * this.value;
    }

    /**
     * Returns a CSS 3 color specification of this color. It has the form
     * "rgb(R, G, B)" or "rgba(R, G, B, A)" where R, G, and B are the red,
     * green, and blue components ranging from 0 to 255 and A ist the alpha
     * component ranging from 0 to 1.
     * 
     * @return CSS 3 color specification of this color
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        final int alpha = this.getAlpha();
        if (alpha != 255) {
            result.append("rgba(");
        }
        else {
            result.append("rgb(");
        }

        result.append(this.getRed());
        result.append(", ");
        result.append(this.getGreen());
        result.append(", ");
        result.append(this.getBlue());
        if (alpha != 255) {
            result.append(", ");
            result.append(alpha / 255.0);
        }

        result.append(")");
        return result.toString();
    }

    private Color(int value) {
        this.value = value;
    }
}
