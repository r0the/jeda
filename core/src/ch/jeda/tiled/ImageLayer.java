/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
package ch.jeda.tiled;

import ch.jeda.physics.PhysicsView;
import ch.jeda.ui.Image;

/**
 * Represents a Tiled image layer.
 *
 * @since 2.0
 */
public final class ImageLayer extends Layer {

    private final Image image;
    private final int x;
    private final int y;

    ImageLayer(final TiledMap map, final ElementWrapper element, final XmlReader reader) {
        super(map, element);
        image = reader.loadImageChild(element);
        x = element.getIntAttribute(Const.X);
        y = element.getIntAttribute(Const.Y);
    }

    /**
     * Adds this image layer to a physics view. The image is added by drawing it on the background of the view. This
     * method has no effect if the layer is not visible.
     *
     * @param view the view to add this layer to
     *
     * @since 2.0
     */
    @Override
    public void addTo(final PhysicsView view) {
        if (isVisible()) {
            view.getBackground().drawImage(x, y, image, getOpacity());
        }
    }

    /**
     * Returns the image represented by this layer.
     *
     * @return the image represented by this layer
     *
     * @since 2.0
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns the horizontal coordinate of the top left corner of the image layer.
     *
     * @return the horizontal coordinate of the top left corner of the image layer
     *
     * @see #getY()
     * @since 2.0
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the vertical coordinate of the top left corner of the image.
     *
     * @return the vertical coordinate of the top left corner of the image
     *
     * @see #getX()
     * @since 2.0
     */
    public int getY() {
        return y;
    }
}
