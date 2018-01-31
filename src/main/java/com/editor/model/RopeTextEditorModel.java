package com.editor.model;

import com.editor.model.rope.*;
import com.editor.system.Constants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class process all actions that are made with text model such as:
 * Update indexes for paired brackets
 * Update cursor position
 * Add text
 * Process selected text
 */

public class RopeTextEditorModel {
    private int cursorPosition;
    private Pointer selectionEnd;
    private RopeApi rope;
    private Pointer startBracket;
    private Pointer endBracket;
    private LinesBuffer linesBuffer = new LinesBuffer();

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

    public void addText(char[] text) {
        deleteSelection();

//        List<String> lines = splitIntoLines(text);
//        for (int i = 0; i < lines.size(); i++) {
//            String line = lines.get(i);
//            this.lineBuilders.get(cursorPosition.row).insert(cursorPosition.column, line);
//            cursorPosition.column += line.length();
//
//            if (i < lines.size() - 1) {
//                cursorPosition.row++;
//                lineBuilders.add(cursorPosition.row, new StringBuilder());
//                cursorPosition.column = 0;
//            }
//        }
        rope = rope.append(text);
    }

    private List<String> splitIntoLines(String text) {
        List<String> lines = new ArrayList<>();
        Scanner sc = new Scanner(text);
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        return lines;
    }

    public void addNewLine() {
        if (isSelectionInProgress()) {
            onBackspace();

        }
//        StringBuilder currentRow = this.lineBuilders.get(cursorPosition.row);
//        String restOfCurrentLine = currentRow.substring(cursorPosition.column);
//
//        currentRow.delete(cursorPosition.column, currentRow.length());
//        lineBuilders.add(cursorPosition.row + 1, new StringBuilder(restOfCurrentLine));
        rope.append(Constants.NEW_LINE);//todo: to cursor position
//        cursorPosition.row++;
//        cursorPosition.column = 0;
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

        cursorPosition++;
    }

    public void movePointerLeft(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        cursorPosition--;
    }

    public void movePointerUp(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        int currentLineIndex = linesBuffer.getLine(cursorPosition);
        if (currentLineIndex < 0) {
            return;
        } else if (currentLineIndex == 0) {
            // TODO: scroll up
        } else {
            List<LineInfo> linesInfo = linesBuffer.getLinesInfo();

            int prevLineIndex = currentLineIndex - 1;
            int prevLineLen = linesInfo.get(prevLineIndex).getLength();
            int lengthToLineStart = Math.min(cursorPosition - linesInfo.get(currentLineIndex).getStartIndex(), prevLineLen);
            cursorPosition = linesInfo.get(prevLineIndex).getStartIndex() + lengthToLineStart;
        }
    }

    public void movePointerDown(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        int currentLineBufferIndex = linesBuffer.getLine(cursorPosition);
        if (currentLineBufferIndex < 0) {
            return;
        } else {
            List<LineInfo> linesInfo = linesBuffer.getLinesInfo();

            if (currentLineBufferIndex >= linesInfo.size() - 1) {
                // TODO: scroll down
            } else {
                int nextLineBufferIndex = currentLineBufferIndex + 1;
                int nextLineLength = linesInfo.get(nextLineBufferIndex).getLength();
                int currentLineStartIndex = linesInfo.get(currentLineBufferIndex).getStartIndex();
                int lengthToStart = Math.min(
                        cursorPosition - currentLineStartIndex,
                        nextLineLength);
                int nextLineStartIndex = linesInfo.get(nextLineBufferIndex).getStartIndex();
                cursorPosition = nextLineStartIndex + lengthToStart;
            }
        }
    }

    public void dropSelection() {
        selectionEnd = null;
    }

    public void startOrContinueSelection() {
        if (!isSelectionInProgress()) {
//            selectionEnd = new Pointer(cursorPosition.row, cursorPosition.column);
        }
    }

    public boolean isSelectionInProgress() {
        return selectionEnd != null;
    }

//    public Pointer getSelectionFrom() {
//        int comparison = cursorPosition.compareTo(selectionEnd);

//        Pointer leftMostPointer = comparison < 0 ? cursorPosition : selectionEnd;
//
//        return new Pointer(leftMostPointer);
//    }

//    public Pointer getSelectionTo() {
//        int comparison = cursorPosition.compareTo(selectionEnd);
//
//        Pointer rightMostPointerExcluded = comparison > 0 ? cursorPosition : selectionEnd;
//
//        return new Pointer(rightMostPointerExcluded.row, rightMostPointerExcluded.column);
//    }

//    private StringBuilder getCurrentRow() {
//        return lineBuilders.get(cursorPosition.row);
//    }

//    private int getCurrentRowLength() {
//        return getCurrentRow().length();
//    }

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

    public void setSelectionEnd(Pointer selectionEnd) {
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

    public void setLinesBuffer(LinesBuffer linesBuffer) {
        this.linesBuffer = linesBuffer;
    }
}
