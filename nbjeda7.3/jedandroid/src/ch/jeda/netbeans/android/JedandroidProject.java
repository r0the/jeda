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

import java.awt.Image;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

class JedandroidProject {

    public static final String PROJECT_TYPE = "org-netbeans-modules-android-project";
    private static final Image ICON = ImageUtilities.loadImage("ch/jeda/netbeans/android/res/icon.png");
    private static final String ANDROID_MANIFEST_XML = "AndroidManifest.xml";
    private static final String ANDROID_MANIFEST_XML_RES = "ch/jeda/netbeans/android/res/android_manifest.xml";
    private static final String APP_ICON = "res/drawable/icon.png";
    private static final String APP_ICON_RES = "ch/jeda/netbeans/android/res/app_icon.png";
    private static final String BUILD_XML = "build.xml";
    private static final String BUILD_XML_RES = "ch/jeda/netbeans/android/res/build.xml";
    private static final String JEDA_PROPERTIES = "jeda.properties";
    private static final String JEDA_PROPERTIES_RES = "ch/jeda/netbeans/android/res/jeda.properties";
    private static final String JEDANDROID_JAR = "libs/jedandroid.jar";
    private static final String JEDANDROID_JAR_RES = "ch/jeda/netbeans/android/res/jedandroid.jar";
    private static final String LIBS = "libs";
    private static final String PROJECT_PROPERTIES = "project.properties";
    private static final String PROJECT_PROPERTIES_RES = "ch/jeda/netbeans/android/res/project.properties";
    private static final String RES = "res";
    private static final String SRC_CH_JEDA_PROJECT = "src/ch/jeda/project";

    static Image annotateIcon(final Project project, final Image orig, final boolean openedNode) {
        if (isJedaProject(project)) {
            return ICON;
        }
        return orig;
    }

    static NodeList createConfigNode(final Project project) {
        if (isJedaProject(project)) {
            try {
                return NodeFactorySupport.fixedNodeList(new Node[]{new JedandroidProject.Node(getJedaPropertiesFile(project), "Jeda Configuration")});
            }
            catch (final DataObjectNotFoundException ex) {
                // ignore
            }
        }

        return NodeFactorySupport.fixedNodeList(new Node[0]);
    }

    static Lookup fixLookup(final Lookup lookup) {
        final Project project = (Project) lookup.lookup(Project.class);
        if (isJedaProject(project)) {
            return Lookups.fixed(new Object[]{new JedandroidProjectIconAnnotator(), new JedandroidProjectOpenedHook(project)});
        }
        else {
            return Lookups.fixed(new Object[0]);
        }
    }

    private static FileObject getJedaPropertiesFile(final Project project) {
        return project.getProjectDirectory().getFileObject(JEDA_PROPERTIES);
    }

    static void init(final FileObject projectDir) {
        FileHelper.addDir(projectDir, LIBS);
        FileHelper.addDir(projectDir, RES);
        FileHelper.addFile(projectDir, ANDROID_MANIFEST_XML, ANDROID_MANIFEST_XML_RES);
        FileHelper.addDir(projectDir, SRC_CH_JEDA_PROJECT);
        FileHelper.addFile(projectDir, JEDA_PROPERTIES, JEDA_PROPERTIES_RES);
        FileHelper.addFile(projectDir, BUILD_XML, BUILD_XML_RES,
                           new FileFilter.BuildXmlFilter(projectDir.getName()));

        FileHelper.addFile(projectDir, PROJECT_PROPERTIES, PROJECT_PROPERTIES_RES,
                           new FileFilter.ProjectPropertiesFilter(projectDir.getName()));

        FileHelper.addFile(projectDir, JEDANDROID_JAR, JEDANDROID_JAR_RES);
        FileHelper.addFile(projectDir, APP_ICON, APP_ICON_RES);
        if (!AndroidCommand.updateProject(projectDir.getPath())) {
            showError("Counldn't find android command. Please check your PATH variable.");
        }
    }

    private static boolean isJedaProject(final Project project) {
        return getJedaPropertiesFile(project) != null &&
               project.getProjectDirectory().getFileObject(ANDROID_MANIFEST_XML) != null;
    }

    private static class JedandroidProjectOpenedHook extends ProjectOpenedHook {

        private final FileObject projectDir;

        JedandroidProjectOpenedHook(final Project project) {
            this.projectDir = project.getProjectDirectory();
        }

        @Override
        protected void projectClosed() {
        }

        @Override
        protected void projectOpened() {
            if (!FileHelper.replaceFile(this.projectDir, BUILD_XML, BUILD_XML_RES,
                                        new FileFilter.BuildXmlFilter(this.projectDir.getName()))) {
                showError("Cannot replace build.xml");
            }

            if (!FileHelper.replaceFile(this.projectDir, JEDANDROID_JAR, JEDANDROID_JAR_RES)) {
                showError("Cannot replace jedandroid.jar");
            }
        }
    }

    private static class Node extends FilterNode {

        public Node(final FileObject fileObject, final String name) throws DataObjectNotFoundException {
            super(DataObject.find(fileObject).getNodeDelegate());
            this.setDisplayName(name);
        }
    }

    public static void showError(final String message) {
        final NotifyDescriptor nd = new NotifyDescriptor.Message(message, NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }
}
