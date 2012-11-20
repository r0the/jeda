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
package ch.jeda.netbeans;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

public class ProjectWrapper {

    public enum Platform {

        Android, Java, Unknown
    }
    protected static final String ANDROID_MANIFEST_XML = "AndroidManifest.xml";
    protected static final String BUILD_XML = "build.xml";
    protected static final String JEDA_PROPERTIES = "jeda.properties";
    protected static final String NB_PROJECT = "nbproject";
    protected static final String RES_ICON_PNG = "ch/jeda/netbeans/resources/logo-16x16.png";
    private static final String RES_JEDA_PROPERTIES = "ch/jeda/netbeans/resources/jeda.properties";
    private final FileObject projectRoot;
    private Project project;

    public static ProjectWrapper forProject(Project project) {
        return forProject(project.getProjectDirectory(), project);
    }

    public static ProjectWrapper forProject(FileObject projectRoot, Project project) {
        if (projectRoot.getFileObject(JEDA_PROPERTIES) == null) {
            // Not a Jeda project
            return new ProjectWrapper(projectRoot, project);
        }
        else if (projectRoot.getFileObject(ANDROID_MANIFEST_XML) != null) {
            return new AndroidProjectWrapper(projectRoot, project);
        }
        else if (projectRoot.getFileObject(NB_PROJECT) != null) {
            return new JavaProjectWrapper(projectRoot, project);
        }
        else {
            return new ProjectWrapper(projectRoot, project);
        }
    }

    protected ProjectWrapper(FileObject projectRoot, Project project) {
        this.projectRoot = projectRoot;
        this.project = project;
    }

    public final ProjectWrapper convertTo(Platform platform) {
        switch (platform) {
            case Android:
                return this.convertTo(new AndroidProjectWrapper(this.projectRoot));
            case Java:
                return this.convertTo(new JavaProjectWrapper(this.projectRoot));
            default:
                return null;
        }
    }

    public Image getIcon() {
        return null;
    }

    public final FileObject getJedaPropertiesFiele() {
        return this.projectRoot.getFileObject(JEDA_PROPERTIES);
    }

    public Platform getPlatform() {
        return Platform.Unknown;
    }

    public final void init() {
        try {
            this.addDir("src");
            this.addFile(JEDA_PROPERTIES, RES_JEDA_PROPERTIES);
            this.doInit();
        }
        catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public boolean isJedaProject() {
        return this.getPlatform() != Platform.Unknown;
    }

    protected void doCleanup() throws Exception {
    }

    protected void doInit() throws Exception {
    }

    protected final void addDir(String targetPath) throws IOException {
        // Do not overwrite existing files
        if (this.projectRoot.getFileObject(targetPath) != null) {
            return;
        }

        this.projectRoot.createFolder(targetPath);
    }

    protected final void addFile(String targetPath, String resourcePath) throws IOException {
        // Do not overwrite existing files
        if (this.projectRoot.getFileObject(targetPath) != null) {
            return;
        }

        OutputStream out = FileUtil.createData(this.projectRoot, targetPath).getOutputStream();
        try {
            FileUtil.copy(Util.openResource(resourcePath), out);
        }
        finally {
            out.close();
        }
    }

    protected final void addFile(String targetPath, String resourcePath, FileFilter filter) throws Exception {
        // Do not overwrite existing files
        if (this.projectRoot.getFileObject(targetPath) != null) {
            return;
        }

        OutputStream out = FileUtil.createData(this.projectRoot, targetPath).getOutputStream();
        InputStream source = Util.openResource(resourcePath);
        filter.setProjectWrapper(this);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(source, baos);
            filter.execute(new ByteArrayInputStream(baos.toByteArray()), out);
        }
        finally {
            out.close();
        }


    }

    protected final void deleteFile(String name) throws IOException {
        FileObject fo = this.projectRoot.getFileObject(name);
        if (fo != null) {
            fo.delete();
        }
    }

    protected final String getName() {
        return this.projectRoot.getName();
    }

    protected final String getRootDir() {
        return this.projectRoot.getPath();
    }

    protected final void rename(String newProjectName) throws IOException {
        this.projectRoot.rename(FileLock.NONE, newProjectName, "");
    }

    protected final void replaceFile(String targetPath, String resourcePath) throws IOException {
        this.deleteFile(targetPath);
        this.addFile(targetPath, resourcePath);
    }

    private void close() {
        OpenProjects.getDefault().close(new Project[]{this.project});
    }

    public final ProjectWrapper convertTo(ProjectWrapper target) {
        this.close();
        try {
            this.doCleanup();
            target.init();
            target.open();
        }
        catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }

        return target;
    }

    private void open() {
        try {
            Util.log("Opening project " + this.projectRoot.getName());
            this.projectRoot.getParent().refresh();
            this.project = ProjectManager.getDefault().findProject(this.projectRoot);
            OpenProjects.getDefault().open(new Project[]{this.project}, false);
            OpenProjects.getDefault().setMainProject(this.project);
        }
        catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public abstract static class FileFilter {

        private ProjectWrapper projectWrapper;

        protected abstract void execute(InputStream in, OutputStream out) throws Exception;

        void setProjectWrapper(ProjectWrapper value) {
            this.projectWrapper = value;
        }

        protected ProjectWrapper getProjectWrapper() {
            return this.projectWrapper;
        }
    }

    public static abstract class TextFileFilter extends FileFilter {

        @Override
        protected final void execute(InputStream is, OutputStream os) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(is, baos);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
            PrintWriter writer = new PrintWriter(os);
            try {
                while (reader.ready()) {
                    writer.println(this.filterLine(reader.readLine()));
                }
            }
            finally {
                writer.close();
            }
        }

        protected abstract String filterLine(String line);
    }
}
