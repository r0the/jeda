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

public abstract class AbstractPolygon extends Shape {

    protected abstract Vector[] axes();

    protected abstract Vector[] vertices();

    protected Collision doIntersectWithPolygon(AbstractPolygon other) {
        Vector minAxis = new Vector();
        final Vector[] theseVertices = this.vertices();
        final Vector[] otherVertices = other.vertices();
        double minOverlap = Double.MAX_VALUE;

        for (Vector axis : this.axes()) {
            final Interval p1 = project(axis, theseVertices);
            final Interval p2 = project(axis, otherVertices);
            if (!p1.overlapsWith(p2)) {
                return Collision.NULL;
            }
            else {
                final double o = p1.overlap(p2);
                if (o < minOverlap) {
                    minOverlap = o;
                    minAxis = axis;
                }
            }
        }

        for (Vector axis : other.axes()) {
            this.worldToLocal(axis);
            axis.normalize();
            final Interval p1 = project(axis, theseVertices);
            final Interval p2 = project(axis, otherVertices);
            if (!p1.overlapsWith(p2)) {
                return Collision.NULL;
            }
            else {
                final double o = p1.overlap(p2);
                if (o < minOverlap) {
                    minOverlap = o;
                    minAxis = axis;
                }
            }
        }

        minAxis.setLength(minOverlap);
        return this.createCollision(new Vector(), minAxis);
    }

    private static Interval project(Vector axis, Vector[] vertices) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (Vector vertex : vertices) {
            final double p = axis.dot(vertex);
            min = Math.min(min, p);
            max = Math.max(max, p);
        }

        return new Interval(min, max);
    }
}
