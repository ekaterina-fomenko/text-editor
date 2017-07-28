package com.editor.parser;

/**
 * This class provides information about start and end indexes of comments
 */

public class CommentsHighlight {
    private int rowIndex;
    private int startIndex;
    private int endIndex;

    public CommentsHighlight(int rowIndex, int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.rowIndex = rowIndex;
        this.endIndex = endIndex;
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
