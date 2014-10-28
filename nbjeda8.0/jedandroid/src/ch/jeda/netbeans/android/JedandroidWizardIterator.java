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
package ch.jeda.netbeans.android;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

// TODO define position attribute
@TemplateRegistration(
    folder = "Project/Jeda",
    displayName = "#Jedandroid_displayName",
    description = "JedandroidDescription.html",
    iconBase = "ch/jeda/netbeans/android/res/icon.png")
@Messages("Jedandroid_displayName=Jeda Application for Android")
public class JedandroidWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    private static final String PROJECT_DIR_PROPERTY = "projdir";
    private static final String NAME_PROPERTY = "name";
    private WizardDescriptor.Panel<WizardDescriptor> panel;
    private WizardDescriptor wizard;

    public static JedandroidWizardIterator createIterator() {
        return new JedandroidWizardIterator();
    }

    public JedandroidWizardIterator() {
    }

    @Override
    public final void addChangeListener(final ChangeListener listener) {
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return this.panel;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
        this.panel = new JedandroidWizardPanel();
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

    @Override
    public Set<?> instantiate() throws IOException {
        Set<FileObject> resultSet = new LinkedHashSet<FileObject>();
        File dirF = FileUtil.normalizeFile((File) wizard.getProperty(PROJECT_DIR_PROPERTY));
        dirF.mkdirs();

        FileObject template = Templates.getTemplate(wizard);
        FileObject dir = FileUtil.toFileObject(dirF);

        JedandroidProject.init(dir);

        // Always open top dir as a project:
        resultSet.add(dir);

        File parent = dirF.getParentFile();
        if (parent != null && parent.exists()) {
            ProjectChooser.setProjectsFolder(parent);
        }

        return resultSet;
    }

    @Override
    public String name() {
        return "1 of 1";
    }

    @Override
    public void nextPanel() {
        throw new NoSuchElementException();
    }

    @Override
    public void previousPanel() {
        throw new NoSuchElementException();
    }

    @Override
    public final void removeChangeListener(final ChangeListener listener) {
    }

    @Override
    public void uninitialize(final WizardDescriptor wizard) {
        this.wizard.putProperty(NAME_PROPERTY, null);
        this.wizard.putProperty(PROJECT_DIR_PROPERTY, null);
        this.wizard = null;
        this.panel = null;
    }

    private String stepName() {
        return NbBundle.getMessage(JedandroidWizardIterator.class, "LBL_CreateProjectStep");
    }
}
