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
import ch.jeda.platform.SoundImp;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

class JavaSoundImp implements SoundImp {

    private final byte[] data;
    private final AudioManager audioManager;

    JavaSoundImp(final AudioManager audioManager, final byte[] data) {
        this.audioManager = audioManager;
        this.data = data;
    }

    @Override
    public void play() {
        try {
            this.audioManager.playSound(javax.sound.sampled.AudioSystem.getAudioInputStream(
                new ByteArrayInputStream(this.data)));
        }
        catch (UnsupportedAudioFileException ex) {
            Log.err("jeda.sound.open.format.error");
        }
        catch (IOException ex) {
            Log.err("jeda.sound.open.read.error");
        }
    }
}
