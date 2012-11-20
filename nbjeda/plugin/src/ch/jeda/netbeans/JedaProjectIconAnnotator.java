/*
 * Copyright (C) 2012 by Stefan Rothe
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

import java.awt.Image;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectIconAnnotator;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ProjectIconAnnotator.class)
public class JedaProjectIconAnnotator implements ProjectIconAnnotator {

    public @Override
    Image annotateIcon(Project project, Image orig, boolean openedNode) {
        ProjectWrapper wrapper = ProjectWrapper.forProject(project);
        Image result = wrapper.getIcon();
        if (result == null) {
            result = orig;
        }

        return result;
    }

    public @Override
    void addChangeListener(ChangeListener listener) {
        // ignored
    }

    public @Override
    void removeChangeListener(ChangeListener listener) {
        // ignored
    }
}
