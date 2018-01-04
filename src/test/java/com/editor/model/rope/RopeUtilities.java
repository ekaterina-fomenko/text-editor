package com.editor.model.rope;

public class RopeUtilities {
    static void setRopeLengthAndDepth(int length, int depth) {
        Rope.MAX_LENGTH_IN_ROPE = length;
        Rope.MAX_DEPTH = depth;
    }

    static void resetToDefaultLengthAndDepth() {
        Rope.MAX_LENGTH_IN_ROPE = 100;
        Rope.MAX_DEPTH = 20;
    }
}
