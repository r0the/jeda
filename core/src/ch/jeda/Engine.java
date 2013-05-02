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

import ch.jeda.platform.ContextImp;
import ch.jeda.platform.SelectionRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Internal.</b> Do not use this class.
 * <p>
 * Represents the Jeda core. The Jeda engine manages the lifecycle of Jeda
 * programs.
 */
public final class Engine {

    private static final Object stateLock = new Object();
    private static Thread engineThread;
    private static ProgramWrapper program;
    private static Context context;
    private static State currentState;
    private static State nextState;
    private static JedaPlugin plugins;

    public static Context getContext() {
        return context;
    }

    public static String getProgramName() {
        if (program == null) {
            return null;
        }
        else {
            return program.getName();
        }
    }

    public static void init(final ContextImp contextImp) {
        context = new Context(contextImp);
        context.init();
        nextState = new InitEngineState();
        engineThread = new EngineThread();
        engineThread.setName(Message.translate(Message.ENGINE_THREAD_NAME));
        engineThread.start();
    }

    public static void pause() {
        synchronized (stateLock) {
            if (currentState != null) {
                currentState.onPause();
            }
        }
    }

    public static void resume() {
        synchronized (stateLock) {
            if (currentState != null) {
                currentState.onResume();
            }
        }
    }

    public static void stop() {
        synchronized (stateLock) {
            if (currentState != null) {
                currentState.onStop();
            }
        }
    }

    private Engine() {
    }

    private static void enterCreateProgramState(final ProgramWrapper programWrapper) {
        Engine.program = programWrapper;
        nextState = new CreateProgramState(programWrapper);
    }

    private static void enterExecuteProgramState() {
        nextState = new ExecuteProgramState(program);
    }

    private static void enterSelectProgramState(final List<ProgramWrapper> candidates) {
        nextState = new SelectProgramState(candidates);
    }

    private static void enterShutdownState() {
        nextState = new ShutdownState();
    }

    private static void logError(final String messageKey, final Object... args) {
        context.log(LogLevel.Error,
                    Util.args(Message.translate(messageKey), args), null);
    }

    private static void logError(final Throwable exception, final String messageKey,
                                 final Object... args) {
        context.log(LogLevel.Error,
                    Util.args(Message.translate(messageKey), args), exception);
    }

    private static class EngineThread extends Thread {

        @Override
        public void run() {
            while (nextState != null) {
                synchronized (stateLock) {
                    currentState = nextState;
                    nextState = null;
                }
                currentState.run();
            }
        }
    }

    static abstract class State {

        abstract void onPause();

        abstract void onResume();

        abstract void onStop();

        abstract void run();
    }

    static class InitEngineState extends State {

        @Override
        void onPause() {
            enterShutdownState();
        }

        @Override
        void onResume() {
        }

        @Override
        void onStop() {
            enterShutdownState();
        }

        @Override
        void run() {
            final List<ProgramWrapper> programs = new ArrayList<ProgramWrapper>();
            ProgramWrapper defaultProgram = null;
            try {
                final String defaultProgramName = context.defaultProgramName();
                final Class[] classes = context.loadClasses();
                // Load jeda plugins and jeda programs
                for (int i = 0; i < classes.length; ++i) {
                    final ProgramWrapper pw = ProgramWrapper.tryCreate(classes[i], context);
                    if (pw != null) {
                        programs.add(pw);
                        if (pw.getProgramClassName().equals(defaultProgramName)) {
                            defaultProgram = pw;
                        }
                    }
                }

            }
            catch (Exception ex) {
                logError(ex, Message.LOAD_CLASSES_ERROR);
            }

            // Determine program to execute
            if (defaultProgram != null) {
                enterCreateProgramState(defaultProgram);
            }
            else if (programs.size() == 1) {
                enterCreateProgramState(programs.get(0));
            }
            else if (programs.isEmpty()) {
                logError(Message.NO_PROGRAM_ERROR);
                enterShutdownState();
            }
            else {
                enterSelectProgramState(programs);
            }
        }
    }

    static class CreateProgramState extends State {

        private final ProgramWrapper programWrapper;

        CreateProgramState(final ProgramWrapper programWrapper) {
            this.programWrapper = programWrapper;
        }

        @Override
        void onPause() {
            enterShutdownState();
        }

        @Override
        void onResume() {
        }

        @Override
        void onStop() {
            enterShutdownState();
        }

        @Override
        void run() {
            try {
                this.programWrapper.createInstance();
                enterExecuteProgramState();
            }
            catch (final Throwable ex) {
                logError(ex, Message.PROGRAM_CREATE_ERROR, this.programWrapper);
                enterShutdownState();
            }
        }
    }

    static class ExecuteProgramState extends State {

        private final ProgramWrapper programWrapper;

        ExecuteProgramState(final ProgramWrapper programWrapper) {
            this.programWrapper = programWrapper;
        }

        @Override
        void onPause() {
            this.programWrapper.setState(ProgramState.Paused);
        }

        @Override
        void onResume() {
            this.programWrapper.setState(ProgramState.Running);
        }

        @Override
        void onStop() {
            this.programWrapper.setState(ProgramState.Stopped);
        }

        @Override
        void run() {
            try {
                this.programWrapper.setState(ProgramState.Running);
                this.programWrapper.run();
                this.programWrapper.setState(ProgramState.Stopped);
            }
            catch (Throwable ex) {
                logError(ex, Message.PROGRAM_RUN_ERROR, this.programWrapper.getClass());
            }
            finally {
                enterShutdownState();
            }
        }
    }

    static class SelectProgramState extends State {

        private final List<ProgramWrapper> candidates;

        SelectProgramState(final List<ProgramWrapper> candidates) {
            this.candidates = candidates;
        }

        @Override
        void onPause() {
            enterShutdownState();
        }

        @Override
        void onResume() {
        }

        @Override
        void onStop() {
            enterShutdownState();
        }

        @Override
        void run() {
            try {
                final SelectionRequest<ProgramWrapper> request = new SelectionRequest<ProgramWrapper>();
                for (ProgramWrapper candidate : this.candidates) {
                    request.addItem(candidate.getName(), candidate);
                }

                request.sortItemsByName();
                request.setTitle(Message.translate(Message.CHOOSE_PROGRAM_TITLE));
                context.showSelectionRequest(request);
                request.waitForResult();
                if (request.isCancelled()) {
                    enterShutdownState();
                }
                else {
                    enterCreateProgramState(request.getResult());
                }
            }
            catch (final Exception ex) {
                logError(ex, Message.CHOOSE_PROGRAM_ERROR);
                enterShutdownState();
            }
        }
    }

    static class ShutdownState extends State {

        @Override
        void onPause() {
        }

        @Override
        void onResume() {
        }

        @Override
        void onStop() {
        }

        @Override
        void run() {
            context.shutdown();
        }
    }
}
