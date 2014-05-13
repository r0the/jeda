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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <b>Internal</b>. Do not use this class.
 */
public class Log {

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static void dbg(final Object... message) {
        Jeda.log(LogLevel.DEBUG, Util.toString(message));
    }

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static void err(final String messageKey, Object... args) {
        Jeda.log(LogLevel.ERROR, String.format(Log.getMessage(messageKey), args) + "\n");
    }

    /**
     * <b>Internal</b>. Do not use this method.
     */
    public static void err(final Throwable throwable, final String messageKey, Object... args) {
        Jeda.log(LogLevel.ERROR, String.format(Log.getMessage(messageKey), args) + "\n");
        if (throwable != null) {
            Jeda.log(LogLevel.ERROR, "  " + throwable + "\n");
            final StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (int i = 0; i < stackTrace.length; ++i) {
                Jeda.log(LogLevel.ERROR, "   " + stackTrace[i].toString() + "\n");
            }
        }
    }

    static String getMessage(final String key) {
        try {
            final ResourceBundle rb = ResourceBundle.getBundle("res/jeda/translations", Locale.GERMAN);
            if (rb.containsKey(key)) {
                return rb.getString(key);
            }
        }
        catch (final Exception ex) {
            // ignore
        }

        return "<" + key + ">";
    }

    private Log() {
    }
}
