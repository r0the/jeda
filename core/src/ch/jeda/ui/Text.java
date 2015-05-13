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
        this.height = -1;
        this.text = text;
        this.textSize = DEFAULT_TEXT_SIZE;
        this.typeface = DEFAULT_TYPEFACE;
        this.width = -1;
    }

    @Override
    public boolean contains(final int x, final int y) {
        return false;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public String getText() {
        return this.text;
    }

    public Typeface getTypeface() {
        return this.typeface;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setTextSize(final int textSize) {
        if (this.textSize != textSize && textSize > 0) {
            this.textSize = textSize;
            this.invalidateSize();
        }
    }

    public void setText(final String text) {
        this.text = text;
        this.invalidateSize();
    }

    public void setTypeface(final Typeface typeface) {
        this.typeface = typeface;
        this.invalidateSize();
    }

    @Override
    protected void draw(final Canvas canvas) {
        canvas.setTypeface(this.typeface);
        canvas.setTextSize(this.textSize);
        if (this.width < 0 || this.height < 0) {
            this.width = canvas.textWidth(text);
            this.height = canvas.textHeight(text);
        }

        canvas.drawText(this.getX(), this.getY(), this.text, this.getAlignment());
    }

    private void invalidateSize() {
        this.height = -1;
        this.width = -1;
    }
}
