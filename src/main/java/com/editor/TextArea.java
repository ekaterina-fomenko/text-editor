package com.editor;

import com.editor.model.RopeTextEditorModel;
import com.editor.model.undo.UndoRedoService;
import com.editor.system.ClipboardAdapterImpl;
import com.editor.system.ClipboardAdapter;
import com.editor.system.ClipboardSystemApiImpl;

import javax.swing.*;
import java.awt.*;

public class TextArea {
    public JScrollPane jScrollPane;
    public RopeDrawComponent ropeDrawComponent;
    public RopeTextEditorModel ropeModel;
    public JFrame frame;
    private DrawComponentMouseListener mouseListener;
    public UndoRedoService undoRedoService;

    public TextArea(JFrame frame, EditorSettings editorSettings) {
        this.frame = frame;
        ropeModel = new RopeTextEditorModel();
        ropeDrawComponent = new RopeDrawComponent(editorSettings, ropeModel);
        ClipboardAdapter clipboardAdapter = new ClipboardAdapterImpl(new ClipboardSystemApiImpl());
        TextActionMap actionMap = new TextActionMap(ropeModel, this, undoRedoService, clipboardAdapter);

        RopeTextEditorModel.setStringSizeProvider((text, offset, count) -> {
            Graphics2D graphics = ropeDrawComponent.getLatestGraphics();
            if (graphics == null) {
                return 0;
            }

            FontMetrics fontMetrics = graphics.getFontMetrics();
            int result = fontMetrics.charsWidth(text, offset, count);
            return result;
        });

        undoRedoService = new UndoRedoService(ropeModel);
        ropeDrawComponent.setActionMap(actionMap);
        ropeDrawComponent.setInputMap(JComponent.WHEN_FOCUSED, new TextInputMap());
        mouseListener = new DrawComponentMouseListener(this, ropeDrawComponent, ropeModel);
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
