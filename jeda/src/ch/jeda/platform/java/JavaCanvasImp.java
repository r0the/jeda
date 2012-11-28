/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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
import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.Transformation;
import ch.jeda.ui.Color;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class JavaCanvasImp implements CanvasImp {

    private static final java.awt.Color CLEAR_COLOR = new java.awt.Color(0, 0, 0, 0);
    private final Map<FontRenderContext, Map<java.awt.Font, Map<String, TextLayout>>> textLayoutCache;
    private BufferedImage buffer;
    private Graphics2D graphics;
    private Size size;

    JavaCanvasImp() {
        this.textLayoutCache = new HashMap();
    }

    JavaCanvasImp(Size size) {
        this();
        this.setBuffer(GUI.createBufferedImage(size));
    }

    @Override
    public void clear() {
        this.graphics.setBackground(CLEAR_COLOR);
        this.graphics.clearRect(0, 0, this.size.width, this.size.height);
        this.modified();
    }

    @Override
    public void copyFrom(Location topLeft, CanvasImp source) {
        assert topLeft != null;
        assert source != null;

        if (source instanceof JavaCanvasImp) {
            this.graphics.drawImage(((JavaCanvasImp) source).buffer, topLeft.x, topLeft.y, null);
        }
        else {
            throw new RuntimeException("Invalid source type");
        }
    }

    @Override
    public void drawCircle(Location center, int radius) {
        assert center != null;
        assert radius > 0;

        int diameter = 2 * radius;
        this.graphics.drawOval(center.x - radius, center.y - radius, diameter, diameter);
        this.modified();
    }

    @Override
    public void drawImage(Location topLeft, ImageImp image) {
        assert topLeft != null;
        assert image != null;
        assert image instanceof JavaImageImp;

        BufferedImage awtImage = ((JavaImageImp) image).getBufferedImage();
        this.graphics.drawImage(awtImage, topLeft.x, topLeft.y, null);
        this.modified();
    }

    @Override
    public void drawLine(Location start, Location end) {
        assert start != null;
        assert end != null;

        this.graphics.drawLine(start.x, start.y, end.x, end.y);
        this.modified();
    }

    @Override
    public void drawPolygon(Iterable<Location> edges) {
        assert edges != null;

        this.graphics.drawPolygon(createPolygon(edges));
        this.modified();
    }

    @Override
    public void drawRectangle(Location topLeft, Size size) {
        assert topLeft != null;
        assert size != null;

        this.graphics.drawRect(topLeft.x, topLeft.y, size.width, size.height);
        this.modified();
    }

    @Override
    public void drawString(Location topLeft, String text) {
        assert topLeft != null;
        assert text != null;

        TextLayout textLayout = this.textLayout(text);
        Rectangle2D bounds = textLayout.getBounds();
        textLayout.draw(this.graphics, topLeft.x, topLeft.y - (int) bounds.getMinY());
        this.modified();
    }

    @Override
    public void fill() {
        this.graphics.clearRect(0, 0, this.size.width, this.size.height);
        this.modified();
    }

    @Override
    public void fillCircle(Location center, int radius) {
        assert center != null;
        assert radius > 0;

        int diameter = 2 * radius;
        this.graphics.fillOval(center.x - radius, center.y - radius, diameter, diameter);
        this.modified();
    }

    @Override
    public void fillPolygon(Iterable<Location> edges) {
        assert edges != null;

        this.graphics.fillPolygon(createPolygon(edges));
        this.modified();
    }

    @Override
    public void fillRectangle(Location topLeft, Size size) {
        assert topLeft != null;
        assert size != null;

        this.graphics.fillRect(topLeft.x, topLeft.y, size.width, size.height);
        this.modified();
    }

    @Override
    public void floodFill(Location pos, Color oldColor, Color newColor) {
        assert pos != null;
        assert oldColor != null;
        assert newColor != null;

        Stack<Location> stack = new Stack<Location>();
        stack.push(pos);
        int oldCol = oldColor.getValue();
        int newCol = newColor.getValue();
        while (!stack.isEmpty()) {
            pos = stack.pop();
            if (this.buffer.getRGB(pos.x, pos.y) == oldCol) {
                this.buffer.setRGB(pos.x, pos.y, newCol);
                stack.push(new Location(pos.x, pos.y + 1));
                stack.push(new Location(pos.x, pos.y - 1));
                stack.push(new Location(pos.x + 1, pos.y));
                stack.push(new Location(pos.x - 1, pos.y));
            }
        }
        this.modified();
    }

    @Override
    public double getLineWidth() {
        return ((BasicStroke) this.graphics.getStroke()).getLineWidth();
    }

    @Override
    public Color getPixelAt(Location location) {
        assert location != null;
        assert this.size.contains(location);

        return new Color(this.buffer.getRGB(location.x, location.y));
    }

    @Override
    public Size getSize() {
        return this.size;
    }

    @Override
    public void setAlpha(int alpha) {
        assert 0 <= alpha && alpha <= 255;

        if (alpha == 255) {
            this.graphics.setPaintMode();
        }
        else if (alpha == 0) {
            this.graphics.setComposite(AlphaComposite.Dst);
        }
        else {
            this.graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
        }
    }

    @Override
    public void setColor(Color color) {
        assert color != null;

        this.graphics.setBackground(new java.awt.Color(color.getValue(), true));
        this.graphics.setColor(new java.awt.Color(color.getValue(), true));
    }

    @Override
    public void setFontSize(int fontSize) {
        assert fontSize > 0;

        this.graphics.setFont(this.graphics.getFont().deriveFont((float) fontSize));
    }

    @Override
    public void setLineWidth(double width) {
        this.graphics.setStroke(new BasicStroke((float) width));
    }

    @Override
    public void setPixelAt(Location location, Color color) {
        assert location != null;
        assert this.size.contains(location);
        assert color != null;

        this.buffer.setRGB(location.x, location.y, color.getValue());
        this.modified();
    }

    @Override
    public void setTransformation(Transformation transformation) {
        assert transformation != null;

        this.graphics.setTransform(new AffineTransform(
                transformation.scaleX, transformation.skewY,
                transformation.skewX, transformation.scaleY,
                transformation.translateX, transformation.translateY));
    }

    @Override
    public ImageImp takeSnapshot() {
        BufferedImage result = GUI.createBufferedImage(this.size);
        result.createGraphics().drawImage(this.buffer, 0, 0, this.size.width, this.size.height, null);
        return new JavaImageImp(result);
    }

    @Override
    public Size textSize(String text) {
        assert text != null;

        Rectangle2D bounds = textLayout(text).getBounds();
        return new Size((int) bounds.getWidth(), (int) bounds.getHeight());
    }

    private TextLayout textLayout(String text) {
        FontRenderContext frc = this.graphics.getFontRenderContext();
        java.awt.Font font = this.graphics.getFont();
        if (!this.textLayoutCache.containsKey(frc)) {
            this.textLayoutCache.put(frc, new HashMap<java.awt.Font, Map<String, TextLayout>>());
        }
        Map<java.awt.Font, Map<String, TextLayout>> cacheByFont = this.textLayoutCache.get(frc);
        if (!cacheByFont.containsKey(font)) {
            cacheByFont.put(font, new HashMap<String, TextLayout>());
        }
        Map<String, TextLayout> byText = cacheByFont.get(font);
        if (!byText.containsKey(text)) {
            byText.put(text, new TextLayout(text, font, frc));
        }
        return byText.get(text);
    }

    void modified() {
    }

    final void setBuffer(BufferedImage buffer) {
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
        this.size = new Size(buffer.getWidth(), buffer.getHeight());
    }

    private static Polygon createPolygon(Iterable<Location> edges) {
        Polygon result = new Polygon();
        for (Location edge : edges) {
            result.addPoint(edge.x, edge.y);
        }
        return result;
    }
}
