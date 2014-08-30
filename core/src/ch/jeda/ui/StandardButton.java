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
 * @since 1.3
 */
public enum StandardButton {

    /**
     * @since 1.3
     */
    CANCEL,
    /**
     * @since 1.3
     */
    DOWN(true),
    /**
     * @since 1.3
     */
    FAVOURITE,
    /**
     * @since 1.3
     */
    KEY,
    /**
     * @since 1.3
     */
    LEFT(true),
    /**
     * @since 1.3
     */
    MENU,
    /**
     * @since 1.3
     */
    OK,
    /**
     * @since 1.3
     */
    PAUSE,
    /**
     * @since 1.3
     */
    PLAY,
    /**
     * @since 1.3
     */
    RIGHT(true),
    /**
     * @since 1.3
     */
    SAVE,
    /**
     * @since 1.3
     */
    UP(true);
    private final boolean circular;

    Image getImage() {
        return new Image("res:jeda/ui/" + this.name().toLowerCase() + "_button.png");
    }

    Image getPressedImage() {
        return new Image("res:jeda/ui/" + this.name().toLowerCase() + "_button_pressed.png");
    }

    boolean isCircular() {
        return this.circular;
    }

    private StandardButton() {
        this(false);
    }

    private StandardButton(final boolean circular) {
        this.circular = circular;
    }
}
