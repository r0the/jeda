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
package ch.jeda.netbeans.java;

import ch.jeda.netbeans.support.FileFilter;
import java.io.InputStream;
import java.io.OutputStream;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

final class ProjectXmlFilter implements FileFilter {

    private final String projectName;

    public ProjectXmlFilter(final String projectName) {
        this.projectName = projectName;
    }

    @Override
    public void apply(final InputStream in, final OutputStream out) throws Exception {
        final Document doc = XMLUtil.parse(new InputSource(in), false, false, null, null);
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

        XMLUtil.write(doc, out, "UTF-8");
    }
}
