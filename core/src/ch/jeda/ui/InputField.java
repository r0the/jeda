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

import ch.jeda.event.KeyEvent;
import ch.jeda.event.KeyTypedListener;
import ch.jeda.event.PointerDownListener;
import ch.jeda.event.PointerEvent;

/**
 * Represents a text field. A text field is a {@link ch.jeda.ui.Widget} that allows the user to enter text.
 *
 * @since 1.3
 * @version 2
 */
public abstract class InputField extends Widget implements KeyTypedListener, PointerDownListener {

    private static final char HIDE_CHAR = '*';
    private String displayText;
    private boolean inputHidden;
    private InputFieldStyle style;
    private String visibleText;

    /**
     * Constructs an input field at the specified position with the specified alignment.
     *
     * @param x the x coordinate of the text field
     * @param y the y coordinate of the text field
     * @param alignment specifies how to align the input field relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    protected InputField(final int x, final int y, final Alignment alignment) {
        super(x, y, alignment);
        this.inputHidden = false;
        this.style = Theme.getDefault().getDefaultInputFieldStyle();
    }

    @Override
    public final boolean contains(final int x, final int y) {
        return this.style.contains(this, x, y);
    }

    /**
     * Returns the text displayed in the input field.
     *
     * @return the text displayed in the input field
     *
     * @since 1.3
     */
    public final String getDisplayText() {
        return this.displayText;
    }

    @Override
    public final int getHeight() {
        return this.style.getHeight(this);
    }

    /**
     * Returns the current style of the text field.
     *
     * @return the current style of the text field
     *
     * @see #setStyle(ch.jeda.ui.InputFieldStyle)
     * @since 1.3
     */
    public final InputFieldStyle getStyle() {
        return this.style;
    }

    /**
     * Returns the part of the text currently visible in the text field.
     *
     * @return the part of the text currently visible in the text field
     *
     * @since 1.4
     */
    public final String getVisibleText() {
        return this.visibleText;
    }

    @Override
    public final int getWidth() {
        return this.style.getWidth(this);
    }

    /**
     * Checks if the user input is hidden in the input field.
     *
     * @return <tt>true</tt> if the user input is hidden in the input field, otherwise <tt>false</tt>
     *
     * @see #setInputHidden(boolean)
     * @since 1.4
     */
    public final boolean isInputHidden() {
        return this.inputHidden;
    }

    /**
     * Shows or hides the user input. If <tt>inputHidden</tt> is <tt>true</tt>, all entered characters are replaced by a
     * placeholde character in the input field. This is useful for entering passwords.
     *
     * @param inputHidden should the user input be hidden
     *
     * @see #isInputHidden()
     * @since 1.4
     */
    public void setInputHidden(final boolean inputHidden) {
        this.inputHidden = inputHidden;
        this.updateVisibleText();
    }

    /**
     * Set the specified style for the text field.
     *
     * @param style the style
     * @throws NullPointerException if <tt>style</tt> is <tt>null</tt>
     *
     * @see #getStyle()
     * @since 1.3
     */
    public final void setStyle(final InputFieldStyle style) {
        if (style == null) {
            throw new NullPointerException("style");
        }

        this.style = style;
    }

    @Override
    public final void onKeyTyped(final KeyEvent event) {
        if (!this.isSelected()) {
            return;
        }

        switch (event.getKey()) {
            case BACKSPACE:
                this.characterDeleted();
                event.consume();
                break;
            case UNDEFINED:
                this.characterTyped(event.getKeyChar());
                event.consume();
                break;
        }
    }

    @Override
    public void onPointerDown(final PointerEvent event) {
        if (this.contains(event.getX(), event.getY())) {
            this.select();
            event.consume();
        }
    }

    /**
     * This method is called when the user deletes a character.
     *
     * @since 1.3
     */
    protected abstract void characterDeleted();

    /**
     * This method is called when the user types a character.
     *
     * @param ch the typed character
     *
     * @since 1.3
     */
    protected abstract void characterTyped(char ch);

    @Override
    protected void draw(final Canvas canvas) {
        this.style.draw(this, canvas);
    }

    /**
     * Sets the text displayed in the input field.
     *
     * @param displayText the text displayed in the input field
     *
     * @since 1.3
     */
    protected final void setDisplayText(final String displayText) {
        this.displayText = displayText;
        this.updateVisibleText();
    }

    private void updateVisibleText() {
        final View view = this.getView();
        if (view != null) {
            final StringBuilder builder = new StringBuilder(this.displayBaseText());
            while (!this.style.fits(this, view.getCanvas(), builder.toString())) {
                builder.deleteCharAt(0);
            }

            this.visibleText = builder.toString();
        }
        else {
            this.visibleText = this.displayText;
        }
    }

    private String displayBaseText() {
        if (this.inputHidden) {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < this.displayText.length(); ++i) {
                result.append(HIDE_CHAR);
            }
            return result.toString();
        }
        else {
            return this.displayText;
        }
    }

}
