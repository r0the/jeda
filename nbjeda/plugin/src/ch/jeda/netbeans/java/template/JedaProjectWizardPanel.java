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
package ch.jeda.netbeans.java.template;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

public class JedaProjectWizardPanel implements WizardDescriptor.Panel,
                                               WizardDescriptor.ValidatingPanel,
                                               WizardDescriptor.FinishablePanel {

    private final ChangeSupport changeSupport;
    private WizardDescriptor wizardDescriptor;
    private JedaProjectPanelVisual component;

    public JedaProjectWizardPanel() {
        this.changeSupport = new ChangeSupport(this);
    }

    @Override
    public final void addChangeListener(ChangeListener listener) {
        this.changeSupport.addChangeListener(listener);
    }

    @Override
    public Component getComponent() {
        if (component == null) {
            component = new JedaProjectPanelVisual(this);
            component.setName(NbBundle.getMessage(JedaProjectWizardPanel.class, "LBL_CreateProjectStep"));
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return new HelpCtx(JedaProjectWizardPanel.class);
    }

    @Override
    public boolean isFinishPanel() {
        return true;
    }

    @Override
    public boolean isValid() {
        getComponent();
        return this.component.valid(this.wizardDescriptor);
    }

    @Override
    public void readSettings(Object settings) {
        this.wizardDescriptor = (WizardDescriptor) settings;
        this.component.read(this.wizardDescriptor);
    }

    @Override
    public final void removeChangeListener(ChangeListener listener) {
        this.changeSupport.removeChangeListener(listener);
    }

    @Override
    public void storeSettings(Object settings) {
        this.component.store((WizardDescriptor) settings);
    }

    @Override
    public void validate() throws WizardValidationException {
        this.getComponent();
        this.component.validate(this.wizardDescriptor);
    }

    void fireChangeEvent() {
        this.changeSupport.fireChange();
    }
}
