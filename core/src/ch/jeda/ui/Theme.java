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

/**
 * Represents a Jeda widget theme. A widget theme defines the default styles for each widget type. When created, every
 * widget assumes the style defined by the default theme.
 *
 * @since 1.3
 */
public final class Theme {

    private ButtonStyle buttonStyle;
    private CheckBoxStyle checkBoxStyle;
    private InputFieldStyle inputFieldStyle;
    private ProgressBarStyle progressBarStyle;
    /**
     * The Jeda antique widget theme.
     *
     * @since 1.3
     */
    public static final Theme ANTIQUE = new Theme(
        DefaultButtonStyle.ANTIQUE_BEIGE,
        DefaultCheckBoxStyle.ANTIQUE_BEIGE,
        DefaultInputFieldStyle.ANTIQUE_BEIGE,
        DefaultProgressBarStyle.ANTIQUE_GREEN);
    /**
     * The Jeda modern widget theme.
     *
     * @since 1.3
     */
    public static final Theme MODERN = new Theme(
        DefaultButtonStyle.MODERN_GREY,
        DefaultCheckBoxStyle.MODERN_GREEN,
        DefaultInputFieldStyle.MODERN_WHITE,
        DefaultProgressBarStyle.MODERN_GREEN);
    private static Theme defaultTheme = MODERN;

    /**
     * Returns the default widget theme.
     *
     * @return the default widget theme
     *
     * @see #setDefault(ch.jeda.ui.Theme)
     * @since 1.3
     */
    public static Theme getDefault() {
        return defaultTheme;
    }

    /**
     * Sets the default widget theme.
     *
     * @param theme the default widget theme
     *
     * @see #getDefault()
     * @since 1.3
     */
    public static void setDefault(final Theme theme) {
        defaultTheme = theme;
    }

    private Theme(final ButtonStyle buttonStyle, final CheckBoxStyle checkBoxStyle,
                  final InputFieldStyle inputFieldStyle, final ProgressBarStyle progressBarStyle) {
        this.buttonStyle = buttonStyle;
        this.checkBoxStyle = checkBoxStyle;
        this.inputFieldStyle = inputFieldStyle;
        this.progressBarStyle = progressBarStyle;
    }

    /**
     * Returns the default button style of the theme.
     *
     * @return the default button style
     *
     * @since 1.3
     */
    public ButtonStyle getDefaultButtonStyle() {
        return buttonStyle;
    }

    /**
     * Returns the default check box of the theme.
     *
     * @return the default check box style
     *
     * @since 1.3
     */
    public CheckBoxStyle getDefaultCheckBoxStyle() {
        return checkBoxStyle;
    }

    /**
     * Returns the default input field style of the theme.
     *
     * @return the default input field style
     *
     * @since 1.3
     */
    public InputFieldStyle getDefaultInputFieldStyle() {
        return inputFieldStyle;
    }

    /**
     * Returns the default progress bar style of the theme.
     *
     * @return the default progress bar style
     *
     * @since 1.3
     */
    public ProgressBarStyle getDefaultProgressBarStyle() {
        return progressBarStyle;
    }

    /**
     * Sets the default button style of the theme.
     *
     * @param buttonStyle the default button style
     *
     * @since 1.3
     */
    public void setButtonStyle(final ButtonStyle buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    /**
     * Sets the default check box style of the theme.
     *
     * @param checkBoxStyle the default check box style
     *
     * @since 1.3
     */
    public void setCheckBoxStyle(final CheckBoxStyle checkBoxStyle) {
        this.checkBoxStyle = checkBoxStyle;
    }

    /**
     * Sets the default input field style of the theme.
     *
     * @param inputFieldStyle the default input field style
     *
     * @since 1.3
     */
    public void setInputFieldStyle(final InputFieldStyle inputFieldStyle) {
        this.inputFieldStyle = inputFieldStyle;
    }

    /**
     * Sets the default progress base style of the theme.
     *
     * @param progressBarStyle the default progress base style
     *
     * @since 1.3
     */
    public void setProgressBarStyle(final ProgressBarStyle progressBarStyle) {
        this.progressBarStyle = progressBarStyle;
    }
}
