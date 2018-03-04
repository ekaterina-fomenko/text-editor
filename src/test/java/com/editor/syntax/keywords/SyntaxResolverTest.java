package com.editor.syntax.keywords;

import com.editor.model.rope.Rope;
import com.editor.syntax.SyntaxType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SyntaxResolverTest {

    private SyntaxResolver trie;
    public static final String COMMENTS_STRING = "oh, let's find our comment: //is it js? -- or hs % may be erl";

    @Before
    public void setup() {
        trie = new SyntaxResolver(Arrays.asList("i","am","happy"));
    }

    @Test
    public void testReservedWordsIndexes() {
        String ropeStr = "i am happy";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        trie.calculateTokens(rope, 0);
        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes();

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
    public void testStringLiteral() {
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

        trie.calculateTokens(rope, 0);
        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes();


        for (Integer key : expectedResultMap.keySet()) {
            assertEquals(expectedResultMap.get(key), resultMap.get(key));
        }
    }

    @Test
    public void testOpenStringLiteral() {
        String ropeStr = "o, \"so shanie today...";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        Map<Integer, TokenType> expectedResultMap = new HashMap<>();
        for (int i = ropeStr.indexOf('\"'); i < ropeStr.length(); i++) {
            expectedResultMap.put(i, TokenType.STRING);
        }

        trie.calculateTokens(rope, 0);
        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes();


        for (Integer key : expectedResultMap.keySet()) {
            assertEquals(expectedResultMap.get(key), resultMap.get(key));
        }

    }

    @Test
    public void testDigits() {
        String ropeStr = "1, 2,3 - I am going to find you!";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        Map<Integer, TokenType> expectedResultMap = new HashMap<>();
        expectedResultMap.put(0, TokenType.DIGIT);
        expectedResultMap.put(3, TokenType.DIGIT);
        expectedResultMap.put(5, TokenType.DIGIT);
        expectedResultMap.put(11, TokenType.RESERVED_WORD);
        expectedResultMap.put(12, TokenType.RESERVED_WORD);

        trie.calculateTokens(rope, 0);
        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes();

        for (Integer key : expectedResultMap.keySet()) {
            assertEquals(expectedResultMap.get(key), resultMap.get(key));
        }
    }

    @Test
    public void testCommentJs() {
        checkCommentIndexes("//", SyntaxType.JAVASCRIPT);
    }

    @Test
    public void testCommentHs() {
        checkCommentIndexes("--", SyntaxType.HASKELL);
    }

    @Test
    public void testCommentEr() {
        checkCommentIndexes("%", SyntaxType.ERLANG);
    }

    @Test
    public void testSymbols() {
        String ropeStr = "< ii \r\n ooops \t + (we are the champions) ***";
        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        trie.calculateTokens(rope, 0);
        Map<Integer, TokenType> resultMap = trie.getKeywordsIndexes();
        assertTrue(resultMap.isEmpty());
    }

    @Test
    public void testPairedBrackets() {
        String ropeStr = "In the {magic wood} lives} one {little-little girl...";

        Rope rope = new Rope();
        rope = rope.append(ropeStr);

        trie.calculateTokens(rope, 0);
        Map<Integer, PairedBracketsInfo> bracketsMap = trie.getBracketsIndexesMap();

        assertEquals(2, bracketsMap.size());
        assertEquals(7, bracketsMap.get(7).getStartInd());
        assertEquals(7, bracketsMap.get(18).getStartInd());
        assertEquals(18, bracketsMap.get(7).getEndInd());
        assertEquals(18, bracketsMap.get(18).getEndInd());

    }

    @Test
    public void testSyntax() {
        SyntaxResolver keywordsTree = new SyntaxResolver(SyntaxType.TEXT);
        assertTrue(keywordsTree.isEmpty());

        Rope rope = new Rope();
        rope = rope.append(COMMENTS_STRING);
        trie.calculateTokens(rope, 0);
        Map<Integer, TokenType> keywordsIndexes = trie.getKeywordsIndexes();

        assertTrue(keywordsIndexes.isEmpty());
    }

    private void checkCommentIndexes(String commentSymbol, SyntaxType syntaxType) {
        Rope rope = new Rope();
        SyntaxResolver resolver = new SyntaxResolver(syntaxType);
        rope = rope.append(COMMENTS_STRING);

        Map<Integer, TokenType> expectedResultMap = new HashMap<>();

        for (int indexOfComment = COMMENTS_STRING.indexOf(commentSymbol); indexOfComment < COMMENTS_STRING.length(); indexOfComment++) {
            expectedResultMap.put(indexOfComment, TokenType.COMMENT);
        }

        resolver.calculateTokens(rope, 0);
        Map<Integer, TokenType> resultMap = resolver.getKeywordsIndexes();

        for (Integer key : expectedResultMap.keySet()) {
            assertEquals(expectedResultMap.get(key), resultMap.get(key));
        }
    }
}
