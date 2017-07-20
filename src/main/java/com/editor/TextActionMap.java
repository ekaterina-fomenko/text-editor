package com.editor;

import com.editor.model.TextEditorModel;
import com.editor.parser.Syntax;
import com.editor.parser.SyntaxParser;
import com.editor.system.ClipboardAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TextActionMap extends ActionMap {
    private final ClipboardAdapter clipboardAdapter;
    private TextEditorModel model;
    private TextArea textArea;

    public TextActionMap(TextEditorModel model, TextArea area) {
        this.model = model;
        this.textArea = area;
        this.clipboardAdapter = new ClipboardAdapter();
    }

    {
        for (char i = ' '; i <= '~'; i++) {
            put(Character.toString(i), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.addText(e.getActionCommand());
                    render();
                }
            });
        }

        put(TextInputMap.DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.onBackspace();
                render();
            }
        });

        put(TextInputMap.NEW_LINE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addNewLine();
                render();
            }
        });

        put(TextInputMap.RIGHT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerRight(true);
                render();
            }
        });

        put(TextInputMap.RIGHT_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerRight(false);
                render();
            }
        });

        put(TextInputMap.LEFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("inside left");
                model.movePointerLeft(true);
                render();
            }
        });

        put(TextInputMap.LEFT_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("inside left");
                model.startOrContinueSelection();
                //model.startOrContinueSelection();
                model.movePointerLeft(false);
                render();
            }
        });

        put(TextInputMap.UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("up");
                model.movePointerUp(true);
                render();
            }
        });

        put(TextInputMap.UP_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("up");
                model.startOrContinueSelection();
                model.movePointerUp(false);
                render();
            }
        });

        put(TextInputMap.DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("down");
                model.movePointerDown(true);
                render();
            }
        });

        put(TextInputMap.DOWN_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("down");
                model.startOrContinueSelection();
                model.movePointerDown(false);
                render();
            }
        });

        put(TextInputMap.CTRL_V, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("!!!" + model.getCursorPosition().column + " " + model.getCursorPosition().row);
                model.addText(clipboardAdapter.getText());
                System.out.println(model.getCursorPosition().column + " " + model.getCursorPosition().row);
                System.out.println(model.isSelectionInProgress());
                render();

            }
        });

        put(TextInputMap.CTRL_C, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedText = model.convertToString(model.getSelectedText());
                clipboardAdapter.setText(selectedText);
                render();
            }
        });
    }

    private void render() {
        DrawComponent jComponent = textArea.jComponent;
        jComponent.repaint();
        jComponent.revalidate();
        jComponent.scrollToPointer();
        if (Syntax.TEXT != SyntaxParser.getCurrentSyntax()) {
            model.updatePairedBrackets();
        }
    }
}
