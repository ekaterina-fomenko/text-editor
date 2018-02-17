package com.editor.model;

public class LineInfo {
    private int startIndex;
    private int length;

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

    public int getIndex(int index) {
        return Math.min(getEndIndex(), index);
    }

    @Override
    public String toString() {
        return String.format("LineInfo{startIndex=%d, length=%d}", startIndex, length);
    }
}
