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
    private Rectangle visibleBounds;
    private Pointer mouseSelectionEndPointer;
    private Pointer mouseCursorPointer;

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        System.out.println();
        System.out.println("Paint started. VisibleBounds: " + visibleBounds);
        long startDraw = System.currentTimeMillis();

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        SyntaxParser syntaxParser = new SyntaxParser();

        updatePreferredSize(graphics);

        int currentReservedWordIndex = 0;
        AffineTransform affineTransform = graphics2D.getTransform();
        ArrayList<StringBuilder> lineBuilders = model.getLineBuilders();
        int fontHeight = graphics.getFontMetrics().getHeight();

        handleMouseActions(graphics2D, lineBuilders, fontHeight);

        updatePointerBounds(graphics2D, model.getCursorPosition().row, model.getCursorPosition().column);

        if (scrollToCursorOnceOnPaint) {
            revalidate();
            scrollRectToVisible(cursorBounds);
            scrollToCursorOnceOnPaint = false;
        }

        int startRow = 0;
        int endRow = lineBuilders.size() - 1;

        if (visibleBounds != null) {
            startRow = Math.max(0, visibleBounds.y / fontHeight - 1);
            graphics2D.translate(0, Math.max(0, visibleBounds.y - fontHeight));
            affineTransform = graphics2D.getTransform();

            endRow = Math.min(lineBuilders.size() - 1, (visibleBounds.y + visibleBounds.height) / fontHeight - 1);
        }

        List<CommonSyntaxHighlight> reservedWordsList = syntaxParser.getReservedWordsHighlightByIndexes(model, startRow, endRow);

        for (int row = startRow; row <= endRow; row++) {
            StringBuilder lineBuilder = lineBuilders.get(row);
            Pointer cursorPosition = model.getCursorPosition();

            int startCol = 0;
            int endCol = lineBuilder.length() - 1;
            if (visibleBounds != null) {
                startCol = Math.max(0, getCharIndex(visibleBounds.x, lineBuilder, graphics2D));
                graphics2D.translate(visibleBounds.x, 0);

                endCol = Math.min(lineBuilder.length() - 1, getCharIndex(visibleBounds.x + visibleBounds.width, lineBuilder, graphics2D));
            }

            for (int column = startCol; column <= endCol; column++) {
                char ch = lineBuilder.charAt(column);

                if (cursorPosition.row == row && cursorPosition.column == column) {
                    drawPointer(graphics2D);
                }

                Color charColor = DEFAULT_CHAR_COLOR;
                Color charBackground = null;

                if (currentReservedWordIndex < reservedWordsList.size()) {
                    CommonSyntaxHighlight currentReservedWord = reservedWordsList.get(currentReservedWordIndex);

                    while (currentReservedWord.getRowIndex() == row && startCol > currentReservedWord.getEndIndex()) {
                        currentReservedWordIndex++;
                        currentReservedWord = reservedWordsList.get(currentReservedWordIndex);
                    }

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
                drawPointer(graphics2D);
            }

            if (row < lineBuilders.size()) {
                graphics2D.setTransform(affineTransform);
                graphics2D.translate(0, graphics2D.getFontMetrics().getHeight());
                affineTransform = graphics2D.getTransform();
            }

        }

        System.out.println("Draw: " + (System.currentTimeMillis() - startDraw));

    }

    private void handleMouseActions(Graphics2D graphics2D, ArrayList<StringBuilder> lineBuilders, int fontHeight) {
        if (mouseCursorPointer != null) {
            int cursorY = mouseCursorPointer.row;
            int cursorX = mouseCursorPointer.column;

            int cursorRow = getColumnByY(fontHeight, cursorY);

            StringBuilder line = lineBuilders.get(cursorRow);
            int cursorCol = getCharIndex(cursorX, line, graphics2D);

            model.setCursorPosition(new Pointer(cursorRow, cursorCol));

            mouseCursorPointer = null;
        }

        if (mouseSelectionEndPointer != null) {
            int cursorY = mouseSelectionEndPointer.row;
            int cursorX = mouseSelectionEndPointer.column;

            int cursorRow = getColumnByY(fontHeight, cursorY);
            int cursorCol = getCharIndex(cursorX, lineBuilders.get(cursorRow), graphics2D);

            model.setSelectionEnd(new Pointer(cursorRow, cursorCol));
            mouseSelectionEndPointer = null;
        } else {
            model.setSelectionEnd(null);
        }
    }

    private int getColumnByY(int fontHeight, int cursorY) {
        int desiredLineNumber = (int) Math.round(cursorY / (double) fontHeight) - 1;
        int lastLineNumber = model.getLineBuilders().size() - 1;
        if (desiredLineNumber > lastLineNumber) {
            desiredLineNumber = lastLineNumber;
        }
        if (desiredLineNumber < 0) {
            desiredLineNumber = 0;
        }
        return desiredLineNumber;
    }

    private int getCharIndex(int x, StringBuilder lineBuilder, Graphics2D graphics2D) {
        int size = 0;
        int i;
        for (i = 0; i < lineBuilder.length() && size < x; i++) {
            size += graphics2D.getFontMetrics().charWidth(lineBuilder.charAt(i));
        }

        return i;
    }

    public void setVisibleBounds(Rectangle visibleBounds) {
        this.visibleBounds = visibleBounds;
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize != null ? preferredSize : super.getPreferredSize();
    }

    private void drawChar(Graphics2D graphics2D, char currentChar, Color color, Color backgroundColor) {
        //  graphics2D.setClip(0,0,100,100);
        //  graphics2D.clearRect(0,0,50,50);
        if (backgroundColor != null) {
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRect(0, 3, graphics2D.getFontMetrics().charWidth(currentChar), graphics2D.getFontMetrics().getHeight());
        }

        graphics2D.setColor(color);
        graphics2D.drawString(Character.toString(currentChar), 0, DEFAULT_Y_COORDINATE);
        graphics2D.translate(graphics2D.getFontMetrics().charWidth(currentChar), 0);
    }

    private void updatePreferredSize(Graphics graphics) {
        long start = System.currentTimeMillis();
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int height = fontMetrics.getHeight() * (model.getLineBuilders().size() + 1);
        int width = 0;
        for (StringBuilder line : model.getLineBuilders()) {
            int length = line.length();
            int horSpaceOffset = length > 0 ? fontMetrics.charWidth(line.charAt(length - 1)) : 0;
            width = Math.max(width, fontMetrics.stringWidth(line.toString())) + horSpaceOffset;
        }
        preferredSize = new Dimension(width, height);
        System.out.println("updatePreferredSize: " + (System.currentTimeMillis() - start));
    }

    private void drawPointer(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 3, POINTER_WIDTH, graphics2D.getFontMetrics().getHeight());
    }

    private void updatePointerBounds(Graphics2D graphics2D, int i, int j) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();

        StringBuilder line = model.getLineBuilders().get(i);
        cursorBounds = new Rectangle(
                fontMetrics.stringWidth(line.substring(0, j)),
                fontMetrics.getHeight() * i,
                POINTER_WIDTH * 4,
                fontMetrics.getHeight() * 2);
        //        System.out.println("fontMetrics.stringWidth" + fontMetrics.stringWidth(line.substring(0, j)) + "fontMetrics.getHeight()" + fontMetrics.getHeight() * i);
    }

    public void setScrollToCursorOnceOnPaint(boolean scrollToCursorOnceOnPaint) {
        this.scrollToCursorOnceOnPaint = scrollToCursorOnceOnPaint;
        //        System.out.println("scrollC:" + this.scrollToCursorOnceOnPaint);
    }

    public void setMouseCursorPointer(Pointer mouseCursorPointer) {
        this.mouseCursorPointer = mouseCursorPointer;
    }

    public void setMouseSelectionEndPointer(Pointer mouseSelectionEndPointer) {
        this.mouseSelectionEndPointer = mouseSelectionEndPointer;
    }
}
