package com.editor;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class TextInputMap extends InputMap {

    public static final String RIGHT_SHIFT = "right+SHIFT";

    public static final String LEFT_SHIFT = "leftSHIFT";

    public static final String UP_SHIFT = "up+SHIFT";

    public static final String DOWN_SHIFT = "down+SHIFT";

    public static final String DOWN = "down";

    public static final String UP = "up";

    public static final String LEFT = "left";

    public static final String RIGHT = "right";

    public static final String NEW_LINE = "newLine";

    public static final String DELETE = "delete";

    public static final String CTRL_C = "CTRL_C";

    public static final String CTRL_V = "CTRL_V";

    {
        for (char i = ' '; i <= '~'; i++) {
            put(KeyStroke.getKeyStroke(i), Character.toString(i));
        }
        put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), DELETE);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), NEW_LINE);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_DOWN_MASK), RIGHT_SHIFT);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.SHIFT_DOWN_MASK), LEFT_SHIFT);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), UP);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK), UP_SHIFT);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), DOWN);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK), DOWN_SHIFT);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK), CTRL_C);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK), CTRL_V);
    }
}
