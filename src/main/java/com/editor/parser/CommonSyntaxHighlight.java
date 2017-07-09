package main.java.com.editor.parser;

import java.awt.*;
/**
 * This class helps to set the start, end and the color for highlight
 */
public class CommonSyntaxHighlight {
    private int startIndex;
    private int endIndex;
    private Color startColor;


    public CommonSyntaxHighlight(int startIndex, Color startColor, int endIndex) {
        this.startIndex = startIndex;
        this.startColor = startColor;
        this.endIndex = endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public Color getStartColor() {
        return startColor;
    }
}
