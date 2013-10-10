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

import ch.jeda.Util;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import java.util.Arrays;

public class Polygon extends Shape {

    private static final GreinerHormann CLIP_ALGORITHM = new GreinerHormann();
    private final float[] axes;
    private float cx;
    private float cy;
    private final float[] points;
    private final BoundingBox boundingBox;

    public Polygon(final float... points) {
        this.boundingBox = new BoundingBox();
        // There must be an even number of coordinates
        if ((points.length % 1) == 1) {
            throw new IllegalArgumentException("points");
        }

        this.points = Arrays.copyOf(points, points.length);

        this.axes = new float[this.points.length];
        float A2 = 0f;
        for (int i = 0; i < this.points.length; i = i + 2) {
            final float xi = this.points[i];
            final float yi = this.points[i + 1];
            final float xip1 = this.points[(i + 2) % this.points.length];
            final float yip1 = this.points[(i + 3) % this.points.length];
            final float ax = yip1 - yi;
            final float ay = xi - xip1;
            final float length = Util.distance(ax, ay);
            this.axes[i] = ax / length;
            this.axes[i + 1] = ay / length;
            // Calculate centroid according to http://en.wikipedia.org/wiki/Centroid
            final float f = (xi * yip1 - xip1 * yi);
            this.cx += (xi + xip1) * f;
            this.cy += (yi + yip1) * f;
            A2 += f;
        }

        this.cx /= 3f * A2;
        this.cy /= 3f * A2;
        this.boundingBox.reset();
        this.boundingBox.include(this.points);
    }

    public float[] axes() {
        return this.axes;
    }

    @Override
    public void draw(final Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException("canvas");
        }

        final Color fillColor = this.getFillColor();
        if (fillColor != null) {
            canvas.setColor(fillColor);
            canvas.fillPolygon(this.points);
        }

        final Color outlineColor = this.getOutlineColor();
        if (outlineColor != null) {
            canvas.setColor(outlineColor);
            canvas.drawPolygon(this.points);
        }
    }

    @Override
    public final BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public Polygons intersectWith(final Polygon other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        final Polygons result = new Polygons();
        CLIP_ALGORITHM.execute(this, other, result, GreinerHormann.Operation.INTERSECTION);
        return result;
    }

    public Polygons unionWith(final Polygon other) {
        if (other == null) {
            throw new NullPointerException("other");
        }

        final Polygons result = new Polygons();
        CLIP_ALGORITHM.execute(this, other, result, GreinerHormann.Operation.UNION);
        return result;
    }

    /**
     * Returns the x coordinate of the polygon's centroid.
     *
     * @return x coordinate of the polygon's centroid
     */
    @Override
    public float getCenterX() {
        return this.cx;
    }

    /**
     * Returns the y coordinate of the polygon's centroid.
     *
     * @return y coordinate of the polygon's centroid
     */
    @Override
    public float getCenterY() {
        return this.cy;
    }

    public float getX(final int index) {
        return this.points[index << 1];
    }

    public float getY(final int index) {
        return this.points[(index << 1) + 1];
    }

    @Override
    public void move(final float dx, final float dy) {
        for (int i = 0; i < this.points.length; i = i + 2) {
            this.points[i] += dx;
            this.points[i + 1] += dy;
        }

        this.cx += dx;
        this.cy += dy;
        this.boundingBox.reset();
        this.boundingBox.include(this.points);
    }

    public int size() {
        return this.points.length >> 1;
    }

    Collision collideWithPolygon(final Polygon other) {
        return collide(this, other);
    }

    @Override
    boolean doesContain(final float x, final float y) {
        // Ray casting algorithm
        // Sources: http://stackoverflow.com/a/218081 (General idea, but intersection test much too complicated)
        //          http://geomalgorithms.com/a03-_inclusion.html (C++ code)
        //          http://web.archive.org/web/20120322002749/http://local.wasp.uwa.edu.au/~pbourke/geometry/
        int intersections = 0;
        for (int i = 0; i < this.points.length; i = i + 2) {
            final float v1y = this.points[i + 1];
            final float v2y = this.points[(i + 3) % this.points.length];
            // Check if polygon edge crosses the line of ray upwards or downwards
            if ((v1y <= y && v2y > y) || (v1y >= y && v2y < y)) {
                // compute x coordinate of intersection
                final float vt = (y - v1y) / (v2y - v1y);
                final float v1x = this.points[i];
                final float v2x = this.points[(i + 2) % this.points.length];
                final float ix = v1x + vt * (v2x - v1x);
                // x coordinate must be on ray and on edge
                if (x < ix) {
                    ++intersections;
                }
            }
        }

        return intersections % 2 == 1;
    }

    // Polygon collision according to separation of axis theorem.
    // Requirement: Both polygons are convex
    private static Collision collide(final Polygon p, final Polygon q) {
        float penetration = Float.MAX_VALUE;
        float nx = 0f;
        float ny = 0f;
        float pmin;
        float pmax;
        float qmin;
        float qmax;
        for (int i = 0; i < p.axes.length; i = i + 2) {
            // axis i of p
            final float ax = p.axes[i];
            final float ay = p.axes[i + 1];

            pmin = Float.MAX_VALUE;
            pmax = -Float.MAX_VALUE;
            qmin = Float.MAX_VALUE;
            qmax = -Float.MAX_VALUE;
            // project all points of p on axis of p
            for (int j = 0; j < p.points.length; j = j + 2) {
                final float d = ax * p.points[j] + ay * p.points[j + 1];
                pmin = Math.min(pmin, d);
                pmax = Math.max(pmax, d);
            }

            // project all points of q on axis of p
            for (int j = 0; j < q.points.length; j = j + 2) {
                final float d = ax * q.points[j] + ay * q.points[j + 1];
                qmin = Math.min(qmin, d);
                qmax = Math.max(qmax, d);
            }

            // Check if there is no overlap
            if (pmax < qmin || qmax < pmin) {
                return null;
            }
            else {
                final float overlap = Math.min(pmax, qmax) - Math.max(pmin, qmin);
                if (overlap < penetration) {
                    penetration = overlap;
                    nx = ax;
                    ny = ay;
                    if (pmax < qmax) {
                        nx = -nx;
                        ny = -ny;
                    }
                }
            }
        }

        for (int i = 0; i < q.axes.length; i = i + 2) {
            // axis i of q
            final float ax = q.axes[i];
            final float ay = q.axes[i + 1];

            pmin = Float.MAX_VALUE;
            pmax = -Float.MAX_VALUE;
            qmin = Float.MAX_VALUE;
            qmax = -Float.MAX_VALUE;
            // project all points of p on axis of p
            for (int j = 0; j < p.points.length; j = j + 2) {
                final float d = ax * p.points[j] + ay * p.points[j + 1];
                pmin = Math.min(pmin, d);
                pmax = Math.max(pmax, d);
            }

            // project all points of q on axis of p
            for (int j = 0; j < q.points.length; j = j + 2) {
                final float d = ax * q.points[j] + ay * q.points[j + 1];
                qmin = Math.min(qmin, d);
                qmax = Math.max(qmax, d);
            }

            // Check if there is no overlap
            if (pmax < qmin || qmax < pmin) {
                return null;
            }
            else {
                final float overlap = Math.min(pmax, qmax) - Math.max(pmin, qmin);
                if (overlap < penetration) {
                    penetration = overlap;
                    nx = ax;
                    ny = ay;
                    if (pmax < qmax) {
                        nx = -nx;
                        ny = -ny;
                    }
                }
            }
        }

        return new Collision(p, q, nx, ny, penetration);
    }
}
