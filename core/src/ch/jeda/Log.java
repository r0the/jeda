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

/**
 * <b>Internal</b>. Do not use this class.
 * <p>
 * Provides a low-level input/output interface for Jeda.
 */
public class Log {

    public static void dbg(final Object... message) {
        Jeda.log(LogLevel.DEBUG, Util.toString(message));
    }

    public static void err(final String messageKey, Object... args) {
        Jeda.log(LogLevel.ERROR, String.format(Helper.getMessage(messageKey), args) + "\n");
    }

    public static void err(final Throwable throwable, final String messageKey, Object... args) {
        Jeda.log(LogLevel.ERROR, String.format(Helper.getMessage(messageKey), args) + "\n");
        if (throwable != null) {
            Jeda.log(LogLevel.ERROR, "  " + throwable + "\n");
            final StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (int i = 0; i < stackTrace.length; ++i) {
                Jeda.log(LogLevel.ERROR, "   " + stackTrace[i].toString() + "\n");
            }
        }
    }

    private Log() {
    }
}
