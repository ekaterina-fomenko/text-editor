package com.editor.model.rope;

import org.junit.Test;

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
}