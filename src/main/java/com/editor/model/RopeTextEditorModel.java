package com.editor.model;

import com.editor.model.rope.*;
import com.editor.system.Constants;
import com.sun.deploy.Environment;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class process all actions that are made with text model such as:
 * Update indexes for paired brackets
 * Update cursor position
 * Add text
 * Process selected text
 */

public class RopeTextEditorModel {
    public static final boolean IS_MULTI_SYMBOL_NEWLINE = System.lineSeparator().length() > 1;
    private int cursorPosition;
    private Rectangle cursorRect = new Rectangle();
    private int selectionEnd;
    private RopeApi rope;
    private Pointer startBracket;
    private Pointer endBracket;
    private TextBuffer textBuffer = new TextBuffer();

    public Pointer getStartBracket() {
        return startBracket;
    }

    public Pointer getEndBracket() {
        return endBracket;
    }

    public RopeTextEditorModel() {
        this.cursorPosition = 0;
        rope = new RopeCached(new Rope());
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

    public int getLinesCount() {
        return rope.getLinesNum();
    }

    public RopeApi getRope() {
        return rope;
    }

    public void updatePairedBrackets() {
        /*startBracket = null;
        endBracket = null;

        int cursorRow = cursorPosition.row;
        int column = cursorPosition.column;
        if (!isValidTextPosition(cursorRow, column)) {
            return;
        }

        char ch = getChar(cursorRow, column);
        Brackets brackets = new Brackets();
        Map<Character, Boolean> bracketsDirectionMap = brackets.getBracketsDirection();
        Map<Character, Character> bracketsOppositeMap = brackets.getBracketsOpposite();

        if (bracketsDirectionMap.containsKey(ch)) {
            boolean isForward = bracketsDirectionMap.get(ch);
            char opposite = bracketsOppositeMap.get(ch);

            Stack<Boolean> stack = new Stack<>();
            int i = cursorRow;
            int j = column;
            while (i > -1 && i < getLinesCount()) {
                if (isValidTextPosition(i, j)) {
                    char aChar = getChar(i, j);
                    if (aChar == opposite) {
                        stack.pop();
                    } else if (aChar == ch) {
                        stack.add(isForward);
                        startBracket = new Pointer(cursorRow, column);
                    }
                }

                if (stack.isEmpty()) {
                    endBracket = new Pointer(i, j);
                    break;
                }

                j += isForward ? 1 : -1;
                if (isForward) {
//                    if (j > lineBuilders.get(i).length() - 1) {
//                        j = 0;
//                        i++;
//                    }
                } else {
                    if (j < 0) {
                        i--;
//                        j = Math.max(0, i > -1 ? lineBuilders.get(i).length() - 1 : 0);
                    }
                }
            }

        }*/
    }

//    private char getChar(int row, int column) {
//        return this.lineBuilders.get(row).charAt(column);
//    }

    private boolean isValidTextPosition(int row, int column) {
        return true;//row > -1 && row < lineBuilders.size() && column > -1 && column < lineBuilders.get(row).length();
    }

    public void onTextInput(char[] text) {
        deleteSelection();

        rope = rope.insert(cursorPosition, text);
        cursorPosition += text.length;
    }

    public void onEnter() {
        if (isSelectionInProgress()) {
            onBackspace();

        }

        char[] text = System.lineSeparator().toCharArray();
        rope = rope.insert(cursorPosition, text);
        cursorPosition += text.length;
    }

    public void onBackspace() {
        if (isSelectionInProgress()) {
            deleteSelection();
        } else {
            startOrContinueSelection();
            movePointerLeft(false);
            deleteSelection();
        }
    }

    private void deleteSelection() {
        if (!isSelectionInProgress()) {
            return;
        }

        dropSelection();
    }

    public void movePointerRight(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        if ('\r' == textBuffer.getCursorChar()) {
            cursorPosition++;
        }

        cursorPosition++;
    }

    public void movePointerLeft(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        cursorPosition--;

        if (textBuffer.isEOL(cursorPosition) && IS_MULTI_SYMBOL_NEWLINE) {
            cursorPosition--;
        }
    }

    public boolean movePointerUp(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        List<LineInfo> linesInfo = textBuffer.getLinesInfo();
        int currentLineBufferIndex = textBuffer.getLineByCharIndex(cursorPosition);
        if (currentLineBufferIndex < 0) {
            return true;
        } else if (currentLineBufferIndex == 0) {
            return true;
        } else {
            int prevLineIndex = currentLineBufferIndex - 1;
            int prevLineLen = linesInfo.get(prevLineIndex).getLength();
            if (IS_MULTI_SYMBOL_NEWLINE) {
                prevLineLen--;
            }

            int lengthToLineStart = Math.min(cursorPosition - linesInfo.get(currentLineBufferIndex).getStartIndex(), prevLineLen);
            cursorPosition = linesInfo.get(prevLineIndex).getStartIndex() + lengthToLineStart;
        }

        return false;
    }

    public boolean movePointerDown(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        int currentLineBufferIndex = textBuffer.getLineByCharIndex(cursorPosition);
        if (currentLineBufferIndex < 0) {
            return true;
        } else {
            List<LineInfo> linesInfo = textBuffer.getLinesInfo();
            int currentLineStartIndex = linesInfo.get(currentLineBufferIndex).getStartIndex();

            if (currentLineBufferIndex == linesInfo.size() - 1) {
//                cursorPosition = linesInfo.get(currentLineBufferIndex).getIndex(currentLineBufferIndex);
                return true;
            } else {
                int nextLineBufferIndex = currentLineBufferIndex + 1;
                int nextLineLength = linesInfo.get(nextLineBufferIndex).getLength();
                if (IS_MULTI_SYMBOL_NEWLINE) {
                    nextLineLength--;
                }

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
        return selectionEnd > 0;
    }

    public String convertToString(List<StringBuilder> list) {
        return list.stream().collect(Collectors.joining(Constants.NEW_LINE));
    }

    public List<StringBuilder> getSelectedText() {
//        Pointer from = getSelectionFrom();
//        Pointer to = getSelectionTo();
//
//        if (from.equals(to)) {
//            return null;
//        }
//
//        StringBuilder startingRow = lineBuilders.get(from.row);
//        StringBuilder endingRow = lineBuilders.get(to.row);
        List<StringBuilder> resultList = new ArrayList<>();
//
//        if (from.row == to.row) {
//            resultList.add(new StringBuilder(startingRow.substring(from.column, to.column)));
//        } else {
//            resultList.add(new StringBuilder(startingRow.substring(from.column, Math.max(from.column, startingRow.length()))));
//
//            for (int i = from.row + 1; i < to.row; i++) {
//                resultList.add(lineBuilders.get(i));
//            }
//            resultList.add(new StringBuilder(endingRow.substring(0, to.column)));
//
//        }
        return resultList;
    }

    public void movePointerToInitPosition() {
        cursorPosition = 0;
    }

    public void movePointerToLastPosition() {
//        cursorPosition.row = lineBuilders.size() - 1;
//        cursorPosition.column = lineBuilders.get(cursorPosition.row).length();
    }
//
//    public void setCursorPosition(Pointer cursorPosition) {
//        this.cursorPosition = cursorPosition;
//        selectionEnd = null;
//    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public void setLineBuildersFromFile(List<StringBuilder> lineBuilders) {
//        this.lineBuilders = lineBuilders;
//        cursorPosition.row = 0;
//        cursorPosition.column = 0;
    }

    public void movePointerToTheEndOfLine() {
//        cursorPosition.column = lineBuilders.get(cursorPosition.row).length();
    }

//    public void movePointerToStartOfLine() {
//        cursorPosition.column = 0;
//    }

    public void append(char[] line) {
        rope = rope.append(line);
    }

    public void clearAll() {
        rope = new Rope();
    }

    public static void setStringSizeProvider(StringSizeProvider provider) {
        RopeNode.setSizeProvider(provider);
    }

    public void setTextBuffer(TextBuffer textBuffer) {
        this.textBuffer = textBuffer;
    }

    public void insertToPointer(char[] chars) {
        rope = rope.insert(cursorPosition, chars);
    }

    public Rectangle getCursorRect() {
        return cursorRect;
    }

    public void setCursorRect(Rectangle cursorRect) {
        this.cursorRect = cursorRect;
    }
}
