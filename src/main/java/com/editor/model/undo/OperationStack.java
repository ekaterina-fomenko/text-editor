package com.editor.model.undo;

import java.util.Stack;

public class OperationStack {
    private Stack<ModelState> commands;

    public OperationStack() {
        this.commands = new Stack<>();
    }

    public ModelState peek() {
        return commands.peek();
    }

    public ModelState pop() {
        return commands.pop();
    }

    public void push(ModelState command) {
        commands.push(command);
    }

    public boolean isNotEmpty() {
        return !commands.isEmpty();
    }

    @Override
    public String toString() {
        return commands.toString();
    }
}
