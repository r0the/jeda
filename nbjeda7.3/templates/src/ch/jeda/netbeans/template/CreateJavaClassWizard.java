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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.java.classpath.ClassPath;
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

public class CreateJavaClassWizard extends AbstractWizard {

    @Override
    protected Set<?> createFiles(final WizardDescriptor wizard) throws IOException {
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

    @Override
    protected WizardDescriptor.Panel createPanel(final WizardDescriptor wizard) {
        final Project project = Templates.getProject(wizard);
        final Sources sources = (Sources) ProjectUtils.getSources(project);
        final SourceGroup[] groups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
        return JavaTemplates.createPackageChooser(project, groups);
    }

    @Override
    protected String stepName() {
        return NbBundle.getMessage(JedaProgramWizardIterator.class, "LBL_CreateClassStep");
    }
}
