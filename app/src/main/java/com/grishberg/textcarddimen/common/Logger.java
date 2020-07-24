package com.grishberg.textcarddimen.common;

public interface Logger {
    void d(String t, String msg);

    Logger STUB = new Logger() {
        @Override
        public void d(String t, String msg) {
            /* stub */
        }
    };
}
