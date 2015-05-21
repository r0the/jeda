/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import ch.jeda.MathF;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.ui.Color;

class AndroidCanvasImp implements CanvasImp {

    private final Paint fillPaint;
    private final Paint imagePaint;
    private final Paint pixelPaint;
    private final Paint strokePaint;
    private final Paint textPaint;
    private Bitmap bitmap;
    private Canvas canvas;

    AndroidCanvasImp() {
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);
        imagePaint = new Paint();
        pixelPaint = new Paint();
        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        textPaint = new Paint();
    }

    @Override
    public void drawCanvas(final int x, final int y, final CanvasImp source) {
        assert source != null;
        assert source instanceof AndroidCanvasImp;

        canvas.drawBitmap(((AndroidCanvasImp) source).bitmap, x, y, fillPaint);
    }

    @Override
    public void drawEllipse(final float centerX, final float centerY, final float radiusX, final float radiusY) {
        canvas.drawOval(new RectF(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY),
                        strokePaint);
    }

    @Override
    public void drawImage(final float x, final float y, final float width, final float height, final ImageImp image,
                          final int alpha) {
        assert image instanceof AndroidImageImp;
        assert 0 < alpha && alpha <= 255;

        imagePaint.setAlpha(alpha);
        RectF dest = new RectF(x, y, x + width, y + height);
        canvas.drawBitmap(((AndroidImageImp) image).bitmap, null, dest, imagePaint);
    }

    @Override
    public void drawPolyline(final float[] points) {
        for (int i = 0; i < points.length - 2; i = i + 2) {
            canvas.drawLine(points[i], points[i + 1], points[i + 2], points[i + 3], strokePaint);
        }
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
        canvas.drawRect(x, y, x + width, y + height, strokePaint);
    }

    @Override
    public void drawText(final float x, final float y, final String text) {
        assert text != null;

        canvas.drawText(text, x, y - textPaint.ascent(), textPaint);
    }

    @Override
    public void fill() {
        canvas.drawColor(fillPaint.getColor());
    }

    @Override
    public void fillEllipse(final float centerX, final float centerY, final float radiusX, final float radiusY) {
        canvas.drawOval(new RectF(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY),
                        fillPaint);
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
        canvas.drawRect(x, y, x + width, y + height, fillPaint);
    }

    @Override
    public int getDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Color getPixel(final int x, final int y) {
        assert contains(x, y);

        return new Color(bitmap.getPixel(x, y));
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public void resetTransformation() {
        Matrix matrix = new Matrix();
        matrix.reset();
        canvas.setMatrix(matrix);
    }

    @Override
    public void rotateRad(final float angle, final float centerX, final float centerY) {
        canvas.rotate(MathF.toDegrees(angle), centerX, centerY);
    }

    @Override
    public void setAntiAliasing(final boolean antiAliasing) {
        fillPaint.setAntiAlias(antiAliasing);
        strokePaint.setAntiAlias(antiAliasing);
        textPaint.setAntiAlias(antiAliasing);
    }

    @Override
    public void setColor(final Color color) {
        assert color != null;

        fillPaint.setColor(color.getValue());
        strokePaint.setColor(color.getValue());
        textPaint.setColor(color.getValue());
    }

    @Override
    public void setLineWidth(final float lineWidth) {
        assert lineWidth >= 0.0;

        strokePaint.setStrokeWidth(lineWidth);
    }

    @Override
    public void setPixel(final int x, final int y, final Color color) {
        assert contains(x, y);
        assert color != null;

        pixelPaint.setColor(color.getValue());
        bitmap.setPixel(x, y, color.getValue());
    }

    @Override
    public void setTextSize(final float textSize) {
        textPaint.setTextSize(textSize);
    }

    @Override
    public void setTypeface(final TypefaceImp typeface) {
        assert typeface instanceof AndroidTypefaceImp;

        textPaint.setTypeface(((AndroidTypefaceImp) typeface).imp);
    }

    @Override
    public ImageImp takeSnapshot() {
        return new AndroidImageImp(Bitmap.createBitmap(bitmap));
    }

    @Override
    public int textHeight(final String text) {
        assert text != null;

        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    @Override
    public int textWidth(final String text) {
        assert text != null;

        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    @Override
    public void translate(final float tx, final float ty) {
        canvas.translate(tx, ty);
    }

    Canvas getCanvas() {
        return canvas;
    }

    Bitmap getBitmap() {
        return bitmap;
    }

    final void init(final int width, final int height) {
        bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < getWidth() && 0 <= y && y < getHeight();
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
