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
package ch.jeda.netbeans.template;

import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle.Messages;

@TemplateRegistration(
    folder = "Jeda",
    content = "JedaElement",
    displayName = "#JedaElementWizardIterator_displayName",
    iconBase = "ch/jeda/netbeans/template/icon.png",
    description = "JedaElementDescription.html",
    scriptEngine = "freemarker")
@Messages(value = {"JedaElementWizardIterator_displayName=Jeda Element"})
public final class JedaElementWizardIterator extends CreateJavaClassWizardIterator
    implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {
}