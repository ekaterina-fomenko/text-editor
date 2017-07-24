package com.editor.parser;

public class CommentsHighlight {
    private int rowIndex;
    private int startIndex;
    private int endIndex;

    public CommentsHighlight(int rowIndex, int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.rowIndex = rowIndex;
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public boolean isIsCommentsInProgress(int currentRow, int currentColumn) {
        if (this.rowIndex == currentRow && this.startIndex <= currentColumn) {
            return true;
        }
        return false;
    }
}
