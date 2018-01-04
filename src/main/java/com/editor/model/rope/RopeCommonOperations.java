package com.editor.model.rope;

import com.editor.system.SystemConstants;
import com.sun.deploy.util.StringUtils;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Describes operations with ropes
 */

public class RopeCommonOperations {

    private final int maxDepth;
    private final int maxLengthInRope;

    public RopeCommonOperations(int maxDepth, int maxLengthInRope) {
        this.maxDepth = maxDepth;
        this.maxLengthInRope = maxLengthInRope;
    }

    public Rope concat(Rope left, Rope right) {

        if (left == null || left.getLength() == 0) {
            return right;
        }

        if (right == null || right.getLength() == 0) {
            return left;
        }

        // If length in summary less than max length in one rope then just concat two strings on one rope
        if (left.getLength() + right.getLength() < maxLengthInRope) {
            return new Rope(left.toString() + right.toString());
        }

        /* If right node has no children and left has children then try to analyze next level of left node over right node*/
        if (!left.isFlat() && right.isFlat()) {
            RopeNode leftNode = left.getNode();

            if (right.getLength() + leftNode.getRight().getLength() < maxLengthInRope) {
                RopeNode rightChild = new RopeNode(leftNode.getRight().getValue() + right.toString());
                RopeNode ropeNode = new RopeNode(leftNode.getLeft(), rightChild);
                return rebalance(new Rope(ropeNode));
            }

        }

        //If left node has no children and right has children then try to analyze next level of right node regarding current left node
        if (left.isFlat() && !right.isFlat()) {
            RopeNode rightNode = right.getNode();

            if (left.getLength() + rightNode.getLeft().getLength() < maxLengthInRope) {
                RopeNode leftChild = new RopeNode(left.toString() + rightNode.getLeft().getValue());
                RopeNode newNode = new RopeNode(leftChild, rightNode.getRight());
                return rebalance(new Rope(newNode));
            }
        }

        return rebalance(new Rope(new RopeNode(left.getNode(), right.getNode())));
    }

    public static void normalize(RopeNode node) {
        if (node == null || node.isLeaf()) {
            return;
        }

        if (node.hasOneChildOnly()) {
            RopeNode child = node.getSingleChild();
            normalize(child);
            copyNodeValues(node, child);
        } else {
            normalize(node.left);
            normalize(node.right);
            node.depth = getIncDepth(node);
        }

        if (node.right != null && node.right.isEmpty()) {
            node.right = null;
        }

        if (node.left != null && node.left.isEmpty()) {
            node.left = null;
        }
    }

    public Rope rebalance(Rope rope) {
        if (rope.getDepth() > maxDepth) {
            rope.node = balance(getAllTreeLeaves(rope.getNode()));
        }
        return rope;
    }

    private static RopeNode balance(List<RopeNode> leavesList) {
        return buildBalancedTree(leavesList, 0, leavesList.size());
    }

    public static RopeNode buildBalancedTree(List<RopeNode> leaves, int start, int end) {
        int range = end - start;
        switch (range) {
            case 1:
                return leaves.get(start);
            case 2:
                return new RopeNode(leaves.get(start), leaves.get(start + 1));
            default:
                int middle = start + (range / 2);
                return new RopeNode(buildBalancedTree(leaves, start, middle), buildBalancedTree(leaves, middle, end));
        }
    }

    private static void copyNodeValues(RopeNode dest, RopeNode source) {
        dest.left = source.left;
        dest.right = source.right;
        dest.value = source.value;
        dest.depth = source.depth;
    }

    /*
    * Return al leaves of the tree
    * @param node is a tree node
    */
    public static List<RopeNode> getAllTreeLeaves(RopeNode node) {
        List<RopeNode> leavesList = new ArrayList<>();
        storeLeafNodesInList(leavesList, node);
        return leavesList;
    }

    /*
    * Store tree leaf nodes in list
    */
    public static void storeLeafNodesInList(List<RopeNode> listOfNodes, RopeNode node) {
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            listOfNodes.add(node);
            return;
        }

