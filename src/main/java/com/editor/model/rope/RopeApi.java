package com.editor.model.rope;

public interface RopeApi {
    int getLinesNum();

    int getLength();

    int getDepth();

    Rope substring(int start, int end);

    Rope append(Rope rope);

    Rope append(char[] str);

    Rope insert(int index, Rope text);

    Rope insert(int index, char[] text);

    RopeIterator iterator(int start);

    int charIndexOfLineStart(int lineIndex);

    int getMaxLineLength();

    char charAt(int i);

    default Rope append(String str) {
        return append(str.toCharArray());
    }

    int lineAtChar(int index);
}
