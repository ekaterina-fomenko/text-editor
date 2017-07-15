package main.java.com.editor;

import main.java.com.editor.model.TextEditorModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TextActionMap extends ActionMap {
    private TextEditorModel model;
    private TextArea textArea;

    public TextActionMap(TextEditorModel model, TextArea area) {
        this.model = model;
        this.textArea = area;
    }

    {
        for (char i = ' '; i <= '~'; i++) {
            put(Character.toString(i), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.addText(e.getActionCommand());
                    textArea.jComponent.repaint();
                }
            });
        }
        //ToDo: Move to string constant
        put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.deleteChar();
                textArea.jComponent.repaint();
            }
        });
        put("newLine", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addNewLine();
                textArea.jComponent.repaint();
            }
        });

        put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerRight();
                textArea.jComponent.repaint();
            }
        });
        put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("inside left");
                model.movePointerLeft();
                textArea.jComponent.repaint();
            }
        });

        put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("up");
                model.movePointerUp();
                textArea.jComponent.repaint();
            }
        });
        put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("down");
                model.movePointerDown();
                textArea.jComponent.repaint();
            }
        });
    }
}
