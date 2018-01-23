package com.editor.model.rope;

public class CountingStringSizeProvider implements StringSizeProvider{
    @Override
    public int getWidth(char[] text, int offset, int count) {
        return count;
    }
}
