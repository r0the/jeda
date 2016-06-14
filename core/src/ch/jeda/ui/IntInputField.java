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

import ch.jeda.Convert;

/**
 * Represents an input field for <code>int</code> values. An input field is a {@link ch.jeda.ui.Widget}.
 *
 * @since 1.3
 */
public class IntInputField extends InputField {

    private static final char NEGATE_CHAR = '-';
    private int maximumValue;
    private int minimumValue;
    private String text;

    /**
     * Constructs an input field for <code>int</code> values at the specified position.
     *
     * @param x the horizontal coordinate of this input field
     * @param y the vertical coordinate of this input field
     *
     * @since 1.3
     */
    public IntInputField(final int x, final int y) {
        this(x, y, Alignment.BOTTOM_LEFT);
    }

    /**
     * Constructs an input field for <code>int</code> values at the specified position with the specified alignment.
     *
     * @param x the horizontal coordinate of this input field
     * @param y the vertical coordinate of this input field
     * @param alignment the alignment of the input field
     * @throws NullPointerException if <code>alignment</code> is <code>null</code>
     *
     * @since 1.3
     */
    public IntInputField(final int x, final int y, final Alignment alignment) {
        super(x, y, alignment);
        minimumValue = Integer.MIN_VALUE;
        maximumValue = Integer.MAX_VALUE;
        text = "";
        setValue(0);
    }

    /**
     * Returns the maximum value to be entered in the input field.
     *
     * @return the maximum value to be entered in the input field
     *
     * @see #getMinimumValue()
     * @see #setMaximumValue(int)
     * @see #setMinimumValue(int)
     * @since 1.3
     */
    public final int getMaximumValue() {
        return maximumValue;
    }

    /**
     * Returns the minimum value to be entered in the input field.
     *
     * @return the minimum value to be entered in the input field
     *
     * @see #getMaximumValue()
     * @see #setMaximumValue(int)
     * @see #setMinimumValue(int)
     * @since 1.3
     */
    public final int getMinimumValue() {
        return minimumValue;
    }

    /**
     * Returns the current value entered in the input field.
     *
     * @return the current value entered in the input field
     *
     * @see #setValue(int)
     * @since 1.3
     */
    public final int getValue() {
        try {
            int value = Integer.parseInt(text);
            if (minimumValue <= value && value <= maximumValue) {
                return value;
            }
        }
        catch (NumberFormatException ex) {
        }

        return 0;
    }

    /**
     * Checks if the current input is a valid <code>int</code> in the specified range.
     *
     * @return <code>true</code> is the current input is valid, otherwise <code>false</code>
     *
     * @since 2.6
     */
    public final boolean isValid() {
        try {
            int value = Integer.parseInt(text);
            return minimumValue <= value && value <= maximumValue;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Sets the maximum value to be entered in the input field.
     *
     * @param maximumValue the maximum value to be entered in the input field
     *
     * @see #getMaximumValue()
     * @see #getMinimumValue()
     * @see #setMinimumValue(int)
     * @since 1.3
     */
    public final void setMaximumValue(final int maximumValue) {
        this.maximumValue = maximumValue;
    }

    /**
     * Sets the minimum value to be entered in the input field.
     *
     * @param minimumValue the minimum value to be entered in the input field
     *
     * @see #getMaximumValue()
     * @see #getMinimumValue()
     * @see #setMaximumValue(int)
     * @since 1.3
     */
    public final void setMinimumValue(final int minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * Set the current value to be edited in the input field.
     *
     * @param value the current value to be edited in the input field
     *
     * @see #getValue()
     * @since 1.3
     */
    public final void setValue(final int value) {
        text = Convert.toString(value);
    }

    @Override
    protected void characterDeleted() {
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
            // Delete a leading negate sign
            if (text.equals("-")) {
                text = "";
            }

            setDisplayText(text);
        }
    }

    @Override
    protected void characterTyped(final char ch) {
        if (ch == NEGATE_CHAR) {
            if (text.length() > 0 && text.charAt(0) == NEGATE_CHAR) {
                text = text.substring(1);
            }
            else {
                text = NEGATE_CHAR + text;
            }
        }
        else if (Character.isDigit(ch)) {
            text = text + ch;
        }

        setDisplayText(text);
    }
}
