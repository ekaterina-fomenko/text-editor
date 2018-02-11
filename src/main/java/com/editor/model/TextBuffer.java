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

    public int getLine(int charIndex) {
        for (int i = 0; i < linesInfo.size(); i++) {
            int startIndex = linesInfo.get(i).getStartIndex();
            if (charIndex >= startIndex
                    && charIndex <= startIndex + linesInfo.get(i).getLength()) {
                return i;
            }
        }

        return -1;
    }

    public Character getCursorChar() {
        return cursorChar;
    }
}
