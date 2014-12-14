/*
 * Copyright (C) 2014 by Stefan Rothe
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

import ch.jeda.Data;
import ch.jeda.ui.Canvas;

/**
 * Base class for TMX map layers.
 *
 * @since 1.6
 */
public abstract class TmxLayer {

    private final TmxMap map;
    private final String name;
    private double opacity;
    private final Data properties;
    private boolean visible;

    TmxLayer(final TmxMap map, final Element element) {
        this.map = map;
        this.name = element.getStringAttribute(Const.NAME);
        this.opacity = Math.max(0.0, Math.min(element.getDoubleAttribute(Const.OPACITY, 1.0), 1.0));
        this.properties = element.parsePropertiesChild();
        this.visible = element.getBooleanAttribute(Const.VISIBLE, true);
    }

    /**
     * Draws this layer at the specified coordinates. The current opacity and visibility of the layer are taken into
     * account when drawing the layer.
     *
     * @param canvas the canvas to draw this layer on
     * @param offsetX the horizontal offset for drawing the layer
     * @param offsetY the vertical offset for drawing the layer
     *
     * @since 1.6
     */
    public abstract void draw(final Canvas canvas, final double offsetX, final double offsetY);

    /**
     * Returns the map this layer belongs to.
     *
     * @return the map this layer belongs to
     *
     * @since 1.6
     */
    public final TmxMap getMap() {
        return this.map;
    }

    /**
     * Returns the name of this layer.
     *
     * @return the name of this layer
     *
     * @since 1.6
     */
    public final String getName() {
        return this.name;
    }

    public TmxObject[] getObjects() {
        return new TmxObject[0];
    }

    /**
     * Returns the opacity of this layer. The opacity is a number between 0 and 1 where 0 means totally transparent and
     * 1 means totally opaque.
     *
     * @return the opacity of this layer
     *
     * @see #setOpacity(double)
     * @since 1.6
     */
    public final double getOpacity() {
        return this.opacity;
    }

    /**
     * Returns the properties of this layer.
     *
     * @return the properties of this layer
     *
     * @since 1.6
     */
    public final Data getProperties() {
        return this.properties;
    }

    /**
     * Returns the tile at the specified tile coordinates. Returns <tt>null</tt> if there is no tile at the specified
     * coordinates. Always returns <tt>null</tt> if this layer is not of type {@link TmxLayerType#TILE}.
     *
     * @param x the horizontal tile coordinate
     * @param y the vertical tile coordinate
     * @return the tile at the specified tile coordinates or <tt>null</tt>
     *
     * @since 1.6
     */
    public TmxTile getTile(final int x, final int y) {
        return null;
    }

    /**
     * Returns the type of this layer.
     *
     * @return the type of this layer
     *
     * @since 1.6
     */
    public abstract TmxLayerType getType();

    /**
     * Checks if this layer is visible.
     *
     * @return <tt>true</tt> is this layer is visible, otherwise <tt>false</tt>
     *
     * @see #setVisible(boolean)
     * @since 1.6
     */
    public final boolean isVisible() {
        return this.visible;
    }

    /**
     * Sets the opacity of this layer. The opacity is a number between 0 and 1 where 0 means totally transparent and 1
     * means totally opaque.
     *
     * @param opacity the opacity of this layer
     *
     * @see #getOpacity()
     * @since 1.6
     */
    public void setOpacity(double opacity) {
        this.opacity = Math.max(0.0, Math.min(opacity, 1.0));
    }

    /**
     * Sets the visibility of this layer.
     *
     * @param visible the visibility of this layer
     *
     * @see #isVisible()
     * @since 1.6
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
