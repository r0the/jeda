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

import ch.jeda.Vector;
import ch.jeda.ui.Canvas;
import java.util.ArrayList;
import java.util.List;

//public class CompositeFigure extends Figure {
//
//    private final List<Figure> subFigures;
//
//    public CompositeFigure() {
//        this.subFigures = new ArrayList();
//    }
//
//    public void add(Figure figure) {
//        this.subFigures.add(figure);
//    }
//
//    @Override
//    protected void doDraw(Canvas canvas) {
//        for (Figure figure : this.subFigures) {
//            figure.draw(canvas);
//        }
//    }
//
//    @Override
//    protected boolean doesContain(Vector point) {
//        for (Figure figure : this.subFigures) {
//            if (figure.contains(point)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//}
