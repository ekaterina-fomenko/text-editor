package com.editor;

public class Main {

    public static void main(String[] args) {
        EditorFrame frame = new EditorFrame();
        frame.setVisible(true);
        System.out.println("!!!! x: "+frame.getBounds().getCenterX()+" y:"+frame.getBounds().getCenterY()+" height: "+frame.getBounds().getHeight());
    }
}
