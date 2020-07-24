package com.grishberg.textcarddimen;

import java.util.HashMap;

public class FontDimensions {
    public final float topDelta;
    public final float height;
    public final float newLineHeight;
    private final boolean bold = false;
    private final HashMap<Character, Float> charMap;

    public FontDimensions(float topDelta, float height, HashMap<Character, Float> charMap) {
        this.topDelta = topDelta;
        this.charMap = charMap;
        this.height = height;
        this.newLineHeight = height;
    }

    public float calculateCharSize(char c) {
        Float integer = charMap.get(c);
        return integer == null ? 0 : integer;
    }
}
