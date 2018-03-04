package com.editor;

import com.editor.model.LineInfo;
import com.editor.model.RopeTextEditorModel;
import com.editor.model.buffer.VisibleLinesInfo;
import com.editor.model.undo.UndoRedoService;
import com.editor.system.ClipboardAdapter;
import com.editor.system.ClipboardAdapterImpl;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

public class TextActionMapTest {
    private RopeTextEditorModel model;
    private UndoRedoService undoRedoService;
    private TextActionMap textActionMap;
    private ClipboardAdapter clipboardAdapter;

    @Before
    public void before() {
        model = new RopeTextEditorModel();
        undoRedoService = Mockito.mock(UndoRedoService.class);
        clipboardAdapter = Mockito.mock(ClipboardAdapter.class);

        textActionMap = new TextActionMap(model,
                new TextArea(new JFrame(), new EditorSettings()),
                undoRedoService,
                clipboardAdapter
        );

    }

    @Test
    public void testChar() {
        invokeAction("a", "a");

        assertEquals("a", model.getRope().toString());
        verifyStatePushedForUndo();
    }

    @Test
    public void testDelete() {
        model.append("ABC".toCharArray());
        model.setCursorPosition(1);

        invokeAction(TextInputMap.DELETE, null);

        assertEquals("BC", model.getRope().toString());
        verifyStatePushedForUndo();
    }

    @Test
    public void testNewLine() {
        model.append("ABC".toCharArray());
        model.setCursorPosition(3);

        invokeAction(TextInputMap.NEW_LINE, null);

        assertEquals("ABC" + System.lineSeparator(), model.getRope().toString());
        verifyStatePushedForUndo();
    }

    @Test
    public void testHorNavigation() {
        model.append("ABC".toCharArray());
        model.setCursorPosition(0);

        invokeAction(TextInputMap.RIGHT, null);
        invokeAction(TextInputMap.RIGHT, null);
        invokeAction(TextInputMap.LEFT, null);

        assertEquals(1, model.getCursorPosition());
    }

    @Test
    public void testVertNavigation() {
        model.append("ABC\nABC".toCharArray());
        model.setCursorPosition(0);

        model.setVisibleLinesInfo(new VisibleLinesInfo(Lists.newArrayList(
                new LineInfo(0, 3),
                new LineInfo(4, 3)
        ), 'A'));

        invokeAction(TextInputMap.RIGHT_SHIFT, null);
        invokeAction(TextInputMap.RIGHT_SHIFT, null);
        invokeAction(TextInputMap.DOWN_SHIFT, null);
        invokeAction(TextInputMap.UP_SHIFT, null);
        invokeAction(TextInputMap.DOWN_SHIFT, null);

        assertEquals("ABC\nAB", model.getSelectedText());
    }

    @Test
    public void testHorShiftNavigation() {
        model.append("ABC".toCharArray());
        model.setCursorPosition(0);

        invokeAction(TextInputMap.RIGHT_SHIFT, null);
        invokeAction(TextInputMap.RIGHT_SHIFT, null);

        assertEquals("AB", model.getSelectedText());
    }

    @Test
    public void testCliboard() {
        Mockito.when(clipboardAdapter.getText()).thenReturn(Optional.of("ABC"));

        invokeAction(TextInputMap.CTRL_V, null);

        assertEquals("ABC", model.getRope().toString());
        assertEquals(3, model.getCursorPosition());

        invokeAction(TextInputMap.LEFT_SHIFT, null);
        invokeAction(TextInputMap.LEFT_SHIFT, null);
        invokeAction(TextInputMap.LEFT_SHIFT, null);
        invokeAction(TextInputMap.CTRL_C, null);

        Mockito.verify(clipboardAdapter, times(1)).setText(eq("ABC"));
    }

    private void invokeAction(String actionKey, String command) {
        textActionMap.get(actionKey).actionPerformed(new ActionEvent(this, 1, command));
    }

    private void verifyStatePushedForUndo() {
        Mockito.verify(undoRedoService, times(1)).pushState();
    }

}