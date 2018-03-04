package com.editor;

import com.editor.model.RopeTextEditorModel;
import com.editor.model.rope.Rope;
import com.editor.model.undo.UndoRedoService;
import com.editor.system.ClipboardAdapter;
import javafx.geometry.VerticalDirection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

/**
 * Process all external actions in tex area
 */

public class TextActionMap extends ActionMap {
    private final ClipboardAdapter clipboardAdapter;
    private final RopeTextEditorModel model;
    private final TextArea textArea;
    private final UndoRedoService undoService;
    private final RopeDrawComponent ropeDrawComponent;

    public TextActionMap(RopeTextEditorModel model,
                         TextArea area,
                         UndoRedoService undoRedoService,
                         ClipboardAdapter clipboardAdapter,
                         RopeDrawComponent ropeDrawComponent) {
        this.model = model;
        this.textArea = area;
        this.clipboardAdapter = clipboardAdapter;
        this.undoService = undoRedoService;
        this.ropeDrawComponent = ropeDrawComponent;
    }

    {
        for (char i = ' '; i <= '~'; i++) {
            put(Character.toString(i), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    char[] chars = e.getActionCommand().toCharArray();
                    model.onTextInput(chars);
                    undoService.pushState();

                    scrollToCursorPositionAndRender();
                }
            });
        }

        put(TextInputMap.DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.onBackspace();
                undoService.pushState();

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.NEW_LINE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.onEnter();
                undoService.pushState();

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.RIGHT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerRight(true);

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.RIGHT_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerRight(false);

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.LEFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerLeft(true);

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.LEFT_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerLeft(false);

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean scrollUp = model.movePointerUp(true);
                if (scrollUp) {
                    scrollOnLine(VerticalDirection.UP);
                }

                scrollToCursorRectAndRender();
            }
        });

        put(TextInputMap.UP_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerUp(false);

                scrollToCursorRectAndRender();
            }
        });

        put(TextInputMap.DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean scrollDown = model.movePointerDown(true);
                if (scrollDown) {
                    scrollOnLine(VerticalDirection.DOWN);
                }

                scrollToCursorRectAndRender();
            }
        });

        put(TextInputMap.DOWN_SHIFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.startOrContinueSelection();
                model.movePointerDown(false);

                scrollToCursorRectAndRender();
            }
        });

        put(TextInputMap.CTRL_V, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Optional<String> clipboardTextOpt = clipboardAdapter.getText();
                if (!clipboardTextOpt.isPresent()) {
                    return;
                }

                model.onTextInput(clipboardTextOpt.get().toCharArray());
                undoService.pushState();

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.CTRL_C, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedText = model.getSelectedText();
                clipboardAdapter.setText(selectedText);
            }
        });

        put(TextInputMap.INIT_POSITION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToInitPosition(true);

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.LAST_POSITION, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToLastPosition(true);

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.LINE_END, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToTheEndOfLine(true);

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.LINE_START, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToStartOfLine(true);

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.CTRL_Z, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoService.undo();

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.CTRL_K, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undoService.redo();

                scrollToCursorPositionAndRender();
            }
        });

        put(TextInputMap.CTRL_A, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.movePointerToLastPosition(true);
                model.setSelectionEnd(0);

                scrollToCursorRectAndRender();
            }
        });
    }

    private void scrollToCursorRectAndRender() {
        textArea.render();
    }

    private void scrollOnLine(VerticalDirection direction) {
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

    public void scrollToCursorPositionAndRender() {
        Rope rope = model.getRope();
        int cursorPosition = model.getCursorPosition();
        if (cursorPosition < 0) {
            return;
        }

        if (cursorPosition >= rope.getLength()) {
            forceScrollToCharIndexAndRender(rope.getLength() - 1);
        }

        forceScrollToCharIndexAndRender(cursorPosition);
    }

    private void forceScrollToCharIndexAndRender(int cursorPosition) {
        Rope rope = model.getRope();
        int lineCount = rope.lineAtChar(cursorPosition);
        int y = lineCount * ropeDrawComponent.getLatestFontHeight();

        model.moveCursorRectToY(y);
        scrollToCursorRectAndRender();

        SwingUtilities.invokeLater(() -> textArea.render());
    }

}