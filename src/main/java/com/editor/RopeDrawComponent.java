package com.editor;

import com.editor.model.Pointer;
import com.editor.model.RopeTextEditorModel;
import com.editor.model.RopeTextModel;
import com.editor.model.TextEditorModel;
import com.editor.parser.CommentsHighlight;
import com.editor.parser.CommonSyntaxHighlight;
import com.editor.parser.SyntaxParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.List;

/**
 * Main class which redraw all in text area.
 */

public class RopeDrawComponent extends JComponent {

    private RopeTextEditorModel model;

    public static final Color DEFAULT_CHAR_COLOR = Color.black;

    public static final Color SELECTOR_COLOR = new Color(250, 128, 114, 100);
    public static final Color CURRENT_ROW_COLOR = new Color(255, 235, 205);
    public static final Color RESERVED_WORDS_COLOR = new Color(204, 0, 153);
    public static final Color PAIRED_BRACKETS_COLOR = Color.GREEN;
    public static final Color COMMENTS_COLOR = new Color(138, 43, 226);

    public static final int DEFAULT_Y_COORDINATE = 15;
    public static final int POINTER_WIDTH = 2;

    public static Logger log = LoggerFactory.getLogger(RopeDrawComponent.class);

    private Dimension preferredSize;
    private Rectangle cursorBounds;
    private boolean scrollToCursorOnceOnPaint;
    private Rectangle visibleBounds;
    private Pointer mouseSelectionEndPointer;
    private Pointer mouseCursorPointer;

    public RopeDrawComponent(RopeTextEditorModel model) {
        this.model = model;
    }

    /**
     * Permanently redraw all text area on each action.
     * Called by repaint() method.
     *
     * @param graphics
     */

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        log.debug("Paint started. VisibleBounds: " + visibleBounds);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        updatePreferredSize(graphics);

        SyntaxParser syntaxParser = new SyntaxParser();
        AffineTransform affineTransform = graphics2D.getTransform();
//        List<StringBuilder> lineBuilders = model.getLineBuilders();

        int currentReservedWordIndex = 0;
        int fontHeight = graphics.getFontMetrics().getHeight();

//        handleMouseActions(graphics2D, lineBuilders, fontHeight);

        updatePointerBounds(graphics2D, model.getCursorPosition().row, model.getCursorPosition().column);

//        if (scrollToCursorOnceOnPaint) {
//            revalidate();
//            scrollRectToVisible(cursorBounds);
//            scrollToCursorOnceOnPaint = false;
//        }

        int startRow = 0;
        int endRow = model.getLinesCount() - 1;

        /* Count startRow and endRow, that will be viewed to user */
        if (visibleBounds != null) {
            startRow = Math.max(0, visibleBounds.y / fontHeight - 1);
            graphics2D.translate(0, Math.max(0, visibleBounds.y - fontHeight));
            affineTransform = graphics2D.getTransform();

            endRow = Math.min(model.getLinesCount() - 1, (visibleBounds.y + visibleBounds.height) / fontHeight - 1);
        }

//        List<CommentsHighlight> lineJsCommentsList = syntaxParser.getLineCommentsHighlight(model, startRow, endRow);
//        List<CommonSyntaxHighlight> reservedWordsList = syntaxParser.getReservedWordsHighlightByIndexes(model, startRow, endRow);

