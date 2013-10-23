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

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Image;

public abstract class Block {

    public static final Block BROWN = new Block.DefaultBlock("BrownBlock");
    public static final Block DIRT = new Block.DefaultBlock("DirtBlock");
    public static final Block EMPTY = new Block.EmptyBlock();
    public static final Block GRASS = new Block.DefaultBlock("GrassBlock");
    public static final Block ICE = new Block.DefaultBlock("IceBlock");
    public static final Block PLAIN = new Block.DefaultBlock("PlainBlock");
    public static final Block STONE = new Block.DefaultBlock("StoneBlock");
    public static final Block WALL = new Block.DefaultBlock("WallBlock");
    public static final Block WATER = new Block.DefaultBlock("WaterBlock");
    public static final Block WINDOW = new Block.DefaultBlock("WindowBlock");
    public static final Block WOOD = new Block.DefaultBlock("WoodBlock");
    public static final float SIZE_X = 100f;
    public static final float SIZE_Y = 40f;
    public static final float SIZE_Z = 82f;
    public static final float TOP_OFFSET = 120f;

    public abstract void draw(Canvas paramCanvas, float paramFloat1, float paramFloat2);

    public abstract boolean isEmpty();

    private static class DefaultBlock extends Block {

        private final Image image;

        public DefaultBlock(final String name) {
            this.image = Cute.loadImage(name);
        }

        @Override
        public void draw(final Canvas canvas, final float x, final float y) {
            canvas.drawImage(x, y, this.image, Alignment.BOTTOM_CENTER);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    private static class EmptyBlock extends Block {

        @Override
        public void draw(final Canvas canvas, final float x, final float y) {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}
