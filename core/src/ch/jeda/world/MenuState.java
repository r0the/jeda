/*
 * Copyright (C) 2010 - 2013 by Stefan Rothe
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
package ch.jeda.world;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

public class MenuState extends WorldState {

    private static final int BORDER = 10;
    private String message;
    private int fontSize;
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255, 100);
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.BLACK;

    public MenuState() {
        this.fontSize = 20;
        this.message = "";
    }

    @Override
    protected void drawOverlay(Canvas canvas) {
        String[] lines = this.message.split("\n");
        int width = 0;
        int x = canvas.getWidth() / 2;
        int y = canvas.getHeight() / 2;
        int lineHeight = 0;
        canvas.setFontSize(this.fontSize);
        for (String line : lines) {
            width = Math.max(width, canvas.textWidth(line));
            lineHeight = Math.max(lineHeight, canvas.textHeight(line));
        }

        int height = lineHeight * lines.length;
        canvas.setColor(BACKGROUND_COLOR);
        canvas.fillRectangle(x, y, width + BORDER, height + BORDER, Alignment.CENTER);
        canvas.setColor(BORDER_COLOR);
        canvas.drawRectangle(x, y, width + BORDER, height + BORDER, Alignment.CENTER);
        canvas.setColor(TEXT_COLOR);
        y = y - height / 2;
        for (String line : lines) {
            canvas.drawText(x, y, line, Alignment.TOP_CENTER);
            y = y + lineHeight;
        }
    }

    public final String getMessage() {
        return this.message;
    }

    public final void setMessage(String message) {
        if (message == null) {
            throw new NullPointerException("message");
        }

        this.message = message;
    }
}
