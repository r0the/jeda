/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
     * @param alignment specifies how to align the check box relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public CheckBox(final int x, final int y, final Alignment alignment) {
        super(x, y, alignment);
        key = Key.UNDEFINED;
        style = Theme.getDefault().getDefaultCheckBoxStyle();
    }

    @Override
    public final boolean contains(final float x, final float y) {
        return style.contains(this, x, y);
    }

    @Override
    public final int getHeight() {
        return style.getHeight(this);
    }

    /**
     * Returns the current hot key of the check box.
     *
     * @return the current hot key of the check box
     *
     * @since 1.3
     */
    public final Key getKey() {
        return key;
    }

    /**
     * Returns the current style of the check box.
     *
     * @return the current style of the check box
     *
     * @since 1.3
     */
    public final CheckBoxStyle getStyle() {
        return style;
    }

    @Override
    public final int getWidth() {
        return style.getWidth(this);
    }

    /**
     * Returns the current checked state of the check box.
     *
     * @return the current checked state of the check box
     *
     * @since 1.3
     */
    public final boolean isChecked() {
        return checked;
    }

    /**
     * Checks if the check box is currently pressed.
     *
     * @return <tt>true</tt> if the check box is currently pressed, otherwise <tt>false</tt>
     *
     * @since 1.3
     */
    public final boolean isPressed() {
        return keyPressed || pointerId != null;
    }

    @Override
    public void onKeyDown(final KeyEvent event) {
        if (Key.UNDEFINED != key && event.getKey() == key && event.getSource() != this && !keyPressed) {
            keyPressed = true;
            select();
            event.consume();
        }
    }

    @Override
    public void onKeyUp(final KeyEvent event) {
        if (Key.UNDEFINED != key && event.getKey() == key && event.getSource() != this && keyPressed) {
            keyPressed = false;
            toggle();
            event.consume();
        }
    }

    @Override
    public void onPointerDown(final PointerEvent event) {
        if (pointerId == null && contains(event.getX(), event.getY())) {
            pointerId = event.getPointerId();
            select();
            sendKeyEvent(EventType.KEY_DOWN);
            event.consume();
        }
    }

    @Override
    public void onPointerMoved(final PointerEvent event) {
        if (pointerId != null && event.getPointerId() == pointerId) {
            if (contains(event.getX(), event.getY())) {
                event.consume();
            }
            else {
                pointerId = null;
                sendKeyEvent(EventType.KEY_UP);
            }
        }
    }

    @Override
    public void onPointerUp(final PointerEvent event) {
        if (pointerId != null && event.getPointerId() == pointerId) {
            pointerId = null;
            sendKeyEvent(EventType.KEY_UP);
            if (contains(event.getX(), event.getY())) {
                toggle();
                event.consume();
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
        checked = !checked;
    }

    @Override
    protected void draw(final Canvas canvas) {
        style.draw(this, canvas);
    }

    private void sendKeyEvent(final EventType eventType) {
        final View view = getView();
        if (key != Key.UNDEFINED && view != null) {
            view.postEvent(new KeyEvent(this, eventType, key));
        }
    }
}
