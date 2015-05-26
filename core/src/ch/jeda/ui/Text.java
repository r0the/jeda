/*
 * Copyright (C) 2015 by Stefan Rothe
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
 * A widget displaying a text.
 *
 * @since 2.0
 */
public class Text extends TextWidget {

    private String text;

    /**
     * Constructs a new text widget.
     *
     * @param x the x coordinate of the widget
     * @param y the y coordinate of the widget
     *
     * @since 2.0
     */
    public Text(final double x, final double y) {
        this(x, y, null, Alignment.BOTTOM_LEFT);
    }

    /**
     * Constructs a new text widget.
     *
     * @param x the x coordinate of the widget
     * @param y the y coordinate of the widget
     * @param text the text to be displayed
     *
     * @since 2.0
     */
    public Text(final double x, final double y, final String text) {
        this(x, y, text, Alignment.BOTTOM_LEFT);
    }

    /**
     * Constructs a new text widget.
     *
     * @param x the x coordinate of the widget
     * @param y the y coordinate of the widget
     * @param text the text to be displayed
     * @param alignment the alignment of the widget
     *
     * @since 2.0
     */
    public Text(final double x, final double y, final String text, final Alignment alignment) {
        super(x, y, alignment);
        setText(text);
    }

    /**
     * Returns the text.
     *
     * @return the text
     *
     * @see #setText(java.lang.String)
     * @since 2.0
     */
    public final String getText() {
        return text;
    }

    /**
     * Sets the text to be displayed by this widget
     *
     * @param text the text
     *
     * @see #getText()
     * @since 2.0
     */
    public final void setText(final String text) {
        this.text = text;
    }

    @Override
    protected void draw(final Canvas canvas) {
        applyStyle(canvas);
        canvas.drawText(0f, 0f, text);
    }
}
