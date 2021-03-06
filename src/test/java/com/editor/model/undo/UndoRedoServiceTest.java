package com.editor.model.undo;

import com.editor.model.RopeTextEditorModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UndoRedoServiceTest {

    private UndoRedoService undoRedoService;
    private RopeTextEditorModel ropeTextEditorModel;

    private static final String TEXT_1 = "Hello it is Katya!";
    private static final String TEXT_2 = "What is the amazing snow behind the window.";

    @Test
    public void testUndoOnBackspace() {
        ropeTextEditorModel = new RopeTextEditorModel();
        ropeTextEditorModel.onTextInput(TEXT_1.toCharArray());
        undoRedoService = new UndoRedoService(ropeTextEditorModel);
        ropeTextEditorModel.onBackspace();
        undoRedoService.pushState();

        assertEquals(TEXT_1.substring(0, TEXT_1.length() - 1), ropeTextEditorModel.getRope().toString());

        undoRedoService.undo();

        assertEquals(TEXT_1, ropeTextEditorModel.getRope().toString());

        undoRedoService.undo();

        assertEquals(TEXT_1, ropeTextEditorModel.getRope().toString());
    }

    @Test
    public void testUndoOnInsertAndBackspace() {
        ropeTextEditorModel = new RopeTextEditorModel();
        undoRedoService = new UndoRedoService(ropeTextEditorModel);
        ropeTextEditorModel.onTextInput(TEXT_2.toCharArray());
        undoRedoService.pushState();
        ropeTextEditorModel.onBackspace();
        undoRedoService.pushState();

        assertEquals(TEXT_2.substring(0, TEXT_2.length() - 1), ropeTextEditorModel.getRope().toString());

        undoRedoService.undo();

        assertEquals(TEXT_2, ropeTextEditorModel.getRope().toString());

        undoRedoService.undo();

        assertEquals("", ropeTextEditorModel.getRope().toString());

        undoRedoService.undo();

        assertEquals("", ropeTextEditorModel.getRope().toString());

    }

    @Test
    public void testRedo() {
        ropeTextEditorModel = new RopeTextEditorModel();
        undoRedoService = new UndoRedoService(ropeTextEditorModel);
        ropeTextEditorModel.onTextInput(TEXT_1.toCharArray());
        undoRedoService.pushState();
        ropeTextEditorModel.onBackspace();
        undoRedoService.pushState();
        ropeTextEditorModel.onTextInput(TEXT_2.toCharArray());
        undoRedoService.pushState();

        assertEquals(TEXT_1.substring(0, TEXT_1.length() - 1) + TEXT_2, ropeTextEditorModel.getRope().toString());

        undoRedoService.undo();

        assertEquals(TEXT_1.substring(0, TEXT_1.length() - 1), ropeTextEditorModel.getRope().toString());

        undoRedoService.undo();

        assertEquals(TEXT_1, ropeTextEditorModel.getRope().toString());

        undoRedoService.redo();

        assertEquals(TEXT_1.substring(0, TEXT_1.length() - 1), ropeTextEditorModel.getRope().toString());

        undoRedoService.redo();

        assertEquals(TEXT_1.substring(0, TEXT_1.length() - 1) + TEXT_2, ropeTextEditorModel.getRope().toString());

        undoRedoService.redo();

        assertEquals(TEXT_1.substring(0, TEXT_1.length() - 1) + TEXT_2, ropeTextEditorModel.getRope().toString());

    }

    @Test
    public void testUndoWithMaxStackSize() {
        ropeTextEditorModel = new RopeTextEditorModel();
        ropeTextEditorModel.onTextInput(TEXT_2.toCharArray()); //"What is the amazing snow behind the window."
        undoRedoService = new UndoRedoService(ropeTextEditorModel);
        undoRedoService.pushState();

        int redoTimes = UndoRedoService.STACK_MAX_SIZE + 3;

        for (int i = 0; i <= UndoRedoService.STACK_MAX_SIZE + 3; i++) {
            ropeTextEditorModel.onBackspace();
            undoRedoService.pushState();
        }
        String expectedResultAfterDelete = TEXT_2.substring(0, TEXT_2.length() - redoTimes - 1); //"What is the amazing" - 24 symbols were deleted
        assertEquals(expectedResultAfterDelete, ropeTextEditorModel.getRope().toString());

        for (int i = 0; i <= UndoRedoService.STACK_MAX_SIZE + 3; i++) {
            undoRedoService.undo();
        }

        String expectedResultAfterRedo = expectedResultAfterDelete + TEXT_2.substring(TEXT_2.length() - redoTimes - 1, TEXT_2.length() - 5);// What is the amazing snow behind the wi
        assertEquals(expectedResultAfterRedo, ropeTextEditorModel.getRope().toString());
    }


}
