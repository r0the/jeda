/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
 * Represents a default text style. This class provides methods to control the text appearance, namely text color and
 * size and the typeface.
 *
 * @since 1.3
 */
public class DefaultTextStyle {

    private Color textColor;
    private int textSize;
    private Typeface typeface;

    /**
     * Constructs a default text style.
     *
     * @since 1.3
     */
    public DefaultTextStyle() {
        this(Typeface.SANS_SERIF, 20, Color.BLACK);
    }

    /**
     * Constructs a default text style with the specified typeface, text size and color.
     *
     * @param typeface the initial typeface of this style
     * @param textSize the initial text size of this style
     * @param textColor the initial text color of this style
     *
     * @since 1.3
     */
    public DefaultTextStyle(final Typeface typeface, final int textSize, final Color textColor) {
        this.textColor = textColor;
        this.textSize = textSize;
        this.typeface = typeface;
    }

    /**
     * Applies the text style to a canvas.
     *
     * @param canvas the canvas to apply the style to
     *
     * @since 1.3
     */
    public void applyTextStyle(final Canvas canvas) {
        canvas.setColor(textColor);
        canvas.setTypeface(typeface);
        canvas.setTextSize(textSize);
    }

    /**
     * Returns the current text color of the style.
     *
     * @return the current text color of the style
     *
     * @since 1.3
     */
    public final Color getTextColor() {
        return textColor;
    }

    /**
     * Returns the current text size of the style.
     *
     * @return the current text size of the style
     *
     * @since 1.3
     */
    public final int getTextSize() {
        return textSize;
    }

    /**
     * Returns the current typeface of the style.
     *
     * @return the current typeface of the style
     *
     * @since 1.3
     */
    public final Typeface getTypeface() {
        return typeface;
    }

    /**
     * Sets the text color for this style.
     *
     * @param textColor the text color for this style
     * @throws NullPointerException if <tt>textColor</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public final void setTextColor(final Color textColor) {
        if (textColor == null) {
            throw new NullPointerException("textColor");
        }

        this.textColor = textColor;
    }

    /**
     * Sets the text size for this style.
     *
     * @param textSize the text size for this style
     * @throws IllegalArgumentException if <tt>textSize</tt> is smaller than 1
     *
     * @since 1.3
     */
    public final void setTextSize(final int textSize) {
        if (textSize < 1) {
            throw new IllegalArgumentException("textSize");
        }

        this.textSize = textSize;
    }

    /**
     * Sets the typeface for this style.
     *
     * @param typeface the typeface for this text style
     * @throws NullPointerException if <tt>typeface</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public final void setTypeface(final Typeface typeface) {
        if (typeface == null) {
            throw new NullPointerException("typeface");
        }

        this.typeface = typeface;
    }
}
