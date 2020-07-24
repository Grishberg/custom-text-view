package com.grishberg.textcarddimen;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.grishberg.textcarddimen.common.LogcatLogger;

import java.util.HashMap;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/blag.ttf");
        TextView textView = findViewById(R.id.textView);
        //textView.setTypeface(typeface);

        TextView scaledTextView = findViewById(R.id.scaledTextView);
        //scaledTextView.setTypeface(typeface);

        TextView cardText = findViewById(R.id.cardText);
        //cardText.setTypeface(typeface);

        FontDimensions fd = getTextMetrics(getString(R.string.abc), textView);

        TextBounds tb = new TextBounds(new LogcatLogger());
        String test = getString(R.string.sample0);
        int targetWidth = getResources().getDimensionPixelSize(R.dimen.cardWidth);
        float scale = android.provider.Settings.System.getFloat(getContentResolver(),
                android.provider.Settings.System.FONT_SCALE, 1f);

        float fontScale = getResources().getConfiguration().fontScale;
        FontSize fs = tb.calculateTextSize(test, fd, fontScale, targetWidth);
        Log.d("SIZE", "target w=" + targetWidth + ", size ." + fs.toString());

        cardText.setText(test);

        ViewGroup content = findViewById(R.id.mainFrameLayout);

        FrameLayout borderView = new FrameLayout(this);
        borderView.setBackgroundColor(Color.YELLOW);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) fs.w, (int) fs.h);
        lp.gravity = Gravity.START | Gravity.TOP;
        borderView.setLayoutParams(lp);
        content.addView(borderView, 0);
    }

    private FontDimensions getTextMetrics(String text, TextView textView) {
        TextPaint paint = textView.getPaint();
        Paint.FontMetrics fm = paint.getFontMetrics();
        float topDelta = Math.abs(Math.abs(fm.top) - Math.abs(fm.ascent));

        HashMap<Character, Float> charMap = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);

            String symbolAsString = "" + c;
            textView.setText(symbolAsString);
            textView.measure(0, 0);
            float measuredWidth = paint.measureText(symbolAsString);
            Rect bounds = new Rect();
            paint.getTextBounds(symbolAsString, 0, symbolAsString.length(), bounds);

            charMap.put(c, measuredWidth);
        }
        float height = roundAvoid(fm.descent - fm.ascent, 3);
        return new FontDimensions(topDelta, height, charMap);
    }

    private float calculateLeading(TextView textView, TextView scaledTextView) {
        String text = "Ay";
        int charCount = 2;
        TextPaint paint = textView.getPaint();
        Log.d(
                "FONT_SIZE", "font scale = " + getResources().getConfiguration().fontScale +
                        ", font size = " + textView.getTextSize()
        );

        textView.setText(text);
        textView.measure(0, 0);

        scaledTextView.setText(text);
        scaledTextView.measure(0, 0);

        Rect staticTextSizeBounds = new Rect();
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height = roundAvoid(fm.bottom - fm.top, 3);
        float twoLineHeight = height * charCount;
        float multipleRowMeasuredHeight = textView.getMeasuredHeight();
        float measuredHeightScaled = scaledTextView.getMeasuredHeight();
        float calculatedScale = measuredHeightScaled / multipleRowMeasuredHeight;

        text = "A";
        textView.setText(text);
        textView.measure(0, 0);

        scaledTextView.setText(text);
        scaledTextView.measure(0, 0);
        float measuredHeightSingleChar = textView.getMeasuredHeight();
        float measuredHeightSingleCharScaled = scaledTextView.getMeasuredHeight();

        float d;
        if (charCount - 1 == 0) {
            d = roundAvoid((multipleRowMeasuredHeight - measuredHeightSingleChar), 3);
        } else {
            d = roundAvoid((measuredHeightSingleChar * charCount - multipleRowMeasuredHeight) / (charCount - 1), 3);
        }

        // calculatedScale = measuredHeightSingleCharScaled / measuredHeightSingleChar;
        TextPaint scaledPaint = scaledTextView.getPaint();
        float sizeInDp = paint.getTextSize() / paint.density;
        float sizeInSp = scaledPaint.getTextSize() / scaledPaint.density;
        Log.d("TEXT SIZE", "h = " + twoLineHeight + ", mh = " + multipleRowMeasuredHeight + " single char H =" + measuredHeightSingleChar +
                ", d = " + d + ", leading = " + fm.leading + ", calculated font scale  = " + calculatedScale +
                ", scale dp=" + sizeInDp + ", scale sp=" + sizeInSp);
        return d;
    }

    private float roundAvoid(float value, int places) {
        float scale = (float) Math.pow(10.0, places);
        return (value * scale) / scale;
    }
}
