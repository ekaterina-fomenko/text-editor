package com.editor;

import com.editor.model.RopeTextEditorModel;
import com.editor.syntax.keywords.PairedBracketsInfo;
import com.editor.syntax.keywords.SyntaxResolver;
import com.editor.syntax.keywords.TokenType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class RopeDrawComponentTest {
    private RopeDrawComponent ropeDrawComponent;
    private RopeTextEditorModel model;

    @Mock
    SyntaxResolver syntaxResolver;

    @Before
    public void setup() {
        EditorSettings editorSettings = new EditorSettings();
        model = new RopeTextEditorModel();
        ropeDrawComponent = new RopeDrawComponent(editorSettings, model);
    }

    @Test
    public void getReservedWordCharColorTest() {
        int currentIndex = 3;
        int currentVisibleIndex = 2;
        Map<Integer, TokenType> reservedWordsMap = new HashMap<>();
        reservedWordsMap.put(2, TokenType.RESERVED_WORD);
        PairedBracketsInfo pairedBracketsInfo = new PairedBracketsInfo(3);

        Color charColor = ropeDrawComponent.getCurrentCharColor(currentIndex, reservedWordsMap, pairedBracketsInfo, currentVisibleIndex);
        assertEquals(charColor, TokenType.RESERVED_WORD.getColor());
    }

    @Test
    public void getBracketCharColorTest() {
        int currentIndex = 3;
        int currentVisibleIndex = 2;
        Map<Integer, TokenType> reservedWordsMap = new HashMap<>();
        reservedWordsMap.put(3, TokenType.RESERVED_WORD);
        PairedBracketsInfo pairedBracketsInfo = new PairedBracketsInfo(3);

        Color charColor = ropeDrawComponent.getCurrentCharColor(currentIndex, reservedWordsMap, pairedBracketsInfo, currentVisibleIndex);
        assertEquals(charColor, TokenType.BRACKET.getColor());
    }

    @Test
    public void getDefaultCharColorTest() {
        int currentIndex = 3;
        int currentVisibleIndex = 2;
        Map<Integer, TokenType> reservedWordsMap = new HashMap<>();
        reservedWordsMap.put(1, TokenType.RESERVED_WORD);
        PairedBracketsInfo pairedBracketsInfo = new PairedBracketsInfo(0);

        Color charColor = ropeDrawComponent.getCurrentCharColor(currentIndex, reservedWordsMap, pairedBracketsInfo, currentVisibleIndex);
        assertEquals(charColor, TokenType.DEFAULT.getColor());
    }

    @Test
    public void getVisibleBracketsInfoTest() {
        model.setCursorPosition(1);
        Map<Integer, PairedBracketsInfo> bracketMap = new HashMap<>();

        PairedBracketsInfo pairedBracketsInfo = putPairedBracketsInfoToMap(bracketMap);

        doReturn(bracketMap).when(syntaxResolver).getBracketsIndexesMap();

        PairedBracketsInfo resultBracketsInfo = ropeDrawComponent.getVisibleBracketsInfo(syntaxResolver);

        assertEquals(resultBracketsInfo, pairedBracketsInfo);
    }

    @Test
    public void getVisibleBracketsEmptyInfoTest() {
        model.setCursorPosition(5);
        Map<Integer, PairedBracketsInfo> bracketMap = new HashMap<>();
        putPairedBracketsInfoToMap(bracketMap);

        doReturn(bracketMap).when(syntaxResolver).getBracketsIndexesMap();

        PairedBracketsInfo resultBracketsInfo = ropeDrawComponent.getVisibleBracketsInfo(syntaxResolver);

        assertEquals(-1, resultBracketsInfo.getStartInd());
        assertEquals(-1, resultBracketsInfo.getEndInd());
        assertEquals(TokenType.DEFAULT, resultBracketsInfo.getTokenType());
    }

    @Test
    public void getIndexOfVisibleEndTest() {
        String str = "Sunnyyy;)";
        model.append(str.toCharArray());
        int result = ropeDrawComponent.getIndexOfVisibleEnd(model.getRope(), 0);
        assertEquals(str.length(), result);
    }

    @Test
    public void getIndexOfVisibleEndTest2() {
        String str = "Omg! \nIt is sunnyyy;)";
        model.append(str.toCharArray());
        int result = ropeDrawComponent.getIndexOfVisibleEnd(model.getRope(), 0);
        assertEquals(str.indexOf('\n') + 1, result);
    }

    private PairedBracketsInfo putPairedBracketsInfoToMap(Map<Integer, PairedBracketsInfo> bracketMap) {
        PairedBracketsInfo pairedBracketsInfo = new PairedBracketsInfo(1);
        pairedBracketsInfo.setEndInd(3);
        bracketMap.put(1, pairedBracketsInfo);
        bracketMap.put(3, pairedBracketsInfo);
        return pairedBracketsInfo;
    }

}
