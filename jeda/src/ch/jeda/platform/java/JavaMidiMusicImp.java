/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.platform.java;

import ch.jeda.Log;
import ch.jeda.platform.MusicImp;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class JavaMidiMusicImp implements MusicImp {

    private final String path;
    private Sequencer sequencer;

    public JavaMidiMusicImp(final String path, final Sequencer sequencer) {
        this.path = path;
        this.sequencer = sequencer;
    }

    @Override
    public void pause() {
        this.sequencer.stop();
    }

    @Override
    public void play() {
        if (this.sequencer.getSequence() == null) {
            this.init();
        }

        if (this.sequencer.isOpen()) {
            this.sequencer.start();
        }
    }

    @Override
    public void stop() {
        this.sequencer.setMicrosecondPosition(0);
        this.sequencer.close();
    }

    private void init() {
        try {
            this.sequencer.open();
            this.sequencer.setSequence(ResourceManager.openInputStream(path));
        }
        catch (final MidiUnavailableException ex) {
            Log.err("jeda.audio.error.midi-unavailable", this.path);
        }
        catch (final InvalidMidiDataException ex) {
            Log.err("jeda.audio.error.invalid-midi-data", path);
        }
        catch (final IOException ex) {
            Log.err(ex, "jeda.audio.error.read", path);
        }
    }
}
