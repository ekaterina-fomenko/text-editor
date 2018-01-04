package com.editor.model.rope;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RopeCreatorTest {
    @Test
    public void createTest(){
        RopeUtilities.setRopeLengthAndDepth(4,2);
        Rope rope = RopeCreator.create("Hey_Mew_Cat_Albert");
        String expectedResult = "(18)(8)(10)(Hey_)(Mew_)(Cat_)(6)(Albe)(rt)";
        assertEquals(expectedResult, rope.printRopeNodes());
        RopeUtilities.resetToDefaultLengthAndDepth();
    }

    @After
    public void after(){
        RopeUtilities.resetToDefaultLengthAndDepth();
    }
}
