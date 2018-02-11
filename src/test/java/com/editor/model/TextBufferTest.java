package com.editor.model;

import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class TextBufferTest {

    @Test
    public void getLine() throws Exception {
        assertEquals(0, new TextBuffer(newArrayList(new LineInfo(0, 2)), 'a').getLineByCharIndex(0));
        assertEquals(0, new TextBuffer(newArrayList(new LineInfo(0, 2)), 'a').getLineByCharIndex(1));
        assertEquals(0, new TextBuffer(newArrayList(new LineInfo(0, 2), new LineInfo(3, 2)), 'a').getLineByCharIndex(2));

        assertEquals(1,
                new TextBuffer(newArrayList(
                        new LineInfo(0, 2),
                        new LineInfo(3, 10)
                ), 'a').getLineByCharIndex(4));

//        assertEquals(-1, new LinesBuffer(newArrayList(new LineInfo(0, 10))).getLineByCharIndex(10));
//        assertEquals(-1, new LinesBuffer(newArrayList(new LineInfo(0, 2), new LineInfo(3, 2))).getLineByCharIndex(-1));
    }
}