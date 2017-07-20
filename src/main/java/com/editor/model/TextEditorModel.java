package com.editor.model;

import com.editor.parser.Brackets;
import com.editor.system.SystemConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class TextEditorModel {
    private Pointer cursorPosition;
    private Pointer selectionEnd;
    private ArrayList<StringBuilder> lineBuilders;
    private List<Integer> lineLengthsList;
    private Pointer startBracket;
    private Pointer endBracket;

    public Pointer getStartBracket() {
        return startBracket;
    }

    public Pointer getEndBracket() {
        return endBracket;
    }

    public TextEditorModel() {
        this.cursorPosition = new Pointer();
        this.lineBuilders = new ArrayList<>();
        this.lineBuilders.add(new StringBuilder());

        this.lineLengthsList = new ArrayList<>();
        this.lineLengthsList.add(0);
    }

    public void updatePairedBrackets() {
        startBracket = null;
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
            while (i > -1 && i < lineBuilders.size()) {
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
                    if (j > lineBuilders.get(i).length() - 1) {
                        j = 0;
                        i++;
                    }
                } else {
                    if (j < 0) {
                        i--;
                        j = Math.max(0, i > -1 ? lineBuilders.get(i).length() - 1 : 0);
                    }
                }
            }

        }
    }

    private char getChar(int row, int column) {
        return this.lineBuilders.get(row).charAt(column);
    }

    private boolean isValidTextPosition(int row, int column) {
        return row > -1 && row < lineBuilders.size() && column > -1 && column < lineBuilders.get(row).length();
    }

    public void addText(String text) {
        deleteSelection();

        String[] lines = text.split(SystemConstants.NEW_LINE);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            this.lineBuilders.get(cursorPosition.row).insert(cursorPosition.column, line);
            cursorPosition.column += line.length();

            if (i < lines.length - 1) {
                cursorPosition.row++;
                lineBuilders.add(cursorPosition.row, new StringBuilder());
                cursorPosition.column = 0;
            }
        }
    }

    public void addNewLine() {
        if (isSelectionInProgress()) {
            onBackspace();

        }
        StringBuilder currentRow = this.lineBuilders.get(cursorPosition.row);
        String restOfCurrentLine = currentRow.substring(cursorPosition.column);

        currentRow.delete(cursorPosition.column, currentRow.length());
        lineBuilders.add(cursorPosition.row + 1, new StringBuilder(restOfCurrentLine));
        cursorPosition.row++;
        cursorPosition.column = 0;
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

        Pointer from = getSelectionFrom();
        Pointer to = getSelectionTo();

        if (from.equals(to)) {
            return;
        }

        StringBuilder startingRow = lineBuilders.get(from.row);
        StringBuilder endingRow = lineBuilders.get(to.row);

        if (from.row == to.row) {
            startingRow.delete(from.column, to.column);
        } else {
            startingRow.delete(from.column, Math.max(from.column, startingRow.length()));

            endingRow.delete(0, to.column);
            startingRow.append(endingRow);
            lineBuilders.remove(to.row);

            for (int i = to.row - 1; i > from.row; i--) {
                lineBuilders.remove(i);
            }

        }
        this.cursorPosition = from;
        dropSelection();
    }

    public Pointer getCursorPosition() {
        return cursorPosition;
    }

    public ArrayList<StringBuilder> getLineBuilders() {
        return lineBuilders;
    }

    public void movePointerRight(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        cursorPosition.column++;

        int currentRowLength = getCurrentRowLength();
        if (cursorPosition.column > currentRowLength) {
            if (cursorPosition.row != lineBuilders.size() - 1) {
                cursorPosition.column = 0;
                cursorPosition.row++;
            } else {
                cursorPosition.column = currentRowLength;
            }
        }
    }

    public void movePointerLeft(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        cursorPosition.column--;

        if (cursorPosition.column < 0) {
            if (cursorPosition.row != 0) {
                cursorPosition.row--;
                cursorPosition.column = getCurrentRowLength();
            } else {
                cursorPosition.column = 0;
            }
        }
    }

    public void movePointerUp(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        if (cursorPosition.row == 0) {
            return;
        }

        cursorPosition.row--;
        cursorPosition.column = Math.min(cursorPosition.column, getCurrentRowLength());
    }

    public void movePointerDown(boolean dropSelection) {
        if (dropSelection) {
            dropSelection();
        }

        if (cursorPosition.row == lineBuilders.size() - 1) {
            return;
        }

        cursorPosition.row++;
        cursorPosition.column = Math.min(cursorPosition.column, getCurrentRowLength());
    }

    public void dropSelection() {
        selectionEnd = null;
    }

    public void startOrContinueSelection() {
        if (!isSelectionInProgress()) {
            selectionEnd = new Pointer(cursorPosition.row, cursorPosition.column);
        }
    }

    public boolean isSelectionInProgress() {
        return selectionEnd != null;
    }

    public Pointer getSelectionFrom() {
        int comparison = cursorPosition.compareTo(selectionEnd);

        Pointer leftMostPointer = comparison < 0 ? cursorPosition : selectionEnd;

        return new Pointer(leftMostPointer);
    }

    public Pointer getSelectionTo() {
        int comparison = cursorPosition.compareTo(selectionEnd);

        Pointer rightMostPointerExcluded = comparison > 0 ? cursorPosition : selectionEnd;

        return new Pointer(rightMostPointerExcluded.row, rightMostPointerExcluded.column);
    }

    private StringBuilder getCurrentRow() {
        return lineBuilders.get(cursorPosition.row);
    }

    private int getCurrentRowLength() {
        return getCurrentRow().length();
    }

    public String convertToString(List<StringBuilder> list) {
        return list.stream().collect(Collectors.joining(SystemConstants.NEW_LINE));
    }

    public List<StringBuilder> getSelectedText() {
        Pointer from = getSelectionFrom();
        Pointer to = getSelectionTo();

        if (from.equals(to)) {
            return null;
        }

        StringBuilder startingRow = lineBuilders.get(from.row);
        StringBuilder endingRow = lineBuilders.get(to.row);
        List<StringBuilder> resultList = new ArrayList<>();

        if (from.row == to.row) {
            resultList.add(new StringBuilder(startingRow.substring(from.column, to.column)));
        } else {
            resultList.add(new StringBuilder(startingRow.substring(from.column, Math.max(from.column, startingRow.length()))));

            for (int i = from.row + 1; i < to.row; i++) {
                resultList.add(lineBuilders.get(i));
            }
            resultList.add(new StringBuilder(endingRow.substring(0, to.column)));

        }
        return resultList;
    }
}
