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
package ch.jeda.tiled;

import ch.jeda.geometry.Ellipse;
import ch.jeda.geometry.Polygon;
import ch.jeda.geometry.PolygonalChain;
import ch.jeda.geometry.Rectangle;
import ch.jeda.geometry.Shape;
import java.util.ArrayList;
import java.util.List;

class Parser {

    /**
     * Parses the collision shapes from a tile element.
     *
     * @param tileElement the tile element
     * @return the shapes
     */
    static Shape[] parseShapes(final ElementWrapper tileElement) {
        final List<Shape> shapeList = new ArrayList<Shape>();
        if (tileElement != null) {
            final ElementWrapper objectGroupElement = tileElement.getChild(Const.OBJECTGROUP);
            if (objectGroupElement != null) {
                for (final ElementWrapper objectElement : objectGroupElement.getChildren(Const.OBJECT)) {
                    shapeList.add(parseShape(objectElement));
                }
            }
        }

        return shapeList.toArray(new Shape[shapeList.size()]);
    }

    /**
     * Parses the shape from an object element.
     *
     * @param objectElement the object element
     * @return the shapes
     */
    static Shape parseShape(final ElementWrapper objectElement) {
        final double height = objectElement.getDoubleAttribute(Const.HEIGHT, 0.0);
        final double width = objectElement.getDoubleAttribute(Const.WIDTH, 0.0);
        if (objectElement.hasChild(Const.ELLIPSE)) {
            return new Ellipse(width / 2.0, height / 2.0);
        }
        else if (objectElement.hasChild(Const.POLYGON)) {
            final String points = objectElement.getChild(Const.POLYGON).getStringAttribute(Const.POINTS);
            return new Polygon(parsePoints(points));
        }
        else if (objectElement.hasChild(Const.POLYLINE)) {
            final String points = objectElement.getChild(Const.POLYLINE).getStringAttribute(Const.POINTS);
            return new PolygonalChain(parsePoints(points));
        }
        else if (width > 0 && height > 0) {
            return new Rectangle(width, height);
        }
        else {
            return null;
        }

    }

    /**
     * Parses the terrains from a tile element.
     *
     * @param tileSet the tile set
     * @param tileElement the tile element
     * @return the array of terrains
     */
    static Terrain[] parseTerrain(final TileSet tileSet, final ElementWrapper tileElement) {
        final Terrain[] result = new Terrain[4];
        if (tileElement != null) {
            final String terrainInfo = tileElement.getStringAttribute(Const.TERRAIN);
            if (terrainInfo != null) {
                final String[] parts = terrainInfo.split(",");
                int i = 0;
                while (i < 4 && i < parts.length) {
                    try {
                        result[i] = tileSet.lookupTerrain(Integer.parseInt(parts[i]));
                    }
                    catch (final NumberFormatException ex) {
                        // ignore
                    }

                    ++i;
                }
            }
        }

        return result;
    }

    /**
     * Parses a list of points from a {@link java.lang.String}.
     *
     * @param points the string
     * @return the list of points
     */
    private static double[] parsePoints(final String points) {
        String[] parts = points.split(" |,");
        final double[] result = new double[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            result[i] = Double.parseDouble(parts[i]);
        }

        return result;
    }
}
