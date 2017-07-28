package com.editor;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Contains all actions that will be processed in TextActionMap
 */

public class TextInputMap extends InputMap {

    public static final String RIGHT_SHIFT = "SHIFT+right";

    public static final String LEFT_SHIFT = "SHIFT+left";

    public static final String UP_SHIFT = "SHIFT+up";

    public static final String DOWN_SHIFT = "SHIFT+down";

    public static final String DOWN = "down";

    public static final String UP = "up";

    public static final String LEFT = "left";

    public static final String RIGHT = "right";

    public static final String NEW_LINE = "newLine";

    public static final String DELETE = "delete";

    public static final String CTRL_C = "CTRL+c";

    public static final String CTRL_V = "CTRL+v";

    public static final String INIT_POSITION = "ALT+up";

    public static final String LAST_POSITION = "ALT+down";

    public static final String LINE_END = "ALT+right";

    public static final String LINE_START = "ALT+left";

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

        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.ALT_MASK), INIT_POSITION);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.ALT_MASK), LAST_POSITION);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK), LINE_END);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK), LINE_START);
    }
}
