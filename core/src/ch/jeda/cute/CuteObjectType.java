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
    public static final CuteObjectType BOY = new CuteObjectType("Boy");
    public static final CuteObjectType CAT_GIRL = new CuteObjectType("CatGirl");
    public static final CuteObjectType DEMON_GIRL = new CuteObjectType("DemonGirl");
    public static final CuteObjectType PINK_GIRL = new CuteObjectType("PinkGirl");
    public static final CuteObjectType PRINCESS_GIRL = new CuteObjectType("PrincessGirl");
    public static final CuteObjectType BLUE_GEM = new CuteObjectType("BlueGem");
    public static final CuteObjectType GREEN_GEM = new CuteObjectType("GreenGem");
    public static final CuteObjectType ORANGE_GEM = new CuteObjectType("OrangeGem");
    public static final CuteObjectType HEART = new CuteObjectType("Heart");
    public static final CuteObjectType STAR = new CuteObjectType("Star");
    public static final CuteObjectType BUG = new CuteObjectType("Bug");
    public static final CuteObjectType SHORT_TREE = new CuteObjectType("ShortTree");
    public static final CuteObjectType TALL_TREE = new CuteObjectType("TallTree");
    public static final CuteObjectType UGLY_TREE = new CuteObjectType("UglyTree");
    public static final CuteObjectType ROCK = new CuteObjectType("Rock");
    public static final CuteObjectType KEY = new CuteObjectType("Key");
    public static final CuteObjectType CLOSED_CHEST = new CuteObjectType("ClosedChest");
    public static final CuteObjectType OPEN_CHEST = new CuteObjectType("OpenChest");
    public static final CuteObjectType CLOSED_DOOR = new CuteObjectType("ClosedDoor");
    public static final CuteObjectType OPEN_DOOR = new CuteObjectType("OpenDoor");
    public static final CuteObjectType SELECTOR = new CuteObjectType("Selector");
    private final Image image;

    public CuteObjectType(final String imagePath) {
        this.image = Cute.loadImage(imagePath);
    }

    private CuteObjectType() {
        this.image = null;
    }

    public final Image getImage() {
        return this.image;
    }
}
