package com.editor.model.undo;

import com.editor.model.RopeTextEditorModel;
import com.editor.model.rope.Rope;
import com.editor.model.rope.RopeApi;

public class ModelState {
    private final int selectionEnd;
    private RopeApi value;
    private int cursorPosition;

    public ModelState(RopeTextEditorModel model) {
        this.value = model.getRope();
        this.cursorPosition = model.getCursorPosition();
        this.selectionEnd = model.getSelectionEnd();
    }

    public void updateModel(RopeTextEditorModel model) {
        model.setRope(value);
        model.setCursorPosition(cursorPosition);
        model.setSelectionEnd(selectionEnd);
    }
}
