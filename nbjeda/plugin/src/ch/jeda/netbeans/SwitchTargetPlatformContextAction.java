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
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.project.Project;

import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.DynamicMenuContent;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Build",
          id = "ch.jeda.netbeans.SwitchTargetPlatformContextAction")
@ActionRegistration(iconBase = "ch/jeda/netbeans/resources/logo-16x16.png",
                    displayName = "#CTL_SwitchTargetPlatformContextAction")
@ActionReferences({
    @ActionReference(path = "Projects/org-netbeans-modules-android-project/Actions",
                     position = 690),
    @ActionReference(path = "Projects/org-netbeans-modules-java-j2seproject/Actions",
                     position = 691)
})
@Messages("CTL_SwitchTargetPlatformContextAction=Switch Target Platform")
public class SwitchTargetPlatformContextAction extends AbstractAction implements ContextAwareAction {

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
            switch (this.wrapper.getPlatform()) {
                case Android:
                    this.putValue(NAME, "Switch to Java");
                    break;
                case Java:
                    this.putValue(NAME, "Switch to Android");
                    break;
            }
        }

        public @Override
        void actionPerformed(final ActionEvent event) {
            switch (this.wrapper.getPlatform()) {
                case Android:
                    this.wrapper.convertTo(ProjectWrapper.Platform.Java);
                    break;
                case Java:
                    this.wrapper.convertTo(ProjectWrapper.Platform.Android);
                    break;
            }
        }
    }
}
