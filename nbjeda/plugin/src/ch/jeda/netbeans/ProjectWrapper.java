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
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
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
    private static final String DEFAULT_PACKAGE = "src/ch/jeda/project";
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

    public final void convertTo(Platform platform) {
        switch (platform) {
            case Android:
                this.convertTo(new AndroidProjectWrapper(this.projectRoot));
                break;
            case Java:
                this.convertTo(new JavaProjectWrapper(this.projectRoot));
                break;
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
            this.addDir(DEFAULT_PACKAGE);
            this.addFile(JEDA_PROPERTIES, RES_JEDA_PROPERTIES);
            this.doInit();
        }
        catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            this.showError("Error while initializing project: " + ex.toString());
        }
    }

    public boolean isJedaProject() {
        return this.getPlatform() != Platform.Unknown;
    }

    protected final void addDir(String targetPath) throws IOException {
        final String[] dirs = targetPath.split("/");
        FileObject fo = this.projectRoot;
        int i = 0;

        while (i < dirs.length) {
            final FileObject child = fo.getFileObject(dirs[i]);
            if (child != null) {
                fo = child;
            }
            else {
                fo = fo.createFolder(dirs[i]);
            }

            ++i;
        }
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

    protected boolean checkConvert() {
        return true;
    }

    protected final void deleteFile(String name) throws IOException {
        FileObject fo = this.projectRoot.getFileObject(name);
        if (fo != null) {
            fo.delete();
        }
    }

    protected void doCleanup() throws Exception {
    }

    protected void doInit() throws Exception {
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

    protected final void showError(String message) {
        NotifyDescriptor nd = new NotifyDescriptor.Message(message,
                                                           NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }

    private void close() {
        OpenProjects.getDefault().close(new Project[]{this.project});
    }

    private void convertTo(ProjectWrapper target) {
        if (!target.checkConvert()) {
            return;
        }

        this.close();
        try {
            this.doCleanup();
            target.doInit();
            target.open();
        }
        catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            this.showError("Cannot switch target platform: " + ex.getMessage());
        }
    }

    private void open() {
        try {
            this.projectRoot.getParent().refresh();
            this.project = ProjectManager.getDefault().findProject(this.projectRoot);
            OpenProjects.getDefault().open(new Project[]{this.project}, false);
            OpenProjects.getDefault().setMainProject(this.project);
        }
        catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            this.showError("Could not open project '" + this.projectRoot.getName() + "'. Please open it manually.");
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
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(is, baos);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
            final PrintWriter writer = new PrintWriter(os);
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
