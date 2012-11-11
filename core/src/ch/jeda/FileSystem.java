/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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

import ch.jeda.platform.ImageImp;
import ch.jeda.platform.Platform;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class FileSystem {

    private static final String DEFAULT_IMAGE_PATH = ":ch/jeda/resources/logo-64x64.png";
    private static final String RESOURCE_PREFIX = ":";
    private final Platform platform;
    private final Map<String, ImageImp> imageMap;
    private ImageImp defaultImage;

    FileSystem(Platform platform) {
        this.platform = platform;
        this.imageMap = new HashMap();
    }

    void init() {
        this.defaultImage = this.loadImageImp(DEFAULT_IMAGE_PATH);
    }

    ImageImp loadImageImp(String filePath) {
        if (this.imageMap.containsKey(filePath)) {
            return this.imageMap.get(filePath);
        }
        else {
            ImageImp result = null;
            URL url = this.urlForFile(filePath);
            if (url == null) {
                Log.warning("Datei {0} nicht gefunden.", filePath);
            }

            try {
                result = this.platform.loadImageImp(url);
            }
            catch (Exception ex) {
                Log.warning(Message.IMAGE_READ_ERROR, filePath);
            }

            if (result == null) {
                return this.defaultImage;
            }
            else {
                this.imageMap.put(filePath, result);
                return result;
            }
        }
    }

    private URL pathToUrl(String filePath) {
        try {
            return new File(filePath).toURI().toURL();
        }
        catch (MalformedURLException ex) {
            Log.warning("jeda.file.file.error", filePath);
            return null;
        }
    }

    private URL resourcePathToUrl(String filePath) {
        String path = filePath.substring(RESOURCE_PREFIX.length());
        URL result = Thread.currentThread().getContextClassLoader().getResource(path);
        if (result == null) {
            Log.warning("jeda.file.resource.error", filePath);
        }

        return result;
    }

    public URL urlForFile(String filePath) {
        if (filePath == null) {
            Log.warning("null ist kein gültiger Dateipfad.");
            return null;
        }

        if (filePath.startsWith(RESOURCE_PREFIX)) {
            return this.resourcePathToUrl(filePath);
        }
        else {
            return this.pathToUrl(filePath);
        }
    }
}
