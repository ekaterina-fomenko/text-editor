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
                    textArea.stringBuilder.insert(textArea.pointer.index, e.getActionCommand());
                    textArea.pointer.index++;
                    textArea.pointer.column++;
                    textArea.newLineIndexesMap.put(textArea.pointer.row, textArea.pointer.column);
                    textArea.jComponent.repaint();
                }
            });
        }
        //ToDo: Move to string constant
        put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.pointer.index != 0) {
                    textArea.stringBuilder.deleteCharAt(textArea.pointer.index - 1);
                    textArea.pointer.index--;
                    textArea.jComponent.repaint();
                }
            }
        });
        put("newLine", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.stringBuilder.insert(textArea.pointer.index, e.getActionCommand());
                textArea.pointer.index++;
                textArea.pointer.column = 0;
                textArea.pointer.row++;
                textArea.newLineIndexesMap.put(textArea.pointer.row, textArea.pointer.column);
                textArea.jComponent.repaint();
            }
        });

        put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.pointer.index < textArea.stringBuilder.length()) {
                    textArea.pointer.index++;
                    textArea.jComponent.repaint();
                }
            }
        });
        put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.pointer.index > 0) {
                    textArea.pointer.index--;
                    textArea.pointer.column--;
                    if (textArea.pointer.column < 0 && textArea.pointer.row > 0) {
                        textArea.pointer.row--;
                        textArea.pointer.column = textArea.newLineIndexesMap.get(textArea.pointer.row) - 1;
                    }
                    textArea.jComponent.repaint();
                }
            }
        });

        put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.newLineIndexesMap.containsKey(textArea.pointer.row - 1)) {
                    if (textArea.pointer.column >= textArea.newLineIndexesMap.get(textArea.pointer.row - 1)) {
                        while (textArea.pointer.column > 0) {
                            textArea.pointer.column--;
                            textArea.pointer.index--;
                        }
                        textArea.pointer.index--;
                        textArea.pointer.column = textArea.newLineIndexesMap.get(textArea.pointer.row - 1) - 1;
                    } else {
                        textArea.pointer.index = textArea.pointer.index - textArea.newLineIndexesMap.get(textArea.pointer.row - 1) - 1;
                        //textArea.pointer.column = textArea.newLineIndexesMap.get(textArea.pointer.row - 1) - 1;
                    }
                    textArea.pointer.row--;
                    textArea.jComponent.repaint();
                }
            }
        });
        put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.newLineIndexesMap.containsKey(textArea.pointer.row + 1)) {
                    if (textArea.pointer.column >= textArea.newLineIndexesMap.get(textArea.pointer.row + 1)) {
                        while (textArea.pointer.column < textArea.newLineIndexesMap.get(textArea.pointer.row) - 1) {
                            textArea.pointer.column++;
                            textArea.pointer.index++;
                        }
                        textArea.pointer.index += textArea.newLineIndexesMap.get(textArea.pointer.row + 1) + 1;
                        textArea.pointer.column = textArea.newLineIndexesMap.get(textArea.pointer.row + 1);
                    } else {
                        textArea.pointer.index = textArea.pointer.index + textArea.newLineIndexesMap.get(textArea.pointer.row) + 1;
                    }
                    textArea.pointer.row++;
                    textArea.jComponent.repaint();
                }
            }
        });
    }
}
