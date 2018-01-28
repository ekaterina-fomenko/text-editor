package com.editor;

import com.editor.model.RopeTextEditorModel;
import com.editor.model.rope.RopeApi;
import com.editor.model.rope.RopeIterator;
import com.editor.system.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.text.MessageFormat;

/**
 * Main class which redraw all in text area.
 */

public class RopeDrawComponent extends JComponent {

    private RopeTextEditorModel model;

    public static final int POINTER_WIDTH = 2;

    public static final Color DEFAULT_CHAR_COLOR = Color.black;

    public static final int DEFAULT_Y_COORDINATE = 15;

    public static Logger log = LoggerFactory.getLogger(RopeDrawComponent.class);

    private Rectangle visibleBounds = new Rectangle();

    private Graphics2D latestGraphices = null;

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

        Graphics2D graphics2D = (Graphics2D) graphics;
        latestGraphices = graphics2D;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        updatePreferredSize(graphics);

        int fontHeight = graphics.getFontMetrics().getHeight();

        graphics2D.translate(0, Math.max(0, visibleBounds.y - fontHeight) - visibleBounds.y % fontHeight);
        AffineTransform affineTransform = graphics2D.getTransform();

        int startRow = Math.max(0, visibleBounds.y / fontHeight - 1);
        int endRow = Math.min(rope.getLinesNum() - 1, (visibleBounds.y + visibleBounds.height) / fontHeight - 1);

        /* Go trough lines that will be painted*/
        int charIndexStart = rope.charIndexOfLineStart(startRow);
        int linesCountToRender = visibleBounds.height / fontHeight + 1;

        long start = System.currentTimeMillis();
        RopeIterator iterator = rope.iterator(charIndexStart);
        long end = System.currentTimeMillis();
        log.info("iterator: {}ms", end - start);
        long drawStart = System.currentTimeMillis();
        int linesCountRendered = 0;
        int currentIndex = charIndexStart;
        while (iterator.hasNext() && linesCountRendered < linesCountToRender) {
            Character c = iterator.next();

            if (c.equals(Constants.NEW_LINE_CHAR)) {
                graphics2D.setTransform(affineTransform);
                graphics2D.translate(0, graphics2D.getFontMetrics().getHeight());
                affineTransform = graphics2D.getTransform();
                linesCountRendered++;
            } else {
                drawChar(graphics2D, c, DEFAULT_CHAR_COLOR, null);
            }

            if (currentIndex == model.getCursorPosition()) {
                drawPointer(graphics2D);
            }

            currentIndex++;
        }
        long drawEnd = System.currentTimeMillis();
        log.info(MessageFormat.format("Drawn: {0} lines, {1}ms", linesCountRendered, drawEnd - drawStart));
    }

    private void drawPointer(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 3, POINTER_WIDTH, graphics2D.getFontMetrics().getHeight());
    }

    private void updatePreferredSize(Graphics graphics) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int height = fontMetrics.getHeight() * (model.getRope().getLinesNum() + 1);
        int width = model.getRope().getMaxLineLength();

//        for (StringBuilder line : model.getLineBuilders()) {
//            int length = line.length();
//            int horSpaceOffset = length > 0 ? fontMetrics.charWidth(line.charAt(length - 1)) : 0;
//            width = Math.max(width, fontMetrics.stringWidth(line.toString())) + horSpaceOffset;
//        }

        setPreferredSize(new Dimension(width, height));
    }

    private boolean areNewLine(Character c, Character cNext) {
        return c == Constants.NEW_LINE.charAt(0) && Constants.NEW_LINE.length() == 1 ||
                (c == Constants.NEW_LINE.charAt(0) && cNext == Constants.NEW_LINE.charAt(1));
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

    public void setModel(RopeTextEditorModel model) {
        this.model = model;
    }

    public Graphics2D getLatestGraphices() {
        return latestGraphices;
    }
}
