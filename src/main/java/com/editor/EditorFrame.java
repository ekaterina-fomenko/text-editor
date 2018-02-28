package com.editor;

import com.editor.menu.MenuBar;

import javax.swing.*;
import java.awt.*;

/**
 * Frame description
 */

public class EditorFrame extends JFrame {

    private static final String TITLE = "Kate Editor";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 700;
    private static final int X_COORDINATE = 100;
    private static final int Y_COORDINATE = 100;

    public TextArea textArea;
    public MenuBar menuBar;

    private EditorSettings editorSettings = new EditorSettings();

    public EditorFrame() {
        super(TITLE);

        setBounds(X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new TextArea(this, editorSettings);

        createContainerPane();

        menuBar = new MenuBar(textArea, editorSettings);
        setJMenuBar(menuBar.getMenuBar());
    }

    private void createContainerPane() {
        Container pane = getContentPane();
        pane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pane.setLayout(new BorderLayout());
        pane.add(textArea.jScrollPane, BorderLayout.CENTER);
    }
}
