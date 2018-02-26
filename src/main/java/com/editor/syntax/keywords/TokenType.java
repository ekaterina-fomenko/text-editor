package com.editor.syntax.keywords;

import java.awt.*;

/**
 *  Store information about type of keywords and also set color for each type
 */
public enum TokenType {
    COMMENT(new Color(138, 43, 226)),
    RESERVED_WORD(new Color(204, 0, 153)),
    BRACKET(new Color(210,105,30)),
    DIGIT(new Color(70,130,180)),
    STRING(new Color(0,128,0));

    private Color color;

    TokenType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
