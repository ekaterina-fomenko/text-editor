package com.editor.parser.keywords;

import com.editor.parser.keywords.Trie;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ReservedWordsTrieTest {

    @Test
    public void putTest(){
        Trie trie = new Trie();
        trie.put("hello");
        trie.put("house");
        trie.put("holly");
        trie.put("world");
        trie.put("or");

        assertTrue(trie.find("hello"));
        assertTrue(trie.find("house"));
        assertTrue(trie.find("holly"));
        assertTrue(trie.find("world"));
        assertTrue(trie.find("or"));

        assertFalse(trie.find("hell"));
        assertFalse(trie.find("orld"));
        assertFalse(trie.find("us"));
        assertFalse(trie.find("ll"));
    }
}
