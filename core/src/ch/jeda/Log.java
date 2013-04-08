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

/**
 * <b>Internal</b>. Do not use this class.<p>
 */
public final class Log {

    public enum Level {

        Debug, Info, Warning, Error
    }

    public static void debug(String message) {
        Engine.getContext().log(Level.Debug, message, null);
    }

    public static void debug(String message, Object... args) {
        Engine.getContext().log(Level.Debug, Util.args(message, args), null);
    }

    public static void error(String messageKey, Object... args) {
        Engine.getContext().log(Level.Error, Util.args(Message.translate(messageKey), args), null);
    }

    public static void info(String messageKey, Object... args) {
        Engine.getContext().log(Level.Info, Util.args(Message.translate(messageKey), args), null);
    }

    public static void warning(String messageKey, Object... args) {
        Engine.getContext().log(Level.Warning, Util.args(Message.translate(messageKey), args), null);
    }
}
