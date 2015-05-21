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

public abstract class TextWidget extends Widget {

    private float height;
    private String text;
    private Color textColor;
    private float textSize;
    private float width;

    public TextWidget(final double x, final double y, final Alignment alignment) {
        super(x, y, alignment);
        textColor = Color.BLACK;
        width = 3;
        height = 1;
        textSize = 16;
    }

    @Override
    public boolean contains(float x, float y) {
        return Math.abs(getX() - x) <= width / 2 && Math.abs(getY() - y) <= height / 2;
    }

    public float getHeight() {
        return height;
    }

    /**
     * Returns the text.
     *
     * @return the text
     *
     * @since 2.0
     */
    public final String getText() {
        return text;
    }

    public Color getTextColor() {
        return textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public float getWidth() {
        return width;
    }

    public final void resize(final double width, final double height) {
        this.width = (float) width;
        this.height = (float) height;
    }

    /**
     * Sets the display text of this widget
     *
     * @param text the display text of this widget
     *
     * @see #getText()
     * @since 2.0
     */
    public final void setText(final String text) {
        this.text = text;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(double textSize) {
        this.textSize = (float) textSize;
    }
}
