/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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

import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.Vector;
import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Image;

//public class Picture extends Figure {
//
//    private final Image image;
//
//    public Picture(Image image) {
//        if (image != null) {
//            throw new NullPointerException("image");
//        }
//
//        this.image = image;
//    }
//
//    public Picture(String filePath) {
//        this(new Image(filePath));
//    }
//
//    @Override
//    protected void doDraw(Canvas canvas) {
//        canvas.drawImage(Location.ORIGIN, this.image, Alignment.CENTER);
//    }
//
//    @Override
//    protected boolean doesContain(Vector point) {
//        final Size size = this.image.getSize();
//        return Math.abs(point.x) <= size.width / 2 &&
//               Math.abs(point.y) <= size.height / 2;
//    }
//
//    @Override
//    protected Collision doIntersectWith(Figure other) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    protected Collision doIntersectWithShape(Shape other) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
