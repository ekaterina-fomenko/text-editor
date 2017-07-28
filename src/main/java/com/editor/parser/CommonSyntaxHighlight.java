package com.editor.parser;

/**
 * This class helps to set start and end indexes for reserved words
 */
public class CommonSyntaxHighlight {
    private int rowIndex;
    private int startIndex;
    private int endIndex;

    public CommonSyntaxHighlight(int rowIndex, int startIndex, int endIndex) {
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
}
