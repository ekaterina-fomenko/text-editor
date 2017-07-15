package com.editor.model;

import java.util.ArrayList;
import java.util.List;

public class TextEditorModel {
    private Pointer cursorPosition;
    private Pointer selectionEnd;
    private ArrayList<StringBuilder> lineBuilders;
    private List<Integer> lineLengthsList;

    public TextEditorModel() {
        this.cursorPosition = new Pointer();
        this.lineBuilders = new ArrayList<>();
        this.lineBuilders.add(new StringBuilder());

        this.lineLengthsList = new ArrayList<>();
        this.lineLengthsList.add(0);
    }

    public void addText(String text) {
        this.lineBuilders.get(cursorPosition.row).insert(cursorPosition.column, text);
        cursorPosition.column += text.length();
    }

    public void addNewLine() {
        if (isSelectionInProgress()) {
            onBackspace();

        }
        StringBuilder currentRow = this.lineBuilders.get(cursorPosition.row);
        String restOfCurrentLine = currentRow.substring(cursorPosition.column);

        currentRow.delete(cursorPosition.column, currentRow.length());
        lineBuilders.add(cursorPosition.row + 1, new StringBuilder(restOfCurrentLine));
        cursorPosition.row++;
        cursorPosition.column = 0;
    }

    public void onBackspace() {
        if (isSelectionInProgress()) {
            removeAll(cursorPosition, selectionEnd);
        } else {
            startOrContinueSelection();
            movePointerLeft(false);
            removeAll(cursorPosition, selectionEnd);
            dropSelection();
        }
    }

    private void removeAll(Pointer cursorPosition, Pointer selectionEnd) {
        int comparison = cursorPosition.compareTo(selectionEnd);
        if (comparison == 0) {
            return;
        }

        Pointer from = comparison < 0 ? cursorPosition : selectionEnd;
        Pointer to = comparison > 0 ? cursorPosition : selectionEnd;

        StringBuilder startingRow = lineBuilders.get(from.row);
        StringBuilder endingRow = lineBuilders.get(to.row);

        if (from.row == to.row) {
            startingRow.delete(from.column, to.column);
        } else {
            startingRow.delete(from.column, Math.max(from.column, startingRow.length() - 1));

            for (int i = from.row + 1; i < to.row; i++) {
                lineBuilders.remove(i);
            }

            endingRow.delete(0, to.column);
            startingRow.append(endingRow);
            lineBuilders.remove(to.row);
        }
    }

    public Pointer getCursorPosition() {
        return cursorPosition;
    }

    public ArrayList<StringBuilder> getLineBuilders() {
        return lineBuilders;
    }

    public void movePointerRight(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        cursorPosition.column++;

        int currentRowLength = getCurrentRowLength();
        if (cursorPosition.column > currentRowLength) {
            if (cursorPosition.row == lineBuilders.size() - 1) {
                cursorPosition.column = currentRowLength;
            } else {
                cursorPosition.column = 0;
                cursorPosition.row++;
            }
        }
    }

    public void movePointerLeft(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        cursorPosition.column--;
        if (cursorPosition.column < 0) {
            if (cursorPosition.row != 0) {
                cursorPosition.row--;
                cursorPosition.column = getCurrentRowLength();
            } else {
                cursorPosition.column = 0;
            }
        }
    }

    public void movePointerUp(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        if (cursorPosition.row == 0) {
            return;
        }

        cursorPosition.row--;
        cursorPosition.column = Math.min(cursorPosition.column, getCurrentRowLength());
    }

    public void movePointerDown(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        if (cursorPosition.row == lineBuilders.size() - 1) {
            return;
        }

        cursorPosition.row++;
        cursorPosition.column = Math.min(cursorPosition.column, getCurrentRowLength());
    }

    public void dropSelection() {
        selectionEnd = null;
    }

    public void startOrContinueSelection() {
        if (!isSelectionInProgress()) {
            selectionEnd = new Pointer(cursorPosition.row, cursorPosition.column);
        }
    }

    public boolean isSelectionInProgress() {
        return selectionEnd != null;
    }

    public Pointer getSelectionFrom() {
        int comparison = cursorPosition.compareTo(selectionEnd);

        return comparison < 0 ? cursorPosition : selectionEnd;
    }

    public Pointer getSelectionTo() {
        int comparison = cursorPosition.compareTo(selectionEnd);

        return comparison > 0 ? cursorPosition : selectionEnd;
    }

    private StringBuilder getCurrentRow() {
        return lineBuilders.get(cursorPosition.row);
    }

    private int getCurrentRowLength() {
        return getCurrentRow().length();
    }
}
