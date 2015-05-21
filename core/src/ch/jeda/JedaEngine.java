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
package ch.jeda;

import ch.jeda.event.Event;
import ch.jeda.event.EventQueue;
import ch.jeda.event.SensorType;
import ch.jeda.event.TickEvent;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.ViewCallback;
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewRequest;
import ch.jeda.ui.ViewFeature;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.xml.sax.XMLReader;

class JedaEngine implements Platform.Callback, Runnable {

    private static final TypefaceImp EMPTY_TYPEFACE_IMP = new EmptyTypefaceImp();
    private static final ImageImp EMPTY_IMAGE_IMP = new EmptyImageImp();
    private static final String DEFAULT_IMAGE_PATH = "res:jeda/logo-64x64.png";
    private static final double DEFAULT_TICK_FREQUENCY = 30.0;
    private static final String JEDA_APPLICATION_PROPERTIES_FILE = "res/jeda.properties";
    private static final String JEDA_PLATFORM_PROPERTIES_FILE = "res/jeda/platform.properties";
    private static final String JEDA_SYSTEM_PROPERTIES_FILE = "res/jeda/system.properties";
    private final AudioManager audioManager;
    private final Object currentProgramLock;
    private final ImageImp defaultImageImp;
    private final EventQueue eventQueue;
    private final FrequencyMeter frequencyMeter;
    private final Object pauseLock;
    private final Platform platform;
    private final ProgramClassWrapper[] programClasses;
    private final Properties properties;
    private final Timer timer;
    private JedaProgramExecutor currentProgram;
    private boolean paused;

    static JedaEngine create() {
        final JedaEngine result = new JedaEngine();
        final Thread eventThread = new Thread(result);
        eventThread.setName(Message.get(Message.ENGINE_EVENT_THREAD_NAME));
        eventThread.setDaemon(true);
        eventThread.start();
        return result;
    }

    JedaEngine() {
        currentProgramLock = new Object();
        eventQueue = new EventQueue();
        frequencyMeter = new FrequencyMeter();
        pauseLock = new Object();
        timer = new Timer(DEFAULT_TICK_FREQUENCY);
        // Load properties
        properties = initProperties();
        // Init platform
        platform = initPlatform(properties.getString("jeda.platform.class"), this);
        // Init audio manager
        audioManager = new AudioManager(platform.getAudioManagerImp());
        // Load default image
        defaultImageImp = platform.createImageImp(DEFAULT_IMAGE_PATH);
        // Find Jeda programs and plugins
        final List<ProgramClassWrapper> programClassList = new ArrayList<ProgramClassWrapper>();
        try {
            final Class[] classes = platform.loadClasses();
            // Load jeda plugins and jeda programs
            for (int i = 0; i < classes.length; ++i) {
                final ProgramClassWrapper pcw = ProgramClassWrapper.tryCreate(classes[i], properties);
                if (pcw != null) {
                    programClassList.add(pcw);
                }
            }
        }
        catch (final Exception ex) {
            Log.err(ex, Message.ENGINE_ERROR_INIT_CLASSES);
        }

        programClasses = programClassList.toArray(new ProgramClassWrapper[programClassList.size()]);
        paused = false;
    }

    @Override
    public void pause() {
        synchronized (pauseLock) {
            paused = true;
        }
    }

    @Override
    public void postEvent(final Event event) {
        eventQueue.addEvent(event);
    }

    @Override
    public void resume() {
        synchronized (pauseLock) {
            if (paused) {
                paused = false;
                timer.start();
            }
        }
    }

    @Override
    public void run() {
        timer.start();
        while (true) {
            // Application is paused
            if (isPaused()) {
                try {
                    Thread.sleep(100);
                }
                catch (final InterruptedException ex) {
                    // ignore
                }
            }
            // Application is running
            else {
                frequencyMeter.count();
                final TickEvent event = new TickEvent(this, timer.getLastStepDuration(), frequencyMeter.getFrequency());
                eventQueue.addEvent(event);
                eventQueue.processEvents();
                timer.tick();
            }
        }
    }

