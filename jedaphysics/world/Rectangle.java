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
package ch.jeda.world;

import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;

public class Rectangle extends Shape {

    private final float width;
    private final float height;
    private float x;
    private float y;

    public Rectangle(final float width, final float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public boolean contains(float x, float y) {
        return this.x <= x && x <= this.x + this.width && this.y <= y && y <= this.y + this.height;
    }

    @Override
    public void draw(Canvas canvas) {
        final Color fillColor = this.getFillColor();
        if (fillColor != null) {
            canvas.setColor(fillColor);
            canvas.fillRectangle(this.x, this.y, this.width, this.height);

        }

        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawRectangle(this.x, this.y, this.width, this.height);
        }
    }

    public final float getHeight() {
        return this.height;
    }

    public final float getWidth() {
        return this.width;
    }
}
