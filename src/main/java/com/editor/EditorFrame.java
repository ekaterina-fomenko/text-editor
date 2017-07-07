package main.java.com.editor;

import javax.swing.*;
import java.awt.*;

public class EditorFrame extends JFrame {

    public static final String TITLE = "Code Editor";
    public static final int WIDTH = 500;
    public static final int HEIGHT = 700;
    public static final int X_COORDINATE = 100;
    public static final int Y_COORDINATE = 100;

    public TextArea textArea;

    public EditorFrame(){
        super(TITLE);
        setBounds(X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new TextArea();

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(textArea.jScrollPane, BorderLayout.CENTER);

        setJMenuBar(textArea.menuBar);

    }

}
