package com.grishberg.textcarddimen;

import com.grishberg.textcarddimen.common.Logger;

import java.util.ArrayList;
import java.util.List;

class TextMeasureDelegate {
    private static final String TAG = TextMeasureDelegate.class.getSimpleName();
    private final Logger logger;
    /**
     * Word after last space.
     */
    private StringBuilder wordFromSpace = new StringBuilder();
    private String previousWordFromFromSpace = "";
    private StringBuilder currentLine = new StringBuilder();
    private StringBuilder wordFromBreak = new StringBuilder();
    private float currentLineWidth = 0;
    private float currentHeight = 0;
    private float wordFromSpaceWidth = 0f;
    private float lastWordWidth = 0f;
    private float prevH = 0;
    private float currentCharWidth = 0;
    private int targetWidth = 0;

    TextMeasureDelegate(Logger logger) {
        this.logger = logger;
    }

    public List<TextLine> calculateTextLines(
            String text,
            FontDimensions dimen,
            float fontScale,
            int targetWidth) {
        ArrayList<TextLine> lines = new ArrayList<>();

        this.targetWidth = targetWidth;
        wordFromSpace = new StringBuilder();
        wordFromBreak = new StringBuilder();
        currentLine = new StringBuilder();
        wordFromBreak = new StringBuilder();
        currentLineWidth = 0;
        currentHeight = dimen.topDelta * fontScale;
        wordFromSpaceWidth = 0f;
        lastWordWidth = 0f;
        prevH = 0;
        currentCharWidth = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            currentCharWidth = dimen.calculateCharSize(c);

            if (isCurrentLineExceeseWidth()) {

            }

            if (!isDivider(c)) {
                lastWordWidth += currentCharWidth;
                wordFromSpace.append(c);
            }
            currentLine.append(c);
            currentLineWidth += currentCharWidth;
        }
        return lines;
    }

    /**
     * previous line: AAAA word_from_space
     * current line: word_from_break DDD
     *
     * @param text
     * @param dimen
     * @param fontScale
     * @param targetWidth
     * @return
     */
    public List<TextLine> calculateTextLines2(
            String text,
            FontDimensions dimen,
            float fontScale,
            int targetWidth) {
        ArrayList<TextLine> lines = new ArrayList<>();

        this.targetWidth = targetWidth;
        wordFromSpace = new StringBuilder();
        wordFromBreak = new StringBuilder();
        currentLine = new StringBuilder();
        wordFromBreak = new StringBuilder();
        currentLineWidth = 0;
        currentHeight = dimen.topDelta * fontScale;
        wordFromSpaceWidth = 0f;
        lastWordWidth = 0f;
        prevH = 0;
        currentCharWidth = 0;
        StringBuilder previousLine = currentLine;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            currentCharWidth = dimen.calculateCharSize(c);
            prevH = dimen.height * fontScale;
            this.currentCharWidth = currentCharWidth * fontScale;

            if (isDivider(c)) {
                // check: if wordFromSpaceWidth + currentLine is less then targetWidth
                if (wordFromSpaceWidth + currentLineWidth <= targetWidth) {
                    // place wordFromSpace and currentLine in current line.
                    currentLineWidth += wordFromSpaceWidth;
                    wordFromSpaceWidth = 0;
                    currentLine = new StringBuilder();
                    currentLine.append(wordFromBreak);
                    currentLine.append(wordFromSpace);
                    if (!previousWordFromFromSpace.isEmpty()) {
                        lines.add(new TextLine(previousWordFromFromSpace, 0f, currentHeight - prevH));
                    }
                } else {
                    // add wordFromSpace to previous line
                    previousLine.append(wordFromSpace);
                }
                previousWordFromFromSpace = wordFromSpace.toString();
                lastWordWidth = 0;
                wordFromSpace = new StringBuilder();
            }

            if (isCurrentLineExceeseWidth()) {
                currentHeight += prevH;
                if (lastWordWidth + wordFromSpaceWidth >= targetWidth) {
                    lastWordWidth = this.currentCharWidth;
                    wordFromSpace = new StringBuilder();
                    wordFromSpace.append(c);

                    currentLineWidth = this.currentCharWidth;
                    lines.add(new TextLine(currentLine, 0f, currentHeight - prevH));
                    currentLine = new StringBuilder();
                    currentLine.append(c);
                    previousLine = currentLine;
                    continue;
                }

                currentLineWidth = currentCharWidth;
                currentLine = new StringBuilder();
                currentLine.append(c);
                previousLine = currentLine;

                wordFromSpaceWidth = lastWordWidth;
                wordFromBreak = wordFromSpace;

                lastWordWidth = this.currentCharWidth;
                wordFromSpace = new StringBuilder();
                wordFromSpace.append(c);
                logger.d(TAG, "new line, w =" + currentLineWidth);
                continue;
            }
            if (wordFromSpaceWidth + currentLineWidth + this.currentCharWidth > targetWidth) {
                if (wordFromSpaceWidth <= targetWidth) {
                    wordFromSpaceWidth = 0;
                }
            }
            if (!isDivider(c)) {
                lastWordWidth += this.currentCharWidth;
                wordFromSpace.append(c);
            }
            currentLineWidth += this.currentCharWidth;
            currentLine.append(c);
        }
        if (wordFromSpaceWidth + currentLineWidth > targetWidth) {
            currentLineWidth = lastWordWidth;
        }
        if (wordFromSpaceWidth + currentLineWidth < targetWidth) {
            currentLineWidth = wordFromSpaceWidth + currentLineWidth;
        }
        currentHeight += prevH;
        return lines;
    }

    private boolean isCurrentLineExceeseWidth() {
        return currentLineWidth + currentCharWidth > targetWidth;
    }

    private boolean isDivider(char c) {
        return c == ' ';
    }

    static class TextLine {
        private final StringBuilder stringBuilder;
        private String text = null;
        final float x;
        final float y;

        public TextLine(StringBuilder sb, float x, float y) {
            this.stringBuilder = sb;
            this.x = x;
            this.y = y;
        }

        TextLine(String text, float x, float y) {
            this.text = text;
            this.x = x;
            this.y = y;
            stringBuilder = new StringBuilder();
        }

        public String getText() {
            if (text == null) {
                text = stringBuilder.toString();
            }
            return text;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TextLine)) {
                return false;
            }
            TextLine tl = (TextLine) obj;

            return tl.getText().equals(getText()) && x == tl.x && y == tl.y;
        }
    }
}
