package com.editor.model;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.*;
import static org.junit.Assert.*;

public class LinesBufferTest {

    @Test
    public void getLine() throws Exception {
        assertEquals(0, new LinesBuffer(newArrayList(new LineInfo(0, 2))).getLine(0));
        assertEquals(0, new LinesBuffer(newArrayList(new LineInfo(0, 2))).getLine(1));
        assertEquals(0, new LinesBuffer(newArrayList(new LineInfo(0, 2), new LineInfo(3, 2))).getLine(2));

        assertEquals(1,
                new LinesBuffer(newArrayList(
                        new LineInfo(0, 2),
                        new LineInfo(3, 10)
                )).getLine(4));

//        assertEquals(-1, new LinesBuffer(newArrayList(new LineInfo(0, 10))).getLine(10));
//        assertEquals(-1, new LinesBuffer(newArrayList(new LineInfo(0, 2), new LineInfo(3, 2))).getLine(-1));
    }
}