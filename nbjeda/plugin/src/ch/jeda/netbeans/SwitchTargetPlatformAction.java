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

import org.openide.awt.ActionReferences;
import static ch.jeda.netbeans.ProjectWrapper.Platform.Android;
import static ch.jeda.netbeans.ProjectWrapper.Platform.Java;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Build",
          id = "ch.jeda.netbeans.SwitchTargetPlatformAction")
@ActionRegistration(iconBase = "ch/jeda/netbeans/resources/logo-16x16.png",
                    displayName = "#CTL_SwitchTargetPlatformAction")
@ActionReferences({
    @ActionReference(path = "Menu/BuildProject", position = 97)
})
@Messages("CTL_SwitchTargetPlatformAction=Switch Target Platform")
public final class SwitchTargetPlatformAction implements ActionListener {

    private final ProjectWrapper wrapper;

    public SwitchTargetPlatformAction(final Project context) {
        this.wrapper = ProjectWrapper.forProject((Project) context);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (!this.wrapper.isJedaProject()) {
            Util.log("Ignoring platform switch: Not a Jeda project.");
            return;
        }

        switch (this.wrapper.getPlatform()) {
            case Android:
                Util.log("Switching to Java platform");
                this.wrapper.convertTo(ProjectWrapper.Platform.Java);
                break;
            case Java:
                Util.log("Switching to Android platform");
                this.wrapper.convertTo(ProjectWrapper.Platform.Android);
                break;
        }
    }
}
