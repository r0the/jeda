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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI {

    public static void center(JDialog dialog) {
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        dialog.setLocation(center.x - dialog.getWidth() / 2, center.y - dialog.getHeight() / 2);
    }

    public static void center(JFrame frame) {
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        frame.setLocation(center.x - frame.getWidth() / 2, center.y - frame.getHeight() / 2);
    }

    public static Icon loadIcon(String path) {
        return loadImageIcon(path);
    }

    public static Image loadImage(String path) {
        return loadImageIcon(path).getImage();
    }

    public static void setIcon(JDialog dialog) {
        dialog.setIconImage(loadImage("ch/jeda/resources/logo-16x16.png"));
    }

    public static void setIcon(JFrame frame) {
        frame.setIconImage(loadImage("ch/jeda/resources/logo-16x16.png"));
    }

    public static void setLookAndFeel() {
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

    private static ImageIcon loadImageIcon(String path) {
        return new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(path));
    }
}
