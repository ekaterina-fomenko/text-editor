package com.editor.model.rope;

import com.editor.utils.LoggingUtils;
import com.editor.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Describes operations with ropes
 */

public class RopeCommonOperations {
    // Need for optimize rebalancing
    public static final long[] FIBONACCI = {0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L, 55L, 89L, 144L, 233L, 377L, 610L, 987L, 1597L, 2584L, 4181L, 6765L, 10946L, 17711L, 28657L, 46368L, 75025L, 121393L, 196418L, 317811L, 514229L, 832040L, 1346269L, 2178309L, 3524578L, 5702887L, 9227465L, 14930352L, 24157817L, 39088169L, 63245986L, 102334155L, 165580141L, 267914296L, 433494437L, 701408733L, 1134903170L, 1836311903L, 2971215073L, 4807526976L, 7778742049L, 12586269025L, 20365011074L, 32951280099L, 53316291173L, 86267571272L, 139583862445L, 225851433717L, 365435296162L, 591286729879L, 956722026041L, 1548008755920L, 2504730781961L, 4052739537881L, 6557470319842L, 10610209857723L, 17167680177565L, 27777890035288L, 44945570212853L, 72723460248141L, 117669030460994L, 190392490709135L, 308061521170129L, 498454011879264L, 806515533049393L, 1304969544928657L, 2111485077978050L, 3416454622906707L, 5527939700884757L, 8944394323791464L, 14472334024676221L, 23416728348467685L, 37889062373143906L, 61305790721611591L, 99194853094755497L, 160500643816367088L, 259695496911122585L, 420196140727489673L, 679891637638612258L, 1100087778366101931L, 1779979416004714189L, 2880067194370816120L, 4660046610375530309L, 7540113804746346429L};

    private final int maxLengthInRope;

    private Logger log = LoggerFactory.getLogger(RopeCommonOperations.class);

    public RopeCommonOperations(int maxLengthInRope) {
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
            return new Rope(StringUtils.concat(left.toChars(), right.toChars()));
        }

        /* If right node has no children and left has children then try to analyze next level of left node over right node*/
        if (!left.isFlat() && right.isFlat()) {
            RopeNode leftNode = left.getNode();

            if (right.getLength() + leftNode.getRight().getLength() < maxLengthInRope) {
                RopeNode rightChild = new RopeNode(StringUtils.concat(leftNode.getRight().toChars(), right.getNode().toChars()));
                RopeNode ropeNode = new RopeNode(leftNode.getLeft(), rightChild);
                return rebalance(new Rope(ropeNode));
            }

        }

        //If left node has no children and right has children then try to analyze next level of right node regarding current left node
        if (left.isFlat() && !right.isFlat()) {
            RopeNode rightNode = right.getNode();

            if (left.getLength() + rightNode.getLeft().getLength() < maxLengthInRope) {
                RopeNode leftChild = new RopeNode(StringUtils.concat(left.getNode().toChars(), rightNode.getLeft().toChars()));
                RopeNode newNode = new RopeNode(leftChild, rightNode.getRight());
                return rebalance(new Rope(newNode));
            }
        }

        return rebalance(new Rope(new RopeNode(left.getNode(), right.getNode())));
    }

    public void normalize(RopeNode node) {
        if (node == null || node.isLeaf()) {
            return;
        }

        if (node.hasOneChildOnly()) {
            RopeNode child = node.getSingleChild();
            normalize(child);
            node.setNodeValuesFrom(child);
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

    public boolean isBalanced(Rope rope) {
        int maxLengthIndex = rope.getDepth() + 1;
        if (maxLengthIndex >= FIBONACCI.length - 1) {
            return false;
        }

        long balanceValue = rope.getLength() - FIBONACCI[maxLengthIndex];
        return balanceValue >= 0;
    }

    public Rope rebalance(Rope rope) {
        if (!isBalanced(rope)) {
            long start = System.currentTimeMillis();
            RopeNode oldNode = rope.node;
            List<RopeNode> allTreeLeaves = getAllTreeLeaves(rope.getNode());
            rope.node = balance(allTreeLeaves);
            long end = System.currentTimeMillis();
            log.info("Rebalanced in {}ms: length={},depth={}->{},lines={}",
                    end - start,
                    rope.getLength(),
                    oldNode.getDepth(),
                    rope.getDepth(),
                    rope.getLinesNum());
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

    public int getIncDepth(RopeNode node) {
        return Math.max(getDepth(node.left), getDepth(node.right)) + 1;
    }

    public int getLinesNumFromChildren(RopeNode node) {
        return node.right.linesNum + node.left.linesNum - 1;
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

        return Arrays.asList(new Rope(leftSplit), new Rope(rightSplit));
    }

    public Rope create(String text) {
        return create(text.toCharArray());
    }

    public Rope create(char[] text) {
        List<RopeNode> nodes = createNodes(text);
        return new Rope(balance(nodes));
    }

    public List<RopeNode> createNodes(char[] text) {
        List<RopeNode> result = new ArrayList<>();

        if (text.length <= maxLengthInRope) {
            result.add(new RopeNode(text));
            return result;
        }

        int i = 0;
        while (i < text.length) {
            int lastIndex = i + maxLengthInRope;
            if (lastIndex <= text.length) {
                result.add(new RopeNode(StringUtils.subArray(text, i, lastIndex - i)));
            } else {
                result.add(new RopeNode(StringUtils.subArray(text, i)));
            }

            i = lastIndex;
        }

        return result;
    }

    private void split(RopeNode leftSplit, RopeNode rightSplit, RopeNode parent, int index) {
        if (parent.isLeaf()) {
            char[] parentValue = parent.getValue();

            leftSplit.populateFrom(StringUtils.subArray(parentValue, 0, index));
            rightSplit.populateFrom(StringUtils.subArray(parentValue, index));
        } else {
            leftSplit.length = index;
            rightSplit.length = parent.getLength() - index;

            if (index < parent.getLeft().getLength()) {
                rightSplit.right = parent.getRight();
                RopeNode leftChildOfRightParent = new RopeNode();
                rightSplit.left = leftChildOfRightParent;

                split(leftSplit, leftChildOfRightParent, parent.getLeft(), index);
                rightSplit.depth = getIncDepth(rightSplit);
                rightSplit.linesNum = getLinesNumFromChildren(rightSplit);
                rightSplit.maxLineLengthInfo = fromChildNodes(rightSplit);
            } else {
                leftSplit.left = parent.getLeft();
                RopeNode rightChildOfLeftParent = new RopeNode();
                leftSplit.right = rightChildOfLeftParent;

                split(rightChildOfLeftParent, rightSplit, parent.right, index - parent.left.getLength());
                leftSplit.depth = getIncDepth(leftSplit);
                leftSplit.linesNum = getLinesNumFromChildren(leftSplit);
                leftSplit.maxLineLengthInfo = fromChildNodes(leftSplit);
            }
        }
    }

    private MaxLineLengthInfo fromChildNodes(RopeNode node) {
        RopeNode left = node.left;
        RopeNode right = node.right;

        if (left != null && right != null) {
            return MaxLineLengthInfo.fromChildNodes(left, right);
        } else if (node.hasOneChildOnly()) {
            return node.maxLineLengthInfo;
        }

        return null;
    }


}
