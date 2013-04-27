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

import ch.jeda.platform.SelectionRequest;
import java.util.ArrayList;
import java.util.List;

class SelectProgramState extends EngineState {

    private static final String DEFAULT_PROGRAM_PROPERTY = "jeda.default.program";

    SelectProgramState(final Context context) {
        super(context);
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
            // Load all program classes
            final List<ProgramWrapper> programWrappers = new ArrayList<ProgramWrapper>();
            ProgramWrapper defaultProgram = null;
            final String defaultProgramName = this.context.getProperties().
                    getString(DEFAULT_PROGRAM_PROPERTY);
            for (String className : this.context.listClassNames()) {
                final ProgramWrapper pi = ProgramWrapper.tryCreate(loadClass(className), this.context);
                if (pi != null) {
                    programWrappers.add(pi);
                    if (pi.getName().equals(defaultProgramName)) {
                        defaultProgram = pi;
                    }
                }
            }

            if (defaultProgram != null) {
                Engine.enterCreateProgramState(defaultProgram);
            }
            else if (programWrappers.size() == 1) {
                Engine.enterCreateProgramState(programWrappers.get(0));
            }
            else if (programWrappers.isEmpty()) {
                this.logError(Message.NO_PROGRAM_ERROR);
                Engine.enterShutdownState();
            }
            else {
                final SelectionRequest<ProgramWrapper> request = new SelectionRequest<ProgramWrapper>();
                for (ProgramWrapper programInfo : programWrappers) {
                    request.addItem(programInfo.getName(), programInfo);
                }

                request.sortItemsByName();
                request.setTitle(Message.translate(Message.CHOOSE_PROGRAM_TITLE));
                this.context.showSelectionRequest(request);
                request.waitForResult();
                if (request.isCancelled()) {
                    Engine.enterShutdownState();
                }
                else {
                    Engine.enterCreateProgramState(request.getResult());
                }
            }
        }
        catch (final Exception ex) {
            this.logError(ex, Message.CHOOSE_PROGRAM_ERROR);
            Engine.enterShutdownState();
        }
    }

    private static Class<?> loadClass(final String className) throws Exception {
        try {
            return Class.forName(className);
        }
        catch (final NoClassDefFoundError ex) {
            return Class.forName(className, false,
                                 Thread.currentThread().getContextClassLoader());
        }
        catch (final ExceptionInInitializerError ex) {
            return Class.forName(className, false,
                                 Thread.currentThread().getContextClassLoader());
        }
    }
}
