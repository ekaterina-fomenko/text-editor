package com.editor.model.rope;

/**
 * This class contains information about node in rope tree
 */

public class RopeNode {
    CharSequence value;
    RopeNode left;
    RopeNode right;
    int length;
    int depth;
    int linesNum;

    public RopeNode() {
        this(null);
    }

    /*
    * Create node with children
     */
    public RopeNode(RopeNode left, RopeNode right) {
        this.right = right;
        this.left = left;
        this.length = right.length + left.length;
        this.linesNum = right.linesNum + left.linesNum;
        this.depth = Math.max(right.depth, left.depth) + 1;
    }

    /*
    *Create leaf node
    */
    public RopeNode(CharSequence value) {
        this(value, 0);
    }

    public RopeNode(CharSequence value, int linesNum) {
        this.value = value;
        this.right = null;
        this.left = null;
        this.depth = 0;
        this.length = value != null ? value.length() : 0;
        this.linesNum = 1;
    }

    public int getLength() {
        return length;
    }

    public int getDepth() {
        return depth;
    }

    public RopeNode getLeft() {
        return left;
    }

    public RopeNode getRight() {
        return right;
    }

    public String getValue() {
        return value == null ? null : value.toString();
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public boolean hasOneChildOnly() {
        return (null == left) ^ (null == right);
    }

    public RopeNode getSingleChild() {
        if (!hasOneChildOnly()) {
            throw new IllegalStateException();
        }

        return left == null ? right : left;
    }

    public boolean isEmpty() {
        return isLeaf() && value == null;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
