package com.editor;

import com.editor.model.LineInfo;
import com.editor.model.Pointer;
import com.editor.model.RopeTextEditorModel;
import com.editor.model.TextBufferBuilder;
import com.editor.model.rope.RopeApi;
import com.editor.model.rope.RopeIterator;
import com.editor.parser.keywords.KeywordsTrie;
import com.editor.parser.keywords.TokenType;
import com.editor.parser.keywords.Trie;
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

    public static final int POINTER_WIDTH = 2;

    public static final Color DEFAULT_CHAR_COLOR = Color.black;
    public static final Color SELECTOR_COLOR = new Color(250, 128, 114, 100);
    public static final Color CURSOR_ROW_BACKGROUND_COLOR = new Color(255, 235, 205, 192);

    public static final int DEFAULT_Y_COORDINATE = 15;

    public static Logger log = LoggerFactory.getLogger(RopeDrawComponent.class);

    private Rectangle visibleBounds = new Rectangle();

    private Graphics2D latestGraphices = null;
    private Pointer mouseCursorPointer;
    private boolean scrollToCursorOnceOnPaint;

    public RopeDrawComponent() {
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

        RopeApi rope = model.getRope();
        Color charColor;

        Graphics2D graphics2D = (Graphics2D) graphics;
        latestGraphices = graphics2D;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        updatePreferredSize(graphics);

        if (scrollToCursorOnceOnPaint) {
            revalidate();
            scrollRectToVisible(model.getCursorRect());
            scrollToCursorOnceOnPaint = false;
        }

        int fontHeight = graphics.getFontMetrics().getHeight();

        graphics2D.translate(0, Math.max(0, visibleBounds.y) - visibleBounds.y % fontHeight);
        AffineTransform affineTransform = graphics2D.getTransform();

        int startRow = Math.max(0, visibleBounds.y / fontHeight);
        int endRow = Math.min(rope.getLinesNum() - 1, (visibleBounds.y + visibleBounds.height) / fontHeight);

        /* Go trough lines that will be painted */
        int charIndexStart = rope.charIndexOfLineStart(startRow);
        int linesCountToRender = visibleBounds.height / fontHeight;

        Trie keywordsTree = KeywordsTrie.getKeyWordsTrie();
        keywordsTree.setIterator(rope.iterator(charIndexStart));
        Map<Integer, TokenType> reservedWordsSet = keywordsTree.isEmpty() ? new HashMap<>() : keywordsTree.getKeywordsIndexes(startRow, endRow);

        long start = System.currentTimeMillis();
        RopeIterator iterator = rope.iterator(charIndexStart);
        long end = System.currentTimeMillis();
        log.info("iterator: {}ms", end - start);

        int linesCountRendered = 0;
        int currentIndex = charIndexStart;

        int currentLineStartIndex = charIndexStart;

        int currentLineLength = 0;
        int currentLinePixelLength = 0;
        final int charHeight = graphics2D.getFontMetrics().getHeight();

        TextBufferBuilder textBufferBuilder = new TextBufferBuilder();

        while (iterator.hasNext() && linesCountRendered < linesCountToRender) {
            PreReadLineInfo preReadLineInfo = readLine(iterator, currentIndex);
            String line = preReadLineInfo.getStringBuilder().toString();
            if (preReadLineInfo.isCursorLine) {
                AffineTransform transform = graphics2D.getTransform();
                drawLineBackground(graphics2D, CURSOR_ROW_BACKGROUND_COLOR);
                graphics2D.setTransform(transform);
            }

            for (int i = 0; i < line.length(); i++) {
                Character c = line.charAt(i);
                currentLineLength++;
                currentLinePixelLength += graphics2D.getFontMetrics().charWidth(c);

                updateCursorPositionFromCoordinates(
                        graphics2D,
                        currentLinePixelLength,
                        currentIndex,
                        startRow + linesCountRendered,
                        c);

                if (currentIndex == model.getCursorPosition()) {
                    textBufferBuilder.withCursorChar(c);

                    drawPointer(graphics2D);
                    model.setCursorRect(new Rectangle(
                            currentLinePixelLength,
                            visibleBounds.y + linesCountRendered * charHeight,
                            POINTER_WIDTH,
                            charHeight
                    ));
                }

                if (!c.equals('\r')) {
                    if (c.equals('\n')) {
                        graphics2D.setTransform(affineTransform);
                        graphics2D.translate(0, charHeight);
                        affineTransform = graphics2D.getTransform();
                        linesCountRendered++;
                        textBufferBuilder.addLine(new LineInfo(currentLineStartIndex, currentLineLength - 1));

                        currentLineStartIndex = currentLineStartIndex + currentLineLength;
                        currentLineLength = 0;
                        currentLinePixelLength = 0;
                    } else {
                        if (reservedWordsSet.containsKey(iterator.getPos())) {
                            charColor = reservedWordsSet.get(iterator.getPos()).getColor();
                        } else {
                            charColor = DEFAULT_CHAR_COLOR;
                        }
                        drawChar(graphics2D, c, charColor, isInSelection(currentIndex) ? SELECTOR_COLOR : null);
                    }
                }

                currentIndex++;
            }
        }

        model.setTextBuffer(textBufferBuilder.build());
    }

    private PreReadLineInfo readLine(RopeIterator iterator, int currentIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isCursorLine = false;
        Character c;
        int counter = 0;
        boolean newLineMet = false;
        while (iterator.hasNext() && !newLineMet) {
            c = iterator.next();

            if (c.equals('\n')) {
                newLineMet = true;
            }

            stringBuilder.append(c);

            if (currentIndex + counter == model.getCursorPosition()) {
                isCursorLine = true;
            }

            counter++;
        }

        return new PreReadLineInfo(stringBuilder, isCursorLine);
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
        graphics2D.fillRect(0, 3, POINTER_WIDTH, graphics2D.getFontMetrics().getHeight());
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

    public Graphics2D getLatestGraphices() {
        return latestGraphices;
    }

    public void setMouseCursorPointer(Pointer mouseCursorPointer) {
        this.mouseCursorPointer = mouseCursorPointer;
    }

    public void setScrollToCursorOnceOnPaint(boolean scrollToCursorOnceOnPaint) {
        this.scrollToCursorOnceOnPaint = scrollToCursorOnceOnPaint;
    }

    static class PreReadLineInfo {
        private StringBuilder stringBuilder;
        private boolean isCursorLine;

        PreReadLineInfo(StringBuilder stringBuilder, boolean isCursorLine) {
            this.stringBuilder = stringBuilder;
            this.isCursorLine = isCursorLine;
        }

        public StringBuilder getStringBuilder() {
            return stringBuilder;
        }

        public boolean isCursorLine() {
            return isCursorLine;
        }
    }
}
