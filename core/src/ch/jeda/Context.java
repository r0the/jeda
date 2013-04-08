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

import static ch.jeda.Log.Level.Debug;
import static ch.jeda.Log.Level.Error;
import static ch.jeda.Log.Level.Info;
import static ch.jeda.Log.Level.Warning;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ContextImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.InputType;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.WindowImp;
import ch.jeda.platform.WindowRequest;
import ch.jeda.ui.Window;
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
 * Represents the application context of the Jeda engine. The context provides
 * low-level access to platform and system resources.
 */
public final class Context {

    private static final String DEFAULT_IMAGE_PATH = ":ch/jeda/resources/logo-64x64.png";
    private static final String JEDA_APPLICATION_PROPERTIES_FILE = ":jeda.properties";
    private static final String JEDA_PLATFORM_PROPERTIES_FILE = ":ch/jeda/platform/jeda.properties";
    private static final String JEDA_SYSTEM_PROPERTIES_FILE = ":ch/jeda/jeda.properties";
    private static final String RESOURCE_PREFIX = ":";
    private final ContextImp imp;
    private ImageImp defaultImage;
    private Properties properties;
    private Log.Level logLevel = Log.Level.Warning;

    public CanvasImp createCanvasImp(Size size) {
        return this.imp.createCanvasImp(size);
    }

    public Properties getProperties() {
        return this.properties;
    }

    public ImageImp loadImageImp(String filePath) {
        ImageImp result = null;
        try {
            final InputStream in = this.openInputStream(filePath);
            if (in != null) {
                result = this.imp.loadImageImp(in);
            }
        }
        catch (Exception ex) {
            Log.warning(Message.IMAGE_READ_ERROR, filePath, ex);
        }

        if (result == null) {
            result = this.defaultImage;
        }

        return result;
    }

    public WindowImp showWindow(Size size, EnumSet<Window.Feature> features) {
        if (features == null) {
            throw new NullPointerException("features");
        }

        final WindowRequest request = new WindowRequest(size, features);
        this.imp.showWindow(request);
        request.waitForResult();
        return request.getResult();
    }

    public void setLogLevel(Log.Level value) {
        this.logLevel = value;
    }

    Context(ContextImp imp) {
        this.imp = imp;
        this.properties = new Properties();
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

    Transformation createTransformation() {
        return this.imp.createTransformation();
    }

    Iterable<String> listClassNames() throws Exception {
        return this.imp.listClassNames();
    }

    List<String> loadTextFile(String filePath) {
        try {
            final InputStream in = this.openInputStream(filePath);
            if (in == null) {
                Log.warning(Message.FILE_NOT_FOUND_ERROR, filePath);
                return null;
            }

            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            final List<String> result = new ArrayList();
            while (reader.ready()) {
                result.add(reader.readLine());
            }

            return result;
        }
        catch (IOException ex) {
            Log.warning(Message.FILE_READ_ERROR, filePath, ex);
            return null;
        }
    }

    void log(Log.Level level, String message, Throwable exception) {
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

    InputStream openInputStream(String filePath) throws IOException {
        if (filePath == null) {
            throw new NullPointerException("filePath");
        }

        if (filePath.startsWith(RESOURCE_PREFIX)) {
            final URL url = Thread.currentThread().getContextClassLoader().
                    getResource(filePath.substring(RESOURCE_PREFIX.length()));
            if (url == null) {
                Log.warning(Message.FILE_NOT_FOUND_ERROR, filePath);
                return null;
            }
            else {
                return url.openStream();
            }
        }
        else {
            try {
                return new FileInputStream(filePath);
            }
            catch (FileNotFoundException ex) {
                Log.warning(Message.FILE_NOT_FOUND_ERROR, filePath);
                return null;
            }
        }
    }

    double readDouble(String message) {
        InputRequest<Double> request = new InputRequest(InputType.forDouble(), 0d);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        this.imp.showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    int readInt(String message) {
        InputRequest<Integer> request = new InputRequest(InputType.forInt(), 0);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        this.imp.showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    String readString(String message) {
        InputRequest<String> request = new InputRequest(InputType.forString(), "");
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        this.imp.showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    void showSelectionRequest(SelectionRequest selectionRequest) {
        this.imp.showSelectionRequest(selectionRequest);
    }

    void shutdown() {
        this.imp.shutdown();
    }

    void write(String message, Object... args) {
        this.imp.log(Util.args(message, args));
        this.imp.showLog();
    }

    private boolean matchesLogLevel(Log.Level messageLevel) {
        switch (this.logLevel) {
            case Debug:
                return true;
            case Info:
                return messageLevel == Log.Level.Info ||
                       messageLevel == Log.Level.Warning ||
                       messageLevel == Log.Level.Error;
            case Warning:
                return messageLevel == Log.Level.Warning ||
                       messageLevel == Log.Level.Error;
            case Error:
                return messageLevel == Log.Level.Error;
            default:
                return false;
        }
    }
}
