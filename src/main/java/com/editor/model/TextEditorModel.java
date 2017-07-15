package main.java.com.editor.model;

import java.util.ArrayList;
import java.util.List;

public class TextEditorModel {
    private Pointer pointer;
    private ArrayList<StringBuilder> lineBuilders;
    private List<Integer> lineLengthsList;

    public TextEditorModel() {
        this.pointer = new Pointer();
        this.lineBuilders = new ArrayList<>();
        this.lineBuilders.add(new StringBuilder());

        this.lineLengthsList = new ArrayList<>();
        this.lineLengthsList.add(0);
    }

    public void addText(String text) {
        this.lineBuilders.get(pointer.row).insert(pointer.column, text);
        pointer.column += text.length();
    }

    public void addNewLine() {
        StringBuilder currentRow = this.lineBuilders.get(pointer.row);
        String restOfCurrentLine = currentRow.substring(pointer.column);

        currentRow.delete(pointer.column, currentRow.length());
        lineBuilders.add(pointer.row + 1, new StringBuilder(restOfCurrentLine));
        pointer.row++;
        pointer.column = 0;
    }

    public void deleteText(Pointer from, Pointer to) {
        // TODO: selection + escape
    }

    public void deleteChar() {
        if (pointer.isStart()) {
            return;
        }

        if (pointer.column > 0) {
            pointer.column--;

            getCurrentRow().deleteCharAt(pointer.column);
        } else {
            StringBuilder oldRow = getCurrentRow();

            lineBuilders.remove(pointer.row);

            if (pointer.row > 0) {
                pointer.row--;
            }

            StringBuilder newRow = getCurrentRow();

            newRow.append(oldRow);

            pointer.column = newRow.length() - oldRow.length();
        }

    }

    public Pointer getPointer() {
        return pointer;
    }

    public ArrayList<StringBuilder> getLineBuilders() {
        return lineBuilders;
    }

    public void movePointerRight() {
        pointer.column++;

        int currentRowLength = getCurrentRowLength();
        if (pointer.column > currentRowLength) {
            if (pointer.row == lineBuilders.size() - 1) {
                pointer.column = currentRowLength;
            } else {
                pointer.column = 0;
                pointer.row++;
            }
        }
    }

    public void movePointerLeft() {
        pointer.column--;
        if (pointer.column < 0) {
            if (pointer.row != 0) {
                pointer.row--;
                pointer.column = getCurrentRowLength();
            } else {
                pointer.column = 0;
            }
        }
    }

    public void movePointerUp() {
        if (pointer.row == 0) {
            return;
        }

        pointer.row--;
        pointer.column = Math.min(pointer.column, getCurrentRowLength());
    }


    public void movePointerDown() {
        if (pointer.row == lineBuilders.size() - 1) {
            return;
        }

        pointer.row++;
        pointer.column = Math.min(pointer.column, getCurrentRowLength());
    }

    private StringBuilder getCurrentRow() {
        return lineBuilders.get(pointer.row);
    }

    private int getCurrentRowLength() {
        return getCurrentRow().length();
    }
}
