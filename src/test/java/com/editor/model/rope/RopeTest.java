package com.editor.model.rope;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.assertEquals;

public class RopeTest {

    @Test
    public void testGetLength() throws Exception {
        Rope rope = new Rope("abc");

        assertEquals(3, rope.getLength());

        rope = rope.append(new Rope("def"));

        assertEquals(6, rope.getLength());
    }

    @Ignore
    @Test
    public void testGetDepth() throws Exception {
        throw new NotImplementedException();
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

    @Ignore
    @Test
    public void testContainsOneLevelOnly() throws Exception {
        throw new NotImplementedException();
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

}