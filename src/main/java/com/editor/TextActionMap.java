package main.java.com.editor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

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
                    Pointer pointer = textArea.pointer;
                    List<Integer> lineLengthsList = textArea.lineLengthsList;
                    int length = lineLengthsList.get(pointer.row);

                    textArea.stringBuilder.insert(pointer.index, e.getActionCommand());
                    pointer.index++;
                    pointer.column++;
                    System.out.println("index: " + pointer.index + "column: " + pointer.column);
                    lineLengthsList.set(pointer.row, length + 1);
                    textArea.jComponent.repaint();
                }
            });
        }
        //ToDo: Move to string constant
        put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("delete");

                Pointer pointer = textArea.pointer;
                List<Integer> lineLengthsList = textArea.lineLengthsList;
                int currentLineLength = lineLengthsList.get(pointer.row);

                if (pointer.index == 0) {
                    return;
                }

                textArea.stringBuilder.deleteCharAt(pointer.index - 1);
                pointer.index--;

                lineLengthsList.set(pointer.row, currentLineLength - 1);

                pointer.column--;

                if (pointer.column < 0 && pointer.row > 0) { //start of line
                    int prevRowLength = lineLengthsList.get(pointer.row - 1);

                    lineLengthsList.set(pointer.row - 1, currentLineLength + prevRowLength);
                    lineLengthsList.remove(pointer.row);

                    pointer.column = prevRowLength;
                    pointer.row--;
                }

                textArea.jComponent.repaint();
            }
        });
        put("newLine", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("nw line");
                Pointer pointer = textArea.pointer;
                List<Integer> lineLengthsList = textArea.lineLengthsList;

                int currentLineLength = lineLengthsList.get(pointer.row);
                int currentLineNewLength = pointer.column;
                int nextLineNewLength = currentLineLength - currentLineNewLength;

                textArea.stringBuilder.insert(pointer.index, e.getActionCommand());
                pointer.index++;
                pointer.column = 0;
                pointer.row++;
                lineLengthsList.add(pointer.row, nextLineNewLength);
                lineLengthsList.set(pointer.row - 1, currentLineNewLength);
                textArea.jComponent.repaint();
            }
        });

        put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("inside right");
                Pointer pointer = textArea.pointer;
                if (pointer.index < textArea.stringBuilder.length()) {
                    pointer.index++;
                    pointer.column++;
                    if (pointer.column > textArea.lineLengthsList.get(pointer.row)) { //end of line
                        pointer.column = 0;
                        pointer.row++;
                    }
                    textArea.jComponent.repaint();
                }
            }
        });
        put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("inside left");
                Pointer pointer = textArea.pointer;
                if (pointer.index > 0) {
                    pointer.index--;
                    pointer.column--;
                    System.out.println("c1 " + pointer.column);
                    System.out.println("r1 " + pointer.row);
                    if (pointer.column < 0 && pointer.row > 0) { //start of line
                        pointer.row--;
                        pointer.column = textArea.lineLengthsList.get(pointer.row);
                    }
                    System.out.println("c2 " + pointer.column);
                    System.out.println("r2 " + pointer.row);
                    textArea.jComponent.repaint();
                }
            }
        });

        put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("up");
                Pointer pointer = textArea.pointer;

                if (pointer.row > 0) {
                    Integer prevLineLength = textArea.lineLengthsList.get(pointer.row - 1);

                    if (pointer.column > prevLineLength) {
                        pointer.index -= pointer.column + 1;
                        pointer.column = prevLineLength;
                    } else {
                        pointer.index -= prevLineLength + 1;
                    }

                    pointer.row--;
                    textArea.jComponent.repaint();
                }
            }
        });
        put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("down");
                List<Integer> lineLengthsList = textArea.lineLengthsList;
                for (int f : lineLengthsList) {
                    System.out.println("keyset " + f);
                }
                Pointer areaPointer = textArea.pointer;
                if (areaPointer.row < lineLengthsList.size() - 1) {
                    Integer nextLineLength = lineLengthsList.get(areaPointer.row + 1);
                    Integer currentLineLength = lineLengthsList.get(areaPointer.row);

                    if (areaPointer.column > nextLineLength) {
                        areaPointer.index += currentLineLength - areaPointer.column + nextLineLength + 1;//because '\n' is symbol too
                        areaPointer.column = nextLineLength;
                    } else {
                        areaPointer.index = areaPointer.index + currentLineLength + 1;
                    }

                    System.out.println("down ind " + areaPointer.index);
                    System.out.println("down col " + areaPointer.column);
                    areaPointer.row++;
                    textArea.jComponent.repaint();
                }
            }
        });
    }
}
