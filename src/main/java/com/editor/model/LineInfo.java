package com.editor.model;

public class LineInfo {
    private final int startIndex;
    private final int length;

    public LineInfo(int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLength() {
        return length;
    }

    public int getEndIndex() {
        return getStartIndex() + getLength();
    }
}
