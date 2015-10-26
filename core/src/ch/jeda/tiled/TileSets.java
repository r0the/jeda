/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
package ch.jeda.tiled;

import java.util.Map;
import java.util.TreeMap;

final class TileSets {

    private static final int FLIP_HORIZONTALLY_FLAG = 0x80000000;
    private static final int FLIP_VERTICALLY_FLAG = 0x40000000;
    private static final int FLIP_DIAGONALLY_FLAG = 0x20000000;
    private static final int FLAGS = FLIP_HORIZONTALLY_FLAG | FLIP_VERTICALLY_FLAG | FLIP_DIAGONALLY_FLAG;
    private final TreeMap<Integer, TileSet> tileSetsByGid;

    TileSets() {
        tileSetsByGid = new TreeMap<Integer, TileSet>();
    }

    void add(final TileSet tileSet) {
        tileSetsByGid.put(tileSet.getFirstGlobalId(), tileSet);
    }

    Tile lookupTile(final long globalId) {
        long flags = globalId & FLAGS;
        int id = (int) (globalId & ~FLAGS);
        Map.Entry< Integer, TileSet> entry = tileSetsByGid.floorEntry(id);
        if (entry == null) {
            return null;
        }

        Tile tile = entry.getValue().getTile(id - entry.getKey());
        if (flags != 0) {
            tile = new ProxyTile(tile,
                                 (flags & FLIP_HORIZONTALLY_FLAG) != 0,
                                 (flags & FLIP_VERTICALLY_FLAG) != 0);
        }

        return tile;
    }
}
