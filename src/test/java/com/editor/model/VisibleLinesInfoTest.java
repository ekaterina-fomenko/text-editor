package com.editor.model;

import com.editor.model.buffer.VisibleLinesInfo;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class VisibleLinesInfoTest {

    @Test
    public void getLine() throws Exception {
        assertEquals(-1, new VisibleLinesInfo(newArrayList(new LineInfo(0, 2)), 'a').getLineByCharIndex(3));
        assertEquals(0, new VisibleLinesInfo(newArrayList(new LineInfo(0, 2)), 'a').getLineByCharIndex(0));
        assertEquals(0, new VisibleLinesInfo(newArrayList(new LineInfo(0, 2)), 'a').getLineByCharIndex(1));
        assertEquals(0, new VisibleLinesInfo(newArrayList(new LineInfo(0, 2), new LineInfo(3, 2)), 'a').getLineByCharIndex(2));

        assertEquals(1,
                new VisibleLinesInfo(newArrayList(
                        new LineInfo(0, 2),
                        new LineInfo(3, 10)
                ), 'a').getLineByCharIndex(4));
    }
}