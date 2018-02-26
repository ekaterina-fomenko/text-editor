/*
package com.editor.parser;

import com.editor.model.TextEditorModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SyntaxParserTest {
    private List<StringBuilder> testText;
    private SyntaxParser syntaxParser;
    private TextEditorModel textEditorModel;
    private Set<String> jsReservedWordsSet;
    private Set<String> hsReservedWordsSet;
    private Set<String> erReservedWordsSet;


    @Before

    public void setUP() {
        syntaxParser = new SyntaxParser();
        textEditorModel = new TextEditorModel();
        jsReservedWordsSet = new HashSet<>();
        hsReservedWordsSet = new HashSet<>();
        erReservedWordsSet = new HashSet<>();

        testText = new ArrayList<>();
        testText.add(new StringBuilder("module test;"));
        testText.add(new StringBuilder("import package;"));
        testText.add(new StringBuilder());
        testText.add(new StringBuilder("public class Test {"));
        testText.add(new StringBuilder("public int test(int publica) {"));
        testText.add(new StringBuilder("if (publica > 2) return 2"));
        testText.add(new StringBuilder("orelse return 0"));
        testText.add(new StringBuilder("}"));
        testText.add(new StringBuilder("}"));

        textEditorModel.setLineBuildersFromFile(testText);


    }

    @Test
    public void testGetJsReservedWords() {
        SyntaxParser.setCurrentSyntax(SyntaxType.JAVASCRIPT);
        List<CommonSyntaxHighlight> jsReservedWordsResult = syntaxParser.getReservedWordsHighlightByIndexes(textEditorModel, 0, testText.size() - 1);
        List<CommonSyntaxHighlight> jsReservedWordsExpected = getExpectedJsSyntaxHighlight();
        int resultSize = jsReservedWordsResult.size();

        assertEquals(jsReservedWordsExpected.size(), resultSize);

        for (int i = 0; i < jsReservedWordsResult.size(); i++) {
            CommonSyntaxHighlight resultSyntax = jsReservedWordsResult.get(i);
            CommonSyntaxHighlight expectedSyntax = jsReservedWordsExpected.get(i);

            assertEquals(expectedSyntax.getRowIndex(), resultSyntax.getRowIndex());
            assertEquals(expectedSyntax.getStartIndex(), resultSyntax.getStartIndex());
            assertEquals(expectedSyntax.getEndIndex(), resultSyntax.getEndIndex());
            jsReservedWordsSet.contains(testText.get(resultSyntax.getRowIndex()).substring(resultSyntax.getStartIndex(), resultSyntax.getEndIndex()));
        }
    }

    @Test
    public void testGetHsReservedWords() {
        SyntaxParser.setCurrentSyntax(SyntaxType.HASKELL);
        List<CommonSyntaxHighlight> hsReservedWordsResult = syntaxParser.getReservedWordsHighlightByIndexes(textEditorModel, 0, testText.size() - 1);
        List<CommonSyntaxHighlight> hsReservedWordsExpected = getExpectedHsSyntaxHighlight();
        int resultSize = hsReservedWordsResult.size();

        assertEquals(hsReservedWordsExpected.size(), resultSize);

        for (int i = 0; i < hsReservedWordsResult.size(); i++) {
            CommonSyntaxHighlight resultSyntax = hsReservedWordsResult.get(i);
            CommonSyntaxHighlight expectedSyntax = hsReservedWordsExpected.get(i);

            assertEquals(expectedSyntax.getRowIndex(), resultSyntax.getRowIndex());
            assertEquals(expectedSyntax.getStartIndex(), resultSyntax.getStartIndex());
            assertEquals(expectedSyntax.getEndIndex(), resultSyntax.getEndIndex());
            hsReservedWordsSet.contains(testText.get(resultSyntax.getRowIndex()).substring(resultSyntax.getStartIndex(), resultSyntax.getEndIndex()));
        }
    }

    @Test
    public void testGetErReservedWords() {
        SyntaxParser.setCurrentSyntax(SyntaxType.ERLANG);
        List<CommonSyntaxHighlight> erReservedWordsResult = syntaxParser.getReservedWordsHighlightByIndexes(textEditorModel, 0, testText.size() - 1);
        List<CommonSyntaxHighlight> erReservedWordsExpected = getExpectedErSyntaxHighlight();
        int resultSize = erReservedWordsResult.size();

        assertEquals(erReservedWordsExpected.size(), resultSize);

        for (int i = 0; i < erReservedWordsResult.size(); i++) {
            CommonSyntaxHighlight resultSyntax = erReservedWordsResult.get(i);
            CommonSyntaxHighlight expectedSyntax = erReservedWordsExpected.get(i);

            assertEquals(expectedSyntax.getRowIndex(), resultSyntax.getRowIndex());
            assertEquals(expectedSyntax.getStartIndex(), resultSyntax.getStartIndex());
            assertEquals(expectedSyntax.getEndIndex(), resultSyntax.getEndIndex());
            erReservedWordsSet.contains(testText.get(resultSyntax.getRowIndex()).substring(resultSyntax.getStartIndex(), resultSyntax.getEndIndex()));
        }
    }

    public List<CommonSyntaxHighlight> getExpectedErSyntaxHighlight() {
        erReservedWordsSet.add("module");
        erReservedWordsSet.add("import");
        erReservedWordsSet.add("if");
        erReservedWordsSet.add("orelse");


        List<CommonSyntaxHighlight> list = new ArrayList<>();
        list.add(new CommonSyntaxHighlight(0, 0, 5));
        list.add(new CommonSyntaxHighlight(1, 0, 5));
        list.add(new CommonSyntaxHighlight(5, 0, 1));
        list.add(new CommonSyntaxHighlight(6, 0, 5));
        return list;
    }

    public List<CommonSyntaxHighlight> getExpectedHsSyntaxHighlight() {
        hsReservedWordsSet.add("import");
        hsReservedWordsSet.add("public");
        hsReservedWordsSet.add("class");
        hsReservedWordsSet.add("if");
        hsReservedWordsSet.add("return");


        List<CommonSyntaxHighlight> list = new ArrayList<>();
        list.add(new CommonSyntaxHighlight(0, 0, 5));
        list.add(new CommonSyntaxHighlight(1, 0, 5));
        list.add(new CommonSyntaxHighlight(3, 7, 11));
        list.add(new CommonSyntaxHighlight(5, 0, 1));
        list.add(new CommonSyntaxHighlight(5, 17, 22));
        list.add(new CommonSyntaxHighlight(6, 7, 12));
        return list;
    }

    public List<CommonSyntaxHighlight> getExpectedJsSyntaxHighlight() {
        jsReservedWordsSet.add("module");
        jsReservedWordsSet.add("import");
        jsReservedWordsSet.add("public");
        jsReservedWordsSet.add("class");
        jsReservedWordsSet.add("int");
        jsReservedWordsSet.add("if");
        jsReservedWordsSet.add("return");


        List<CommonSyntaxHighlight> list = new ArrayList<>();
        list.add(new CommonSyntaxHighlight(1, 0, 5));
        list.add(new CommonSyntaxHighlight(3, 0, 5));
        list.add(new CommonSyntaxHighlight(3, 7, 11));
        list.add(new CommonSyntaxHighlight(4, 0, 5));
        list.add(new CommonSyntaxHighlight(4, 7, 9));
        list.add(new CommonSyntaxHighlight(4, 16, 18));
        list.add(new CommonSyntaxHighlight(5, 0, 1));
        list.add(new CommonSyntaxHighlight(5, 17, 22));
        list.add(new CommonSyntaxHighlight(6, 7, 12));
        return list;
    }

}
*/
