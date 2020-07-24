package com.grishberg.textcarddimen;

import com.grishberg.textcarddimen.common.Logger;

public class TextBounds {
    private static final String TAG = "TB";
    private final Logger logger;

    public TextBounds(Logger logger) {
        this.logger = logger;
    }

    public FontSize calculateTextSize(
            String text,
            FontDimensions dimen,
            float fontScale,
            int targetWidth) {
        StringBuilder lastWord = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();
        StringBuilder prevWord = new StringBuilder();
        float currentLineWidth = 0;
        float h = dimen.topDelta * fontScale;
        float prevWordWidth = 0f;
        float lastWordWidth = 0f;
        float prevH = 0;
        float charW = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            float charWidth = dimen.calculateCharSize(c);
            prevH = dimen.height * fontScale;
            charW = charWidth * fontScale;

            if (isDivider(c)) {
                if (prevWordWidth + currentLineWidth <= targetWidth) {
                    currentLineWidth += prevWordWidth;
                    prevWordWidth = 0;
                    currentLine = new StringBuilder();
                    currentLine.append(prevWord);
                    currentLine.append(lastWord);
                }
                lastWordWidth = 0;
                lastWord = new StringBuilder();
            }

            if (currentLineWidth + charW > targetWidth) {
                h += prevH;
                if (lastWordWidth + prevWordWidth >= targetWidth) {
                    lastWordWidth = charW;
                    lastWord = new StringBuilder();
                    lastWord.append(c);

                    currentLineWidth = charW;
                    currentLine = new StringBuilder();
                    currentLine.append(c);
                    continue;
                }
                currentLineWidth = charW;
                currentLine = new StringBuilder();
                currentLine.append(c);

                prevWordWidth = lastWordWidth;
                prevWord = lastWord;

                lastWordWidth = charW;
                lastWord = new StringBuilder();
                lastWord.append(c);
                logger.d(TAG, "new line, w =" + currentLineWidth);
                continue;
            }
            if (prevWordWidth + currentLineWidth + charW > targetWidth) {
                if (prevWordWidth <= targetWidth) {
                    prevWordWidth = 0;
                }
            }
            if (!isDivider(c)) {
                lastWordWidth += charW;
                lastWord.append(c);
            }
            currentLineWidth += charW;
            currentLine.append(c);
        }
        if (prevWordWidth + currentLineWidth > targetWidth) {
            currentLineWidth = lastWordWidth;
        }
        if (prevWordWidth + currentLineWidth < targetWidth){
            currentLineWidth = prevWordWidth + currentLineWidth;
        }
        h += prevH;
        return new FontSize(currentLineWidth, h);
    }

    private boolean isDivider(char c) {
        return c == ' ';
    }
}
