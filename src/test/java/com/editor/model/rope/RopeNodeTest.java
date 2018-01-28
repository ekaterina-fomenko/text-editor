package com.editor.model.rope;

import org.junit.Test;

import static com.editor.system.Constants.*;
import static java.text.MessageFormat.*;
import static org.junit.Assert.assertEquals;

public class RopeNodeTest {

    @Test
    public void testLinesNum() {
        assertEquals(1, new RopeNode().getLinesNum());
        assertEquals(1, new RopeNode("").getLinesNum());
        assertEquals(2, new RopeNode(NEW_LINE).getLinesNum());
        assertEquals(2, new RopeNode(NEW_LINE + "ABC").getLinesNum());
        assertEquals(3, new RopeNode(NEW_LINE + "ABC" + NEW_LINE).getLinesNum());
    }

    @Test
    public void linesNumShouldNotCountSpecialSymbolsInText() {
        assertEquals(1, new RopeNode("A\\n\\rB").getLinesNum());
        assertEquals(1, new RopeNode("A\\n\\nB").getLinesNum());
        assertEquals(1, new RopeNode("A/n/rB").getLinesNum());
    }

    @Test
    public void testMaxLineLengthOnLeaf() {
        RopeNode ropeNode = new RopeNode(format("ABC{0}DEFG{1}D", NEW_LINE, NEW_LINE));
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
        RopeNode ropeNode = new RopeNode(format("{0}ABC{1}DEFG{2}D{3}", NEW_LINE, NEW_LINE, NEW_LINE, NEW_LINE));
        MaxLineLengthInfo maxLineLengthInfo = ropeNode.getMaxLineLengthInfo();
        assertEquals(4, ropeNode.getMaxLineLength());
        assertEquals(0, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(0, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(4, maxLineLengthInfo.getMaxLineLengthInCenter());
    }

    @Test
    public void testMaxLineLengthOnDepth1() {
        RopeNode left = new RopeNode(format("{0}ABC{1}DEFG", NEW_LINE, NEW_LINE));
        RopeNode right = new RopeNode(format("BB{0}A{1}DEFG", NEW_LINE, NEW_LINE));
        RopeNode root = new RopeNode(left, right);
        MaxLineLengthInfo maxLineLengthInfo = root.getMaxLineLengthInfo();
        assertEquals(6, root.getMaxLineLength());
        assertEquals(0, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(4, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(6, maxLineLengthInfo.getMaxLineLengthInCenter());
    }

    @Test
    public void testMaxLineLengthOnDepth2() {
        RopeNode left = new RopeNode(format("{0}ABC{1}DEFG", NEW_LINE, NEW_LINE));
        RopeNode right = new RopeNode("BBADEFG");
        RopeNode nodeDepth1 = new RopeNode(left, right);
        RopeNode nodeLeaf = new RopeNode(format("BBB{0}A{1}DEFG", NEW_LINE, NEW_LINE));

        RopeNode root = new RopeNode(nodeDepth1, nodeLeaf);
        MaxLineLengthInfo maxLineLengthInfo = root.getMaxLineLengthInfo();
        assertEquals(14, root.getMaxLineLength());

        assertEquals(0, maxLineLengthInfo.getLengthToFirstBoundary());
        assertEquals(4, maxLineLengthInfo.getLengthFromLastBoundary());
        assertEquals(14, maxLineLengthInfo.getMaxLineLengthInCenter());
    }
}