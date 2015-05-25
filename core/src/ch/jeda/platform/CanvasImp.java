/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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

import ch.jeda.ui.Color;

/**
 * <b>Internal</b>. Do not use this interface.
 */
public interface CanvasImp {

    void drawCanvas(int x, int y, CanvasImp source);

    void drawEllipse(float centerX, float centerY, float radiusX, float radiusY);

    void drawImage(float x, float y, float width, float height, ImageImp image, int alpha);

    void drawPolygon(float[] points);

    void drawPolyline(float[] points);

    void drawRectangle(float x, float y, float width, float height);

    void drawText(float x, float y, String text);

    void fill();

    void fillEllipse(float centerX, float centerY, float radiusX, float radiusY);

    void fillPolygon(float[] points);

    void fillRectangle(float x, float y, float width, float height);

    int getHeight();

    int getTextHeight();

    Color getPixel(int x, int y);

    int getWidth();

    int measureLength(String text, TypefaceImp typeface, float textSize);

    void resetTransformation();

    void rotateRad(float angle, float centerX, float centerY);

    void setAntiAliasing(boolean antiAliasing);

    void setColor(Color color);

    void setLineWidth(float lineWidth);

    void setPixel(int x, int y, Color color);

    void setTextSize(float textSize);

    void setTypeface(TypefaceImp typeface);

    ImageImp takeSnapshot();

    void translate(float tx, float ty);
}
