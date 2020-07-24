package com.grishberg.textcarddimen.common;

import android.util.Log;

public class LogcatLogger implements Logger {
    @Override
    public void d(String t, String msg) {
        Log.d(t, msg);
    }
}
