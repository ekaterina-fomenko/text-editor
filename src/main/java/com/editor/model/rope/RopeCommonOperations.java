package com.editor.model.rope;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes operations with ropes
 */

public class RopeCommonOperations {
    private static final int MAX_DEPTH = 100;

    public Rope concatenate(Rope left, Rope right) {

        if (left == null || left.getLength() == 0) {
            return right;
        }

        if (right == null || right.getLength() == 0) {
            return left;
        }

        /* If length in summary less than max length in one rope then just concat two strings on one rope */
        if (left.getLength() + right.getLength() < Rope.MAX_LENGTH_IN_ROPE) {
            return new Rope(left.toString() + right.toString());
        }

        /* If right node has no children and left has children then try to analyze next level of left node over right node*/
        if (!left.containsOneLevelOnly() && right.containsOneLevelOnly()) {
            RopeNode leftNode = left.node;

            if (right.getLength() + leftNode.getRight().getLength() < Rope.MAX_LENGTH_IN_ROPE) {
                RopeNode rightChild = new RopeNode(leftNode.getRight().getValue() + right.toString());
                RopeNode ropeNode = new RopeNode(leftNode.getLeft(), rightChild);
                return rebalance(new Rope(ropeNode));
            }

        }

        /* If left node has no children and right has children then try to analyze next level of right node regarding current left node*/
        if (left.containsOneLevelOnly() && !right.containsOneLevelOnly()) {
            RopeNode rightNode = right.node;

            if (left.getLength() + rightNode.getLeft().getLength() < Rope.MAX_LENGTH_IN_ROPE) {
                RopeNode leftChild = new RopeNode(left.toString() + rightNode.getLeft().getValue());
                RopeNode newNode = new RopeNode(leftChild, rightNode.getRight());
                return rebalance(new Rope(newNode));
            }
        }
        return rebalance(new Rope(new RopeNode(left.node, right.node)));
    }

    public Rope rebalance(Rope rope) {
        if (rope.getDepth() > MAX_DEPTH) {
            rope.node = balance(getAllTreeLeaves(rope.node));
        }
        return rope;
    }

    private RopeNode balance(List<RopeNode> leavesList) {
        return buildBalancedTree(leavesList, 0, leavesList.size());
    }

    public RopeNode buildBalancedTree(List<RopeNode> leaves, int start, int end) {
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

    /*
    * Return al leaves of the tree
    * @param node is a tree node
    */
    public List<RopeNode> getAllTreeLeaves(RopeNode node) {
        List<RopeNode> leavesList = new ArrayList<>();
        storeLeafNodesInList(leavesList, node);
        return leavesList;
    }

    /*
    * Store tree leaf nodes in list
    */
    public void storeLeafNodesInList(List<RopeNode> listOfNodes, RopeNode node) {
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            listOfNodes.add(node);
            return;
        }

        storeLeafNodesInList(listOfNodes, node.getLeft());
        storeLeafNodesInList(listOfNodes, node.getRight());
    }

}
