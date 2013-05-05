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
package ch.jeda.geometry;

import ch.jeda.Vector;

/**
 * <b>Experimental</b>
 */
public abstract class AbstractPolygon extends Shape {

    /**
     * Returns the axes of the polygon. The axes are represented as vectors in the global coordinate system.
     *
     * @return axes of the polygon
     */
    protected abstract Vector[] axes();

    /**
     * Returns the vertices of the polygon. The vertices are represented as vectors in the global coordinate system.
     *
     * @return vertices of the polygon
     */
    protected abstract Vector[] vertices();

    /**
     * Intersects two polygons. The polygons are intersected by applying the Separation of Axes theorem.
     *
     * @param other the other polygon
     * @return collision object representing the intersection
     */
    protected Collision doIntersectWithPolygon(final AbstractPolygon other) {
        Vector minAxis = new Vector();
        final Vector[] theseAxes = this.axes();
        final Vector[] theseVertices = this.vertices();
        final Vector[] otherAxes = other.axes();
        final Vector[] otherVertices = other.vertices();
        float minOverlap = Float.MAX_VALUE;

        for (int i = 0; i < theseAxes.length; ++i) {
            final Interval p1 = project(theseAxes[i], theseVertices);
            final Interval p2 = project(theseAxes[i], otherVertices);
            if (!p1.overlapsWith(p2)) {
                return null;
            }
            else {
                final float o = p1.overlap(p2);
                if (o < minOverlap) {
                    minOverlap = o;
                    minAxis = theseAxes[i];
                }
            }
        }

        for (int i = 0; i < otherAxes.length; ++i) {
            final Interval p1 = project(otherAxes[i], theseVertices);
            final Interval p2 = project(otherAxes[i], otherVertices);
            if (!p1.overlapsWith(p2)) {
                return null;
            }
            else {
                final float o = p1.overlap(p2);
                if (o < minOverlap) {
                    minOverlap = o;
                    minAxis = otherAxes[i];
                }
            }
        }

        minAxis.setLength(minOverlap);
        return this.createCollision(new Vector(), minAxis);
    }

    private static Interval project(final Vector axis, final Vector[] vertices) {
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        for (Vector vertex : vertices) {
            final float p = axis.dot(vertex);
            min = Math.min(min, p);
            max = Math.max(max, p);
        }

        return new Interval(min, max);
    }
}
