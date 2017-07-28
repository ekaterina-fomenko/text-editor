package com.editor.model;

/**
 * This class provides current position of cursor
 */

public class Pointer implements Comparable<Pointer> {
    public int row;
    public int column;

    public Pointer(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Pointer(Pointer pointer) {
        this(pointer.row, pointer.column);
    }

    public Pointer() {
        this(0, 0);
    }

    @Override
    public int compareTo(Pointer obj) {
        int rowsCompare = Integer.compare(row, obj.row);
        int colsCompare = Integer.compare(column, obj.column);

        return rowsCompare == 0 ? colsCompare : rowsCompare;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pointer pointer = (Pointer) o;

        if (column != pointer.column) return false;
        if (row != pointer.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public String toString() {
        return "Pointer{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
