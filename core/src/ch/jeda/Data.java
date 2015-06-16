/*
 * Copyright (C) 2014- 2015 by Stefan Rothe
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

import ch.jeda.event.Key;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Stores named values of different types. Each value is identified by a unique {@link java.lang.String} called
 * <i>name</i>. Data objects can be used to convert data of different types to a String to be stored or transmitted over
 * a network.
 *
 * @since 1.2
 * @version 3
 */
public class Data {

    private static final boolean DEFAULT_BOOLEAN = false;
    private static final double DEFAULT_DOUBLE = 0.0;
    private static final float DEFAULT_FLOAT = 0f;
    private static final int DEFAULT_INT = 0;
    private static final Key DEFAULT_KEY = Key.UNDEFINED;
    private static final String DEFAULT_STRING = null;
    private static final char NEXT_LINE = 0x0085;
    private static final char LINE_SEPARATOR = 0x2028;
    private static final char PARAGRAPH_SEPARATOR = 0x2029;

    private Document document;
    private Element element;

    /**
     * Constructs an empty data object.
     *
     * @since 1.2
     */
    public Data() {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = dbf.newDocumentBuilder();
            document = builder.newDocument();
            element = document.createElement("data");
            document.appendChild(element);
        }
        catch (final ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Construct a data object from a string or from a XML file. If the parameter starts with <code><data></code>, it is
     * assumed to be an XML string and is read directly. Otherwise, the parameter is assumed to be a resource path. The
     * corresponding file is opened and read.
     *
     * @param string the XML string or file path
     * @throws NullPointerException if <code>string</code> is <code>null</code>
     *
     * @since 1.3
     */
    public Data(final String string) {
        if (string == null) {
            throw new NullPointerException("string");
        }

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = dbf.newDocumentBuilder();
            if (string.startsWith("<data>")) {
                document = builder.parse(new InputSource(new StringReader(string)));
            }
            else {
                document = builder.parse(JedaInternal.openResource(string));
            }

            element = document.getDocumentElement();
        }

        catch (final SAXException ex) {
            Log.e(ex, "Error while reading XML data in '", string, "'.");
        }
        catch (final IOException ex) {
            Log.e(ex, "Error while reading from file '", string, "'.");
        }
        catch (final ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Data(final Document document, final Element element) {
        this.document = document;
        this.element = element;
    }

    /**
     * Clears all values in the data object. After a call of this method, the data object is empty.
     *
     * @since 1.2
     */
    public void clear() {
        remove("*");
    }

    /**
     * Returns all names of values stores in this data object.
     *
     * @return all names of values stores in this data object
     *
     * @since 1.2
     */
    public String[] getNames() {
        final SortedSet<String> names = new TreeSet<String>();
        final Node child = this.element.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                names.add(child.getLocalName());
            }
        }

        return names.toArray(new String[names.size()]);
    }

    /**
     * Checks if there is a value stored for the specified name.
     *
     * @param name the name to check
     * @return <code>true</code> if there is a value stored for the specified name, otherwise <code>false</code>
     *
     * @since 1.2
     */
    public boolean hasValue(final String name) {
        return element.getElementsByTagName(name).getLength() > 0;
    }

