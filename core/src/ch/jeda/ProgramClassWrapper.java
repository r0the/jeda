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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

abstract class ProgramClassWrapper {

    private final String name;

    static ProgramClassWrapper tryCreate(final Class<?> candidate, final Properties properties) {
        // An abstract class cannot be a valid Jeda program.
        if (Modifier.isAbstract(candidate.getModifiers())) {
            return null;
        }
        else if (Program.class.isAssignableFrom(candidate)) {
            return tryCreateInherited((Class<Program>) candidate, properties);
        }
        else if (Helper.hasInterface(candidate, JedaProgram.class)) {
            return tryCreateJedaProgram(candidate, properties);
        }
        else {
            return null;
        }
    }

    abstract Runnable createInstance() throws Throwable;

    final String getName() {
        return this.name;
    }

    abstract String getProgramClassName();

    static ProgramClassWrapper tryCreateJedaProgram(final Class<?> candidate, final Properties properties) {
        try {
            final Constructor<JedaProgram> constructor = (Constructor<JedaProgram>) candidate.getDeclaredConstructor();
            constructor.setAccessible(true);
            return new JedaProgramClassWrapper(programName(candidate, properties), constructor);
        }
        catch (final NoSuchMethodException ex) {
            return null;
        }
        catch (final SecurityException ex) {
            return null;
        }
    }

    private static ProgramClassWrapper tryCreateInherited(final Class<Program> candidate, final Properties properties) {
        try {
            final Constructor<Program> constructor = candidate.getDeclaredConstructor();
            if (!Modifier.isPublic(constructor.getModifiers())) {
                return null;
            }

            return new InheritedProgramClassWrapper(programName(candidate, properties), constructor);
        }
        catch (final NoSuchMethodException ex) {
            return null;
        }
    }

    private static String programName(final Class<?> programClass, final Properties properties) {
        final String name = properties.getString(programClass.getName());
        if (name == null) {
            return programClass.getSimpleName();
        }
        else {
            return name;
        }
    }

    ProgramClassWrapper(final String name) {
        this.name = name;
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
                return this.constructor.newInstance();
            }
            catch (final InvocationTargetException ex) {
                throw ex.getCause();
            }
        }

        @Override
        String getProgramClassName() {
            return this.constructor.getDeclaringClass().getName();
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
                return this.constructor.newInstance();
            }
            catch (final InvocationTargetException ex) {
                throw ex.getCause();
            }
        }

        @Override
        String getProgramClassName() {
            return this.constructor.getDeclaringClass().getName();
        }
    }
}
