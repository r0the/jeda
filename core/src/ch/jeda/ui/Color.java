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

import ch.jeda.Data;
import ch.jeda.Storable;
import java.io.Serializable;

/**
 * Represents a color in the RGBA color model. A color is defined by three values for the red, green, and blue part, and
 * an alpha value defining the color's opacity. Each value can be a number between 0 and 255.
 *
 * @since 1.0
 */
public final class Color implements Serializable, Storable {

    private static final String R = "r";
    private static final String G = "g";
    private static final String B = "b";
    private static final String A = "a";
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
     * The Jeda color. Same as <tt>new Color(126, 218, 66)</tt>.
     *
     * @since 1.0
     */
    public static final Color JEDA = new Color(126, 218, 66);
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
     * The fully transparent color.
     *
     * @since 1.0
     */
    public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
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
     * Constructs a color from an internal value.
     *
     * @param value the internal value
     *
     * @since 1.0
     */
    public Color(final int value) {
        this.value = value;
    }

    /**
     * Constructs a color from serialized data.
     *
     * @param data the serialized data
     *
     * @since 1.2
     */
    public Color(final Data data) {
        this(data.readInt(R), data.readInt(G), data.readInt(B), data.readInt(A));
    }

    /**
     * Constructs an RGB color. The values for the red, green, and blue components of the color can be specified. All
     * values must be in the range from 0 to 255.
     *
     * @param red color's red component
     * @param green color's green component
     * @param blue color's blue component
     * @throws IllegalArgumentException if not all component values are valid (in the range from 0 to 255).
     *
     * @since 1.0
     */
    public Color(final int red, final int green, final int blue) {
        this(red, green, blue, 255);
    }

    /**
     * Constructs an RGBA color. The values for the red, green, blue, and alpha components of the color can be
     * specified. All values must be in the range from 0 to 255.
     *
     * @param red color's red component
     * @param green color's green component
     * @param blue color's blue component
     * @param alpha color's alpha component (opacity)
     * @throws IllegalArgumentException if not all component values are valid (in the range of 0 to 255).
     *
     * @since 1.0
     */
    public Color(final int red, final int green, final int blue, final int alpha) {
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

    /**
     * Constructs a color from an HTML color specification. The string must contain an HTML color that starts with an
     * '#', followed by three two-digit hex values for red, green, and blue (e.g. <b>#ABCDEF</b>).
     *
     * @param value the HTML color specification
     * @throws NullPointerException if <tt>value</tt> is <tt>null</tt>
     * @throws IllegalArgumentException if <tt>value</tt> does not contain a valid HTML color
     *
     * @since 1.0
     */
    public Color(final String value) {
        if (value == null) {
            throw new NullPointerException("value");
        }

        if (value.length() != 7) {
            throw new IllegalArgumentException("'" + value + "' is not a valid HTML color, it's length is not 7.");
        }

        if (value.charAt(0) != '#') {
            throw new IllegalArgumentException("'" + value + "' is not a valid HTML color, it doesn't start with #.");
        }

        final int red = Integer.parseInt(value.substring(1, 3), 16);
        final int green = Integer.parseInt(value.substring(3, 5), 16);
        final int blue = Integer.parseInt(value.substring(5, 7), 16);
        this.value = (255 << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Color) {
            final Color other = (Color) object;
            return this.value == other.value;
        }
        else {
            return false;
        }
    }

    /**
     * Returns the alpha component.
     *
     * @return the alpha component of the color
     *
     * @since 1.0
     */
    public int getAlpha() {
        return 255 & (this.value >> 24);
    }

    /**
     * Returns the blue component.
     *
     * @return the blue component of the color
     *
     * @since 1.0
     */
    public int getBlue() {
        return 255 & this.value;
    }

    /**
     * Returns the green component.
     *
     * @return the green component of the color
     *
     * @since 1.0
     */
    public int getGreen() {
        return 255 & (this.value >> 8);
    }

    /**
     * Returns the red component.
     *
     * @return the red component of the color
     *
     * @since 1.0
     */
    public int getRed() {
        return 255 & (this.value >> 16);
    }

    /**
     * The internal value of the color.
     *
     * @since 1.0
     */
    public int getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return 23 * this.value;
    }

    /**
     * Returns a text representation of the color. The returned text is a CSS 3 color specification of the color. It has
     * the form <tt>"rgb(R, G, B)"</tt>
     * or <tt>"rgba(R, G, B, A)"</tt> where R, G, and B are the red, green, and blue components ranging from 0 to 255
     * and A ist the alpha component ranging from 0 to 1.
     *
     * @return CSS 3 color specification of the color
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
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

    @Override
    public void writeTo(final Data data) {
        data.writeInt(R, this.getRed());
        data.writeInt(G, this.getGreen());
        data.writeInt(B, this.getBlue());
        data.writeInt(A, this.getAlpha());
    }
}
