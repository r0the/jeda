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
 * The default Jeda progress bar style. The graphical representation of a progress bar is given by a background image
 * and an image for the full progress.
 *
 * @since 1.3
 */
public class DefaultProgressBarStyle implements ProgressBarStyle {

    /**
     * The Jeda antique progress bar style in blue.
     *
     * @since 1.3
     */
    public static final ProgressBarStyle ANTIQUE_BLUE = new DefaultProgressBarStyle("antique", "blue");
    /**
     * The Jeda antique progress bar style in green.
     *
     * @since 1.3
     */
    public static final ProgressBarStyle ANTIQUE_GREEN = new DefaultProgressBarStyle("antique", "green");
    /**
     * The Jeda antique progress bar style in red.
     *
     * @since 1.3
     */
    public static final ProgressBarStyle ANTIQUE_RED = new DefaultProgressBarStyle("antique", "red");
    /**
     * The Jeda antique progress bar style in yellow.
     *
     * @since 1.3
     */
    public static final ProgressBarStyle ANTIQUE_YELLOW = new DefaultProgressBarStyle("antique", "yellow");
    /**
     * The Jeda modern progress bar style in blue.
     *
     * @since 1.3
     */
    public static final ProgressBarStyle MODERN_BLUE = new DefaultProgressBarStyle("modern", "blue");
    /**
     * The Jeda modern progress bar style in green.
     *
     * @since 1.3
     */
    public static final ProgressBarStyle MODERN_GREEN = new DefaultProgressBarStyle("modern", "green");
    /**
     * The Jeda modern progress bar style in red.
     *
     * @since 1.3
     */
    public static final ProgressBarStyle MODERN_RED = new DefaultProgressBarStyle("modern", "red");
    /**
     * The Jeda modern progress bar style in yellow.
     *
     * @since 1.3
     */
    public static final ProgressBarStyle MODERN_YELLOW = new DefaultProgressBarStyle("modern", "yellow");

    private final Image background;
    private final Image foreground;

    private DefaultProgressBarStyle(final String type, final String color) {
        this(new Image("res:jeda/ui/" + type + "_progress_bar_" + color + ".png"),
             new Image("res:jeda/ui/" + type + "_progress_bar.png"));
    }

    /**
     * Constructs a new default progress bar style with the specified foreground and background images.
     *
     * @param foreground the foreground image
     * @param background the background image
     * @throws NullPointerException if <tt>foreground</tt> or <tt>background</tt> are <tt>null</tt>
     *
     * @since 1.3
     */
    public DefaultProgressBarStyle(final Image foreground, final Image background) {
        if (foreground == null) {
            throw new NullPointerException("foreground");
        }

        if (background == null) {
            throw new NullPointerException("background");
        }

        this.background = background;
        this.foreground = foreground;
    }

    @Override
    public boolean contains(final ProgressBar progressBar, final int x, final int y) {
        final int dx = progressBar.getCenterX() - x;
        final int dy = progressBar.getCenterY() - y;
        return Math.abs(dx) <= getWidth(progressBar) / 2 && Math.abs(dy) <= getHeight(progressBar) / 2;
    }

    @Override
    public void draw(final ProgressBar progressBar, final Canvas canvas) {
        canvas.drawImage(progressBar.getLeft(), progressBar.getTop(), background);
        final double percent = progressBar.getProgress();
        int width = (int) (getWidth(progressBar) * percent);
        if (width > 0) {
            canvas.drawImage(progressBar.getLeft(), progressBar.getTop(),
                             foreground.subImage(0, 0, width, getHeight(progressBar)));
        }
    }

    @Override
    public int getHeight(final ProgressBar progressBar) {
        return background.getHeight();
    }

    @Override
    public int getWidth(final ProgressBar progressBar) {
        return background.getWidth();
    }
}
