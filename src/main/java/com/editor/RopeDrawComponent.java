package com.editor;

import com.editor.model.LineInfo;
import com.editor.model.RopeTextEditorModel;
import com.editor.model.buffer.VisibleLinesInfo;
import com.editor.model.rope.Rope;
import com.editor.syntax.keywords.PairedBracketsInfo;
import com.editor.syntax.keywords.SyntaxResolver;
import com.editor.syntax.keywords.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

/**
 * Main class which redraw all in text area.
 */

public class RopeDrawComponent extends JComponent {
    public static final int CURSOR_LEFT_OFFSET = 5;
    public static final int CURSOR_RIGHT_OFFSET = 10;
    public static final int CURSOR_WIDTH = 2;

    public static final Color DEFAULT_CHAR_COLOR = Color.black;
    public static final Color SELECTOR_COLOR = new Color(250, 128, 114, 100);
    public static final Color CURSOR_ROW_BACKGROUND_COLOR = new Color(255, 235, 205, 192);
    public static final int DEFAULT_Y_COORDINATE = 15;

    public static Logger log = LoggerFactory.getLogger(RopeDrawComponent.class);

    private RopeTextEditorModel model;
    private Rectangle visibleBounds = new Rectangle();
    private Graphics2D latestGraphics;

    private Point mouseCursorPointer;
    private boolean scrollToCursorOnceOnPaint;

    private final EditorSettings editorSettings;

    public RopeDrawComponent(EditorSettings editorSettings, RopeTextEditorModel model) {
        this.editorSettings = editorSettings;
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
        long paintStart = System.currentTimeMillis();

        Rope rope = model.getRope();

        Graphics2D graphics2D = (Graphics2D) graphics;
        latestGraphics = graphics2D;
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

        VisibleLinesInfo.Builder visibleLinesBufferBuilder = new VisibleLinesInfo.Builder();

        Rope visibleRope = charIndexOfVisibleStart < charIndexOfVisibleEnd && charIndexOfVisibleStart > -1
                ? rope.substring(charIndexOfVisibleStart, charIndexOfVisibleEnd)
                : rope;

        SyntaxResolver syntaxResolver = new SyntaxResolver(editorSettings.getCurrentSyntax());
        syntaxResolver.calculateTokens(visibleRope, currentIndex);

        Map<Integer, TokenType> reservedWordsMap = syntaxResolver.getKeywordsIndexes();
        PairedBracketsInfo currentBracketsInfo = getVisibleBracketsInfo(syntaxResolver);

        int cursorLine = rope.lineAtChar(model.getCursorPosition());
        drawBackground(graphics, graphics2D, cursorLine - startRow);

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
                    Color charColor = getCurrentCharColor(currentIndex, reservedWordsMap, currentBracketsInfo, i);
                    Color backgroundColor = model.isInSelection(currentIndex) ? SELECTOR_COLOR : null;
                    drawChar(graphics2D, c, currentLinePixelLength, charColor, backgroundColor);
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
            scrollToCursorRect();
            scrollToCursorOnceOnPaint = false;
        }


        long paintEnd = System.currentTimeMillis();
        log.debug("Paint: {}ms", paintEnd - paintStart);
    }

    private void drawBackground(Graphics graphics, Graphics2D graphics2D, int index) {
        AffineTransform transform = graphics2D.getTransform();
        graphics.translate(0, graphics2D.getFontMetrics().getHeight() * index);
        drawLineBackground(graphics2D, CURSOR_ROW_BACKGROUND_COLOR);
        graphics2D.setTransform(transform);
    }

    protected Color getCurrentCharColor(int currentIndex, Map<Integer, TokenType> reservedWordsMap, PairedBracketsInfo currentBracketsInfo, int currentIndexOfVisibleRope) {
        Color charColor;
        if (reservedWordsMap.containsKey(currentIndexOfVisibleRope)) {
            charColor = reservedWordsMap.get(currentIndexOfVisibleRope).getColor();
        } else if (currentBracketsInfo.getStartInd() == currentIndex || currentBracketsInfo.getEndInd() == currentIndex) {
            charColor = currentBracketsInfo.getTokenType().getColor();
        } else {
            charColor = DEFAULT_CHAR_COLOR;
        }
        return charColor;
    }

    private void scrollToCursorRect() {
        Rectangle cursorRect = model.getCursorRect();
        scrollRectToVisible(new Rectangle(
                cursorRect.x - CURSOR_LEFT_OFFSET,
                cursorRect.y,
                cursorRect.width + CURSOR_RIGHT_OFFSET,
                cursorRect.height));
    }

    protected int getIndexOfVisibleEnd(Rope rope, int endRow) {
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
                && currentLineIndex == mouseCursorPointer.y / height
                && (distanceFromLineStart >= mouseCursorPointer.x || currentChar.equals('\n'))) {
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

    private void drawChar(Graphics2D graphics2D, char currentChar, int currentLinePixelLength, Color color, Color backgroundColor) {
        int charWidth = graphics2D.getFontMetrics().charWidth(currentChar);
        if (currentLinePixelLength >= visibleBounds.x && currentLinePixelLength <= visibleBounds.x + visibleBounds.width) {
            if (backgroundColor != null) {
                graphics2D.setColor(backgroundColor);
                graphics2D.fillRect(0, 3, charWidth, graphics2D.getFontMetrics().getHeight());
            }

            graphics2D.setColor(color);
            // Not so fast for each char separately
            graphics2D.drawString(Character.toString(currentChar), 0, DEFAULT_Y_COORDINATE);
        }

        graphics2D.translate(charWidth, 0);
    }

    public void setMouseCursorPointer(Point mouseCursorPointer) {
        this.mouseCursorPointer = mouseCursorPointer;
    }

    public void setScrollToCursorOnceOnPaint(boolean scrollToCursorOnceOnPaint) {
        this.scrollToCursorOnceOnPaint = scrollToCursorOnceOnPaint;
    }

    public Graphics2D getLatestGraphics() {
        return latestGraphics;
    }

    public int getLatestFontHeight() {
        if (latestGraphics == null) {
            return 0;
        }

        return latestGraphics.getFontMetrics().getHeight();
    }

    protected PairedBracketsInfo getVisibleBracketsInfo(SyntaxResolver syntaxResolver) {
        Map<Integer, PairedBracketsInfo> bracketMap = syntaxResolver.getBracketsIndexesMap();
        PairedBracketsInfo currentBracketsInfo = new PairedBracketsInfo();

        PairedBracketsInfo bracketsInfo = bracketMap.get(model.getCursorPosition());
        PairedBracketsInfo bracketPrevInfo = bracketMap.get(model.getCursorPosition() - 1);

        if (bracketsInfo != null || bracketPrevInfo != null) {

            currentBracketsInfo = bracketsInfo != null ? bracketsInfo : bracketPrevInfo;

        }
        return currentBracketsInfo;
    }
}
