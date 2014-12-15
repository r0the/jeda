/*
 * Copyright (C) 2014 by Stefan Rothe
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

import java.util.Locale;
import java.util.ResourceBundle;

public class Message {

    public static final String AUDIO_ERROR_INVALID_RESOURCE_NAME = "jeda.audio.error.invalid-resource-name";
    public static final String AUDIO_ERROR_READ = "jeda.audio.error.read";
    public static final String AUDIO_ERROR_UNSUPPORTED_FILE_FORMAT = "jeda.audio.error.unsupported-file-format";
    public static final String DATA_ERROR_ACCESS = "jeda.data.error.access";
    public static final String DATA_ERROR_CLASS_INITIALIZER = "jeda.data.error.class-initializer";
    public static final String DATA_ERROR_CLASS_NOT_FOUND = "jeda.data.error.class-not-found";
    public static final String DATA_ERROR_CONSTRUCTOR = "jeda.data.error.constructor";
    public static final String DATA_ERROR_CONSTRUCTOR_NOT_FOUND = "jeda.data.error.constructor-not-found";
    public static final String DATA_ERROR_INSTANTIATION = "jeda.data.error.instantiation";
    public static final String EVENT_ERROR = "jeda.event.error";
    public static final String ENGINE_ERROR_INIT_CLASSES = "jeda.engine.error.init-classes";
    public static final String ENGINE_ERROR_PLATFORM_CLASS_NOT_FOUND = "jeda.engine.error.platform-class-not-found";
    public static final String ENGINE_ERROR_PLATFORM_ACCESS = "jeda.engine.error.platform-access";
    public static final String ENGINE_ERROR_PLATFORM_CONSTRUCTOR = "jeda.engine.error.platform-constructor";
    public static final String ENGINE_ERROR_PLATFORM_CONSTRUCTOR_NOT_FOUND = "jeda.engine.error.platform-constructor-not-found";
    public static final String ENGINE_ERROR_PLATFORM_INSTANTIATION = "jeda.engine.error.platform-instantiation";
    public static final String ENGINE_ERROR_PLATFORM_MISSING_CLASS_NAME = "jeda.engine.error.platform-missing-class-name";
    public static final String ENGINE_ERROR_PLATFORM_MISSING_INTERFACE = "jeda.engine.error.platform-missing-interface";
    public static final String ENGINE_ERROR_PROPERTIES_NOT_FOUND = "jeda.engine.error.properties-not-found";
    public static final String ENGINE_ERROR_PROPERTIES_READ = "jeda.engine.error.properties-read";
    public static final String ENGINE_PROGRAM_THREAD_NAME = "jeda.engine.program-thread-name";
    public static final String ENGINE_EVENT_THREAD_NAME = "jeda.engine.event-thread-name";
    public static final String FILE_ERROR_NOT_FOUND = "jeda.file.error.not-found";
    public static final String FILE_ERROR_OPEN = "jeda.file.error.open";
    public static final String FILE_ERROR_READ = "jeda.file.error.read";
    public static final String GUI_SELECT_PROGRAM_TITLE = "jeda.gui.select-program-title";
    public static final String IMAGE_ERROR_READ = "jeda.image.error.read";
    public static final String PROGRAM_ERROR_ALREADY_RUNNING = "jeda.program.error.already-running";
    public static final String PROGRAM_ERROR_CLASS_NOT_FOUND = "jeda.program.error.class-not-found";
    public static final String PROGRAM_ERROR_CREATE = "jeda.program.error.create";
    public static final String PROGRAM_ERROR_SELECT = "jeda.program.error.select";
    public static final String PROGRAM_ERROR_NO_CLASS_FOUND = "jeda.program.error.no-class-found";
    public static final String PROGRAM_ERROR_RUN = "jeda.program.error.run";
    public static final String TYPEFACE_ERROR_READ = "jeda.typeface.error.read";
    public static final String TYPEFACE_ERROR_FORMAT = "jeda.typeface.error.format";

    public static String get(final String key) {
        try {
            final ResourceBundle rb = ResourceBundle.getBundle("res/jeda/translations", Locale.GERMAN);
            if (rb.containsKey(key)) {
                return rb.getString(key);
            }
        }
        catch (final Exception ex) {
            // ignore
        }

        return "<" + key + ">";
    }

}
