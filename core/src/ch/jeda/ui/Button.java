/*
 * Copyright (C) 2013 by Stefan Rothe
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

public class Button extends AbstractButton {

    private static Color BG_NORMAL_COLOR = new Color(230, 230, 230);
    private static Color BG_PRESSED_COLOR = new Color(130, 130, 130);
    private static Color BORDER_COLOR = new Color(90, 90, 90);
    private static Color TEXT_COLOR = new Color(0, 0, 0);
    private float height;
    private String text;
    private float width;

    public Button(final Window window, final String action) {
        super(window, action);
        window.add(this);
        this.width = 100;
        this.height = 50;
        this.setDrawOrder(Integer.MAX_VALUE);
    }

    public Button(final Window window, final float x, final float y, final String text) {
        this(window, x, y, text, Alignment.TOP_LEFT);
    }

    public Button(final Window window, final float x, final float y, final String text, final Alignment alignment) {
        this(window, text);
        window.add(this);
        this.setPosition(alignment.alignX(x, this.width), alignment.alignY(y, this.height));
        this.text = text;
    }

    @Override
    protected boolean contains(final float x, final float y) {
        return this.getX() <= x && x < this.getX() + this.width &&
               this.getY() <= y && y < this.getY() + this.height;

    }

    public String getText() {
        return this.text;
    }

    @Override
    public void draw(final Canvas canvas) {
        if (this.text == null || this.text.isEmpty()) {
            return;
        }

        if (this.isPressed()) {
            canvas.setColor(BG_PRESSED_COLOR);
        }
        else {
            canvas.setColor(BG_NORMAL_COLOR);
        }

        canvas.fillRectangle(0f, 0f, this.width, this.height);
        canvas.setColor(BORDER_COLOR);
        canvas.drawRectangle(0f, 0f, this.width, this.height);
        canvas.setColor(TEXT_COLOR);
        canvas.drawText(this.width / 2f, this.height / 2f, text, Alignment.CENTER);
    }

    public void setText(final String text) {
        this.text = text;
    }
}
