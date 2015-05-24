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
 * Specifies the graphical representation of an input field widget.
 *
 * @since 1.3
 */
public interface InputFieldStyle {

    /**
     * Checks if the specified point (<tt>x</tt>, <tt>y</tt>) lies inside the input field widget.
     *
     * @param inputField the inputField
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <tt>true</tt> if the specified point lies inside the input field, otherwiese <tt>false</tt>
     *
     * @since 1.3
     */
    boolean contains(InputField inputField, float x, float y);

    /**
     * Draws the input field widget on the specified canvas.
     *
     * @param inputField the input field to draw
     * @param visibleText the visible part of the text to draw
     * @param canvas the canvas to draw on
     *
     * @since 1.3
     */
    void draw(InputField inputField, String visibleText, Canvas canvas);

    /**
     * Checks if the specified text would fit in the graphical representation of the input widget. This method is used
     * by the {@link ch.jeda.ui.InputField} widget to determine the visible portion of the edited text.
     *
     * @param inputField the input field
     * @param canvas the canvas
     * @param text the text to check
     * @return <tt>true</tt> if the specified text would fit, otherwise <tt>false</tt>
     */
    boolean fits(InputField inputField, Canvas canvas, String text);

    /**
     * Returns the height of the input field widget.
     *
     * @param inputField the input field
     * @return the height of the input field widget
     *
     * @since 1.3
     */
    int getHeight(InputField inputField);

    /**
     * Returns the width of the input field widget.
     *
     * @param inputField the input field
     * @return the width of the input field widget
     *
     * @since 1.3
     */
    int getWidth(InputField inputField);
}
