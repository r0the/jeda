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

/**
 * <b>Internal.</b> Do not use this class.
 * <p>
 * Represents the Jeda core. The Jeda engine manages the lifecycle of Jeda
 * programs.
 */
public final class Engine {

    private static final Object stateLock = new Object();
    private static Thread engineThread;
    private static ProgramWrapper programWrapper;
    private static Context context;
    private static EngineState currentState;
    private static EngineState nextState;

    public static Context getContext() {
        return context;
    }

    public static String getProgramName() {
        if (programWrapper == null) {
            return null;
        }
        else {
            return programWrapper.getName();
        }
    }

    public static void init(final ContextImp contextImp) {
        context = new Context(contextImp);
        context.init();
        nextState = new SelectProgramState(context);
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

    static void enterCreateProgramState(final ProgramWrapper programWrapper) {
        Engine.programWrapper = programWrapper;
        nextState = new CreateProgramState(context, programWrapper);
    }

    static void enterExecuteProgramState() {
        nextState = new ExecuteProgramState(context, programWrapper);
    }

    static void enterShutdownState() {
        nextState = new ShutdownState(context);
    }

    private Engine() {
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
}
