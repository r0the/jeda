/*
 * Copyright (C) 2012 - 2013 by Stefan Rothe
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
package ch.jeda.netbeans;

import java.awt.Image;
import java.io.File;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

public class AndroidProjectWrapper extends ProjectWrapper {

    private static final String ANDROID_SUFFIX = "_android";
    private static final String ICON_PNG = "res/drawable/icon.png";
    private static final String JEDANDROID_JAR = "libs/jedandroid.jar";
    private static final String PROJECT_PROPERTIES = "project.properties";
    private static final String RES_ANDROID_MANIFEST_XML = "ch/jeda/netbeans/resources/android/android_manifest.xml";
    private static final String RES_APP_ICON_ANDROID_PNG = "ch/jeda/netbeans/resources/android/icon.png";
    private static final String RES_BUILD_XML = "ch/jeda/netbeans/resources/android/build.xml";
    private static final String RES_ICON_ANDROID_PNG = "ch/jeda/netbeans/resources/logo-android-16x16.png";
    private static final String RES_JEDANDROID_JAR = "ch/jeda/netbeans/resources/android/jedandroid.jar";
    private static final String RES_PROJECT_PROPERTIES = "ch/jeda/netbeans/resources/android/project.properties";
    private static Image ICON;

    public AndroidProjectWrapper(FileObject projectRoot) {
        super(projectRoot, null);
    }

    public AndroidProjectWrapper(FileObject projectRoot, Project project) {
        super(projectRoot, project);
    }

    @Override
    public Image getIcon() {
        if (ICON == null) {
            ICON = ImageUtilities.loadImage(RES_ICON_ANDROID_PNG);
        }

        return ICON;
    }

    @Override
    public Platform getPlatform() {
        return Platform.Android;
    }

    @Override
    protected void doCleanup() throws Exception {
        this.deleteFile(ANDROID_MANIFEST_XML);
        this.deleteFile(BUILD_XML);
        this.deleteFile("bin");
        this.deleteFile("gen");
        this.deleteFile("libs/jedandroid.jar");
        this.deleteFile("local.properties");
        this.deleteFile("nbandroid");
        this.deleteFile("project.properties");
        this.rename(this.getName().replace(ANDROID_SUFFIX, ""));
    }

    @Override
    protected boolean checkConvert() {
        File targetDir = new File(new File(this.getRootDir()).getParentFile(), this.getName() + ANDROID_SUFFIX);
        if (targetDir.exists()) {
            this.showError("Cannot convert project, directory '" + targetDir + "' already exists.");
            return false;
        }

        return true;
    }

    @Override
    protected void doInit() throws Exception {
        this.replaceFile(ANDROID_MANIFEST_XML, RES_ANDROID_MANIFEST_XML);
        this.addFile(BUILD_XML, RES_BUILD_XML, new TextFileFilter() {
            @Override
            protected String filterLine(String line) {
                return line.replace("${ProjectName}", this.getProjectWrapper().getName());
            }
        });
        this.replaceFile(JEDANDROID_JAR, RES_JEDANDROID_JAR);
        this.addFile(ICON_PNG, RES_APP_ICON_ANDROID_PNG);
        this.addFile(PROJECT_PROPERTIES, RES_PROJECT_PROPERTIES);

        this.rename(this.getName() + ANDROID_SUFFIX);

        ProcessBuilder pb = new ProcessBuilder("android", "update", "project", "-p", this.getRootDir());
        Process p = pb.start();
        try {
            p.waitFor();
        }
        catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
