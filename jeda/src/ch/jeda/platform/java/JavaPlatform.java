/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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

import ch.jeda.DisplayMetrics;
import ch.jeda.JedaError;
import ch.jeda.LogLevel;
import ch.jeda.event.SensorType;
import ch.jeda.platform.AudioManagerImp;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.Platform;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.ViewRequest;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

class JavaPlatform implements Platform {

    private final JavaAudioManagerImp audioManager;
    private final WindowManager windowManager;

    public JavaPlatform(final Platform.Callback callback) {
        setLookAndFeel();
        audioManager = new JavaAudioManagerImp();
        windowManager = new WindowManager(callback);
    }

    @Override
    public CanvasImp createCanvasImp(final int width, final int height) {
        return new JavaCanvasImp(width, height);
    }

    @Override
    public ImageImp createImageImp(final String path) {
        final BufferedImage image = ResourceManager.loadImage(path);
        if (image == null) {
            return null;
        }
        else {
            return new JavaImageImp(image);
        }
    }

    @Override
    public TypefaceImp createTypefaceImp(final String path) {
        return ResourceManager.loadTypeface(path);
    }

    @Override
    public XMLReader createXmlReader() {
        try {
            return XMLReaderFactory.createXMLReader();
        }
        catch (final SAXException ex) {
            throw new JedaError(JedaError.XML_READER_CREATION_FAILED, ex);
        }
    }

    @Override
    public AudioManagerImp getAudioManagerImp() {
        return audioManager;
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        return new DisplayMetrics(96, (int) size.getWidth(), (int) size.getHeight());
    }

    @Override
    public TypefaceImp getStandardTypefaceImp(final Platform.StandardTypeface standardFont) {
        if (standardFont == StandardTypeface.SANS_SERIF) {
            return ResourceManager.loadTypeface("res:jeda/fonts/roboto_regular.ttf");
        }
        else {
            return new JavaTypefaceImp(new Font(lookupStandardTypeface(standardFont), 20, 0));
        }
    }

    @Override
    public boolean isSensorAvailable(final SensorType sensorType) {
        return false;
    }

    @Override
    public boolean isSensorEnabled(final SensorType sensorType) {
        return false;
    }

    @Override
    public boolean isVirtualKeyboardVisible() {
        return false;
    }

    @Override
    public Class<?>[] loadClasses() throws Exception {
        return ResourceManager.loadClasses();
    }

    @Override
    public void log(final LogLevel logLevel, final String message) {
        windowManager.log(logLevel, message);
    }

    @Override
    public InputStream openResource(final String path) {
        return ResourceManager.openInputStream(path);
    }

    @Override
    public void setSensorEnabled(final SensorType sensorType, final boolean enabled) {
        // ignore
    }

    @Override
    public void setVirtualKeyboardVisible(final boolean visible) {
        // ignore
    }

    @Override
    public void showInputRequest(final InputRequest inputRequest) {
        windowManager.showInputRequest(inputRequest);
    }

    @Override
    public void showSelectionRequest(final SelectionRequest selectionRequest) {
        windowManager.showSelectionRequest(selectionRequest);
    }

    @Override
    public void showViewRequest(final ViewRequest viewRequest) {
        windowManager.showViewRequest(viewRequest);
    }

    @Override
    public void shutdown() {
        windowManager.shutdown();
    }

    private static void setLookAndFeel() {
        try {
            final String defaultLaf = System.getProperty("swing.defaultlaf");
            if (defaultLaf != null) {
                UIManager.setLookAndFeel(defaultLaf);
            }
            else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }
        catch (final UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        catch (final ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (final InstantiationException ex) {
            ex.printStackTrace();
        }
        catch (final IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private static String lookupStandardTypeface(final Platform.StandardTypeface standardTypeface) {
        switch (standardTypeface) {
            case MONOSPACED:
                return Font.MONOSPACED;
            case SANS_SERIF:
                return Font.SANS_SERIF;
            case SERIF:
                return Font.SERIF;
            default:
                return null;
        }
    }
}
