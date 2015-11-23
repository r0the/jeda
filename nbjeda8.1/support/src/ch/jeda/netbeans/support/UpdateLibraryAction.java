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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.netbeans.api.project.Project;

public class UpdateLibraryAction extends AbstractAction {

    private final ProjectType projectType;
    private final Project project;

    public UpdateLibraryAction(final ProjectType projectType, final Project project) {
        this.projectType = projectType;
        this.project = project;
        this.putValue(NAME, "Update Jeda Library");
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (Dialog.askQuestion("You are about to update the project " + this.project.getProjectDirectory().getName() +
                               " from Jeda version " + this.projectType.projectVersion(this.project) +
                               " to " + this.projectType.pluginVersion() + ". Are you sure?")) {
            this.projectType.updateProject(this.project);
        }
    }
}
