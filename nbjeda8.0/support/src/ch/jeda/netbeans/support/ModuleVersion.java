/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda.netbeans.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.openide.modules.ModuleInfo;
import org.openide.util.Lookup;

public class ModuleVersion {

    private static final Map<String, Version> MAP = initMap();

    public static Version lookup(final String codeNameBase) {
        return MAP.get(codeNameBase);
    }

    private static Map<String, Version> initMap() {
        Map<String, Version> result = new HashMap<String, Version>();
        final Collection<? extends ModuleInfo> modules = Lookup.getDefault().<ModuleInfo>lookupAll(ModuleInfo.class);
        for (final ModuleInfo module : modules) {
            result.put(module.getCodeNameBase(), new Version(module.getSpecificationVersion()));
        }

        return result;
    }
}
