package com.editor.model.rope;

import org.junit.Before;
import org.junit.Test;

import static java.text.MessageFormat.*;
import static org.junit.Assert.assertEquals;

public class RopeNodeTest {
    @Before
    public void before() {
        RopeNode.setSizeProvider(new CountingStringSizeProvider());
    }

    @Test
    public void testLinesNum() {
        assertEquals(1, new RopeNode().getLinesNum());
        assertEquals(1, new RopeNode("").getLinesNum());
        assertEquals(2, new RopeNode("\n").getLinesNum());
        assertEquals(2, new RopeNode("\n" + "ABC").getLinesNum());
        assertEquals(3, new RopeNode("\n" + "ABC" + "\n").getLinesNum());
    }

    @Test
    public void linesNumShouldNotCountSpecialSymbolsInText() {
        assertEquals(1, new RopeNode("A\\n\\rB").getLinesNum());
        assertEquals(1, new RopeNode("A\\n\\nB").getLinesNum());
        assertEquals(1, new RopeNode("A/n/rB").getLinesNum());
    }

    @Test
    public void testMaxLineLengthOnLeaf() {
        RopeNode ropeNode = new RopeNode("ABC\nDEFG\nD");
        MaxLineLengthInfo maxLineLengthInfo = ropeNode.getMaxLineLengthInfo();
        assertEquals(3, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(1, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(4, maxLineLengthInfo.getMaxLineLengthInCenter());
    }

    @Test
    public void testMaxLineLengthOnLeafNoBoundaries() {
        RopeNode ropeNode = new RopeNode("ABC");
        MaxLineLengthInfo maxLineLengthInfo = ropeNode.getMaxLineLengthInfo();
        assertEquals(3, ropeNode.getMaxLineLength());
        assertEquals(-1, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(-1, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(3, maxLineLengthInfo.getMaxLineLengthInCenter());
    }

    @Test
    public void testMaxLineLengthOnLeafNoBoundariesDepth1() {
        RopeNode left = new RopeNode("ABC");
        RopeNode right = new RopeNode("D");
        RopeNode root = new RopeNode(left, right);

        MaxLineLengthInfo maxLineLengthInfo = root.getMaxLineLengthInfo();
        assertEquals(4, root.getMaxLineLength());
        assertEquals(-1, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(-1, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(4, maxLineLengthInfo.getMaxLineLengthInCenter());
    }

    @Test
    public void testMaxLineLengthOnLeafWithEntersOnEdges() {
        RopeNode ropeNode = new RopeNode("\nABC\nDEFG\nD\n");
        MaxLineLengthInfo maxLineLengthInfo = ropeNode.getMaxLineLengthInfo();
        assertEquals(4, ropeNode.getMaxLineLength());
        assertEquals(0, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(0, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(4, maxLineLengthInfo.getMaxLineLengthInCenter());
    }

    @Test
    public void testMaxLineLengthOnDepth1() {
        RopeNode left = new RopeNode("\nABC\nDEFG");
        RopeNode right = new RopeNode("BB\nA\nDEFG");
        RopeNode root = new RopeNode(left, right);
        MaxLineLengthInfo maxLineLengthInfo = root.getMaxLineLengthInfo();
        assertEquals(6, root.getMaxLineLength());
        assertEquals(0, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(4, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(6, maxLineLengthInfo.getMaxLineLengthInCenter());
    }

    @Test
    public void testMaxLineLengthOnDepth2() {
        RopeNode left = new RopeNode("\nABC\nDEFG");
        RopeNode right = new RopeNode("BBADEFG");
        RopeNode nodeDepth1 = new RopeNode(left, right);
        RopeNode nodeLeaf = new RopeNode("BBB\nA\nDEFG");

        RopeNode root = new RopeNode(nodeDepth1, nodeLeaf);
        MaxLineLengthInfo maxLineLengthInfo = root.getMaxLineLengthInfo();
        assertEquals(14, root.getMaxLineLength());

        assertEquals(0, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(4, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(14, maxLineLengthInfo.getMaxLineLengthInCenter());
    }

    @Test
    public void testLineAtChar() {
        RopeNode root = new RopeNode(
                new RopeNode(
                        new RopeNode("AA\nBB\nCC"),// 0..7
                        new RopeNode("AA\nBB\nCC") // 8..15
                ),
                new RopeNode(
                        new RopeNode("AABBCCD"), // 17..24
                        new RopeNode("AA\nBB\nCC") // 25..32
                )
        );

        assertEquals(0, root.lineAtChar(0));
        assertEquals(0, root.lineAtChar(2));
        assertEquals(1, root.lineAtChar(3));
        assertEquals(1, root.lineAtChar(4));
        assertEquals(2, root.lineAtChar(7));

        assertEquals(4, root.lineAtChar(16));

        assertEquals(4, root.lineAtChar(20));
        assertEquals(4, root.lineAtChar(25));
        assertEquals(6, root.lineAtChar(31));
    }
}