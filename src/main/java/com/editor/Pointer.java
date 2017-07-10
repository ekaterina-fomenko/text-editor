package main.java.com.editor;

public class Pointer {
    public int column;
    public int row;
    public Character prevChar;
    public boolean printChars;

    public Pointer(int column, int row){
        this.column = column;
        this.row = row;
        this.prevChar = null;
        this.printChars =true;
    }
}
