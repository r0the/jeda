/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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
package ch.jeda.platform;

import ch.jeda.Transformation;
import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.ui.Color;

public interface CanvasImp {

    void clear();

    void copyFrom(int x, int y, CanvasImp source);

    void drawCircle(int x, int y, int radius);

    void drawImage(int x, int y, ImageImp image);

    void drawLine(int x1, int y1, int x2, int y2);

    void drawPolygon(Iterable<Location> edges);

    void drawRectangle(int x, int y, int width, int height);

    void drawText(int x, int y, String text);

    void fill();

    void fillCircle(int x, int y, int radius);

    void fillPolygon(Iterable<Location> edges);

    void fillRectangle(int x, int y, int width, int height);

    public double getLineWidth();

    Color getPixelAt(int x, int y);

    Size getSize();

    void setAlpha(int alpha);

    void setColor(Color color);

    void setFontSize(int size);

    void setLineWidth(double width);

    void setPixelAt(int x, int y, Color color);

    void setTransformation(Transformation transformation);

    ImageImp takeSnapshot();

    Size textSize(String text);
}
