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

        if (!left.hasChildren() && right.hasChildren()) {
            RopeNode node = right.node;

            if (left.length + node.getLeft().getWeight() < Rope.MAX_LENGTH_IN_ROPE) {
                //create new node and rebalance    
            }

        }

        if (left.hasChildren() && !right.hasChildren()) {
            RopeNode node = left.node;

            if (right.length + node.getRight().getWeight() < Rope.MAX_LENGTH_IN_ROPE) {
                //create new node and rebalance
            }
        }
        //return rebalance
        return null;
    }

    private Rope rebalance(Rope left, Rope right) {
        return null;
    }

}
