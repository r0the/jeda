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
 * Specifies the graphical representation of a progress bar widget.
 *
 * @since 1.3
 */
public interface ProgressBarStyle {

    /**
     * Checks if the specified point (<tt>x</tt>, <tt>y</tt>) lies inside the progress bar widget.
     *
     * @param progressBar the progress bar
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <tt>true</tt> if the specified point lies inside the progress bar, otherwiese <tt>false</tt>
     *
     * @since 1.3
     */
    boolean contains(ProgressBar progressBar, float x, float y);

    /**
     * Draws the progrss bar widget on the specified canvas.
     *
     * @param progressBar the progrss bar to draw
     * @param canvas the canvas to draw on
     *
     * @since 1.3
     */
    void draw(ProgressBar progressBar, Canvas canvas);

    /**
     * Returns the height of the progress bar widget.
     *
     * @param progressBar the progress bar
     * @return the height of the progress bar widget
     *
     * @since 1.3
     */
    int getHeight(ProgressBar progressBar);

    /**
     * Returns the width of the progress bar widget.
     *
     * @param progressBar the progress bar
     * @return the width of the progress bar widget
     *
     * @since 1.3
     */
    int getWidth(ProgressBar progressBar);
}
