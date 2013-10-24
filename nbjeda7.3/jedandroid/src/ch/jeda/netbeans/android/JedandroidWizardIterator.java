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

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
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
public class JedandroidWizardIterator implements WizardDescriptor./*Progress*/InstantiatingIterator {

    private int index;
    private WizardDescriptor.Panel[] panels;
    private WizardDescriptor wiz;

    public JedandroidWizardIterator() {
    }

    public static JedandroidWizardIterator createIterator() {
        return new JedandroidWizardIterator();
    }

    private WizardDescriptor.Panel[] createPanels() {
        return new WizardDescriptor.Panel[]{
            new JedandroidWizardPanel(),};
    }

    private String[] createSteps() {
        return new String[]{
            NbBundle.getMessage(JedandroidWizardIterator.class, "LBL_CreateProjectStep")
        };
    }

    @Override
    public Set/*<FileObject>*/ instantiate(/*ProgressHandle handle*/) throws IOException {
        Set<FileObject> resultSet = new LinkedHashSet<FileObject>();
        File dirF = FileUtil.normalizeFile((File) wiz.getProperty("projdir"));
        dirF.mkdirs();

        FileObject template = Templates.getTemplate(wiz);
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
    public void initialize(WizardDescriptor wiz) {
        this.wiz = wiz;
        index = 0;
        panels = createPanels();
        // Make sure list of steps is accurate.
        String[] steps = createSteps();
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if (steps[i] == null) {
                // Default step name to component name of panel.
                // Mainly useful for getting the name of the target
                // chooser to appear in the list of steps.
                steps[i] = c.getName();
            }
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                // Step #.
                // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                // Step name (actually the whole list for reference).
                jc.putClientProperty("WizardPanel_contentData", steps);
            }
        }
    }

    @Override
    public void uninitialize(final WizardDescriptor wiz) {
        this.wiz.putProperty("projdir", null);
        this.wiz.putProperty("name", null);
        this.wiz = null;
        this.panels = null;
    }

    @Override
    public String name() {
        return MessageFormat.format("{0} of {1}", new Object[]{
            new Integer(this.index + 1), new Integer(this.panels.length)});
    }

    @Override
    public boolean hasNext() {
        return this.index < this.panels.length - 1;
    }

    @Override
    public boolean hasPrevious() {
        return this.index > 0;
    }

    @Override
    public void nextPanel() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }

        this.index++;
    }

    @Override
    public void previousPanel() {
        if (!this.hasPrevious()) {
            throw new NoSuchElementException();
        }

        this.index--;
    }

    @Override
    public WizardDescriptor.Panel current() {
        return this.panels[this.index];
    }

    @Override
    public final void addChangeListener(final ChangeListener l) {
    }

    @Override
    public final void removeChangeListener(final ChangeListener l) {
    }
}
