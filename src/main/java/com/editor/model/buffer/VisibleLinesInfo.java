package com.editor.model.buffer;

import com.editor.model.LineInfo;

import java.util.ArrayList;
import java.util.List;

public class VisibleLinesInfo {
    private List<LineInfo> linesInfo = new ArrayList<>();
    private Character cursorChar = Character.MIN_VALUE;

    public VisibleLinesInfo(List<LineInfo> linesInfo, Character cursorChar) {
        this.linesInfo = linesInfo;
        this.cursorChar = cursorChar;
    }

    public VisibleLinesInfo() {
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

    public Character getCursorChar() {
        return cursorChar;
    }
}
