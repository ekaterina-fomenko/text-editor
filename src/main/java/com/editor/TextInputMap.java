package main.java.com.editor;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class TextInputMap extends InputMap{
    {
        for(char i = ' '; i <='~';i++ ){
            put(KeyStroke.getKeyStroke(i), Character.toString(i));
        }
        put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "delete");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "newLine");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),"right");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),"left");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),"up");
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),"down");
    }
}
