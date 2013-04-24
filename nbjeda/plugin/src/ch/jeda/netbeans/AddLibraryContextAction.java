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
package ch.jeda.netbeans;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.DynamicMenuContent;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Build",
          id = "ch.jeda.netbeans.AddLibraryContextAction")
@ActionRegistration(iconBase = "ch/jeda/netbeans/resources/logo-16x16.png",
                    displayName = "#CTL_AddLibraryContextAction")
@ActionReferences({
    @ActionReference(path = "Projects/org-netbeans-modules-android-project/Actions",
                     position = 692),
    @ActionReference(path = "Projects/org-netbeans-modules-java-j2seproject/Actions",
                     position = 693)
})
@Messages("CTL_AddLibraryContextAction=Add JAR Library")
public class AddLibraryContextAction extends AbstractAction implements ContextAwareAction {

    public @Override
    void actionPerformed(final ActionEvent event) {
        assert false;
    }

    public @Override
    Action createContextAwareInstance(final Lookup context) {
        return new ContextAction(context);
    }

    private static final class ContextAction extends AbstractAction {

        private final ProjectWrapper wrapper;

        public ContextAction(final Lookup context) {
            this.wrapper = ProjectWrapper.forProject(context.lookup(Project.class));
            this.setEnabled(this.wrapper.isJedaProject());
            this.putValue(DynamicMenuContent.HIDE_WHEN_DISABLED, true);
            this.putValue(NAME, "Add JAR Library");
        }

        public @Override
        void actionPerformed(final ActionEvent event) {
            FileChooserBuilder fbc = new FileChooserBuilder(AddLibraryContextAction.class);
            final FileFilter ff = new FileNameExtensionFilter("Java Library (JAR)", "jar");
            fbc.addFileFilter(ff);
            fbc.setFileFilter(ff);
            fbc.setApproveText("Add Library");
            fbc.setTitle("Add Library");
            final File file = fbc.showOpenDialog();
            if (file != null) {
                this.wrapper.addLibrary(file);
            }
        }
    }
}
