package com.editor.parser.keywords;

/**
 * Store info about start and end indexes if paired brackets
 */
public class PairedBracketsInfo {
    private int startInd;
    private int endInd;
    private TokenType tokenType;

    public PairedBracketsInfo(int startInd) {
        this.startInd = startInd;
        this.tokenType = TokenType.BRACKET;
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

    public void setEndInd(int endInd) {
        this.endInd = endInd;
    }
}
