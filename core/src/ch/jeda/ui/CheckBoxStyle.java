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
 * Specifies the graphical representation of a check box widget.
 *
 * @since 1.3
 */
public interface CheckBoxStyle {

    /**
     * Checks if the specified point (<tt>x</tt>, <tt>y</tt>) lies inside the check box widget.
     *
     * @param checkBox the check box
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <tt>true</tt> if the specified point lies inside the check box, otherwiese <tt>false</tt>
     *
     * @since 1.3
     */
    boolean contains(CheckBox checkBox, float x, float y);

    /**
     * Draws the check box widget on the specified canvas.
     *
     * @param checkBox the check box to draw
     * @param canvas the canvas to draw on
     *
     * @since 1.3
     */
    void draw(CheckBox checkBox, Canvas canvas);

    /**
     * Returns the height of the check box widget.
     *
     * @param checkBox the check box
     * @return the height of the check box widget
     *
     * @since 1.3
     */
    int getHeight(CheckBox checkBox);

    /**
     * Returns the width of the check box widget.
     *
     * @param checkBox the check box
     * @return the width of the check box widget
     *
     * @since 1.3
     */
    int getWidth(CheckBox checkBox);
}
