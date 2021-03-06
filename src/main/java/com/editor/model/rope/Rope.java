package com.editor.model.rope;

import com.editor.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a rope data structure
 */
public class Rope {
    protected static int MAX_LENGTH_IN_ROPE = 256;

    Logger log = LoggerFactory.getLogger(Rope.class);

    RopeNode node;

    protected RopeCommonOperations operations = new RopeCommonOperations(MAX_LENGTH_IN_ROPE);

    Rope(String str) {
        this(str.toCharArray());
    }

    Rope(char[] charSequence) {
        node = new RopeNode(charSequence);
    }

    Rope(RopeNode node) {
        this.node = node;
    }

    public Rope() {
        this(new char[0]);
    }

    public int getLinesNum() {
        return this.node.getLinesNum();
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

    public Rope append(char[] str) {
        return append(operations.create(str));
    }

    public Rope append(String str) {
        return append(str.toCharArray());
    }

    public int lineAtChar(int index) {
        return node.lineAtChar(index);
    }

    public Rope substring(int start, int end) {
        if (start < 0 || end > getLength())
            throw new IllegalArgumentException("Illegal subsequence (" + start + "," + end + ")");

        if (start == end) {
            return Rope.empty();
        }

        if (start == 0) {
            return operations.split(this, end).get(0);
        }

        if (end == getLength()) {
            return operations.split(this, start).get(1);
        }

        Rope splittedRope = operations.split(this, start).get(1);
        return operations.split(splittedRope, end - start).get(0);
    }

    public static Rope empty() {
        return new Rope();
    }

    public Rope insert(int index, char[] text) {
        return insert(index, operations.create(text));
    }

    public Rope insert(int index, Rope text) {
        Rope start = substring(0, index);
        Rope end = substring(index, getLength());

        return start.append(text).append(end);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        appendToBuilder(stringBuilder, node);
        return stringBuilder.toString();
    }

    public char[] toChars() {
        return node.toChars();
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

    public int charIndexOfLineStart(int lineIndex) {
        long start = System.currentTimeMillis();

        int result = charIndexOfLineStart(lineIndex, node);

        long end = System.currentTimeMillis();

        log.debug("CharIndexOfLineStart: {}ms", end - start);

        return result;
    }

    boolean isFlat() {
        return node.isFlat();
    }

    public int charIndexOfLineStart(int lineIndex, RopeNode node) {
        int linesNum = node.getLinesNum();
        if (linesNum < lineIndex) {
            return -1;
        }

        RopeNode left = node.getLeft();
        if (left != null && left.getLinesNum() > lineIndex) {
            return charIndexOfLineStart(lineIndex, left);
        }

        RopeNode right = node.getRight();
        int leftLength = left == null ? 0 : left.getLength();
        int leftLinesNum = left == null ? 0 : left.getLinesNum();
        if (right != null && right.getLinesNum() > lineIndex - leftLinesNum + 1) {
            return leftLength + charIndexOfLineStart(lineIndex - leftLinesNum + 1, right);
        }

        // it must be a leaf
        if (node.getValue() == null) {
            return -1;
        }

        if (lineIndex == 0) {
            return 0;
        }

        int startingFrom = -1;
        int lineCounter = 0;
        while ((startingFrom = StringUtils.indexOf(node.getValue(), '\n', startingFrom + 1)) > -1) {
            lineCounter++;
            if (lineCounter == lineIndex) {
                return startingFrom + 1;
            }
        }

        return -1;
    }

    public void writeTo(FileWriter writer) {
        writeTo(writer, node);
    }

    private void writeTo(FileWriter writer, RopeNode ropeNode) {
        if (ropeNode == null) {
            return;
        }
        if (ropeNode.isLeaf()) {
            try {
                writer.write(ropeNode.getValue(), 0, ropeNode.getValue().length);
            } catch (IOException e) {
                log.error("Error while writing to file", e);
            }
            return;
        }
        writeTo(writer, ropeNode.getLeft());
        writeTo(writer, ropeNode.getRight());
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

    public int getMaxLineLength() {
        return node.getMaxLineLength();
    }

    public char charAt(int i) {
        return node.charAt(i);
    }
}
