package com.editor.utils;

public class StringUtils {
    public static int indexOf(char[] source, char target, int fromIndex) {
        for (int i = fromIndex; i < source.length; i++) {
            if (source[i] == target) {
                return i;
            }
        }

        return -1;
    }

    public static char[] concat(char[] ar1, char[] ar2) {
        char[] res = new char[ar1.length + ar2.length];
        System.arraycopy(ar1, 0, res, 0, ar1.length);
        System.arraycopy(ar2, 0, res, ar1.length, ar2.length);
        return res;
    }

    public static char[] subArray(char[] chars, int start, int len) {
        char[] res = new char[len];
        System.arraycopy(chars, start, res, 0, len);
        return res;
    }

    public static char[] subArray(char[] chars, int start) {
        return subArray(chars, start, chars.length - start);
    }

    public static int countChars(char[] chars, char c, int from, int to) {
        int counter = 0;

        for (int i = from; i < to; i++) {
         if (chars[i] == c) {
             counter++;
         }
        }
        return counter;
    }
}
