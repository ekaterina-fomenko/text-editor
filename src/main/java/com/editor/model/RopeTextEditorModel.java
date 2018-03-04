package com.editor.model;

import com.editor.model.buffer.VisibleLinesInfo;
import com.editor.model.rope.Rope;
import com.editor.model.rope.RopeNode;
import com.editor.model.rope.StringSizeProvider;

import java.awt.*;
import java.util.List;

/**
 * This class process all actions that are made with text model such as:
 * Update indexes for paired brackets
 * Update cursor position
 * Add text
 * Process selected text
 */

public class RopeTextEditorModel implements Resetable {
    private int cursorPosition;
    private Rectangle cursorRect;
    private int selectionEnd;
    private Rope rope;
    private VisibleLinesInfo visibleLinesInfo;

    public RopeTextEditorModel() {
        cursorRect = new Rectangle();
        cursorRect = new Rectangle();
        visibleLinesInfo = new VisibleLinesInfo.Builder().build();
        reset();
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public void setRope(Rope rope) {
        this.rope = rope;
    }

    public Rope getRope() {
        return rope;
    }

    public void onTextInput(char[] text) {
        deleteSelection();

        rope = rope.insert(cursorPosition, text);
        cursorPosition += text.length;
    }

    public void onEnter() {
        char[] text = System.lineSeparator().toCharArray();

        onTextInput(text);
    }

    private void incCursorPosition(int count) {
        cursorPosition = Math.min(rope.getLength(), cursorPosition + count);
    }

    private void decCursorPosition(int count) {
        cursorPosition = Math.max(0, cursorPosition - count);
    }

    public void onBackspace() {
        if (!isSelectionInProgress()) {
            startOrContinueSelection();
            movePointerLeft(false);
        }

        deleteSelection();
    }

    private void deleteSelection() {
        if (!isSelectionInProgress()) {
            return;
        }

        int end = Math.max(selectionEnd, cursorPosition);
        int start = Math.min(selectionEnd, cursorPosition);

        if (end == start) {
            return;
        }

        Rope ropeStart = rope.substring(0, start);
        Rope ropeEnd = rope.substring(end, rope.getLength());

        rope = ropeStart.append(ropeEnd);
        cursorPosition = start;
        dropSelection();
    }

    public void movePointerRight(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        if ('\r' == visibleLinesInfo.getCursorChar()) {
            incCursorPosition(1);
        }

        incCursorPosition(1);
    }

    public void movePointerLeft(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        decCursorPosition(1);
    }

    public boolean movePointerUp(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        List<LineInfo> linesInfo = visibleLinesInfo.getLinesInfo();
        int currentLineBufferIndex = visibleLinesInfo.getLineByCharIndex(cursorPosition);
        if (currentLineBufferIndex < 0) {
            return true;
        } else if (currentLineBufferIndex == 0) {
            return true;
        } else {
            int prevLineIndex = currentLineBufferIndex - 1;
            int prevLineLen = linesInfo.get(prevLineIndex).getLength();
            int lengthToLineStart = Math.min(cursorPosition - linesInfo.get(currentLineBufferIndex).getStartIndex(), prevLineLen);
            cursorPosition = linesInfo.get(prevLineIndex).getStartIndex() + lengthToLineStart;
        }

        return false;
    }

    public boolean movePointerDown(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        int currentLineBufferIndex = visibleLinesInfo.getLineByCharIndex(cursorPosition);
        if (currentLineBufferIndex < 0) {
            return true;
        } else {
            List<LineInfo> linesInfo = visibleLinesInfo.getLinesInfo();
            int currentLineStartIndex = linesInfo.get(currentLineBufferIndex).getStartIndex();

            if (currentLineBufferIndex == linesInfo.size() - 1) {
                return true;
            } else {
                int nextLineBufferIndex = currentLineBufferIndex + 1;
                int nextLineLength = linesInfo.get(nextLineBufferIndex).getLength();

                int nextLineStartIndex = linesInfo.get(nextLineBufferIndex).getStartIndex();

                int lengthToStart = Math.min(
                        cursorPosition - currentLineStartIndex,
                        nextLineLength);
                cursorPosition = nextLineStartIndex + lengthToStart;
            }
        }

        return false;
    }

    public void dropSelection() {
        selectionEnd = -1;
    }

    public void startOrContinueSelection() {
        if (!isSelectionInProgress()) {
            selectionEnd = cursorPosition;
        }
    }

    public boolean isSelectionInProgress() {
        return selectionEnd >= 0;
    }

    public String getSelectedText() {
        if (!isSelectionInProgress()) {
            return "";
        }

        int end = Math.max(selectionEnd, cursorPosition);
        int start = Math.min(selectionEnd, cursorPosition);

        Rope selectedRope = rope.substring(start, end);
        return selectedRope.toString();
    }

    public boolean isInSelection(int position) {
        if (!isSelectionInProgress()) {
            return false;
        }

        int selectionEnd = getSelectionEnd();
        int cursorPosition = getCursorPosition();

        return (selectionEnd <= position && position < cursorPosition) ||
                (cursorPosition <= position && position < selectionEnd);
    }

    public void resetCursorPosition() {
        cursorPosition = 0;
    }

    public void movePointerToInitPosition(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }
        cursorPosition = 0;
    }

    public void movePointerToLastPosition(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }
        cursorPosition = rope.getLength();
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public void movePointerToTheEndOfLine(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }
        int currentLineBufferIndex = visibleLinesInfo.getLineByCharIndex(cursorPosition);
        if (currentLineBufferIndex > -1) {
            cursorPosition = visibleLinesInfo.getLinesInfo().get(currentLineBufferIndex).getEndIndex();
        }
    }

    public void movePointerToStartOfLine(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }
        int currentLineBufferIndex = visibleLinesInfo.getLineByCharIndex(cursorPosition);
        if (currentLineBufferIndex > -1) {
            cursorPosition = visibleLinesInfo.getLinesInfo().get(currentLineBufferIndex).getStartIndex();
        }
    }

    public void append(char[] line) {
        rope = rope.append(line);
    }

    @Override
    public void reset() {
        rope = Rope.empty();
        resetCursorPosition();
        dropSelection();
    }

    public static void setStringSizeProvider(StringSizeProvider provider) {
        RopeNode.setSizeProvider(provider);
    }

    public void setVisibleLinesInfo(VisibleLinesInfo visibleLinesInfo) {
        this.visibleLinesInfo = visibleLinesInfo;
    }

    public Rectangle getCursorRect() {
        return cursorRect;
    }

    public void setCursorRect(Rectangle cursorRect) {
        this.cursorRect = cursorRect;
    }

    public void moveCursorRectToY(int y) {
        cursorRect = new Rectangle(cursorRect.x, y, cursorRect.width, cursorRect.height);
    }
}
