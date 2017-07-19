package com.editor;

import com.editor.menu.MenuBar;

import javax.swing.*;
import java.awt.*;

public class EditorFrame extends JFrame {

    public static final String TITLE = "Kate Editor";
    public static final int WIDTH = 500;
    public static final int HEIGHT = 700;
    public static final int X_COORDINATE = 100;
    public static final int Y_COORDINATE = 100;

    public TextArea textArea;
    public com.editor.menu.MenuBar menuBar;

    public EditorFrame() {
        super(TITLE);
        setBounds(X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new TextArea(this);

        Container pane = getContentPane();
        pane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pane.setLayout(new BorderLayout());
        pane.add(textArea.jScrollPane, BorderLayout.CENTER);

        menuBar = new MenuBar(textArea.jComponent);
        setJMenuBar(menuBar.getMenuBar());
    }


}
