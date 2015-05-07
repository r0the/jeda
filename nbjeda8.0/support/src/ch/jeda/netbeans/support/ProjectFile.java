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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class ProjectFile {

    private final FileObject projectDirectory;
    private final String path;

    public static ProjectFile get(final Project project, final String path) {
        return new ProjectFile(project.getProjectDirectory(), path);
    }

    public static ProjectFile get(final FileObject projectDirectory, final String path) {
        return new ProjectFile(projectDirectory, path);
    }

    private ProjectFile(final FileObject projectDirectory, final String path) {
        this.projectDirectory = projectDirectory;
        this.path = path;
    }

    public boolean exists() {
        final FileObject file = this.fileObject();
        return file != null && file.isValid();
    }

    public boolean createFrom(final String resourcePath) {
        if (exists()) {
            return false;
        }

        OutputStream out = null;
        try {
            out = FileUtil.createData(this.projectDirectory, this.path).getOutputStream();
            FileUtil.copy(openResource(resourcePath), out);
            return true;
        }
        catch (final IOException ex) {
            Exceptions.printStackTrace(ex);
            return false;
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    public boolean createFrom(final String resourcePath, final FileFilter filter) {
        if (exists()) {
            return false;
        }

        OutputStream out = null;
        try {
            out = FileUtil.createData(this.projectDirectory, this.path).getOutputStream();
            InputStream source = openResource(resourcePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(source, baos);
            filter.apply(new ByteArrayInputStream(baos.toByteArray()), out);
            return true;
        }
        catch (final Exception ex) {
            Exceptions.printStackTrace(ex);
            return false;
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (final IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    public Node createNode(final String name, final Action... additionalActions) {
        final FileObject file = this.fileObject();
        if (file != null && file.isValid()) {
            try {
                return new FileNode(file, name, additionalActions);
            }
            catch (final DataObjectNotFoundException ex) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public boolean delete() {
        final FileObject file = this.fileObject();
        if (file != null && file.canWrite()) {
            try {
                file.delete();
                return true;
            }
            catch (final IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return false;
    }

    private FileObject fileObject() {
        return this.projectDirectory.getFileObject(this.path);
    }

    public Version readJarImplementationVersion() {
        final FileObject file = this.fileObject();
        if (file != null && file.canRead()) {
            try {
                Manifest manifest = new JarFile(file.getPath()).getManifest();
                return new Version(manifest.getMainAttributes().getValue("Implementation-Version"));
            }
            catch (final IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return null;
    }

    private static InputStream openResource(final String resourcePath) throws IOException {
        final ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        final URL url = cl.getResource(resourcePath);
        return url.openStream();
    }

    private static class FileNode extends FilterNode {

        private final Action[] additionalActions;

        public FileNode(final FileObject fileObject, final String name, final Action[] additionalActions) throws DataObjectNotFoundException {
            super(DataObject.find(fileObject).getNodeDelegate());
            this.setDisplayName(name);
            this.additionalActions = additionalActions;
        }

        @Override
        public Action[] getActions(boolean context) {
            final Action[] commonActions = super.getActions(context);
            final Action[] result = new Action[commonActions.length + this.additionalActions.length];
            for (int i = 0; i < this.additionalActions.length; ++i) {
                result[i] = this.additionalActions[i];
            }

            for (int i = 0; i < commonActions.length; ++i) {
                result[this.additionalActions.length + i] = commonActions[i];
            }

            return result;
        }
    }
}
