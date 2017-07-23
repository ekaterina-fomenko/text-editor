package com.editor;

import com.editor.model.Pointer;
import com.editor.model.TextEditorModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawComponentMouseListener implements MouseListener, MouseMotionListener {
    private final DrawComponent drawComponent;
    private final TextEditorModel model;

    public DrawComponentMouseListener(DrawComponent drawComponent, TextEditorModel model) {
        this.drawComponent = drawComponent;
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        model.dropSelection();
        drawComponent.setMouseCursorPointer(new Pointer(e.getY(), e.getX()));
        drawComponent.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawComponent.setMouseSelectionEndPointer(new Pointer(e.getY(), e.getX()));
        drawComponent.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        drawComponent.setMouseSelectionEndPointer(new Pointer(e.getY(), e.getX()));
        drawComponent.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
