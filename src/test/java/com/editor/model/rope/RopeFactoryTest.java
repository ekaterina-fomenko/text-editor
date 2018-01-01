package com.editor.model.rope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RopeFactoryTest {
    @Test
    public void createShouldConstructCorrectRopeStructure(){
        Rope.MAX_LENGTH_IN_ROPE = 4;
        Rope.MAX_DEPTH = 2;
        Rope rope = RopeFactory.create("Hey_Mew_Cat_Albert");
        String expectedResult = "(18)(8)(10)(Hey_)(Mew_)(Cat_)";
        assertEquals(expectedResult, rope.printRopeNodes());
    }

    @Test
    public void createShouldNotChangeText(){
        Rope.MAX_LENGTH_IN_ROPE = 4;
        Rope.MAX_DEPTH = 2;
        String text = "New Year is coming! Look at the snow behind your window. It is like a fairy tale.";
        Rope rope = RopeFactory.create(text);
        assertEquals(text, rope.toString());
    }
}
