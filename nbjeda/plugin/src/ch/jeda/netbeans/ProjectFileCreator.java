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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

public class ProjectFileCreator {

    private final Map<String, FileFilter> fileFilters;
    private final FileObject projectRoot;

    public ProjectFileCreator(FileObject projectRoot) {
        this.fileFilters = new HashMap();
        this.projectRoot = projectRoot;
    }

    public final void addFileFilter(String fileName, FileFilter fileFilter) {
        this.fileFilters.put(fileName, fileFilter);
    }

    public final void createFile(InputStream templateSource, String fileName) throws IOException {
        FileObject fileObject = FileUtil.createData(this.projectRoot, fileName);
        FileFilter fileFilter = this.fileFilters.get(fileName);
        boolean success = false;
        if (fileFilter != null) {
            fileFilter.setProjectName(this.projectRoot.getName());
            OutputStream out = fileObject.getOutputStream();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileUtil.copy(templateSource, baos);
                fileFilter.doExecute(new ByteArrayInputStream(baos.toByteArray()), out);
                success = true;
            }
            catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
            finally {
                out.close();
            }
        }

        if (!success) {
            OutputStream out = fileObject.getOutputStream();
            try {
                FileUtil.copy(templateSource, out);
            }
            finally {
                out.close();
            }
        }
    }

    protected FileObject getProjectRoot() {
        return this.projectRoot;
    }

    public static abstract class FileFilter {

        private String projectName;

        protected abstract void doExecute(InputStream is, OutputStream os) throws Exception;

        protected final String getProjectName() {
            return this.projectName;
        }

        private void setProjectName(String value) {
            this.projectName = value;
        }
    }

    public static abstract class TextFileFilter extends FileFilter {

        @Override
        protected final void doExecute(InputStream is, OutputStream os) throws IOException {
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
