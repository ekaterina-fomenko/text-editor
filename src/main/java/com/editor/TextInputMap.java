package main.java.com.editor;

import javax.swing.*;

public class TextInputMap extends InputMap{
    {
        for(char i = ' '; i <='~';i++ ){
            put(KeyStroke.getKeyStroke(i), Character.toString(i));
        }
    }
}
