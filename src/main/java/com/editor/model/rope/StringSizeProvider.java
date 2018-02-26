package com.editor.model.rope;

public interface StringSizeProvider {
    default int getWidth(char[] text) {
        return getWidth(text, 0, text.length);
    }

    int getWidth(char[] text, int offset, int count);
}
