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

import ch.jeda.Data;
import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;

/**
 * Represents a TMX object.
 *
 * @since 2.0
 */
public final class TmxObject {

    private final boolean ellipse;
    private final double height;
    private final String name;
    private final Data properties;
    private final double rotation;
    private final TmxTile tile;
    private final String type;
    private final boolean visible;
    private final double width;
    private final double x;
    private final double y;

    TmxObject(final TmxMap map, final Element element) {
        this.ellipse = element.hasChild(Const.ELLIPSE);
        this.height = element.getDoubleAttribute(Const.HEIGHT, 0.0);
        this.name = element.getStringAttribute(Const.NAME);
        this.properties = element.parsePropertiesChild();
        this.rotation = element.getDoubleAttribute(Const.ROTATION, 0.0);
        this.type = element.getStringAttribute(Const.TYPE);
        this.visible = element.getBooleanAttribute(Const.VISIBLE, true);
        this.width = element.getDoubleAttribute(Const.WIDTH, 0.0);
        this.x = element.getDoubleAttribute(Const.X);
        this.y = element.getDoubleAttribute(Const.Y);
        if (element.hasAttribute(Const.GID)) {
            this.tile = map.lookupTile(element.getIntAttribute(Const.GID));
        }
        else {
            this.tile = null;
        }
    }

    /**
     * Draws this tile with the offset. Has no effect if the object is not visible.
     *
     * @param canvas the canvas to draw on
     * @param offsetX the horizontal offset
     * @param offsetY the vertical offset
     *
     * @since 2.0
     */
    public void draw(final Canvas canvas, final double offsetX, final double offsetY) {
        if (!this.visible) {
            return;
        }

        canvas.pushTransformations();
        canvas.resetTransformations();
        canvas.setTranslation(this.x + offsetX, this.y + offsetY);
        canvas.setRotationDeg(this.rotation);
        if (this.tile != null) {
            canvas.drawImage(0, 0, this.tile.getImage(), Alignment.BOTTOM_LEFT);
        }

        if (this.ellipse) {
            canvas.drawEllipe(this.width / 2.0, this.height / 2.0, this.width / 2.0, this.height / 2.0);
        }
        else {
            canvas.drawRectangle(0, 0, this.width, this.height);
        }

        canvas.popTransformations();
    }

    /**
     * Returns the height of this object.
     *
     * @return the height of this object
     *
     * @since 2.0
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Returns the name of this object.
     *
     * @return the name of this object
     *
     * @since 2.0
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the properties of this object.
     *
     * @return the properties of this object
     *
     * @since 2.0
     */
    public Data getProperties() {
        return this.properties;
    }

    /**
     * Returns the rotation of this object in degrees.
     *
     * @return the rotation of this object in degrees
     *
     * @since 2.0
     */
    public double getRotationDeg() {
        return this.rotation;
    }

    /**
     * Returns the rotation of this object in radians.
     *
     * @return the rotation of this object in radians
     *
     * @since 2.0
     */
    public double getRotationRad() {
        return Math.toRadians(this.rotation);
    }

    /**
     * Returns the tile representing this object.
     *
     * @return the tile representing this object
     *
     * @since 2.0
     */
    public TmxTile getTile() {
        return this.tile;
    }

    /**
     * Returns the type of this object.
     *
     * @return the type of this object
     *
     * @since 2.0
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the width of this object.
     *
     * @return the width of this object
     *
     * @since 2.0
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Returns the x coordinate of this object.
     *
     * @return the x coordinate of this object
     *
     * @since 2.0
     */
    public double getX() {
        return this.x;
    }

    /**
     * Returns the y coordinate of this object.
     *
     * @return the y coordinate of this object
     *
     * @since 2.0
     */
    public double getY() {
        return this.y;
    }

    public boolean isEllipse() {
        return this.ellipse;
    }
}
