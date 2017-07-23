package com.editor;

import com.editor.model.Pointer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawComponentMouseListener implements MouseListener, MouseMotionListener {
    private final DrawComponent drawComponent;

    public DrawComponentMouseListener(DrawComponent drawComponent) {
        this.drawComponent = drawComponent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Pointer mouseClickedPointer = new Pointer(e.getY(), e.getX());
        System.out.println("Mouse clicked on: " + mouseClickedPointer);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        drawComponent.setMouseSelectionEndPointer(null);
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
