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

class ExecuteState extends EngineState {

    private final Program program;

    @Override
    public void run() {
        try {
            this.program.setState(ProgramState.Running);
            this.program.run();
        }
        catch (Exception ex) {
            this.logError(ex, Message.PROGRAM_RUN_ERROR, this.program.getClass());
        }
        finally {
            this.setShutdownState();
        }
    }

    ExecuteState(final Context context, final String name,
                 final Program program) {
        super(context, name);
        this.program = program;
    }

    @Override
    void onPause() {
        this.program.setState(ProgramState.Paused);
    }

    @Override
    void onResume() {
        this.program.setState(ProgramState.Running);
    }

    @Override
    void onStop() {
        this.program.setState(ProgramState.Stopped);
    }
}
