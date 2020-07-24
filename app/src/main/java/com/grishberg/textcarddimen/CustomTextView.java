package com.grishberg.textcarddimen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.grishberg.textcarddimen.common.Logger;

import java.util.ArrayList;

public class CustomTextView extends View {
    private static final String TAG = CustomTextView.class.getSimpleName();
    private Logger logger = Logger.STUB;
    private final Paint textPaint = new Paint();
    private String text = "";
    private ArrayList<TextLine> lines = new ArrayList<>();
    private FontDimensions fontDimensions;
    private float fontScale;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint.setColor(Color.BLACK);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        textPaint.setColor(textColor);
    }

    public void setFontDimensions(FontDimensions fd) {
        fontDimensions = fd;
    }

    public void setText(String text) {
        this.text = text;
        if (getMeasuredWidth() > 0 && !text.isEmpty()) {
            prepareLines();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!text.isEmpty()) {
            return;
        }
        prepareLines();
    }

    private void prepareLines() {
        calculateTextSize(text, fontDimensions, 1f, getMeasuredWidth());
    }

    private FontSize calculateTextSize(
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
        if (prevWordWidth + currentLineWidth < targetWidth) {
            currentLineWidth = prevWordWidth + currentLineWidth;
        }
        h += prevH;
        return new FontSize(currentLineWidth, h);
    }

    private boolean isDivider(char c) {
        return c == ' ';
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w=" + w + ", h=" + h);
    }

    private class TextLine {
        final String text;
        final float x;
        final float y;

        public TextLine(String text, float x, float y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }
}
