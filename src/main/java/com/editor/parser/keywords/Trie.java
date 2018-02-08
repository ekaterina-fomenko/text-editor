package com.editor.parser.keywords;

import java.util.Map;
import java.util.TreeMap;

/**
 * Prefix Tree for constructing reserved words
 */
public class Trie {

    static class TrieNode {
        Map<Character, TrieNode> children = new TreeMap<>();
        boolean isLeaf;
    }

    TrieNode root = new TrieNode();

    public void put(String string) {
        TrieNode node = root;
        for (char ch : string.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }
        node.isLeaf = true;
    }

    public boolean find(String string) {
        TrieNode node = root;
        for (char ch : string.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return false;
            } else {
                node = node.children.get(ch);
            }
        }
        if (node.isLeaf) {
            return true;
        }
        return false;
    }
}
