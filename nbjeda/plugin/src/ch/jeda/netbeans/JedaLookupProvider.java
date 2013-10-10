/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
package ch.jeda.netbeans;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.spi.project.LookupProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

@LookupProvider.Registration(projectTypes = {
    @LookupProvider.Registration.ProjectType(id = "org-netbeans-modules-android-project"),
    @LookupProvider.Registration.ProjectType(id = "org-netbeans-modules-java-j2seproject")
})
public class JedaLookupProvider implements LookupProvider {

    static {
        OpenProjects.getDefault().addPropertyChangeListener(null);
    }

    @Override
    public Lookup createAdditionalLookup(final Lookup lookup) {
        final ProjectWrapper wrapper = ProjectWrapper.forProject(lookup.lookup(Project.class));
        if (wrapper.isJedaProject()) {
            return Lookups.fixed(new JedaProjectIconAnnotator(), new JedaProjectOpenedHook(wrapper));
        }
        else {
            return Lookups.fixed();
        }
    }
}
