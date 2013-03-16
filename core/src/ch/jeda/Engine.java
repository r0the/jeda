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
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.InputType;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.WindowImp;
import ch.jeda.platform.WindowRequest;
import ch.jeda.ui.Window;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * <b>Internal</b>. Do not use this class.<p>
 */
public final class Engine {

    private static final ThreadLocal<Engine> CURRENT_ENGINE = new ThreadLocal();
    private static final String DEFAULT_PROGRAM_PROPERTY = "jeda.default.program";
    private static final String JEDA_APPLICATION_PROPERTIES_FILE = "jeda.properties";
    private static final String JEDA_PLATFORM_PROPERTIES_FILE = "ch/jeda/platform/jeda.properties";
    private static final String JEDA_SYSTEM_PROPERTIES_FILE = "ch/jeda/jeda.properties";
    private static final Class<?>[] NO_PARAMS = new Class<?>[0];
    private final FileSystem fileSystem;
    private final EngineThread thread;
    private Log.Level logLevel;
    private final Platform platform;
    private final Properties properties;
    private Program program;

    public static Engine getCurrentEngine() {
        return CURRENT_ENGINE.get();
    }

    public Engine(Platform platform) {
        if (platform == null) {
            throw new NullPointerException("platform");
        }

        this.fileSystem = new FileSystem(platform);
        this.thread = new EngineThread(this);

        this.logLevel = Log.Level.Warning;
        this.platform = platform;
        this.properties = new Properties();
    }

