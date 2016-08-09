package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
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

    private AssetManager mAssetManager;
    private List<Sound> mSoundList = new ArrayList<>();

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        loadSound();
    }

    private void loadSound() {
        String[] soundNames;
        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);

            Log.d(TAG, "FOUND " + soundNames.length + " sounds");

            for (String fileName: soundNames) {
                String assetPath = SOUNDS_FOLDER + "/" + fileName;
                Sound sound = new Sound(assetPath);
                mSoundList.add(sound);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
        }
    }

    public List<Sound> listSounds() {
        return mSoundList;
    }
}
