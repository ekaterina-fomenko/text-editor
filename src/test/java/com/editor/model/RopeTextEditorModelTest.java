package com.editor.model;

import com.editor.model.buffer.VisibleLinesInfo;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class RopeTextEditorModelTest {

    @Test
    public void movePointerUp() throws Exception {
        RopeTextEditorModel model = new RopeTextEditorModel();
        model.append("aaaaaaaaaaaaaaaaaaaaaaaaaa".toCharArray());
        model.setVisibleLinesInfo(
                new VisibleLinesInfo(
                        newArrayList(
                                new LineInfo(0, 3), // 0..2
                                new LineInfo(4, 5), // 4..8
                                new LineInfo(10, 1), // 10..10
                                new LineInfo(12, 100)), // 12..111
                        'a')
        );

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
        assertEquals(2, model.getCursorPosition());
    }
}