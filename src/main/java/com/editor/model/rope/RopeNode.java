package com.editor.model.rope;

import com.editor.system.SystemConstants;

/**
 * This class contains information about node in rope tree
 */

public class RopeNode {
    String value;
    RopeNode left;
    RopeNode right;
    int length;
    int depth;
    int linesNum;
    MaxLineLengthInfo maxLineLengthInfo;

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
        this.maxLineLengthInfo = MaxLineLengthInfo.fromChildNodes(left, right);
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
        if (value == null) {
            linesNum = 1;
            maxLineLengthInfo = new MaxLineLengthInfo(0, 0, 0);
        } else {
            final String NEW_LINE = SystemConstants.NEW_LINE;

            int index = value.indexOf(NEW_LINE);
            int lengthToFirstBoundary = index;
            int substringsCount = 0;
            int maxLineLengthInCenter = -1;
            while (index > -1) {
                substringsCount++;

                int nextIndex = value.indexOf(NEW_LINE, index + 1);
                if (nextIndex == -1) {
                    break;
                } else {
                    maxLineLengthInCenter = Math.max(maxLineLengthInCenter, nextIndex - index - NEW_LINE.length());
                    index = nextIndex;
                }
            }

            int lengthFromLastBoundary = index == -1 ? -1 : value.length() - index - NEW_LINE.length();

            this.linesNum = substringsCount + 1;
            this.maxLineLengthInfo = new MaxLineLengthInfo(lengthToFirstBoundary, lengthFromLastBoundary, maxLineLengthInCenter);
        }
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
        while (index >= 0) {
            substringsCount++;
            index = text.indexOf(substring, index + 1);
        }

        return substringsCount;
    }

    public MaxLineLengthInfo getMaxLineLengthInfo() {
        return maxLineLengthInfo;
    }

    public int getLinesNum() {
        return linesNum;
    }
}