        /* Go trough lines that will be painted*/
        Iterator<Character> iterator = model.getRope().iterator(0);
        while(iterator.hasNext()) {
            Character c = iterator.next();
            drawChar(graphics2D,c,DEFAULT_CHAR_COLOR, null);
        }

//        for (int row = startRow; row <= endRow; row++) {
//            StringBuilder lineBuilder = lineBuilders.get(row);
//            Pointer cursorPosition = model.getCursorPosition();
//
//            if (cursorPosition.row == row) {
//                drawLineBackground(graphics2D, CURRENT_ROW_COLOR);
//            }
//
//            /* Count startColumn and endColumn, that will be viewed to user */
//            int startColumn = 0;
//            int endColumn = lineBuilder.length() - 1;
//            if (visibleBounds != null) {
//                startColumn = Math.max(0, getCharIndex(visibleBounds.x, lineBuilder, graphics2D));
//                graphics2D.translate(visibleBounds.x, 0);
//
//                endColumn = Math.min(lineBuilder.length() - 1, getCharIndex(visibleBounds.x + visibleBounds.width, lineBuilder, graphics2D));
//            }
//
//            /* Go trough chars in line */
//            for (int column = startColumn; column <= endColumn; column++) {
//                char ch = lineBuilder.charAt(column);
//
//                if (cursorPosition.row == row && cursorPosition.column == column) {
//                    drawPointer(graphics2D);
//                }
//
//                Color charColor = DEFAULT_CHAR_COLOR;
//                Color charBackground = null;
//
//                /* Check if this char is in reserved word list */
//                if (currentReservedWordIndex < reservedWordsList.size()) {
//                    CommonSyntaxHighlight currentReservedWord = reservedWordsList.get(currentReservedWordIndex);
//
//                    while (currentReservedWordIndex < reservedWordsList.size() && currentReservedWord.getRowIndex() == row && startColumn > currentReservedWord.getEndIndex()) {
//                        currentReservedWord = reservedWordsList.get(currentReservedWordIndex);
//                        currentReservedWordIndex++;
//                    }
//
//                    if (currentReservedWord.getRowIndex() == row && currentReservedWord.getStartIndex() <= column && column <= currentReservedWord.getEndIndex()) {
//                        charColor = RESERVED_WORDS_COLOR;
//                        if (column == currentReservedWord.getEndIndex()) {
//                            currentReservedWordIndex++;
//                        }
//                    }
//                }
//
//                if (!SyntaxParser.isTextSyntax()) {
//
//                    /* Check if this char is bracket */
//                    Pointer startBracket = model.getStartBracket();
//                    Pointer endBracket = model.getEndBracket();
//                    if (startBracket != null && row == startBracket.row && column == startBracket.column ||
//                            (endBracket != null && row == endBracket.row && column == endBracket.column)) {
//                        charColor = PAIRED_BRACKETS_COLOR;
//                    }
//
//                    if (Character.isDigit(ch)) {
//                        charColor = Color.BLUE;
//                    }
//
//                    if (ch == ',' || ch == ';') {
//                        charColor = RESERVED_WORDS_COLOR;
//                    }
//                }
//
//                /* Check if this char is comment */
//                if (!lineJsCommentsList.isEmpty()) {
//                    for (CommentsHighlight currentComment : lineJsCommentsList) {
//                        if (currentComment.isIsCommentsInProgress(row, column)) {
//                            charColor = COMMENTS_COLOR;
//                            break;
//                        }
//                    }
//                }
//
//                /* Check if this char is in selection */
//                if (model.isSelectionInProgress()) {
//                    Pointer currentCharPoint = new Pointer(row, column);
//                    Pointer from = model.getSelectionFrom();
//                    Pointer to = model.getSelectionTo();
//
//                    if (from.compareTo(currentCharPoint) <= 0 && to.compareTo(currentCharPoint) > 0) {
//                        charBackground = SELECTOR_COLOR;
//                    }
//                }
//
//                drawChar(graphics2D, ch, charColor, charBackground);
//            }
//
//            /* Check if cursor is occurred in current position */
//            if (cursorPosition.row == row && cursorPosition.column == lineBuilder.length()) {
//                drawPointer(graphics2D);
//            }
//
//            if (row < lineBuilders.size()) {
//                graphics2D.setTransform(affineTransform);
//                graphics2D.translate(0, graphics2D.getFontMetrics().getHeight());
//                affineTransform = graphics2D.getTransform();
//            }
//
//        }
    }

    private void handleMouseActions(Graphics2D graphics2D, List<StringBuilder> lineBuilders, int fontHeight) {
//        if (mouseCursorPointer != null) {
//            int cursorY = mouseCursorPointer.row;
//            int cursorX = mouseCursorPointer.column;
//
//            int cursorRow = getColumnByY(fontHeight, cursorY);
//
//            StringBuilder line = lineBuilders.get(cursorRow);
//            int cursorCol = getCharIndex(cursorX, line, graphics2D);
//
//            model.setCursorPosition(new Pointer(cursorRow, cursorCol));
//
//            mouseCursorPointer = null;
//        }
//
//        if (mouseSelectionEndPointer != null) {
//            int cursorY = mouseSelectionEndPointer.row;
//            int cursorX = mouseSelectionEndPointer.column;
//
//            int cursorRow = getColumnByY(fontHeight, cursorY);
//            int cursorCol = getCharIndex(cursorX, lineBuilders.get(cursorRow), graphics2D);
//
//            Pointer selectionEnd = new Pointer(cursorRow, cursorCol);
//            if (selectionEnd.equals(model.getCursorPosition())) {
//                selectionEnd = null;
//            }
//            model.setSelectionEnd(selectionEnd);
//
//            mouseSelectionEndPointer = null;
//        }
    }

    /*private int getColumnByY(int fontHeight, int cursorY) {
        int desiredLineNumber = (int) Math.round(cursorY / (double) fontHeight) - 1;
        int lastLineNumber = model.getLineBuilders().size() - 1;
        if (desiredLineNumber > lastLineNumber) {
            desiredLineNumber = lastLineNumber;
        }
        if (desiredLineNumber < 0) {
            desiredLineNumber = 0;
        }
        return desiredLineNumber;
    }*/

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
        if (backgroundColor != null) {
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRect(0, 3, graphics2D.getFontMetrics().charWidth(currentChar), graphics2D.getFontMetrics().getHeight());
        }

        graphics2D.setColor(color);
        graphics2D.drawString(Character.toString(currentChar), 0, DEFAULT_Y_COORDINATE);
        graphics2D.translate(graphics2D.getFontMetrics().charWidth(currentChar), 0);
    }

    private void drawLineBackground(Graphics graphics2D, Color backgroundColor) {
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRect(0, 3, visibleBounds.x + visibleBounds.width, graphics2D.getFontMetrics().getHeight());
    }

    private void updatePreferredSize(Graphics graphics) {
//        2preferredSize = new Dimension(width, height);
    }

    private void drawPointer(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 3, POINTER_WIDTH, graphics2D.getFontMetrics().getHeight());
    }

    private void updatePointerBounds(Graphics2D graphics2D, int i, int j) {
//        FontMetrics fontMetrics = graphics2D.getFontMetrics();
//
//        StringBuilder line = model.getLineBuilders().get(i);
//        cursorBounds = new Rectangle(
//                fontMetrics.stringWidth(line.substring(0, j)),
//                fontMetrics.getHeight() * i,
//                POINTER_WIDTH * 4,
//                fontMetrics.getHeight() * 2);
    }

    public void setScrollToCursorOnceOnPaint(boolean scrollToCursorOnceOnPaint) {
        this.scrollToCursorOnceOnPaint = scrollToCursorOnceOnPaint;
    }

    public void setMouseCursorPointer(Pointer mouseCursorPointer) {
        this.mouseCursorPointer = mouseCursorPointer;
    }

    public void setMouseSelectionEndPointer(Pointer mouseSelectionEndPointer) {
        this.mouseSelectionEndPointer = mouseSelectionEndPointer;
    }
}
