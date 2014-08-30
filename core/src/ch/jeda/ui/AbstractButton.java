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

import ch.jeda.event.ActionEvent;
import ch.jeda.event.KeyDownListener;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.KeyUpListener;
import ch.jeda.event.Key;
import ch.jeda.event.PointerListener;
import java.util.EnumSet;

/**
 * @since 1.0
 */
@Deprecated
public abstract class AbstractButton extends Widget implements KeyDownListener, KeyUpListener, PointerListener {

    private final EnumSet<Key> keys;
    private final EnumSet<Key> pressedKeys;
    private String action;
    private Integer pointerId;
    private final Window window;

    /**
     * @since 1.0
     */
    @Deprecated
    protected AbstractButton(final Window window, final String action) {
        super(0, 0, Alignment.TOP_LEFT);
        this.action = action;
        this.keys = EnumSet.noneOf(Key.class);
        this.pressedKeys = EnumSet.noneOf(Key.class);
        this.window = window;
        this.window.addEventListener(this);
    }

    /**
     * @since 1.0
     */
    @Deprecated
    public final void addKey(final Key key) {
        this.keys.add(key);
    }

    /**
     * @since 1.0
     */
    @Deprecated
    public final String getAction() {
        return this.action;
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
            this.window.postEvent(new ActionEvent(this, this.action));
        }
    }

    /**
     * @since 1.0
     */
    @Deprecated
    public final void removeKey(final Key key) {
        this.keys.remove(key);
    }

    /**
     * @since 1.0
     */
    @Deprecated
    public final void setAction(final String action) {
        this.action = action;
    }
}
