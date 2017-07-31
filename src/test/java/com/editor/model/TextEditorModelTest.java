package com.editor.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TextEditorModelTest {

    private TextEditorModel textEditorModel;

    private String line0;
    private String line1;
    private String line2;
    private String line3;

    private StringBuilder defaultTextBuilder;
    private List<StringBuilder> defaultStringBuilderList;
    private int defaultStringBuilderSize;


    @Before
    public void setUp() {
        textEditorModel = new TextEditorModel();
        defaultStringBuilderList = new ArrayList<>();

        line0 = "line0";
        line1 = "line01";
        line2 = "line012";
        line3 = "line1123";

        defaultTextBuilder = new StringBuilder(line0);
        defaultTextBuilder.append("\n").append(line1);
        defaultTextBuilder.append("\n").append(line2);
        defaultTextBuilder.append("\n").append(line3);

        defaultStringBuilderList.add(new StringBuilder(line0));
        defaultStringBuilderList.add(new StringBuilder(line1));
        defaultStringBuilderList.add(new StringBuilder(line2));
        defaultStringBuilderList.add(new StringBuilder(line3));

        defaultStringBuilderSize = defaultStringBuilderList.size();


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
    public void testAddTextWithSeveralLines() {
        textEditorModel.addText(defaultTextBuilder.toString());

        List<StringBuilder> resultStringBuildersList = textEditorModel.getLineBuilders();

        assertEquals(defaultStringBuilderSize, resultStringBuildersList.size());


        StringBuilder resultLine0 = resultStringBuildersList.get(0);
        StringBuilder resultLine1 = resultStringBuildersList.get(1);
        StringBuilder resultLine2 = resultStringBuildersList.get(2);
        StringBuilder resultLine3 = resultStringBuildersList.get(3);

        assertNotNull(resultLine0);
        assertNotNull(resultLine1);
        assertNotNull(resultLine2);
        assertNotNull(resultLine3);

        assertTrue(line0.equals(resultLine0.toString()));
        assertTrue(line1.equals(resultLine1.toString()));
        assertTrue(line2.equals(resultLine2.toString()));
        assertTrue(line3.equals(resultLine3.toString()));

        int lastRow = defaultStringBuilderList.size() - 1;

        assertEquals(lastRow, textEditorModel.getCursorPosition().row);
        assertEquals(defaultStringBuilderList.get(lastRow).length(), textEditorModel.getCursorPosition().column);

    }

    @Test
    public void testSetLineBuildersFromFile() {
        textEditorModel.addText("text");
        textEditorModel.setLineBuildersFromFile(defaultStringBuilderList);

        List<StringBuilder> resultStringBuildersList = textEditorModel.getLineBuilders();

        assertEquals(defaultStringBuilderSize, resultStringBuildersList.size());


        StringBuilder resultLine0 = resultStringBuildersList.get(0);
        StringBuilder resultLine1 = resultStringBuildersList.get(1);
        StringBuilder resultLine2 = resultStringBuildersList.get(2);
        StringBuilder resultLine3 = resultStringBuildersList.get(3);

        assertNotNull(resultLine0);
        assertNotNull(resultLine1);
        assertNotNull(resultLine2);
        assertNotNull(resultLine3);

        assertTrue(line0.equals(resultLine0.toString()));
        assertTrue(line1.equals(resultLine1.toString()));
        assertTrue(line2.equals(resultLine2.toString()));
        assertTrue(line3.equals(resultLine3.toString()));

        assertEquals(0, textEditorModel.getCursorPosition().row);
        assertEquals(0, textEditorModel.getCursorPosition().column);
    }

    @Test
    public void testMovePointerToInitAndLastPositions() {
        textEditorModel.addText(defaultTextBuilder.toString());

        Pointer cursorPosition = textEditorModel.getCursorPosition();
        textEditorModel.movePointerToInitPosition();

        assertEquals(0, cursorPosition.column);
        assertEquals(0, cursorPosition.row);

        textEditorModel.movePointerToLastPosition();

        int lastRow = defaultStringBuilderList.size() - 1;

        assertEquals(lastRow, cursorPosition.row);
        assertEquals(defaultStringBuilderList.get(lastRow).length(), cursorPosition.column);

    }

    @Test
    public void testMovePointerToStartAndEndOfLine() {
        textEditorModel.addText(defaultTextBuilder.toString());
        int row = 2;
        int column = 3;
        Pointer initCursorPosition = new Pointer(row, column);
        textEditorModel.setCursorPosition(initCursorPosition);

        Pointer resultCursorPosition = textEditorModel.getCursorPosition();

        textEditorModel.movePointerToStartOfLine();

        assertEquals(row, resultCursorPosition.row);
        assertEquals(0, resultCursorPosition.column);

        textEditorModel.movePointerToTheEndOfLine();

        assertEquals(row, resultCursorPosition.row);
        assertEquals(defaultStringBuilderList.get(row).length(), resultCursorPosition.column);
    }
}

