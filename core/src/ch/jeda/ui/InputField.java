/*
 * Copyright (C) 2014 - 2016 by Stefan Rothe
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
 * @version 3
 */
public abstract class InputField extends TextWidget implements KeyTypedListener, PointerDownListener {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.GRAY;
    private static final float DEFAULT_HEIGHT = 48f;
    private static final Color DEFAULT_HIGHLIGHT_COLOR = Color.PINK_A400;
    private static final float DEFAULT_WIDTH = 200f;
    private static final float DEFAULT_TEXT_SIZE = 16f;
    private static final char HIDE_CHAR = '\u2022';
    private String displayText;
    private Color highlightColor;
    private String hintText;
    private boolean inputHidden;
    private String visibleText;

    /**
     * Constructs an input field at the specified position with the specified alignment.
     *
     * @param x the horizontal coordinate of this input field
     * @param y the vertical coordinate of this input field
     * @param alignment specifies how to align the input field relative to (<tt>x</tt>, <tt>y</tt>)
     * @throws NullPointerException if <tt>alignment</tt> is <tt>null</tt>
     *
     * @since 1.3
     */
    protected InputField(final double x, final double y, final Alignment alignment) {
        super(x, y, alignment);
        resize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTextSize(DEFAULT_TEXT_SIZE);
        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        highlightColor = DEFAULT_HIGHLIGHT_COLOR;
        displayText = "";
        hintText = "";
        inputHidden = false;
    }

    /**
     * Returns the text displayed in this input field.
     *
     * @return the text displayed in this input field
     *
     * @since 1.3
     */
    public final String getDisplayText() {
        return displayText;
    }

    /**
     * Returns the highlight color of this input field.
     *
     * @return the highlight color
     *
     * @since 2.0
     */
    public final Color getHighlightColor() {
        return highlightColor;
    }

    /**
     * Returns the hint text for this input field.
     *
     * @return the hint text for this input field
     *
     * @since 2.0
     */
    public final String getHintText() {
        return hintText;
    }

    /**
     * Checks if the user input is hidden in this input field.
     *
     * @return <tt>true</tt> if the user input is hidden in the input field, otherwise <tt>false</tt>
     *
     * @see #setInputHidden(boolean)
     * @since 1.4
     */
    public final boolean isInputHidden() {
        return inputHidden;
    }

    /**
     * Sets the hightlight color for this input field.
     *
     * @param highlightColor the hightlight color
     *
     * @since 2.0
     */
    public final void setHighlightColor(final Color highlightColor) {
        this.highlightColor = highlightColor;
    }

    /**
     * Sets the hint text for this input fied.
     *
     * @param hintText the hint text
     *
     * @since 2.0
     */
    public final void setHintText(final String hintText) {
        this.hintText = hintText;
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
    public final void setInputHidden(final boolean inputHidden) {
        this.inputHidden = inputHidden;
        visibleText = null;
    }

    @Override
    public final void onKeyTyped(final KeyEvent event) {
        if (!isSelected()) {
            return;
        }

        switch (event.getKey()) {
            case BACKSPACE:
                characterDeleted();
                event.consume();
                break;
            case UNDEFINED:
                characterTyped(event.getKeyChar());
                event.consume();
                break;
        }
    }

    @Override
    public final void onPointerDown(final PointerEvent event) {
        if (contains(event.getX(), event.getY())) {
            select();
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
    protected abstract void characterTyped(final char ch);

    @Override
    protected void draw(final Canvas canvas) {
        applyStyle(canvas);
        if (visibleText == null) {
            updateVisibleText(canvas);
        }

        if (isSelected()) {
            canvas.setLineWidth(2);
            canvas.setColor(highlightColor);
        }
        else {
            canvas.setLineWidth(1);
            canvas.setColor(getBackgroundColor());
        }

        final float dividerY = getBottom() + 8;
        final float endX = canvas.measureLength(visibleText, getTypeface(), getTextSize());
        canvas.drawPolyline(getLeft(), dividerY, getRight(), dividerY);
        canvas.setAlignment(Alignment.BOTTOM_LEFT);
        final float textY = getBottom() + 16;
        if (visibleText == null || visibleText.isEmpty()) {
            canvas.setColor(getBackgroundColor());
            canvas.drawText(getLeft(), textY, hintText);
        }
        else {
            if (isSelected()) {
                // Draw text cursor
                canvas.drawPolyline(endX, textY, endX, textY + getTextSize());
            }

            canvas.setColor(getTextColor());
            canvas.drawText(getLeft(), textY, visibleText);
        }
    }

    /**
     * Sets the text displayed in this input field.
     *
     * @param displayText the text displayed in the input field
     *
     * @since 1.3
     */
    protected final void setDisplayText(final String displayText) {
        if (displayText == null) {
            this.displayText = "";
        }
        else {
            this.displayText = displayText;
        }

        visibleText = null;
    }

    private void updateVisibleText(final Canvas canvas) {
        final View view = getView();
        if (view != null) {
            final StringBuilder builder = new StringBuilder(displayBaseText());
            while (!fits(canvas, builder.toString())) {
                builder.deleteCharAt(0);
            }

            visibleText = builder.toString();
        }
        else {
            visibleText = displayText;
        }
    }

    private boolean fits(final Canvas canvas, final String text) {
        final float maxWidth = getWidth();
        return canvas.measureLength(text, getTypeface(), getTextSize()) <= maxWidth;
    }

    private String displayBaseText() {
        if (inputHidden) {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < displayText.length(); ++i) {
                result.append(HIDE_CHAR);
            }
            return result.toString();
        }
        else {
            return displayText;
        }
    }
}
