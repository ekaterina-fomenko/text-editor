package main.java.com.editor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TextActionMap extends ActionMap {
    private TextArea textArea;

    public TextActionMap(TextArea textArea) {
        this.textArea = textArea;
    }

    {
        for (char i = ' '; i <= '~'; i++) {
            put(Character.toString(i), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textArea.stringBuilder.insert(textArea.pointer.column, e.getActionCommand());
                    textArea.pointer.column++;
                    textArea.jComponent.repaint();
                }
            });
        }
        //ToDo: Move to string constant
        put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.pointer.column != 0) {
                    textArea.stringBuilder.deleteCharAt(textArea.pointer.column - 1);
                    textArea.pointer.column--;
                    textArea.jComponent.repaint();
                }
            }
        });
        //ToDo: fix - Doesn't work for now
        put("newLine", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.stringBuilder.insert(textArea.pointer.column, e.getActionCommand());
                textArea.pointer.column++;
                textArea.jComponent.repaint();
            }
        });

        put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.pointer.column < textArea.stringBuilder.length()) {
                    textArea.pointer.printChars = false;
                    textArea.pointer.prevChar = textArea.stringBuilder.charAt(textArea.pointer.column);
                    textArea.pointer.column++;
                    textArea.jComponent.repaint();
                }
            }
        });

        put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.pointer.column > 0) {
                    textArea.pointer.printChars = false;
                    textArea.pointer.column--;
                    textArea.pointer.prevChar = textArea.stringBuilder.charAt(textArea.pointer.column);
                    textArea.jComponent.repaint();
                }
            }
        });


    }
}
