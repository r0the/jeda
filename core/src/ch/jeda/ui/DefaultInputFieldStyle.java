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

/**
 * The default Jeda input field style. The graphical representation of an input field is given by an image for the
 * background and an image for the text cursor.
 *
 * @since 1.3
 */
public class DefaultInputFieldStyle extends DefaultTextStyle implements InputFieldStyle {

    private static final String ANTIQUE = "antique";
    private static final String MODERN = "modern";
    /**
     * The Jeda antique input field style in beige.
     *
     * @since 1.3
     */
    public static final InputFieldStyle ANTIQUE_BEIGE = new DefaultInputFieldStyle(ANTIQUE);
    /**
     * The Jeda modern input field style in white.
     *
     * @since 1.3
     */
    public static final InputFieldStyle MODERN_WHITE = new DefaultInputFieldStyle(MODERN);

    private static final int BORDER = 10;
    private final Image background;
    private final Image cursor;

    private DefaultInputFieldStyle(final String type) {
        this(new Image("res:jeda/ui/" + type + "_input_field.png"),
             new Image("res:jeda/ui/" + type + "_input_field_cursor.png"));
        if (ANTIQUE.equals(type)) {
            setTypeface(Typeface.SERIF);
        }
    }

    /**
     * Constructs a new default input field style with the specified images for background and cursor.
     *
     * @param background the image for background
     * @param cursor the image for cursor
     * @throws NullPointerException if <tt>background</tt> or <tt>cursor</tt> are <tt>null</tt>
     *
     * @since 1.3
     */
    public DefaultInputFieldStyle(final Image background, final Image cursor) {
        if (background == null) {
            throw new NullPointerException("background");
        }

        if (cursor == null) {
            throw new NullPointerException("cursor");
        }

        this.background = background;
        this.cursor = cursor;
    }

    @Override
    public boolean contains(final InputField inputField, final int x, final int y) {
        final int dx = inputField.getCenterX() - x;
        final int dy = inputField.getCenterY() - y;
        return Math.abs(dx) <= getWidth(inputField) / 2 && Math.abs(dy) <= getHeight(inputField) / 2;
    }

    @Override
    public void draw(final InputField inputField, final String visibleText, final Canvas canvas) {
        applyTextStyle(canvas);
        canvas.drawImage(inputField.getLeft(), inputField.getTop(), background);
        canvas.drawText(inputField.getLeft() + BORDER, inputField.getCenterY(), visibleText, Alignment.LEFT);
        if (inputField.isSelected()) {
            canvas.drawImage(inputField.getLeft() + BORDER + canvas.textWidth(visibleText), inputField.getCenterY(),
                             cursor, Alignment.CENTER);
        }
    }

    @Override
    public boolean fits(final InputField inputField, final Canvas canvas, final String text) {
        final int maxWidth = getWidth(inputField) - 2 * BORDER;
        canvas.setTypeface(getTypeface());
        canvas.setTextSize(getTextSize());
        return canvas.textWidth(text) <= maxWidth;
    }

    @Override
    public int getHeight(final InputField inputField) {
        return background.getHeight();
    }

    @Override
    public int getWidth(final InputField inputField) {
        return background.getWidth();
    }

}
