/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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
package ch.jeda.netbeans.template;

import java.awt.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.spi.java.project.support.ui.templates.JavaTemplates;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;
import org.openide.util.NbBundle;

public class CreateJavaClassWizardIterator {

    private WizardDescriptor.Panel<WizardDescriptor> panel;
    private WizardDescriptor wizard;

    protected CreateJavaClassWizardIterator() {
    }

    public final void addChangeListener(final ChangeListener listener) {
    }

    public final WizardDescriptor.Panel<WizardDescriptor> current() {
        return this.panel;
    }

    public final boolean hasNext() {
        return false;
    }

    public final boolean hasPrevious() {
        return false;
    }

    public final void initialize(final WizardDescriptor wizard) {
        this.wizard = wizard;
        this.panel = this.createPanel(wizard);
        final Component component = this.panel.getComponent();
        if (component instanceof JComponent) {
            final JComponent jc = (JComponent) component;
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, 0);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, this.stepName());
            jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
        }
    }

    public final Set<?> instantiate() throws IOException {
        return this.createFiles(this.wizard);
    }

    public final String name() {
        return "1 of 1";
    }

    public final void nextPanel() {
        throw new NoSuchElementException();
    }

    public final void previousPanel() {
        throw new NoSuchElementException();
    }

    public final void removeChangeListener(final ChangeListener listener) {
    }

    public final void uninitialize(final WizardDescriptor wizard) {
        this.wizard = null;
        this.panel = null;
    }

    private Set<?> createFiles(final WizardDescriptor wizard) throws IOException {
        // Create file from template
        final String className = Templates.getTargetName(wizard);
        final FileObject pkg = Templates.getTargetFolder(wizard);
        final DataFolder targetFolder = DataFolder.findFolder(pkg);
        final TemplateWizard template = (TemplateWizard) wizard;
        final DataObject doTemplate = template.getTemplate();
        final Set<DataObject> result = new HashSet<DataObject>();
        final String projectDir = Templates.getProject(wizard).getProjectDirectory().getPath() + "/src/";
        String packageName = pkg.getPath().substring(projectDir.length());
        packageName = packageName.replace('/', '.');

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("package", packageName);
        params.put("name", className);
        result.add(doTemplate.createFromTemplate(targetFolder, className + ".java", params));
        return result;
    }

    private WizardDescriptor.Panel<WizardDescriptor> createPanel(final WizardDescriptor wizard) {
        final Project project = Templates.getProject(wizard);
        final Sources sources = ProjectUtils.getSources(project);
        final SourceGroup[] groups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
        return JavaTemplates.createPackageChooser(project, groups);
    }

    private String stepName() {
        return NbBundle.getMessage(JedaProgramWizardIterator.class, "LBL_CreateClassStep");
    }
}
