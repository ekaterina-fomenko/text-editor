package com.editor.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TextEditorModelTest {
    TextEditorModel textEditorModel;


    @Before
    public void setUp() {
        textEditorModel = new TextEditorModel();

    }

    @Test
    public void lineBuildersToStringTestOnEmpty() {
        assertEquals(1, textEditorModel.getLineBuilders().size());
        StringBuilder line = textEditorModel.getLineBuilders().get(0);
        assertNotNull(line);
        assertTrue(line.toString().isEmpty());
        assertTrue(textEditorModel.lineBuildersToString().isEmpty());
    }

    @Test
    public void testAddTextWithOneLine() {
        String text = "text";
        textEditorModel.addText(text);

        assertEquals(1, textEditorModel.getLineBuilders().size());
        assertTrue(text.equals(textEditorModel.lineBuildersToString()));
    }

    @Test
    public void testAddTextWithSeveralLines(){
        String line0 = "line0";
        String line1 = "line1";
        String line2 = "line2";
        String line3 = "line3";

        StringBuilder text= new StringBuilder(line0);
        text.append("\n").append(line1);
        text.append("\n").append(line2);
        text.append("\n").append(line3);

        textEditorModel.addText(text.toString());

        List<StringBuilder> stringBuilders = textEditorModel.getLineBuilders();

        assertEquals(4, stringBuilders.size());

        StringBuilder resultLine0 = stringBuilders.get(0);
        StringBuilder resultLine1 = stringBuilders.get(1);
        StringBuilder resultLine2 = stringBuilders.get(2);
        StringBuilder resultLine3 = stringBuilders.get(3);

        assertNotNull(resultLine0);
        assertNotNull(resultLine1);
        assertNotNull(resultLine2);
        assertNotNull(resultLine3);

        assertTrue(line0.equals(stringBuilders.get(0).toString()));
        assertTrue(line1.equals(stringBuilders.get(1).toString()));
        assertTrue(line2.equals(stringBuilders.get(2).toString()));
        assertTrue(line3.equals(stringBuilders.get(3).toString()));


    }
}

