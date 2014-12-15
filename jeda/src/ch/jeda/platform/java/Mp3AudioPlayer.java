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
package ch.jeda.platform.java;

import ch.jeda.Log;
import ch.jeda.Message;
import java.io.BufferedInputStream;
import java.io.IOException;
import org.mp3transform.Decoder;

class Mp3AudioPlayer extends AudioPlayer {

    private static final int STREAM_BUFFER_SIZE = 128 * 1024;
    private final Decoder decoder;
    private final String path;

    Mp3AudioPlayer(final JavaAudioManagerImp audioManager, final String path) {
        super(audioManager);
        this.decoder = new Decoder();
        this.path = path;
    }

    @Override
    public void pause() {
        super.pause();
        this.decoder.setPaused(true);
    }

    @Override
    public void resume() {
        super.resume();
        this.decoder.setPaused(false);
    }

    @Override
    public void stop() {
        super.stop();
        this.decoder.requestStop();
    }

    @Override
    protected void playLoop() {
        try {
            final BufferedInputStream bin = new BufferedInputStream(ResourceManager.openInputStream(this.path), STREAM_BUFFER_SIZE);
            this.decoder.play(this.path, bin);
        }
        catch (final IOException ex) {
            Log.err(ex, Message.AUDIO_ERROR_READ, this.path);
        }
    }
}
