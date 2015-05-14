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

    private final TreeMap<Integer, TileSet> tileSetsByGid;

    TileSets() {
        tileSetsByGid = new TreeMap<Integer, TileSet>();
    }

    void add(final TileSet tileSet) {
        tileSetsByGid.put(tileSet.getFirstGlobalId(), tileSet);
    }

    Tile lookupTile(final int globalId) {
        Map.Entry<Integer, TileSet> entry = tileSetsByGid.floorEntry(globalId);
        if (entry == null) {
            return null;
        }
        else {
            return entry.getValue().getTile(globalId - entry.getKey());
        }
    }
}
