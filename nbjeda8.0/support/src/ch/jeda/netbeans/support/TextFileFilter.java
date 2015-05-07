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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.openide.filesystems.FileUtil;

public abstract class TextFileFilter implements FileFilter {

    @Override
    public final void apply(final InputStream in, final OutputStream out) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileUtil.copy(in, baos);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));

        PrintWriter writer = new PrintWriter(out);
        try {
            while (reader.ready()) {
                writer.println(filterLine(reader.readLine()));
            }
        }
        finally {
            writer.close();
        }
    }

    protected abstract String filterLine(String line);
}
