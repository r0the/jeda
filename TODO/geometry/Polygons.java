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

import ch.jeda.ui.Canvas;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Polygons extends Shape implements Iterable<Polygon> {

    private static final GreinerHormann CLIP_ALGORITHM = new GreinerHormann();
    private final BoundingBox boundingBox;
    private List<Polygon> list;

    public Polygons() {
        this.boundingBox = new BoundingBox();
        this.list = new ArrayList<Polygon>();
    }

    public Polygons(final Polygons polygons) {
        this.boundingBox = new BoundingBox(polygons.boundingBox);
        this.list = new ArrayList<Polygon>(polygons.list);
    }

    public void add(final Polygon polygon) {
        this.list.add(polygon);
        this.boundingBox.include(polygon.getBoundingBox());
    }

    public void addAll(final Polygons polygons) {
        this.list.addAll(polygons.list);
        this.boundingBox.include(polygons.getBoundingBox());
    }

    public void clear() {
        this.list.clear();
    }

    public Polygon get(int index) {
        return this.list.get(index);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public void addUnion(final Polygon polygon) {
        List<Polygon> mine = this.list;
        this.list = new ArrayList<Polygon>();
        boolean added = false;
        for (int i = 0; i < mine.size(); ++i) {
            final Polygon mypoly = mine.get(i);
            if (mypoly.getBoundingBox().intersects(polygon.getBoundingBox())) {
                CLIP_ALGORITHM.execute(mypoly, polygon, this, GreinerHormann.Operation.UNION);
                added = true;
            }
        }

        if (!added) {
            this.add(polygon);
        }
    }

    @Override
    public Iterator<Polygon> iterator() {
        return this.list.iterator();
    }

    public int size() {
        return this.list.size();
    }

    @Override
    public void draw(Canvas canvas) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getCenterX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getCenterY() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void move(float dx, float dy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    boolean doesContain(float x, float y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
