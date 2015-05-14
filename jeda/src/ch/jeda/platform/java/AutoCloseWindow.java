/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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
package ch.jeda.platform.java;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class AutoCloseWindow extends BaseWindow {

    protected AutoCloseWindow(final WindowManager manager) {
        super(manager);
        addWindowListener(new WindowListener(this));
    }

    private static class WindowListener extends WindowAdapter {

        protected final BaseWindow window;

        public WindowListener(final BaseWindow window) {
            this.window = window;
        }

        @Override
        public void windowClosing(final WindowEvent event) {
            window.cancel();
        }
    }
}
