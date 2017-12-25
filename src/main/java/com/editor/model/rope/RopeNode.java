package com.editor.model.rope;

/**
 * This class contains information about node in rope tree
 */

public class RopeNode {
    private CharSequence value;
    private RopeNode left;
    private RopeNode right;
    //private int weight;
    private int length;

    private int depth;

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
        this.depth = Math.max(right.depth, left.depth) + 1;
    }

    /*
    *Create leaf node
    */
    public RopeNode(CharSequence value) {
        this.value = value;
        this.right = null;
        this.left = null;
        this.depth = 0;
        this.length = value != null ? value.length() : 0;
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
        return value.toString();
    }

    public boolean isLeaf() {
        return left == null && right == null;

    }
}
