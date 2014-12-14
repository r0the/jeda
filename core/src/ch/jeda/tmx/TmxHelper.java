/*
 * Copyright (C) 2014 by Stefan Rothe
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
package ch.jeda.tmx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import javax.xml.bind.DatatypeConverter;

final class TmxHelper {

    static TmxMapOrientation parseOrientation(final String value) {
        if (value == null) {
            return TmxMapOrientation.ORTHOGONAL;
        }

        try {
            return TmxMapOrientation.valueOf(value.toUpperCase());
        }
        catch (final IllegalArgumentException ex) {
            return TmxMapOrientation.ORTHOGONAL;
        }
    }

    static void parseTerrain(final String terrainInfo, final TmxTerrain[] terrainTypes, TmxTerrain[] result) {
        if (terrainInfo != null) {
            String[] parts = terrainInfo.split(",");
            int i = 0;
            while (i < 4 && i < parts.length) {
                try {
                    int terrainId = Integer.parseInt(parts[i]);
                    if (0 <= terrainId && terrainId < terrainTypes.length) {
                        result[i] = terrainTypes[terrainId];
                    }
                }
                catch (final NumberFormatException ex) {
                    // ignore
                }

                ++i;
            }
        }
    }

}
