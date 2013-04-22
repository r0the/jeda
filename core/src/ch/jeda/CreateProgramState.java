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

import java.lang.reflect.InvocationTargetException;

class CreateProgramState extends EngineState {

    private final ProgramInfo programInfo;

    CreateProgramState(final Context context, final ProgramInfo programInfo) {
        super(context);
        this.programInfo = programInfo;
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
        final Program program = createProgram(this.programInfo.getProgramClass());
        if (program == null) {
            Engine.enterShutdownState();
        }
        else {
            Engine.enterExecuteProgramState(program);
        }
    }

    private Program createProgram(final Class<Program> programClass) {
        try {
            return programClass.getDeclaredConstructor(new Class[0]).
                    newInstance(new Object[0]);
        }
        catch (final NoSuchMethodException ex) {
            this.logError(ex, Message.PROGRAM_CREATE_ERROR, programClass);
        }
        catch (final IllegalAccessException ex) {
            this.logError(ex, Message.PROGRAM_CREATE_ERROR, programClass);
        }
        catch (final InvocationTargetException ex) {
            this.logError(ex.getCause(), Message.PROGRAM_CREATE_ERROR, programClass);
        }
        catch (final InstantiationException ex) {
            this.logError(ex, Message.PROGRAM_CREATE_ERROR, programClass);
        }
        catch (final NoClassDefFoundError ex) {
            this.logError(ex, Message.PROGRAM_CREATE_ERROR, programClass);
        }

        return null;
    }
}
