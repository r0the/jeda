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

abstract class EngineState {

    final Context context;

    EngineState(final Context context) {
        this.context = context;
    }

    final void logError(final String messageKey, final Object... args) {
        this.context.log(Log.Level.Error,
                         Util.args(Message.translate(messageKey), args), null);
    }

    final void logError(final Throwable exception, final String messageKey,
                        final Object... args) {
        this.context.log(Log.Level.Error,
                         Util.args(Message.translate(messageKey), args), exception);
    }

    abstract void onPause();

    abstract void onResume();

    abstract void onStop();

    abstract void run();
}
