package com.editor;

import com.editor.menu.MenuBar;
import com.editor.model.FileManager;
import com.editor.model.FileManagerImpl;
import com.editor.model.RopeTextEditorModel;
import com.editor.model.undo.UndoRedoService;

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

    private final DrawComponentMouseListener mouseListener;
    private final JScrollPane jScrollPane;

    private final TextArea textArea;
    private final MenuBar menuBar;

    private final EditorSettings editorSettings;
    private final FileManagerImpl fileManager;
    private final RopeTextEditorModel model;

    public EditorFrame() {
        super(TITLE);

        setBounds(X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        model = new RopeTextEditorModel();
        UndoRedoService undoRedoService = new UndoRedoService(model);

        editorSettings = new EditorSettings();
        RopeDrawComponent ropeDrawComponent = new RopeDrawComponent(editorSettings, model);

        jScrollPane = createJScRollPane(ropeDrawComponent);
        textArea = new TextArea(this, model, undoRedoService, jScrollPane, ropeDrawComponent);

        Container pane = getContentPane();
        pane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        pane.setLayout(new BorderLayout());
        pane.add(jScrollPane, BorderLayout.CENTER);

        fileManager = new FileManagerImpl(this, model, editorSettings);
        menuBar = new MenuBar(textArea, editorSettings, fileManager, undoRedoService);
        mouseListener = new DrawComponentMouseListener(textArea, ropeDrawComponent, model);

        setJMenuBar(menuBar.getMenuBar());
    }

    private JScrollPane createJScRollPane(RopeDrawComponent ropeDrawComponent) {
        JScrollPane jScrollPane = new JScrollPane(ropeDrawComponent);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jScrollPane.getHorizontalScrollBar().addAdjustmentListener(listener -> {
            ropeDrawComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());

            textArea.render(false);
        });

        jScrollPane.getVerticalScrollBar().addAdjustmentListener(listener -> {
            ropeDrawComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());

            textArea.render(false);
        });

        ropeDrawComponent.addMouseListener(mouseListener);
        ropeDrawComponent.addMouseMotionListener(mouseListener);
        ropeDrawComponent.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        return jScrollPane;
    }

    public RopeTextEditorModel getModel() {
        return model;
    }

    public void renderTextArea() {
        textArea.render();
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
