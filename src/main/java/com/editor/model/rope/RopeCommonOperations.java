package com.editor.model.rope;

/**
 * Describes operations with ropes
 */

public class RopeCommonOperations {

    public Rope concatenate(Rope left, Rope right) {

        if (left == null || left.length == 0) {
            return right;
        }

        if (right == null || right.length == 0) {
            return left;
        }

        if (left.length + right.length < Rope.MAX_LENGTH_IN_ROPE) {
            return rebalance(left, right);
        }

        return null;
    }

    private Rope rebalance(Rope left, Rope right) {
        return null;
    }

}
