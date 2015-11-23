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
package ch.jeda.netbeans.support;

import java.awt.Image;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

public abstract class ProjectType {

    public static final String BUILD_XML = "build.xml";
    public static final String JEDA_PROPERTIES = "jeda.properties";
    private static final Set<ProjectType> projectTypes = new HashSet<ProjectType>();

    public static Lookup createAdditionalLookup(final Lookup lookup) {
        final Project project = lookup.lookup(Project.class);
        final ProjectType projectType = lookup(project);
        if (projectType != null) {
            return Lookups.fixed(new Object[]{new ProjectOpenedHookImp(projectType, project)});
        }
        else {
            return Lookups.fixed(new Object[0]);
        }
    }

    public static final NodeList<?> createJedaConfigurationNode(final Project project) {
        final ProjectType projectType = lookup(project);
        if (projectType == null) {
            return NodeFactorySupport.fixedNodeList();
        }

        final Node node = ProjectFile.get(project, JEDA_PROPERTIES).createNode(
            "Jeda Configuration",
            new InfoAction(projectType, project),
            new UpdateLibraryAction(projectType, project)
        );

        if (node == null) {
            return NodeFactorySupport.fixedNodeList();
        }

        return NodeFactorySupport.fixedNodeList(node);
    }

    public static void addDirectory(final FileObject baseDir, final String targetPath) {
        try {
            final String[] dirs = targetPath.split("/");
            FileObject fo = baseDir;
            for (int i = 0; i < dirs.length; i++) {
                final FileObject child = fo.getFileObject(dirs[i]);
                if (child != null) {
                    fo = child;
                }
                else {
                    fo = fo.createFolder(dirs[i]);
                }
            }
        }
        catch (final IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static ProjectType lookup(final Project project) {
        if (project == null) {
            return null;
        }

        for (final ProjectType projectType : projectTypes) {
            if (projectType.matches(project)) {
                return projectType;
            }
        }

        return null;
    }

    public static void register(final ProjectType projectType) {
        projectTypes.add(projectType);
    }

    public final Version pluginVersion() {
        return ModuleVersion.lookup(codeNameBase());
    }

    public final Version projectVersion(final Project project) {
        return ProjectFile.get(project, jedaLibraryPath()).readJarImplementationVersion();
    }

    public final void updateProject(final Project project) {
        final ProjectFile library = ProjectFile.get(project, this.jedaLibraryPath());
        library.delete();
        library.createFrom(jedaLibraryResourcePath());
        final ProjectFile buildXml = ProjectFile.get(project, BUILD_XML);
        buildXml.delete();
        buildXml.createFrom(buildXmlResourcePath(), new BuildXmlFilter(project.getProjectDirectory().getName()));
    }

    public abstract Image annotateIcon(final Image orig, final boolean openedNode);

    protected abstract String buildXmlResourcePath();

    protected abstract String codeNameBase();

    protected abstract String jedaLibraryPath();

    protected abstract String jedaLibraryResourcePath();

    protected abstract boolean matches(Project project);

    protected abstract void onProjectClosed(Project project);

    protected void onProjectOpened(Project project) {
        final ProjectFile jedaJar = ProjectFile.get(project, this.jedaLibraryPath());
        if (!jedaJar.exists()) {
            jedaJar.createFrom(this.jedaLibraryResourcePath());
        }

        final Version projectVersion = this.projectVersion(project);
        final Version pluginVersion = this.pluginVersion();
        if (pluginVersion != null && projectVersion != null &&
            pluginVersion.compareTo(projectVersion) > 0 && pluginVersion.major == projectVersion.major) {
            this.updateProject(project);
        }
    }

    private static class ProjectOpenedHookImp extends ProjectOpenedHook {

        private final ProjectType projectType;
        private final Project project;

        ProjectOpenedHookImp(final ProjectType projectType, final Project project) {
            this.projectType = projectType;
            this.project = project;
        }

        @Override
        protected void projectOpened() {
            this.projectType.onProjectOpened(this.project);
        }

        @Override
        protected void projectClosed() {
            this.projectType.onProjectClosed(this.project);
        }
    }
}
