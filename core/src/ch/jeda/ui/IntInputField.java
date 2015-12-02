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
 * Represents an input field for <tt>int</tt> values. An input field is a {@link ch.jeda.ui.Widget} that allows the user
 * to enter an <tt>int</tt> value.
 *
 * @since 1.3
 */
public class IntInputField extends InputField {

    private int maximumValue;
    private int minimumValue;
    private int value;

    /**
     * Constructs an input field for int values at the specified position.
     *
     * @param x the x coordinate of the input field
     * @param y the y coordinate of the input field
     *
     * @since 1.3
     */
    public IntInputField(final int x, final int y) {
        this(x, y, Alignment.BOTTOM_LEFT);
    }

    /**
     * Constructs an input field for int values at the specified position with the specified alignment.
     *
     * @param x the x coordinate of the input field
     * @param y the y coordinate of the input field
     * @param alignment the alignment of the input field
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public IntInputField(final int x, final int y, final Alignment alignment) {
        super(x, y, alignment);
        minimumValue = Integer.MIN_VALUE;
        maximumValue = Integer.MAX_VALUE;
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
        return value;
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
        checkValue();
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
        checkValue();
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
        this.value = value;
        checkValue();
    }

    @Override
    protected void characterDeleted() {
        value = value / 10;
        setDisplayText(Convert.toString(value));
    }

    @Override
    protected void characterTyped(final char ch) {
        if (ch == '-') {
            value = -value;
        }
        else if (Character.isDigit(ch)) {
            final int digit = Character.digit(ch, 10);
            long candidate = digit;
            if (value != 0) {
                candidate = value * 10l + MathUtil.signum(value) * digit;
            }

            if (minimumValue <= candidate && candidate <= maximumValue) {
                value = (int) candidate;
            }
        }

        setDisplayText(Convert.toString(value));
    }

    private void checkValue() {
        value = Math.max(minimumValue, Math.min(value, maximumValue));
        setDisplayText(Convert.toString(value));
    }
}
