package com.editor.model.rope;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RopeCommonOperationsTest {

    @Test
    public void testConcatenate() throws Exception {
        RopeCommonOperations ops = new RopeCommonOperations(3, 10);

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

        RopeCommonOperations ops = new RopeCommonOperations(3, 3);

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