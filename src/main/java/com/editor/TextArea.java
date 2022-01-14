package com.editor;

import com.editor.model.RopeTextEditorModel;
import com.editor.model.undo.UndoRedoService;
import com.editor.system.ClipboardAdapterImpl;
import com.editor.system.ClipboardAdapter;
import com.editor.system.ClipboardSystemApiImpl;

import javax.swing.*;
import java.awt.*;

public class TextArea implements Renderer {
    private final RopeDrawComponent ropeDrawComponent;
    private final JFrame frame;
    private final JScrollPane jScrollPane;

    public TextArea(JFrame frame,
                    RopeTextEditorModel ropeModel,
                    UndoRedoService undoRedoService,
                    JScrollPane jScrollPane,
                    RopeDrawComponent ropeDrawComponent) {
        this.frame = frame;
        this.jScrollPane = jScrollPane;
        this.ropeDrawComponent = ropeDrawComponent;
        ClipboardAdapter clipboardAdapter = new ClipboardAdapterImpl(new ClipboardSystemApiImpl());
        TextActionMap actionMap = new TextActionMap(ropeModel, this, undoRedoService, clipboardAdapter, this.ropeDrawComponent);

        RopeTextEditorModel.setStringSizeProvider((text, offset, count) -> {
            Graphics2D graphics = this.ropeDrawComponent.getLatestGraphics();
            if (graphics == null) {
                return 0;
            }

            FontMetrics fontMetrics = graphics.getFontMetrics();
            return fontMetrics.charsWidth(text, offset, count);
        });

        this.ropeDrawComponent.setActionMap(actionMap);
        this.ropeDrawComponent.setInputMap(JComponent.WHEN_FOCUSED, new TextInputMap());
    }

    @Override
    public void render() {
        render(true);
    }

    /**
     * Calls when need to repaint graphics and move scroll bar to cursor position.
     * Also update paired brackets indexes.
     *
     * @param forceScrollToCursor says if we need to move scroll to cursor position
     */

    @Override
    public void render(boolean forceScrollToCursor) {
        this.ropeDrawComponent.revalidate();
        this.ropeDrawComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());
        this.ropeDrawComponent.repaint();
        this.ropeDrawComponent.setScrollToCursorOnceOnPaint(forceScrollToCursor);
    }

    @Override
    public JFrame getFrame() {
        return frame;
    }
}
