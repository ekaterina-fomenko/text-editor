package com.editor.syntax.keywords;

import com.editor.model.rope.Rope;
import com.editor.syntax.SyntaxSetter;
import com.editor.syntax.SyntaxType;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SyntaxScannerTrieTest {

    private SyntaxScannerTrie trie;
    public static final String COMMENTS_STRING = "oh, let's find our comment: //is it js? -- or hs % may be erl";

    @Before
    public void setup() {
        trie = new SyntaxScannerTrie();

        trie.registerReservedWord("i");
        trie.registerReservedWord("am");
        trie.registerReservedWord("happy");
    }


    @Test
    public void reservedWordsIndexesTest() {
        String ropeStr = "i am happy";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes(rope, 0);

        for (int i = 0; i < ropeStr.length(); i++) {
            if (ropeStr.charAt(i) == ' ') {
                i++;
            }
            assertTrue(resultMap.containsKey(i));
            assertTrue(resultMap.get(i) == TokenType.RESERVED_WORD);
            resultMap.remove(i);
        }

        assertTrue(resultMap.isEmpty());
    }

    @Test
    public void stringLiteralTest() {
        String ropeStr = "hi, i am a \" miracle \".";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        Map<Integer, TokenType> expectedResultMap = new HashMap<>();
        expectedResultMap.put(4, TokenType.RESERVED_WORD);
        expectedResultMap.put(6, TokenType.RESERVED_WORD);
        expectedResultMap.put(7, TokenType.RESERVED_WORD);
        expectedResultMap.put(11, TokenType.STRING);
        expectedResultMap.put(12, TokenType.STRING);
        expectedResultMap.put(13, TokenType.STRING);
        expectedResultMap.put(14, TokenType.STRING);
        expectedResultMap.put(15, TokenType.STRING);
        expectedResultMap.put(16, TokenType.STRING);
        expectedResultMap.put(17, TokenType.STRING);
        expectedResultMap.put(18, TokenType.STRING);
        expectedResultMap.put(19, TokenType.STRING);
        expectedResultMap.put(20, TokenType.STRING);

        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes(rope, 0);


        for (Integer key : expectedResultMap.keySet()) {
            assertEquals(expectedResultMap.get(key), resultMap.get(key));
        }
    }

    @Test
    public void openStringLiteralTest() {
        String ropeStr = "o, \"so shanie today...";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        Map<Integer, TokenType> expectedResultMap = new HashMap<>();
        for (int i = ropeStr.indexOf('\"'); i < ropeStr.length(); i++) {
            expectedResultMap.put(i, TokenType.STRING);
        }

        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes(rope, 0);


        for (Integer key : expectedResultMap.keySet()) {
            assertEquals(expectedResultMap.get(key), resultMap.get(key));
        }

    }

    @Test
    public void digitsTest() {
        String ropeStr = "1, 2,3 - I am going to find you!";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        Map<Integer, TokenType> expectedResultMap = new HashMap<>();
        expectedResultMap.put(0, TokenType.DIGIT);
        expectedResultMap.put(3, TokenType.DIGIT);
        expectedResultMap.put(5, TokenType.DIGIT);
        expectedResultMap.put(11, TokenType.RESERVED_WORD);
        expectedResultMap.put(12, TokenType.RESERVED_WORD);

        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes(rope, 0);

        for (Integer key : expectedResultMap.keySet()) {
            assertEquals(expectedResultMap.get(key), resultMap.get(key));
        }
    }

    @Test
    public void commentJsTest() {
        checkCommentIndexes("//", SyntaxType.JAVASCRIPT);
    }

    @Test
    public void commentHsTest() {
        checkCommentIndexes("--", SyntaxType.HASKELL);
    }

    @Test
    public void commentErTest() {
        checkCommentIndexes("%", SyntaxType.ERLANG);

    }

    @Test
    public void symbolsTest() {
        String ropeStr = "< ii \r\n ooops \t + (we are the champions) ***";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes(rope, 0);
        assertTrue(resultMap.isEmpty());
    }

    @Test
    public void pairedBracketsTest() {
        String ropeStr = "In the {magic wood} lives} one {little-little girl...";

        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        Map<Integer, TokenType> keywordsIndexes = trie.getKeywordsIndexes(rope, 0);
        Map<Integer, PairedBracketsInfo> bracketsMap = trie.getBracketsIndexesMap();

        assertEquals(2, bracketsMap.size());
        assertEquals(7, bracketsMap.get(7).getStartInd());
        assertEquals(7, bracketsMap.get(18).getStartInd());
        assertEquals(18, bracketsMap.get(7).getEndInd());
        assertEquals(18, bracketsMap.get(18).getEndInd());

    }

    @Test
    public void testSyntaxTest() {
        SyntaxSetter.setCurrentSyntax(SyntaxType.TEXT);
        SyntaxScannerTrie keywordsTree = KeywordsTrie.getCurrentSyntaxTrie();
        assertTrue(keywordsTree.isEmpty());

        Rope rope = new Rope();
        rope = rope.append(COMMENTS_STRING);
        Map<Integer, TokenType> keywordsIndexes = trie.getKeywordsIndexes(rope, 0);

        assertTrue(keywordsIndexes.isEmpty());
    }

    private void checkCommentIndexes(String commentSymbol, SyntaxType syntaxType) {
        Rope rope = new Rope();
        rope = rope.append(COMMENTS_STRING);

        SyntaxSetter.setCurrentSyntax(syntaxType);

        Map<Integer, TokenType> expectedResultMap = new HashMap<>();

        for (int indexOfComment = COMMENTS_STRING.indexOf(commentSymbol); indexOfComment < COMMENTS_STRING.length(); indexOfComment++) {
            expectedResultMap.put(indexOfComment, TokenType.COMMENT);
        }

        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes(rope, 0);

        for (Integer key : expectedResultMap.keySet()) {
            assertEquals(expectedResultMap.get(key), resultMap.get(key));
        }
    }
}
