/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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

import java.util.HashMap;
import java.util.Map;

public class Message {

    public static final String CHOOSE_PROGRAM_ERROR = "jeda.error.init.programs";
    public static final String CHOOSE_PROGRAM_TITLE = "jeda.gui.programchooser.title";
    public static final String ENGINE_THREAD_NAME = "jeda.engine.thread.name";
    public static final String FILE_NOT_FOUND_ERROR = "jeda.file.not.found.error";
    public static final String FILE_READ_ERROR = "jeda.file.read.error";
    public static final String IMAGE_FORMAT_ERROR = "jeda.image.format.error";
    public static final String IMAGE_READ_ERROR = "jeda.image.read.error";
    public static final String IMAGE_WRITE_ERROR = "jeda.image.write.error";
    public static final String INPUT_REQUEST_TITLE = "jeda.gui.inputrequest.title";
    public static final String LOAD_PROPERTIES_ERROR = "jeda.error.init.properties";
    public static final String LOG_BUTTON = "jeda.gui.log.button";
    public static final String LOG_TITLE = "jeda.gui.log.title";
    public static final String NO_PROGRAM_ERROR = "jeda.error.init.noprogram";
    public static final String PROGRAM_CREATE_ERROR = "jeda.programrunner.create.error";
    public static final String PROGRAM_RUN_ERROR = "jeda.program.run.error";
    // -- TODO --
    public static final String CLASS_NOT_FOUND_ERROR = "jeda.classnotfound.error";
    public static final String GUI_CLOSE_BUTTON = "jeda.gui.close.button";
    public static final String LIST_RESOURCES_ERROR = "jeda.listclasses.error";
    public static final String SUBSYSTEM_CREATE_ERROR = "jeda.subsystem.create.error";
    public static final String SUBSYSTEM_INIT_ERROR = "jeda.subsystem.init.error";
    public static final String SUBSYSTEM_THREAD_NAME = "jeda.subsystem.shutdown.threadname";
    public static final String SUBSYSTEM_SHUTDOWN_ERROR = "jeda.subsystem.shutdown.error";
    private static final Map<String, String> messageMap = new HashMap<String, String>();
    private static final String NO_TRANSLATION_ERROR = "jeda.message.translation.error";

    static {
        put(CHOOSE_PROGRAM_ERROR, "Fehler beim Laden der Program-Klassen.");
        put(CHOOSE_PROGRAM_TITLE, "Jeda Programmauswahl");
        put(ENGINE_THREAD_NAME, "Jeda Engine");
        put(FILE_READ_ERROR, "Fehler beim Lesen der Datei '{0}': {1}");
        put(FILE_NOT_FOUND_ERROR, "Datei {0} nicht gefunden.");
        put(IMAGE_FORMAT_ERROR, "Bilddatei '{0}' kann nicht im unbekannten Bildformat '{1}' gespeichert werden.");
        put(IMAGE_READ_ERROR, "Fehler beim Lesen der Bilddatei '{0}': {1}");
        put(IMAGE_WRITE_ERROR, "Fehler beim Schreiben der Bilddatei '{0}': {1}");
        put(INPUT_REQUEST_TITLE, "Jeda Eingabe");
        put(LOAD_PROPERTIES_ERROR, "Fehler beim Laden der Property-Dateien.");
        put(LOG_BUTTON, "Schliessen");
        put(LOG_TITLE, "Jeda Log");
        put(NO_PROGRAM_ERROR, "Es ist keine Jeda-Programmklasse vorhanden.");
        put(PROGRAM_CREATE_ERROR, "Beim Initalisieren des Programms '{0}' ist ein Fehler aufgetreten.");
        put(PROGRAM_RUN_ERROR, "Fehler beim Ausführen von Programm '{0}'.");
        // -- TODO --
        put(NO_TRANSLATION_ERROR, "Es ist keine Übersetzung für '{0}' vorhanden.");
        put("jeda.file.resource.error", "'{0}' ist kein gültiger Resourcenpfad.");
        put("jeda.file.file.error", "'{0}' ist kein gültiger Dateipfad.");
        put(CLASS_NOT_FOUND_ERROR, "Kann Klasse '{0}' nicht finden.");
        put(GUI_CLOSE_BUTTON, "Schliessen");
        put("jeda.gui.programchooser.run", "Programm Starten");
        put("jeda.gui.programchooser.cancel", "Beenden");
        put("jeda.gui.programchooser.text", "In diesem Projekt sind mehrere Programme definiert. Wählen Sie das gewünschte aus:");
        put(LIST_RESOURCES_ERROR, "Fehler beim Suchen nach Ressourcen.");
        put("jeda.net.connection.init.error", "Fehler beim Erstellen einer Verbindung.");
        put("jeda.net.server.accept", "Verbindung von {0} akzeptiert.");
        put("jeda.net.server.accept.error", "Fehler beim Akzeptieren einer Verbindung.");
        put("jeda.net.server.close.error", "Fehler beim Beenden des Servers.");
        put("jeda.net.server.permission.error", "Der Server kann nicht gestartet werden, die Berechtigung fehlt. Wählen Sie einen Port grösser als 1023.");
        put("jeda.net.server.port.error", "Der Server kann nicht gestartet werden, der Port {0} ist schon in Gebrauch.");
        put("jeda.net.server.started", "Server an Port {0} gestartet.");
        put("jeda.net.server.stopped", "Server an Port {0} gestopps.");
        put("jeda.sound.open.format.error", "Das Format der Audiodatei '{0}' wird nicht unterstützt.");
        put("jeda.sound.open.read.error", "Fehler beim Lesen der Audiodatei '{0}'.");
        put("jeda.sound.playback.format.error", "Das Format '{0}' wird nicht unterstützt.");
        put("jeda.sound.playback.read.error", "Fehler beim Abspielen eines Playbacks.");
        put("jeda.sound.playback.start.error", "Playback kann nicht gestartet werden - keine Linie frei.");
        put("jeda.sound.playback.stop.error", "Fehler beim Stoppen des Playbacks.");
        put(SUBSYSTEM_CREATE_ERROR, "Fehler beim Erstellen des Subsystems {0}.");
        put(SUBSYSTEM_INIT_ERROR, "Fehler beim Initialisieren des Subsystems {0}.");
        put(SUBSYSTEM_THREAD_NAME, "Jeda {0} Shutdown Thread");
        put(SUBSYSTEM_SHUTDOWN_ERROR, "Fehler beim Beenden des Subsystems {0}.");
        put("jeda.ui.fullscreen.applet.error", "Der Vollbildmodus ist für Applets nicht verfügbar.");
        put("jeda.ui.fullscreen.other.error", "Ein anderes Fenster befindet sich schon im Vollbildmodus.");
    }

    private static void put(String key, String translation) {
        messageMap.put(key, translation);
    }

    public static String translate(String key, Object... args) {
        return doTranslate(key, args);
    }

    public static String translate(String key) {
        return doTranslate(key, null);
    }

    private static String doTranslate(String key, Object[] args) {
        if (key == null) {
            return "<null>";
        }
        String result = "";
        if (!messageMap.containsKey(key) && !NO_TRANSLATION_ERROR.equals(key)) {
            //Log.warning(NO_TRANSLATION_ERROR, key);
            result = key;
        }
        else {
            result = messageMap.get(key);
        }
        return Util.args(result, args);
    }
}
