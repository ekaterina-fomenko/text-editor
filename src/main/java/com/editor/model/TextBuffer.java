package com.editor.model;

import java.util.ArrayList;
import java.util.List;

public class TextBuffer {
    private List<LineInfo> linesInfo = new ArrayList<>();
    private Character cursorChar = Character.MIN_VALUE;

    public TextBuffer(List<LineInfo> linesInfo, Character cursorChar) {
        this.linesInfo = linesInfo;
        this.cursorChar = cursorChar;
    }

    public TextBuffer() {
    }

    public List<LineInfo> getLinesInfo() {
        return linesInfo;
    }

    public int getLineByCharIndex(int charIndex) {
        for (int i = 0; i < linesInfo.size(); i++) {
            int startIndex = linesInfo.get(i).getStartIndex();
            if (charIndex >= startIndex
                    && charIndex <= startIndex + linesInfo.get(i).getLength()) {
                return i;
            }
        }

        return -1;
    }

    public boolean isEOL(int charIndex) {
        int lineIndex = getLineByCharIndex(charIndex);
        if (lineIndex < 0) {
            return false;
        }

        LineInfo lineInfo = linesInfo.get(lineIndex);
        return lineInfo.getStartIndex() + lineInfo.getLength() == charIndex;
    }

    public Character getCursorChar() {
        return cursorChar;
    }

    public int getBufferStartIndex() {
        if (linesInfo.size() == 0) {
            return -1;
        }

        return linesInfo.get(0).getStartIndex();
    }

    public int getBufferEndIndex() {
        if (linesInfo.size() == 0) {
            return -1;
        }

        return linesInfo.get(linesInfo.size() - 1).getEndIndex();
    }
}