        storeLeafNodesInList(listOfNodes, node.left);
        storeLeafNodesInList(listOfNodes, node.right);
    }

    public static int getIncDepth(RopeNode node) {
        return Math.max(getDepth(node.left), getDepth(node.right)) + 1;
    }

    public static int getDepth(RopeNode node) {
        return node == null ? 0 : node.depth;
    }

    public List<Rope> split(Rope rope, int index) {
        if (index > rope.getLength()) {
            throw new IndexOutOfBoundsException(String.format("Index '%s' must not be higher than '%s'", index, rope.getLength()));
        }

        RopeNode leftSplit = new RopeNode();
        RopeNode rightSplit = new RopeNode();

        split(leftSplit, rightSplit, rope.node, index);
        RopeCommonOperations.normalize(leftSplit);
        RopeCommonOperations.normalize(rightSplit);

        return Arrays.asList(new Rope(leftSplit), new Rope(rightSplit));
    }

    public Rope create(String string) {
        return create(string, 0);
    }

    public Rope create(String text, int linesCount) {
        /*int newLineIndex = string.indexOf(SystemConstants.NEW_LINE);
        if (newLineIndex > -1) {
            Rope start = create(string.substring(0, newLineIndex));
            Rope end = create(string.substring(newLineIndex + SystemConstants.NEW_LINE.length()));
            Rope concat = concat(start, end);
            concat.node.linesNum++;
            return concat;
        }*/

        if (text.length() <= maxLengthInRope) {
            return new Rope(text);
        }

        Rope rope = new Rope("");
        int i = 0;
        while (i < text.length()) {
            int lastIndex = i + maxLengthInRope;
            int lastIndexNewLineAware = incIndexIfNewLineSymbolSplit(text, lastIndex);

            if (lastIndex <= text.length()) {
                rope = concat(rope, new Rope(text.substring(i, lastIndexNewLineAware)));
            } else {
                rope = concat(rope, new Rope(text.substring(i)));
            }

            i = lastIndexNewLineAware;
        }

        return rope;

//        return new Rope(new RopeNode(string, linesCount));
    }

    private boolean endsWithPart(String string, String subString) {
        return !subString.isEmpty() && (string.endsWith(subString) || endsWithPart(string, subString.substring(0, subString.length() - 1)));
    }

    int incIndexIfNewLineSymbolSplit(String text, int splitIndex) {
        if (SystemConstants.NEW_LINE.length() == 1) {
            return splitIndex;
        }

        if (SystemConstants.NEW_LINE.length() > 2) {
            throw new InvalidStateException("Unkown new line symbol: " + SystemConstants.NEW_LINE);
        }

        if (splitIndex > 1
                && splitIndex < text.length()
                && text.charAt(splitIndex - 1) == SystemConstants.NEW_LINE.charAt(0)
                && text.charAt(splitIndex) == SystemConstants.NEW_LINE.charAt(1)) {
            return splitIndex + 1;
        }

        return splitIndex;
    }

    boolean endsWith(String text, int lastTextIndex, String substring) {
        for (int i = 0; i < substring.length(); i++) {
            int currentIndexInText = lastTextIndex - i;
            if (currentIndexInText < 0 || currentIndexInText >= text.length() ||
                    text.charAt(currentIndexInText) != substring.charAt(substring.length() - 1 - i)) {
                return false;
            }
        }

        return true;
    }

    private void split(RopeNode leftSplit, RopeNode rightSplit, RopeNode parent, int index) {
        if (parent.isLeaf()) {
            String parentValue = parent.getValue();

            leftSplit.value = parentValue.substring(0, index);
            leftSplit.length = leftSplit.value.length();

            leftSplit.depth = 0;

            rightSplit.value = parentValue.substring(index);
            rightSplit.length = rightSplit.value.length();

            rightSplit.depth = 0;
        } else {
            leftSplit.length = index;
            rightSplit.length = parent.getLength() - index;

            if (index < parent.getLeft().getLength()) {
                rightSplit.right = parent.getRight();
                RopeNode leftChildOfRightParent = new RopeNode();
                rightSplit.left = leftChildOfRightParent;

                split(leftSplit, leftChildOfRightParent, parent.getLeft(), index);
                rightSplit.depth = RopeCommonOperations.getIncDepth(rightSplit);
            } else {
                leftSplit.left = parent.getLeft();
                RopeNode rightChildOfLeftParent = new RopeNode();
                leftSplit.right = rightChildOfLeftParent;

                split(rightChildOfLeftParent, rightSplit, parent.right, index - parent.left.getLength());
                leftSplit.depth = RopeCommonOperations.getIncDepth(leftSplit);
            }
        }
    }


}
