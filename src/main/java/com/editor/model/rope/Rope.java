package com.editor.model.rope;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a rope data structure
 */
public class Rope {

    protected static int MAX_DEPTH = 100;
    protected static int MAX_LENGTH_IN_ROPE = 20;

    RopeNode node;

    protected RopeCommonOperations operations = new RopeCommonOperations(MAX_DEPTH, MAX_LENGTH_IN_ROPE);

    public Rope(String charSequence) {
        node = new RopeNode(charSequence);
    }

    public Rope(RopeNode node) {
        this.node = node;
    }

    public int getLength() {
        return node.getLength();
    }

    public int getDepth() {
        return node.getDepth();
    }

    public RopeNode getNode() {
        return node;
    }

    public Rope append(Rope rope) {
        return operations.concat(this, rope);
    }

    public Rope append(String str) {
        return append(new Rope(str));
    }

    public Rope substring(int start, int end) {
        if (start < 0 || end > getLength())
            throw new IllegalArgumentException("Illegal subsequence (" + start + "," + end + ")");

        if (start == 0) {
            return operations.split(this, end).get(0);
        }
        if (end == getLength()) {
            return operations.split(this, start).get(1);
        }

        Rope splittedRope = operations.split(this, start).get(1);
        return operations.split(splittedRope, end - start).get(0);
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        appendToBuilder(stringBuilder, node);
        return stringBuilder.toString();
    }

    public Iterator<Character> iterator(final int start) {
        if (start < 0 || start > this.getLength())
            throw new IndexOutOfBoundsException("Rope index out of range: " + start);
        if (start >= this.getNode().getLeft().getLength()) {
            return new Rope(this.getNode().getRight()).iterator(start - this.getNode().getLeft().getLength());
        } else {
            return new RopeIterator(this, start);
        }
    }

    public String printRopeNodes() {
        RopeNode localNode = node;
        StringBuilder stringBuilder = new StringBuilder();
        Queue<RopeNode> queue = new LinkedList<>();
        do {
            stringBuilder.append("(");
            if (localNode.getValue() != null) {
                stringBuilder.append(localNode.getValue());
            } else {
                stringBuilder.append(localNode.getLength());
            }
            stringBuilder.append(")");
            if (localNode.left != null) {
                queue.add(localNode.left);
            }
            if (localNode.right != null) {
                queue.add(localNode.right);
            }
            if (!queue.isEmpty()) {
                localNode = queue.poll();
            } else {
                localNode = null;
            }
        } while (!queue.isEmpty() || localNode != null);
        return stringBuilder.toString();
    }

    public char charAt(int index) {
        return charAt(index, node);
    }

    boolean isFlat() {
        return node.getDepth() == 0;
    }

    private char charAt(int index, RopeNode ropeNode) {
        if (ropeNode.isLeaf()) {
            return ropeNode.getValue().charAt(index);
        }
        if (index > ropeNode.getLeft().getLength()) {
            return charAt(index, ropeNode.getRight());
        }
        return charAt(index, ropeNode.getLeft());
    }

    private void appendToBuilder(StringBuilder builder, RopeNode ropeNode) {
        if (ropeNode == null) {
            return;
        }
        if (ropeNode.isLeaf()) {
            builder.append(ropeNode.getValue());
            return;
        }
        appendToBuilder(builder, ropeNode.getLeft());
        appendToBuilder(builder, ropeNode.getRight());
    }
}
