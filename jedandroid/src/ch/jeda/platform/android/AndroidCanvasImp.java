/*
 * Copyright (C) 2012 by Stefan Rothe
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
package ch.jeda.platform.android;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.Transformation;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.ui.Color;

class AndroidCanvasImp implements CanvasImp {

    private final Paint fill;
    private final Paint stroke;
    private Bitmap bitmap;
    private Canvas canvas;
    private Size size;

    AndroidCanvasImp() {
        this.fill = new Paint();
        this.fill.setStyle(Paint.Style.FILL);
        this.fill.setAntiAlias(true);
        this.stroke = new Paint();
        this.stroke.setStyle(Paint.Style.STROKE);
        this.stroke.setAntiAlias(true);
    }

    AndroidCanvasImp(Size size) {
        this();
        this.bitmap = Bitmap.createBitmap(size.width, size.height, Config.ARGB_8888);
        this.setCanvas(new Canvas(bitmap));
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void copyFrom(Location location, CanvasImp source) {
        assert location != null;
        assert source != null;

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawCircle(Location center, int radius) {
        assert center != null;
        assert radius > 0;

        this.canvas.drawCircle(center.x, center.y, radius, this.stroke);
        this.modified();
    }

    public void drawImage(Location topLeft, ImageImp image) {
        assert topLeft != null;
        assert image != null;

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawLine(Location start, Location end) {
        assert start != null;
        assert end != null;

        this.canvas.drawLine(start.x, start.y, end.x, end.y, this.stroke);
        this.modified();
    }

    public void drawPolygon(Iterable<Location> edges) {
        assert edges != null;

        this.canvas.drawPath(createPath(edges), this.stroke);
        this.modified();
    }

    public void drawRectangle(Location topLeft, Size size) {
        assert topLeft != null;
        assert size != null;

        int right = topLeft.x + size.width;
        int bottom = topLeft.y + size.height;
        this.canvas.drawRect(topLeft.x, topLeft.y, right, bottom, this.stroke);
        this.modified();
    }

    public void drawString(Location topLeft, String text) {
        assert topLeft != null;
        assert text != null;

        this.canvas.drawText(text, topLeft.x, topLeft.y, this.stroke);
        this.modified();
    }

    public void fill() {
        this.canvas.drawColor(this.fill.getColor());
        this.modified();
    }

    public void fillCircle(Location center, int radius) {
        assert center != null;
        assert radius > 0;

        this.canvas.drawCircle(center.x, center.y, radius, this.fill);
        this.modified();
    }

    public void fillPolygon(Iterable<Location> edges) {
        assert edges != null;

        this.canvas.drawPath(createPath(edges), this.fill);
        this.modified();
    }

    public void fillRectangle(Location topLeft, Size size) {
        assert topLeft != null;
        assert size != null;

        int right = topLeft.x + size.width;
        int bottom = topLeft.y + size.height;
        this.canvas.drawRect(topLeft.x, topLeft.y, right, bottom, this.fill);
        this.modified();
    }

    public void floodFill(Location pos, Color oldColor, Color newColor) {
        assert pos != null;
        assert oldColor != null;
        assert newColor != null;

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getLineWidth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Color getPixelAt(Location location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Size getSize() {
        return this.size;
    }

    public Transformation getTransformation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAlpha(int alpha) {
        assert 0 <= alpha && alpha <= 255;

        this.fill.setAlpha(alpha);
    }

    public void setColor(Color color) {
        this.fill.setColor(color.getValue());
        this.stroke.setColor(color.getValue());
    }

    public void setFontSize(int size) {
        this.stroke.setTextSize(size);
    }

    public void setLineWidth(double width) {
        this.stroke.setStrokeWidth((float) width);
    }

    public void setPixelAt(Location location, Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTransformation(Transformation transformation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ImageImp takeSnapshot() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Size textSize(String text) {
        Rect bounds = new Rect();
        this.stroke.getTextBounds(text, 0, text.length() - 1, bounds);
        return new Size(bounds.width(), bounds.height());
    }

    Canvas getCanvas() {
        return this.canvas;
    }

    void modified() {
    }

    Bitmap getBitmap() {
        return this.bitmap;
    }

    final void setSize(int width, int height) {
        this.size = new Size(width, height);
        this.bitmap = Bitmap.createBitmap(size.width, size.height, Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
    }

    final void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.size = new Size(this.canvas.getWidth(), this.canvas.getHeight());
    }

    private static Path createPath(Iterable<Location> edges) {
        Path result = new Path();
        for (Location edge : edges) {
            if (result.isEmpty()) {
                result.moveTo(edge.x, edge.y);
            }
            else {
                result.lineTo(edge.x, edge.y);
            }
        }

        result.close();
        return result;
    }
}
