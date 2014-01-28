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

import ch.jeda.Log;
import java.io.IOException;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

class AudioPlayback {

    private final FloatControl balanceControl;
    private final FloatControl gainControl;
    private final SourceDataLine line;
    private final Object lineLock;
    private final BooleanControl muteControl;
    private final FloatControl panControl;
    private final AudioDataSource source;

    AudioPlayback(final AudioDataSource source, final SourceDataLine line) {
        this.source = source;
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
                if (this.source.available() > 0) {
                    return false;
                }
            }
            catch (final IOException ex) {
            }

            // No data available, check if there is data in line
            return this.line.available() >= this.line.getBufferSize();
        }
    }

    public void setBalance(final float balance) {
        if (balance < -1f || balance > 1f) {
            throw new IllegalArgumentException("Valid range of balance is -1 to 1.");
        }

        if (this.balanceControl != null) {
            this.balanceControl.setValue(balance);
        }
    }

    public void setDefaultGain() {
        if (this.gainControl != null) {
            this.gainControl.setValue(0f);
        }
    }

    public void setGain(final float gain) {
        if (gain < 0f || gain > 1f) {
            throw new IllegalArgumentException("Valid range of gain is 0 to 1.");
        }

        if (this.gainControl != null) {
            float max = this.gainControl.getMaximum();
            float min = this.gainControl.getMinimum();
            this.gainControl.setValue(min + (max - min) * gain);
        }
    }

    public void stop() {
        synchronized (this.lineLock) {
            this.line.stop();
            this.line.flush();
            this.line.close();
            try {
                this.source.close();
            }
            catch (final IOException ex) {
                Log.err(ex, "jeda.audio.error.internal-close");
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
            final int read = this.source.read(buffer, Math.min(line.available(), buffer.length));
            if (read > 0) {
                synchronized (this.lineLock) {
                    this.line.write(buffer, 0, read);
                }
            }
        }
        catch (final IOException ex) {
            Log.err(ex, "jeda.audio.error.internal-read");
        }
    }
}
