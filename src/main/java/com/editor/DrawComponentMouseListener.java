package com.editor;

import com.editor.model.RopeTextEditorModel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Process mouse actions in text area, like mouse click and select by mouse
 */

public class DrawComponentMouseListener implements MouseListener, MouseMotionListener {
    private final TextArea textArea;
    private final RopeDrawComponent drawComponent;
    private final RopeTextEditorModel model;

    public DrawComponentMouseListener(TextArea textArea, RopeDrawComponent drawComponent, RopeTextEditorModel model) {
        this.textArea = textArea;
        this.drawComponent = drawComponent;
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        model.dropSelection();
        drawComponent.setMouseCursorPointer(new Point(e.getX(), e.getY()));
        textArea.render(false);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        drawComponent.setMouseSelectionEndPointer(new Pointer(e.getY(), e.getX()));
        textArea.render(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        drawComponent.setMouseSelectionEndPointer(new Pointer(e.getY(), e.getX()));
        textArea.render(false);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
