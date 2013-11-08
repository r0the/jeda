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
package ch.jeda.platform.java;

import ch.jeda.platform.WindowImp;
import ch.jeda.event.Event;
import ch.jeda.ui.MouseCursor;
import ch.jeda.ui.WindowFeature;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.EnumMap;
import java.util.EnumSet;

abstract class JavaWindowImp extends JavaCanvasImp implements WindowImp {

    private static final EnumMap<MouseCursor, Cursor> MOUSE_CURSOR_MAP = initCursorMap();
    protected final CanvasWindow canvasWindow;

    @Override
    public void close() {
        this.canvasWindow.dispose();
    }

    @Override
    public Event[] fetchEvents() {
        return this.canvasWindow.fetchEvents();
    }

    @Override
    public EnumSet<WindowFeature> getFeatures() {
        return this.canvasWindow.getFeatures();
    }

    @Override
    public boolean isActive() {
        return this.canvasWindow.isActive();
    }

    @Override
    public boolean isValid() {
        return this.canvasWindow.isVisible();
    }

    @Override
    public void setFeature(final WindowFeature feature, final boolean enabled) {
        this.canvasWindow.setFeature(feature, enabled);
    }

    @Override
    public void setMouseCursor(final MouseCursor mouseCursor) {
        assert mouseCursor != null;

        this.canvasWindow.setCursor(MOUSE_CURSOR_MAP.get(mouseCursor));
    }

    @Override
    public void setTitle(final String title) {
        this.canvasWindow.setTitle(title);
    }

    @Override
    public abstract void update();

    static JavaWindowImp create(final CanvasWindow viewWindow) {
        if (viewWindow.getFeatures().contains(WindowFeature.DOUBLE_BUFFERED)) {
            return new DoubleBufferedWindowImp(viewWindow);
        }
        else {
            return new SingleBufferedWindowImp(viewWindow);
        }
    }

    JavaWindowImp(final CanvasWindow viewWindow) {
        this.canvasWindow = viewWindow;
    }

    private static Cursor createInvisibleCursor() {
        final java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        final int[] pixels = new int[16 * 16];
        final java.awt.Image image = toolkit.createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
        return toolkit.createCustomCursor(image, new java.awt.Point(0, 0), "invisibleCursor");
    }

    private static EnumMap<MouseCursor, Cursor> initCursorMap() {
        final EnumMap<MouseCursor, Cursor> result = new EnumMap(MouseCursor.class);
        result.put(MouseCursor.CROSSHAIR, new Cursor(Cursor.CROSSHAIR_CURSOR));
        result.put(MouseCursor.DEFAULT, new Cursor(Cursor.DEFAULT_CURSOR));
        result.put(MouseCursor.HAND, new Cursor(Cursor.HAND_CURSOR));
        result.put(MouseCursor.INVISIBLE, createInvisibleCursor());
        result.put(MouseCursor.TEXT, new Cursor(Cursor.TEXT_CURSOR));
        return result;
    }

    private static class DoubleBufferedWindowImp extends JavaWindowImp {

        private BufferedImage backBuffer;
        private BufferedImage frontBuffer;

        public DoubleBufferedWindowImp(final CanvasWindow canvasWindow) {
            super(canvasWindow);
            this.backBuffer = createBufferedImage(canvasWindow.getImageWidth(),
                                                  canvasWindow.getImageHeight());
            this.frontBuffer = createBufferedImage(canvasWindow.getImageWidth(),
                                                   canvasWindow.getImageHeight());
            canvasWindow.setImage(this.frontBuffer);
            this.setBuffer(this.backBuffer);
        }

        @Override
        public void update() {
            final BufferedImage temp = this.frontBuffer;
            this.frontBuffer = this.backBuffer;
            this.backBuffer = temp;
            super.setBuffer(this.backBuffer);
            this.canvasWindow.setImage(this.frontBuffer);
            this.canvasWindow.update();
        }
    }

    private static class SingleBufferedWindowImp extends JavaWindowImp {

        private final BufferedImage buffer;

        public SingleBufferedWindowImp(final CanvasWindow canvasWindow) {
            super(canvasWindow);
            this.buffer = createBufferedImage(canvasWindow.getImageWidth(),
                                              canvasWindow.getImageHeight());
            canvasWindow.setImage(this.buffer);
            this.setBuffer(this.buffer);
        }

        @Override
        public void update() {
            this.canvasWindow.update();
        }
    }
}
