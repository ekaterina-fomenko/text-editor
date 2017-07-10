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
                    textArea.stringBuilder.append(e.getActionCommand());
                    textArea.jComponent.repaint();
                }
            });
        }
        //ToDo: Move to string constant
        put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int length = textArea.stringBuilder.length();
                textArea.stringBuilder.deleteCharAt(length - 1);
                textArea.jComponent.repaint();
            }
        });
        //Doesn't work for now
        put("newLine", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.stringBuilder.append(e.getActionCommand());
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
                    textArea.pointer.printChars = true;
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
                    textArea.pointer.printChars = true;
                }
            }
        });


    }
}
