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
package ch.jeda.event;

import ch.jeda.Convert;
import java.util.EnumSet;

/**
 * Keeps track of pressed keys. A <tt>PressedKeys</tt> object can keep track of all keys that are currently pressed. In
 * order to do so, the object must be added as event listener to a window using the method
 * {@link ch.jeda.ui.Window#addEventListener(java.lang.Object)}.
 *
 * @since 1
 */
public final class PressedKeys implements KeyDownListener, KeyUpListener {

    private final EnumSet<Key> pressedKeys;

    /**
     * Constructs a new <tt>PressedKeys</tt> object.
     *
     * @since 1
     */
    public PressedKeys() {
        this.pressedKeys = EnumSet.noneOf(Key.class);
    }

    /**
     * Checks if a key is currenty pressed. Returns <tt>true</tt> if the specified key is currently pressed, returns
     * <tt>false</tt> otherwise.
     *
     * @param key the key to check
     * @return <tt>true</tt> if the specified key is currently pressed, <tt>false</tt> otherwise.
     *
     * @since 1
     */
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

    /**
     * Returns an array of all currently pressed keys.
     *
     * @return an array of all currently pressed keys
     *
     * @since 1
     */
    public Key[] toArray() {
        return this.pressedKeys.toArray(new Key[this.pressedKeys.size()]);
    }

    @Override
    public String toString() {
        return Convert.toString(this.pressedKeys);
    }
}
