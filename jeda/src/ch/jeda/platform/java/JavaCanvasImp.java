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
package ch.jeda.platform.java;

import ch.jeda.platform.ImageImp;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.ui.Color;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class JavaCanvasImp implements CanvasImp {

    private static final Map<?, ?> ALIASING_RENDERING_HINTS = initAliasingRenderingHints();
    private static final Map<?, ?> ANTI_ALIASING_RENDERING_HINTS = initAntiAliasingRenderingHints();
    private static final AffineTransform IDENTITY = new AffineTransform();
    private final BufferedImage bitmap;
    private final Graphics2D graphics;
    private final Map<FontRenderContext, Map<java.awt.Font, Map<String, TextLayout>>> textLayoutCache;

    JavaCanvasImp(final int width, final int height) {
        bitmap = createBufferedImage(width, height);
        graphics = bitmap.createGraphics();
        textLayoutCache = new HashMap();
    }

    @Override
    public void drawCanvas(final int x, final int y, final CanvasImp source) {
        assert source instanceof JavaCanvasImp;
        graphics.drawImage(((JavaCanvasImp) source).bitmap, 0, 0, null);
    }

    @Override
    public void drawEllipse(final float centerX, final float centerY, final float radiusX, final float radiusY) {
        graphics.drawOval((int) (centerX - radiusX), (int) (centerY - radiusY),
                          (int) (2.0 * radiusX), (int) (2.0 * radiusY));
    }

    @Override
    public void drawImage(final float x, final float y, final float width, final float height,
                          final ImageImp image, final int alpha) {
        assert image instanceof JavaImageImp;
        assert 0 < alpha && alpha <= 255;

        if (alpha != 255) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
        }

        graphics.drawImage(((JavaImageImp) image).bufferedImage, (int) x, (int) y, (int) width, (int) height,
                           null);
        if (alpha != 255) {
            graphics.setPaintMode();
        }
    }

    @Override
    public void drawPolygon(final float[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        graphics.drawPolygon(createPolygon(points));
    }

    @Override
    public void drawPolyline(final float[] points) {
        assert points != null;
        assert points.length >= 4;
        assert points.length % 2 == 0;

        for (int i = 0; i < points.length - 2; i = i + 2) {
            graphics.drawLine((int) points[i], (int) points[i + 1], (int) points[i + 2], (int) points[i + 3]);
        }
    }

    @Override
    public void drawRectangle(final float x, final float y, final float width, final float height) {
        graphics.drawRect((int) x, (int) y, (int) width, (int) height);
    }

    @Override
    public void drawText(final float x, final float y, String text) {
        assert text != null;

        final TextLayout textLayout = textLayout(text);
        final Rectangle2D bounds = textLayout.getBounds();
        textLayout.draw(graphics, x, (int) (y - bounds.getMinY()));
    }

    @Override
    public void fill() {
        if (graphics.getTransform().isIdentity()) {
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }
        else {
            final AffineTransform oldTransform = graphics.getTransform();
            graphics.setTransform(IDENTITY);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            graphics.setTransform(oldTransform);
        }
    }

    @Override
    public void fillEllipse(final float centerX, final float centerY, final float radiusX, final float radiusY) {
        graphics.fillOval((int) (centerX - radiusX), (int) (centerY - radiusY),
                          (int) (2f * radiusX), (int) (2f * radiusY));
    }

    @Override
    public void fillPolygon(final float[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        graphics.fillPolygon(createPolygon(points));
    }

    @Override
    public void fillRectangle(final float x, final float y, final float width, final float height) {
        graphics.fillRect((int) x, (int) y, (int) width, (int) height);
    }

    @Override
    public int getDpi() {
        return 96;
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Color getPixel(final int x, final int y) {
        assert contains(x, y);

        return new Color(bitmap.getRGB(x, y));
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public void resetTransformation() {
        graphics.setTransform(IDENTITY);
    }

    @Override
    public void rotateRad(final float angle, final float centerX, final float centerY) {
        graphics.rotate(angle, centerX, centerY);
    }

    @Override
    public void setAntiAliasing(final boolean antiAliasing) {
        if (antiAliasing) {
            graphics.setRenderingHints(ANTI_ALIASING_RENDERING_HINTS);
        }
        else {
            graphics.setRenderingHints(ALIASING_RENDERING_HINTS);
        }
    }

    @Override
    public void setColor(final Color color) {
        assert color != null;

        graphics.setColor(new java.awt.Color(color.getValue(), true));
    }

    @Override
    public void setTypeface(final TypefaceImp font) {
        assert font instanceof JavaTypefaceImp;

        graphics.setFont(((JavaTypefaceImp) font).font.deriveFont(graphics.getFont().getSize2D()));
    }

    @Override
    public void setLineWidth(final float lineWidth) {
        assert lineWidth >= 0f;

        graphics.setStroke(new BasicStroke(lineWidth));
    }

    @Override
    public void setPixel(final int x, final int y, final Color color) {
        assert contains(x, y);
        assert color != null;

        bitmap.setRGB(x, y, color.getValue());
    }

    @Override
    public void setTextSize(final float textSize) {
        assert textSize > 0f;

        graphics.setFont(graphics.getFont().deriveFont(textSize));
    }

    @Override
    public ImageImp takeSnapshot() {
        final BufferedImage result = createBufferedImage(getWidth(), getHeight());
        result.createGraphics().drawImage(bitmap, 0, 0, getWidth(), getHeight(), null);
        return new JavaImageImp(result);
    }

    @Override
    public int textHeight(final String text) {
        assert text != null;

        return (int) textLayout(text).getBounds().getHeight();
    }

    @Override
    public int textWidth(final String text) {
        assert text != null;

        return (int) textLayout(text).getAdvance();
    }

    BufferedImage getBitmap() {
        return bitmap;
    }

    private TextLayout textLayout(final String text) {
        final FontRenderContext frc = graphics.getFontRenderContext();
        final java.awt.Font font = graphics.getFont();
        if (!textLayoutCache.containsKey(frc)) {
            textLayoutCache.put(frc, new HashMap());
        }

        final Map<java.awt.Font, Map<String, TextLayout>> cacheByFont = textLayoutCache.get(frc);
        if (!cacheByFont.containsKey(font)) {
            cacheByFont.put(font, new HashMap());
        }

        final Map<String, TextLayout> byText = cacheByFont.get(font);
        if (!byText.containsKey(text)) {
            byText.put(text, new TextLayout(text, font, frc));
        }

        return byText.get(text);
    }

    @Override
    public void translate(final float tx, final float ty) {
        graphics.translate(tx, ty);
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < getWidth() && 0 <= y && y < getHeight();
    }

    private static BufferedImage createBufferedImage(final int width, final int height) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration().
            createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    private static Polygon createPolygon(final float[] points) {
        final Polygon result = new Polygon();
        for (int i = 0; i < points.length; i = i + 2) {
            result.addPoint((int) points[i], (int) points[i + 1]);
        }

        return result;
    }

    private static Map<?, ?> initAliasingRenderingHints() {
        final Map<RenderingHints.Key, Object> result = new HashMap<RenderingHints.Key, Object>();
        result.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        result.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        return result;
    }

    private static Map<?, ?> initAntiAliasingRenderingHints() {
        final Map<RenderingHints.Key, Object> result = new HashMap<RenderingHints.Key, Object>();
        result.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        result.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return result;
    }
}
