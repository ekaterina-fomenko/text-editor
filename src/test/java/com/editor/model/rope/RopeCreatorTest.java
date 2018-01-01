package com.editor.model.rope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RopeCreatorTest {
    @Test
    public void createTest(){
        Rope.MAX_LENGTH_IN_ROPE = 4;
        Rope.MAX_DEPTH = 2;
       // Rope rope = RopeCreator.create("New Year is coming! Look at the snow behind your window. It is like a fairy tale.");
        Rope rope = RopeCreator.create("Hey_Mew_Cat_Albert");
        String expectedResult = "(18)(8)(10)(Hey_)(Mew_)(Cat_)(6)(Albe)(rt)";
        assertEquals(expectedResult, rope.printRopeNodes());
    }
}
