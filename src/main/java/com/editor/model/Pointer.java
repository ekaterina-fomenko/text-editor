package main.java.com.editor.model;

public class Pointer {
    public int row;
    public int column;

    public Pointer(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Pointer() {
        this(0, 0);
    }

    public boolean isStart() {
        return row == 0 && column == 0;
    }
}
