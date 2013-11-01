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
        this.executeProgram();
        this.engine.programTerminated();
    }

    String getProgramName() {
        if (this.programClass == null) {
            return "";
        }
        else {
            return this.programClass.getName();
        }
    }

    void stop() {
        // TODO
    }

    private void executeProgram() {
        this.programClass = this.selectProgramClass();
        if (this.programClass == null) {
            return;
        }

        final Runnable program = this.createProgram(this.programClass);
        if (program == null) {
            return;
        }

        try {
            program.run();
        }
        catch (final Throwable ex) {
            IO.err(ex, "jeda.program.error.run", this.programClassName);
        }
    }

    private Runnable createProgram(final ProgramClassWrapper programClass) {
        try {
            return programClass.createInstance();
        }
        catch (final Throwable ex) {
            IO.err(ex, "jeda.program.error.create");
            return null;
        }
    }

    private ProgramClassWrapper selectProgramClass() {
        final ProgramClassWrapper[] candidates = this.engine.getProgramClasses();
        // If a program class has been specified...
        if (this.programClassName != null) {
            for (int i = 0; i < candidates.length; ++i) {
                if (candidates[i].getProgramClassName().equals(this.programClassName)) {
                    return candidates[i];
                }
            }

            IO.err("jeda.engine.error.program-class-not-found", this.programClassName);
            return null;
        }

        // No program class is specified, try default program
        final String defaultProgramName = this.engine.getProperties().getString(DEFAULT_PROGRAM_PROPERTY);
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
            IO.err("jeda.program.error.no-class-found");
            return null;
        }

        try {
            final SelectionRequest<ProgramClassWrapper> request = new SelectionRequest<ProgramClassWrapper>();
            for (int i = 0; i < candidates.length; ++i) {
                request.addItem(candidates[i].getName(), candidates[i]);
            }

            request.sortItemsByName();
            request.setTitle(Helper.getMessage("jeda.gui.select-program.title"));
            this.engine.showSelectionRequest(request);
            request.waitForResult();
            if (request.isCancelled()) {
                return null;
            }
            else {
                return request.getResult();
            }
        }
        catch (final Exception ex) {
            IO.err(ex, "jeda.program.error.select");
            return null;
        }

    }
}
