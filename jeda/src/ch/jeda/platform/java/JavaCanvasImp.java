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
import ch.jeda.platform.CanvasTransformation;
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
    private final AffineTransform affineTransform;
    private final BufferedImage bitmap;
    private final Graphics2D graphics;
    private final Map<FontRenderContext, Map<java.awt.Font, Map<String, TextLayout>>> textLayoutCache;

    JavaCanvasImp(final int width, final int height) {
        affineTransform = new AffineTransform();
        textLayoutCache = new HashMap();
        bitmap = createBufferedImage(width, height);
        graphics = bitmap.createGraphics();
    }

    @Override
    public void drawCanvas(final int x, final int y, final CanvasImp source) {
        assert source instanceof JavaCanvasImp;

        graphics.drawImage(((JavaCanvasImp) source).bitmap, x, y, null);
    }

    @Override
    public void drawEllipse(final int x, final int y, final int width, final int height) {
        graphics.drawOval(x, y, width, height);
    }

    @Override
    public void drawImage(final int x, final int y, final ImageImp image, final int alpha) {
        assert image instanceof JavaImageImp;
        assert 0 < alpha && alpha <= 255;

        if (alpha != 255) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
        }

        graphics.drawImage(((JavaImageImp) image).bufferedImage, x, y, null);
        if (alpha != 255) {
            graphics.setPaintMode();
        }
    }

    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawPolygon(final int[] x, final int[] y) {
        assert x != null;
        assert y != null;
        assert x.length >= 3;
        assert x.length == y.length;

        graphics.drawPolygon(createPolygon(x, y));
    }

    @Override
    public void drawRectangle(final int x, final int y, final int width, final int height) {
        graphics.drawRect(x, y, width, height);
    }

    @Override
    public void drawText(final int x, final int y, String text) {
        assert text != null;

        final TextLayout textLayout = textLayout(text);
        final Rectangle2D bounds = textLayout.getBounds();
        textLayout.draw(graphics, x, y - (int) bounds.getMinY());
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
    public void fillEllipse(final int x, final int y, final int width, final int height) {
        graphics.fillOval(x, y, width, height);
    }

    @Override
    public void fillPolygon(final int[] x, final int[] y) {
        assert x != null;
        assert y != null;
        assert x.length >= 3;
        assert x.length == y.length;

        graphics.fillPolygon(createPolygon(x, y));
    }

    @Override
    public void fillRectangle(final int x, final int y, final int width, final int height) {
        graphics.fillRect(x, y, width, height);
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public double getLineWidth() {
        return ((BasicStroke) graphics.getStroke()).getLineWidth();
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
    public void setLineWidth(final double lineWidth) {
        assert lineWidth >= 0.0;

        graphics.setStroke(new BasicStroke((float) lineWidth));
    }

    @Override
    public void setPixel(final int x, final int y, final Color color) {
        assert contains(x, y);
        assert color != null;

        bitmap.setRGB(x, y, color.getValue());
    }

    @Override
    public void setTextSize(final int textSize) {
        assert textSize > 0;

        graphics.setFont(graphics.getFont().deriveFont((float) textSize));
    }

    @Override
    public void setTransformation(final CanvasTransformation transformation) {
        affineTransform.setToIdentity();
        affineTransform.translate(transformation.translationX, transformation.translationY);
        affineTransform.rotate(transformation.rotation);
        affineTransform.scale(transformation.scale, transformation.scale);
        graphics.setTransform(affineTransform);
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

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < getWidth() && 0 <= y && y < getHeight();
    }

    private static BufferedImage createBufferedImage(final int width, final int height) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration().
            createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    private static Polygon createPolygon(final int[] x, final int[] y) {
        final Polygon result = new Polygon();
        for (int i = 0; i < x.length; ++i) {
            result.addPoint(x[i], y[i]);
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
