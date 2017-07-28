package com.editor;

import com.editor.model.TextEditorModel;
import com.editor.parser.SyntaxParser;

import javax.swing.*;
import java.awt.*;

public class TextArea {
    public JScrollPane jScrollPane;
    public DrawComponent jComponent;

    public TextEditorModel model;
    public JFrame frame;
    private DrawComponentMouseListener mouseListener;

    public TextArea(JFrame frame) {
        this.frame = frame;
        model = new TextEditorModel();

        jComponent = new DrawComponent(model);
        jComponent.setActionMap(new TextActionMap(model, this));
        jComponent.setInputMap(JComponent.WHEN_FOCUSED, new TextInputMap());
        mouseListener = new DrawComponentMouseListener(this, jComponent, model);

        createJScRollPane();

    }

    private void createJScRollPane() {

        jScrollPane = new JScrollPane(jComponent);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jScrollPane.getHorizontalScrollBar().addAdjustmentListener(listener -> {
            jComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());
        });

        jScrollPane.getVerticalScrollBar().addAdjustmentListener(listener -> {
            jComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());
        });

        jComponent.addMouseListener(mouseListener);
        jComponent.addMouseMotionListener(mouseListener);
        jComponent.setCursor(new Cursor(Cursor.TEXT_CURSOR));
    }

    public void render() {
        render(true);
    }

    /**
     * Calls when need to repaint graphics and move scroll bar to cursor position.
     * Also update paired brackets indexes.
     *
     * @param forceScrollToCursor says if we need to move scroll to cursor position
     */

    public void render(boolean forceScrollToCursor) {
        this.jComponent.revalidate();
        this.jComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());
        this.jComponent.repaint();
        this.jComponent.setScrollToCursorOnceOnPaint(forceScrollToCursor);

        if (!SyntaxParser.isTextSyntax()) {
            model.updatePairedBrackets();
        }

    }
}
