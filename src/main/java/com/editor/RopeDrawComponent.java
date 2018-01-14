package com.editor;

import com.editor.model.RopeTextEditorModel;
import com.editor.model.rope.Rope;
import com.editor.system.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Optional;

/**
 * Main class which redraw all in text area.
 */

public class RopeDrawComponent extends JComponent {

    private RopeTextEditorModel model;

    public static final Color DEFAULT_CHAR_COLOR = Color.black;

    public static final int DEFAULT_Y_COORDINATE = 15;

    public static Logger log = LoggerFactory.getLogger(RopeDrawComponent.class);

    private Rectangle visibleBounds;

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

        Rope rope = model.getRope();

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        AffineTransform affineTransform = graphics2D.getTransform();

        int fontHeight = graphics.getFontMetrics().getHeight();
        int startRow = Math.max(0, visibleBounds.y / fontHeight - 1);
        int endRow = Math.min(rope.getLinesNum() - 1, (visibleBounds.y + visibleBounds.height) / fontHeight - 1);

        /* Go trough lines that will be painted*/
        int charIndexStart = rope.charIndexOfLineStart(startRow);
        int linesCountToRender = visibleBounds.height / fontHeight;

        Iterator<Character> iterator = rope.iterator(charIndexStart);
        long drawStart = System.currentTimeMillis();
        int linesCountRendered = 0;
        while (iterator.hasNext() && linesCountRendered < linesCountToRender) {
            Character c = iterator.next();
            Character cNext = iterator.hasNext() ? iterator.next() : null;

            if (areNewLine(c, cNext)) {
                graphics2D.setTransform(affineTransform);
                graphics2D.translate(0, graphics2D.getFontMetrics().getHeight());
                affineTransform = graphics2D.getTransform();
                linesCountRendered++;
            } else {
                drawChar(graphics2D, c, DEFAULT_CHAR_COLOR, null);
                if (cNext != null) {
                    drawChar(graphics2D, cNext, DEFAULT_CHAR_COLOR, null);
                }
            }
        }
        long drawEnd = System.currentTimeMillis();
        log.info(MessageFormat.format("Drawn: {0} lines, {1}ms", linesCountRendered, drawEnd - drawStart));
    }

    private boolean areNewLine(Character c, Character cNext) {
        return c == SystemConstants.NEW_LINE.charAt(0) && SystemConstants.NEW_LINE.length() == 1 ||
                (c == SystemConstants.NEW_LINE.charAt(0) && cNext == SystemConstants.NEW_LINE.charAt(1));
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
}
