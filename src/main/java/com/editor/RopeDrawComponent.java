package com.editor;

import com.editor.model.LineInfo;
import com.editor.model.Pointer;
import com.editor.model.RopeTextEditorModel;
import com.editor.model.buffer.VisibleLinesBufferBuilder;
import com.editor.model.rope.Rope;
import com.editor.syntax.keywords.PairedBracketsInfo;
import com.editor.syntax.keywords.KeywordsTrie;
import com.editor.syntax.keywords.TokenType;
import com.editor.syntax.keywords.SyntaxScannerTrie;
import com.editor.system.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class which redraw all in text area.
 */

public class RopeDrawComponent extends JComponent {

    private RopeTextEditorModel model;

    public static final int CURSOR_WIDTH = 2;

    public static final Color DEFAULT_CHAR_COLOR = Color.black;
    public static final Color SELECTOR_COLOR = new Color(250, 128, 114, 100);
    public static final Color CURSOR_ROW_BACKGROUND_COLOR = new Color(255, 235, 205, 192);

    public static final int DEFAULT_Y_COORDINATE = 15;

    public static Logger log = LoggerFactory.getLogger(RopeDrawComponent.class);

    private Rectangle visibleBounds = new Rectangle();

    private Graphics2D latestGraphices = null;

    private Pointer mouseCursorPointer;
    private boolean scrollToCursorOnceOnPaint;

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
        long paintStart = System.currentTimeMillis();

        Rope rope = model.getRope();
        Color charColor;

        Graphics2D graphics2D = (Graphics2D) graphics;
        latestGraphices = graphics2D;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        updatePreferredSize(graphics);

        int fontHeight = graphics.getFontMetrics().getHeight();

        graphics2D.translate(0, Math.max(0, visibleBounds.y) - visibleBounds.y % fontHeight);
        AffineTransform affineTransform = graphics2D.getTransform();

        int startRow = Math.max(0, visibleBounds.y / fontHeight);
        int endRow = Math.min(rope.getLinesNum() - 1, (visibleBounds.y + visibleBounds.height) / fontHeight);

        // Go trough lines that will be painted
        int charIndexOfVisibleStart = rope.charIndexOfLineStart(startRow);
        int charIndexOfVisibleEnd = getIndexOfVisibleEnd(rope, endRow);
        int linesCountToRender = endRow - startRow + 1;

        int linesCountRendered = 0;
        int currentIndex = charIndexOfVisibleStart;

        int currentLineStartIndex = charIndexOfVisibleStart;

        int currentLineLength = 0;
        int currentLinePixelLength = 0;
        final int charHeight = graphics2D.getFontMetrics().getHeight();

        VisibleLinesBufferBuilder visibleLinesBufferBuilder = new VisibleLinesBufferBuilder();

        PairedBracketsInfo bracketInfo;

        Rope visibleRope = charIndexOfVisibleStart < charIndexOfVisibleEnd && charIndexOfVisibleStart > -1
                ? rope.substring(charIndexOfVisibleStart, charIndexOfVisibleEnd)
                : rope;


        SyntaxScannerTrie keywordsTree = KeywordsTrie.getCurrentSyntaxTrie();
        Map<Integer, TokenType> reservedWordsSet = keywordsTree.isEmpty() ? new HashMap<>() : keywordsTree.getKeywordsIndexes(visibleRope, currentIndex);

        int bracketStart = -1;
        int bracketEnd = -1;
        Color bracketColor = DEFAULT_CHAR_COLOR;
        Map<Integer, PairedBracketsInfo> bracketMap = keywordsTree.isEmpty() ? new HashMap<>() : keywordsTree.getBracketsIndexesMap();

        if (bracketMap.containsKey(model.getCursorPosition()) || bracketMap.containsKey(model.getCursorPosition() - 1)) {
            bracketInfo = bracketMap.containsKey(model.getCursorPosition()) ? bracketMap.get(model.getCursorPosition()) : bracketMap.get(model.getCursorPosition() - 1);

            bracketStart = bracketInfo.getStartInd();
            bracketEnd = bracketInfo.getEndInd();
            bracketColor = bracketInfo.getTokenType().getColor();
        }

        int cursorLine = rope.lineAtChar(model.getCursorPosition());
        AffineTransform transform = graphics2D.getTransform();
        graphics.translate(0, graphics2D.getFontMetrics().getHeight() * (cursorLine - startRow));
        drawLineBackground(graphics2D, CURSOR_ROW_BACKGROUND_COLOR);
        graphics2D.setTransform(transform);

