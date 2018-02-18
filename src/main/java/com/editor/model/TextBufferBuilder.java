package com.editor.model;

import java.util.ArrayList;
import java.util.List;

public class TextBufferBuilder {
    private List<LineInfo> lines = new ArrayList<>();
    private Character cursorChar = Character.MIN_VALUE;

    public TextBufferBuilder addLine(LineInfo lineInfo) {
        lines.add(lineInfo);
        return this;
    }

    public TextBufferBuilder withCursorChar(Character c) {
        cursorChar = c;
        return this;
    }

    public TextBuffer build() {
        return new TextBuffer(lines, cursorChar);
    }
}
