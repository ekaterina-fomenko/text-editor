package com.editor;

import com.editor.model.Pointer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawComponentMouseListener implements MouseListener, MouseMotionListener {
    private final DrawComponent drawComponent;
    private boolean isMouseDown;

    public DrawComponentMouseListener(DrawComponent drawComponent) {
        this.drawComponent = drawComponent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Pointer mouseClickedPointer = new Pointer(e.getY(), e.getX());
        System.out.println("Mouse clicked on: " + mouseClickedPointer);
//
//        drawComponent.setMouseCursorPointer(mouseClickedPointer);
//        drawComponent.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        isMouseDown = true;
        drawComponent.setMouseCursorPointer(new Pointer(e.getY(), e.getX()));
        drawComponent.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isMouseDown = false;
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

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isMouseDown) {
            drawComponent.setMouseSelectionEndPointer(new Pointer(e.getY(), e.getX()));
            drawComponent.repaint();
        }
    }
}
