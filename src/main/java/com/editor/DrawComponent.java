package com.editor;

import com.editor.model.Pointer;
import com.editor.model.TextEditorModel;
import com.editor.parser.CommonSyntaxHighlight;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class DrawComponent extends JComponent {
    private TextEditorModel model;
    public static final Color DEFAULT_CHAR_COLOR = Color.black;
    public static final Color SELECTOR_COLOR = new Color(250, 128, 114, 100);//(153, 255, 204);
    public static final Color RESERVED_WORDS_COLOR = new Color(204, 0, 153);
    public static final int DEFAULT_Y_COORDINATE = 15;

    public DrawComponent(TextEditorModel model) {
        this.model = model;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        //graphics2D.setColor(new Color(99, 74, 68));
        SyntaxParser js = new SyntaxParser();
        java.util.List<CommonSyntaxHighlight> reservedWordsList = js.getReservedWordsHighlight2(model);
        char currentReservedWordIndex = 0;
        AffineTransform affineTransform = graphics2D.getTransform();
        ArrayList<StringBuilder> lineBuilders = model.getLineBuilders();

        for (int row = 0; row < lineBuilders.size(); row++) {
            StringBuilder lineBuilder = lineBuilders.get(row);
            Pointer cursorPosition = model.getCursorPosition();
            for (int column = 0; column < lineBuilder.length(); column++) {
                char ch = lineBuilder.charAt(column);

                if (cursorPosition.row == row && cursorPosition.column == column) {
                    drawPointer(graphics2D);
                }

                Color charColor = DEFAULT_CHAR_COLOR;
                Color charBackground = null;

                 if (currentReservedWordIndex < reservedWordsList.size()) {
                    CommonSyntaxHighlight currentReservedWord = reservedWordsList.get(currentReservedWordIndex);
                    if (currentReservedWord.getRowIndex() == row && currentReservedWord.getStartIndex() <= column && column <= currentReservedWord.getEndIndex()) {
                        charColor = RESERVED_WORDS_COLOR;
                        if (column == currentReservedWord.getEndIndex()) {
                            currentReservedWordIndex++;
                        }
                    }
                }

                if (model.isSelectionInProgress()) {
                    Pointer currentCharPoint = new Pointer(row, column);
                    Pointer from = model.getSelectionFrom();
                    Pointer to = model.getSelectionTo();

                    if (from.compareTo(currentCharPoint) <= 0 && to.compareTo(currentCharPoint) > 0) {
                        charBackground = SELECTOR_COLOR;
                    }
                }

                drawChar(graphics2D, ch, charColor, charBackground);
            }

            if (cursorPosition.row == row && cursorPosition.column == lineBuilder.length()) {
                drawPointer(graphics2D);
            }

            if (row < lineBuilders.size()) {
                graphics2D.setTransform(affineTransform);
                graphics2D.translate(0, graphics2D.getFontMetrics().getHeight());
                affineTransform = graphics2D.getTransform();
            }

        }
    }

    private void drawChar(Graphics2D graphics2D, char currentChar, Color color, Color backgroundColor) {
        if (backgroundColor != null) {
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRect(0, 3, graphics2D.getFontMetrics().charWidth(currentChar), graphics2D.getFontMetrics().getHeight());
        }

        graphics2D.setColor(color);
        graphics2D.drawString(Character.toString(currentChar), 0, DEFAULT_Y_COORDINATE);
        graphics2D.translate(graphics2D.getFontMetrics().charWidth(currentChar), 0);
    }

    private void drawPointer(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 3, 2, graphics2D.getFontMetrics().getHeight());
    }
}
