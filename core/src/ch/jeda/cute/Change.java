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

abstract class Change {

    static Change createAddObjectChange(final CuteObject object) {
        return new AddObjectChange(object);
    }

    static Change createRemoveObjectChange(final CuteObject object) {
        return new RemoveObjectChange(object);
    }

    static Change createSetBlockChange(final int x, final int y, final int z, final Block block) {
        return new SetBlockChange(x, y, z, block);
    }

    abstract void apply(CuteWorld paramCuteWorld);

    private static class AddObjectChange extends Change {

        private final CuteObject object;

        AddObjectChange(final CuteObject object) {
            this.object = object;
        }

        @Override
        void apply(final CuteWorld renderer) {
            renderer.doAddObject(this.object);
        }
    }

    private static class RemoveObjectChange extends Change {

        private final CuteObject object;

        RemoveObjectChange(final CuteObject object) {
            this.object = object;
        }

        @Override
        void apply(final CuteWorld renderer) {
            renderer.doRemoveObject(this.object);
        }
    }

    private static class SetBlockChange extends Change {

        private final int x;
        private final int y;
        private final int z;
        private final Block block;

        SetBlockChange(final int x, final int y, final int z, final Block block) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.block = block;
        }

        @Override
        void apply(CuteWorld renderer) {
            renderer.doSetBlock(this.x, this.y, this.z, this.block);
        }
    }
}
