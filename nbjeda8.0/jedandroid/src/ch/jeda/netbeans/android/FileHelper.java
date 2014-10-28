/*
 * Copyright (C) 2013 - 2014 by Stefan Rothe
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class FileHelper {

    static void addDir(final FileObject baseDir, final String targetPath) {
        try {
            String[] dirs = targetPath.split("/");
            FileObject fo = baseDir;
            for (int i = 0; i < dirs.length; i++) {
                FileObject child = fo.getFileObject(dirs[i]);
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

    static boolean addFile(final FileObject baseDir, final String targetPath, final String resourcePath) {
        if (exists(baseDir.getFileObject(targetPath))) {
            return false;
        }

        OutputStream out = null;
        try {
            out = FileUtil.createData(baseDir, targetPath).getOutputStream();
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

    static boolean addFile(final FileObject baseDir, final String targetPath, final String resourcePath,
                           final FileFilter filter) {
        if (exists(baseDir.getFileObject(targetPath))) {
            return false;
        }

        OutputStream out = null;
        try {
            out = FileUtil.createData(baseDir, targetPath).getOutputStream();
            InputStream source = openResource(resourcePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(source, baos);
            filter.execute(new ByteArrayInputStream(baos.toByteArray()), out);
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

    static boolean deleteFile(final FileObject baseDir, final String name) {
        final FileObject fo = baseDir.getFileObject(name);
        if (fo != null) {
            try {
                fo.delete();
                return true;
            }
            catch (final IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return false;
    }

    static boolean exists(final FileObject file) {
        return file != null && file.isValid();
    }

    static boolean replaceFile(final FileObject baseDir, final String targetPath, final String resourcePath) {
        deleteFile(baseDir, targetPath);
        return addFile(baseDir, targetPath, resourcePath);
    }

    static boolean replaceFile(final FileObject baseDir, final String targetPath, final String resourcePath,
                               FileFilter filter) {
        return deleteFile(baseDir, targetPath) && addFile(baseDir, targetPath, resourcePath, filter);
    }

    private static InputStream openResource(final String resourcePath) throws IOException {
        final ClassLoader cl = Lookup.getDefault().lookup(ClassLoader.class);
        final URL url = cl.getResource(resourcePath);
        return url.openStream();
    }
}
