package com.editor.model.rope;

import com.editor.system.SystemConstants;

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
        this.linesNum = right.linesNum + left.linesNum - 1;
        this.depth = Math.max(right.depth, left.depth) + 1;
    }

    /*
    *Create leaf node
    */
    public RopeNode(String value) {
        populateFrom(value);
    }

    public void populateFrom(String value) {
        this.value = value;
        this.right = null;
        this.left = null;
        this.depth = 0;
        this.length = value != null ? value.length() : 0;

        int newLinesCount = value == null ? 0 : countSubstrings(value, SystemConstants.NEW_LINE);
        this.linesNum = newLinesCount + 1;
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

    private int countSubstrings(String text, String substring) {
        int index = text.indexOf(substring);
        int substringsCount = 0;
        while (index > 0) {
            substringsCount++;
            index = text.indexOf(substring, index + 1);
        }

        return substringsCount;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public int getLinesNum() {
        return linesNum;
    }
}
