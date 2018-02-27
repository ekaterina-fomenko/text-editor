package com.editor.syntax.keywords;

import com.editor.model.rope.Rope;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class SyntaxScannerTrieTest {


    @Test
    public void test() {
        SyntaxScannerTrie trie = new SyntaxScannerTrie();

        trie.registerReservedWord("i");
        trie.registerReservedWord("am");
        trie.registerReservedWord("happy");

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
}
