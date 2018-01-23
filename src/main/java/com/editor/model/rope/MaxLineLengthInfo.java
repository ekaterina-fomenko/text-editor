package com.editor.model.rope;

import static java.lang.Math.*;

public class MaxLineLengthInfo {
    int lengthToFirstBoundary;
    int lengthFromLastBoundary;
    int maxLineLengthInCenter;

    public MaxLineLengthInfo(int lengthToFirstBoundary, int lengthFromLastBoundary, int maxLineLengthInCenter) {
        this.lengthToFirstBoundary = lengthToFirstBoundary;
        this.lengthFromLastBoundary = lengthFromLastBoundary;
        this.maxLineLengthInCenter = maxLineLengthInCenter;
    }

    public int getLengthToFirstBoundary() {
        return lengthToFirstBoundary;
    }

    public int getLengthFromLastBoundary() {
        return lengthFromLastBoundary;
    }

    public int getMaxLineLengthInCenter() {
        return maxLineLengthInCenter;
    }

    public boolean hasNoBoundaries() {
        return maxLineLengthInCenter == -1 && lengthToFirstBoundary == -1 && lengthFromLastBoundary == -1;
    }

    public static MaxLineLengthInfo fromChildNodes(RopeNode left, RopeNode right) {
        MaxLineLengthInfo leftInfo = left.maxLineLengthInfo;
        MaxLineLengthInfo rightInfo = right.maxLineLengthInfo;

        if (leftInfo.hasNoBoundaries() && rightInfo.hasNoBoundaries()) {
            return new MaxLineLengthInfo(0, 0, left.getLength() + right.getLength());
        }

        if (leftInfo.hasNoBoundaries()) {
            return new MaxLineLengthInfo(left.getLength() + rightInfo.lengthToFirstBoundary, rightInfo.lengthFromLastBoundary, rightInfo.maxLineLengthInCenter);
        }

        if (rightInfo.hasNoBoundaries()) {
            return new MaxLineLengthInfo(leftInfo.lengthToFirstBoundary, leftInfo.lengthFromLastBoundary + right.length, leftInfo.maxLineLengthInCenter);
        }

        int maxLength = max(leftInfo.lengthFromLastBoundary + rightInfo.lengthToFirstBoundary,
                max(leftInfo.maxLineLengthInCenter, rightInfo.maxLineLengthInCenter));

        return new MaxLineLengthInfo(leftInfo.lengthToFirstBoundary, rightInfo.lengthFromLastBoundary, maxLength);
    }
}
