/*
 * Copyright (C) 2015 by Stefan Rothe
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
 * Represents a button. A button is a {@link ch.jeda.ui.Widget} that allows the user to trigger an action by clicking on
 * it.
 *
 * @since 2.0
 */
public class ActionButton extends Widget implements KeyDownListener, KeyUpListener, PointerListener {

    private Icon icon;
    private Key key;
    private boolean keyPressed;
    private Integer pointerId;
    private float radius;

    /**
     * Constructs a new action button with center alignment.
     *
     * @param x the horizontal canvas coordinate of this button's center
     * @param y the vertical canvas coordinate of this button's center
     * @param icon the icon
     *
     * @since 2.0
     */
    public ActionButton(final double x, final double y, final Icon icon) {
        this(x, y, icon, Alignment.CENTER);
    }

    /**
     * Constructs a new action button.
     *
     * @param x the horizontal canvas coordinate of this button's alignment point
     * @param y the vertical canvas coordinate of this button's alignment point
     * @param icon the icon
     * @param alignment the alignment of this button
     *
     * @since 2.0
     */
    public ActionButton(final double x, final double y, final Icon icon, final Alignment alignment) {
        super((float) x, (float) y, alignment);
        this.icon = icon;
        setName(icon.name());
        key = Key.UNDEFINED;
        radius = 0.5f;
    }

    @Override
    public float getHeight() {
        return 2f * radius;
    }

    /**
     * Returns the key associated with the button.
     *
     * @return the key associated with the button
     *
     * @see #setKey(ch.jeda.event.Key)
     * @since 2.0
     */
    public Key getKey() {
        return key;
    }

    @Override
    public float getWidth() {
        return 2f * radius;
    }

    /**
     * Checks if the widget is currently pressed.
     *
     * @return <tt>true</tt> if the widget is currently pressed, otherwise <tt>false</tt>
     *
     * @since 2.0
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
            clicked();
            event.consume();
        }
    }

    @Override
    public void onPointerDown(final PointerEvent event) {
        if (pointerId == null && contains(event.getCanvasX(), event.getCanvasY())) {
            pointerId = event.getPointerId();
            select();
            sendKeyEvent(EventType.KEY_DOWN);
            event.consume();
        }
    }

    @Override
    public void onPointerMoved(final PointerEvent event) {
        if (pointerId != null && event.getPointerId() == pointerId) {
            if (contains(event.getCanvasX(), event.getCanvasY())) {
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
            if (contains(event.getCanvasX(), event.getCanvasY())) {
                clicked();
                event.consume();
            }
        }
    }

    /**
     * Associates a key with the button. Associating a key with the button makes the button a virtual copy of the key:
     * The button will be pressed and released synchronously with the key. Pressing and releasing the button will
     * generate the corresponding key events.
     *
     * @param key the key to associate with the button.
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     *
     * @see #getKey()
     * @since 2.0
     */
    public void setKey(final Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        this.key = key;
    }

    /**
     * This method is called when the user has clicked the button. Override this method to add behaviour.
     *
     * @since 2.0
     */
    protected void clicked() {
        triggerAction();
    }

    @Override
    protected boolean containsLocal(final float x, final float y) {
        final float dx = getCenterX() - x;
        final float dy = getCenterY() - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    protected void draw(final Canvas canvas) {
        final float cx = getCenterX();
        final float cy = getCenterY();
        applyStyle(canvas);
        canvas.fillCircle(cx, cy, radius);
        if (!isPressed()) {
            canvas.drawShadowCircle(cx, cy, radius);
        }

        canvas.drawIcon(cx, cy, icon);
    }

    private void sendKeyEvent(final EventType eventType) {
        final View view = getView();
        if (key != Key.UNDEFINED && view != null) {
            view.postEvent(new KeyEvent(this, eventType, key));
        }
    }
}
