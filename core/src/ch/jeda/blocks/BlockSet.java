/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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
package ch.jeda.blocks;

import ch.jeda.Direction;
import ch.jeda.Engine;
import ch.jeda.Properties;
import ch.jeda.ui.Image;
import java.util.HashMap;
import java.util.Map;

final class BlockSet {

    public static final String BROWN_BLOCK = "BrownBlock";
    public static final String DIRT_BLOCK = "DirtBlock";
    public static final String GRASS_BLOCK = "GrassBlock";
    public static final String PLAIN_BLOCK = "PlainBlock";
    public static final String STONE_BLOCK = "StoneBlock";
    public static final String WALL_BLOCK = "WallBlock";
    public static final String WATER_BLOCK = "WaterBlock";
    public static final String WOOD_BLOCK = "WoodBlock";
    public static final String BOY = "Boy";
    public static final String CAT_GIRL = "CatGirl";
    public static final String DEMON_GIRL = "DemonGirl";
    public static final String PINK_GIRL = "PinkGirl";
    public static final String PRINCESS_GIRL = "PrincessGirl";
    public static final String BLUE_GEM = "BlueGem";
    public static final String GREEM_GEM = "GreenGem";
    public static final String ORANGE_GEM = "OrangeGem";
    public static final String HEART = "Heart";
    public static final String STAR = "Star";
    public static final String BUG = "Bug";
    public static final String SHORT_TREE = "ShortTree";
    public static final String TALL_TREE = "TallTree";
    public static final String UGLY_TREE = "UglyTree";
    public static final String ROCK = "Rock";
    public static final String KEY = "Key";
    public static final String CLOSED_CHEST = "ClosedChest";
    public static final String OPEN_CHEST = "OpenChest";
    public static final String CLOSED_DOOR = "ClosedDoor";
    public static final String OPEN_DOOR = "OpenDoor";
    public static final String SELECTOR = "Selector";
    private static final String IMAGE_EXTENSION = ".png";
    private final Map<String, Image> blockMap;
    private final Image defaultBlock;
    private final String imageBasePath;
    private final Map<Direction, Image> shadows;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final Image speechBubble;
    private static final String[] DEFAULT_BLOCKS = {
        BROWN_BLOCK, DIRT_BLOCK, GRASS_BLOCK, PLAIN_BLOCK, STONE_BLOCK,
        WALL_BLOCK, WATER_BLOCK, WOOD_BLOCK,
        BOY, CAT_GIRL, DEMON_GIRL, PINK_GIRL, PRINCESS_GIRL,
        BUG, SHORT_TREE, TALL_TREE, UGLY_TREE, ROCK,
        BLUE_GEM, GREEM_GEM, ORANGE_GEM, HEART, STAR,
        KEY, CLOSED_CHEST, OPEN_CHEST, CLOSED_DOOR, OPEN_DOOR, SELECTOR
    };

    BlockSet() {
        this.blockMap = new HashMap();
        Properties p = Engine.getContext().getProperties();
        this.sizeX = p.getInt("ch.jeda.blocks.sizex", 1);
        this.sizeY = p.getInt("ch.jeda.blocks.sizey", 1);
        this.sizeZ = p.getInt("ch.jeda.blocks.sizez", 1);
        this.imageBasePath = p.getString("ch.jeda.blocks.imagedir");

        for (String name : DEFAULT_BLOCKS) {
            this.addBlock(name);
        }

        this.defaultBlock = getBlockImage(PLAIN_BLOCK);
        this.shadows = new HashMap();
        for (Direction direction : Direction.ALL) {
            this.addShadow(direction);
        }

        this.speechBubble = this.loadImage("SpeechBubble");
    }

    void addBlockImage(String name, Image image) {
        this.blockMap.put(name, image);
    }

    Image getBlockImage(String brickName) {
        if (this.blockMap.containsKey(brickName)) {
            return this.blockMap.get(brickName);
        }
        else {
            return this.defaultBlock;
        }
    }

    Image getShadowImage(Direction direction) {
        return this.shadows.get(direction);
    }

    Image getSpeechBubble() {
        return this.speechBubble;
    }

    int brickSizeX() {
        return this.sizeX;
    }

    int brickSizeY() {
        return this.sizeY;
    }

    int brickSizeZ() {
        return this.sizeZ;
    }

    private void addBlock(String name) {
        this.addBlockImage(name, this.loadImage(name));
    }

    private void addShadow(Direction direction) {
        this.shadows.put(direction, this.loadImage("Shadow" + direction.toString()));
    }

    private Image loadImage(String name) {
        return new Image(this.imageBasePath + "/" + name + IMAGE_EXTENSION);
    }
}
