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
                    System.out.println("index: " + textArea.pointer.index + "column: " + textArea.pointer.column);
                    int length = textArea.lineLengthsList.get(textArea.pointer.row);
                    textArea.lineLengthsList.set(textArea.pointer.row, length + 1);
                    textArea.jComponent.repaint();
                }
            });
        }
        //ToDo: Move to string constant
        put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("delete");
                if (textArea.pointer.index != 0) {
                    textArea.stringBuilder.deleteCharAt(textArea.pointer.index - 1);
                    textArea.pointer.index--;
                    textArea.pointer.column--;
                    int length = textArea.lineLengthsList.get(textArea.pointer.row);
                    textArea.lineLengthsList.set(textArea.pointer.row, length - 1);
                    if (textArea.pointer.column < 0 && textArea.pointer.row > 0) { //start of line
                        textArea.pointer.row--;
                        int lengthPrevRow = textArea.lineLengthsList.get(textArea.pointer.row);
                        textArea.lineLengthsList.set(textArea.pointer.row, length + lengthPrevRow);
                        textArea.pointer.column = lengthPrevRow;
                        textArea.lineLengthsList.remove(textArea.pointer.row + 1);
                        // textArea.pointer.column = textArea.newLineIndexesMap.get(textArea.pointer.row);
                    }
                    textArea.jComponent.repaint();
                }
            }
        });
        put("newLine", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("nw line");
                textArea.stringBuilder.insert(textArea.pointer.index, e.getActionCommand());
                int length = textArea.lineLengthsList.get(textArea.pointer.row);
                int newLength = length - textArea.pointer.column;
                int currentLength = textArea.pointer.column;
                textArea.pointer.index++;
                textArea.pointer.column = 0;
                textArea.pointer.row++;
                textArea.lineLengthsList.add(textArea.pointer.row, newLength);
                textArea.lineLengthsList.set(textArea.pointer.row - 1, currentLength);
                textArea.jComponent.repaint();
            }
        });

        put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("inside right");
                if (textArea.pointer.index < textArea.stringBuilder.length()) {
                    textArea.pointer.index++;
                    textArea.pointer.column++;
                    if (textArea.pointer.column > textArea.lineLengthsList.get(textArea.pointer.row)) { //end of line
                        textArea.pointer.column = 0;
                        textArea.pointer.row++;
                    }
                    textArea.jComponent.repaint();
                }
            }
        });
        put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("inside left");
                if (textArea.pointer.index > 0) {
                    textArea.pointer.index--;
                    textArea.pointer.column--;
                    System.out.println("c1 " + textArea.pointer.column);
                    System.out.println("r1 " + textArea.pointer.row);
                    if (textArea.pointer.column < 0 && textArea.pointer.row > 0) { //start of line
                        textArea.pointer.row--;
                        textArea.pointer.column = textArea.lineLengthsList.get(textArea.pointer.row);
                    }
                    System.out.println("c2 " + textArea.pointer.column);
                    System.out.println("r2 " + textArea.pointer.row);
                    textArea.jComponent.repaint();
                }
            }
        });

        put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("up");
                if (textArea.pointer.row > 0) {
                    if (textArea.pointer.column > textArea.lineLengthsList.get(textArea.pointer.row - 1)) {
                        //ToDo: index-=column
                        while (textArea.pointer.column > 0) {
                            textArea.pointer.column--;
                            textArea.pointer.index--;
                        }
                        textArea.pointer.index--;
                        textArea.pointer.column = textArea.lineLengthsList.get(textArea.pointer.row - 1);
                    } else {
                        textArea.pointer.index = textArea.pointer.index - textArea.lineLengthsList.get(textArea.pointer.row - 1) - 1;
                    }
                    textArea.pointer.row--;
                    textArea.jComponent.repaint();
                }
            }
        });
        put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("down");
                for (int f : textArea.lineLengthsList) {
                    System.out.println("keyset " + f);
                }
                if (textArea.pointer.row < textArea.lineLengthsList.size() - 1) {
                    if (textArea.pointer.column > textArea.lineLengthsList.get(textArea.pointer.row + 1)) {
                        //ToDo: remove while
                        while (textArea.pointer.column != textArea.lineLengthsList.get(textArea.pointer.row)) {
                            textArea.pointer.column++;
                            textArea.pointer.index++;
                        }
                        textArea.pointer.index += textArea.lineLengthsList.get(textArea.pointer.row + 1) + 1;//because '\n' is symbol too
                        textArea.pointer.column = textArea.lineLengthsList.get(textArea.pointer.row + 1);
                    } else {
                        textArea.pointer.index = textArea.pointer.index + textArea.lineLengthsList.get(textArea.pointer.row) + 1;
                    }
                    System.out.println("down ind " + textArea.pointer.index);
                    System.out.println("down col " + textArea.pointer.column);
                    textArea.pointer.row++;
                    textArea.jComponent.repaint();
                }
            }
        });
    }
}
