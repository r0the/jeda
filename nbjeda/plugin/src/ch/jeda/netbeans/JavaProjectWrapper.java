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
import java.io.InputStream;
import java.io.OutputStream;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class JavaProjectWrapper extends ProjectWrapper {

    private static final String PROJECT_PROPERTIES = "nbproject/project.properties";
    private static final String PROJECT_XML = "nbproject/project.xml";
    private static final String RES_BUILD_XML = "ch/jeda/netbeans/resources/java/build.xml";
    private static final String RES_ICON_JAVA_PNG = "ch/jeda/netbeans/resources/logo-java-16x16.png";
    private static final String RES_PROJECT_PROPERTIES = "ch/jeda/netbeans/resources/java/project.properties";
    private static final String RES_PROJECT_XML = "ch/jeda/netbeans/resources/java/project.xml";
    private static Image ICON;

    public JavaProjectWrapper(FileObject projectRoot) {
        super(projectRoot, null);
    }

    public JavaProjectWrapper(FileObject projectRoot, Project project) {
        super(projectRoot, project);
    }

    @Override
    public Image getIcon() {
        if (ICON == null) {
            ICON = ImageUtilities.loadImage(RES_ICON_JAVA_PNG);
        }

        return ICON;
    }

    @Override
    public Platform getPlatform() {
        return Platform.Java;
    }

    @Override
    protected void doCleanup() throws Exception {
        this.deleteFile(NB_PROJECT);
        this.deleteFile(BUILD_XML);
        this.deleteFile("build");
        this.deleteFile("dist");
    }

    @Override
    protected void doInit() throws Exception {
        this.addFile(BUILD_XML, RES_BUILD_XML, new TextFileFilter() {

            @Override
            protected String filterLine(String line) {
                return line.replace("${ProjectName}", this.getProjectWrapper().getName());
            }
        });

        this.addFile(PROJECT_XML, RES_PROJECT_XML, new ProjectXmlFilter());

        this.addFile(PROJECT_PROPERTIES, RES_PROJECT_PROPERTIES, new TextFileFilter() {

            @Override
            protected String filterLine(String line) {
                if (line.startsWith("application.title=")) {
                    return "application.title=" + this.getProjectWrapper().getName();
                }
                else if (line.startsWith("dist.jar=")) {
                    return "dist.jar=${dist.dir}/" + this.getProjectWrapper().getName() + ".jar";
                }
                else {
                    return line;
                }
            }
        });
    }

    protected static class ProjectXmlFilter extends FileFilter {

        @Override
        protected void execute(InputStream is, OutputStream os) throws Exception {
            Document doc = XMLUtil.parse(new InputSource(is), false, false, null, null);
            NodeList nl = doc.getDocumentElement().getElementsByTagName("name");
            if (nl != null) {
                for (int i = 0; i < nl.getLength(); i++) {
                    Element el = (Element) nl.item(i);
                    if (el.getParentNode() != null && "data".equals(el.getParentNode().getNodeName())) {
                        NodeList nl2 = el.getChildNodes();
                        if (nl2.getLength() > 0) {
                            nl2.item(0).setNodeValue(this.getProjectWrapper().getName());
                        }

                        break;
                    }
                }
            }

            XMLUtil.write(doc, os, "UTF-8");
        }
    }
}
