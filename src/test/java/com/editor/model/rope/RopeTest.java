package com.editor.model.rope;

import com.editor.RandomTextBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static java.lang.String.valueOf;
import static java.text.MessageFormat.format;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RopeTest {

    @Before
    public void before() {
        RopeNode.setSizeProvider(new CountingStringSizeProvider());
    }

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
        assertEquals(new String(right.getValue()).substring(4, 6), rope.substring(11, 13).toString());
        assertEquals("Ho!W", rope.substring(4, 8).toString());
    }

    @Test
    public void testLinesNumber() {
        Rope rope = new Rope("1st_line\n2nd_line");
        assertEquals(2, rope.getLinesNum());

        assertEquals(2, rope.substring(3, rope.toString().length()).getLinesNum());
        assertEquals(1, rope.substring(11, rope.toString().length()).getLinesNum());

        rope = rope.append("3rd_line\n!");
        assertEquals(3, rope.getLinesNum());
        assertEquals(4, rope.append("\n").getLinesNum());
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
        RopeNode lChild = new RopeNode("A\nB\nC");
        RopeNode rlChild = new RopeNode("D\nE");
        RopeNode rrChild = new RopeNode("F\nG");
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
        RopeNode llChild = new RopeNode("\nB\n");
        RopeNode lrChild = new RopeNode("\n");
        RopeNode rlChild = new RopeNode("D\nE");
        RopeNode rrChild = new RopeNode("\nG");
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
        assertEquals("\n".substring(0, 1), valueOf(rope.charAt(rope.charIndexOfLineStart(0))));
        assertEquals("B", valueOf(rope.charAt(rope.charIndexOfLineStart(1))));
        assertEquals("\n".substring(0, 1), valueOf(rope.charAt(rope.charIndexOfLineStart(2))));
        assertEquals("D", valueOf(rope.charAt(rope.charIndexOfLineStart(3))));
        assertEquals("E", valueOf(rope.charAt(rope.charIndexOfLineStart(4))));
        assertEquals("G", valueOf(rope.charAt(rope.charIndexOfLineStart(5))));
        assertEquals(-1, rope.charIndexOfLineStart(6));
    }

    @Test
    public void testMultiLineMaxLineLen() {
        String secondLine = "+ Project technologies onboarding was very quick. Overall further development performan";
        String text = "3) Overall\n" +
                secondLine;

        assertEquals(secondLine.length(), new Rope(text).getNode().getMaxLineLength());
    }

    @Test
    public void testInsert() {
        assertEquals("AA2BCD", new Rope("ABCD").insert(1, new Rope("A2")).toString());
        assertEquals("123ABCD", new Rope("ABCD").insert(0, new Rope("123")).toString());
        assertEquals("ABCDE", new Rope("ABCD").insert(4, new Rope("E")).toString());
        assertEquals("ABCD", new Rope("ABCD").insert(3, new Rope("")).toString());
        assertEquals("ABCD", new Rope("ABC").insert(3, new Rope("D")).toString());
    }

    @Test
    public void testToChars() {
        String value = "Ho_ho_ho!_New_year_is_not_finished;)";
        Rope rope = new RopeCommonOperations(4).create(value);
        assertArrayEquals(value.toCharArray(), rope.toChars());
    }
}