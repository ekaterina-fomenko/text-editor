package com.editor.model.rope;

/**
 * This class contains information about node in rope tree
 */

public class RopeNode {
    private CharSequence value;
    private RopeNode left;
    private RopeNode right;
    private int weight;

    public RopeNode() {
        this(null);
    }

    /*
    *Create leaf node
    */
    public RopeNode(CharSequence value) {
        this.value = value;
        this.right = null;
        this.left = null;
        this.weight = value != null ? value.length() : 0;
    }

    public int getWeight() {
        return weight;
    }

    public RopeNode getLeft() {
        return left;
    }

    public RopeNode getRight() {
        return right;
    }

    public CharSequence getValue() {
        return value;
    }

    public void addChildren(RopeNode left, RopeNode right) {
        this.right = right;
        this.left = left;
        this.weight = right.weight + left.weight;
    }

    public boolean isLeaf() {
        return left == null && right == null;

    }
}
