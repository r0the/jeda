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

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioStreamAdapter implements AudioDataSource {

    private AudioInputStream audioInputStream;

    public AudioStreamAdapter(final InputStream inputStream) throws IOException, UnsupportedAudioFileException {
        this.audioInputStream = AudioSystem.getAudioInputStream(inputStream);
    }

    @Override
    public int available() throws IOException {
        return this.audioInputStream.available();
    }

    @Override
    public void close() throws IOException {
        this.audioInputStream.close();
    }

    @Override
    public AudioFormat getFormat() {
        return this.audioInputStream.getFormat();
    }

    @Override
    public int read(final byte[] buffer, final int length) throws IOException {
        return this.audioInputStream.read(buffer, 0, length);
    }

    @Override
    public void reset() throws IOException {
        this.audioInputStream.reset();
    }
}
