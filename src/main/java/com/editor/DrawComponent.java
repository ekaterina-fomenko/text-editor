package main.java.com.editor;

import main.java.com.editor.model.Pointer;
import main.java.com.editor.model.TextEditorModel;
import main.java.com.editor.parser.CommonSyntaxHighlight;
import main.java.com.editor.parser.JavaScriptSyntax;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DrawComponent extends JComponent {
    private TextEditorModel model;
    private Pointer pointer;
    public static final Color DEFAULT_CHAR_COLOR = Color.black;
    public static final int DEFAULT_Y_COORDINATE = 15;

    public DrawComponent(TextEditorModel model) {
        this.model = model;
        this.pointer = model.getPointer();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        //graphics2D.setColor(new Color(99, 74, 68));
        JavaScriptSyntax js = new JavaScriptSyntax();
        java.util.List<CommonSyntaxHighlight> reservedWordsList = js.getReservedWordsHighlight2(model);
        char currentReservedWordIndex = 0;
        AffineTransform affineTransform = graphics2D.getTransform();
        java.util.ArrayList<StringBuilder> lineBuilders = model.getLineBuilders();
        for (int row = 0; row < lineBuilders.size(); row++) {
            StringBuilder lineBuilder = lineBuilders.get(row);
            for (int column = 0; column < lineBuilder.length(); column++) {
                char ch = lineBuilder.charAt(column);

                if (model.getPointer().row == row && model.getPointer().column == column) {
                    drawPointer(graphics2D);
                }

                Color charColor = DEFAULT_CHAR_COLOR;

                if (currentReservedWordIndex < reservedWordsList.size()) {
                    CommonSyntaxHighlight currentReservedWord = reservedWordsList.get(currentReservedWordIndex);
                    if (currentReservedWord.getRowIndex() == row && currentReservedWord.getStartIndex() <= column && column <= currentReservedWord.getEndIndex()) {
                        charColor = Color.PINK;
                        if (column == currentReservedWord.getEndIndex()) {
                            currentReservedWordIndex++;
                        }
                    }
                }

                drawChar(graphics2D, ch, charColor);
            }

            if (pointer.row == row && pointer.column == lineBuilder.length()) {
                drawPointer(graphics2D);
            }

            if (row < lineBuilders.size()) {
                graphics2D.setTransform(affineTransform);
                graphics2D.translate(0, graphics2D.getFontMetrics().getHeight());
                affineTransform = graphics2D.getTransform();
            }

        }
        //ToDo: When add selector - implement here
        //if(selected){
        //    setBackgroundColor(Blue);
        //}

    }

    private void drawChar(Graphics2D graphics2D, char currentChar, Color color) {
//        if (!(blink = !blink)) {
//            graphics2D.setColor(Color.LIGHT_GRAY);
//            graphics2D.fillRect(0,
//                    3,
//                    graphics2D.getFontMetrics().charWidth(currentChar),
//                    graphics2D.getFontMetrics().getHeight());
//        }

        graphics2D.setColor(color);
        graphics2D.drawString(Character.toString(currentChar), 0, DEFAULT_Y_COORDINATE);
        graphics2D.translate(graphics2D.getFontMetrics().charWidth(currentChar), 0);
        graphics2D.setColor(DEFAULT_CHAR_COLOR);
    }

    private void drawPointer(Graphics2D graphics2D) {
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(0, 3, 2, graphics2D.getFontMetrics().getHeight());
    }
}
