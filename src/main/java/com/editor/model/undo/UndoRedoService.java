package com.editor.model.undo;

import com.editor.model.RopeTextEditorModel;

import java.util.Stack;

public class UndoRedoService {
    private Stack<ModelState> statesStack;
    private Stack<ModelState> oldStatesStack;
    private RopeTextEditorModel model;

    public UndoRedoService(RopeTextEditorModel model) {
        this.model = model;
        this.statesStack = new Stack<>();
        this.oldStatesStack = new Stack<>();
        pushState();
    }

    public void pushState() {
        this.statesStack.push(new ModelState(model));
    }

    public void undo() {
        if (statesStack.size() <= 1) {
            // We need 2 latest states: current (to push into Redo states) and previous (to rollback to)
            return;
        }

        oldStatesStack.push(statesStack.pop());
        statesStack.peek().updateModel(model);
    }

    public void redo() {
        if (oldStatesStack.isEmpty()) {
            return;
        }

        ModelState targetState = oldStatesStack.pop();

        statesStack.push(targetState);
        targetState.updateModel(model);
    }
}
