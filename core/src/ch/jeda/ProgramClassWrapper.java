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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

abstract class ProgramClassWrapper {

    private final String name;

    @SuppressWarnings("unchecked")
    static ProgramClassWrapper tryCreate(final Class<?> candidate) {
        // An abstract class cannot be a valid Jeda program.
        if (Modifier.isAbstract(candidate.getModifiers())) {
            return null;
        }
        else if (Program.class.isAssignableFrom(candidate)) {
            return tryCreateInherited((Class<Program>) candidate);
        }
        else if (hasInterface(candidate, JedaProgram.class)) {
            return tryCreateJedaProgram(candidate);
        }
        else {
            return null;
        }
    }

    ProgramClassWrapper(final String name) {
        this.name = name;
    }

    abstract Runnable createInstance() throws Throwable;

    final String getName() {
        return name;
    }

    abstract String getProgramClassName();

    @Override
    public String toString() {
        return getProgramClassName();
    }

    private static boolean hasInterface(final Class<?> candidateClass, final Class<?> targetInterface) {
        final Class[] interfaces = candidateClass.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            if (interfaces[i].equals(targetInterface)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private static ProgramClassWrapper tryCreateJedaProgram(final Class<?> candidate) {
        try {
            final Constructor<JedaProgram> constructor = (Constructor<JedaProgram>) candidate.getDeclaredConstructor();
            constructor.setAccessible(true);
            return new JedaProgramClassWrapper(programName(candidate), constructor);
        }
        catch (final NoSuchMethodException ex) {
            return null;
        }
        catch (final SecurityException ex) {
            return null;
        }
    }

    private static ProgramClassWrapper tryCreateInherited(final Class<Program> candidate) {
        try {
            final Constructor<Program> constructor = candidate.getDeclaredConstructor();
            if (!Modifier.isPublic(constructor.getModifiers())) {
                return null;
            }

            return new InheritedProgramClassWrapper(programName(candidate), constructor);
        }
        catch (final NoSuchMethodException ex) {
            return null;
        }
    }

    private static String programName(final Class<?> programClass) {
        final String name = Configuration.getString(programClass.getName(), null);
        if (name == null) {
            return programClass.getSimpleName();
        }
        else {
            return name;
        }
    }

    private static final class JedaProgramClassWrapper extends ProgramClassWrapper {

        private final Constructor<JedaProgram> constructor;

        JedaProgramClassWrapper(final String name, final Constructor<JedaProgram> constructor) {
            super(name);
            this.constructor = constructor;
        }

        @Override
        Runnable createInstance() throws Throwable {
            try {
                return constructor.newInstance();
            }
            catch (final InvocationTargetException ex) {
                throw ex.getCause();
            }
        }

        @Override
        String getProgramClassName() {
            return constructor.getDeclaringClass().getName();
        }
    }

    private static final class InheritedProgramClassWrapper extends ProgramClassWrapper {

        private final Constructor<Program> constructor;

        InheritedProgramClassWrapper(final String name, final Constructor<Program> constructor) {
            super(name);
            this.constructor = constructor;
        }

        @Override
        Runnable createInstance() throws Throwable {
            try {
                return constructor.newInstance();
            }
            catch (final InvocationTargetException ex) {
                throw ex.getCause();
            }
        }

        @Override
        String getProgramClassName() {
            return constructor.getDeclaringClass().getName();
        }
    }
}
