package com.editor.model.buffer;

import com.editor.model.LineInfo;

import java.util.ArrayList;
import java.util.List;

public class VisibleLinesBufferBuilder {
    private List<LineInfo> lines = new ArrayList<>();
    private Character cursorChar = Character.MIN_VALUE;

    public VisibleLinesBufferBuilder addLine(LineInfo lineInfo) {
        lines.add(lineInfo);
        return this;
    }

    public VisibleLinesBufferBuilder withCursorChar(Character c) {
        cursorChar = c;
        return this;
    }

    public VisibleLinesInfo build() {
        return new VisibleLinesInfo(lines, cursorChar);
    }
}
