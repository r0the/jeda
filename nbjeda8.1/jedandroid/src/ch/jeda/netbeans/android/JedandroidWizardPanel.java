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
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

public final class JedandroidWizardPanel implements WizardDescriptor.Panel<WizardDescriptor>,
                                                    WizardDescriptor.ValidatingPanel<WizardDescriptor>,
                                                    WizardDescriptor.FinishablePanel<WizardDescriptor> {

    private final ChangeSupport changeSupport;
    private WizardDescriptor wizardDescriptor;
    private JedandroidPanelVisual component;

    public JedandroidWizardPanel() {
        this.changeSupport = new ChangeSupport(this);
    }

    @Override
    public void addChangeListener(final ChangeListener listener) {
        this.changeSupport.addChangeListener(listener);
    }

    @Override
    public Component getComponent() {
        if (this.component == null) {
            this.component = new JedandroidPanelVisual(this);
            this.component.setName("Name and Location");
        }
        return this.component;
    }

    @Override
    public HelpCtx getHelp() {
        return null;
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
    public void readSettings(final WizardDescriptor settings) {
        this.wizardDescriptor = settings;
        this.component.read(this.wizardDescriptor);
    }

    @Override
    public void removeChangeListener(final ChangeListener listener) {
        this.changeSupport.removeChangeListener(listener);
    }

    @Override
    public void storeSettings(final WizardDescriptor settings) {
        this.component.store(settings);
    }

    @Override
    public void validate() throws WizardValidationException {
        this.getComponent();
        this.component.validate(this.wizardDescriptor);
    }

    protected void fireChangeEvent() {
        this.changeSupport.fireChange();
    }
}
