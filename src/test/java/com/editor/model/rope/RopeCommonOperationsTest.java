package com.editor.model.rope;

import com.editor.system.SystemConstants;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RopeCommonOperationsTest {
    static final int NEW_LINE_LENGTH = SystemConstants.NEW_LINE.length();

    @Test
    public void testIncIndexIfNewLineSymbolSplit() {
        RopeCommonOperations ops = new RopeCommonOperations(4);
        assertEquals(
                1,
                ops.incIndexIfNewLineSymbolSplit("ab" + SystemConstants.NEW_LINE, 1));

        assertEquals(
                2 + NEW_LINE_LENGTH,
                ops.incIndexIfNewLineSymbolSplit("ab" + SystemConstants.NEW_LINE, 3));

        assertEquals(
                2,
                ops.incIndexIfNewLineSymbolSplit("ab" + SystemConstants.NEW_LINE, 2));

        assertEquals(
                2 + NEW_LINE_LENGTH,
                ops.incIndexIfNewLineSymbolSplit("Hey!" + SystemConstants.NEW_LINE + "?", 4));
    }

    @Test
    public void newLineShouldSplitStringToDifferentNodesOnCreate() {
        RopeCommonOperations ops = new RopeCommonOperations(4);
        Rope rope = ops.create("Hi!" + SystemConstants.NEW_LINE + "?");
        assertThat(rope.printRopeNodes(), CoreMatchers.containsString("(Hi!" + SystemConstants.NEW_LINE + ")"));
        assertThat(rope.printRopeNodes(), CoreMatchers.containsString("(?)"));
    }

    @Test
    public void createShouldConstructCorrectRopeStructure() {
        RopeCommonOperations ops = new RopeCommonOperations(4);
        Rope rope = ops.create("Hey_Mew_Cat_Albert");
        String expectedResult = "(18)(8)(10)(Hey_)(Mew_)(Cat_)(6)(Albe)(rt)";
        assertEquals(expectedResult, rope.printRopeNodes());
    }

    @Test
    public void createShouldNotChangeText() {
        RopeCommonOperations ops = new RopeCommonOperations(4);
        String text = "New Year is coming! Look at the snow behind your window. It is like a fairy tale.";
        Rope rope = ops.create(text);
        assertEquals(text, rope.toString());
    }

    @Test
    public void testConcatenate() throws Exception {
        RopeCommonOperations ops = new RopeCommonOperations(10);

        Rope heyYoYoDots = ops.concat(new Rope("Hey YoYo"), new Rope(".."));
        assertEquals("Hey YoYo..", heyYoYoDots.toString());

        Rope heyYoYoDotsSuf = ops.concat(heyYoYoDots, new Rope("!"));
        assertEquals("Hey YoYo..!", heyYoYoDotsSuf.toString());

        Rope heyYoYoDotsPref = ops.concat(new Rope("!"), heyYoYoDots);
        assertEquals("!Hey YoYo..", heyYoYoDotsPref.toString());

        Rope sufPlusPref = ops.concat(heyYoYoDotsSuf, heyYoYoDotsPref);
        assertEquals("Hey YoYo..!!Hey YoYo..", sufPlusPref.toString());

        Rope heyMyNameIsHeyMyNameIsHeyMyNameIsHeyMyNameIs = ops.concat(sufPlusPref, sufPlusPref);
        assertEquals("Hey YoYo..!!Hey YoYo..Hey YoYo..!!Hey YoYo..", heyMyNameIsHeyMyNameIsHeyMyNameIsHeyMyNameIs.toString());

        assertEquals(
                "Hey YoYo..Hey YoYo..!!Hey YoYo..Hey YoYo..!!Hey YoYo..",
                ops.concat(heyYoYoDots, heyMyNameIsHeyMyNameIsHeyMyNameIsHeyMyNameIs).toString()
        );
    }

    @Test
    public void testSplit() {
        RopeNode rope = new RopeNode("HoHoHoHoHoHe!");

        RopeCommonOperations ops = new RopeCommonOperations(3);

        List<Rope> splits = ops.split(new Rope(rope), 6);
        Rope leftSplit = splits.get(0);
        Rope rightSplit = splits.get(1);

        assertEquals("HoHoHo", leftSplit.toString());
        assertEquals("HoHoHe!", rightSplit.toString());

        // corner cases
        List<Rope> splitByZero = ops.split(new Rope(rope), 0);
        assertEquals(rope.toString(), splitByZero.get(1).toString());
        assertEquals("", splitByZero.get(0).toString());

        List<Rope> splitByLast = ops.split(new Rope(rope), rope.length);
        assertEquals("", splitByLast.get(1).toString());
        assertEquals(rope.toString(), splitByLast.get(0).toString());
    }
}