/*
 * Copyright (C) 2012 by Stefan Rothe
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

import ch.jeda.Size;
import ch.jeda.platform.EventsImp;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class ViewWindow extends JFrame implements JedaWindow {

    private final ImageCanvas canvas;
    private final JavaEventsImp eventsImp;
    private final boolean fullscreen;
    private final Size size;

    protected ViewWindow(Size size, boolean fullscreen) {
        super(GUI.graphicsConfiguration());
        this.canvas = new ImageCanvas(size);
        this.eventsImp = new JavaEventsImp(this.canvas);
        this.fullscreen = fullscreen;
        this.size = size;
        this.setIgnoreRepaint(true);
        this.setResizable(false);
        this.getContentPane().add(this.canvas);
        this.setUndecorated(fullscreen);
        this.pack();
        this.canvas.requestFocus();
        GUI.center(this);
        GUI.setIcon(this);
    }

    @Override
    public void onHide() {
        this.setVisible(false);
    }

    EventsImp getEventsImp() {
        return this.eventsImp;
    }

    Size getImageSize() {
        return this.size;
    }

    boolean isFullscreen() {
        return this.fullscreen;
    }

    void setImage(BufferedImage image) {
        this.canvas.setImage(image);
    }

    void update() {
        this.canvas.repaint();
        this.eventsImp.update();
    }

    private static class ImageCanvas extends java.awt.Canvas {

        private BufferedImage buffer;

        ImageCanvas(Size size) {
            Dimension d = new Dimension(size.width, size.height);
            this.setPreferredSize(d);
            this.setSize(d);
        }

        void setImage(BufferedImage buffer) {
            this.buffer = buffer;
        }

        @Override
        public void paint(Graphics graphics) {
            if (graphics != null) {
                graphics.drawImage(this.buffer, 0, 0, null);
            }
        }

        @Override
        public void repaint() {
            this.paint(this.getGraphics());
        }
    }
}
