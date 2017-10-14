package com.editor.model.rope;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes operations with ropes
 */

public class RopeCommonOperations {
    private static final int MAX_DEPTH = 100;

    public Rope concatenate(Rope left, Rope right) {

        if (left == null || left.length == 0) {
            return right;
        }

        if (right == null || right.length == 0) {
            return left;
        }

        if (left.length + right.length < Rope.MAX_LENGTH_IN_ROPE) {
            //create new node and rebalance
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

    public int getHeight(RopeNode node) {
        if (node == null) {
            return 0;
        }
        int left = getHeight(node.getLeft());
        int right = getHeight(node.getRight());

        if (left == -1 || right == -1) {
            return -1;
        }

        if (Math.abs(left - right) > 1) {
            return -1;
        }

        return Math.max(left, right) + 1;
    }

    private boolean isBalanced(RopeNode node) {
        // if (getHeight(node) > MAX_DEPTH)
        if (node != null && getHeight(node) == -1) {
            return false;
        }
        return true;
    }

    public Rope rebalance(RopeNode node) {
        List<RopeNode> leavesList = getAllTreeLeaves(node);
        return new Rope(buildBalancedTree(leavesList, 0, leavesList.size()));
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
