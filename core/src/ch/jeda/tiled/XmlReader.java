/*
 * Copyright (C) 2014 - 2015 by Stefan Rothe
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
package ch.jeda.tiled;

import ch.jeda.JedaInternal;
import ch.jeda.Log;
import ch.jeda.ui.Image;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

class XmlReader {

    private final String prefix;

    XmlReader(final String prefix) {
        this.prefix = prefix;
    }

    ElementWrapper read(final String path) {
        try {
            final XMLReader xmlReader = JedaInternal.createXmlReader();
            final XmlContentHandler contentHandler = new XmlContentHandler();
            xmlReader.setContentHandler(contentHandler);
            xmlReader.setEntityResolver(new TiledEntityResolver());
            InputSource inputSource = new InputSource(JedaInternal.openResource(prefix + path));
            xmlReader.parse(inputSource);
            return contentHandler.getRootElement();
        }
        catch (final SAXException ex) {
            Log.e(ex, "Error while reading TMX file.");
        }
        catch (final IOException ex) {
            Log.e(ex, "Error while reading TMX file.");
        }

        return null;
    }

    Image loadImageChild(final ElementWrapper element) {
        final ElementWrapper imageElement = element.getChild(Const.IMAGE);
        if (imageElement == null) {
            return null;
        }

        final String source = imageElement.getStringAttribute(Const.SOURCE);
        if (source != null) {
            return new Image(prefix + source);
        }
        else {
            return null;
        }
    }
}
