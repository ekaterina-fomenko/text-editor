package com.editor.parser;

public class BracketModel {
    private int rowFirstBracket;
    private int firstIndex;
    private int secondIndex;
    private int rowSecondBracket;

    public BracketModel(int rowFirstBracket, int firstIndex, int secondIndex){
      this(rowFirstBracket, firstIndex, secondIndex, -1);
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

    public boolean isPaired() {
        return firstIndex != -1 && secondIndex != -1;
    }

    public int getRowSecondBracket() {
        return rowSecondBracket;
    }

    public void setSecondIndex(int secondIndex) {
        this.secondIndex = secondIndex;
    }

    public void setRowSecondBracket(int rowSecondBracket) {
        this.rowSecondBracket = rowSecondBracket;
    }


}
