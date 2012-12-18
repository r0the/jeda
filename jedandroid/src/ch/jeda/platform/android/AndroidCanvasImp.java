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
import android.graphics.Matrix;
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

    private final Paint fillPaint;
    private final Paint pixelPaint;
    private final Paint strokePaint;
    private final Paint textPaint;
    private Bitmap bitmap;
    private Canvas canvas;
    private Size size;

    @Override
    public void clear() {
        this.canvas.drawColor(0);
    }

    @Override
    public void copyFrom(Location topLeft, CanvasImp source) {
        assert topLeft != null;
        assert source != null;
        assert source instanceof AndroidCanvasImp;

        this.canvas.drawBitmap(((AndroidCanvasImp) source).bitmap,
                               topLeft.x, topLeft.y, this.fillPaint);
    }

    @Override
    public void drawCircle(Location center, int radius) {
        assert center != null;
        assert radius > 0;

        this.canvas.drawCircle(center.x, center.y, radius, this.strokePaint);
        this.modified();
    }

    @Override
    public void drawImage(Location topLeft, ImageImp image) {
        assert topLeft != null;
        assert image != null;
        assert image instanceof AndroidImageImp;

        this.canvas.drawBitmap(((AndroidImageImp) image).getBitmap(),
                               topLeft.x, topLeft.y, this.fillPaint);
        this.modified();
    }

    @Override
    public void drawLine(Location start, Location end) {
        assert start != null;
        assert end != null;

        this.canvas.drawLine(start.x, start.y, end.x, end.y, this.strokePaint);
        this.modified();
    }

    @Override
    public void drawPolygon(Iterable<Location> edges) {
        assert edges != null;

        this.canvas.drawPath(createPath(edges), this.strokePaint);
        this.modified();
    }

    @Override
    public void drawRectangle(Location topLeft, Size size) {
        assert topLeft != null;
        assert size != null;

        int right = topLeft.x + size.width;
        int bottom = topLeft.y + size.height;
        this.canvas.drawRect(topLeft.x, topLeft.y, right, bottom, this.strokePaint);
        this.modified();
    }

    @Override
    public void drawText(Location topLeft, String text) {
        assert topLeft != null;
        assert text != null;

        this.canvas.drawText(text, topLeft.x,
                             topLeft.y - (int) this.textPaint.ascent(),
                             this.textPaint);
        this.modified();
    }

    @Override
    public void fill() {
        this.canvas.drawColor(this.fillPaint.getColor());
        this.modified();
    }

    @Override
    public void fillCircle(Location center, int radius) {
        assert center != null;
        assert radius > 0;

        this.canvas.drawCircle(center.x, center.y, radius, this.fillPaint);
        this.modified();
    }

    @Override
    public void fillPolygon(Iterable<Location> edges) {
        assert edges != null;

        this.canvas.drawPath(createPath(edges), this.fillPaint);
        this.modified();
    }

    @Override
    public void fillRectangle(Location topLeft, Size size) {
        assert topLeft != null;
        assert size != null;

        int right = topLeft.x + size.width;
        int bottom = topLeft.y + size.height;
        this.canvas.drawRect(topLeft.x, topLeft.y, right, bottom, this.fillPaint);
        this.modified();
    }

    @Override
    public double getLineWidth() {
        return this.strokePaint.getStrokeWidth();
    }

    @Override
    public Color getPixelAt(Location location) {
        assert location != null;
        assert this.size.contains(location);

        return Color.from(this.bitmap.getPixel(location.x, location.y));
    }

    @Override
    public Size getSize() {
        return this.size;
    }

    @Override
    public void setAlpha(int alpha) {
        assert 0 <= alpha && alpha <= 255;

        this.fillPaint.setAlpha(alpha);
    }

    public void setColor(Color color) {
        this.fillPaint.setColor(color.value);
        this.strokePaint.setColor(color.value);
        this.textPaint.setColor(color.value);
    }

    @Override
    public void setFontSize(int size) {
        this.strokePaint.setTextSize(size);
    }

    @Override
    public void setLineWidth(double width) {
        this.strokePaint.setStrokeWidth((float) width);
    }

    @Override
    public void setPixelAt(Location location, Color color) {
        assert location != null;
        assert this.size.contains(location);
        assert color != null;

        this.pixelPaint.setColor(color.value);
        this.canvas.drawPoint(location.x, location.y, this.pixelPaint);
    }

    @Override
    public void setTransformation(Transformation transformation) {
        float[] array = new float[9];
        array[Matrix.MSCALE_X] = (float) transformation.scaleX;
        array[Matrix.MSCALE_Y] = (float) transformation.scaleY;
        array[Matrix.MSKEW_X] = (float) transformation.skewX;
        array[Matrix.MSKEW_Y] = (float) transformation.skewY;
        array[Matrix.MTRANS_X] = (float) transformation.translateX;
        array[Matrix.MTRANS_Y] = (float) transformation.translateY;
        array[Matrix.MPERSP_0] = 0f;
        array[Matrix.MPERSP_1] = 0f;
        array[Matrix.MPERSP_2] = 1f;
        Matrix matrix = new Matrix();
        matrix.setValues(array);
        this.canvas.setMatrix(matrix);
    }

    @Override
    public ImageImp takeSnapshot() {
        return new AndroidImageImp(Bitmap.createBitmap(this.bitmap));
    }

    @Override
    public Size textSize(String text) {
        Rect bounds = new Rect();
        this.strokePaint.getTextBounds(text, 0, text.length(), bounds);
        return Size.from(bounds.width(), bounds.height());
    }

    AndroidCanvasImp() {
        this.fillPaint = new Paint();
        this.fillPaint.setStyle(Paint.Style.FILL);
        this.fillPaint.setAntiAlias(true);
        this.pixelPaint = new Paint();
        this.strokePaint = new Paint();
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setAntiAlias(true);
        this.textPaint = new Paint();
    }

    Canvas getCanvas() {
        return this.canvas;
    }

    void modified() {
    }

    Bitmap getBitmap() {
        return this.bitmap;
    }

    final void setSize(Size size) {
        this.size = size;
        this.bitmap = Bitmap.createBitmap(size.width, size.height, Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
    }

    private static Path createPath(Iterable<Location> edges) {
        Path result = new Path();
        boolean first = true;
        for (Location edge : edges) {
            if (first) {
                result.moveTo(edge.x, edge.y);
                first = false;
            }
            else {
                result.lineTo(edge.x, edge.y);
            }
        }

        result.close();
        return result;
    }
}
