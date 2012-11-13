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
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class GUI {

    static void center(JDialog dialog) {
        Point center = graphicsEnvironment().getCenterPoint();
        dialog.setLocation(center.x - dialog.getWidth() / 2, center.y - dialog.getHeight() / 2);
    }

    static void center(JFrame frame) {
        Point center = graphicsEnvironment().getCenterPoint();
        frame.setLocation(center.x - frame.getWidth() / 2, center.y - frame.getHeight() / 2);
    }

    static BufferedImage createBufferedImage(Size size) {
        return graphicsConfiguration().createCompatibleImage(size.width, size.height, Transparency.TRANSLUCENT);
    }

    static GraphicsConfiguration graphicsConfiguration() {
        return graphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    static Icon loadIcon(String path) {
        return loadImageIcon(path);
    }

    static Image loadImage(String path) {
        return loadImageIcon(path).getImage();
    }

    static void setIcon(JDialog dialog) {
        dialog.setIconImage(loadImage("ch/jeda/resources/logo-16x16.png"));
    }

    static void setIcon(JFrame frame) {
        frame.setIconImage(loadImage("ch/jeda/resources/logo-16x16.png"));
    }

    static DisplayMode findDisplayMode(Size size) {
        GraphicsDevice gd = graphicsDevice();
        DisplayMode result = null;
        int fdx = Integer.MAX_VALUE;
        int fdy = Integer.MAX_VALUE;
        int fcolor = Integer.MIN_VALUE;
        for (DisplayMode candidate : gd.getDisplayModes()) {
            int dx = candidate.getWidth() - size.width;
            int dy = candidate.getHeight() - size.height;
            if (dx >= 0 && dy >= 0 && (dx < fdx || dy < fdy || candidate.getBitDepth() > fcolor)) {
                result = candidate;
                fdx = dx;
                fdy = dy;
                fcolor = result.getBitDepth();
            }
        }

        if (result == null) {
            result = gd.getDisplayMode();
        }

        return result;
    }

    static void finishFullscreenMode() {
        graphicsDevice().setFullScreenWindow(null);
    }

    static void setFullscreenMode(Window window, DisplayMode displayMode) {
        graphicsDevice().setFullScreenWindow(window);
        graphicsDevice().setDisplayMode(displayMode);
    }

    static void setLookAndFeel() {
        try {
            String defaultLaf = System.getProperty("swing.defaultlaf");
            if (defaultLaf != null) {
                UIManager.setLookAndFeel(defaultLaf);
            }
            else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }
        catch (UnsupportedLookAndFeelException e) {
            // TODO: handle exception
        }
        catch (ClassNotFoundException e) {
            // TODO: handle exception
        }
        catch (InstantiationException e) {
            // TODO: handle exception
        }
        catch (IllegalAccessException e) {
            // TODO: handle exception
        }
    }

    private static GraphicsDevice graphicsDevice() {
        return graphicsEnvironment().getDefaultScreenDevice();
    }

    private static GraphicsEnvironment graphicsEnvironment() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment();
    }

    private static ImageIcon loadImageIcon(String path) {
        return new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(path));
    }
}