    /**
     * Checks if the data object is empty.
     *
     * @return <code>true</code> if the data object is empty, otherwise <code>false</code>
     *
     * @since 1.2
     */
    public boolean isEmpty() {
        final Node child = this.element.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the value associated with the specified name as a <code>boolean</code>. Returns the <code>false</code>,
     * if there is no valid <code>boolean</code> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the <code>boolean</code> value associated with the name or <code>false</code>
     *
     * @since 1.2
     */
    public final boolean readBoolean(final String name) {
        return readBoolean(name, DEFAULT_BOOLEAN);
    }

    /**
     * Returns the value associated with the specified name as a <code>boolean</code>. Returns
     * <code>defaultValue</code>, if there is no valid <code>boolean</code> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the <code>boolean</code> value associated with the name or <code>defaultValue</code>
     *
     * @since 1.2
     */
    public final boolean readBoolean(final String name, final boolean defaultValue) {
        final Node child = getFirstElementByTagName(name);
        if (child == null) {
            return defaultValue;
        }
        else {
            return Convert.toBoolean(child.getTextContent(), defaultValue);
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public boolean[] readBooleans(final String name) {
        return readBooleans(name, DEFAULT_BOOLEAN);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public boolean[] readBooleans(final String name, final boolean defaultValue) {
        final NodeList nodes = element.getElementsByTagName(name);
        final boolean[] result = new boolean[nodes.getLength()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = Convert.toBoolean(nodes.item(i).getTextContent(), defaultValue);
        }

        return result;
    }

    /**
     * Returns the value associated with the specified name as a <code>double</code>. Returns <code>0.0</code>, if there
     * is no valid <code>double</code> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the <code>double</code> value associated with the name or <code>defaultValue</code>
     *
     * @since 1.2
     */
    public final double readDouble(final String name) {
        return readDouble(name, DEFAULT_DOUBLE);
    }

    /**
     * Returns the value associated with the specified name as a <code>double</code>. Returns <code>defaultValue</code>,
     * if there is no valid <code>double</code> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the <code>double</code> value associated with the name or <code>defaultValue</code>
     *
     * @since 1.2
     */
    public final double readDouble(final String name, final double defaultValue) {
        final Node child = getFirstElementByTagName(name);
        if (child == null) {
            return defaultValue;
        }
        else {
            return Convert.toDouble(child.getTextContent(), defaultValue);
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public double[] readDoubles(final String name) {
        return readDoubles(name, DEFAULT_DOUBLE);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public double[] readDoubles(final String name, final double defaultValue) {
        final NodeList nodes = element.getElementsByTagName(name);
        final double[] result = new double[nodes.getLength()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = Convert.toDouble(nodes.item(i).getTextContent(), defaultValue);
        }

        return result;
    }

    /**
     * Returns the value associated with the specified name as a <code>float</code>. Returns <code>0.0</code>, if there
     * is no valid <code>float</code> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the <code>float</code> value associated with the name or <code>defaultValue</code>
     *
     * @since 2.0
     */
    public final float readFloat(final String name) {
        return readFloat(name, DEFAULT_FLOAT);
    }

    /**
     * Returns the value associated with the specified name as a <code>float</code>. Returns <code>defaultValue</code>,
     * if there is no valid <code>float</code> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the <code>float</code> value associated with the name or <code>defaultValue</code>
     *
     * @since 2.0
     */
    public final float readFloat(final String name, final float defaultValue) {
        final Node child = getFirstElementByTagName(name);
        if (child == null) {
            return defaultValue;
        }
        else {
            return Convert.toFloat(child.getTextContent(), defaultValue);
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 2.0
     */
    public float[] readFloats(final String name) {
        return readFloats(name, DEFAULT_FLOAT);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 2.0
     */
    public float[] readFloats(final String name, final float defaultValue) {
        final NodeList nodes = element.getElementsByTagName(name);
        final float[] result = new float[nodes.getLength()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = Convert.toFloat(nodes.item(i).getTextContent(), defaultValue);
        }

        return result;
    }

    /**
     * Returns the value associated with the specified name as an <code>int</code>. Returns <code>0</code>, if there is
     * no valid <code>int</code> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the <code>int</code> value associated with the name or <code>0</code>
     *
     * @since 1.2
     */
    public final int readInt(final String name) {
        return readInt(name, DEFAULT_INT);
    }

    /**
     * Returns the value associated with the specified name as an <code>int</code>. Returns <code>defaultValue</code>,
     * if there is no valid <code>int</code> value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the <code>int</code> value associated with the name or <code>defaultValue</code>
     *
     * @since 1.2
     */
    public final int readInt(final String name, final int defaultValue) {
        final Node child = getFirstElementByTagName(name);
        if (child == null) {
            return defaultValue;
        }
        else {
            return Convert.toInt(child.getTextContent(), defaultValue);
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public int[] readInts(final String name) {
        return readInts(name, DEFAULT_INT);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public int[] readInts(final String name, final int defaultValue) {
        final NodeList nodes = element.getElementsByTagName(name);
        final int[] result = new int[nodes.getLength()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = Convert.toInt(nodes.item(i).getTextContent(), defaultValue);
        }

        return result;
    }

    /**
     * Returns the value associated with the specified name as a {@link ch.jeda.event.Key}. Returns
     * {@link ch.jeda.event.Key#UNDEFINED}, if there is no valid {@link ch.jeda.event.Key} value associated with the
     * name.
     *
     * @param name the name of the value to retrieve
     * @return the {@link ch.jeda.event.Key} value associated with the name or {@link ch.jeda.event.Key#UNDEFINED}
     *
     * @since 1.2
     */
    public Key readKey(final String name) {
        return readKey(name, DEFAULT_KEY);
    }

    /**
     * Returns the value associated with the specified name as a {@link ch.jeda.event.Key}. Returns
     * <code>defaultValue</code>, if there is no valid {@link ch.jeda.event.Key} value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the {@link ch.jeda.event.Key} value associated with the name or <code>defaultValue</code>
     *
     * @since 1.2
     */
    public Key readKey(final String name, final Key defaultValue) {
        final Node child = getFirstElementByTagName(name);
        if (child == null) {
            return defaultValue;
        }

        try {
            return Key.valueOf(child.getTextContent());
        }
        catch (final IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public Key[] readKeys(final String name) {
        return readKeys(name, DEFAULT_KEY);
    }

    /**
     * Returns the array associated with the specified name. Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public Key[] readKeys(final String name, final Key defaultValue) {
        final NodeList nodes = element.getElementsByTagName(name);
        final Key[] result = new Key[nodes.getLength()];
        for (int i = 0; i < result.length; ++i) {
            try {
                result[i] = Key.valueOf(nodes.item(i).getTextContent());
            }
            catch (final IllegalArgumentException ex) {
                result[i] = defaultValue;
            }
        }

        return result;
    }

    /**
     * Reads an {@link ch.jeda.Storable} object with the specified name. Creates a new object from the information
     * stored in this data. Returns <code>null</code>, there is no valid object associated with the name.
     *
     * @param <T> the class of the object to return
     * @param name the name of the object to retrieve
     * @return the {@link ch.jeda.Storable} object associated with the name or <code>null</code>
     *
     * @since 1.2
     */
    public <T extends Storable> T readObject(final String name) {
        return readObject(name, null);
    }

    /**
     * Reads an {@link ch.jeda.Storable} object with the specified name. Creates a new object from the information
     * stored in this data. Returns <code>defaultValue</code>, there is no valid object associated with the name.
     *
     * @param <T> the class of the object to return
     * @param name the name of the object to retrieve
     * @param defaultValue the default value
     * @return the {@link ch.jeda.Storable} object associated with the name or <code>defaultValue</code>
     *
     * @since 1.2
     */
    @SuppressWarnings("unchecked")
    public <T extends Storable> T readObject(final String name, final T defaultValue) {
        final Element child = (Element) getFirstElementByTagName(name);
        final String className = child.getAttribute("class");
        try {
            final Class clazz = Class.forName(className);
            final Constructor ctor = clazz.getConstructor(Data.class);
            ctor.setAccessible(true);
            return (T) ctor.newInstance(new Data(document, child));
        }
        catch (final NoSuchMethodException ex) {
            Log.e(ex, "Error while reading serialized data: Class '", className, "' does not have a matching constructor.");
        }
        catch (final ClassNotFoundException ex) {
            Log.e(ex, "Error while reading serialized data: Class '", className, "' not found.");
        }
        catch (final InstantiationException ex) {
            Log.e(ex, "Error while reading serialized data: Cannot create object of class '", className, "'.");
        }
        catch (final IllegalAccessException ex) {
            Log.e(ex, "Error while reading serialized data: Constructor of class '", className, "' is not public.");
        }
        catch (final ExceptionInInitializerError ex) {
            Log.e(ex.getCause(), "Error while reading serialized data: Error while initializing class '", className, "'.");
        }
        catch (final InvocationTargetException ex) {
            Log.w(ex.getCause(), "Error while reading serialized data: Error while creating object of class '", className, "'.");
        }

        return defaultValue;
    }

    /**
     * Returns the value associated with the specified name as a {@link java.lang.String}. Returns <code>null</code>, if
     * there is no valid {@link java.lang.String} value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @return the {@link java.lang.String} value associated with the name or <code>null</code>
     *
     * @since 1.2
     */
    public String readString(final String name) {
        return readString(name, DEFAULT_STRING);
    }

    /**
     * Returns the value associated with the specified name as a {@link java.lang.String}. Returns
     * <code>defaultValue</code>, if there is no valid {@link java.lang.String} value associated with the name.
     *
     * @param name the name of the value to retrieve
     * @param defaultValue the default value
     * @return the {@link java.lang.String} value associated with the name or <code>defaultValue</code>
     *
     * @since 1.2
     */
    public String readString(final String name, final String defaultValue) {
        final Element child = (Element) getFirstElementByTagName(name);
        if (child == null) {
            return defaultValue;
        }

        if ("true".equals(child.getAttribute("null"))) {
            return null;
        }

        return unescape(child.getTextContent());
    }

    /**
     * Returns the array associated with the specified name . Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public String[] readStrings(final String name) {
        return readStrings(name, DEFAULT_STRING);
    }

    /**
     * Returns the array associated with the specified name . Returns an empty array, if there is no valid array
     * associated with the name.
     *
     * @param name the name of the array to retrieve
     * @param defaultValue the default value
     * @return the array associated with the name.
     *
     * @since 1.2
     */
    public String[] readStrings(final String name, final String defaultValue) {
        final NodeList nodes = element.getElementsByTagName(name);
        final String[] result = new String[nodes.getLength()];
        for (int i = 0; i < result.length; ++i) {
            Element node = (Element) nodes.item(i);
            if ("true".equals(node.getAttribute("null"))) {
                result[i] = null;
            }
            else {
                result[i] = unescape(node.getTextContent());
            }
        }

        return result;
    }

    /**
     * Removes a value associated with the specified name from the data object.
     *
     * @param name the name of the value to remove
     *
     * @since 1.2
     */
    public void remove(final String name) {
        final NodeList nodeList = element.getElementsByTagName(name);
        final Node[] nodes = new Node[nodeList.getLength()];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = nodeList.item(i);
        }

        for (int i = 0; i < nodes.length; ++i) {
            element.removeChild(nodes[i]);
        }
    }

    /**
     * Saves this data as an XML file.
     *
     * @param path the file path
     *
     * @since 2.0
     */
    public void save(final String path) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(path);
            serialize(writer, true);
        }
        catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (final IOException ex) {
                    // ignore
                }
            }
        }
    }

    /**
     * Serializes the data to a line of text.
     *
     * @return the serialized data as a line of text
     *
     * @since 1.3
     */
    public String toLine() {
        final StringWriter writer = new StringWriter();
        serialize(writer, false);
        return writer.toString();
    }

    @Override
    public String toString() {
        return toLine();
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeBoolean(final String name, final boolean value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        remove(name);
        addElement(name, Convert.toString(value));
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <code>name</code> or <code>values</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeBooleans(final String name, final boolean[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        remove(name);
        for (int i = 0; i < values.length; ++i) {
            addElement(name, Convert.toString(values[i]));
        }
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeDouble(final String name, final double value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        remove(name);
        addElement(name, Convert.toString(value));
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <code>name</code> or <code>values</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeDoubles(final String name, final double[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        remove(name);
        for (int i = 0; i < values.length; ++i) {
            addElement(name, Convert.toString(values[i]));
        }
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeFloat(final String name, final float value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        remove(name);
        addElement(name, String.valueOf(value));
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <code>name</code> or <code>values</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeFloats(final String name, final float[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        remove(name);
        for (int i = 0; i < values.length; ++i) {
            addElement(name, Convert.toString(values[i]));
        }
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeInt(final String name, final int value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        remove(name);
        addElement(name, Convert.toString(value));
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <code>name</code> or <code>values</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeInts(final String name, final int[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        remove(name);
        for (int i = 0; i < values.length; ++i) {
            addElement(name, Convert.toString(values[i]));
        }
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name. A <code>null</code> value is not added to the data. Instead, a previously stored value with
     * the same name is removed.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeKey(final String name, final Key value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        remove(name);
        addElement(name, value.toString());
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <code>name</code> or <code>values</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeKeys(final String name, final Key[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        remove(name);
        for (int i = 0; i < values.length; ++i) {
            addElement(name, values[i].toString());
        }
    }

    /**
     * Stores a {@link ch.jeda.Storable} object in the data object.
     *
     * @param name the name of the object
     * @param value the {@link ch.jeda.Storable} object to store
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeObject(final String name, final Storable value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        remove(name);
        doWriteObject(name, value);
    }

    /**
     * Stores an array of {@link ch.jeda.Storable} objects in the data object.
     *
     * @param name the name of the array
     * @param values the array of {@link ch.jeda.Storable} objects to store
     * @throws NullPointerException if <code>name</code> or <code>values</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeObjects(final String name, final Storable[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        remove(name);
        for (int i = 0; i < values.length; ++i) {
            doWriteObject(name, values[i]);
        }
    }

    /**
     * Stores the specified value with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param value the value to store
     * @throws NullPointerException if <code>name</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeString(final String name, final String value) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        remove(name);
        addElement(name, escape(value));
    }

    /**
     * Stores the specified array with the specified name in the data object. Overwrites any previously stored value
     * with the same name.
     *
     * @param name the name of the value
     * @param values the array of values to store
     * @throws NullPointerException if <code>name</code> or <code>values</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>name</code> is not a valid XML name
     *
     * @since 1.2
     */
    public void writeStrings(final String name, final String[] values) {
        if (name == null) {
            throw new NullPointerException("name");
        }

        if (values == null) {
            throw new NullPointerException("values");
        }

        remove(name);
        for (int i = 0; i < values.length; ++i) {
            addElement(name, escape(values[i]));
        }
    }

    private Node getFirstElementByTagName(final String name) {
        NodeList nodes = element.getElementsByTagName(name);
        if (nodes.getLength() > 0) {
            return nodes.item(0);
        }
        else {
            return null;
        }
    }

    private void addElement(final String name, final String content) {
        try {
            final Element newElement = document.createElement(name);
            if (content == null) {
                newElement.setAttribute("null", "true");
            }
            else {
                newElement.setTextContent(content);
            }

            element.appendChild(newElement);
        }
        catch (final DOMException ex) {
            throw new IllegalArgumentException("name", ex);
        }
    }

    private void doWriteObject(final String name, final Storable value) {
        if (value != null) {
            try {
                final Element newElement = document.createElement(name);
                newElement.setAttribute("class", value.getClass().getName());
                element.appendChild(newElement);
                value.writeTo(new Data(document, newElement));
            }
            catch (final DOMException ex) {
                throw new IllegalArgumentException("name", ex);
            }
        }
    }

    private void serialize(final Writer writer, final boolean multiline) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            if (multiline) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            }
            else {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "no");
            }

            Result output = new StreamResult(writer);
            Source input = new DOMSource(document);
            transformer.transform(input, output);
        }
        catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String escape(final String text) {
        if (text == null) {
            return null;
        }

        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            final char ch = text.charAt(i);
            switch (ch) {
                case '\'':
                    result.append("\\a");
                    break;
                case '\\':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '>':
                    result.append("\\g");
                    break;
                case '<':
                    result.append("\\l");
                    break;
                case '&':
                    result.append("\\m");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case PARAGRAPH_SEPARATOR:
                    result.append("\\p");
                    break;
                case '"':
                    result.append("\\q");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case LINE_SEPARATOR:
                    result.append("\\s");
                    break;
                case NEXT_LINE:
                    result.append("\\x");
                    break;
                default:
                    result.append(ch);
                    break;
            }
        }

        return result.toString();
    }

    private static String unescape(final String text) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            final char ch = text.charAt(i);
            if (ch == '\\' && i < text.length() - 1) {
                ++i;
                final char escapeChar = text.charAt(i);
                switch (escapeChar) {
                    case 'a':
                        result.append('\'');
                        break;
                    case 'b':
                        result.append('\\');
                        break;
                    case 'f':
                        result.append('\f');
                        break;
                    case 'g':
                        result.append('>');
                        break;
                    case 'l':
                        result.append('<');
                        break;
                    case 'm':
                        result.append('&');
                        break;
                    case 'n':
                        result.append('\n');
                        break;
                    case 'p':
                        result.append(PARAGRAPH_SEPARATOR);
                        break;
                    case 'q':
                        result.append('"');
                        break;
                    case 'r':
                        result.append('\r');
                        break;
                    case 's':
                        result.append(LINE_SEPARATOR);
                        break;
                    case 'x':
                        result.append(NEXT_LINE);
                        break;
                    default:
                        // ignore invalid escape
                        result.append(ch);
                        result.append(escapeChar);
                        break;
                }
            }
            else {
                result.append(ch);
            }
        }

        return result.toString();
    }
}
