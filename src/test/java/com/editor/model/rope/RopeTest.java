package com.editor.model.rope;

import com.editor.RandomTextBuilder;
import com.editor.system.SystemConstants;
import org.junit.Test;

import java.util.Iterator;

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
    public void testGetDepth() throws Exception {
        assertEquals(0, new Rope("abc").getDepth());

        RopeCommonOperations ops = new RopeCommonOperations(3, 5);
        assertEquals(0, ops.concat(new Rope("abc"), new Rope("a")).getDepth());

        assertEquals(1, ops.concat(new Rope("abc"), new Rope("de")).getDepth());

        assertEquals(1, new Rope(new RopeNode(new RopeNode("a"), new RopeNode("b"))).getDepth());
    }

    @Test
    public void testAppend() throws Exception {
        Rope rope = new Rope("abc");
        rope = rope.append(new Rope("cdef\n"));
        rope = rope.append(new Rope("gheeey"));

        assertEquals("abccdef\ngheeey", rope.toString());
    }

    @Test
    public void testAppend_deep() throws Exception {
        assertEquals("Hey! My name is Katya", (new Rope("Hey! ").append(new Rope("My "))).append(new Rope("name ").append("is").append(" Katya")).toString());
    }

    @Test
    public void testCharAt() throws Exception {
        Rope rope = new Rope("abc");
        rope = rope.append(new Rope("def"));

        assertEquals('a', rope.charAt(0));
        assertEquals('b', rope.charAt(1));
        assertEquals('c', rope.charAt(2));

        assertEquals('d', rope.charAt(3));
        assertEquals('e', rope.charAt(4));
        assertEquals('f', rope.charAt(5));

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
        rope.operations = new RopeCommonOperations(5, 7);

        assertEquals("HoHoHo!", rope.substring(0, 7).toString());
        assertEquals("Who ", rope.substring(7, 11).toString());
        assertEquals(right.getValue().substring(4, 6), rope.substring(11, 13).toString());
        assertEquals("Ho!W", rope.substring(4, 8).toString());
    }

    @Test
    public void iteratorTest() {
        String value = "Ho_ho_ho!_New_year_is_not_finished;)";
        Rope rope = new RopeCommonOperations(2, 4).create(value);
        Iterator<Character> iterator = rope.iterator(10);
        String expectedResult = value.substring(10);
        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            result.append(iterator.next());
        }
        assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testLinesNumber() {
        Rope rope = new Rope("1st_line" + SystemConstants.NEW_LINE + "2nd_line");
        assertEquals(2, rope.getLinesNum());

        assertEquals(2, rope.substring(3, rope.toString().length()).getLinesNum());
        assertEquals(1, rope.substring(11, rope.toString().length()).getLinesNum());

        rope = rope.append("3rd_line" + SystemConstants.NEW_LINE + "!");
        assertEquals(3, rope.getLinesNum());
        assertEquals(4, rope.append(SystemConstants.NEW_LINE).getLinesNum());
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
}