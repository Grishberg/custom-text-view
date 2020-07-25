package com.grishberg.textcarddimen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.grishberg.textcarddimen.common.Logger;

import java.util.Collections;
import java.util.List;

public class CustomTextView extends View {
    private static final String TAG = CustomTextView.class.getSimpleName();
    private Logger logger = Logger.STUB;
    private final Paint textPaint = new Paint();
    private String text = "";
    private List<TextLines.TextLine> lines = Collections.emptyList();
    private float fontScale;
    private TextLines textLines;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint.setColor(Color.WHITE);
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
        textLines = new TextLines(fd);
        setText(text);
    }

    public void setTypeface(Typeface tf) {
        textPaint.setTypeface(tf);
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
        if (text.isEmpty()) {
            return;
        }
        prepareLines();
    }

    private void prepareLines() {
        if (textLines == null) {
            return;
        }
        lines = textLines.calculateTextLines(text, getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (TextLines.TextLine line : lines) {
            canvas.drawText(line.getText(), line.x, line.y, textPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "w=" + w + ", h=" + h);
    }
}
