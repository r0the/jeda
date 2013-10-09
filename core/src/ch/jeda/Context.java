/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda;

import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ContextImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowImp;
import ch.jeda.platform.WindowRequest;
import ch.jeda.ui.WindowFeature;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * <b>Internal</b>. Do not use this class.
 * <p>
 * Represents the application context of the Jeda engine. The context provides low-level access to platform and system
 * resources.
 */
public final class Context {

    private static final String DEFAULT_IMAGE_PATH = ":ch/jeda/resources/logo-64x64.png";
    private static final String DEFAULT_PROGRAM_PROPERTY = "jeda.default.program";
    private static final String JEDA_APPLICATION_PROPERTIES_FILE = ":jeda.properties";
    private static final String JEDA_PLATFORM_PROPERTIES_FILE = ":ch/jeda/platform/jeda.properties";
    private static final String JEDA_SYSTEM_PROPERTIES_FILE = ":ch/jeda/jeda.properties";
    private final ContextImp imp;
    private ImageImp defaultImage;
    private Properties properties;
    private LogLevel logLevel = LogLevel.WARNING;

    public CanvasImp createCanvasImp(final int width, final int height) {
        return this.imp.createCanvasImp(width, height);
    }

    public Properties getProperties() {
        return this.properties;
    }

    public ImageImp loadImageImp(final String filePath) {
        final InputStream in = IO.openInputStream(filePath);
        if (in == null) {
            return this.defaultImage;
        }

        try {
            return this.imp.loadImageImp(in);
        }
        catch (final Exception ex) {
            IO.err(ex, "jeda.image.error.read", filePath);
            return this.defaultImage;
        }
        finally {
            try {
                in.close();
            }
            catch (final IOException ex) {
                // ignore
            }
        }
    }

    public WindowImp showWindow(final int width, final int height, final EnumSet<WindowFeature> features) {
        if (features == null) {
            throw new NullPointerException("features");
        }

        final WindowRequest request = new WindowRequest(width, height, features);
        this.imp.showWindow(request);
        request.waitForResult();
        return request.getResult();
    }

    public void setLogLevel(final LogLevel value) {
        this.logLevel = value;
    }

    Context(final ContextImp imp) {
        this.imp = imp;
        this.properties = new Properties();
    }

    String defaultProgramName() {
        String result = this.imp.defaultProgramName();
        if (result == null || result.isEmpty()) {
            result = this.properties.getString(DEFAULT_PROGRAM_PROPERTY);
        }

        return result;
    }

    void init() {
        // This class requires a two-step initialization. Here, we already need
        // a working environment to handle and log possible errors.
        if (this.defaultImage == null) {
            this.defaultImage = this.loadImageImp(DEFAULT_IMAGE_PATH);
        }

        this.properties.clear();
        this.properties.addAll(new Properties(JEDA_APPLICATION_PROPERTIES_FILE));
        this.properties.addAll(new Properties(JEDA_PLATFORM_PROPERTIES_FILE));
        this.properties.addAll(new Properties(JEDA_SYSTEM_PROPERTIES_FILE));
        this.properties.loadFromSystem();
    }

    Class<?>[] loadClasses() throws Exception {
        return this.imp.loadClasses();
    }

    void log(final LogLevel level, final String message, final Throwable exception) {
        if (this.matchesLogLevel(level)) {
            this.imp.log(level.toString() + ": " + message + '\n');
            if (exception != null) {
                this.imp.log(exception.toString() + '\n');
                for (StackTraceElement el : exception.getStackTrace()) {
                    this.imp.log("   " + el.toString() + '\n');
                }
            }
        }
    }

    void showInputRequest(final InputRequest inputRequest) {
        this.imp.showInputRequest(inputRequest);
    }

    void showSelectionRequest(final SelectionRequest selectionRequest) {
        this.imp.showSelectionRequest(selectionRequest);
    }

    void shutdown() {
        this.imp.shutdown();
    }

    void write(final String message) {
        this.imp.log(message);
        this.imp.showLog();
    }

    private boolean matchesLogLevel(final LogLevel messageLevel) {
        switch (this.logLevel) {
            case DEBUG:
                return true;
            case INFO:
                return messageLevel == LogLevel.INFO ||
                       messageLevel == LogLevel.WARNING ||
                       messageLevel == LogLevel.ERROR;
            case WARNING:
                return messageLevel == LogLevel.WARNING ||
                       messageLevel == LogLevel.ERROR;
            case ERROR:
                return messageLevel == LogLevel.ERROR;
            default:
                return false;
        }
    }
}
