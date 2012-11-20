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
package ch.jeda.netbeans.java.template;

import ch.jeda.netbeans.TemplateUnpacker;
import java.io.InputStream;
import java.io.OutputStream;
import org.openide.filesystems.FileObject;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class JedaProjectTemplateUnpacker extends TemplateUnpacker {

    public JedaProjectTemplateUnpacker(FileObject projectRoot) {
        super(projectRoot);
        this.addFileFilter("nbproject/project.xml", new ProjectXmlFilter());
        this.addFileFilter("build.xml", new TextFileFilter() {

            @Override
            protected String filterLine(String line) {
                return line.replace("${ProjectName}", this.getProjectName());
            }
        });

        this.addFileFilter("nbproject/project.properties", new TextFileFilter() {

            @Override
            protected String filterLine(String line) {
                if (line.startsWith("application.title=")) {
                    return "application.title=" + this.getProjectName();
                }
                else if (line.startsWith("dist.jar=")) {
                    return "dist.jar=${dist.dir}/" + this.getProjectName() + ".jar";
                }
                else {
                    return line;
                }
            }
        });
    }

    protected static class ProjectXmlFilter extends FileFilter {

        @Override
        protected void doExecute(InputStream is, OutputStream os) throws Exception {
            Document doc = XMLUtil.parse(new InputSource(is), false, false, null, null);
            NodeList nl = doc.getDocumentElement().getElementsByTagName("name");
            if (nl != null) {
                for (int i = 0; i < nl.getLength(); i++) {
                    Element el = (Element) nl.item(i);
                    if (el.getParentNode() != null && "data".equals(el.getParentNode().getNodeName())) {
                        NodeList nl2 = el.getChildNodes();
                        if (nl2.getLength() > 0) {
                            nl2.item(0).setNodeValue(this.getProjectName());
                        }

                        break;
                    }
                }
            }

            XMLUtil.write(doc, os, "UTF-8");
        }
    }
}
