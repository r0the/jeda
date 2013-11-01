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
package ch.jeda;

import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.Platform;
import ch.jeda.platform.PlatformCallback;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.SoundImp;
import ch.jeda.ui.TickEvent;
import ch.jeda.ui.TickListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

class JedaEngine implements PlatformCallback, Runnable {

    private static final String DEFAULT_IMAGE_PATH = "res:jeda/logo-64x64.png";
    private static final float DEFAULT_TICK_FREQUENCY = 60f;
    private static final String JEDA_APPLICATION_PROPERTIES_FILE = "res/jeda.properties";
    private static final String JEDA_PLATFORM_PROPERTIES_FILE = "res/jeda/platform.properties";
    private static final String JEDA_SYSTEM_PROPERTIES_FILE = "res/jeda/system.properties";
    private final Object currentProgramLock;
    private final ImageImp defaultImage;
    private final FrequencyMeter frequencyMeter;
    private final Platform platform;
    private final Plugins plugins;
    private final ProgramClassWrapper[] programClasses;
    private final Properties properties;
    private final AtomicBoolean running;
    private final List<TickListener> tickListeners;
    private final Timer timer;
    private JedaProgramExecutor currentProgram;

    static JedaEngine create() {
        final JedaEngine result = new JedaEngine();
        final Thread eventThread = new Thread(result);
        eventThread.setName(Helper.getMessage("jeda.engine.event-thread-name"));
        eventThread.start();
        return result;
    }

    JedaEngine() {
        this.currentProgramLock = new Object();
        this.frequencyMeter = new FrequencyMeter();
        this.plugins = new Plugins();
        this.running = new AtomicBoolean(true);
        this.tickListeners = new CopyOnWriteArrayList<TickListener>();
        this.timer = new Timer(DEFAULT_TICK_FREQUENCY);
        // Load properties
        this.properties = initProperties();
        // Init platform
        this.platform = initPlatform(this.properties.getString("jeda.platform.class"), this);
        // Load default image
        this.defaultImage = this.platform.createImageImp(DEFAULT_IMAGE_PATH);
        // Find Jeda programs and plugins
        final List<ProgramClassWrapper> programClassList = new ArrayList<ProgramClassWrapper>();
        try {
            final Class[] classes = this.platform.loadClasses();
            // Load jeda plugins and jeda programs
            for (int i = 0; i < classes.length; ++i) {
                final ProgramClassWrapper pcw = ProgramClassWrapper.tryCreate(classes[i], this.properties);
                if (pcw != null) {
                    programClassList.add(pcw);
                }
                else {
                    this.plugins.tryAddPlugin(classes[i]);
                }
            }
        }
        catch (final Exception ex) {
            Log.err(ex, "jeda.engine.error.init-classes");
        }

        this.programClasses = programClassList.toArray(new ProgramClassWrapper[programClassList.size()]);
        this.plugins.initialize();
    }

    @Override
    public void addTickListener(final TickListener listener) {
        if (listener != null && !this.tickListeners.contains(listener)) {
            this.tickListeners.add(listener);
        }
    }

    @Override
    public void run() {
        this.timer.start();
        while (this.running.get()) {
            this.frequencyMeter.count();
            final TickEvent event = new TickEvent(this, this.timer.getLastStepDuration(), this.frequencyMeter.getFrequency());
            for (int i = 0; i < this.tickListeners.size(); ++i) {
                try {
                    this.tickListeners.get(i).onTick(event);
                }
                catch (final Throwable ex) {
                    Log.err(ex, "java.event.error");
                }
            }

            this.timer.tick();
        }
    }

    @Override
    public void stop() {
        synchronized (this.currentProgramLock) {
            if (this.currentProgram != null) {
                this.currentProgram.stop();
            }
            else {
                this.running.set(false);
                this.platform.shutdown();
            }
        }
    }

    public CanvasImp createCanvasImp(final int width, final int height) {
        return this.platform.createCanvasImp(width, height);
    }

