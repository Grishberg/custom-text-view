package com.grishberg.textcarddimen;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TextBoundsTest {
    private static final int CARD_WIDTH = 80;

    private float scale = 1f;
    private TextBounds underTest = new TextBounds(new LoggerStub());
    private FontDimensions fontDimensions = createFontDimension();

    @Test
    public void wordWrap() {
        FontSize size = underTest.calculateTextSize("AAA " + "ABCDEF A" + "AAABCDEF", fontDimensions, scale, CARD_WIDTH);
        assertEquals(new FontSize(80, 60), size);
    }

    @Test
    public void wordWrap2() {
        FontSize size = underTest.calculateTextSize("ABC ABCDEF", fontDimensions, scale, CARD_WIDTH);
        assertEquals(new FontSize(60, 40), size);
    }

    @Test
    public void wordWrap3() {
        FontSize size = underTest.calculateTextSize("ABCABCDE" + "FA", fontDimensions, scale, CARD_WIDTH);
        assertEquals(new FontSize(20, 40), size);
    }

    @Test
    public void wordWrap4() {
        FontSize size = underTest.calculateTextSize("AAAAA BB" + "CCCCCCC", fontDimensions, scale, CARD_WIDTH);
        assertEquals(new FontSize(70, 40), size);
    }

    @Test
    public void wordWrap5() {
        FontSize size = underTest.calculateTextSize("AAAAA BB" + "CCCCCCC " + "DD", fontDimensions, scale, CARD_WIDTH);
        assertEquals(new FontSize(20, 60), size);
    }

    @Test
    public void singleLineTest() {
        FontSize size = underTest.calculateTextSize("! - A B ", fontDimensions, scale, CARD_WIDTH);
        assertEquals(new FontSize(80, 20), size);
    }

    @Test
    public void wordWrap6() {
        FontSize size = underTest.calculateTextSize("AAAAA " + "BBBBBBBB"+"! ! !", fontDimensions, scale, CARD_WIDTH);
        assertEquals(new FontSize(10, 60), size);
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