    @Override
    public void stop() {
        synchronized (currentProgramLock) {
            if (currentProgram != null) {
                currentProgram.stop();
            }
            else {
                platform.shutdown();
            }
        }
    }

    void addEventListener(final Object listener) {
        eventQueue.addListener(listener);
    }

    CanvasImp createCanvasImp(final int width, final int height) {
        return platform.createCanvasImp(width, height);
    }

    ImageImp createImageImp(final String path) {
        if (path == null) {
            return defaultImageImp;
        }

        final ImageImp result = platform.createImageImp(path);
        if (result == null) {
            return defaultImageImp;
        }
        else {
            return result;
        }
    }

    TypefaceImp createTypefaceImp(final String path) {
        if (path == null) {
            return EMPTY_TYPEFACE_IMP;
        }

        final TypefaceImp result = platform.createTypefaceImp(path);
        if (result == null) {
            return EMPTY_TYPEFACE_IMP;
        }
        else {
            return result;
        }
    }

    ViewImp createViewImp(final ViewCallback callback, final int width, final int height,
                          final EnumSet<ViewFeature> features) {
        if (features == null) {
            throw new NullPointerException("features");
        }

        final ViewRequest request = new ViewRequest(callback, width, height, features);
        platform.showViewRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    XMLReader createXmlReader() {
        return platform.createXmlReader();
    }

    AudioManager getAudioManager() {
        return audioManager;
    }

    String getProgramName() {
        synchronized (currentProgramLock) {
            if (currentProgram != null) {
                return currentProgram.getProgramName();
            }
            else {
                return null;
            }
        }
    }

    ProgramClassWrapper[] getProgramClasses() {
        return programClasses;
    }

    Properties getProperties() {
        return properties;
    }

    TypefaceImp getStandardTypefaceImp(final Platform.StandardTypeface standardTypeface) {
        final TypefaceImp result = platform.getStandardTypefaceImp(standardTypeface);
        if (result == null) {
            return EMPTY_TYPEFACE_IMP;
        }
        else {
            return result;
        }
    }

    double getTickFrequency() {
        return timer.getTargetFrequency();
    }

    boolean isSensorAvailable(final SensorType sensorType) {
        return platform.isSensorAvailable(sensorType);
    }

    boolean isSensorEnabled(final SensorType sensorType) {
        return platform.isSensorEnabled(sensorType);
    }

    boolean isVirtualKeyboardVisible() {
        return platform.isVirtualKeyboardVisible();
    }

    void log(final LogLevel logLevel, final String message) {
        if (platform == null) {
            System.err.print(message);
        }
        else {
            platform.log(logLevel, message);
        }
    }

    InputStream openResource(final String path) {
        return platform.openResource(path);
    }

    void programTerminated() {
        synchronized (currentProgramLock) {
            currentProgram = null;
            platform.shutdown();
        }
    }

    void removeEventListener(final Object listener) {
        eventQueue.removeListener(listener);
    }

    void showInputRequest(final InputRequest request) {
        platform.showInputRequest(request);
    }

    void showSelectionRequest(final SelectionRequest request) {
        platform.showSelectionRequest(request);
    }

    void setSensorEnabled(final SensorType sensorType, final boolean enabled) {
        platform.setSensorEnabled(sensorType, enabled);
    }

    void setTickFrequency(final double hertz) {
        timer.setTargetFrequency(hertz);
    }

    void setVirtualKeyboardVisible(final boolean visible) {
        platform.setVirtualKeyboardVisible(visible);
    }

    void startProgram(final String programClassName) {
        synchronized (currentProgramLock) {
            if (currentProgram != null) {
                Log.err(Message.PROGRAM_ERROR_ALREADY_RUNNING);
            }
            else {
                currentProgram = new JedaProgramExecutor(this, programClassName);
                final Thread programThread = new Thread(currentProgram);
                programThread.setName(Message.get(Message.ENGINE_PROGRAM_THREAD_NAME));
                programThread.start();
            }
        }
    }

    private boolean isPaused() {
        synchronized (pauseLock) {
            return paused;
        }
    }

    private static Platform initPlatform(final String platformClassName, final Platform.Callback callback) {
        if (platformClassName == null || platformClassName.isEmpty()) {
            initErr(Message.ENGINE_ERROR_PLATFORM_MISSING_CLASS_NAME);
            return null;
        }

        try {
            final Class<?> clazz = JedaEngine.class.getClassLoader().loadClass(platformClassName);
            final Constructor<?> ctor = clazz.getConstructor(Platform.Callback.class);
            ctor.setAccessible(true);
            final Object result = ctor.newInstance(callback);
            if (result instanceof Platform) {
                return (Platform) ctor.newInstance(callback);
            }
            else {
                initErr(Message.ENGINE_ERROR_PLATFORM_MISSING_INTERFACE, platformClassName, Platform.class);
                return null;
            }
        }
        catch (final ClassNotFoundException ex) {
            initErr(ex, Message.ENGINE_ERROR_PLATFORM_CLASS_NOT_FOUND, platformClassName);
            return null;
        }
        catch (final NoSuchMethodException ex) {
            initErr(ex, Message.ENGINE_ERROR_PLATFORM_CONSTRUCTOR_NOT_FOUND, platformClassName);
            return null;
        }
        catch (final InstantiationException ex) {
            initErr(ex.getCause(), Message.ENGINE_ERROR_PLATFORM_INSTANTIATION, platformClassName);
            return null;
        }
        catch (final IllegalAccessException ex) {
            initErr(ex.getCause(), Message.ENGINE_ERROR_PLATFORM_ACCESS, platformClassName);
            return null;
        }
        catch (final InvocationTargetException ex) {
            initErr(ex.getCause(), Message.ENGINE_ERROR_PLATFORM_CONSTRUCTOR, platformClassName);
            return null;
        }
    }

    private static Properties initProperties() {
        final java.util.Properties result = new java.util.Properties();
        loadProperties(result, JEDA_SYSTEM_PROPERTIES_FILE);
        loadProperties(result, JEDA_PLATFORM_PROPERTIES_FILE);
        loadProperties(result, JEDA_APPLICATION_PROPERTIES_FILE);
        result.putAll(System.getProperties());
        return new Properties(result);
    }

    private static void loadProperties(final java.util.Properties properties, final String path) {
        final URL url = JedaEngine.class.getClassLoader().getResource(path);
        if (url == null) {
            initErr(Message.ENGINE_ERROR_PROPERTIES_NOT_FOUND, path);
            return;
        }

        InputStream in = null;
        try {
            in = url.openStream();
            properties.load(in);
        }
        catch (final Exception ex) {
            initErr(Message.ENGINE_ERROR_PROPERTIES_READ, path);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (final IOException ex) {
                    // ignore
                }
            }
        }
    }

    private static void initErr(final String messageKey, Object... args) {
        System.err.format(Message.get(messageKey), args);
        System.err.println();
    }

    private static void initErr(final Throwable throwable, final String messageKey, Object... args) {
        System.err.format(Message.get(messageKey), args);
        System.err.println();
        if (throwable != null) {
            System.err.println("  " + throwable);
            final StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (int i = 0; i < stackTrace.length; ++i) {
                System.err.println("   " + stackTrace[i].toString());
            }
        }
    }

    private static class EmptyTypefaceImp implements TypefaceImp {

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public String getName() {
            return "";
        }
    }

    private static class EmptyImageImp implements ImageImp {

        @Override
        public ImageImp flipHorizontally() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ImageImp flipVertically() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getPixel(int x, int y) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int[] getPixels(int x, int y, int width, int height) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public ImageImp rotateRad(double angle) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ImageImp scale(int width, int height) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ImageImp subImage(int x, int y, int width, int height) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean write(OutputStream out, Encoding encoding) throws IOException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
