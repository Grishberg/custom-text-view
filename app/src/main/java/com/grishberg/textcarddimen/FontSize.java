package com.grishberg.textcarddimen;

public class FontSize {
    final float w;
    final float h;

    public FontSize(float w, float h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FontSize)) {
            return false;
        }
        FontSize other = (FontSize) obj;
        return Float.compare(w, other.w) == 0 &&
                Float.compare(h, other.h) == 0;
    }

    @Override
    public String toString() {
        return "{w=" + w + ", h=" + h + "}";
    }
}
