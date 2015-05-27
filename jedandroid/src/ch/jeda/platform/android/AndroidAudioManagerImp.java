/*
 * Copyright (C) 2013 - 2015 by Stefan Rothe
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
package ch.jeda.platform.android;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.SoundPool;
import ch.jeda.Log;
import ch.jeda.platform.AudioManagerImp;
import java.util.HashMap;
import java.util.Map;

class AndroidAudioManagerImp implements AudioManagerImp, MediaPlayer.OnCompletionListener {

    private static final String RES_PREFIX = "res:";
    private static final int DEFAULT_PRIORITY = 0;
    private AudioManagerImp.Callback callback;
    private final android.media.AudioManager imp;
    private final Main main;
    private final Map<String, Integer> soundMap;
    private final SoundPool soundPool;
    private MediaPlayer mediaPlayer;

    public AndroidAudioManagerImp(final Main main) {
        this.main = main;
        imp = (android.media.AudioManager) main.getSystemService(Activity.AUDIO_SERVICE);
        soundMap = new HashMap<String, Integer>();
        soundPool = new SoundPool(10, android.media.AudioManager.STREAM_MUSIC, 0);
    }

    public void onCompletion(final MediaPlayer mediaPlayer) {
        if (callback != null) {
            callback.playbackStopped();
        }
    }

    @Override
    public boolean isSoundAvailable(final String path) {
        return soundMap.containsKey(path);
    }

    @Override
    public void loadSound(final String path) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (soundMap.containsKey(path)) {
                    return;
                }

                if (path.startsWith(RES_PREFIX)) {
                    final int resId = getResourceId(path);
                    if (resId != 0) {
                        soundMap.put(path, soundPool.load(main, resId, DEFAULT_PRIORITY));
                    }
                }
                else {
                    soundMap.put(path, soundPool.load(path, DEFAULT_PRIORITY));
                }
            }
        });
    }

    @Override
    public void pausePlayback() {
        mediaPlayer.pause();
    }

    @Override
    public void playSound(final String path) {
        if (soundMap.containsKey(path)) {
            final float volume = getVolume();
            soundPool.play(soundMap.get(path), volume, volume, 0, 0, 1.0f);
        }
    }

    @Override
    public void resumePlayback() {
        mediaPlayer.start();
    }

    @Override
    public void setCallback(final Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean startPlayback(final String path) {
        final int resId = getResourceId(path);
        if (resId == 0) {
            return false;
        }

        mediaPlayer = MediaPlayer.create(main, resId);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
        return true;
    }

    @Override
    public void stopPlayback() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        callback.playbackStopped();
    }

    private int getResourceId(final String path) {
        final int slashPos = path.indexOf('/');
        final int dotPos = path.lastIndexOf('.');
        if (slashPos == -1 || dotPos == -1) {
            Log.e("Invalid audio resource name '", path, "'.");
            return 0;
        }

        final String type = path.substring(RES_PREFIX.length(), slashPos);
        final String name = path.substring(slashPos + 1, dotPos);
        return main.getResources().getIdentifier(name, type, main.getApplicationContext().getPackageName());
    }

    private float getVolume() {
        return (float) imp.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) /
               (float) imp.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC);
    }

}
