/*
 * Copyright (C) 2014 by Stefan Rothe
 * Copyright (C) 2010 by Thomas Mueller
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
package org.mp3transform;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Decoder {

    public static final int BUFFER_SIZE = 2 * 1152;
    public static final int MAX_CHANNELS = 2;
    private static final boolean BENCHMARK = false;
    private final byte[] buffer;
    protected final int[] bufferPointer;
    private final Object lock;
    protected int channels;
    private SynthesisFilter filter1;
    private SynthesisFilter filter2;
    private Layer3Decoder l3decoder;
    private boolean initialized;
    private SourceDataLine line;
    private boolean paused;
    private boolean stopRequested;

    public Decoder() {
        this.buffer = new byte[BUFFER_SIZE * 2];
        this.bufferPointer = new int[MAX_CHANNELS];
        this.lock = new Object();
        this.paused = false;
        this.stopRequested = false;
    }

    public boolean isPaused() {
        synchronized (this.lock) {
            return this.paused;
        }
    }

    public void requestStop() {
        synchronized (this.lock) {
            this.stopRequested = true;
        }
    }

    public void setPaused(final boolean paused) {
        synchronized (this.lock) {
            this.paused = paused;
        }
    }

    public void decodeFrame(Header header, Bitstream stream) throws IOException {
        if (!initialized) {
            double scaleFactor = 32700.0f;
            int mode = header.mode();
            int channels = mode == Header.MODE_SINGLE_CHANNEL ? 1 : 2;
            filter1 = new SynthesisFilter(0, scaleFactor);
            if (channels == 2) {
                filter2 = new SynthesisFilter(1, scaleFactor);
            }
            initialized = true;
        }
        if (l3decoder == null) {
            l3decoder = new Layer3Decoder(stream, header, filter1, filter2,
                                          this);
        }
        l3decoder.decodeFrame();
        writeBuffer();
    }

    protected void initOutputBuffer(SourceDataLine line, int numberOfChannels) {
        this.line = line;
        channels = numberOfChannels;
        for (int i = 0; i < channels; i++) {
            bufferPointer[i] = i + i;
        }
    }

    public void appendSamples(int channel, double[] f) {
        int p = bufferPointer[channel];
        for (int i = 0; i < 32; i++) {
            double sample = f[i];
            int s = (int) ((sample > 32767.0f) ? 32767 : ((sample < -32768.0f) ? -32768 : sample));
            buffer[p] = (byte) (s >> 8);
            buffer[p + 1] = (byte) (s & 0xff);
            p += 4;
        }
        bufferPointer[channel] = p;
    }

    protected void writeBuffer() throws IOException {
        if (line != null) {
            line.write(buffer, 0, bufferPointer[0]);
        }
        for (int i = 0; i < channels; i++) {
            bufferPointer[i] = i + i;
        }
    }

    public void play(String name, InputStream in) throws IOException {
        synchronized (this.lock) {
            stopRequested = false;
        }

        int frameCount = Integer.MAX_VALUE;

        // int testing;
        // frameCount = 100;

        Bitstream stream = new Bitstream(in);
        SourceDataLine line = null;
        int error = 0;
        for (int frame = 0; !this.isStopRequested() && frame < frameCount; frame++) {
            if (this.isPaused()) {
                line.stop();
                while (this.isPaused() && !this.isStopRequested()) {
                    try {
                        Thread.sleep(10);
                    }
                    catch (InterruptedException e) {
                        // ignore
                    }
                }
                line.flush();
                line.start();
            }
            try {
                Header header = stream.readFrame();
                if (header == null) {
                    break;
                }
                if (this.channels == 0) {
                    int channels = (header.mode() == Header.MODE_SINGLE_CHANNEL) ? 1 : 2;
                    float sampleRate = header.frequency();
                    int sampleSize = 16;
                    AudioFormat format = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED, sampleRate,
                        sampleSize, channels, channels * (sampleSize / 8),
                        sampleRate, true);
                    // big endian
                    SourceDataLine.Info info = new DataLine.Info(
                        SourceDataLine.class, format);
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    if (BENCHMARK) {
                        this.initOutputBuffer(null, channels);
                    }
                    else {
                        this.initOutputBuffer(line, channels);
                    }
                    // TODO sometimes the line can not be opened (maybe not enough system resources?): display error message
                    // System.out.println(line.getFormat().toString());
                    line.open(format);
                    line.start();
                }
                while (line.available() < 100) {
                    Thread.yield();
                    Thread.sleep(20);
                }
                this.decodeFrame(header, stream);
            }
            catch (Exception e) {
                if (error++ > 1000) {
                    break;
                }
                // TODO should not write directly
                // System.out.println("Error at: " + name + " Frame: " + frame + " Error: " + e.toString());
                // e.printStackTrace();
            }
            finally {
                stream.closeFrame();
            }
        }

        in.close();
        if (line != null) {
            line.stop();
            line.close();
            line = null;
        }
    }

    private boolean isStopRequested() {
        synchronized (this.lock) {
            return this.stopRequested;
        }
    }
}
