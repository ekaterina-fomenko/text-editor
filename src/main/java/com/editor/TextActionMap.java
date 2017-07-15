package main.java.com.editor;

import main.java.com.editor.model.Pointer;
import main.java.com.editor.model.TextEditorModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TextActionMap extends ActionMap {
    private TextEditorModel model;
    private TextArea textArea;

    private Pointer pointer;
    private StringBuilder textBuilder;
    private List<Integer> lineLengthsList;

    public TextActionMap(TextEditorModel model, TextArea area) {
        this.model = model;
        this.textArea = area;
        this.pointer = model.getPointer();
        this.textBuilder = model.getTextBuilder();
        this.lineLengthsList = model.getLineLengthsList();
    }

    {
        for (char i = ' '; i <= '~'; i++) {
            put(Character.toString(i), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Pointer pointer = model.getPointer();
                    List<Integer> lineLengthsList = model.getLineLengthsList();
                    int length = lineLengthsList.get(pointer.row);

                    model.addText(pointer.index, e.getActionCommand());
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

                Pointer pointer = model.getPointer();
                int currentLineLength = lineLengthsList.get(pointer.row);

                if (pointer.index == 0) {
                    return;
                }

                textBuilder.deleteCharAt(pointer.index - 1);
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
                int currentLineLength = lineLengthsList.get(pointer.row);
                int currentLineNewLength = pointer.column;
                int nextLineNewLength = currentLineLength - currentLineNewLength;

                model.addText(pointer.index, e.getActionCommand());
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
                if (pointer.index < textBuilder.length()) {
                    pointer.index++;
                    pointer.column++;
                    if (pointer.column > lineLengthsList.get(pointer.row)) { //end of line
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
                if (pointer.index > 0) {
                    pointer.index--;
                    pointer.column--;
                    System.out.println("c1 " + pointer.column);
                    System.out.println("r1 " + pointer.row);
                    if (pointer.column < 0 && pointer.row > 0) { //start of line
                        pointer.row--;
                        pointer.column = lineLengthsList.get(pointer.row);
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
                if (pointer.row > 0) {
                    Integer prevLineLength = lineLengthsList.get(pointer.row - 1);

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
                for (int f : lineLengthsList) {
                    System.out.println("keyset " + f);
                }
                if (pointer.row < lineLengthsList.size() - 1) {
                    Integer nextLineLength = lineLengthsList.get(pointer.row + 1);
                    Integer currentLineLength = lineLengthsList.get(pointer.row);

                    if (pointer.column > nextLineLength) {
                        pointer.index += currentLineLength - pointer.column + nextLineLength + 1;//because '\n' is symbol too
                        pointer.column = nextLineLength;
                    } else {
                        pointer.index = pointer.index + currentLineLength + 1;
                    }

                    System.out.println("down ind " + pointer.index);
                    System.out.println("down col " + pointer.column);
                    pointer.row++;
                    textArea.jComponent.repaint();
                }
            }
        });
    }
}
