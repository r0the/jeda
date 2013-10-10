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

import java.util.EnumSet;

public abstract class AbstractButton implements KeyDownListener, KeyUpListener, PointerListener {

    private final EnumSet<Key> keys;
    private final EnumSet<Key> pressedKeys;
    private boolean activated;
    private Integer pointerId;

    protected AbstractButton() {
        this.keys = EnumSet.noneOf(Key.class);
        this.pressedKeys = EnumSet.noneOf(Key.class);
    }

    public void addKey(final Key key) {
        this.keys.add(key);
    }

    public boolean isActivated() {
        return this.activated;
    }

    public boolean isPressed() {
        return !this.pressedKeys.isEmpty() || pointerId != null;
    }

    @Override
    public void onKeyDown(KeyEvent event) {
        if (this.keys.contains(event.getKey())) {
            this.pressedKeys.add(event.getKey());
            this.activated = false;
        }
    }

    @Override
    public void onKeyUp(KeyEvent event) {
        if (this.pressedKeys.remove(event.getKey())) {
            this.activated = true;
        }
    }

    public void removeKey(final Key key) {
        this.keys.remove(key);
    }

    @Override
    public void onPointerDown(PointerEvent event) {
        if (this.pointerId == null && contains(event.getX(), event.getY())) {
            this.pointerId = event.getPointerId();
            this.activated = false;
        }
    }

    @Override
    public void onPointerMoved(PointerEvent event) {
        if (this.pointerId != null && event.getPointerId() == this.pointerId && !contains(event.getX(), event.getY())) {
            this.pointerId = null;
        }
    }

    @Override
    public void onPointerUp(PointerEvent event) {
        if (this.pointerId != null && event.getPointerId() == this.pointerId && contains(event.getX(), event.getY())) {
            this.pointerId = null;
            this.activated = true;
        }
    }

    protected abstract boolean contains(float x, float y);
}
