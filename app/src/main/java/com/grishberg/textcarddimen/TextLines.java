package com.grishberg.textcarddimen;

import java.util.ArrayList;
import java.util.List;

public class TextLines {
    private static final StringBuilder EMPTY_SB = new StringBuilder();

    private SingleLine singleLine = new SingleLine();
    private MultiLine multiLine = new MultiLine();
    private final FontDimensions dimensions;

    private State state = singleLine;
    private float currentLineWidth;
    private StringBuilder currentLine = new StringBuilder();
    private ArrayList<TextLine> lines = new ArrayList<>();
    private String cachedText;
    private int width;
    private float spaceWidth;
    private float currentTop;

    public TextLines(FontDimensions dimensions) {
        this.dimensions = dimensions;
    }

    public List<TextLine> calculateTextLines(String text, int width) {
        if (cachedText == text) {
            return lines;
        }
        cachedText = text;
        this.width = width;
        lines.clear();
        spaceWidth = dimensions.calculateCharWidth(' ');
        state = singleLine;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            state.processSymbol(c);
        }
        state.processEndOfString();
        cachedText = text;
        return lines;
    }

    private class SingleLine implements State {
        private StringBuilder previousLine;
        private StringBuilder currentWord = new StringBuilder();
        private float currentWordWidth;

        @Override
        public void init(StringBuilder previousLine, String lastWord, float lastWordWidth) {
            this.previousLine = previousLine;
            currentWord = new StringBuilder();
            currentWordWidth = 0f;
        }

        @Override
        public void processSymbol(char c) {
            float charWidth = dimensions.calculateCharWidth(c);

            if (isDivider(c)) {
                currentLine.append(currentWord);
                currentLine.append(c);
                currentLineWidth += currentWordWidth + charWidth;

                currentWord = new StringBuilder();
                currentWordWidth = 0f;
                return;
            }

            if (currentWordWidth + currentLineWidth + charWidth <= width) {
                currentWordWidth += charWidth;
                currentWord.append(c);
                return;
            }

            lines.add(new TextLine(currentLine, 0f, currentTop));
            currentTop += dimensions.getFontHeight();
            state = multiLine;
            state.init(currentLine, currentWord.toString(), currentWordWidth);
            currentLine = new StringBuilder();
            currentLineWidth = 0f;
            state.processSymbol(c);
            currentLine = new StringBuilder();
        }

        @Override
        public void processEndOfString() {
            currentLine.append(currentWord);
            lines.add(new TextLine(currentLine, 0f, currentTop));
        }
    }

    private class MultiLine implements State {
        private StringBuilder currentWord = new StringBuilder();
        private float currentWordWidth;

        private StringBuilder previousLine;
        private String previousWord;
        private float previousWordWidth;

        @Override
        public void init(StringBuilder previousLine, String lastWord, float lastWordWidth) {
            this.previousLine = previousLine;
            this.previousWord = lastWord;
            this.previousWordWidth = lastWordWidth;
            currentWord = new StringBuilder();
            currentWordWidth = 0f;
        }

        @Override
        public void processSymbol(char c) {
            float charWidth = dimensions.calculateCharWidth(c);
            if (isDivider(c)) {
                if (currentWordWidth + previousWordWidth + currentLineWidth <= width) {
                    // place word before new line to current line.
                    currentLine.append(previousWord);
                    currentLine.append(currentWord);
                    currentLineWidth += previousWordWidth + currentWordWidth;
                    state = singleLine;
                    state.init(currentWord, "", 0f);
                    state.processSymbol(c);
                    return;
                } // else ?
            }

            if (currentLineWidth + currentWordWidth + charWidth <= width) {
                currentWord.append(c);
                currentWordWidth += charWidth;
                return;
            }

            // new line
            if (previousWordWidth > 0) {
                // store previous word in previous line
                previousLine.append(previousWord);
                currentLine.append(currentWord);
                currentLineWidth += currentWordWidth;

                lines.add(new TextLine(currentLine, 0f, currentTop));
                currentTop += dimensions.getFontHeight();
                currentLine = new StringBuilder();
                currentLineWidth = 0f;
                state = singleLine;
                state.init(EMPTY_SB, "", 0f);
                state.processSymbol(c);
                return;
            }
        }

        @Override
        public void processEndOfString() {
            if (currentLineWidth + currentWordWidth + previousWordWidth <= width) {
                currentLine.append(previousWord);
            } else {
                previousLine.append(previousWord);
            }
            currentLine.append(currentWord);
            lines.add(new TextLine(currentLine, 0f, currentTop));
        }
    }

    private boolean isDivider(char c) {
        return c == ' ';
    }

    private interface State {
        void init(StringBuilder previousLine, String lastWord, float lastWordWidth);

        void processSymbol(char c);

        void processEndOfString();
    }

    static class TextLine {
        final float x;
        final float y;
        final StringBuilder text;

        public TextLine(StringBuilder text, float x, float y) {
            this.x = x;
            this.y = y;
            this.text = text;
        }

        public TextLine(String text, float x, float y) {
            this.x = x;
            this.y = y;
            this.text = new StringBuilder().append(text);
        }

        public String getText() {
            return text.toString().trim();
        }
    }
}
