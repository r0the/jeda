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
package ch.jeda;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileProxy {

    private final File file;
    private final String filePath;

    public FileProxy(String filePath) {
        if (filePath == null) {
            throw new NullPointerException("filePath");
        }

        this.file = new File(filePath);
        this.filePath = filePath;
    }

    public OutputStream openForWrite() throws IOException {
        return new FileOutputStream(this.file);
    }

    public String getExtension() {
        int pos = this.filePath.indexOf('.');
        if (pos == -1) {
            return "";
        }
        else {
            return this.filePath.substring(pos + 1);
        }
    }

    public boolean makeDirectories() {
        File dir = this.file.getParentFile();
        if (dir == null) {
            return true;
        }
        else {
            return dir.mkdirs();
        }
    }
}
