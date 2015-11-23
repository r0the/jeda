/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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

import java.awt.Image;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectIconAnnotator;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ProjectIconAnnotator.class)
public class ProjectIconAnnotatorImp implements ProjectIconAnnotator {

    @Override
    public Image annotateIcon(final Project project, final Image orig, final boolean openedNode) {
        final ProjectType projectType = ProjectType.lookup(project);
        if (projectType != null) {
            return projectType.annotateIcon(orig, openedNode);
        }
        else {
            return orig;
        }
    }

    @Override
    public void addChangeListener(final ChangeListener listener) {
    }

    @Override
    public void removeChangeListener(final ChangeListener listener) {
    }
}
