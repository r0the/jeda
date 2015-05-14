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
package ch.jeda.tmx;

import ch.jeda.physics.PhysicsView;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Image;

/**
 * Represents a TMX image layer.
 *
 * @since 2.0
 */
public final class TmxImageLayer extends TmxLayer {

    private final Image image;
    private final int x;
    private final int y;

    TmxImageLayer(final TmxMap map, final Element element, final XmlReader reader) {
        super(map, element);
        image = reader.loadImageChild(element);
        x = element.getIntAttribute(Const.X);
        y = element.getIntAttribute(Const.Y);
    }

    @Override
    public void addTo(final PhysicsView view) {
        // TODO
    }

    @Override
    public void draw(final Canvas canvas, final double offsetX, final double offsetY) {
        if (isVisible()) {
            canvas.drawImage(x + offsetX, y + offsetY, image, (int) (getOpacity() * 255));
        }
    }

    /**
     * Returns the image of this layer.
     *
     * @return the image of this layer
     *
     * @since 2.0
     */
    public Image getImage() {
        return image;
    }

    @Override
    public final TmxLayerType getType() {
        return TmxLayerType.IMAGE;
    }

    /**
     * Returns the horizontal offset of the image layer.
     *
     * @return the horizontal offset of the image layer
     *
     * @see #getY()
     * @since 2.0
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the vertical offset of the image layer.
     *
     * @return the vertical offset of the image layer
     *
     * @see #getX()
     * @since 2.0
     */
    public int getY() {
        return y;
    }
}
