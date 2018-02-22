package com.editor.model.undo;

import com.editor.model.RopeTextEditorModel;

import java.util.Stack;

public class UndoService {
    private Stack<ModelState> statesStack;
    private Stack<ModelState> oldStatesStack;
    private RopeTextEditorModel model;

    public UndoService(RopeTextEditorModel model) {
        this.model = model;
        this.statesStack = new Stack<>();
        this.oldStatesStack = new Stack<>();
    }

    public void pushState() {
        this.statesStack.push(new ModelState(model));
    }

    public void undo() {
        if (statesStack.size() <= 1) {
            return;
        }

        oldStatesStack.push(statesStack.pop());
        statesStack.peek().updateModel(model);
    }

    public void redo() {
        if (oldStatesStack.size() <= 1) {
            return;
        }

        statesStack.push(oldStatesStack.pop());
        statesStack.peek().updateModel(model);
    }
}
