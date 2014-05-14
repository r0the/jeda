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
package ch.jeda.ui;

import ch.jeda.event.KeyDownListener;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.KeyUpListener;
import ch.jeda.event.Key;
import ch.jeda.event.PointerEvent;
import ch.jeda.event.PointerListener;
import java.util.EnumSet;

/**
 * @since 1
 */
public abstract class AbstractButton extends GraphicsItem implements KeyDownListener, KeyUpListener, PointerListener {

    private final EnumSet<Key> keys;
    private final EnumSet<Key> pressedKeys;
    private String action;
    private Integer pointerId;
    private final Window window;

    /**
     * @since 1
     */
    protected AbstractButton(final Window window, final String action) {
        this.action = action;
        this.keys = EnumSet.noneOf(Key.class);
        this.pressedKeys = EnumSet.noneOf(Key.class);
        this.window = window;
        this.window.addEventListener(this);
    }

    /**
     * @since 1
     */
    public final void addKey(final Key key) {
        this.keys.add(key);
    }

    /**
     * @since 1
     */
    public final String getAction() {
        return this.action;
    }

    /**
     * @since 1
     */
    public final boolean isPressed() {
        return !this.pressedKeys.isEmpty() || pointerId != null;
    }

    @Override
    public final void onKeyDown(KeyEvent event) {
        if (this.keys.contains(event.getKey())) {
            this.pressedKeys.add(event.getKey());
        }
    }

    @Override
    public final void onKeyUp(KeyEvent event) {
        if (this.pressedKeys.remove(event.getKey())) {
            this.window.sendAction(this, this.action);
        }
    }

    /**
     * @since 1
     */
    public final void removeKey(final Key key) {
        this.keys.remove(key);
    }

    @Override
    public final void onPointerDown(PointerEvent event) {
        if (this.pointerId == null && contains(event.getX(), event.getY())) {
            this.pointerId = event.getPointerId();
        }
    }

    @Override
    public final void onPointerMoved(PointerEvent event) {
        if (this.pointerId != null && event.getPointerId() == this.pointerId && !contains(event.getX(), event.getY())) {
            this.pointerId = null;
        }
    }

    @Override
    public final void onPointerUp(PointerEvent event) {
        if (this.pointerId != null && event.getPointerId() == this.pointerId) {
            this.pointerId = null;
            if (this.contains(event.getX(), event.getY())) {
                this.window.sendAction(this, this.action);
            }
        }
    }

    /**
     * @since 1
     */
    public final void setAction(final String action) {
        this.action = action;
    }
}
