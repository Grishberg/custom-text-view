package com.grishberg.textcarddimen;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FontDimensions {
    private static final String TAG = "FontDimensions";
    private final boolean bold = false;
    private final HashMap<Character, Float> charMap;
    private float top;
    private float ascent;
    private float descent;
    private float bottom;
    private float storedFontSize;

    public FontDimensions(HashMap<Character, Float> charMap,
                          float top,
                          float ascent,
                          float descent,
                          float bottom,
                          float fontSize) {
        this.charMap = charMap;
        this.top = top;
        this.ascent = ascent;
        this.descent = descent;
        this.bottom = bottom;
        storedFontSize = fontSize;
    }

    public FontDimensions(File file) {
        charMap = new HashMap<>();

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            int count = ois.readInt();
            Log.d(TAG, "count = " + count);
            storedFontSize = ois.readFloat();
            Log.d(TAG, "font size = " + storedFontSize);
            top = ois.readFloat();
            ascent = ois.readFloat();
            descent = ois.readFloat();
            bottom = ois.readFloat();
            Log.d(TAG, "h = " + (descent - top));
            for (int i = 0; i < count; i++) {
                charMap.put(ois.readChar(), ois.readFloat());
            }
        } catch (Exception e) {
            storedFontSize = 0f;
            Log.e(TAG, "Read error", e);
        }
    }

    public FontDimensions(InputStream is) {
        charMap = new HashMap<>();

        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            ObjectInputStream ois = new ObjectInputStream(bis);
            int count = ois.readInt();
            Log.d(TAG, "count = " + count);
            storedFontSize = ois.readFloat();
            Log.d(TAG, "font size = " + storedFontSize);
            top = ois.readFloat();
            ascent = ois.readFloat();
            descent = ois.readFloat();
            bottom = ois.readFloat();
            Log.d(TAG, "h = " + (descent - top));
            for (int i = 0; i < count; i++) {
                charMap.put(ois.readChar(), ois.readFloat());
            }
        } catch (Exception e) {
            storedFontSize = 0f;
            Log.e(TAG, "Read error", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    public float getFontHeight() {
        return descent - top;
    }

    public float calculateCharWidth(char c) {
        return charMap.get(c);
    }

    public void save(File file) {
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(getBytes()); // in loop probably
            bos.close();
        } catch (Exception e) {
            Log.e(TAG, "Save error", e);
        }
    }

    private byte[] getBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeInt(charMap.size());
            out.writeFloat(storedFontSize);
            out.writeFloat(top);
            out.writeFloat(ascent);
            out.writeFloat(descent);
            out.writeFloat(bottom);
            for (Map.Entry<Character, Float> entry : charMap.entrySet()) {
                out.writeChar(entry.getKey());
                out.writeFloat(entry.getValue());
            }
            out.flush();
            return bos.toByteArray();
        } finally {
            bos.close();
        }
    }
}