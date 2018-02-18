package com.editor.parser.keywords;

public class BracketInfo {
    private char ch;
    private int startInd;
    private int endInd;
    private TokenType tokenType = TokenType.BRACKET;

    public BracketInfo(char ch, int startInd) {
        this.ch = ch;
        this.startInd = startInd;
    }
//todo: remove set char
    public char getCh() {
        return ch;
    }

    public int getStartInd() {
        return startInd;
    }

    public int getEndInd() {
        return endInd;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setStartInd(int startInd) {
        this.startInd = startInd;
    }

    public void setEndInd(int endInd) {
        this.endInd = endInd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BracketInfo)) return false;

        BracketInfo charInfo = (BracketInfo) o;

        if (ch != charInfo.ch) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) ch;
    }
}
