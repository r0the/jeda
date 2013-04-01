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
import java.util.List;

public abstract class AbstractPolygon extends Shape {

    protected abstract List<Vector> axes();

    protected abstract List<Vector> vertices();

    protected Collision doIntersectWithPolygon(AbstractPolygon other) {
        Vector minAxis = null;
        double minOverlap = Double.MAX_VALUE;

        for (Vector axis : this.axes()) {
            final Interval p1 = this.project(axis);
            final Interval p2 = other.project(axis);
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
            axis = this.toLocal(axis).normalized();
            final Interval p1 = this.project(axis);
            final Interval p2 = other.project(axis);
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

        return this.createCollision(new Vector(), minAxis.withLength(minOverlap));
    }

    private Interval project(Vector axis) {
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (Vector vertex : this.vertices()) {
            final double p = axis.dot(vertex);
            min = Math.min(min, p);
            max = Math.max(max, p);
        }

        return new Interval(min, max);
    }
}
