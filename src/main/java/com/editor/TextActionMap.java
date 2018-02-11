package com.editor;

import com.editor.model.RopeTextEditorModel;
import com.editor.model.TextEditorModel;
import com.editor.system.ClipboardAdapter;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Process all external actions in tex area
 */

public class TextActionMap extends ActionMap {
    private final ClipboardAdapter clipboardAdapter;
    private RopeTextEditorModel model;
    private TextArea textArea;

    public TextActionMap(RopeTextEditorModel model, TextArea area) {

        this.model = model;
        this.textArea = area;
        this.clipboardAdapter = new ClipboardAdapter();

    }

    {
        for (char i = ' '; i <= '~'; i++) {
            put(Character.toString(i), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    char[] chars = e.getActionCommand().toCharArray();
                    model.insertToPointer(chars);
                    model.setCursorPosition(model.getCursorPosition() + chars.length);
                    textArea.render();
                }
            });
        }

        put(TextInputMap.DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.onBackspace();
                textArea.render();
            }
        });

        put(TextInputMap.NEW_LINE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addNewLine();
                textArea.render();
            }
        });

        put(TextInputMap.RIGHT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerRight(true);
                textArea.render();
            }
        });

        put(TextInputMap.RIGHT_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerRight(false);
                textArea.render();
            }
        });

        put(TextInputMap.LEFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerLeft(true);
                textArea.render();
            }
        });

        put(TextInputMap.LEFT_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerLeft(false);
                textArea.render();
            }
        });

        put(TextInputMap.UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerUp(true);
                textArea.render();
            }
        });

        put(TextInputMap.UP_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerUp(false);
                textArea.render();
            }
        });

        put(TextInputMap.DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerDown(true);
                textArea.render();
            }
        });

        put(TextInputMap.DOWN_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerDown(false);
                textArea.render();
            }
        });

        put(TextInputMap.CTRL_V, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addText(clipboardAdapter.getText().toCharArray());
                textArea.render();
            }
        });

        put(TextInputMap.CTRL_C, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedText = model.convertToString(model.getSelectedText());
                clipboardAdapter.setText(selectedText);
                textArea.render();
            }
        });

        put(TextInputMap.INIT_POSITION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToInitPosition();
                textArea.render();
            }
        });

        put(TextInputMap.LAST_POSITION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToLastPosition();
                textArea.render();
            }
        });

        put(TextInputMap.LINE_END, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToTheEndOfLine();
                textArea.render();
            }
        });

        put(TextInputMap.LINE_START, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                model.movePointerToStartOfLine();
//                textArea.render();
            }
        });
    }
}