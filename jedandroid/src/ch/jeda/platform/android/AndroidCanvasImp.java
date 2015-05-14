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

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.CanvasTransformation;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.ui.Color;

class AndroidCanvasImp implements CanvasImp {

    private final Paint fillPaint;
    private final Paint imagePaint;
    private final Matrix matrix;
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
        matrix = new Matrix();
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
    public void drawEllipse(final int x, final int y, final int width, final int height) {
        canvas.drawOval(new RectF(x, y, x + width, y + height), strokePaint);
    }

    @Override
    public void drawImage(final int x, final int y, final ImageImp image, final int alpha) {
        assert image != null;
        assert image instanceof AndroidImageImp;
        assert 0 < alpha && alpha <= 255;

        imagePaint.setAlpha(alpha);
        canvas.drawBitmap(((AndroidImageImp) image).bitmap, x, y, imagePaint);
    }

    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        canvas.drawLine(x1, y1, x2, y2, strokePaint);
    }

    @Override
    public void drawPolygon(final int[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        canvas.drawPath(createPath(points), strokePaint);
    }

    @Override
    public void drawRectangle(final int x, final int y, final int width, final int height) {
        canvas.drawRect(x, y, x + width, y + height, strokePaint);
    }

    @Override
    public void drawText(final int x, final int y, final String text) {
        assert text != null;

        canvas.drawText(text, x, y - (int) textPaint.ascent(), textPaint);
    }

    @Override
    public void fill() {
        canvas.drawColor(fillPaint.getColor());
    }

    @Override
    public void fillEllipse(final int x, final int y, final int width, final int height) {
        canvas.drawOval(new RectF(x, y, x + width, y + height), fillPaint);
    }

    @Override
    public void fillPolygon(final int[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        canvas.drawPath(createPath(points), fillPaint);
    }

    @Override
    public void fillRectangle(final int x, final int y, final int width, final int height) {
        canvas.drawRect(x, y, x + width, y + height, fillPaint);
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public double getLineWidth() {
        return strokePaint.getStrokeWidth();
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
    public void setLineWidth(final double lineWidth) {
        assert lineWidth >= 0.0;

        strokePaint.setStrokeWidth((float) lineWidth);
    }

    @Override
    public void setPixel(final int x, final int y, final Color color) {
        assert contains(x, y);
        assert color != null;

        pixelPaint.setColor(color.getValue());
        bitmap.setPixel(x, y, color.getValue());
    }

    @Override
    public void setTextSize(final int textSize) {
        textPaint.setTextSize(textSize);
    }

    @Override
    public void setTransformation(final CanvasTransformation transformation) {
        assert transformation != null;

        matrix.setTranslate((float) transformation.translationX, (float) transformation.translationY);
        matrix.preRotate((float) Math.toDegrees(transformation.rotation));
        float scale = (float) transformation.scale;
        matrix.preScale(scale, scale);
        canvas.setMatrix(matrix);
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

    private static Path createPath(final int[] points) {
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
