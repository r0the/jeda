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
package ch.jeda.event;

import ch.jeda.ui.Key;
import ch.jeda.ui.KeyDownListener;
import ch.jeda.ui.KeyEvent;
import ch.jeda.ui.KeyUpListener;
import java.util.EnumSet;

public final class PressedKeys implements KeyDownListener, KeyUpListener {

    private final EnumSet<Key> pressedKeys;

    public PressedKeys() {
        this.pressedKeys = EnumSet.noneOf(Key.class);
    }

    public boolean contains(final Key key) {
        return this.pressedKeys.contains(key);
    }

    @Override
    public void onKeyDown(final KeyEvent event) {
        this.pressedKeys.add(event.getKey());
    }

    @Override
    public void onKeyUp(final KeyEvent event) {
        this.pressedKeys.remove(event.getKey());
    }
}
