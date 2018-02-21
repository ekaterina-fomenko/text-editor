package com.editor.model.undo;

import com.editor.model.undo.OppositeCommands.Command;

public class OperationInfo {
    Command command;
    char[] value;
    int startIndex;
    int endIndex;

    public OperationInfo(Command command, char[] value, int startIndex, int endIndex) {
        this.command = command;
        this.value = value;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public OperationInfo(Command command, char[] value, int startIndex) {
        this(command, value, startIndex, -1);
    }

    public Command getCommand() {
        return command;
    }

    public char[] getValue() {
        return value;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}
