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
import ch.jeda.ui.Window;
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
    private double scrollX;
    private double scrollY;

    public CuteWorld(final int sizeX, final int sizeY, final int sizeZ) {
        changes = new ArrayList<Change>();
        objects = new ArrayList<CuteObject>();
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        slices = new Slice[sizeY];
        for (int y = 0; y < sizeY; y++) {
            slices[y] = new Slice(sizeX, sizeZ);
        }
    }

    public void addObject(final CuteObject object) {
        if (object != null) {
            changes.add(Change.createAddObjectChange(object));
        }
    }

    public void clearObjects() {
        // TODO:
    }

    public void fill(final int z, final Block block) {
        if (0 <= z && z < sizeZ) {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
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

    public int getHeightAt(int x, int y) {
        int height = sizeZ;
        while (height > 0 && getBlockAt(x, y, height - 1).isEmpty()) {
            --height;
        }

        return height;
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
        return scrollLock;
    }

    public double getScrollX() {
        return scrollX;
    }

    public double getScrollY() {
        return scrollY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public boolean isValidCoordinate(final int x, final int y, final int z) {
        return 0 <= x && x < sizeX &&
               0 <= y && y < sizeY &&
               0 <= z && z < sizeZ;
    }

    public void removeObject(final CuteObject object) {
        if (object != null) {
            changes.add(Change.createRemoveObjectChange(object));
        }
    }

    public void draw(final Window canvas) {
        checkScrollPos(canvas);

        double top = Block.SIZE_Y + sizeZ * Block.SIZE_Z;
        double screenStartY = top - scrollY;
        int startX = (int) Math.max(0f, scrollX / Block.SIZE_X);
        int endX = (int) Math.min(sizeX, startX + canvas.getWidth() / Block.SIZE_X + 1f);
        int startY = 0;
        int endY = sizeY - 1;
        double screenStartX = Block.SIZE_X / 2.0 - scrollX % Block.SIZE_X;
        for (int y = startY; y <= endY; y++) {
            final Slice slice = slices[y];
            // Draw blocks and objects of this slice
            double screenY = screenStartY;
            for (int z = 0; z <= sizeZ; z++) {
                double screenX = screenStartX;
                for (int x = startX; x <= endX; x++) {
                    final Block block = slice.getBlockAt(x, z);
                    if (!block.isEmpty()) {
                        block.draw(canvas, screenX, screenY);
                        if (getBlockAt(x, y, z + 1).isEmpty()) {
                            boolean east = !getBlockAt(x + 1, y, z + 1).isEmpty();
                            boolean north = !getBlockAt(x, y - 1, z + 1).isEmpty();
                            boolean south = !getBlockAt(x, y + 1, z + 1).isEmpty();
                            boolean west = !getBlockAt(x - 1, y, z + 1).isEmpty();
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

                            if (!north && !east && !getBlockAt(x + 1, y - 1, z + 1).isEmpty()) {
                                canvas.drawImage(screenX, screenY, Cute.getShadow(Direction.SOUTH_WEST), Alignment.BOTTOM_CENTER);
                            }

                            if (!north && !west && !getBlockAt(x - 1, y - 1, z + 1).isEmpty()) {
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

                    for (final CuteObject object : slice.getObjectsAt(x, z)) {
                        object.draw(canvas,
                                    screenX + (object.getX() - object.getIntX()) * Block.SIZE_X,
                                    screenY - (object.getIntY() - object.getY()) * Block.SIZE_Y -
                                    (object.getZ() - object.getIntZ()) * Block.SIZE_Z);
                    }

                    screenX += Block.SIZE_X;
                }

                screenY -= Block.SIZE_Z;
            }

            // Draw object messages of this slice
            screenY = screenStartY;
            for (int z = 0; z <= sizeZ; z++) {
                double screenX = screenStartX;
                for (int x = startX; x <= endX; x++) {
                    for (final CuteObject object : slice.getObjectsAt(x, z)) {
                        final String message = object.getMessage();
                        if (message != null) {
                            SpeechBubble.draw(canvas, screenX + (object.getX() - object.getIntX()) * Block.SIZE_X,
                                              screenY - (object.getY() - object.getIntY()) * Block.SIZE_Y -
                                              (object.getZ() - object.getIntZ()) * Block.SIZE_Z, message);
                        }
                    }

                    screenX += Block.SIZE_X;
                }

                screenY -= Block.SIZE_Z;
            }

            screenStartY += Block.SIZE_Y;
        }
    }

    public void scroll(final double dx, final double dy) {
        scrollX += dx;
        scrollY += dy;
    }

    public void setBlockAt(final int x, final int y, final int z, final Block block) {
        changes.add(Change.createSetBlockChange(x, y, z, block));
    }

    public void setScrollLock(final CuteObject object) {
        scrollLock = object;
    }

    public void update(final double dt) {
        Change[] changesToApply = (Change[]) changes.toArray(new Change[changes.size()]);
        changes.clear();
        for (int i = 0; i < changesToApply.length; i++) {
            changesToApply[i].apply(this);
        }

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).internalUpdate(dt);
        }

        for (int i = 0; i < objects.size(); ++i) {
            for (int j = i + 1; j < objects.size(); ++j) {
                final CuteObject a = objects.get(i);
                final CuteObject b = objects.get(j);
                if (a.distanceTo(b) <= a.getRadius() + b.getRadius()) {
                    a.collideWith(b);
                    b.collideWith(a);
                }
            }
        }
    }

    Box getBox(final int x, final int y, final int z) {
        if ((0 <= y) && (y < sizeY)) {
            return slices[y].getBoxAt(x, z);
        }
        else {
            return null;
        }
    }

    void doAddObject(final CuteObject object) {
        objects.add(object);
        object.setRenderer(this);
    }

    void doRemoveObject(final CuteObject object) {
        objects.remove(object);
        object.setRenderer(null);
        if (object.equals(scrollLock)) {
            scrollLock = null;
        }
    }

    void doSetBlock(final int x, final int y, final int z, final Block block) {
        Box box = getBox(x, y, z);
        if (box != null) {
            box.setBlock(block);
        }
    }

    private void checkScrollPos(final Window canvas) {
        double maxScrollX = sizeX * Block.SIZE_X - canvas.getWidth();
        double maxScrollY = sizeY * Block.SIZE_Y + sizeZ * Block.SIZE_Z - canvas.getHeight();
        if (scrollLock != null) {
            scrollX = scrollLock.getX() * Block.SIZE_X + Block.SIZE_X / 2.0 - canvas.getWidth() / 2.0;
            scrollY = scrollLock.getY() * Block.SIZE_Y - scrollLock.getZ() * Block.SIZE_Z -
                      canvas.getHeight() / 2.0 + sizeZ * Block.SIZE_Z;
        }

        scrollX = Math.max(0f, Math.min(maxScrollX, scrollX));
        scrollY = Math.max(0f, Math.min(maxScrollY, scrollY));
    }
}
