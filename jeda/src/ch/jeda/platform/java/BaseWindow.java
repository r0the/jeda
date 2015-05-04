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

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

class BaseWindow extends JFrame {

    private final WindowManager manager;

    public BaseWindow(final WindowManager manager) {
        super(graphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
        this.manager = manager;
        this.addWindowListener(new WindowListener(this));
    }

    protected final void accept() {
        this.setVisible(false);
        this.manager.windowClosed(this);
        this.onAccept();
    }

    protected final void cancel() {
        this.setVisible(false);
        this.manager.windowClosed(this);
        this.onCancel();
    }

    protected void onAccept() {
    }

    protected void onCancel() {
    }

    public final void init() {
        final Point center = graphicsEnvironment().getCenterPoint();
        this.setLocation(center.x - this.getWidth() / 2, center.y - this.getHeight() / 2);
        this.setIconImage(loadImage("res/jeda/logo-16x16.png"));
    }

    protected final void setDefaultButton(final JButton button) {
        this.getRootPane().setDefaultButton(button);
    }

    private static GraphicsEnvironment graphicsEnvironment() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    private static Image loadImage(final String path) {
        return new ImageIcon(BaseWindow.class.getClassLoader().getResource(path)).getImage();
    }

    private static class WindowListener extends WindowAdapter {

        protected final BaseWindow window;

        public WindowListener(final BaseWindow window) {
            this.window = window;
        }

        @Override
        public void windowClosing(final WindowEvent event) {
            this.window.cancel();
        }
    }
}
