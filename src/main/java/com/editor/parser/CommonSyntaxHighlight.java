package com.editor.parser;

/**
 * This class helps to set the start, end and the color for highlight
 */
public class CommonSyntaxHighlight {
    private int rowIndex;
    private int startIndex;
    private int endIndex;
    private int rowEndIndex;

    public CommonSyntaxHighlight(int rowIndex, int startIndex, int endIndex) {
        new CommonSyntaxHighlight(rowIndex, startIndex, endIndex, -1);
    }

    public CommonSyntaxHighlight(int rowIndex, int startIndex, int endIndex, int rowEndIndex) {
        this.startIndex = startIndex;
        this.rowIndex = rowIndex;
        this.endIndex = endIndex;
        this.rowEndIndex = rowEndIndex;
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

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public void setRowEndIndex(int rowEndIndex) {
        this.rowEndIndex = rowEndIndex;
    }
}
