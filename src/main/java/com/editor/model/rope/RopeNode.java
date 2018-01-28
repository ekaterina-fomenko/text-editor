package com.editor.model.rope;

import com.editor.model.StringUtils;
import com.editor.system.SystemConstants;

/**
 * This class contains information about node in rope tree
 */

public class RopeNode {
    char[] value;
    RopeNode left;
    RopeNode right;
    int length;
    int depth;
    int linesNum;
    MaxLineLengthInfo maxLineLengthInfo;

    private static StringSizeProvider sizeProvider = new CountingStringSizeProvider();

    public static void setSizeProvider(StringSizeProvider value) {
        sizeProvider = value;
    }

    public static StringSizeProvider getSizeProvider() {
        return sizeProvider;
    }

    public RopeNode() {
        this((char[]) null);
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

    public RopeIterator iterator(final int start) {
        if (start < 0 || start > getLength())
            throw new IndexOutOfBoundsException("Rope index out of range: " + start);

        RopeNode leftChild = getLeft();
        if (leftChild != null && start >= leftChild.getLength()) {
            return getRight().iterator(start - getLeft().getLength());
        } else {
            return new RopeIterator(this, start);
        }
    }

    /*
    *Create leaf node
    */
    public RopeNode(String value) {
        this(value.toCharArray());
    }

    public RopeNode(char[] value) {
        populateFrom(value);
    }

    public void populateFrom(char[] value) {
        this.value = value;
        this.right = null;
        this.left = null;
        this.depth = 0;
        this.length = value != null ? value.length : 0;
        if (value == null) {
            linesNum = 1;
            maxLineLengthInfo = new MaxLineLengthInfo(0, 0, 0);
        } else {
            final char[] NEW_LINE = SystemConstants.NEW_LINE_CHARS;

            int index = StringUtils.indexOf(value, NEW_LINE, 0);
            int lengthToFirstBoundary = index < 0 ? -1 : sizeProvider.getWidth(value, 0, index);
            int maxLineLengthInCenter = index < 0 ? sizeProvider.getWidth(value) : -1;
            int substringsCount = 0;

            while (index > -1) {
                substringsCount++;

                int nextIndex = StringUtils.indexOf(value, NEW_LINE, index + 1);
                if (nextIndex == -1) {
                    break;
                } else {
//                    int nextLineSize = nextIndex - index - NEW_LINE.length;
                    int nextLineSize = sizeProvider.getWidth(
                            value,
                            index + NEW_LINE.length,
                            nextIndex - index - NEW_LINE.length);
                    maxLineLengthInCenter = Math.max(maxLineLengthInCenter, nextLineSize);
                    index = nextIndex;
                }
            }

            int lengthFromLastBoundary = index == -1 ?
                    -1 :
                    sizeProvider.getWidth(value,
                            index + NEW_LINE.length,
                            value.length - index - NEW_LINE.length);

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

    public char[] getValue() {
        return value;
    }

    public String getValueString() {
        return new String(getValue());
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

    public MaxLineLengthInfo getMaxLineLengthInfo() {
        return maxLineLengthInfo;
    }

    public char charAt(int i) {
        char result = charAt(i, this);
        return result;
    }

    public int getMaxLineLength() {
        MaxLineLengthInfo info = this.maxLineLengthInfo;
        if (info.hasNoBoundaries()) {
            return isLeaf() ? info.maxLineLengthInCenter : left.getMaxLineLength() + right.getMaxLineLength();
        }

        return Math.max(Math.max(info.lengthFromLastBoundary, info.lengthFromLastBoundary), info.maxLineLengthInCenter);
    }

    public int getLinesNum() {
        return linesNum;
    }

    public boolean isFlat() {
        return getDepth() == 0;
    }

    private char charAt(int index, RopeNode ropeNode) {
        if (ropeNode.isLeaf()) {
            return ropeNode.getValue()[index];
        }
        if (index >= ropeNode.getLeft().getLength()) {
            return charAt(index - ropeNode.getLeft().getLength(), ropeNode.getRight());
        }
        return charAt(index, ropeNode.getLeft());
    }
}
