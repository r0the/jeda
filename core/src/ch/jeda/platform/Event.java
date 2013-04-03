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
package ch.jeda.platform;

import ch.jeda.ui.Key;

public final class Event {

    public enum Type {

        KeyPressed, KeyReleased, KeyTyped, PointerAvailable, PointerMoved,
        PointerUnavailable, WindowFocusGained, WindowFocusLost
    }
    public final Key key;
    public final char keyChar;
    public final int pointerId;
    public final Type type;
    public final int x;
    public final int y;

    public static Event createKeyPressed(Key key) {
        assert key != null;

        return new Event(Type.KeyPressed, key, '\0');
    }

    public static Event createKeyReleased(Key key) {
        assert key != null;

        return new Event(Type.KeyReleased, key, '\0');
    }

    public static Event createKeyTyped(char keyChar) {
        return new Event(Type.KeyTyped, null, keyChar);
    }

    public static Event createPointerAvailable(int pointerId, int x, int y) {
        return new Event(Type.PointerAvailable, pointerId, x, y);
    }

    public static Event createPointerMoved(int pointerId, int x, int y) {
        return new Event(Type.PointerMoved, pointerId, x, y);
    }

    public static Event createPointerUnavailable(int pointerId) {
        return new Event(Type.PointerUnavailable, pointerId, -1, -1);
    }

    public static Event createWindowFocusGained() {
        return new Event(Type.WindowFocusGained);
    }

    public static Event createWindowFocusLost() {
        return new Event(Type.WindowFocusLost);
    }

    private Event(Type type) {
        this(type, null, '\0', 0, -1, -1);
    }

    private Event(Type type, Key key, char keyChar) {
        this(type, key, keyChar, 0, -1, -1);
    }

    private Event(Type type, int pointerId, int x, int y) {
        this(type, null, '\0', pointerId, x, y);
    }

    public Event(Type type, Key key, char keyChar, int pointerId, int x, int y) {
        this.key = key;
        this.keyChar = keyChar;
        this.pointerId = pointerId;
        this.type = type;
        this.x = x;
        this.y = y;
    }
}
