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

public class Text extends Widget {

    private static final int DEFAULT_TEXT_SIZE = 18;
    private static final Typeface DEFAULT_TYPEFACE = Typeface.SANS_SERIF;
    private int height;
    private String text;
    private int textSize;
    private Typeface typeface;
    private int width;

    public Text(final int x, final int y) {
        this(x, y, null, Alignment.TOP_LEFT);
    }

    public Text(final int x, final int y, final String text) {
        this(x, y, text, Alignment.TOP_LEFT);
    }

    public Text(final int x, final int y, final String text, final Alignment alignment) {
        super(x, y, alignment);
        height = -1;
        this.text = text;
        textSize = DEFAULT_TEXT_SIZE;
        typeface = DEFAULT_TYPEFACE;
        width = -1;
    }

    @Override
    public boolean contains(final float x, final float y) {
        return false;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public int getTextSize() {
        return textSize;
    }

    public String getText() {
        return text;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setTextSize(final int textSize) {
        if (textSize != this.textSize && textSize > 0) {
            this.textSize = textSize;
            invalidateSize();
        }
    }

    public void setText(final String text) {
        this.text = text;
        invalidateSize();
    }

    public void setTypeface(final Typeface typeface) {
        this.typeface = typeface;
        invalidateSize();
    }

    @Override
    protected void draw(final Canvas canvas) {
        canvas.setTypeface(typeface);
        canvas.setTextSize(textSize);
        if (width < 0 || height < 0) {
            width = canvas.textWidth(text);
            height = canvas.textHeight(text);
        }

        canvas.drawText((int) getX(), (int) getY(), text, getAlignment());
    }

    private void invalidateSize() {
        height = -1;
        width = -1;
    }
}
