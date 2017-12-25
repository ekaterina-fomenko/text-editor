package com.editor.model.rope;

/**
 * This class represents a rope data structure
 */
public class Rope {

    private static final int MAX_DEPTH = 100;
    private static final int MAX_LENGTH_IN_ROPE = 20;

    RopeNode node;

    private RopeCommonOperations operations = new RopeCommonOperations(MAX_DEPTH, MAX_LENGTH_IN_ROPE);

    public Rope(CharSequence charSequence) {
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

    public Rope append(Rope rope) {
        return operations.concat(this, rope);
    }

    public boolean containsOneLevelOnly(){
        return node.getDepth() == 0;
    }

    public char charAt(int index) {
        return charAt(index, node);
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        appendToBuilder(stringBuilder, node);
        return stringBuilder.toString();
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

    public Rope append(CharSequence str) {
        return append(new Rope(str));
    }
}
