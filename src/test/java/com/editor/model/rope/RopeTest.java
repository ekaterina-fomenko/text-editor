package com.editor.model.rope;

import com.editor.RandomTextBuilder;
import com.editor.system.SystemConstants;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import java.util.Iterator;

import static com.editor.system.SystemConstants.*;
import static java.lang.String.valueOf;
import static java.text.MessageFormat.format;
import static org.junit.Assert.assertEquals;

public class RopeTest {

    @Test
    public void testGetLength() throws Exception {
        Rope rope = new Rope("abc");

        assertEquals(3, rope.getLength());

        rope = rope.append(new Rope("def"));

        assertEquals(6, rope.getLength());
    }

    @Test
    public void testAppend() throws Exception {
        Rope rope = new Rope("abc");
        rope = rope.append(new Rope("cdef\n"));
        rope = rope.append(new Rope("gheeey"));

        assertEquals("abccdef\ngheeey", rope.toString());
    }

    @Test
    public void testAppendDeep() throws Exception {
        assertEquals("Hey! My name is Katya", (new Rope("Hey! ").append(new Rope("My "))).append(new Rope("name ").append("is").append(" Katya")).toString());
    }

    @Test
    public void testCharAtWithAppend() throws Exception {
        Rope rope = new Rope("abc");
        rope = rope.append(new Rope("def"));

        assertEquals('a', rope.charAt(0));
        assertEquals('b', rope.charAt(1));
        assertEquals('c', rope.charAt(2));

        assertEquals('d', rope.charAt(3));
        assertEquals('e', rope.charAt(4));
        assertEquals('f', rope.charAt(5));
    }

