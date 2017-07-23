package com.editor;

import com.editor.model.Pointer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawComponentMouseListener implements MouseListener {
    private final DrawComponent drawComponent;

    public DrawComponentMouseListener(DrawComponent drawComponent) {
        this.drawComponent = drawComponent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Pointer mouseClickedPointer = new Pointer(e.getY(), e.getX());
        System.out.println("Mouse clicked on: " + mouseClickedPointer);

        drawComponent.setMouseClickedPointer(mouseClickedPointer);
        drawComponent.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
