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
import ch.jeda.Transformation;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.ui.Color;

class AndroidCanvasImp implements CanvasImp {

    private final Paint fillPaint;
    private final Paint imagePaint;
    private final Matrix matrix;
    private final float[] matrixArray;
    private final Paint pixelPaint;
    private final Paint strokePaint;
    private final Paint textPaint;
    private Bitmap bitmap;
    private Canvas canvas;

    @Override
    public void copyFrom(final int x, final int y, final CanvasImp source) {
        assert source != null;
        assert source instanceof AndroidCanvasImp;

        this.canvas.drawBitmap(((AndroidCanvasImp) source).bitmap, x, y, this.fillPaint);
    }

    @Override
    public void drawCircle(final float x, final float y, final float radius) {
        assert radius > 0;

        this.canvas.drawCircle(x, y, radius, this.strokePaint);
    }

    @Override
    public void drawImage(final float x, final float y, final ImageImp image, final int alpha) {
        assert image != null;
        assert image instanceof AndroidImageImp;
        assert 0 < alpha && alpha <= 255;

        this.imagePaint.setAlpha(alpha);
        this.canvas.drawBitmap(((AndroidImageImp) image).bitmap, x, y, this.imagePaint);
    }

    @Override
    public void drawLine(final float x1, final float y1, final float x2, final float y2) {
        this.canvas.drawLine(x1, y1, x2, y2, this.strokePaint);
    }

    @Override
    public void drawPolygon(final float[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        this.canvas.drawPath(createPath(points), this.strokePaint);
    }

    @Override
    public void drawRectangle(final float x, final float y, final float width, final float height) {
        this.canvas.drawRect(x, y, x + width, y + height, this.strokePaint);
    }

    @Override
    public void drawText(final float x, final float y, final String text) {
        assert text != null;

        this.canvas.drawText(text, x, y - (int) this.textPaint.ascent(), this.textPaint);
    }

    @Override
    public void fill() {
        this.canvas.drawColor(this.fillPaint.getColor());
    }

    @Override
    public void fillCircle(final float x, final float y, final float radius) {
        assert radius > 0;

        this.canvas.drawCircle(x, y, radius, this.fillPaint);
    }

    @Override
    public void fillPolygon(final float[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        this.canvas.drawPath(createPath(points), this.fillPaint);
    }

    @Override
    public void fillRectangle(final float x, final float y, final float width, final float height) {
        this.canvas.drawRect(x, y, x + width, y + height, this.fillPaint);
    }

    @Override
    public int getHeight() {
        return this.bitmap.getHeight();
    }

    @Override
    public float getLineWidth() {
        return this.strokePaint.getStrokeWidth();
    }

    @Override
    public Color getPixelAt(final int x, final int y) {
        assert this.contains(x, y);

        return new Color(this.bitmap.getPixel(x, y));
    }

    @Override
    public int getWidth() {
        return this.bitmap.getWidth();
    }

    public void setColor(final Color color) {
        assert color != null;

        this.fillPaint.setColor(color.getValue());
        this.strokePaint.setColor(color.getValue());
        this.textPaint.setColor(color.getValue());
    }

    @Override
    public void setFontSize(final int fontSize) {
        this.textPaint.setTextSize(fontSize);
    }

    @Override
    public void setLineWidth(final float lineWidth) {
        assert lineWidth >= 0f;

        this.strokePaint.setStrokeWidth(lineWidth);
    }

    @Override
    public void setPixelAt(final int x, final int y, final Color color) {
        assert this.contains(x, y);
        assert color != null;

        this.pixelPaint.setColor(color.getValue());
        this.bitmap.setPixel(x, y, color.getValue());
    }

    @Override
    public void setTransformation(final Transformation transformation) {
        assert transformation != null;

        this.matrixArray[Matrix.MPERSP_0] = 0;
        this.matrixArray[Matrix.MPERSP_1] = 0;
        this.matrixArray[Matrix.MPERSP_2] = 1;
        transformation.copyToArray(this.matrixArray);
        this.matrix.setValues(this.matrixArray);
        this.canvas.setMatrix(this.matrix);
    }

    @Override
    public ImageImp takeSnapshot() {
        return new AndroidImageImp(Bitmap.createBitmap(this.bitmap));
    }

    @Override
    public int textHeight(final String text) {
        assert text != null;

        Rect bounds = new Rect();
        this.textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    @Override
    public int textWidth(final String text) {
        assert text != null;

        Rect bounds = new Rect();
        this.textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    AndroidCanvasImp() {
        this.fillPaint = new Paint();
        this.fillPaint.setStyle(Paint.Style.FILL);
        this.fillPaint.setAntiAlias(true);
        this.imagePaint = new Paint();
        this.matrix = new Matrix();
        this.matrixArray = new float[9];
        this.pixelPaint = new Paint();
        this.strokePaint = new Paint();
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setAntiAlias(true);
        this.textPaint = new Paint();
    }

    Canvas getCanvas() {
        return this.canvas;
    }

    Bitmap getBitmap() {
        return this.bitmap;
    }

    final void init(final int width, final int height) {
        this.bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < this.getWidth() && 0 <= y && y < this.getHeight();
    }

    private static Path createPath(final float[] points) {
        final Path result = new Path();
        boolean first = true;
        for (int i = 0; i < points.length; i = i + 2) {
            if (first) {
                result.moveTo(points[i], points[i + 1]);
                first = false;
            }
            else {
                result.lineTo(points[i], points[i + 1]);
            }
        }

        result.close();
        return result;
    }
}
