/*
 * Copyright (C) 2012 - 2015 by Stefan Rothe
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
package ch.jeda.platform.android;

import android.graphics.Typeface;
import android.view.WindowManager;
import ch.jeda.JedaError;
import ch.jeda.LogLevel;
import ch.jeda.event.Event;
import ch.jeda.event.SensorType;
import ch.jeda.platform.AudioManagerImp;
import ch.jeda.platform.CanvasImp;
import ch.jeda.platform.ImageImp;
import ch.jeda.platform.InputRequest;
import ch.jeda.platform.Platform;
import ch.jeda.platform.SelectionRequest;
import ch.jeda.platform.TypefaceImp;
import ch.jeda.platform.ViewRequest;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

class AndroidPlatform implements Platform {

    private static AndroidPlatform INSTANCE;
    private final Platform.Callback callback;

    static AndroidPlatform getInstance() {
        return INSTANCE;
    }

    public AndroidPlatform(final Platform.Callback callback) {
        INSTANCE = this;
        this.callback = callback;
        // Adjust window when soft keyboard is shown.
        Main.getInstance().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public CanvasImp createCanvasImp(final int width, final int height) {
        final AndroidCanvasImp result = new AndroidCanvasImp();
        result.init(width, height);
        return result;
    }

    @Override
    public ImageImp createImageImp(final String path) {
        return Main.getInstance().createImageImp(path);
    }

    @Override
    public TypefaceImp createTypefaceImp(final String path) {
        return Main.getInstance().createTypefaceImp(path);
    }

    @Override
    public XMLReader createXmlReader() {
        try {
            final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            final SAXParser parser = parserFactory.newSAXParser();
            return parser.getXMLReader();
        }
        catch (final ParserConfigurationException ex) {
            throw new JedaError(JedaError.XML_READER_CREATION_FAILED, ex);
        }
        catch (final SAXException ex) {
            throw new JedaError(JedaError.XML_READER_CREATION_FAILED, ex);
        }
    }

    @Override
    public AudioManagerImp getAudioManagerImp() {
        return Main.getInstance().getAudioManagerImp();
    }

    @Override
    public TypefaceImp getStandardTypefaceImp(StandardTypeface standardTypeface) {
        return new AndroidTypefaceImp(lookupStandardTypeface(standardTypeface));
    }

    @Override
    public boolean isSensorAvailable(final SensorType sensorType) {
        return Main.getInstance().isSensorAvailable(sensorType);
    }

    @Override
    public boolean isSensorEnabled(final SensorType sensorType) {
        return Main.getInstance().isSensorEnabled(sensorType);
    }

    @Override
    public boolean isVirtualKeyboardVisible() {
        return Main.getInstance().isVirtualKeyboardVisible();
    }

    @Override
    public Class<?>[] loadClasses() throws Exception {
        return Main.getInstance().loadClasses();
    }

    @Override
    public void log(final LogLevel logLevel, String message) {
        Main.getInstance().log(logLevel, message);
    }

    @Override
    public InputStream openResource(final String path) {
        return Main.getInstance().openResource(path);
    }

    @Override
    public void setSensorEnabled(final SensorType sensorType, boolean enabled) {
        Main.getInstance().setSensorEnabled(sensorType, enabled);
    }

    @Override
    public void setVirtualKeyboardVisible(final boolean visible) {
        Main.getInstance().setVirtualKeyboardVisible(visible);
    }

    @Override
    public void showInputRequest(final InputRequest inputRequest) {
        Main.getInstance().showInputRequest(inputRequest);
    }

    @Override
    public void showSelectionRequest(final SelectionRequest selectionRequest) {
        Main.getInstance().showSelectionRequest(selectionRequest);
    }

    @Override
    public void showViewRequest(final ViewRequest viewRequest) {
        Main.getInstance().showViewRequest(viewRequest);
    }

    @Override
    public void shutdown() {
        Main.getInstance().shutdown();
    }

    void onPause() {
        callback.pause();
    }

    void postEvent(final Event event) {
        callback.postEvent(event);
    }

    void onResume() {
        callback.resume();
    }

    private static Typeface lookupStandardTypeface(final Platform.StandardTypeface standardTypeface) {
        switch (standardTypeface) {
            case MONOSPACED:
                return Typeface.MONOSPACE;
            case SANS_SERIF:
                return Typeface.SANS_SERIF;
            case SERIF:
                return Typeface.SERIF;
            default:
                return null;
        }
    }
}
