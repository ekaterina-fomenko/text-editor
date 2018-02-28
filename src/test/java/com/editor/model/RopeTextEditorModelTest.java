package com.editor.model;

import com.editor.model.buffer.VisibleLinesBufferBuilder;
import com.editor.model.buffer.VisibleLinesInfo;
import org.junit.Test;

import java.awt.*;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class RopeTextEditorModelTest {

    @Test
    public void testOnTextInput() {
        RopeTextEditorModel model = new RopeTextEditorModel();

        assertEquals(0, model.getCursorPosition());

        model.onTextInput(new char[]{'a', 'b'});
        assertEquals(2, model.getCursorPosition());

        model.onBackspace();
        assertEquals("a", model.getRope().toString());
        assertEquals(1, model.getCursorPosition());

        model.onBackspace();
        assertEquals("", model.getRope().toString());
        assertEquals(0, model.getCursorPosition());

        model.onBackspace();
        assertEquals(0, model.getCursorPosition());
    }

    @Test
    public void testOnEnter() {
        RopeTextEditorModel model = new RopeTextEditorModel();
        model.onTextInput(new char[]{'a', 'b'});

        model.onEnter();
        assertEquals("ab" + System.lineSeparator(), model.getRope().toString());
        assertEquals(2 + System.lineSeparator().length(), model.getCursorPosition());
    }

    @Test
    public void testSelectionWithoutCursor() {
        RopeTextEditorModel model = new RopeTextEditorModel();
        model.onTextInput("abcde".toCharArray());

        model.setCursorPosition(1);

        assertEquals("", model.getSelectedText());
    }

    @Test
    public void testSelectionBack() {
        RopeTextEditorModel model = new RopeTextEditorModel();
        model.onTextInput("abcde".toCharArray());

        model.setCursorPosition(1);
        model.setSelectionEnd(0);

        assertEquals("a", model.getSelectedText());

        model.onBackspace();
        assertEquals("bcde", model.getRope().toString());
    }

    @Test
    public void testSelectionForward() {
        RopeTextEditorModel model = new RopeTextEditorModel();
        model.onTextInput("abcde".toCharArray());

        model.setCursorPosition(0);
        model.setSelectionEnd(1);

        assertEquals("a", model.getSelectedText());

        model.onBackspace();
        assertEquals("bcde", model.getRope().toString());
    }

    @Test
    public void movePointerToEndPositions() {
        RopeTextEditorModel model = new RopeTextEditorModel();
        model.onTextInput("abc\ndef".toCharArray());

        model.setVisibleLinesInfo(
                new VisibleLinesBufferBuilder()
                        .addLine(new LineInfo(0, 3))
                        .addLine(new LineInfo(4, 3))
                        .withCursorChar('d')
                        .build()
        );

        model.setCursorPosition(5);

        model.movePointerToStartOfLine(true);
        assertEquals(4, model.getCursorPosition());

        model.movePointerToTheEndOfLine(true);
        assertEquals(7, model.getCursorPosition());

        model.movePointerToInitPosition(true);
        assertEquals(0, model.getCursorPosition());

        model.movePointerToLastPosition(true);
        assertEquals(7, model.getCursorPosition());
    }

    @Test
    public void testCursorRect() {
        RopeTextEditorModel model = new RopeTextEditorModel();
        model.append("aaaaaaaaaaaaaa".toCharArray());

        model.setCursorRect(new Rectangle(0, 0, 10, 10));

        model.moveCursorRectToY(10);
        assertEquals(new Rectangle(0, 10, 10, 10), model.getCursorRect());
    }

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
        assertEquals(3, model.getCursorPosition());

        // test last line
        model.setCursorPosition(15);
        model.movePointerDown(true);
        assertEquals(15, model.getCursorPosition());

        // test first line
        model.setCursorPosition(1);
        model.movePointerUp(true);
        assertEquals(1, model.getCursorPosition());
    }
}