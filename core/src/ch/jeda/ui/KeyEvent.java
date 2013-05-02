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
package ch.jeda.ui;

/**
 * Represents an event of types {@link EventType#KEY_DOWN},
 * {@link EventType#KEY_TYPED}, or {@link EventType#KEY_UP}.
 *
 * @since 1
 */
public final class KeyEvent extends Event {

    private final Key key;
    private final char keyChar;
    private final int repeatCount;

    public KeyEvent(final EventSource source, final EventType type,
                    final Key key) {
        this(source, type, key, '\0', 0);
    }

    public KeyEvent(final EventSource source, final EventType type,
                    final Key key, int repeatCount) {
        this(source, type, key, '\0', repeatCount);
    }

    public KeyEvent(final EventSource source, final EventType type,
                    final char keyChar) {
        this(source, type, Key.UNDEFINED, keyChar, 0);
    }

    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return this.key;
    }

    public char getKeyChar() {
        return this.keyChar;
    }

    public int getRepeatCount() {
        return this.repeatCount;
    }

    private KeyEvent(final EventSource source, final EventType type,
                     final Key key, final char keyChar, int repeatCount) {
        super(source, type);
        this.key = key;
        this.keyChar = keyChar;
        this.repeatCount = repeatCount;
    }
}
