package com.editor.model;

import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class RopeTextEditorModelTest {

    @Test
    public void movePointerUp() throws Exception {
        RopeTextEditorModel model = new RopeTextEditorModel();
        model.append("aaaaaaaaaaaaaaaaaaaaaaaaaa".toCharArray());
        /*
        0:3/aaa
        4:5/aaaaa
        11:1/a
        ...
         */
        model.setLinesBuffer(new LinesBuffer(newArrayList(
                new LineInfo(0, 3),
                new LineInfo(4, 5),
                new LineInfo(10, 1),
                new LineInfo(12, 100))));

        // When
        model.movePointerDown(true);
        assertEquals(4, model.getCursorPosition());

        model.movePointerUp(true);
        assertEquals(0, model.getCursorPosition());

        model.movePointerRight(true);
        assertEquals(1, model.getCursorPosition());

        model.movePointerRight(true);
        assertEquals(2, model.getCursorPosition());

        model.movePointerDown(true);
        assertEquals(6, model.getCursorPosition());

        model.movePointerUp(true);
        assertEquals(2, model.getCursorPosition());

        model.setCursorPosition(8);
        model.movePointerDown(true);
        assertEquals(11, model.getCursorPosition());

        model.setCursorPosition(8);
        model.movePointerUp(true);
        assertEquals(3, model.getCursorPosition());
    }
}