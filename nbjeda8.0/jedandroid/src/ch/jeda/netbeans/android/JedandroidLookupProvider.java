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
package ch.jeda.netbeans.android;

import org.netbeans.spi.project.LookupProvider;
import org.openide.util.Lookup;

@LookupProvider.Registration(projectTypes = {
    @LookupProvider.Registration.ProjectType(id = JedandroidProject.PROJECT_TYPE)
})
public class JedandroidLookupProvider implements LookupProvider {

    @Override
    public Lookup createAdditionalLookup(final Lookup lookup) {
        return JedandroidProject.fixLookup(lookup);
    }
}
