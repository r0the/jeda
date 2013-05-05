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
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class JavaCanvasImp implements CanvasImp {

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
    public void drawCircle(final float x, final float y, final float radius) {
        assert radius > 0;

        final int diameter = (int) (2 * radius);
        this.graphics.drawOval((int) (x - radius), (int) (y - radius), diameter, diameter);
        this.modified();
    }

    @Override
    public void drawImage(final float x, final float y, final ImageImp image, final int alpha) {
        assert image != null;
        assert image instanceof JavaImageImp;
        assert 0 < alpha && alpha <= 255;

        if (alpha != 255) {
            this.graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
        }

        this.graphics.drawImage(((JavaImageImp) image).bufferedImage, (int) x, (int) y, null);
        if (alpha != 255) {
            this.graphics.setPaintMode();
        }

        this.modified();
    }

    @Override
    public void drawLine(final float x1, final float y1, final float x2, final float y2) {
        this.graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        this.modified();
    }

    @Override
    public void drawPolygon(final float[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        this.graphics.drawPolygon(createPolygon(points));
        this.modified();
    }

    @Override
    public void drawRectangle(final float x, final float y, final float width, final float height) {
        this.graphics.drawRect((int) x, (int) y, (int) width, (int) height);
        this.modified();
    }

    @Override
    public void drawText(final float x, final float y, String text) {
        assert text != null;

        final TextLayout textLayout = this.textLayout(text);
        final Rectangle2D bounds = textLayout.getBounds();
        textLayout.draw(this.graphics, x, y - (int) bounds.getMinY());
        this.modified();
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

        this.modified();
    }

    @Override
    public void fillCircle(final float x, final float y, final float radius) {
        assert radius > 0;

        final int diameter = (int) (2 * radius);
        this.graphics.fillOval((int) (x - radius), (int) (y - radius), diameter, diameter);
        this.modified();
    }

    @Override
    public void fillPolygon(final float[] points) {
        assert points != null;
        assert points.length >= 6;
        assert points.length % 2 == 0;

        this.graphics.fillPolygon(createPolygon(points));
        this.modified();
    }

    @Override
    public void fillRectangle(final float x, final float y, final float width, final float height) {
        this.graphics.fillRect((int) x, (int) y, (int) width, (int) height);
        this.modified();
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
        this.modified();
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

    void modified() {
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

    private static Polygon createPolygon(final float[] points) {
        final Polygon result = new Polygon();
        for (int i = 0; i < points.length; i = i + 2) {
            result.addPoint((int) points[i], (int) points[i + 1]);
        }

        return result;
    }
}
