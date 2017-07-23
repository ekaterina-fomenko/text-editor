package com.editor;

import com.editor.model.TextEditorModel;
import com.editor.parser.SyntaxParser;

import javax.swing.*;

public class TextArea {
    public JScrollPane jScrollPane;
    public DrawComponent jComponent;

    private TextEditorModel model;
    public JFrame frame;

    private SyntaxParser syntaxParser;

    public TextArea(JFrame frame) {
        this.frame = frame;
        syntaxParser = new SyntaxParser();
        model = new TextEditorModel();
        jComponent = new DrawComponent(model);
        jComponent.setActionMap(new TextActionMap(model, this));
        jComponent.setInputMap(JComponent.WHEN_FOCUSED, new TextInputMap());
        jScrollPane = new JScrollPane(jComponent);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public void render(boolean isNeedToReCountReservedWords) {
        System.out.println("!!!! x: " + frame.getBounds().getCenterX() + " y:" + frame.getBounds().getCenterY() + " height: " + frame.getBounds().getHeight());
        DrawComponent jComponent = this.jComponent;

        if(isNeedToReCountReservedWords){
            model.setReservedWordsList(syntaxParser.getReservedWordsHighlight(model));
        }

        jComponent.revalidate();
        jComponent.repaint();
        jComponent.setScrollToCursorOnceOnPaint(true);
        if (!SyntaxParser.isTextSyntax()) {
            model.updatePairedBrackets();
        }
        jComponent.setVisibleBounds(jScrollPane.getViewport().getViewRect());
    }
}
