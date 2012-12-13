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

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

class JedaWindow extends JFrame {

    private final WindowManager manager;

    public JedaWindow(WindowManager manager) {
        super(graphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        this.manager = manager;
        this.addWindowListener(new WindowListener(this));
    }

    protected final void accept() {
        this.close();
        this.onAccept();
    }

    protected final void cancel() {
        this.close();
        this.onCancel();
    }

    protected void onAccept() {
    }

    protected void onCancel() {
    }

    protected final void init() {
        Point center = graphicsEnvironment().getCenterPoint();
        this.setLocation(center.x - this.getWidth() / 2, center.y - this.getHeight() / 2);
        this.setIconImage(loadImage("ch/jeda/resources/logo-16x16.png"));
    }

    protected final void setDefaultButton(JButton button) {
        this.getRootPane().setDefaultButton(button);
    }

    private void close() {
        if (this.manager.isShuttingDown()) {
            this.manager.notifyDisposing(this);
            this.dispose();
        }
        else {
            this.setVisible(false);
            this.manager.notifyHidden(this);
        }
    }

    private static GraphicsEnvironment graphicsEnvironment() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    private static Image loadImage(String path) {
        return new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(path)).getImage();
    }

    private static class WindowListener extends WindowAdapter {

        protected JedaWindow window;

        public WindowListener(JedaWindow window) {
            this.window = window;
        }

        @Override
        public void windowClosing(WindowEvent event) {
            this.window.cancel();
        }
    }
}
