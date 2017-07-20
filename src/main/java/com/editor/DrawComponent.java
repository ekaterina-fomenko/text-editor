package com.editor;

import com.editor.model.Pointer;
import com.editor.model.TextEditorModel;
import com.editor.parser.CommonSyntaxHighlight;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class DrawComponent extends JComponent {
    public static final int POINTER_WIDTH = 2;
    private TextEditorModel model;
    public static final Color DEFAULT_CHAR_COLOR = Color.black;

    public static final Color SELECTOR_COLOR = new Color(250, 128, 114, 100);
    public static final Color RESERVED_WORDS_COLOR = new Color(204, 0, 153);
    public static final Color PAIRED_BRACKETS_COLOR = Color.GREEN;
    public static final int DEFAULT_Y_COORDINATE = 15;

    public DrawComponent(TextEditorModel model) {
        this.model = model;
    }

    private Dimension preferredSize;
    private Rectangle cursorBounds;

    private boolean scrollToCursorOnceOnPaint;

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        updatePreferredSize(graphics);

        //graphics2D.setColor(new Color(99, 74, 68));
        SyntaxParser syntaxParser = new SyntaxParser();
        List<CommonSyntaxHighlight> reservedWordsList = syntaxParser.getReservedWordsHighlight(model);

        int currentReservedWordIndex = 0;
        AffineTransform affineTransform = graphics2D.getTransform();
        ArrayList<StringBuilder> lineBuilders = model.getLineBuilders();

        for (int row = 0; row < lineBuilders.size(); row++) {
            StringBuilder lineBuilder = lineBuilders.get(row);
            Pointer cursorPosition = model.getCursorPosition();

            for (int column = 0; column < lineBuilder.length(); column++) {
                char ch = lineBuilder.charAt(column);

                if (cursorPosition.row == row && cursorPosition.column == column) {
                    drawPointer(graphics2D, row, column);
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

                if (!SyntaxParser.isTextSyntax()) {
                    Pointer startBracket = model.getStartBracket();
                    Pointer endBracket = model.getEndBracket();
                    if (startBracket != null && row == startBracket.row && column == startBracket.column ||
                            (endBracket != null && row == endBracket.row && column == endBracket.column)) {
                        charColor = PAIRED_BRACKETS_COLOR;
                    }

                    if (Character.isDigit(ch)) {
                        charColor = Color.BLUE;
                    }

                    if (ch == ',' || ch == ';') {
                        charColor = RESERVED_WORDS_COLOR;
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
                drawPointer(graphics2D, row, lineBuilder.length());
            }

            if (row < lineBuilders.size()) {
                graphics2D.setTransform(affineTransform);
                graphics2D.translate(0, graphics2D.getFontMetrics().getHeight());
                affineTransform = graphics2D.getTransform();
            }

        }

    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize != null ? preferredSize : super.getPreferredSize();
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

    private void updatePreferredSize(Graphics graphics) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int height = fontMetrics.getHeight() * (model.getLineBuilders().size() + 1);
        int width = 0;
        for (StringBuilder line : model.getLineBuilders()) {
            int length = line.length();
            int horSpaceOffset = length > 0 ? fontMetrics.charWidth(line.charAt(length - 1)) : 0;
            width = Math.max(width, fontMetrics.stringWidth(line.toString())) + horSpaceOffset;
        }
        preferredSize = new Dimension(width, height);
    }

    private void drawPointer(Graphics2D graphics2D, int row, int column) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 3, POINTER_WIDTH, graphics2D.getFontMetrics().getHeight());

        updatePointerBounds(graphics2D, row, column);

        if (scrollToCursorOnceOnPaint) {
            revalidate();
            scrollRectToVisible(cursorBounds);
            scrollToCursorOnceOnPaint = false;
        }
    }

    private void updatePointerBounds(Graphics2D graphics2D, int i, int j) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        StringBuilder line = model.getLineBuilders().get(i);
        cursorBounds = new Rectangle(
                fontMetrics.stringWidth(line.substring(0, j)),
                fontMetrics.getHeight() * i,
                POINTER_WIDTH * 4,
                fontMetrics.getHeight() * 2);
    }

    public void setScrollToCursorOnceOnPaint(boolean scrollToCursorOnceOnPaint) {
        this.scrollToCursorOnceOnPaint = scrollToCursorOnceOnPaint;
    }
}
