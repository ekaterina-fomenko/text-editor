package com.editor.utils;

public class StringUtils {
    /**
     * [Copied from String.indexOf]
     * Code shared by String and StringBuffer to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param source       the characters being searched.
     * @param sourceOffset offset of the source string.
     * @param sourceCount  count of the source string.
     * @param target       the characters being searched for.
     * @param targetOffset offset of the target string.
     * @param targetCount  count of the target string.
     * @param fromIndex    the index to begin searching from.
     */
    static int indexOf(char[] source, int sourceOffset, int sourceCount,
                       char[] target, int targetOffset, int targetCount,
                       int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source[i] != first) {
                while (++i <= max && source[i] != first) ;
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]
                        == target[k]; j++, k++)
                    ;

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

    public static int indexOf(char[] source, char target, int fromIndex) {
        for (int i = fromIndex; i < source.length; i++) {
            if (source[i] == target) {
                return i;
            }
        }

        return -1;
    }

    public static int indexOf(char[] source, char[] target, int fromIndex) {
        return indexOf(source, 0, source.length,
                target, 0, target.length, fromIndex);
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
