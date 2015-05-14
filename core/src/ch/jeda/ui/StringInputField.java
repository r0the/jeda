/*
 * Copyright (C) 2014 by Stefan Rothe
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
 * Represents an input field for {@link java.lang.String} values. An input field is a {@link ch.jeda.ui.Widget} that
 * allows the user to enter an {@link java.lang.String} value.
 *
 * @since 1.3
 */
public class StringInputField extends InputField {

    private int maximumLength;
    private String value;

    /**
     * Constructs an input field for String values at the specified position.
     *
     * @param x the x coordinate of the input field
     * @param y the y coordinate of the input field
     *
     * @since 1.3
     */
    public StringInputField(final int x, final int y) {
        this(x, y, Alignment.TOP_LEFT);
    }

    /**
     * Constructs an input field for String values at the specified position with the specified alignment.
     *
     * @param x the x coordinate of the input field
     * @param y the y coordinate of the input field
     * @param alignment specifies how to align the input field relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public StringInputField(final int x, final int y, final Alignment alignment) {
        super(x, y, alignment);
        maximumLength = 20;
        value = "";
    }

    /**
     * Returns the maximum length of text the user is allowed to enter.
     *
     * @return the maximum length of text
     *
     * @see #setMaximumLength(int)
     * @since 1.3
     */
    public final int getMaximumLength() {
        return maximumLength;
    }

    /**
     * Returns the text entered in the input field.
     *
     * @return the text entered in the input field
     *
     * @see #setValue(java.lang.String)
     * @since 1.3
     */
    public final String getValue() {
        return value;
    }

    /**
     * Sets the maximum length of text the user is allowed to enter.
     *
     * @param maximumLength the maximum length of text
     * @throws IllegalArgumentException if <tt>maximumLength</tt> is smaller than <tt>1</tt>
     *
     * @see #getMaximumLength()
     * @since 1.3
     */
    public final void setMaximumLength(final int maximumLength) {
        if (maximumLength < 1) {
            throw new IllegalArgumentException("maximumLength");
        }

        this.maximumLength = maximumLength;
        checkValue();
    }

    /**
     * Sets the text to be edited in the input field.
     *
     * @param value the text to be edited
     *
     * @see #getValue()
     * @since 1.3
     */
    public final void setValue(final String value) {
        this.value = value;
        checkValue();
    }

    @Override
    protected void characterDeleted() {
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
            setDisplayText(value);
        }
    }

    @Override
    protected void characterTyped(final char ch) {
        if (value.length() < maximumLength) {
            value = value + ch;
            setDisplayText(value);
        }
    }

    private void checkValue() {
        if (value == null) {
            value = "";
        }
        else if (value.length() > maximumLength) {
            value = value.substring(0, maximumLength);
        }

        setDisplayText(value);
    }
}
