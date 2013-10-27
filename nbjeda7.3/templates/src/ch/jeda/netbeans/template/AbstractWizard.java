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
package ch.jeda.netbeans.template;

import java.awt.Component;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;

public abstract class AbstractWizard {

    private int index;
    private WizardDescriptor.Panel[] panels;
    private WizardDescriptor wizard;

    public void addChangeListener(final ChangeListener listener) {
    }

    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return this.panels[this.index];
    }

    public boolean hasNext() {
        return this.index < this.panels.length - 1;
    }

    public boolean hasPrevious() {
        return this.index > 0;
    }

    public void initialize(final WizardDescriptor wizard) {
        this.wizard = wizard;
        this.panels = new WizardDescriptor.Panel[1];
        this.panels[0] = this.createPanel(wizard);
        final Component c = this.panels[0].getComponent();
        String stepsName = this.stepName();
        if (stepsName == null) {
            stepsName = c.getName();
        }

        if (c instanceof JComponent) {
            final JComponent jc = (JComponent) c;
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, 0);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, stepsName);
            jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
            jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
        }
    }

    public Set<?> instantiate() throws IOException {
        return this.createFiles(this.wizard);
    }

    public String name() {
        return this.index + 1 + ". from " + this.panels.length;
    }

    public void nextPanel() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }

        this.index++;
    }

    public void previousPanel() {
        if (!this.hasPrevious()) {
            throw new NoSuchElementException();
        }

        this.index--;
    }

    public void removeChangeListener(final ChangeListener listener) {
    }

    public void uninitialize(final WizardDescriptor wizard) {
        this.panels = null;
    }

    protected abstract Set<?> createFiles(WizardDescriptor wizard) throws IOException;

    protected abstract WizardDescriptor.Panel createPanel(WizardDescriptor wizard);

    protected abstract String stepName();
}
