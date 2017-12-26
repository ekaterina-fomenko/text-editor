package com.editor.model.rope;

import org.junit.Test;

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

        assertEquals('a', rope.charAt(0));
        assertEquals('b', rope.charAt(1));
        assertEquals('c', rope.charAt(2));

        rope = rope.append(new Rope("def"));
        assertEquals('b', rope.charAt(1));
        assertEquals('d', rope.charAt(3));
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

//    @Test
//    public void subsequenceTestWithOneNode() throws Exception {
//        Rope rope = new Rope("HoHoHo! I am Santa!");
//        rope = rope.subSequence(0, 7);
//        assertEquals("HoHoHo!", rope.toString());
//    }
//
//    @Test
//    public void subsequenceTest() throws Exception {
//        RopeNode left = new RopeNode("HoHoHo!");
//        RopeNode right = new RopeNode("Who is looking for Santa?");
//        RopeNode node = new RopeNode(left, right);
//        Rope rope = new Rope(node);
//        rope.setOperations(new RopeCommonOperations(5,7));
//
//        //assertEquals("HoHoHo!", rope.subSequence(0, 7).toString());
//        assertEquals("Who", rope.subSequence(8,11).toString());
//    }

}