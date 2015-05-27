/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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

/**
 * <b>Internal</b>. Do not use this class.
 */
public final class Log {

    private static LogImp imp = new SystemLogImp();

    public static final void d(final Object... message) {
        imp.d(Convert.toString(message), null);
    }

    public static final void d(final Throwable throwable, final Object... message) {
        imp.d(Convert.toString(message), throwable);
    }

    public static final void e(final Object... message) {
        imp.e(Convert.toString(message), null);
    }

    public static final void e(final Throwable throwable, final Object... message) {
        imp.e(Convert.toString(message), throwable);
    }

    public static final void i(final Object... message) {
        imp.i(Convert.toString(message), null);
    }

    public static final void i(final Throwable throwable, final Object... message) {
        imp.i(Convert.toString(message), throwable);
    }

    public static final void w(final Object... message) {
        imp.w(Convert.toString(message), null);
    }

    public static final void w(final Throwable throwable, final Object... message) {
        imp.w(Convert.toString(message), throwable);
    }

//    /**
//     * <b>Internal</b>. Do not use this method.
//     */
//    public static void err(final String messageKey, Object... args) {
//        Jeda.log(LogLevel.ERROR, String.format(Message.get(messageKey), args) + "\n");
//    }
//
//    /**
//     * <b>Internal</b>. Do not use this method.
//     */
//    public static void err(final Throwable throwable, final String messageKey, Object... args) {
//        Jeda.log(LogLevel.ERROR, String.format(Message.get(messageKey), args) + "\n");
//        if (throwable != null) {
//            Jeda.log(LogLevel.ERROR, "  " + throwable + "\n");
//            final StackTraceElement[] stackTrace = throwable.getStackTrace();
//            for (int i = 0; i < stackTrace.length; ++i) {
//                Jeda.log(LogLevel.ERROR, "   " + stackTrace[i].toString() + "\n");
//            }
//        }
//    }
    private Log() {
    }
}
