package com.editor.model;

import java.util.ArrayList;
import java.util.List;

public class LinesBuffer {
    private List<LineInfo> linesInfo = new ArrayList<>();

    public LinesBuffer(List<LineInfo> linesInfo) {
        this.linesInfo = linesInfo;
    }

    public LinesBuffer() {
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
}
