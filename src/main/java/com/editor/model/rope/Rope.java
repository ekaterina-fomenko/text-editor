package com.editor.model.rope;

/**
 * This class represents a rope data structure
 */
public class Rope {

    RopeNode node;
    int weight;
    int length;
    public static final int MAX_LENGTH_IN_ROPE = 20;

    public Rope(CharSequence charSequence) {
        node = new RopeNode(charSequence);
    }

    public Rope(RopeNode node) {
        this.node = node;
    }

    public Rope append(Rope rope) {
        return null;
    }

    public boolean hasChildren(){
        return (node.getLeft()!=null || node.getRight()!=null);
    }
}
