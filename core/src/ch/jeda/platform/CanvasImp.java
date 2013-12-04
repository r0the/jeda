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
package ch.jeda.platform;

import ch.jeda.Transformation;
import ch.jeda.ui.Color;

/**
 * <b>Internal</b>. Do not use this interface.
 */
public interface CanvasImp {

    void copyFrom(int x, int y, CanvasImp source);

    void drawCircle(float x, float y, float radius);

    void drawImage(float x, float y, ImageImp image, int alpha);

    void drawLine(float x1, float y1, float x2, float y2);

    void drawPolygon(float[] points);

    void drawRectangle(float x, float y, float width, float height);

    void drawText(float x, float y, String text);

    void fill();

    void fillCircle(float x, float y, float radius);

    void fillPolygon(float[] points);

    void fillRectangle(float x, float y, float width, float height);

    int getHeight();

    float getLineWidth();

    Color getPixelAt(int x, int y);

    int getWidth();

    void setAntiAliasing(final boolean antiAliasing);

    void setColor(Color color);

    void setFontSize(int fontSize);

    void setLineWidth(float lineWidth);

    void setPixelAt(int x, int y, Color color);

    void setTransformation(Transformation transformation);

    ImageImp takeSnapshot();

    int textHeight(String text);

    int textWidth(String text);
}