        int i = 0;
        while (i < visibleRope.getLength() && linesCountRendered < linesCountToRender) {
            Character c = visibleRope.charAt(i);

            currentLineLength++;
            currentLinePixelLength += graphics2D.getFontMetrics().charWidth(c);

            updateCursorPositionFromCoordinates(
                    graphics2D,
                    currentLinePixelLength,
                    currentIndex,
                    startRow + linesCountRendered,
                    c
            );

            if (currentIndex == model.getCursorPosition()) {
                visibleLinesBufferBuilder.withCursorChar(c);

                drawPointer(graphics2D);
                model.setCursorRect(new Rectangle(
                        currentLinePixelLength - graphics.getFontMetrics().charWidth(c),
                        visibleBounds.y + linesCountRendered * charHeight,
                        CURSOR_WIDTH,
                        charHeight
                ));
            }

            if (!c.equals('\r')) {
                if (c.equals('\n')) {
                    graphics2D.setTransform(affineTransform);
                    graphics2D.translate(0, charHeight);
                    affineTransform = graphics2D.getTransform();
                    linesCountRendered++;
                    visibleLinesBufferBuilder.addLine(new LineInfo(currentLineStartIndex, currentLineLength - 1));

                    currentLineStartIndex = currentLineStartIndex + currentLineLength;
                    currentLineLength = 0;
                    currentLinePixelLength = 0;
                } else {
                    if (reservedWordsSet.containsKey(i)) {
                        charColor = reservedWordsSet.get(i).getColor();
                    } else if (bracketStart == currentIndex || bracketEnd == currentIndex) {
                        charColor = bracketColor;
                    } else {
                        charColor = DEFAULT_CHAR_COLOR;
                    }
                    drawChar(graphics2D, c, charColor, isInSelection(currentIndex) ? SELECTOR_COLOR : null);
                }
            }

            currentIndex++;
            i++;
        }

        visibleLinesBufferBuilder.addLine(new LineInfo(currentLineStartIndex, currentLineLength));
        model.setVisibleLinesInfo(visibleLinesBufferBuilder.build());

        if (currentIndex == model.getCursorPosition()) {
            drawPointer(graphics2D);
        }

        if (scrollToCursorOnceOnPaint) {
            revalidate();
            scrollRectToVisible(model.getCursorRect());
            scrollToCursorOnceOnPaint = false;
        }


        long paintEnd = System.currentTimeMillis();
        log.debug("Paint: {}ms", paintEnd - paintStart);
    }

    private int getIndexOfVisibleEnd(Rope rope, int endRow) {
        int indexOfLastPlusOneLineStart = rope.charIndexOfLineStart(endRow + 1);
        if (indexOfLastPlusOneLineStart == -1) {
            return rope.getLength();
        }

        return indexOfLastPlusOneLineStart;
    }

    private void drawLineBackground(Graphics graphics2D, Color backgroundColor) {
        graphics2D.setColor(backgroundColor);
        graphics2D.fillRect(0, 3, visibleBounds.x + visibleBounds.width, graphics2D.getFontMetrics().getHeight());
    }

    private void updateCursorPositionFromCoordinates(Graphics2D graphics2D,
                                                     int distanceFromLineStart,
                                                     int currentCharIndex,
                                                     int currentLineIndex,
                                                     Character currentChar) {
        int height = graphics2D.getFontMetrics().getHeight();
        if (mouseCursorPointer != null
                && currentLineIndex == mouseCursorPointer.row / height
                && (distanceFromLineStart >= mouseCursorPointer.column || currentChar.equals(Constants.NEW_LINE_CHAR))) {
            model.setCursorPosition(currentCharIndex);
            mouseCursorPointer = null;
        }
    }

    private void drawPointer(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 3, CURSOR_WIDTH, graphics2D.getFontMetrics().getHeight());
    }


    private void updatePreferredSize(Graphics graphics) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int height = fontMetrics.getHeight() * model.getRope().getLinesNum();
        int width = model.getRope().getMaxLineLength();

        setPreferredSize(new Dimension(width, height));
    }

    public void setVisibleBounds(Rectangle visibleBounds) {
        this.visibleBounds = visibleBounds;
    }

    private void drawChar(Graphics2D graphics2D, char currentChar, Color color, Color backgroundColor) {
        if (backgroundColor != null) {
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRect(0, 3, graphics2D.getFontMetrics().charWidth(currentChar), graphics2D.getFontMetrics().getHeight());
        }

        graphics2D.setColor(color);
        //Not so fast for each char separately
        graphics2D.drawString(Character.toString(currentChar), 0, DEFAULT_Y_COORDINATE);
        graphics2D.translate(graphics2D.getFontMetrics().charWidth(currentChar), 0);
    }

    private boolean isInSelection(int position) {
        if (!model.isSelectionInProgress()) {
            return false;
        }

        int selectionEnd = model.getSelectionEnd();
        int cursorPosition = model.getCursorPosition();

        return (selectionEnd <= position && position < cursorPosition) ||
                (cursorPosition <= position && position < selectionEnd);
    }

    public void setModel(RopeTextEditorModel model) {
        this.model = model;
    }

    public void setMouseCursorPointer(Pointer mouseCursorPointer) {
        this.mouseCursorPointer = mouseCursorPointer;
    }

    public void setScrollToCursorOnceOnPaint(boolean scrollToCursorOnceOnPaint) {
        this.scrollToCursorOnceOnPaint = scrollToCursorOnceOnPaint;
    }

    public Graphics2D getLatestGraphices() {
        return latestGraphices;
    }

    public int getLatestFontHeight() {
        if (latestGraphices == null) {
            return 0;
        }

        return latestGraphices.getFontMetrics().getHeight();
    }
}