    public CanvasImp createCanvasImp(Size size) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        return this.platform.createCanvasImp(size);
    }

    public Properties getProperties() {
        return this.properties;
    }

    public ImageImp loadImageImp(String filePath) {
        return this.fileSystem.loadImageImp(filePath);
    }

    public void requestStop() {
        if (this.program != null) {
            this.program.requestStop();
        }
    }

    public void setLogLevel(Log.Level value) {
        this.logLevel = value;
    }

    public WindowImp showWindow(Size size, EnumSet<Window.Feature> features) {
        if (features == null) {
            throw new NullPointerException("features");
        }

        WindowRequest request = new WindowRequest(size, features);
        this.platform.showWindow(request);
        request.waitForResult();
        return request.getResult();
    }

    public void start() {
        if (this.thread.isAlive()) {
            throw new IllegalStateException("Engine has already been started.");
        }

        this.thread.start();
    }

    List<String> loadTextFile(String filePath) {
        return this.fileSystem.loadTextFile(filePath);
    }

    void log(Log.Level level, String message, Throwable exception) {
        if (this.matchesLogLevel(level)) {
            this.platform.log(level.toString() + ": " + message + '\n');
            if (exception != null) {
                this.platform.log(exception.toString() + '\n');
                for (StackTraceElement el : exception.getStackTrace()) {
                    this.platform.log("   " + el.toString() + '\n');
                }
            }
        }
    }

    double readDouble(String message) {
        InputRequest<Double> request = new InputRequest(InputType.forDouble(), 0d);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        this.platform.showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    int readInt(String message) {
        InputRequest<Integer> request = new InputRequest(InputType.forInt(), 0);
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        this.platform.showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    String readString(String message) {
        InputRequest<String> request = new InputRequest(InputType.forString(), "");
        request.setMessage(message);
        request.setTitle(Message.translate(Message.INPUT_REQUEST_TITLE));
        this.platform.showInputRequest(request);
        request.waitForResult();
        return request.getResult();
    }

    void write(String message, Object... args) {
        this.platform.log(Util.args(message, args));
        this.platform.showLog();
    }

    private Program createProgram(Class<Program> programClass) {
        try {
            Constructor<? extends Program> ctor = programClass.getDeclaredConstructor(new Class[0]);
            return ctor.newInstance(new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            this.fatalError(ex, Message.PROGRAM_CREATE_ERROR, programClass);
        }
        catch (IllegalAccessException ex) {
            this.fatalError(ex, Message.PROGRAM_CREATE_ERROR, programClass);
        }
        catch (InvocationTargetException ex) {
            this.fatalError(ex.getCause(), Message.PROGRAM_CREATE_ERROR, programClass);
        }
        catch (InstantiationException ex) {
            this.fatalError(ex, Message.PROGRAM_CREATE_ERROR, programClass);
        }
        catch (NoClassDefFoundError ex) {
            this.fatalError(ex, Message.PROGRAM_CREATE_ERROR, programClass);
        }
        return null;
    }

    private ProgramInfo createProgramInfo(Class<Program> programClass) {
        String title = this.properties.getString(programClass.getName());
        if (title == null) {
            title = programClass.getSimpleName();
        }

        return new ProgramInfo(programClass, title);
    }

    private void fatalError(String messageKey, Object... args) {
        this.fatalError(null, messageKey, args);
    }

    private void fatalError(Throwable exception, String messageKey, Object... args) {
        this.log(Log.Level.Error, Util.args(Message.translate(messageKey), args), exception);
        this.platform.showLog();
        this.platform.stop();
    }

    private boolean matchesLogLevel(Log.Level messageLevel) {
        switch (this.logLevel) {
            case Debug:
                return true;
            case Info:
                return messageLevel == Log.Level.Info || messageLevel == Log.Level.Warning || messageLevel == Log.Level.Error;
            case Warning:
                return messageLevel == Log.Level.Warning || messageLevel == Log.Level.Error;
            case Error:
                return messageLevel == Log.Level.Error;
            default:
                return false;
        }
    }

    private void startProgram() {
        try {
            // Load all program classes
            List<ProgramInfo> programInfos = new ArrayList();
            ProgramInfo defaultProgram = null;
            String defaultProgramName = this.properties.getString(DEFAULT_PROGRAM_PROPERTY);
            for (String className : this.platform.listClassNames()) {
                Class cls = loadClass(className);
                if (isProgramClass(cls)) {
                    ProgramInfo pi = this.createProgramInfo(cls);
                    programInfos.add(pi);
                    if (cls.getName().equals(defaultProgramName)) {
                        defaultProgram = pi;
                    }
                }
            }

            if (defaultProgram != null) {
                this.startProgram(defaultProgram);
            }
            else if (programInfos.size() == 1) {
                this.startProgram(programInfos.get(0));
            }
            else if (programInfos.isEmpty()) {
                this.fatalError(Message.NO_PROGRAM_ERROR);
            }
            else {
                SelectionRequest<ProgramInfo> request = new SelectionRequest();
                for (ProgramInfo programInfo : programInfos) {
                    request.addItem(programInfo.getName(), programInfo);
                }

                request.sortItemsByName();
                request.setTitle(Message.translate(Message.CHOOSE_PROGRAM_TITLE));
                this.platform.showSelectionRequest(request);
                request.waitForResult();
                if (request.isCancelled()) {
                    this.platform.stop();
                }
                else {
                    this.startProgram(request.getResult());
                }
            }
        }
        catch (Exception ex) {
            this.fatalError(ex, Message.CHOOSE_PROGRAM_ERROR);
        }
    }

    private void startProgram(ProgramInfo programInfo) {
        this.program = this.createProgram(programInfo.getProgramClass());
        if (this.program != null) {
            ProgramThread programThread = new ProgramThread(this, this.program);
            programThread.setName(programInfo.getName());
            programThread.start();
        }
    }

    private static boolean isProgramClass(Class cls) {
        if (!Program.class.isAssignableFrom(cls)) {
            return false;
        }

        if (Modifier.isAbstract(cls.getModifiers())) {
            return false;
        }

        try {
            int ctorModifiers = cls.getConstructor(NO_PARAMS).getModifiers();
            return Modifier.isPublic(ctorModifiers);
        }
        catch (NoSuchMethodException ex) {
            return false;
        }
    }

    private static Class loadClass(String className) throws Exception {
        try {
            return Class.forName(className);
        }
        catch (NoClassDefFoundError ex) {
            return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        }
        catch (ExceptionInInitializerError ex) {
            return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        }
    }

    private static class EngineThread extends Thread {

        private final Engine engine;

        EngineThread(Engine engine) {
            this.engine = engine;
            this.setName(Message.translate(Message.ENGINE_THREAD_NAME));
        }

        @Override
        public void run() {
            CURRENT_ENGINE.set(this.engine);
            this.engine.fileSystem.init();
            this.loadProperties();
            this.engine.startProgram();
        }

        private void loadProperties() {
            try {
                // Load application properties first to prevent them to
                // overwrite system properties.
                this.engine.properties.loadFromResource(JEDA_APPLICATION_PROPERTIES_FILE);
                this.engine.properties.loadFromResource(JEDA_PLATFORM_PROPERTIES_FILE);
                this.engine.properties.loadFromResource(JEDA_SYSTEM_PROPERTIES_FILE);
                this.engine.properties.loadFromSystem();
            }
            catch (Exception ex) {
                this.engine.fatalError(ex, Message.LOAD_PROPERTIES_ERROR);
            }
        }
    }

    private static class ProgramThread extends Thread {

        private final Engine engine;
        private final Program program;

        public ProgramThread(Engine engine, Program program) {
            this.engine = engine;
            this.program = program;
        }

        @Override
        public void run() {
            CURRENT_ENGINE.set(this.engine);

            try {
                this.program.run();
            }
            catch (Exception ex) {
                this.engine.fatalError(ex, Message.PROGRAM_RUN_ERROR, this.program.getClass());
            }
            finally {
                this.engine.platform.stop();
            }
        }
    }
}
