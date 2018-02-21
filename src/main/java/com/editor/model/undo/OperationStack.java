package com.editor.model.undo;

import java.util.Stack;

public class OperationStack {
    private Stack<OperationInfo> commands;

    public OperationStack() {
        this.commands = new Stack<>();
    }

    public OperationInfo peek() {
        return commands.peek();
    }

    public OperationInfo pop() {
        return commands.pop();
    }

    public void push(OperationInfo command) {
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
