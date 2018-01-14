package com.editor.model.rope;

import com.editor.system.SystemConstants;
import org.junit.Test;

import java.text.MessageFormat;

import static java.text.MessageFormat.*;
import static org.junit.Assert.assertEquals;

public class RopeNodeTest {
    @Test
    public void testLinesNum() {
        assertEquals(1, new RopeNode().getLinesNum());
        assertEquals(1, new RopeNode("").getLinesNum());
        assertEquals(2, new RopeNode(SystemConstants.NEW_LINE).getLinesNum());
        assertEquals(2, new RopeNode(SystemConstants.NEW_LINE + "ABC").getLinesNum());
        assertEquals(3, new RopeNode(SystemConstants.NEW_LINE + "ABC" + SystemConstants.NEW_LINE).getLinesNum());
    }

    @Test
    public void linesNumShouldNotCountSpecialSymbolsInText() {
        assertEquals(1, new RopeNode("A\\n\\rB").getLinesNum());
        assertEquals(1, new RopeNode("A\\n\\nB").getLinesNum());
        assertEquals(1, new RopeNode("A/n/rB").getLinesNum());
    }
}