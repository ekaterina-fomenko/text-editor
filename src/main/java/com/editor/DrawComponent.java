package main.java.com.editor;

import main.java.com.editor.parser.CommonSyntaxHighlight;
import main.java.com.editor.parser.JavaScriptSyntax;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DrawComponent extends JComponent {
    private StringBuilder stringBuilder;
    public static final Color DEFAULT_COLOR = Color.black;
    public static final int DEFAULT_X_COORDINATE = 5;
    public static final int DEFAULT_Y_COORDINATE = 15;

    public DrawComponent(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(Font.getFont(Font.MONOSPACED));
        //graphics2D.setColor(new Color(99, 74, 68));
        JavaScriptSyntax js = new JavaScriptSyntax();
        Map<Integer, CommonSyntaxHighlight> map = js.getReservedWordsHighlight(stringBuilder.toString());
        for (int i = 0; i < stringBuilder.length(); i++) {
            int endIndex = i;
            if (map.containsKey(i)) {
                endIndex = drawSyntax(graphics2D, map, i);
            }
            i = endIndex;
            if (i < stringBuilder.length()) {
                char currentChar = stringBuilder.charAt(i);
                drawChar(graphics2D, currentChar, DEFAULT_COLOR);
            }
        }
    }

    private int drawSyntax(Graphics2D graphics2D, Map<Integer, CommonSyntaxHighlight> map, int i) {
        int endIndex;
        CommonSyntaxHighlight commonSyntaxHighlight = map.get(i);
        for (int k = commonSyntaxHighlight.getStartIndex(); k < commonSyntaxHighlight.getEndIndex(); k++) {
            char currentChar = stringBuilder.charAt(k);
            drawChar(graphics2D, currentChar, commonSyntaxHighlight.getStartColor());
        }
        endIndex = commonSyntaxHighlight.getEndIndex();
        return endIndex;
    }

    private void drawChar(Graphics2D graphics2D, char currentChar, Color color) {
        graphics2D.setColor(color);
        graphics2D.drawString(Character.toString(currentChar), DEFAULT_X_COORDINATE, DEFAULT_Y_COORDINATE);
        graphics2D.translate(graphics2D.getFontMetrics().charWidth(currentChar), 0);
    }
}
