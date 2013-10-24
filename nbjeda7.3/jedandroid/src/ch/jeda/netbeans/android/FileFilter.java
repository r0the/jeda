/*
 * Copyright (C) 2013 by Stefan Rothe
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.openide.filesystems.FileUtil;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

abstract class FileFilter {

    protected abstract void execute(InputStream paramInputStream, OutputStream paramOutputStream) throws Exception;

    static abstract class TextFileFilter extends FileFilter {

        @Override
        protected final void execute(final InputStream is, final OutputStream os) throws Exception {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(is, baos);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));

            final PrintWriter writer = new PrintWriter(os);
            try {
                while (reader.ready()) {
                    writer.println(filterLine(reader.readLine()));
                }
            }
            finally {
                writer.close();
            }
        }

        protected abstract String filterLine(String paramString);
    }

    static class BuildXmlFilter extends TextFileFilter {

        private final String projectName;

        public BuildXmlFilter(final String projectName) {
            this.projectName = projectName;
        }

        @Override
        protected String filterLine(final String line) {
            return line.replace("${ProjectName}", this.projectName);
        }
    }

    static class ProjectPropertiesFilter extends TextFileFilter {

        private final String projectName;

        public ProjectPropertiesFilter(final String projectName) {
            this.projectName = projectName;
        }

        @Override
        protected String filterLine(final String line) {
            if (line.startsWith("application.title=")) {
                return "application.title=" + this.projectName;
            }
            else if (line.startsWith("dist.jar=")) {
                return "dist.jar=${dist.dir}/" + this.projectName + ".jar";
            }
            else {
                return line;
            }
        }
    }

    static class ProjectXmlFilter extends FileFilter {

        private final String projectName;

        public ProjectXmlFilter(final String projectName) {
            this.projectName = projectName;
        }

        @Override
        protected void execute(final InputStream is, final OutputStream os) throws Exception {
            final Document doc = XMLUtil.parse(new InputSource(is), false, false, null, null);
            final NodeList nl = doc.getDocumentElement().getElementsByTagName("name");
            if (nl != null) {
                for (int i = 0; i < nl.getLength(); i++) {
                    final Element el = (Element) nl.item(i);
                    if ((el.getParentNode() != null) && ("data".equals(el.getParentNode().getNodeName()))) {
                        NodeList nl2 = el.getChildNodes();
                        if (nl2.getLength() <= 0) {
                            break;
                        }

                        nl2.item(0).setNodeValue(this.projectName);
                        break;
                    }
                }
            }

            XMLUtil.write(doc, os, "UTF-8");
        }
    }
}
