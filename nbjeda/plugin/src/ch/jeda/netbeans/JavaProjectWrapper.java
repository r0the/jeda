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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.project.classpath.ProjectClassPathModifier;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
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

    public JavaProjectWrapper(final FileObject projectRoot) {
        super(projectRoot, null);
    }

    public JavaProjectWrapper(final FileObject projectRoot, final Project project) {
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
        this.renameFile(NB_PROJECT, NB_PROJECT_ANDROID);
        this.deleteFile(BUILD_XML);
        this.deleteFile("build");
        this.deleteFile("dist");
    }

    @Override
    protected void doInit() throws Exception {
        this.addFile(BUILD_XML, RES_BUILD_XML, new TextFileFilter() {
            @Override
            protected String filterLine(final String line) {
                return line.replace("${ProjectName}", this.getProjectWrapper().getName());
            }
        });

        this.addFile(PROJECT_XML, RES_PROJECT_XML, new ProjectXmlFilter());

        this.addFile(PROJECT_PROPERTIES, RES_PROJECT_PROPERTIES, new TextFileFilter() {
            @Override
            protected String filterLine(final String line) {
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

    @Override
    protected void librariesChanged() {
        try {
            final FileObject[] libs = this.getLibs().getChildren();
            final List<URI> libUris = new ArrayList<URI>();
            for (int i = 0; i < libs.length; ++i) {
                libUris.add(libs[i].toURI());
            }

            ProjectClassPathModifier.addRoots(libUris.toArray(new URI[libUris.size()]),
                this.getSrc(), ClassPath.COMPILE);
        }
        catch (final IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        catch (final UnsupportedOperationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    protected static class ProjectXmlFilter extends FileFilter {

        @Override
        protected void execute(final InputStream is, final OutputStream os) throws Exception {
            final Document doc = XMLUtil.parse(new InputSource(is), false, false, null, null);
            final NodeList nl = doc.getDocumentElement().getElementsByTagName("name");
            if (nl != null) {
                for (int i = 0; i < nl.getLength(); i++) {
                    final Element el = (Element) nl.item(i);
                    if (el.getParentNode() != null && "data".equals(el.getParentNode().getNodeName())) {
                        final NodeList nl2 = el.getChildNodes();
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
