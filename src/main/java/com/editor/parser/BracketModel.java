package com.editor.parser;

public class BracketModel {
    private int rowFirstBracket;
    private int firstIndex;
    private int secondIndex;
    private int rowSecondBracket;

    public BracketModel(int rowFirstBracket, int firstIndex, int secondIndex){
      new BracketModel(rowFirstBracket, firstIndex, secondIndex, -1);
    }
    public BracketModel(int rowFirstBracket, int firstIndex, int secondIndex, int rowSecondBracket) {
        this.rowFirstBracket = rowFirstBracket;
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        this.rowSecondBracket = rowSecondBracket;
    }

    public int getRowFirstBracket() {
        return rowFirstBracket;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    public int getRowSecondBracket() {
        return rowSecondBracket;
    }

    public void setEndIndex(int endIndex) {
        this.secondIndex = endIndex;
    }

    public void setRowSecondBracket(int rowSecondBracket) {
        this.rowSecondBracket = rowSecondBracket;
    }
}
