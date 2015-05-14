/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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

    public static final Block BROWN = new Block.DefaultBlock("BROWN");
    public static final Block DIRT = new Block.DefaultBlock("DIRT");
    public static final Block EMPTY = new Block.EmptyBlock();
    public static final Block GRASS = new Block.DefaultBlock("GRASS");
    public static final Block ICE = new Block.DefaultBlock("ICE");
    public static final Block PLAIN = new Block.DefaultBlock("PLAIN");
    public static final Block STONE = new Block.DefaultBlock("STONE");
    public static final Block WALL = new Block.DefaultBlock("WALL");
    public static final Block WATER = new Block.DefaultBlock("WATER");
    public static final Block WOOD = new Block.DefaultBlock("WOOD");
    public static final int SIZE_X = 100;
    public static final int SIZE_Y = 82;
    public static final int SIZE_Z = 40;
    public static final int TOP_OFFSET = 120;

    public abstract void draw(Canvas canvas, double x, double y);

    public abstract void draw(Canvas canvas, double x, double y, int alpha);

    public abstract boolean isEmpty();

    private static class DefaultBlock extends Block {

        private final Image image;
        private final String name;

        public DefaultBlock(final String name) {
            image = Cute.loadImage(name.toLowerCase() + "_block");
            this.name = name;
        }

        @Override
        public void draw(final Canvas canvas, final double x, final double y) {
            canvas.drawImage(x, y, image, Alignment.BOTTOM_CENTER);
        }

        @Override
        public void draw(final Canvas canvas, final double x, final double y, final int alpha) {
            canvas.drawImage(x, y, image, alpha, Alignment.BOTTOM_CENTER);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class EmptyBlock extends Block {

        @Override
        public void draw(final Canvas canvas, final double x, final double y) {
        }

        @Override
        public void draw(final Canvas canvas, final double x, final double y, final int alpha) {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}
