package com.editor.model.undo;

import com.editor.model.RopeTextEditorModel;

import java.util.Stack;

public class UndoRedoService {
    private Stack<ModelState> undoStack;
    private Stack<ModelState> redoStack;
    private RopeTextEditorModel model;
    // to avoid OutOfMemory
    private static final int STACK_MAX_SIZE = 20;

    public UndoRedoService(RopeTextEditorModel model) {
        this.model = model;
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        pushState();
    }

    public void pushState() {
        updateWithStackSize(undoStack);
        this.undoStack.push(new ModelState(model));
    }

    public void undo() {
        // Need 2 latest states: current (to push into Redo states) and previous (to rollback to)
        if (undoStack.size() <= 1) {
            return;
        }

        updateWithStackSize(redoStack);
        redoStack.push(undoStack.pop());
        undoStack.peek().updateModel(model);
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }

        updateWithStackSize(undoStack);
        ModelState targetState = redoStack.pop();
        undoStack.push(targetState);
        targetState.updateModel(model);
    }

    private static void updateWithStackSize(Stack stack) {
        if (stack.size() == STACK_MAX_SIZE) {
            stack.remove(0);
        }
    }
}
