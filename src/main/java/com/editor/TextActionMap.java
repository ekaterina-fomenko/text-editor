package main.java.com.editor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TextActionMap extends ActionMap {
    private TextArea textArea;

    public TextActionMap(TextArea textArea) {
        this.textArea = textArea;
    }

    {
        for (char i = ' '; i<='~';i++){
            put(Character.toString(i), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                 textArea.stringBuilder = textArea.stringBuilder.append(e.getActionCommand());
                    textArea.jComponent.repaint();
                }
            });
        }
    }
}
