package com.editor;

import com.editor.model.RopeTextEditorModel;
import com.editor.model.rope.Rope;
import com.editor.model.undo.UndoRedoService;
import com.editor.system.ClipboardAdapter;
import javafx.geometry.VerticalDirection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Process all external actions in tex area
 */

public class TextActionMap extends ActionMap {
    private final ClipboardAdapter clipboardAdapter;
    private RopeTextEditorModel model;
    private TextArea textArea;
    private UndoRedoService undoService;

    public TextActionMap(RopeTextEditorModel model, TextArea area, UndoRedoService undoRedoService) {
        this.model = model;
        this.textArea = area;
        this.clipboardAdapter = new ClipboardAdapter();
        this.undoService = undoRedoService;
    }

    {
        for (char i = ' '; i <= '~'; i++) {
            put(Character.toString(i), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    char[] chars = e.getActionCommand().toCharArray();
                    model.onTextInput(chars);
                    undoService.pushState();
                    textArea.render();
                }
            });
        }

        put(TextInputMap.DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.onBackspace();
                undoService.pushState();
                textArea.render();
            }
        });

        put(TextInputMap.NEW_LINE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.onEnter();
                undoService.pushState();
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
                boolean scrollUp = model.movePointerUp(true);
                if (scrollUp) {
                    scrollOnLine(VerticalDirection.UP);
                }

                textArea.render(true);
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
                boolean scrollDown = model.movePointerDown(true);
                if (scrollDown) {
                    scrollOnLine(VerticalDirection.DOWN);
                }
                textArea.render(true);
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
                model.onTextInput(clipboardAdapter.getText().toCharArray());
                undoService.pushState();
                textArea.render();
            }
        });

        put(TextInputMap.CTRL_C, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedText = model.getSelectedText();
                clipboardAdapter.setText(selectedText);
                textArea.render();
            }
        });

        put(TextInputMap.INIT_POSITION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToInitPosition(true);

                // Moving to not-rendered position requires more work
                model.moveCursorRectTo(new Point(0, 0));

                textArea.render();
            }
        });

        put(TextInputMap.LAST_POSITION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToLastPosition(true);

                // Moving to not-rendered position requires more work
                Rope rope = model.getRope();
                int lastLineIndex = rope.lineAtChar(rope.getLength() - 1);
                int fontHeight = textArea.ropeDrawComponent.getLatestFontHeight();
                model.moveCursorRectTo(new Point(0, lastLineIndex * fontHeight));
                textArea.render();

                SwingUtilities.invokeLater(() -> {
                    // It is hard to determine the x coordinate of last symbol. That is the easiest way.
                    model.movePointerToTheEndOfLine(true);
                    textArea.render();
                });
            }
        });

        put(TextInputMap.LINE_END, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToTheEndOfLine(true);
                textArea.render();
            }
        });

        put(TextInputMap.LINE_START, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToStartOfLine(true);
                textArea.render();
            }
        });

        put(TextInputMap.CTRL_Z, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoService.undo();
                textArea.render();
            }
        });

        put(TextInputMap.CTRL_K, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoService.redo();
                textArea.render();
            }
        });

        put(TextInputMap.CTRL_A, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToLastPosition(true);
                model.setSelectionEnd(0);
                textArea.render();
            }
        });
    }

    private void scrollOnLine(VerticalDirection direction) {
        RopeDrawComponent ropeDrawComponent = this.textArea.ropeDrawComponent;
        Rectangle cursorRect = model.getCursorRect();
        int charHeight = ropeDrawComponent.getLatestFontHeight();

        int newY = direction == VerticalDirection.DOWN
                ? cursorRect.y + charHeight
                : cursorRect.y - charHeight;

        model.setCursorRect(new Rectangle(
                cursorRect.x,
                newY,
                cursorRect.width,
                cursorRect.height));

        ropeDrawComponent.scrollRectToVisible(model.getCursorRect());
    }
}