package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 16/8/9.
 */
public class BeatBox {

    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssetManager;
    private List<Sound> mSoundList = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        } else {
            SoundPool.Builder soundPoolBuilder = new SoundPool.Builder();
            soundPoolBuilder.setMaxStreams(MAX_SOUNDS);

            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);

            mSoundPool = soundPoolBuilder.setAudioAttributes(attrBuilder.build()).build();
        }
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);

            Log.d(TAG, "FOUND " + soundNames.length + " sounds");

            for (String fileName: soundNames) {
                String assetPath = SOUNDS_FOLDER + "/" + fileName;
                Sound sound = new Sound(assetPath);
                loadSound(sound);
                mSoundList.add(sound);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
        }
    }

    private void loadSound(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssetManager.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public List<Sound> listSounds() {
        return mSoundList;
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }

        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        mSoundPool.release();
    }
}
