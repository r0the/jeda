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
import java.util.ArrayList;
import java.util.List;

public final class CuteWorld {

    private static final CuteObject[] NO_OBJECTS = new CuteObject[0];
    private final List<Change> changes;
    private final List<CuteObject> objects;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final Slice[] slices;
    private CuteObject scrollLock;
    private float scrollX;
    private float scrollY;

    public CuteWorld(final int sizeX, final int sizeY, final int sizeZ) {
        this.changes = new ArrayList();
        this.objects = new ArrayList();
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.slices = new Slice[sizeZ];
        for (int z = 0; z < sizeZ; z++) {
            this.slices[z] = new Slice(sizeX, sizeY);
        }
    }

    public void addObject(final CuteObject object) {
        this.changes.add(Change.createAddObjectChange(object));
    }

    public void fill(final int y, final Block block) {
        if (0 <= y && y < this.sizeY) {
            for (int x = 0; x < this.sizeX; x++) {
                for (int z = 0; z < this.sizeZ; z++) {
                    setBlockAt(x, y, z, block);
                }
            }
        }
    }

    public Block getBlockAt(final int x, final int y, final int z) {
        final Box box = getBox(x, y, z);
        if (box != null) {
            return box.getBlock();
        }
        else {
            return Block.EMPTY;
        }
    }

    public CuteObject[] getObjectsAt(final int x, final int y, final int z) {
        final Box box = getBox(x, y, z);
        if (box != null) {
            return box.getObjectsArray();
        }
        else {
            return NO_OBJECTS;
        }
    }

    public CuteObject getScrollLock() {
        return this.scrollLock;
    }

    public float getScrollX() {
        return this.scrollX;
    }

    public float getScrollY() {
        return this.scrollY;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public int getSizeZ() {
        return this.sizeZ;
    }

    public boolean isValidCoordinate(final int x, final int y, final int z) {
        return 0 <= x && x < this.sizeX &&
               0 <= y && y < this.sizeY &&
               0 <= z && z < this.sizeZ;
    }

    public void removeObject(final CuteObject object) {
        this.changes.add(Change.createRemoveObjectChange(object));
    }

    public void draw(final Canvas canvas) {
        checkScrollPos(canvas);

        float top = Block.SIZE_Z + this.sizeY * Block.SIZE_Y;
        float screenStartY = top - this.scrollY;
        int startX = (int) Math.max(0f, this.scrollX / Block.SIZE_X);
        int endX = (int) Math.min(this.sizeX, startX + canvas.getWidth() / Block.SIZE_X + 1f);
        int startZ = 0;
        int endZ = this.sizeZ - 1;
        float screenStartX = Block.SIZE_X / 2f - this.scrollX % Block.SIZE_X;
        for (int z = startZ; z <= endZ; z++) {
            final Slice slice = this.slices[z];
            float screenY = screenStartY;
            for (int y = 0; y <= this.sizeY; y++) {
                float screenX = screenStartX;
                for (int x = startX; x <= endX; x++) {
                    final Block block = slice.getBlockAt(x, y);
                    if (!block.isEmpty()) {
                        block.draw(canvas, screenX, screenY);
                        if (getBlockAt(x, y + 1, z).isEmpty()) {
                            boolean east = !getBlockAt(x + 1, y + 1, z).isEmpty();
                            boolean north = !getBlockAt(x, y + 1, z - 1).isEmpty();
                            boolean south = !getBlockAt(x, y + 1, z + 1).isEmpty();
                            boolean west = !getBlockAt(x - 1, y + 1, z).isEmpty();
                            if (east) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.WEST), Alignment.BOTTOM_CENTER);
                            }

                            if (north) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.SOUTH), Alignment.BOTTOM_CENTER);
                            }

                            if (south) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.NORTH), Alignment.BOTTOM_CENTER);
                            }

                            if (west) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.EAST), Alignment.BOTTOM_CENTER);
                            }

                            if (!north && !east && !getBlockAt(x + 1, y + 1, z - 1).isEmpty()) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.SOUTH_WEST), Alignment.BOTTOM_CENTER);
                            }

                            if (!north && !west && !getBlockAt(x - 1, y + 1, z - 1).isEmpty()) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.SOUTH_EAST), Alignment.BOTTOM_CENTER);
                            }

                            if (!south && !east && !getBlockAt(x + 1, y + 1, z + 1).isEmpty()) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.NORTH_WEST), Alignment.BOTTOM_CENTER);
                            }

                            if (!south && !west && !getBlockAt(x - 1, y + 1, z + 1).isEmpty()) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.NORTH_EAST), Alignment.BOTTOM_CENTER);
                            }
                        }
                    }

                    for (CuteObject object : slice.getObjectsAt(x, y)) {
                        object.draw(canvas,
                                    screenX + (object.getX() - object.getIntX()) * Block.SIZE_X,
                                    screenY + (object.getY() - object.getIntY()) * Block.SIZE_Y +
                                    (object.getZ() - object.getIntZ()) * Block.SIZE_Z);
                    }

                    screenX += Block.SIZE_X;
                }

                screenY -= Block.SIZE_Y;
            }

            screenStartY += Block.SIZE_Z;
        }
    }

    public void scroll(final float dx, final float dy) {
        this.scrollX += dx;
        this.scrollY += dy;
    }

    public void setBlockAt(final int x, final int y, final int z, final Block block) {
        this.changes.add(Change.createSetBlockChange(x, y, z, block));
    }

    public void setScrollLock(final CuteObject object) {
        this.scrollLock = object;
    }

    public void update(final float dt) {
        Change[] changesToApply = (Change[]) this.changes.toArray(new Change[this.changes.size()]);
        this.changes.clear();
        for (int i = 0; i < changesToApply.length; i++) {
            changesToApply[i].apply(this);
        }

        for (int i = 0; i < this.objects.size(); i++) {
            ((CuteObject) this.objects.get(i)).internalUpdate(dt);
        }
    }

    Box getBox(final int x, final int y, final int z) {
        if ((0 <= z) && (z < this.sizeZ)) {
            return this.slices[z].getBoxAt(x, y);
        }
        return null;
    }

    void doAddObject(final CuteObject object) {
        this.objects.add(object);
        object.setRenderer(this);
    }

    void doRemoveObject(final CuteObject object) {
        this.objects.remove(object);
        object.setRenderer(null);
    }

    void doSetBlock(final int x, final int y, final int z, final Block block) {
        Box box = getBox(x, y, z);
        if (box != null) {
            box.setBlock(block);
        }
    }

    private void checkScrollPos(final Canvas canvas) {
        float maxScrollX = this.sizeX * Block.SIZE_X - canvas.getWidth();
        float maxScrollY = this.sizeY * Block.SIZE_Y + this.sizeZ * Block.SIZE_Z - canvas.getHeight();
        if (this.scrollLock != null) {
            this.scrollX = (this.scrollLock.getX() * Block.SIZE_X + Block.SIZE_X / 2 - canvas.getWidth() / 2f);
            this.scrollY = (this.scrollLock.getY() * Block.SIZE_Y + this.scrollLock.getZ() * Block.SIZE_Z - canvas.getHeight() / 2f);
        }

        this.scrollX = Math.max(0f, Math.min(maxScrollX, this.scrollX));
        this.scrollY = Math.max(0f, Math.min(maxScrollY, this.scrollY));
    }
}
