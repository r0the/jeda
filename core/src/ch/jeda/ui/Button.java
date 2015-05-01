/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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
 * @since 1.0
 * @version 2
 */
public class Button extends Widget implements KeyDownListener, KeyUpListener, PointerListener {

    private Key key;
    private boolean keyPressed;
    private Integer pointerId;
    private ButtonStyle style;
    private String text;

    /**
     * Constructs a button at the specified position.
     *
     * @param x the x coordinate of the button
     * @param y the y coordinate of the button
     *
     * @since 1.3
     */
    public Button(final int x, final int y) {
        this(x, y, Alignment.TOP_LEFT, null);
    }

    /**
     * Constructs a button at the specified position with the specified alignment.
     *
     * @param x the x coordinate of the button
     * @param y the y coordinate of the button
     * @param alignment specifies how to align the button relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public Button(final int x, final int y, final Alignment alignment) {
        this(x, y, alignment, null);
    }

    /**
     * Constructs a button at the specified position with the specified text.
     *
     * @param x the x coordinate of the button
     * @param y the y coordinate of the button
     * @param text the button text
     *
     * @since 1.3
     */
    public Button(final int x, final int y, final String text) {
        this(x, y, Alignment.TOP_LEFT, text);
    }

    /**
     * Constructs a button at the specified position with the specified alignment and text.
     *
     * @param x the x coordinate of the button
     * @param y the y coordinate of the button
     * @param alignment specifies how to align the button relative to (<tt>x</tt>, <tt>y</tt>)
     * @param text the button text
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public Button(final int x, final int y, final Alignment alignment, final String text) {
        super(x, y, alignment);
        this.key = Key.UNDEFINED;
        this.style = Theme.getDefault().getDefaultButtonStyle();
        this.text = text;
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
     * Returns the key associated with the button.
     *
     * @return the key associated with the button
     *
     * @see #setKey(ch.jeda.event.Key)
     * @since 1.3
     */
    public Key getKey() {
        return this.key;
    }

    /**
     * Returns the current style of the button.
     *
     * @return the current style of the button
     *
     * @see #setStyle(ch.jeda.ui.ButtonStyle)
     * @since 1.3
     */
    public final ButtonStyle getStyle() {
        return this.style;
    }

    /**
     * Returns the button text.
     *
     * @return the button text
     *
     * @since 1.0
     */
    public final String getText() {
        return this.text;
    }

    @Override
    public final int getWidth() {
        return this.style.getWidth(this);
    }

    /**
     * Checks if the widget is currently pressed.
     *
     * @return <tt>true</tt> if the widget is currently pressed, otherwise <tt>false</tt>
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
            event.consume();
        }
    }

    @Override
    public void onKeyUp(final KeyEvent event) {
        if (Key.UNDEFINED != this.key && event.getKey() == this.key && event.getSource() != this && this.keyPressed) {
            this.keyPressed = false;
            this.clicked();
            event.consume();
        }
    }

    @Override
    public void onPointerDown(final PointerEvent event) {
        if (this.pointerId == null && contains(event.getX(), event.getY())) {
            this.pointerId = event.getPointerId();
            this.select();
            this.sendKeyEvent(EventType.KEY_DOWN);
            event.consume();
        }
    }

    @Override
    public void onPointerMoved(final PointerEvent event) {
        if (this.pointerId != null && event.getPointerId() == this.pointerId) {
            if (contains(event.getX(), event.getY())) {
                event.consume();
            }
            else {
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
                this.clicked();
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
     * @since 1.3
     */
    public void setKey(final Key key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        this.key = key;
    }

    /**
     * Set the specified style for the button.
     *
     * @param style the style
     * @throws NullPointerException if <tt>style</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    public final void setStyle(final ButtonStyle style) {
        if (style == null) {
            throw new NullPointerException("style");
        }

        this.style = style;
    }

    /**
     * Sets the display text of the button
     *
     * @param text the display text of the button
     *
     * @see #getText()
     * @since 1.0
     */
    public final void setText(final String text) {
        this.text = text;
    }

    /**
     * This method is called when the user has clicked the button. Override this method to add behaviour.
     *
     * @since 1.3
     */
    protected void clicked() {
        this.action(this.text);
    }

    @Override
    protected void draw(final Canvas canvas) {
        this.style.draw(this, canvas);
    }

    private void sendKeyEvent(final EventType eventType) {
        final Window window = this.getWindow();
        if (this.key != Key.UNDEFINED && window != null) {
            window.postEvent(new KeyEvent(this, eventType, this.key));
        }
    }
}
