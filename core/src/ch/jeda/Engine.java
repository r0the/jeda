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
 * <b>Internal</b>. Do not use this class.
 * <p>
 * Represents the Jeda core. The Jeda engine manages the lifecycle of Jeda
 * programs.
 */
public final class Engine {

    private static Context context;
    private static StateThread stateThread;

    public static Context getContext() {
        return context;
    }

    public static void init(final ContextImp contextImp) {
        context = new Context(contextImp);
        context.init();
        setState(new SelectProgramState(context));
    }

    public static void pause() {
        stateThread.state.onPause();
    }

    public static void resume() {
        stateThread.state.onResume();
    }

    public static void stop() {
        stateThread.state.onStop();
    }

    static void setState(final EngineState state) {
        stateThread = new StateThread(stateThread, state);
        stateThread.start();
    }

    private Engine() {
    }

    private static class StateThread extends Thread {

        private final Thread previousStateThread;
        private final EngineState state;

        public StateThread(final Thread previousStateThread,
                           final EngineState state) {
            this.previousStateThread = previousStateThread;
            this.state = state;
            this.setName(this.state.name);
        }

        @Override
        public void run() {
            if (this.previousStateThread != null) {
                try {
                    this.previousStateThread.join();
                }
                catch (InterruptedException ex) {
                    // ignore
                }
            }

            this.state.run();
        }
    }
}
