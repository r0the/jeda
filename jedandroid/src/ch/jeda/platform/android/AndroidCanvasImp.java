/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
    private final Matrix matrix;
    private final float[] matrixArray;
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
    public void copyFrom(int x, int y, CanvasImp source) {
        assert source != null;
        assert source instanceof AndroidCanvasImp;

        this.canvas.drawBitmap(((AndroidCanvasImp) source).bitmap,
                               x, y, this.fillPaint);
    }

    @Override
    public void drawCircle(int x, int y, int radius) {
        assert radius > 0;

        this.canvas.drawCircle(x, y, radius, this.strokePaint);
        this.modified();
    }

    @Override
    public void drawImage(int x, int y, ImageImp image) {
        assert image != null;
        assert image instanceof AndroidImageImp;

        this.canvas.drawBitmap(((AndroidImageImp) image).getBitmap(),
                               x, y, this.fillPaint);
        this.modified();
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        this.canvas.drawLine(x1, y1, x2, y2, this.strokePaint);
        this.modified();
    }

    @Override
    public void drawPolygon(Iterable<Location> edges) {
        assert edges != null;

        this.canvas.drawPath(createPath(edges), this.strokePaint);
        this.modified();
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height) {
        this.canvas.drawRect(x, y, x + width, y + height, this.strokePaint);
        this.modified();
    }

    @Override
    public void drawText(int x, int y, String text) {
        assert text != null;

        this.canvas.drawText(text, x, y - (int) this.textPaint.ascent(),
                             this.textPaint);
        this.modified();
    }

    @Override
    public void fill() {
        this.canvas.drawColor(this.fillPaint.getColor());
        this.modified();
    }

    @Override
    public void fillCircle(int x, int y, int radius) {
        assert radius > 0;

        this.canvas.drawCircle(x, y, radius, this.fillPaint);
        this.modified();
    }

    @Override
    public void fillPolygon(Iterable<Location> edges) {
        assert edges != null;

        this.canvas.drawPath(createPath(edges), this.fillPaint);
        this.modified();
    }

    @Override
    public void fillRectangle(int left, int top, int width, int height) {
        this.canvas.drawRect(left, top, left + width, top + height, this.fillPaint);
        this.modified();
    }

    @Override
    public double getLineWidth() {
        return this.strokePaint.getStrokeWidth();
    }

    @Override
    public Color getPixelAt(int x, int y) {
        assert this.size.contains(x, y);

        return new Color(this.bitmap.getPixel(x, y));
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
    public void setPixelAt(int x, int y, Color color) {
        assert this.size.contains(x, y);
        assert color != null;

        this.pixelPaint.setColor(color.value);
        this.canvas.drawPoint(x, y, this.pixelPaint);
    }

    @Override
    public void setTransformation(Transformation transformation) {
        this.matrixArray[Matrix.MSCALE_X] = transformation.scaleX;
        this.matrixArray[Matrix.MSCALE_Y] = transformation.scaleY;
        this.matrixArray[Matrix.MSKEW_X] = transformation.skewX;
        this.matrixArray[Matrix.MSKEW_Y] = transformation.skewY;
        this.matrixArray[Matrix.MTRANS_X] = transformation.translateX;
        this.matrixArray[Matrix.MTRANS_Y] = transformation.translateY;
        this.matrix.setValues(this.matrixArray);
        this.canvas.setMatrix(this.matrix);
    }

    @Override
    public ImageImp takeSnapshot() {
        return new AndroidImageImp(Bitmap.createBitmap(this.bitmap));
    }

    @Override
    public Size textSize(String text) {
        Rect bounds = new Rect();
        this.strokePaint.getTextBounds(text, 0, text.length(), bounds);
        return new Size(bounds.width(), bounds.height());
    }

    AndroidCanvasImp() {
        this.fillPaint = new Paint();
        this.fillPaint.setStyle(Paint.Style.FILL);
        this.fillPaint.setAntiAlias(true);
        this.matrix = new Matrix();
        this.matrixArray = new float[9];
        this.matrixArray[Matrix.MPERSP_0] = 0f;
        this.matrixArray[Matrix.MPERSP_1] = 0f;
        this.matrixArray[Matrix.MPERSP_2] = 1f;
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
