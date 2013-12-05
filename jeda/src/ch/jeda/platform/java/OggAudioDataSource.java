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

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;

public class OggAudioDataSource implements AudioDataSource {

    private static final int BUFSIZE = 4096 * 2;
    private final BufferedInputStream bitStream;
    private AudioFormat audioFormat;
    private SyncState syncState;
    private StreamState streamState;
    private Page og;
    private Packet op;
    private Info vi;
    private Comment vc;
    private DspState vd;
    private Block vb;
    private int bytes;
    private boolean chained = false;
    private int convsize = BUFSIZE * 2;

    public OggAudioDataSource(final InputStream in) {
        this.bitStream = new BufferedInputStream(in);
        this.bitStream.mark(Integer.MAX_VALUE);

        this.syncState = new SyncState();
        this.streamState = new StreamState();
        og = new Page();
        op = new Packet();

        vi = new Info();
        vc = new Comment();
        vd = new DspState();
        vb = new Block(vd);

        this.syncState.init();
    }

    @Override
    public AudioFormat getFormat() {
        return this.audioFormat;
    }

    @Override
    public void reset() throws IOException {
        int index = this.syncState.buffer(BUFSIZE);
        this.bytes = this.bitStream.read(this.syncState.data, index, BUFSIZE);
        this.syncState.wrote(this.bytes);
        if (this.chained) {
            this.chained = false;
        }
        else {
            if (this.syncState.pageout(this.og) != 1) {
//                if (bytes < BUFSIZE) {
//                    break;
//                }
//
//                throw new InternalException("Input does not appear to be an Ogg bitstream.");
            }
        }

        this.streamState.init(this.og.serialno());
        this.streamState.reset();
        this.vi.init();
        this.vc.init();
        if (this.streamState.pagein(this.og) < 0) {
            // error; stream version mismatch perhaps
            throw new IOException("Error reading first page of Ogg bitstream data.");
        }

        if (this.streamState.packetout(this.op) != 1) {
            // no page? must not be vorbis
            throw new IOException("Error reading initial header packet.");
        }

        if (this.vi.synthesis_headerin(this.vc, this.op) < 0) {
            // error case; not a vorbis header
            throw new IOException("This Ogg bitstream does not contain Vorbis audio data.");
        }

        int i = 0;
        while (i < 2) {
            while (i < 2) {
                int result = this.syncState.pageout(this.og);
                if (result == 0) {
                    break; // Need more data
                }

                if (result == 1) {
                    this.streamState.pagein(og);
                    while (i < 2) {
                        result = this.streamState.packetout(this.op);
                        if (result == 0) {
                            break;
                        }
                        if (result == -1) {
                            throw new IOException("Corrupt secondary header.  Exiting.");
                        }
                        this.vi.synthesis_headerin(this.vc, this.op);
                        i++;
                    }
                }
            }

            index = this.syncState.buffer(BUFSIZE);
            this.bytes = this.bitStream.read(this.syncState.data, index, BUFSIZE);
            if (this.bytes == 0 && i < 2) {
                throw new IOException("End of file before finding all Vorbis headers!");
            }

            this.syncState.wrote(this.bytes);
        }

        this.convsize = BUFSIZE / this.vi.channels;
        this.vd.synthesis_init(this.vi);
        this.vb.init(this.vd);
        this.audioFormat = new AudioFormat(this.vi.rate, 16, this.vi.channels, true, false);
    }

    @Override
    public int available() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int read(byte[] buffer, int length) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
//    private void playStream() throws IOException {
//        int eos = 0;
//
//        float[][][] _pcmf = new float[1][][];
//        int[] _index = new int[this.vi.channels];
//
//        while (eos == 0) {
//            while (eos == 0) {
//                if (player != me) {
//                    return;
//                }
//
//                int result = this.syncState.pageout(og);
//                if (result == 0) {
//                    break; // need more data
//                }
//                if (result == -1) { // missing or corrupt data at this page
//                    // position
//                    // System.err.println("Corrupt or missing data in
//                    // bitstream;
//                    // continuing...");
//                }
//                else {
//                    streamState.pagein(og);
//
//                    if (og.granulepos() == 0) { //
//                        chained = true; //
//                        eos = 1; //
//                        break; //
//                    } //
//
//                    while (true) {
//                        if (checkState()) {
//                            return;
//                        }
//
//                        result = streamState.packetout(op);
//                        if (result == 0) {
//                            break; // need more data
//                        }
//                        if (result == -1) { // missing or corrupt data at
//                            // this page position
//                            // no reason to complain; already complained
//                            // above
//                            // System.err.println("no reason to complain;
//                            // already complained above");
//                        }
//                        else {
//                            // we have a packet. Decode it
//                            int samples;
//                            if (vb.synthesis(op) == 0) { // test for
//                                // success!
//                                vd.synthesis_blockin(vb);
//                            }
//                            while ((samples = vd.synthesis_pcmout(_pcmf,
//                                                                  _index)) > 0) {
//                                if (checkState()) {
//                                    return;
//                                }
//
//                                float[][] pcmf = _pcmf[0];
//                                int bout = (samples < convsize ? samples : convsize);
//
//                                // convert doubles to 16 bit signed ints
//                                // (host order) and
//                                // interleave
//                                for (i = 0; i < vi.channels; i++) {
//                                    int ptr = i * 2;
//                                    // int ptr=i;
//                                    int mono = _index[i];
//                                    for (int j = 0; j < bout; j++) {
//                                        int val = (int) (pcmf[i][mono + j] * 32767.);
//                                        if (val > 32767) {
//                                            val = 32767;
//                                        }
//                                        if (val < -32768) {
//                                            val = -32768;
//                                        }
//                                        if (val < 0) {
//                                            val = val | 0x8000;
//                                        }
//                                        convbuffer[ptr] = (byte) (val);
//                                        convbuffer[ptr + 1] = (byte) (val >>> 8);
//                                        ptr += 2 * (vi.channels);
//                                    }
//                                }
//                                outputLine.write(convbuffer, 0, 2 *
//                                                                vi.channels * bout);
//                                vd.synthesis_read(bout);
//                            }
//                        }
//                    }
//                    if (og.eos() != 0) {
//                        eos = 1;
//                    }
//                }
//            }
//
//            if (eos == 0) {
//                index = this.syncState.buffer(BUFSIZE);
//                buffer = this.syncState.data;
//                try {
//                    bytes = bitStream.read(buffer, index, BUFSIZE);
//                }
//                catch (Exception e) {
//                    throw new InternalException(e);
//                }
//                if (bytes == -1) {
//                    break;
//                }
//                this.syncState.wrote(bytes);
//                if (bytes == 0) {
//                    eos = 1;
//                }
//            }
//        }
//
//        streamState.clear();
//        vb.clear();
//        vd.clear();
//        vi.clear();
//    }
//
//    oy.clear ();
//}

