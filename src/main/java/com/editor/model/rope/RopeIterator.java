package com.editor.model.rope;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Fast Iterator for ropes
 */
public class RopeIterator implements Iterator<Character> {
    private final ArrayDeque<Rope> ropeDeque;
    private Rope currentRope;
    private int currentRopePos;
    private int skip;
    private int currentAbsolutePos;

    public RopeIterator(final Rope rope) {
        this(rope, 0);
    }

    public RopeIterator(final Rope rope, final int start) {
        this.ropeDeque = new ArrayDeque<>();
        this.ropeDeque.push(new Rope(rope.getNode()));
        this.currentRope = null;
        this.initialize();

        if (start < 0 || start > rope.getLength()) {
            throw new IllegalArgumentException("Rope index out of range: " + start);
        }
        this.moveForward(start);

    }


    public boolean canMoveBackwards(final int amount) {
        return (-1 <= (this.currentRopePos - amount));
    }

    public int getPos() {
        return this.currentAbsolutePos;
    }

    @Override
    public boolean hasNext() {
        return this.currentRopePos < this.currentRope.getLength() - 1 || !this.ropeDeque.isEmpty();
    }

    /**
     * Initialize the currentRope and currentRopePos fields.
     */
    private void initialize() {
        while (!this.ropeDeque.isEmpty()) {
            this.currentRope = this.ropeDeque.pop();
            if (!this.currentRope.isFlat()) {
                this.ropeDeque.push(new Rope(this.currentRope.getNode().getRight()));
                this.ropeDeque.push(new Rope(this.currentRope.getNode().getLeft()));
            } else {
                break;
            }
        }
        if (this.currentRope == null)
            throw new IllegalArgumentException("No terminal ropes present.");
        this.currentRopePos = -1;
        this.currentAbsolutePos = -1;
    }

    public void moveBackwards(final int amount) {
        if (!this.canMoveBackwards(amount))
            throw new IllegalArgumentException("Unable to move backwards " + amount + ".");
        this.currentRopePos -= amount;
        this.currentAbsolutePos -= amount;
    }

    public void moveForward(final int amount) {
        this.currentAbsolutePos += amount;
        int remainingAmt = amount;
        while (remainingAmt != 0) {
            final int available = this.currentRope.getLength() - this.currentRopePos - 1;
            if (remainingAmt <= available) {
                this.currentRopePos += remainingAmt;
                return;
            }
            remainingAmt -= available;
            if (this.ropeDeque.isEmpty()) {
                this.currentAbsolutePos -= remainingAmt;
                throw new IllegalArgumentException("Unable to move forward " + amount + ". Reached end of rope.");
            }
            while (!this.ropeDeque.isEmpty()) {
                this.currentRope = this.ropeDeque.pop();
                if (!this.currentRope.isFlat()) {
                    this.ropeDeque.push(new Rope(this.currentRope.getNode().getRight()));
                    this.ropeDeque.push(new Rope(this.currentRope.getNode().getLeft()));
                } else {
                    this.currentRopePos = -1;
                    break;
                }
            }
        }
    }

    @Override
    public Character next() {
        this.moveForward(1 + this.skip);
        this.skip = 0;
        return this.currentRope.charAt(this.currentRopePos);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Rope iterator is read-only.");
    }

    public void skip(final int skip) {
        this.skip = skip;
    }
}
