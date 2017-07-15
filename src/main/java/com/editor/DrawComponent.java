package main.java.com.editor;

import main.java.com.editor.model.Pointer;
import main.java.com.editor.model.TextEditorModel;
import main.java.com.editor.parser.CommonSyntaxHighlight;
import main.java.com.editor.parser.JavaScriptSyntax;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DrawComponent extends JComponent {
    private StringBuilder stringBuilder;
    private Pointer pointer;
    public static final Color DEFAULT_CHAR_COLOR = Color.black;
    //public static final int DEFAULT_X_COORDINATE = 5;
    public static final int DEFAULT_Y_COORDINATE = 15;

    public DrawComponent(TextEditorModel model) {
        this.stringBuilder = model.getTextBuilder();
        this.pointer = model.getPointer();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        //graphics2D.setColor(new Color(99, 74, 68));
        JavaScriptSyntax js = new JavaScriptSyntax();
        java.util.List<CommonSyntaxHighlight> reservedWordsList = js.getReservedWordsHighlight2(stringBuilder.toString());
        char currentReservedWordIndex = 0;
        Color currentCharColor;
        AffineTransform affineTransform = graphics2D.getTransform();
        for (int i = 0; i < stringBuilder.length(); i++) {

            currentCharColor = DEFAULT_CHAR_COLOR;
            char currentChar = stringBuilder.charAt(i);

            if (pointer.index == i) {
                drawPointer(graphics2D);
            }

            if (currentChar == '\n') {
                graphics2D.setTransform(affineTransform);
                graphics2D.translate(0, graphics2D.getFontMetrics().getHeight());
                affineTransform = graphics2D.getTransform();
                continue;
            }

            if (currentReservedWordIndex < reservedWordsList.size()) {
                CommonSyntaxHighlight currentReservedWord = reservedWordsList.get(currentReservedWordIndex);
                if (currentReservedWord.getStartIndex() <= i && i <= currentReservedWord.getEndIndex()) {
                    currentCharColor = Color.PINK;
                    if (i == currentReservedWord.getEndIndex()) {
                        currentReservedWordIndex++;
                    }
                }
            }

            //ToDo: When add selector - implement here
            //if(selected){
            //    setBackgroundColor(Blue);
            //}
            drawChar(graphics2D, currentChar, currentCharColor);
        }

        if (pointer.index == stringBuilder.length()) {
            drawPointer(graphics2D);
        }
    }

//    private boolean blink;

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
