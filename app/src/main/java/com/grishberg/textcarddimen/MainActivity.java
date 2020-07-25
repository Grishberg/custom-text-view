package com.grishberg.textcarddimen;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {
    private static final int REQUEST_PERMISSION = 1234;

    private static final String FONT = "fonts/NewCMSans10-Regular.otf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !hasPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSION);
            return;
        }

        doWork();
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                doWork();
            } else {
                // User refused to grant permission.
            }
        }
    }

    private void doWork() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), FONT);
        TextView textView = findViewById(R.id.textView);
        textView.setTypeface(typeface);

        TextView scaledTextView = findViewById(R.id.scaledTextView);
        scaledTextView.setTypeface(typeface);

        FontDimensions fd = getTextMetrics(getString(R.string.abc), textView);
        //FontDimensions fd = new FontDimensions(getAssets().open("fontdimension.fdb"));
        fd.save(new File(Environment.getExternalStorageDirectory(), "Download/fontdimension.fdb"));

        CustomTextView cardText = findViewById(R.id.cardText);
        float textSize = getResources().getDimension(R.dimen.cardTextSizeInSp);
        fd.setCurrentTextSize(textSize);

        cardText.setFontDimensions(fd);
        cardText.setTypeface(typeface);
        cardText.setTextSize(textSize);
        cardText.setText(getString(R.string.sample));

        String test = getString(R.string.sample0);
        int targetWidth = getResources().getDimensionPixelSize(R.dimen.cardWidth);
        float scale = android.provider.Settings.System.getFloat(getContentResolver(),
                android.provider.Settings.System.FONT_SCALE, 1f);

        float fontScale = getResources().getConfiguration().fontScale;

        //cardText.setText(test);

        ViewGroup content = findViewById(R.id.mainFrameLayout);
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
        return new FontDimensions(charMap,
                fm.top, fm.ascent, fm.descent, fm.bottom,
                paint.getTextSize());
    }

    private float roundAvoid(float value, int places) {
        float scale = (float) Math.pow(10.0, places);
        return (value * scale) / scale;
    }
}
