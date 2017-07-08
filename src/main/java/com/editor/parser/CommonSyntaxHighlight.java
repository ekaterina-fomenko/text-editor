package main.java.com.editor.parser;

import java.awt.*;
/**
 * This class helps to set the start and the color for highlight
 */
public class CommonSyntaxHighlight {
    public int startIndex;
    public Color color;

    public CommonSyntaxHighlight(int startIndex, Color color) {
        this.startIndex = startIndex;
        this.color = color;
    }
}
