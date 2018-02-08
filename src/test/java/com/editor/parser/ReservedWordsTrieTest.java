package com.editor.parser;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ReservedWordsTrieTest {
    
    @Test
    public void putTest(){
        ReservedWordsTrie trie = new ReservedWordsTrie();
        trie.put("hello");
        trie.put("house");
        trie.put("holly");
        trie.put("world");

        assertTrue(trie.find("hello"));
        assertTrue(trie.find("house"));
        assertTrue(trie.find("holly"));
        assertTrue(trie.find("world"));
        assertFalse(trie.find("hell"));
        assertFalse(trie.find("orld"));
        assertFalse(trie.find("us"));
    }
}
