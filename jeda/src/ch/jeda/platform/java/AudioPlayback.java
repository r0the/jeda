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
package ch.jeda.platform.java;

import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

class AudioPlayback {

    private final FloatControl balanceControl;
    private final AudioInputStream data;
    private final FloatControl gainControl;
    private final SourceDataLine line;
    private final Object lineLock;
    private final BooleanControl muteControl;
    private final FloatControl panControl;

    AudioPlayback(final AudioInputStream data, final SourceDataLine line) {
        this.data = data;
        this.line = line;
        this.lineLock = new Object();
        if (line.isControlSupported(BooleanControl.Type.MUTE)) {
            this.muteControl = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
        }
        else {
            this.muteControl = null;
        }

        if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            this.gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
        }
        else {
            this.gainControl = null;
        }

        if (line.isControlSupported(FloatControl.Type.BALANCE)) {
            this.balanceControl = (FloatControl) line.getControl(FloatControl.Type.BALANCE);
        }
        else {
            this.balanceControl = null;
        }

        if (line.isControlSupported(FloatControl.Type.PAN)) {
            this.panControl = (FloatControl) line.getControl(FloatControl.Type.PAN);
        }
        else {
            panControl = null;
        }

        line.start();
    }

    public boolean isPaused() {
        synchronized (this.lineLock) {
            return !this.line.isActive();
        }
    }

    boolean hasFinished() {
        synchronized (this.lineLock) {

            try {
                // Data is available -> not finished
                if (this.data.available() > 0) {
                    return false;
                }
            }
            catch (final IOException ex) {
            }

            // No data available, check if there is data in line
            return this.line.available() >= this.line.getBufferSize();
        }
    }

    public void stop() {
        synchronized (this.lineLock) {
            this.line.flush();
            this.line.close();
            try {
                this.data.close();
            }
            catch (final IOException ex) {
//            Log.warning(ex, "jeda.sound.playback.stop.error");
            }
        }
    }

    public void setPaused(boolean paused) {
        synchronized (this.lineLock) {
            if (this.isPaused() == paused) {
                return;
            }
            else if (paused) {
                this.line.stop();
            }
            else {
                this.line.start();
            }
        }
    }

    public void step() {
        synchronized (this.lineLock) {
            if (!this.line.isOpen()) {
                return;
            }
        }

        try {
            final byte[] buffer = new byte[1024];
            final int read = this.data.read(buffer, 0, Math.min(line.available(), buffer.length));
            if (read > 0) {
                synchronized (this.lineLock) {
                    this.line.write(buffer, 0, read);
                }
            }
        }
        catch (final IOException ex) {
//            Log.warning(ex, "jeda.sound.playback.read.error");
        }
    }
}
