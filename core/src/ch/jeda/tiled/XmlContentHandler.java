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

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

final class XmlContentHandler implements ContentHandler {

    private final Stack<ElementWrapper> elementStack;
    private ElementWrapper rootElement;

    XmlContentHandler() {
        elementStack = new Stack<ElementWrapper>();
    }

    @Override
    public void setDocumentLocator(final Locator locator) {
        // ignore
    }

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
    }

    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName,
                             final Attributes attributes) throws SAXException {
        elementStack.push(new ElementWrapper(localName, attributes));
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        final ElementWrapper element = elementStack.pop();
        if (elementStack.empty()) {
            rootElement = element;
        }
        else {
            elementStack.peek().addChild(element);
        }
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        elementStack.peek().setContent(new String(ch, start, length));
    }

    @Override
    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
    }

    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
    }

    @Override
    public void skippedEntity(final String name) throws SAXException {
    }

    public ElementWrapper getRootElement() {
        return rootElement;
    }
}
