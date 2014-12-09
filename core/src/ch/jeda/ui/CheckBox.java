/*
 * Copyright (C) 2014 by Stefan Rothe
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

import ch.jeda.event.EventType;
import ch.jeda.event.Key;
import ch.jeda.event.KeyDownListener;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.KeyUpListener;
import ch.jeda.event.PointerEvent;
import ch.jeda.event.PointerListener;

/**
 * Represents a check box. A check box is a {@link ch.jeda.ui.Widget} that allows the user to control a dual state, e.g.
 * to turn on or off an option. The state of the check box can be changed by clicking on it or by typing the specified
 * hot key.
 *
 * @since 1.3
 */
public class CheckBox extends Widget implements KeyDownListener, KeyUpListener, PointerListener {

    private boolean checked;
    private Key key;
    private boolean keyPressed;
    private Integer pointerId;
    private CheckBoxStyle style;

    /**
     * Constructs a check box at the specified position.
     *
     * @param x the x coordinate of the check box
     * @param y the y coordinate of the check box
     *
     * @since 1.3
     */
    public CheckBox(final int x, final int y) {
        this(x, y, Alignment.TOP_LEFT);
    }

    /**
     * Constructs a check box at the specified position with the specified alignment.
     *
     * @param x the x coordinate of the check box
     * @param y the y coordinate of the check box
     * @param alignment the alignment of the check box
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public CheckBox(final int x, final int y, final Alignment alignment) {
        super(x, y, alignment);
        this.key = Key.UNDEFINED;
        this.style = Theme.getDefault().getDefaultCheckBoxStyle();
    }

    @Override
    public final boolean contains(final int x, final int y) {
        return this.style.contains(this, x, y);
    }

    @Override
    public final int getHeight() {
        return this.style.getHeight(this);
    }

    /**
     * Returns the current hot key of the check box.
     *
     * @return the current hot key of the check box
     *
     * @since 1.3
     */
    public final Key getKey() {
        return this.key;
    }

    /**
     * Returns the current style of the check box.
     *
     * @return the current style of the check box
     *
     * @since 1.3
     */
    public final CheckBoxStyle getStyle() {
        return this.style;
    }

    @Override
    public final int getWidth() {
        return this.style.getWidth(this);
    }

    /**
     * Returns the current checked state of the check box.
     *
     * @return the current checked state of the check box
     *
     * @since 1.3
     */
    public final boolean isChecked() {
        return this.checked;
    }

    /**
     * Checks if the check box is currently pressed.
     *
     * @return <tt>true</tt> if the check box is currently pressed, otherwise <tt>false</tt>
     *
     * @since 1.3
     */
    public final boolean isPressed() {
        return this.keyPressed || this.pointerId != null;
    }

    @Override
    public void onKeyDown(final KeyEvent event) {
        if (Key.UNDEFINED != this.key && event.getKey() == this.key && event.getSource() != this && !this.keyPressed) {
            this.keyPressed = true;
            this.select();
        }
    }

    @Override
    public void onKeyUp(final KeyEvent event) {
        if (Key.UNDEFINED != this.key && event.getKey() == this.key && event.getSource() != this && this.keyPressed) {
            this.keyPressed = false;
            this.toggle();
        }
    }

    @Override
    public void onPointerDown(final PointerEvent event) {
        if (this.pointerId == null && contains(event.getX(), event.getY())) {
            this.pointerId = event.getPointerId();
            this.select();
            this.sendKeyEvent(EventType.KEY_DOWN);
        }
    }

    @Override
    public void onPointerMoved(final PointerEvent event) {
        if (this.pointerId != null && event.getPointerId() == this.pointerId) {
            if (!contains(event.getX(), event.getY())) {
                this.pointerId = null;
                this.sendKeyEvent(EventType.KEY_UP);
            }
        }
    }

    @Override
    public void onPointerUp(final PointerEvent event) {
        if (this.pointerId != null && event.getPointerId() == this.pointerId) {
            this.pointerId = null;
            this.sendKeyEvent(EventType.KEY_UP);
            if (this.contains(event.getX(), event.getY())) {
                this.toggle();
            }
        }
    }

    /**
     * Sets the checked state of the check box.
     *
     * @param checked the checked state of the check box
     *
     * @since 1.3
     */
    public final void setChecked(final boolean checked) {
        this.checked = checked;
    }

    /**
     * Sets the hot key for this check box.
     *
     * @param key the hot key for this check box
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public final void setKey(final Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        this.key = key;
    }

    /**
     * Set the specified style for the check box.
     *
     * @param style the style
     * @throws NullPointerException if <tt>style</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public final void setStyle(final CheckBoxStyle style) {
        if (style == null) {
            throw new NullPointerException("style");
        }

        this.style = style;
    }

    /**
     * Toggles the checked state of the check box.
     *
     * @since 1.3
     */
    public final void toggle() {
        this.checked = !this.checked;
    }

    @Override
    protected void draw(final Canvas canvas) {
        this.style.draw(this, canvas);
    }

    private void sendKeyEvent(final EventType eventType) {
        final Window window = this.getView();
        if (this.key != Key.UNDEFINED && window != null) {
            window.postEvent(new KeyEvent(this, eventType, this.key));
        }
    }
}
