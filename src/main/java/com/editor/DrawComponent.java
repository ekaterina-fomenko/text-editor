package main.java.com.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DrawComponent extends JComponent {
    private StringBuilder stringBuilder;

public DrawComponent(StringBuilder stringBuilder){
    this.stringBuilder = stringBuilder;
}
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setFont(Font.getFont(Font.MONOSPACED));
        //graphics2D.setColor(new Color(99, 74, 68));
        graphics2D.setTransform(new AffineTransform());
        for(int i = 0; i< stringBuilder.length(); i++){
            char currentChar = stringBuilder.charAt(i);
           graphics2D.drawString(Character.toString(currentChar),5,15);
           graphics2D.translate(graphics2D.getFontMetrics().charWidth(currentChar), 0);
        }
       // graphics2D.drawString(stringBuilder.toString(),5,15);
    }
}
