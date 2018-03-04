package com.editor.model.buffer;

import com.editor.model.LineInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Info about visible area collected while painting for faster processing visible area
 */
public class VisibleLinesInfo {

    public static class Builder {
        private List<LineInfo> linesInfo;
        private Character cursorChar;

        public Builder() {
            linesInfo = new ArrayList<>();
            cursorChar = Character.MIN_VALUE;
        }

        public Builder addLine(LineInfo lineInfo) {
            linesInfo.add(lineInfo);
            return this;
        }

        public Builder withCursorChar(Character c) {
            cursorChar = c;
            return this;
        }

        public VisibleLinesInfo build() {
            return new VisibleLinesInfo(linesInfo, cursorChar);
        }
    }

    private List<LineInfo> linesInfo;
    private Character cursorChar;

    public VisibleLinesInfo(List<LineInfo> linesInfo, Character cursorChar) {
        this.linesInfo = linesInfo;
        this.cursorChar = cursorChar;
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