    @Test
    public void testCharAtDepth2() {
        RopeNode llChild = new RopeNode("ABC");
        RopeNode lrChild = new RopeNode("DEF");
        RopeNode lChild = new RopeNode(llChild, lrChild);
        RopeNode rlChild = new RopeNode("GH");
        RopeNode rrChild = new RopeNode("EK");
        RopeNode rChild = new RopeNode(rlChild, rrChild);
        RopeNode root = new RopeNode(lChild, rChild);
        Rope rope = new Rope(root);

        String text = "ABCDEFGHEK";
        for (int i = 0; i < text.length(); i++) {
            assertEquals(valueOf(text.charAt(i)), valueOf(rope.charAt(i)));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCharAt_negative() throws Exception {
        new Rope("abc").charAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCharAt_negative2() throws Exception {
        new Rope("abc").charAt(3);
    }

    @Test
    public void subStringTestWithOneNode() throws Exception {
        Rope rope = new Rope("HoHoHo! I am Santa!");
        rope = rope.substring(0, 7);
        assertEquals("HoHoHo!", rope.toString());
    }

    @Test
    public void subStringTest() throws Exception {
        RopeNode left = new RopeNode("HoHoHo!");
        RopeNode right = new RopeNode("Who is looking for Santa?");
        RopeNode node = new RopeNode(left, right);
        Rope rope = new Rope(node);
        rope.operations = new RopeCommonOperations(7);

        assertEquals("HoHoHo!", rope.substring(0, 7).toString());
        assertEquals("Who ", rope.substring(7, 11).toString());
        assertEquals(right.getValue().substring(4, 6), rope.substring(11, 13).toString());
        assertEquals("Ho!W", rope.substring(4, 8).toString());
    }

    @Test
    public void iteratorTest() {
        String value = "Ho_ho_ho!_New_year_is_not_finished;)";
        Rope rope = new RopeCommonOperations(4).create(value);
        Iterator<Character> iterator = rope.iterator(10);
        String expectedResult = value.substring(10);
        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            result.append(iterator.next());
        }
        assertEquals(expectedResult, result.toString());
    }

    @Test
    public void shouldBeAbleToIteraterThroughSingleNodeRope() {
        Rope rope = new RopeCommonOperations(10).create("ABC");
        Iterator<Character> iterator = rope.iterator(0);
        assertEquals('A', (char) iterator.next());
        assertEquals('B', (char) iterator.next());
        assertEquals('C', (char) iterator.next());
        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void testLinesNumber() {
        Rope rope = new Rope("1st_line" + NEW_LINE + "2nd_line");
        assertEquals(2, rope.getLinesNum());

        assertEquals(2, rope.substring(3, rope.toString().length()).getLinesNum());
        assertEquals(1, rope.substring(11, rope.toString().length()).getLinesNum());

        rope = rope.append("3rd_line" + NEW_LINE + "!");
        assertEquals(3, rope.getLinesNum());
        assertEquals(4, rope.append(NEW_LINE).getLinesNum());
    }

    @Test
    public void testLinesNumberBigSize() {
        RandomTextBuilder randomTextBuilder = new RandomTextBuilder();
        Rope rope = new Rope(randomTextBuilder
                .addLetters(100)
                .addEnters(10)
                .addLetters(100)
                .getText());

        assertEquals(11, rope.getLinesNum());

        rope = rope.append(randomTextBuilder.getText());
        assertEquals(21, rope.getLinesNum());

        rope = rope.substring(rope.toString().length() - 10, rope.toString().length());
        assertEquals(1, rope.getLinesNum());
    }

    @Test
    public void findCharIndexOfLine_depth2() {
        RopeNode lChild = new RopeNode(format("A{0}B{1}C", NEW_LINE, NEW_LINE));
        RopeNode rlChild = new RopeNode(format("D{1}E", NEW_LINE, NEW_LINE));
        RopeNode rrChild = new RopeNode(format("F{1}G", NEW_LINE, NEW_LINE));
        RopeNode rChild = new RopeNode(rlChild, rrChild);
        RopeNode root = new RopeNode(lChild, rChild);
        Rope rope = new Rope(root);

        assertEquals("A", valueOf(rope.charAt(rope.charIndexOfLineStart(0))));
        assertEquals("B", valueOf(rope.charAt(rope.charIndexOfLineStart(1))));
        assertEquals("C", valueOf(rope.charAt(rope.charIndexOfLineStart(2))));
        assertEquals("E", valueOf(rope.charAt(rope.charIndexOfLineStart(3))));
        assertEquals("G", valueOf(rope.charAt(rope.charIndexOfLineStart(4))));
        assertEquals(-1, rope.charIndexOfLineStart(5));
    }

    @Test
    public void findCharIndexOfLineWithEntersInCorners() {
        RopeNode llChild = new RopeNode(format("{0}B{1}", NEW_LINE, NEW_LINE));
        RopeNode lrChild = new RopeNode(format("{0}", NEW_LINE));
        RopeNode rlChild = new RopeNode(format("D{0}E", NEW_LINE));
        RopeNode rrChild = new RopeNode(format("{0}G", NEW_LINE));
        RopeNode root = new RopeNode(new RopeNode(llChild, lrChild), new RopeNode(rlChild, rrChild));
        Rope rope = new Rope(root);
        /*
0
1        B
2
3        D
4        E
5        G
         */
        assertEquals(SystemConstants.NEW_LINE.substring(0, 1), valueOf(rope.charAt(rope.charIndexOfLineStart(0))));
        assertEquals("B", valueOf(rope.charAt(rope.charIndexOfLineStart(1))));
        assertEquals(SystemConstants.NEW_LINE.substring(0, 1), valueOf(rope.charAt(rope.charIndexOfLineStart(2))));
        assertEquals("D", valueOf(rope.charAt(rope.charIndexOfLineStart(3))));
        assertEquals("E", valueOf(rope.charAt(rope.charIndexOfLineStart(4))));
        assertEquals("G", valueOf(rope.charAt(rope.charIndexOfLineStart(5))));
        assertEquals(-1, rope.charIndexOfLineStart(6));
    }
}