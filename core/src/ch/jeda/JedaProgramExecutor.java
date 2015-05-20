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

import ch.jeda.platform.SelectionRequest;

class JedaProgramExecutor implements Runnable {

    private static final String DEFAULT_PROGRAM_PROPERTY = "jeda.default.program";
    private final JedaEngine engine;
    private final String programClassName;
    private ProgramClassWrapper programClass;

    JedaProgramExecutor(final JedaEngine engine, final String programClassName) {
        this.engine = engine;
        this.programClassName = programClassName;
    }

    @Override
    public void run() {
        executeProgram();
        engine.programTerminated();
    }

    String getProgramName() {
        if (programClass == null) {
            return "";
        }
        else {
            return programClass.getName();
        }
    }

    void stop() {
        // TODO
    }

    private void executeProgram() {
        programClass = selectProgramClass();
        if (programClass == null) {
            return;
        }

        final Runnable program = createProgram(programClass);
        if (program == null) {
            return;
        }

        try {
            program.run();
        }
        catch (final Throwable ex) {
            Log.err(ex, Message.PROGRAM_ERROR_RUN, programClass);
        }
    }

    private Runnable createProgram(final ProgramClassWrapper programClass) {
        try {
            return programClass.createInstance();
        }
        catch (final Throwable ex) {
            Log.err(ex, Message.PROGRAM_ERROR_CREATE);
            return null;
        }
    }

    private ProgramClassWrapper selectProgramClass() {
        final ProgramClassWrapper[] candidates = engine.getProgramClasses();
        // If a program class has been specified...
        if (programClassName != null) {
            for (int i = 0; i < candidates.length; ++i) {
                if (candidates[i].getProgramClassName().equals(programClassName)) {
                    return candidates[i];
                }
            }

            Log.err(Message.PROGRAM_ERROR_CLASS_NOT_FOUND, programClassName);
            return null;
        }

        // No program class is specified, try default program
        final String defaultProgramName = engine.getProperties().getString(DEFAULT_PROGRAM_PROPERTY);
        for (int i = 0; i < candidates.length; ++i) {
            if (candidates[i].getProgramClassName().equals(defaultProgramName)) {
                return candidates[i];
            }
        }

        // Default program not found, check if only one program available
        if (candidates.length == 1) {
            return candidates[0];
        }

        if (candidates.length == 0) {
            Log.err(Message.PROGRAM_ERROR_NO_CLASS_FOUND);
            return null;
        }

        try {
            final SelectionRequest<ProgramClassWrapper> request = new SelectionRequest<ProgramClassWrapper>();
            for (int i = 0; i < candidates.length; ++i) {
                request.addItem(candidates[i].getName(), candidates[i]);
            }

            request.sortItemsByName();
            request.setTitle(Message.get(Message.GUI_SELECT_PROGRAM_TITLE));
            engine.showSelectionRequest(request);
            request.waitForResult();
            if (request.isCancelled()) {
                return null;
            }
            else {
                return request.getResult();
            }
        }
        catch (final Exception ex) {
            Log.err(ex, Message.PROGRAM_ERROR_SELECT);
            return null;
        }

    }
}
