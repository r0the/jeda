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
package ch.jeda;

import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.InputType;
import ch.jeda.platform.ListInfo;
import ch.jeda.platform.LogInfo;
import ch.jeda.platform.Platform;
import ch.jeda.platform.ViewImp;
import ch.jeda.platform.ViewInfo;
import ch.jeda.ui.Window;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public final class Engine {

    private enum State {

        Created, Started
    }
    private static final ThreadLocal<Engine> CURRENT_ENGINE = new ThreadLocal();
    private static final String DEFAULT_PROGRAM_PROPERTY = "jeda.default.program";
    private static final String JEDA_PROPERTIES_FILE = "ch/jeda/resources/jeda.properties";
    private static final Class<?>[] NO_PARAMS = new Class<?>[0];
    private final FileSystem fileSystem;
    private final StringBuilder log;
    private Log.Level logLevel;
    private final Platform platform;
    private final Properties properties;
    private Program program;
    private State state;

    public static Engine getCurrentEngine() {
        return CURRENT_ENGINE.get();
    }

    public Engine(Platform platform) {
        if (platform == null) {
            throw new NullPointerException("platform");
        }

        this.fileSystem = new FileSystem(platform);
        this.log = new StringBuilder();
        this.logLevel = Log.Level.Warning;
        this.platform = platform;
        this.properties = new Properties();
        this.state = State.Created;
        CURRENT_ENGINE.set(this);
    }

    public CanvasImp createCanvasImp(Size size) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        return this.platform.createCanvasImp(size);
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

    public ViewImp showView(Size size, EnumSet<Window.Feature> features) {
        if (size == null) {
            throw new NullPointerException("size");
        }

        if (features == null) {
            throw new NullPointerException("features");
        }

        return this.platform.showView(new ViewInfo(size, features));
    }

    public void start() {
        if (this.state != State.Created) {
            throw new IllegalStateException("Engine has already been started.");
        }

        this.state = State.Started;
        this.fileSystem.init();
        this.loadProperties();
        this.startProgram();
    }

    void log(Log.Level level, String message, Throwable exception) {
        if (this.matchesLogLevel(level)) {
            this.writeLine(level.toString() + ": " + message);
            if (exception != null) {
                this.writeLine(exception.toString());
                for (StackTraceElement el : exception.getStackTrace()) {
                    this.writeLine("   " + el.toString());
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
        this.writeLine(Util.args(message, args));
        this.showLog();
    }

    void fatalError(String messageKey, Object... args) {
        this.fatalError(null, messageKey, args);
    }

    void fatalError(Throwable exception, String messageKey, Object... args) {
        this.log(Log.Level.Error, Util.args(Message.translate(messageKey), args), exception);
        this.showLog();
        this.platform.stop();
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

    private void loadProperties() {
        try {
            for (String propertyFile : this.platform.listPropertyFiles()) {
                this.properties.loadFromResource(propertyFile);
            }

            // Load official properties file again to prevent manipulation
            this.properties.loadFromResource(JEDA_PROPERTIES_FILE);
        }
        catch (Exception ex) {
            this.fatalError(ex, Message.LOAD_PROPERTIES_ERROR);
        }
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

    private void showLog() {
        LogInfo logInfo = new LogInfo();
        logInfo.setButtonText(Message.translate(Message.LOG_BUTTON));
        logInfo.setLog(this.log.toString());
        logInfo.setTitle(Message.translate(Message.LOG_TITLE));
        this.platform.showLog(logInfo);
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
                ListInfo<ProgramInfo> listInfo = new ListInfo<ProgramInfo>() {

                    @Override
                    protected void onSelect(ProgramInfo item) {
                        startProgram(item);
                    }

                    @Override
                    protected void onCancel() {
                        platform.stop();
                    }
                };
                for (ProgramInfo programInfo : programInfos) {
                    listInfo.addItem(programInfo.getName(), programInfo);
                }

                listInfo.sortItemsByName();
                listInfo.setTitle(Message.translate(Message.CHOOSE_PROGRAM_TITLE));
                this.platform.showList(listInfo);
            }
        }
        catch (Exception ex) {
            this.fatalError(ex, Message.CHOOSE_PROGRAM_ERROR);
        }
    }

    private void startProgram(ProgramInfo programInfo) {
        this.program = this.createProgram(programInfo.getProgramClass());
        if (this.program != null) {
            ProgramThread thread = new ProgramThread(this, this.program);
            thread.setName(programInfo.getName());
            thread.start();
        }
    }

    private void writeLine(String line) {
        this.log.append(line);
        this.log.append('\n');
        System.out.println(line);
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

    private static class ProgramThread extends Thread {

        private final Engine engine;
        private final Program program;

        public ProgramThread(Engine engine, Program program) {
            this.engine = engine;
            this.program = program;
        }

        @Override
        public final void run() {
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
