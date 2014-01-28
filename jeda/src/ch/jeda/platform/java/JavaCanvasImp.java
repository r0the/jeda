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
package ch.jeda.platform.java;

import ch.jeda.platform.ImageImp;
import ch.jeda.platform.CanvasImp;
import ch.jeda.Transformation;
import ch.jeda.ui.Color;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
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
    private final float[] matrix;
    private final Map<FontRenderContext, Map<java.awt.Font, Map<String, TextLayout>>> textLayoutCache;
    private BufferedImage buffer;
    private Graphics2D graphics;
    private int height;
    private int width;

    JavaCanvasImp() {
        this.affineTransform = new AffineTransform();
        this.matrix = new float[6];
        this.textLayoutCache = new HashMap();
    }

    JavaCanvasImp(final int width, final int height) {
        this();
        this.setBuffer(createBufferedImage(width, height));
    }

    @Override
    public void copyFrom(final int x, final int y, final CanvasImp source) {
        assert source != null;
        assert source instanceof JavaCanvasImp;

        this.graphics.drawImage(((JavaCanvasImp) source).buffer, x, y, null);
    }

    @Override
    public void drawCircle(final int x, final int y, final int radius) {
        assert radius > 0;

        final int diameter = 2 * radius;
        this.graphics.drawOval(x - radius, y - radius, diameter, diameter);
    }

    @Override
    public void drawImage(final int x, final int y, final ImageImp image, final int alpha) {
        assert image != null;
        assert image instanceof JavaImageImp;
        assert 0 < alpha && alpha <= 255;

        if (alpha != 255) {
            this.graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
        }

        this.graphics.drawImage(((JavaImageImp) image).bufferedImage, x, y, null);
        if (alpha != 255) {
            this.graphics.setPaintMode();
        }
    }

    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        this.graphics.drawLine((int) x1, y1, x2, y2);
    }

    @Override
    public void drawPolygon(final int[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        this.graphics.drawPolygon(createPolygon(points));
    }

    @Override
    public void drawRectangle(final int x, final int y, final int width, final int height) {
        this.graphics.drawRect(x, y, width, height);
    }

    @Override
    public void drawText(final int x, final int y, String text) {
        assert text != null;

        final TextLayout textLayout = this.textLayout(text);
        final Rectangle2D bounds = textLayout.getBounds();
        textLayout.draw(this.graphics, x, y - (int) bounds.getMinY());
    }

    @Override
    public void fill() {
        if (this.graphics.getTransform().isIdentity()) {
            this.graphics.fillRect(0, 0, this.width, this.height);
        }
        else {
            final AffineTransform oldTransform = this.graphics.getTransform();
            this.graphics.setTransform(IDENTITY);
            this.graphics.fillRect(0, 0, this.width, this.height);
            this.graphics.setTransform(oldTransform);
        }
    }

    @Override
    public void fillCircle(final int x, final int y, final int radius) {
        assert radius > 0;

        final int diameter = 2 * radius;
        this.graphics.fillOval(x - radius, y - radius, diameter, diameter);
    }

    @Override
    public void fillPolygon(final int[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        this.graphics.fillPolygon(createPolygon(points));
    }

    @Override
    public void fillRectangle(final int x, final int y, final int width, final int height) {
        this.graphics.fillRect(x, y, width, height);
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public float getLineWidth() {
        return ((BasicStroke) this.graphics.getStroke()).getLineWidth();
    }

    @Override
    public Color getPixelAt(final int x, final int y) {
        assert this.contains(x, y);

        return new Color(this.buffer.getRGB(x, y));
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void setAntiAliasing(final boolean antiAliasing) {
        if (antiAliasing) {
            this.graphics.setRenderingHints(ANTI_ALIASING_RENDERING_HINTS);
        }
        else {
            this.graphics.setRenderingHints(ALIASING_RENDERING_HINTS);
        }
    }

    @Override
    public void setColor(final Color color) {
        assert color != null;

        this.graphics.setColor(new java.awt.Color(color.getValue(), true));
    }

    @Override
    public void setFontSize(final int fontSize) {
        assert fontSize > 0;

        this.graphics.setFont(this.graphics.getFont().deriveFont((float) fontSize));
    }

    @Override
    public void setLineWidth(final float lineWidth) {
        assert lineWidth >= 0f;

        this.graphics.setStroke(new BasicStroke(lineWidth));
    }

    @Override
    public void setPixelAt(final int x, final int y, final Color color) {
        assert this.contains(x, y);
        assert color != null;

        this.buffer.setRGB(x, y, color.getValue());
    }

    @Override
    public void setTransformation(final Transformation transformation) {
        assert transformation != null;

        transformation.copyToArray(this.matrix);
        this.affineTransform.setTransform(
            this.matrix[0], this.matrix[3], this.matrix[1],
            this.matrix[4], this.matrix[2], this.matrix[5]);
        this.graphics.setTransform(this.affineTransform);
    }

    @Override
    public ImageImp takeSnapshot() {
        final BufferedImage result = createBufferedImage(this.width, this.height);
        result.createGraphics().drawImage(this.buffer, 0, 0, this.width, this.height, null);
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

        return (int) textLayout(text).getBounds().getWidth();
    }

    private TextLayout textLayout(final String text) {
        final FontRenderContext frc = this.graphics.getFontRenderContext();
        final java.awt.Font font = this.graphics.getFont();
        if (!this.textLayoutCache.containsKey(frc)) {
            this.textLayoutCache.put(frc, new HashMap());
        }

        final Map<java.awt.Font, Map<String, TextLayout>> cacheByFont = this.textLayoutCache.get(frc);
        if (!cacheByFont.containsKey(font)) {
            cacheByFont.put(font, new HashMap());
        }

        final Map<String, TextLayout> byText = cacheByFont.get(font);
        if (!byText.containsKey(text)) {
            byText.put(text, new TextLayout(text, font, frc));
        }

        return byText.get(text);
    }

    final void setBuffer(final BufferedImage buffer) {
        java.awt.Color oldColor = null;
        java.awt.Font oldFont = null;
        Composite oldComposite = null;
        if (this.graphics != null) {
            oldColor = this.graphics.getColor();
            oldComposite = this.graphics.getComposite();
            oldFont = this.graphics.getFont();
        }

        this.buffer = buffer;
        this.graphics = buffer.createGraphics();
        if (oldColor != null) {
            this.graphics.setColor(oldColor);
            this.graphics.setComposite(oldComposite);
            this.graphics.setFont(oldFont);
        }

        this.width = buffer.getWidth();
        this.height = buffer.getHeight();
    }

    private boolean contains(final int x, final int y) {
        return 0 <= x && x < this.width && 0 <= y && y < this.height;
    }

    static BufferedImage createBufferedImage(final int width, final int height) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration().
            createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    private static Polygon createPolygon(final int[] points) {
        final Polygon result = new Polygon();
        for (int i = 0; i < points.length; i = i + 2) {
            result.addPoint(points[i], points[i + 1]);
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
