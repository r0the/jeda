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
package ch.jeda.world;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Key;
import ch.jeda.ui.KeyEvent;
import ch.jeda.ui.KeyListener;
import ch.jeda.ui.PointerEvent;
import ch.jeda.ui.PointerListener;
import java.util.EnumSet;
import java.util.Set;

public class Button extends WorldObject implements KeyListener, PointerListener {

    private final Set<Key> keys;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private boolean clicked;
    private Color backgroundColor;
    private Key pressedKey;
    private Integer pressedPointer;
    private String text;

    public Button(final float x, final float y, final float width, final float height) {
        super();
        this.keys = EnumSet.noneOf(Key.class);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = new Color(150, 150, 150, 150);
    }

    public Button(final float x, final float y, final float width, final float height, final Key... keys) {
        this(x, y, width, height);
        for (int i = 0; i < keys.length; ++i) {
            this.keys.add(keys[i]);
        }
    }

    public final void addKey(final Key key) {
        this.keys.add(key);
    }

    @Override
    public boolean contains(final float x, final float y) {
        return this.x <= x && x <= this.x + this.width && this.y <= y && y <= this.y + this.height;
    }

    @Override
    public void draw(final Canvas canvas) {
        this.clicked = false;
        canvas.setColor(this.backgroundColor);
        canvas.fillRectangle(this.x, this.y, this.width, this.height);
        canvas.setColor(Color.WHITE);
        canvas.drawRectangle(this.x, this.y, this.width, this.height);
        canvas.drawText(this.x + this.width / 2f, this.y + this.height / 2f, this.text, Alignment.CENTER);
    }

    public final String getText() {
        return this.text;
    }

    public final boolean isClicked() {
        return this.clicked;
    }

    public final boolean isPressed() {
        return this.pressedKey != null;
    }

    @Override
    public final void onKeyDown(final KeyEvent event) {
        if (this.pressedKey == null && this.keys.contains(event.getKey())) {
            this.pressedKey = event.getKey();
        }
    }

    @Override
    public final void onKeyUp(final KeyEvent event) {
        if (this.pressedKey == event.getKey()) {
            this.clicked = true;
            this.pressedKey = null;
        }
    }

    @Override
    public final void onPointerDown(final PointerEvent event) {
        if (this.pressedPointer == null &&
            this.contains(event.getX(), event.getY())) {
            this.pressedPointer = event.getPointerId();
        }
    }

    @Override
    public final void onPointerMoved(final PointerEvent event) {
        if (this.pressedPointer != null &&
            this.pressedPointer == event.getPointerId() &&
            !this.contains(event.getX(), event.getY())) {
            this.pressedPointer = null;
        }
    }

    @Override
    public final void onPointerUp(final PointerEvent event) {
        if (this.pressedPointer != null && this.pressedPointer == event.getPointerId()) {
            this.clicked = true;
            this.pressedPointer = null;
        }
    }

    public final void setText(final String text) {
        this.text = text;
    }
}
