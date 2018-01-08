package com.editor;

import com.editor.model.RopeTextEditorModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

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

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        /* Go trough lines that will be painted*/
        Iterator<Character> iterator = model.getRope().iterator(0);
        while (iterator.hasNext()) {
            Character c = iterator.next();
            drawChar(graphics2D, c, DEFAULT_CHAR_COLOR, null);
        }
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
