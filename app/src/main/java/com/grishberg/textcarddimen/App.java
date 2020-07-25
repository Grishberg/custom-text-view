package com.grishberg.textcarddimen;

import android.app.Application;
import android.util.Log;

public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        float textSizeInDp = getResources().getDimension(R.dimen.cardTextSize);
        float textSizeInSp = getResources().getDimension(R.dimen.cardTextSizeInSp);
        float textScale = getResources().getConfiguration().fontScale;
        Log.d(TAG, "text size (dp) = " + textSizeInDp + ", (sp) = " + textSizeInSp +
                ", scale = " + textScale +
                ", calculated scale = " + textSizeInSp / textSizeInDp);
    }
}
