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

class CreateProgramState extends EngineState {

    private final Class<Program> programClass;

    CreateProgramState(final Context context, final Class<Program> programClass) {
        super(context);
        this.programClass = programClass;
    }

    @Override
    void onPause() {
        Engine.enterShutdownState();
    }

    @Override
    void onResume() {
    }

    @Override
    void onStop() {
    }

    @Override
    void run() {
        try {
            Engine.enterExecuteProgramState(this.programClass.
                    getDeclaredConstructor(new Class[0]).
                    newInstance(new Object[0]));
        }
        catch (final Exception ex) {
            this.logError(ex, Message.PROGRAM_CREATE_ERROR, this.programClass);
            Engine.enterShutdownState();
        }
    }
}
