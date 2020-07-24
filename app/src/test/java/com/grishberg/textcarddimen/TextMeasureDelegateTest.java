package com.grishberg.textcarddimen;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TextMeasureDelegateTest {
    private static final int CARD_WIDTH = 80;

    private float scale = 1f;
    private TextMeasureDelegate underTest = new TextMeasureDelegate(new LoggerStub());
    private FontDimensions fontDimensions = createFontDimension();
    private List<TextMeasureDelegate.TextLine> result = new ArrayList<>();

    @Test
    public void wordWrap() {
        List<TextMeasureDelegate.TextLine> lines = underTest.calculateTextLines("AAA " + "ABCDEF A" + "AAABCDEF", fontDimensions, scale, CARD_WIDTH);
        result.add(new TextMeasureDelegate.TextLine("AAA", 0f, 0f));
        result.add(new TextMeasureDelegate.TextLine("ABCDEF A", 0f, 0f));
        result.add(new TextMeasureDelegate.TextLine("AAABCDEF", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap2() {
        List<TextMeasureDelegate.TextLine> lines = underTest.calculateTextLines("ABC ABCDEF", fontDimensions, scale, CARD_WIDTH);
        result.add(new TextMeasureDelegate.TextLine("ABC ABCDEF", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap3() {
        List<TextMeasureDelegate.TextLine> lines = underTest.calculateTextLines("ABCABCDE" + "FA", fontDimensions, scale, CARD_WIDTH);
        result.add(new TextMeasureDelegate.TextLine("ABCABCDE", 0f, 0f));
        result.add(new TextMeasureDelegate.TextLine("FA", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap4() {
        List<TextMeasureDelegate.TextLine> lines = underTest.calculateTextLines("AAAAA BB" + "CCCCCCC", fontDimensions, scale, CARD_WIDTH);
        result.add(new TextMeasureDelegate.TextLine("AAAAA BB", 0f, 0f));
        result.add(new TextMeasureDelegate.TextLine("CCCCCCC", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap5() {
        List<TextMeasureDelegate.TextLine> lines = underTest.calculateTextLines("AAAAA BB" + "CCCCCCC " + "DD", fontDimensions, scale, CARD_WIDTH);
        result.add(new TextMeasureDelegate.TextLine("AAAAA BB", 0f, 0f));
        result.add(new TextMeasureDelegate.TextLine("CCCCCCC", 0f, 0f));
        result.add(new TextMeasureDelegate.TextLine("DD", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void singleLineTest() {
        List<TextMeasureDelegate.TextLine> lines = underTest.calculateTextLines("! - A B ", fontDimensions, scale, CARD_WIDTH);
        result.add(new TextMeasureDelegate.TextLine("! - A B", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap6() {
        List<TextMeasureDelegate.TextLine> lines = underTest.calculateTextLines("AAAAA " + "BBBBBBBB" + "! ! !", fontDimensions, scale, CARD_WIDTH);
        result.add(new TextMeasureDelegate.TextLine("AAAAA", 0f, 0f));
        result.add(new TextMeasureDelegate.TextLine("BBBBBBBB", 0f, 0f));
        result.add(new TextMeasureDelegate.TextLine("! ! !", 0f, 0f));
        checkLines(result, lines);
    }

    private void checkLines(
            List<TextMeasureDelegate.TextLine> expected,
            List<TextMeasureDelegate.TextLine> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getText(), actual.get(i).getText());
        }
    }

    private FontDimensions createFontDimension() {
        HashMap<Character, Float> map = new HashMap<>();
        map.put('A', 10f);
        map.put('B', 10f);
        map.put('C', 10f);
        map.put('D', 10f);
        map.put('E', 10f);
        map.put('F', 10f);
        map.put(' ', 10f);
        map.put('!', 10f);
        map.put('-', 10f);
        return new FontDimensions(0f, 20f, map);
    }
}