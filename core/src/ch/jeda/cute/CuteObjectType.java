/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.cute;

import ch.jeda.ui.Image;

public class CuteObjectType {

    public static final CuteObjectType EMPTY = new CuteObjectType();
    public static final CuteObjectType BOY = new CuteObjectType("BOY");
    public static final CuteObjectType CAT_GIRL = new CuteObjectType("CAT_GIRL");
    public static final CuteObjectType DEMON_GIRL = new CuteObjectType("DEMON_GIRL");
    public static final CuteObjectType PINK_GIRL = new CuteObjectType("PINK_GIRL");
    public static final CuteObjectType PRINCESS_GIRL = new CuteObjectType("PRINCESS_GIRL");
    public static final CuteObjectType BLUE_GEM = new CuteObjectType("BLUE_GEM");
    public static final CuteObjectType GREEN_GEM = new CuteObjectType("GREEN_GEM");
    public static final CuteObjectType ORANGE_GEM = new CuteObjectType("ORANGE_GEM");
    public static final CuteObjectType HEART = new CuteObjectType("HEART");
    public static final CuteObjectType STAR = new CuteObjectType("STAR");
    public static final CuteObjectType BIG_BUG = new CuteObjectType("BIG_BUG");
    public static final CuteObjectType SHORT_TREE = new CuteObjectType("SHORT_TREE");
    public static final CuteObjectType TALL_TREE = new CuteObjectType("TALL_TREE");
    public static final CuteObjectType UGLY_TREE = new CuteObjectType("UGLY_TREE");
    public static final CuteObjectType ROCK = new CuteObjectType("ROCK");
    public static final CuteObjectType KEY = new CuteObjectType("KEY");
    public static final CuteObjectType CLOSED_CHEST = new CuteObjectType("CLOSED_CHEST");
    public static final CuteObjectType OPEN_CHEST = new CuteObjectType("OPEN_CHEST");
    public static final CuteObjectType CLOSED_DOOR = new CuteObjectType("CLOSED_DOOR");
    public static final CuteObjectType OPEN_DOOR = new CuteObjectType("OPEN_DOOR");
    public static final CuteObjectType SELECTOR = new CuteObjectType("SELECTOR");
    private final Image image;
    private final String name;

    public CuteObjectType(final String name) {
        this.image = Cute.loadImage(name.toLowerCase());
        this.name = name;
    }

    private CuteObjectType() {
        this.image = null;
        this.name = "EMPTY";
    }

    public final Image getImage() {
        return this.image;
    }

    @Override
    public final String toString() {
        return this.name;
    }
}
