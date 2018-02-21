package com.editor.model.undo;

import com.editor.model.RopeTextEditorModel;

import static com.editor.model.undo.OppositeCommands.Command;

public class UndoRedoUtil {
    private OperationStack undoCommands;
    private OperationStack redoCommands;
    private RopeTextEditorModel model;

    public UndoRedoUtil(RopeTextEditorModel model) {
        this.model = model;
        this.undoCommands = new OperationStack();
        this.redoCommands = new OperationStack();
    }

    public boolean hasUndo() {
        return undoCommands.isNotEmpty();
    }

    public void addCommand(OperationInfo appendTextCommand) {
        this.undoCommands.push(appendTextCommand);
    }

    public void addCommand(int startIndex, int endIndex, char[] value, Command commandType) {
        OperationInfo operationInfo = new OperationInfo(commandType, value, startIndex, endIndex);
        this.undoCommands.push(operationInfo);
    }

    @Override
    public String toString() {
        return undoCommands.toString();
    }

    public boolean hasRedo() {
        return redoCommands.isNotEmpty();
    }

    public void undo() {
        if (hasUndo()) {
            applyChanges(undoCommands.peek());
            moveLastCommand(undoCommands, redoCommands);
        }
    }

    private void moveLastCommand(OperationStack from, OperationStack to) {
        OperationInfo lastCommand = from.pop();
        to.push(lastCommand);
    }

    public void redo() {
        if (hasRedo()) {
            applyChanges(redoCommands.peek());
            moveLastCommand(redoCommands, undoCommands);
        }
    }

    private void applyChanges(OperationInfo commandInfo) {
        switch (commandInfo.getCommand()) {
            case PASTE:
                model.paste(commandInfo.getValue(), commandInfo.startIndex);
                break;
            case DELETE:
                model.remove(commandInfo.getStartIndex(), commandInfo.getEndIndex());
                break;
        }
    }
}
