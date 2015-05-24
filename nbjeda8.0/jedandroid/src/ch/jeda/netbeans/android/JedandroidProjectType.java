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
package ch.jeda.netbeans.android;

import ch.jeda.netbeans.support.BuildXmlFilter;
import ch.jeda.netbeans.support.Dialog;
import ch.jeda.netbeans.support.ProjectFile;
import ch.jeda.netbeans.support.ProjectPropertiesFilter;
import ch.jeda.netbeans.support.ProjectType;
import java.awt.Image;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

public class JedandroidProjectType extends ProjectType {

    public static final String PROJECT_TYPE = "org-netbeans-modules-android-project";
    private static final Image ICON = ImageUtilities.loadImage("ch/jeda/netbeans/android/res/icon.png");
    private static final String ANDROID_MANIFEST_XML = "AndroidManifest.xml";
    private static final String ANDROID_MANIFEST_XML_RES = "ch/jeda/netbeans/android/res/android_manifest.xml";
    private static final String ANDROID_SUPPORT_V13_JAR = "libs/android-support-v13.jar";
    private static final String ANDROID_SUPPORT_V13_JAR_RES = "ch/jeda/netbeans/android/res/android-support-v13.jar";
    private static final String APP_ICON = "res/drawable/icon.png";
    private static final String APP_ICON_RES = "ch/jeda/netbeans/android/res/app_icon.png";
    private static final String BUILD_XML = "build.xml";
    private static final String BUILD_XML_RES = "ch/jeda/netbeans/android/res/build.xml";
    private static final String JEDA_PROPERTIES_RES = "ch/jeda/netbeans/android/res/jeda.properties";
    private static final String JEDANDROID_JAR = "libs/jedandroid.jar";
    private static final String JEDANDROID_JAR_RES = "ch/jeda/netbeans/android/res/jedandroid.jar";
    private static final String LIBS = "libs";
    private static final String PROJECT_PROPERTIES = "project.properties";
    private static final String PROJECT_PROPERTIES_RES = "ch/jeda/netbeans/android/res/project.properties";
    private static final String RES = "res";
    private static final String SRC_CH_JEDA_PROJECT = "src/ch/jeda/project";

    static void init(final FileObject projectDir) {
        addDirectory(projectDir, LIBS);
        addDirectory(projectDir, RES);
        addDirectory(projectDir, SRC_CH_JEDA_PROJECT);
        ProjectFile.get(projectDir, ANDROID_MANIFEST_XML).createFrom(ANDROID_MANIFEST_XML_RES);
        ProjectFile.get(projectDir, JEDA_PROPERTIES).createFrom(JEDA_PROPERTIES_RES);
        ProjectFile.get(projectDir, BUILD_XML).createFrom(BUILD_XML_RES, new BuildXmlFilter(projectDir.getName()));
        ProjectFile.get(projectDir, PROJECT_PROPERTIES).createFrom(PROJECT_PROPERTIES_RES, new ProjectPropertiesFilter(projectDir.getName()));
        ProjectFile.get(projectDir, JEDANDROID_JAR).createFrom(JEDANDROID_JAR_RES);
        ProjectFile.get(projectDir, ANDROID_SUPPORT_V13_JAR).createFrom(ANDROID_SUPPORT_V13_JAR_RES);
        ProjectFile.get(projectDir, APP_ICON).createFrom(APP_ICON_RES);
        if (!AndroidCommand.updateProject(projectDir.getPath())) {
            Dialog.showError("Counldn't find android command. Please check your PATH variable.");
        }
    }

    @Override
    public Image annotateIcon(final Image orig, final boolean openedNode) {
        return ICON;
    }

    @Override
    protected String codeNameBase() {
        return "ch.jeda.netbeans.android";
    }

    @Override
    protected String jedaLibraryPath() {
        return JEDANDROID_JAR;
    }

    @Override
    protected String jedaLibraryResourcePath() {
        return JEDANDROID_JAR_RES;
    }

    @Override
    protected boolean matches(final Project project) {
        return ProjectFile.get(project, JEDA_PROPERTIES).exists() && ProjectFile.get(project, ANDROID_MANIFEST_XML).exists();
    }

    @Override
    protected void onProjectClosed(final Project project) {
    }
}
