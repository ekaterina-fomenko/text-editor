package com.editor.model.rope;

public interface RopeApi {
    int getLinesNum();

    int getLength();

    int getDepth();

    Rope append(Rope rope);

    Rope append(char[] str);

    RopeIterator iterator(int start);

    int charIndexOfLineStart(int lineIndex);

    int getMaxLineLength();

    char charAt(int i);

    default Rope append(String str) {
        return append(str.toCharArray());
    }
}