    public ImageImp createImageImp(final String path) {
        final ImageImp result = this.platform.createImageImp(path);
        if (result == null) {
            return this.defaultImage;
        }
        else {
            return result;
        }
    }

    public SoundImp createSoundImp(final String path) {
        return this.platform.createSoundImp(path);
    }

    float getTickFrequency() {
        return this.timer.getTargetFrequency();
    }

    @Deprecated
    Platform getPlatform() {
        return this.platform;
    }

    ProgramClassWrapper[] getProgramClasses() {
        return this.programClasses;
    }

    Properties getProperties() {
        return this.properties;
    }

    void programTerminated() {
        synchronized (this.currentProgramLock) {
            this.currentProgram = null;
        }
    }

    void removeTickListener(final TickListener listener) {
        if (listener != null) {
            this.tickListeners.remove(listener);
        }
    }

    void showSelectionRequest(final SelectionRequest request) {
        this.platform.showSelectionRequest(request);
    }

    void setTickFrequency(final float hertz) {
        this.timer.setTargetFrequency(hertz);
    }

    void startProgram(final String programClassName) {
        synchronized (this.currentProgramLock) {
            if (this.currentProgram != null) {
                Log.err("jeda.engine.error.program-already-running");
            }
            else {
                this.currentProgram = new JedaProgramExecutor(this, programClassName);
                final Thread programThread = new Thread(this.currentProgram);
                programThread.setName(this.currentProgram.getProgramName());
                programThread.start();
            }
        }
    }

    private static Platform initPlatform(final String platformClassName, final PlatformCallback callback) {
        if (platformClassName == null || platformClassName.isEmpty()) {
            Log.err("jeda.engine.error.platform-missing-class-name");
            return null;
        }

        try {
            final Class<?> clazz = JedaEngine.class.getClassLoader().loadClass(platformClassName);
            final Constructor<?> ctor = clazz.getConstructor(PlatformCallback.class);
            ctor.setAccessible(true);
            final Object result = ctor.newInstance(callback);
            if (result instanceof Platform) {
                return (Platform) ctor.newInstance(callback);
            }
            else {
                Log.err("jeda.engine.error.platform-missing-interface", platformClassName, Platform.class);
                return null;
            }
        }
        catch (final ClassNotFoundException ex) {
            Log.err(ex, "jeda.engine.error.platform-class-not-found", platformClassName);
            return null;
        }
        catch (final NoSuchMethodException ex) {
            Log.err(ex, "jeda.engine.error.platform-missing-constructor", platformClassName);
            return null;
        }
        catch (final InstantiationException ex) {
            Log.err(ex.getCause(), "jeda.engine.error.platform-instantiation-exception", platformClassName);
            return null;
        }
        catch (final IllegalAccessException ex) {
            Log.err(ex.getCause(), "jeda.engine.error.platform-constructor-access", platformClassName);
            return null;
        }
        catch (final InvocationTargetException ex) {
            Log.err(ex.getCause(), "jeda.engine.error.platform-exception-in-constructor", platformClassName);
            return null;
        }
    }

    private static Properties initProperties() {
        java.util.Properties result = new java.util.Properties();
        loadProperties(result, JEDA_SYSTEM_PROPERTIES_FILE);
        loadProperties(result, JEDA_PLATFORM_PROPERTIES_FILE);
        loadProperties(result, JEDA_APPLICATION_PROPERTIES_FILE);
        result.putAll(System.getProperties());
        return new Properties(result);
    }

    private static void loadProperties(final java.util.Properties properties, final String path) {
        final URL url = JedaEngine.class.getClassLoader().getResource(path);
        if (url == null) {
            Log.err("jeda.engine.error.properties-not-found", path);
            return;
        }

        InputStream in = null;
        try {
            in = url.openStream();
            properties.load(in);
        }
        catch (final Exception ex) {
            Log.err("jeda.engine.error.properties-read", path);
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
}
