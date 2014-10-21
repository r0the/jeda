/*
 * Copyright (C) 2012 - 2014 by Stefan Rothe
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

import ch.jeda.LogLevel;
import ch.jeda.event.SensorType;
import ch.jeda.platform.AudioManagerImp;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.Platform;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowRequest;
import java.awt.Font;
import java.io.InputStream;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

class JavaPlatform implements Platform {

    private final JavaAudioManagerImp audioManager;
    private final WindowManager windowManager;

    public JavaPlatform(final Platform.Callback callback) {
        setLookAndFeel();
        this.audioManager = new JavaAudioManagerImp();
        this.windowManager = new WindowManager(callback);
    }

    @Override
    public CanvasImp createCanvasImp(final int width, final int height) {
        return new JavaCanvasImp(width, height);
    }

    @Override
    public TypefaceImp createTypefaceImp(final String path) {
        return ResourceManager.loadFont(path);
    }

    @Override
    public ImageImp createImageImp(final String path) {
        return ResourceManager.loadImage(path);
    }

    @Override
    public AudioManagerImp getAudioManagerImp() {
        return this.audioManager;
    }

    @Override
    public TypefaceImp getStandardTypefaceImp(final Platform.StandardTypeface standardFont) {
        return new JavaTypefaceImp(new Font(lookupStandardTypeface(standardFont), 20, 0));
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
        this.windowManager.log(logLevel, message);
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
        this.windowManager.showInputRequest(inputRequest);
    }

    @Override
    public void showSelectionRequest(final SelectionRequest selectionRequest) {
        this.windowManager.showSelectionRequest(selectionRequest);
    }

    @Override
    public void showWindow(final WindowRequest windowRequest) {
        this.windowManager.showWindow(windowRequest);
    }

    @Override
    public void shutdown() {
        this.windowManager.shutdown();
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
