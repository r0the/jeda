/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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
 * @since 1.0
 */
public class Button extends AbstractButton {

    private static Color BG_NORMAL_COLOR = new Color(230, 230, 230);
    private static Color BG_PRESSED_COLOR = new Color(130, 130, 130);
    private static Color BORDER_COLOR = new Color(90, 90, 90);
    private static Color TEXT_COLOR = new Color(0, 0, 0);
    private int height;
    private String text;
    private int width;
    private int x;
    private int y;

    /**
     * @since 1.0
     */
    public Button(final Window window, final String action) {
        super(window, action);
        window.add(this);
        this.width = 100;
        this.height = 50;
        this.setDrawOrder(Integer.MAX_VALUE);
    }

    /**
     * @since 1.0
     */
    public Button(final Window window, final int x, final int y, final String text) {
        this(window, x, y, text, Alignment.TOP_LEFT);
    }

    /**
     * @since 1.0
     */
    public Button(final Window window, final int x, final int y, final String text, final Alignment alignment) {
        this(window, text);
        window.add(this);
        this.x = alignment.alignX(x, this.width);
        this.y = alignment.alignY(y, this.height);
        this.text = text;
    }

    @Override
    protected boolean contains(final int x, final int y) {
        return this.x <= x && x < this.x + this.width &&
               this.y <= y && y < this.y + this.height;

    }

    /**
     * @since 1.0
     */
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

        canvas.fillRectangle(this.x, this.y, this.width, this.height);
        canvas.setColor(BORDER_COLOR);
        canvas.drawRectangle(this.x, this.y, this.width, this.height);
        canvas.setColor(TEXT_COLOR);
        canvas.drawText(this.x + this.width / 2, this.y + this.height / 2, text, Alignment.CENTER);
    }

    /**
     * @since 1.0
     */
    public void setText(final String text) {
        this.text = text;
    }
}
