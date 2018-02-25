package com.editor;

import com.editor.model.RopeTextEditorModel;

import javax.swing.*;
import java.awt.*;

public class TextArea {
    public JScrollPane jScrollPane;
    //    public DrawComponent jComponent;
    public RopeDrawComponent ropeDrawComponent;

    //    public TextEditorModel model;
    public RopeTextEditorModel ropeModel;
    public JFrame frame;
    private DrawComponentMouseListener mouseListener;

    public TextArea(JFrame frame) {
        this.frame = frame;
        ropeDrawComponent = new RopeDrawComponent();

        RopeTextEditorModel.setStringSizeProvider((text, offset, count) -> {
            Graphics2D graphics = ropeDrawComponent.getLatestGraphices();
            if (graphics == null) {
                return 0;
            }

            FontMetrics fontMetrics = graphics.getFontMetrics();
            int result = fontMetrics.charsWidth(text, offset, count);
            return result;
        });
        ropeModel = new RopeTextEditorModel();
        ropeDrawComponent.setModel(ropeModel);
        ropeDrawComponent.setActionMap(new TextActionMap(ropeModel, this));
        ropeDrawComponent.setInputMap(JComponent.WHEN_FOCUSED, new TextInputMap());
        mouseListener = new DrawComponentMouseListener(this, ropeDrawComponent);
        createJScRollPane();
    }

    private void createJScRollPane() {

        jScrollPane = new JScrollPane(ropeDrawComponent);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jScrollPane.getHorizontalScrollBar().addAdjustmentListener(listener -> {
            ropeDrawComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());
            render(false);
        });

        jScrollPane.getVerticalScrollBar().addAdjustmentListener(listener -> {
            ropeDrawComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());
            render(false);
        });

        ropeDrawComponent.addMouseListener(mouseListener);
        ropeDrawComponent.addMouseMotionListener(mouseListener);
        ropeDrawComponent.setCursor(new Cursor(Cursor.TEXT_CURSOR));
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
        this.ropeDrawComponent.revalidate();
        this.ropeDrawComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());
        this.ropeDrawComponent.repaint();
        this.ropeDrawComponent.setScrollToCursorOnceOnPaint(forceScrollToCursor);
//todo: remove
/*        if (!SyntaxParser.isTextSyntax()) {
            ropeModel.updatePairedBrackets();
        }*/

    }
}
