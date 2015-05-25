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
package ch.jeda.netbeans.java;

import ch.jeda.netbeans.support.BuildXmlFilter;
import ch.jeda.netbeans.support.ProjectFile;
import ch.jeda.netbeans.support.ProjectPropertiesFilter;
import ch.jeda.netbeans.support.ProjectType;
import java.awt.Image;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;

public final class JedaProjectType extends ProjectType {

    public static final String PROJECT_TYPE = "org-netbeans-modules-java-j2seproject";
    private static final String BUILD_XML_RES = "ch/jeda/netbeans/java/res/build.xml";
    private static final Image ICON = ImageUtilities.loadImage("ch/jeda/netbeans/java/res/icon.png");
    private static final String JEDA_JAR = "libs/jeda.jar";
    private static final String JEDA_JAR_RES = "ch/jeda/netbeans/java/res/jeda.jar";
    private static final String JEDA_PROPERTIES_RES = "ch/jeda/netbeans/java/res/jeda.properties";
    private static final String LIBS = "libs";
    private static final String NBPROJECT = "nbproject";
    private static final String PROJECT_PROPERTIES = "nbproject/project.properties";
    private static final String PROJECT_PROPERTIES_RES = "ch/jeda/netbeans/java/res/project.properties";
    private static final String PROJECT_XML = "nbproject/project.xml";
    private static final String PROJECT_XML_RES = "ch/jeda/netbeans/java/res/project.xml";
    private static final String RES = "res";
    private static final String SRC_CH_JEDA_PROJECT = "src/ch/jeda/project";

    static ProjectFile getResourceDirectory(final Project project) {
        return ProjectFile.get(project, RES);
    }

    static void init(final FileObject projectDir) {
        addDirectory(projectDir, LIBS);
        addDirectory(projectDir, NBPROJECT);
        addDirectory(projectDir, RES);
        addDirectory(projectDir, SRC_CH_JEDA_PROJECT);
        ProjectFile.get(projectDir, JEDA_PROPERTIES).createFrom(JEDA_PROPERTIES_RES);
        ProjectFile.get(projectDir, BUILD_XML).createFrom(BUILD_XML_RES, new BuildXmlFilter(projectDir.getName()));
        ProjectFile.get(projectDir, PROJECT_PROPERTIES).createFrom(PROJECT_PROPERTIES_RES, new ProjectPropertiesFilter(projectDir.getName()));
        ProjectFile.get(projectDir, PROJECT_XML).createFrom(PROJECT_XML_RES, new ProjectXmlFilter(projectDir.getName()));
        ProjectFile.get(projectDir, JEDA_JAR).createFrom(JEDA_JAR_RES);
    }

    @Override
    protected boolean matches(final Project project) {
        return ProjectFile.get(project, JEDA_PROPERTIES).exists() && ProjectFile.get(project, NBPROJECT).exists();
    }

    @Override
    public Image annotateIcon(final Image orig, final boolean openedNode) {
        return ICON;
    }

    @Override
    protected String buildXmlResourcePath() {
        return BUILD_XML_RES;
    }

    @Override
    protected String codeNameBase() {
        return "ch.jeda.netbeans.java";
    }

    @Override
    protected String jedaLibraryPath() {
        return JEDA_JAR;
    }

    @Override
    protected String jedaLibraryResourcePath() {
        return JEDA_JAR_RES;
    }

    @Override
    protected void onProjectClosed(final Project project) {
    }
}
