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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

abstract class ProgramWrapper {

    private static final String RUN_METHOD = "run";
    private final String name;

    static ProgramWrapper tryCreate(final Class<?> candidate, final Context context) {
        if (Modifier.isAbstract(candidate.getModifiers())) {
            return null;
        }
        else if (Program.class.isAssignableFrom(candidate)) {
            return tryCreateInherited((Class<Program>) candidate, context);
        }
        else if (candidate.isAnnotationPresent(JedaProgram.class)) {
            return tryCreateAnnotated(candidate, context);
        }
        else if (candidate.isAnnotationPresent(NoMain.class)) {
            return tryCreateAnnotated(candidate, context);
        }
        else {
            return null;
        }
    }

    abstract void createInstance() throws Throwable;

    final String getName() {
        return this.name;
    }

    abstract String getProgramClassName();

    abstract void run() throws Throwable;

    abstract void setState(ProgramState state);

    private static ProgramWrapper tryCreateAnnotated(final Class<?> candidate, final Context context) {
        try {
            final Constructor<?> constructor = candidate.getDeclaredConstructor();
            if (!Modifier.isPublic(constructor.getModifiers())) {
                return null;
            }

            final Method runMethod = candidate.getDeclaredMethod(RUN_METHOD);
            if (!Modifier.isPublic(runMethod.getModifiers())) {
                return null;
            }

            return new AnnotatedProgramWrapper(programName(candidate, context), constructor, runMethod);

        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }

    private static ProgramWrapper tryCreateInherited(final Class<Program> candidate, final Context context) {
        try {
            final Constructor<Program> constructor = candidate.getDeclaredConstructor();
            if (!Modifier.isPublic(constructor.getModifiers())) {
                return null;
            }

            return new InheritedProgramWrapper(programName(candidate, context), constructor);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }

    private static String programName(final Class<?> programClass, final Context context) {
        final String name = context.getProperties().getString(programClass.getName());
        if (name == null) {
            return programClass.getSimpleName();
        }
        else {
            return name;
        }
    }

    ProgramWrapper(final String name) {
        this.name = name;
    }

    private static final class AnnotatedProgramWrapper extends ProgramWrapper {

        private final Constructor<?> constructor;
        private final Method runMethod;
        private Object program;

        public AnnotatedProgramWrapper(final String name,
                                       final Constructor<?> constructor,
                                       final Method runMethod) {
            super(name);
            this.constructor = constructor;
            this.runMethod = runMethod;
        }

        @Override
        void createInstance() throws Throwable {
            try {
                this.program = this.constructor.newInstance();
            }
            catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        }

        @Override
        String getProgramClassName() {
            return this.constructor.getDeclaringClass().getName();
        }

        @Override
        void run() throws Throwable {
            try {
                this.runMethod.invoke(this.program);
            }
            catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        }

        @Override
        void setState(final ProgramState state) {
            // ignore
        }
    }

    private static final class InheritedProgramWrapper extends ProgramWrapper {

        private final Constructor<Program> constructor;
        private Program program;

        InheritedProgramWrapper(final String name,
                                final Constructor<Program> constructor) {
            super(name);
            this.constructor = constructor;
        }

        @Override
        void createInstance() throws Throwable {
            try {
                this.program = this.constructor.newInstance();
            }
            catch (InvocationTargetException ex) {
                throw (Exception) ex.getCause();
            }
        }

        @Override
        String getProgramClassName() {
            return this.constructor.getDeclaringClass().getName();
        }

        @Override
        void run() throws Throwable {
            this.program.run();
        }

        @Override
        void setState(final ProgramState state) {
            this.program.setState(state);
        }
    }
}
