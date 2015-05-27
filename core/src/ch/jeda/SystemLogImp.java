/*
 * Copyright (C) 2015 by Stefan Rothe
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

import ch.jeda.platform.LogImp;

class SystemLogImp implements LogImp {

    @Override
    public void d(final String message, final Throwable throwable) {
        println("D", message);
        printThrowable("D", throwable);
    }

    @Override
    public void e(final String message, final Throwable throwable) {
        println("E", message);
        printThrowable("E", throwable);
    }

    @Override
    public void i(final String message, final Throwable throwable) {
        println("I", message);
        printThrowable("I", throwable);
    }

    @Override
    public void w(final String message, final Throwable throwable) {
        println("W", message);
        printThrowable("W", throwable);
    }

    private void printThrowable(final String level, final Throwable throwable) {
        if (throwable != null) {
            println(level, "  " + throwable);
            final StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (int i = 0; i < stackTrace.length; ++i) {
                println(level, "    " + stackTrace[i].toString());
            }
        }
    }

    private void println(final String level, final String message) {
        System.err.println(level + ", " + message);
    }
}
