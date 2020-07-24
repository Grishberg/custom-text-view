package com.grishberg.textcarddimen;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TextLinesTest {
    private TextLines underTest = new TextLines(createFontDimension());

    private static final int CARD_WIDTH = 80;

    private List<TextLines.TextLine> result = new ArrayList<>();

    @Test
    public void dontWrapWordWhenSingleLine() {
        List<TextLines.TextLine> lines = underTest.calculateTextLines("ABC DEF", CARD_WIDTH);
        result.add(new TextLines.TextLine("ABC DEF", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap() {
        List<TextLines.TextLine> lines = underTest.calculateTextLines("AAA " + "ABCDEF A" + "AAABCDEF", CARD_WIDTH);
        result.add(new TextLines.TextLine("AAA", 0f, 0f));
        result.add(new TextLines.TextLine("ABCDEF A", 0f, 0f));
        result.add(new TextLines.TextLine("AAABCDEF", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap2() {
        List<TextLines.TextLine> lines = underTest.calculateTextLines("ABC ABCDEF", CARD_WIDTH);
        result.add(new TextLines.TextLine("ABC ABCDEF", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap3() {
        List<TextLines.TextLine> lines = underTest.calculateTextLines("ABCABCDE" + "FA", CARD_WIDTH);
        result.add(new TextLines.TextLine("ABCABCDE", 0f, 0f));
        result.add(new TextLines.TextLine("FA", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap4() {
        List<TextLines.TextLine> lines = underTest.calculateTextLines("AAAAA BB" + "CCCCCCC", CARD_WIDTH);
        result.add(new TextLines.TextLine("AAAAA BB", 0f, 0f));
        result.add(new TextLines.TextLine("CCCCCCC", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap5() {
        List<TextLines.TextLine> lines = underTest.calculateTextLines("AAAAA BB" + "CCCCCCC " + "DD", CARD_WIDTH);
        result.add(new TextLines.TextLine("AAAAA BB", 0f, 0f));
        result.add(new TextLines.TextLine("CCCCCCC", 0f, 0f));
        result.add(new TextLines.TextLine("DD", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void singleLineTest() {
        List<TextLines.TextLine> lines = underTest.calculateTextLines("! - A B ", CARD_WIDTH);
        result.add(new TextLines.TextLine("! - A B", 0f, 0f));
        checkLines(result, lines);
    }

    @Test
    public void wordWrap6() {
        List<TextLines.TextLine> lines = underTest.calculateTextLines("AAAAA " + "BBBBBBBB" + "! ! !", CARD_WIDTH);
        result.add(new TextLines.TextLine("AAAAA", 0f, 0f));
        result.add(new TextLines.TextLine("BBBBBBBB", 0f, 0f));
        result.add(new TextLines.TextLine("! ! !", 0f, 0f));
        checkLines(result, lines);
    }

    private void checkLines(
            List<TextLines.TextLine> expected,
            List<TextLines.TextLine> actual) {
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