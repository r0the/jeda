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
 * Represents a widget displaying a text.
 *
 * @since 2.0
 */
public abstract class TextWidget extends Widget {

    private static final int DEFAULT_TEXT_SIZE = 16;
    private static final Typeface DEFAULT_TYPEFACE = Typeface.SANS_SERIF;
    private float height;
    private Color textColor;
    private float textSize;
    private Typeface typeface;
    private float width;

    /**
     * Constructs a new text widget.
     *
     * @param x the horizontal alignment coordinate of this widget
     * @param y the vertical alignment coordinate of this widget
     * @param alignment the alignment of this widget
     *
     * @since 2.0
     */
    protected TextWidget(final double x, final double y, final Alignment alignment) {
        super(x, y, alignment);
        textColor = Color.BLACK;
        width = 80f;
        height = 48f;
        textSize = DEFAULT_TEXT_SIZE;
        typeface = DEFAULT_TYPEFACE;
    }

    @Override
    public final float getHeight() {
        return height;
    }

    /**
     * Returns the text color.
     *
     * @return the text color
     *
     * @since 2.0
     */
    public final Color getTextColor() {
        return textColor;
    }

    /**
     * Returns the text size.
     *
     * @return the text size
     *
     * @since 2.0
     */
    public final float getTextSize() {
        return textSize;
    }

    /**
     * Returns the typeface of this widget.
     *
     * @return the typeface of this widget
     *
     * @since 2.0
     */
    public final Typeface getTypeface() {
        return typeface;
    }

    @Override
    public final float getWidth() {
        return width;
    }

    /**
     * Sets a new size for this widget.
     *
     * @param width the new width of this widget
     * @param height the new height of this widget
     *
     * @since 2.0
     */
    public final void resize(final double width, final double height) {
        this.width = (float) width;
        this.height = (float) height;
    }

    /**
     * Sets the height of this widget.
     *
     * @param height the height of this widget
     *
     * @since 2.0
     */
    public final void setHeight(final double height) {
        this.height = (float) height;
    }

    /**
     * Sets the text color for this widget.
     *
     * @param textColor the text color for this widget
     *
     * @see #getTextColor()
     * @since 2.0
     */
    public final void setTextColor(final Color textColor) {
        this.textColor = textColor;
    }

    /**
     * Sets the text size for this widget.
     *
     * @param textSize the text size for this widget
     *
     * @see #getTextSize()
     * @since 2.0
     */
    public final void setTextSize(final double textSize) {
        this.textSize = (float) textSize;
    }

    /**
     * Sets the typeface for this widget.
     *
     * @param typeface the typeface for this widget
     *
     * @since 2.0
     */
    public final void setTypeface(final Typeface typeface) {
        this.typeface = typeface;
    }

    /**
     * Sets the width of this widget.
     *
     * @param width the width of this widget
     *
     * @since 2.0
     */
    public final void setWidth(final double width) {
        this.width = (float) width;
    }

    @Override
    protected void applyStyle(final Canvas canvas) {
        super.applyStyle(canvas);
        canvas.setTextSize(textSize);
        canvas.setTypeface(typeface);
    }

    @Override
    protected final boolean containsLocal(final float x, final float y) {
        return Math.abs(getCenterX() - x) <= width / 2 && Math.abs(getCenterY() - y) <= height / 2;
    }
}
