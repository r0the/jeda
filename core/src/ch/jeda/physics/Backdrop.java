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
package ch.jeda.physics;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Element;
import ch.jeda.ui.Image;

/**
 * Represents a background image in a physics view.
 *
 * @since 2.1
 */
public final class Backdrop extends Element {

    private final float height;
    private final Image image;
    private int opacity;
    private final float width;
    private final float x;
    private final float y;

    /**
     * Constructs a new backdrop.
     *
     * @param x the horizontal coordinate of the backdrop's center
     * @param y the vertical coordinate of the backdrop's center
     * @param width the width of the backdrop in meters
     * @param height the height of the backdrop in meters
     * @param image the image
     *
     * @since 2.1
     */
    public Backdrop(final float x, final float y, final float width, final float height, final Image image) {
        this.height = height;
        this.image = image;
        this.width = width;
        this.x = x;
        this.y = y;
        opacity = 255;
        setDrawOrder(Integer.MIN_VALUE);
    }

    @Override
    public float getAngleRad() {
        return 0f;
    }

    /**
     * Returns the image of this backdrop.
     *
     * @return the image of this backdrop
     *
     * @see #setImage(ch.jeda.ui.Image)
     * @since 2.1
     */
    public final Image getImage() {
        return image;
    }

    /**
     * Returns the opacity of this backdrop. The opacity is a number between 0 and 255 where 0 means totally transparent
     * and 255 means totally opaque.
     *
     * @return the opacity of this backdrop
     *
     * @see #setOpacity(int)
     * @since 2.1
     */
    public final int getOpacity() {
        return opacity;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    /**
     * Sets the opacity of this backdrop. The opacity is a number between 0 and 255 where 0 means totally transparent
     * and 255 means totally opaque.
     *
     * @param opacity the opacity of this backdrop
     *
     * @see #getOpacity()
     * @since 2.1
     */
    public final void setOpacity(final int opacity) {
        this.opacity = Math.max(0, Math.min(opacity, 255));
    }

    @Override
    protected void draw(final Canvas canvas) {
        if (image != null) {
            canvas.setAlignment(Alignment.CENTER);
            canvas.setOpacity(opacity);
            canvas.drawImage(0f, 0f, width, height, image);
        }
    }
}
